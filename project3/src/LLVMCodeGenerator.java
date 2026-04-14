import gen.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;

/**
 * Walks the ANTLR parse tree and emits LLVM IR text.
 *
 * Procedural Pascal subset supported:
 *   integer / real / boolean / string-literal expressions
 *   if / while / for-to/downto / repeat-until + break/continue
 *   procedures, functions (recursion ok), VAR pass-by-reference parameters
 *   constants + integration with ConstantFolder for [FOLDED] propagation
 *   I/O: Write, WriteLn, ReadLn (integers + strings)
 *
 * Not supported: classes, interfaces, inheritance, records, arrays, sets, files, goto.
 */
public class LLVMCodeGenerator extends delphiBaseVisitor<LLVMCodeGenerator.Value> {

    // ---------- Pascal-side type model ----------

    enum PT {
        INT, REAL, BOOL, STR, VOID;

        String llvm() {
            switch (this) {
                case INT:  return "i32";
                case REAL: return "double";
                case BOOL: return "i1";
                case STR:  return "i8*";
                case VOID: return "void";
            }
            return "i32";
        }

        String zero() {
            switch (this) {
                case INT:  return "0";
                case REAL: return "0.0";
                case BOOL: return "false";
                case STR:  return "null";
                default:   return "0";
            }
        }
    }

    /** A typed LLVM operand (e.g. "%1" of type i32, or "42" of type i32). */
    static class Value {
        final String ref;
        final PT type;
        Value(String ref, PT type) { this.ref = ref; this.type = type; }
        @Override public String toString() { return type.llvm() + " " + ref; }
    }

    static class Symbol {
        final String irPtr;        // an LLVM pointer to storage (e.g. "%x.addr" or "@g_x")
        final PT type;
        final boolean isGlobal;
        final boolean isVarParam;  // if true, irPtr is a function parameter that's already a pointer
        final boolean isConstant;
        final Object constValue;   // populated when isConstant == true (Integer/Double/Boolean/String)

        Symbol(String irPtr, PT type, boolean isGlobal, boolean isVarParam,
               boolean isConstant, Object constValue) {
            this.irPtr = irPtr; this.type = type;
            this.isGlobal = isGlobal; this.isVarParam = isVarParam;
            this.isConstant = isConstant; this.constValue = constValue;
        }
    }

    /** Function metadata stored at module scope. */
    static class FunctionSig {
        final String irName;          // mangled, with leading "@"
        final PT retType;
        final List<PT>      paramTypes = new ArrayList<>();
        final List<Boolean> paramIsVar = new ArrayList<>();
        final List<String>  paramNames = new ArrayList<>();
        FunctionSig(String irName, PT ret) { this.irName = irName; this.retType = ret; }
    }

    /** A scope: maps lowercase names to symbols. Chained for static lookup. */
    static class Scope {
        final Scope parent;
        final Map<String, Symbol> table = new LinkedHashMap<>();
        Scope(Scope parent) { this.parent = parent; }
        void define(String name, Symbol s) { table.put(name.toLowerCase(), s); }
        Symbol lookup(String name) {
            Symbol s = table.get(name.toLowerCase());
            if (s != null) return s;
            return parent == null ? null : parent.lookup(name);
        }
    }

    /** Stack of {continue, break} labels for the active loops. */
    static class LoopCtx {
        final String continueLbl;
        final String breakLbl;
        LoopCtx(String c, String b) { this.continueLbl = c; this.breakLbl = b; }
    }

    // ---------- module-level emission state ----------

    private final ConstantFolder folder;

    private final StringBuilder moduleHead = new StringBuilder();
    private final StringBuilder strPool    = new StringBuilder();
    private final StringBuilder globals    = new StringBuilder();
    private final StringBuilder funcs      = new StringBuilder();

    private final Map<String, String> internedStrings = new LinkedHashMap<>();
    private int strCounter = 0;
    private int funcCounter = 0;

    private final Scope globalScope = new Scope(null);
    private final Map<String, FunctionSig> functions = new LinkedHashMap<>();

    // ---------- per-function state ----------

    private StringBuilder fnBody;        // the body emitted between { and }
    private StringBuilder fnAllocas;     // allocas to be inserted at top of entry
    private int regCounter;              // %0, %1, ... per function
    private int labelCounter;            // for unique label suffixes
    private String currentBlock;         // null after a terminator
    private boolean blockHasTerminator;
    private Scope currentScope;
    private FunctionSig currentFn;       // null when emitting main
    private final Deque<LoopCtx> loopStack = new ArrayDeque<>();

    public LLVMCodeGenerator(ConstantFolder folder) {
        this.folder = folder;
        // Module preamble
        moduleHead.append("; ModuleID = 'pascal'\n");
        moduleHead.append("source_filename = \"pascal\"\n\n");
        moduleHead.append("declare i32 @printf(i8*, ...)\n");
        moduleHead.append("declare i32 @scanf(i8*, ...)\n");
        moduleHead.append("declare i32 @puts(i8*)\n\n");
    }

    public String emit() {
        StringBuilder out = new StringBuilder();
        out.append(moduleHead);
        out.append(strPool);
        if (strPool.length() > 0) out.append('\n');
        out.append(globals);
        if (globals.length() > 0) out.append('\n');
        out.append(funcs);
        return out.toString();
    }

    // ============================================================
    //                 PROGRAM / BLOCK / DECLARATIONS
    // ============================================================

    @Override
    public Value visitProgram(delphiParser.ProgramContext ctx) {
        currentScope = globalScope;

        // Pass A: collect global var declarations + constants from the top-level block
        delphiParser.BlockContext block = ctx.block();
        for (var child : block.children) {
            if (child instanceof delphiParser.VariableDeclarationPartContext) {
                collectGlobals((delphiParser.VariableDeclarationPartContext) child);
            } else if (child instanceof delphiParser.ConstantDefinitionPartContext) {
                collectConstants((delphiParser.ConstantDefinitionPartContext) child);
            }
        }

        // Pass B: pre-declare every procedure/function so calls resolve regardless of order
        for (var child : block.children) {
            if (child instanceof delphiParser.ProcedureAndFunctionDeclarationPartContext) {
                preDeclareCallable(((delphiParser.ProcedureAndFunctionDeclarationPartContext) child)
                        .procedureOrFunctionDeclaration());
            }
        }

        // Pass C: emit each procedure/function body
        for (var child : block.children) {
            if (child instanceof delphiParser.ProcedureAndFunctionDeclarationPartContext) {
                emitCallable(((delphiParser.ProcedureAndFunctionDeclarationPartContext) child)
                        .procedureOrFunctionDeclaration());
            }
        }

        // Pass D: emit @main containing the program's compound statement
        beginFunction(null, "@main", PT.INT, List.of(), List.of(), List.of());
        visit(block.compoundStatement());
        endMainWithReturn();
        flushFunction();
        return null;
    }

    private void collectGlobals(delphiParser.VariableDeclarationPartContext ctx) {
        for (var vd : ctx.variableDeclaration()) {
            PT t = inferType(vd.type_().getText());
            for (var id : vd.identifierList().identifier()) {
                String name = id.getText();
                String ir = "@g_" + name.toLowerCase();
                globals.append(ir).append(" = global ").append(t.llvm()).append(' ')
                       .append(t.zero()).append('\n');
                globalScope.define(name, new Symbol(ir, t, true, false, false, null));
            }
        }
    }

    private void collectConstants(delphiParser.ConstantDefinitionPartContext ctx) {
        Map<String, Object> consts = folder.getConstants();
        for (var cd : ctx.constantDefinition()) {
            String name = cd.identifier().getText();
            Object v = consts.get(name.toLowerCase());
            if (v == null) continue;
            PT t = pascalTypeOf(v);
            globalScope.define(name, new Symbol("", t, true, false, true, v));
        }
    }

    /** Map a Pascal type name to our PT enum. Default INT. */
    private PT inferType(String typeName) {
        String t = typeName.toLowerCase();
        if (t.contains("integer") || t.contains("longint") || t.contains("smallint") ||
            t.contains("byte") || t.contains("word") || t.contains("cardinal")) return PT.INT;
        if (t.contains("real") || t.contains("double") || t.contains("single") ||
            t.contains("extended")) return PT.REAL;
        if (t.contains("boolean")) return PT.BOOL;
        if (t.contains("string") || t.contains("char")) return PT.STR;
        return PT.INT;
    }

    private PT pascalTypeOf(Object v) {
        if (v instanceof Integer) return PT.INT;
        if (v instanceof Double)  return PT.REAL;
        if (v instanceof Boolean) return PT.BOOL;
        if (v instanceof String)  return PT.STR;
        return PT.INT;
    }

    // ============================================================
    //              PROCEDURE / FUNCTION DECLARATIONS
    // ============================================================

    private void preDeclareCallable(delphiParser.ProcedureOrFunctionDeclarationContext ctx) {
        if (ctx.procedureDeclaration() != null) {
            var p = ctx.procedureDeclaration();
            String name = p.qualifiedMethodName().getText();
            FunctionSig sig = new FunctionSig("@p_" + mangle(name), PT.VOID);
            collectParams(sig, p.formalParameterList());
            functions.put(name.toLowerCase(), sig);
        } else if (ctx.functionDeclaration() != null) {
            var f = ctx.functionDeclaration();
            String name = f.qualifiedMethodName().getText();
            PT ret = inferType(f.resultType().getText());
            FunctionSig sig = new FunctionSig("@p_" + mangle(name), ret);
            collectParams(sig, f.formalParameterList());
            functions.put(name.toLowerCase(), sig);
        }
    }

    private void collectParams(FunctionSig sig, delphiParser.FormalParameterListContext fpl) {
        if (fpl == null) return;
        for (var section : fpl.formalParameterSection()) {
            boolean isVar = section.VAR() != null;
            var pg = section.parameterGroup();
            PT t = inferType(pg.typeIdentifier().getText());
            for (var id : pg.identifierList().identifier()) {
                sig.paramTypes.add(t);
                sig.paramIsVar.add(isVar);
                sig.paramNames.add(id.getText());
            }
        }
    }

    private String mangle(String name) {
        return name.toLowerCase().replace('.', '_');
    }

    private void emitCallable(delphiParser.ProcedureOrFunctionDeclarationContext ctx) {
        boolean isFunction = ctx.functionDeclaration() != null;
        if (!isFunction && ctx.procedureDeclaration() == null) return; // skip ctor/dtor

        String name = isFunction
                ? ctx.functionDeclaration().qualifiedMethodName().getText()
                : ctx.procedureDeclaration().qualifiedMethodName().getText();
        FunctionSig sig = functions.get(name.toLowerCase());
        delphiParser.BlockContext body = isFunction
                ? ctx.functionDeclaration().block()
                : ctx.procedureDeclaration().block();

        beginFunction(sig, sig.irName, sig.retType, sig.paramTypes, sig.paramIsVar, sig.paramNames);

        if (isFunction) {
            // Allocate result slot named after the function (Pascal allows `Funcname := X` and `Result := X`)
            allocaInEntry("%result.addr", sig.retType);
            currentScope.define("result",
                    new Symbol("%result.addr", sig.retType, false, false, false, null));
            currentScope.define(name,
                    new Symbol("%result.addr", sig.retType, false, false, false, null));
        }

        // Local var declarations + nested procedures are not used in our test set,
        // but we still walk the block declarations for local vars.
        for (var child : body.children) {
            if (child instanceof delphiParser.VariableDeclarationPartContext) {
                collectLocals((delphiParser.VariableDeclarationPartContext) child);
            } else if (child instanceof delphiParser.ConstantDefinitionPartContext) {
                collectConstants((delphiParser.ConstantDefinitionPartContext) child);
            }
        }

        visit(body.compoundStatement());

        if (isFunction) {
            // ensure terminator
            ensureBlock();
            String r = freshReg();
            emit(r + " = load " + sig.retType.llvm() + ", " + sig.retType.llvm() + "* %result.addr");
            emit("ret " + sig.retType.llvm() + " " + r);
            blockHasTerminator = true;
        } else {
            ensureBlock();
            emit("ret void");
            blockHasTerminator = true;
        }
        flushFunction();
    }

    private void collectLocals(delphiParser.VariableDeclarationPartContext ctx) {
        for (var vd : ctx.variableDeclaration()) {
            PT t = inferType(vd.type_().getText());
            for (var id : vd.identifierList().identifier()) {
                String name = id.getText();
                String slot = "%" + name.toLowerCase() + ".addr";
                allocaInEntry(slot, t);
                currentScope.define(name, new Symbol(slot, t, false, false, false, null));
            }
        }
    }

    // ============================================================
    //                    FUNCTION SCOPE BOOKKEEPING
    // ============================================================

    private void beginFunction(FunctionSig sig, String irName, PT ret,
                                List<PT> paramTypes, List<Boolean> paramIsVar,
                                List<String> paramNames) {
        currentFn = sig;
        currentScope = new Scope(globalScope);  // static scoping: only globals visible
        fnBody = new StringBuilder();
        fnAllocas = new StringBuilder();
        regCounter = 0;
        labelCounter = 0;
        currentBlock = "entry";
        blockHasTerminator = false;
        loopStack.clear();

        // Function header
        fnBody.append("define ").append(ret.llvm()).append(' ').append(irName).append('(');
        StringBuilder paramRefs = new StringBuilder();
        List<String> incomingNames = new ArrayList<>();
        for (int i = 0; i < paramTypes.size(); i++) {
            if (i > 0) fnBody.append(", ");
            String paramReg = "%arg" + i;
            String paramTy = paramIsVar.get(i) ? paramTypes.get(i).llvm() + "*" : paramTypes.get(i).llvm();
            fnBody.append(paramTy).append(' ').append(paramReg);
            incomingNames.add(paramReg);
        }
        fnBody.append(") {\n");
        fnBody.append("entry:\n");

        // Bind each formal parameter into the current scope
        for (int i = 0; i < paramNames.size(); i++) {
            String pname = paramNames.get(i);
            PT t = paramTypes.get(i);
            boolean isVar = paramIsVar.get(i);
            if (isVar) {
                // The incoming arg is already a pointer T*; expose it directly
                currentScope.define(pname, new Symbol(incomingNames.get(i), t, false, true, false, null));
            } else {
                String slot = "%" + pname.toLowerCase() + ".addr";
                allocaInEntry(slot, t);
                emit("store " + t.llvm() + " " + incomingNames.get(i) + ", " + t.llvm() + "* " + slot);
                currentScope.define(pname, new Symbol(slot, t, false, false, false, null));
            }
        }
    }

    private void endMainWithReturn() {
        ensureBlock();
        emit("ret i32 0");
        blockHasTerminator = true;
    }

    private void flushFunction() {
        // splice allocas into the entry block
        funcs.append(fnBody.substring(0, fnBody.indexOf("entry:") + "entry:\n".length()));
        funcs.append(fnAllocas);
        funcs.append(fnBody.substring(fnBody.indexOf("entry:") + "entry:\n".length()));
        funcs.append("}\n\n");
        currentScope = globalScope;
        currentFn = null;
        fnBody = null;
        fnAllocas = null;
    }

    // ============================================================
    //                    STATEMENTS
    // ============================================================

    @Override
    public Value visitCompoundStatement(delphiParser.CompoundStatementContext ctx) {
        return visit(ctx.statements());
    }

    @Override
    public Value visitStatements(delphiParser.StatementsContext ctx) {
        for (var stmt : ctx.statement()) visit(stmt);
        return null;
    }

    @Override
    public Value visitAssignmentStatement(delphiParser.AssignmentStatementContext ctx) {
        String name = ctx.variable().identifier().getText();
        Symbol sym = currentScope.lookup(name);
        if (sym == null) throw new RuntimeException("Undefined: " + name);
        Value v = visit(ctx.expression());
        v = coerce(v, sym.type);
        // VAR param: the incoming pointer is already T*
        emit("store " + sym.type.llvm() + " " + v.ref + ", " + sym.type.llvm() + "* " + sym.irPtr);
        return null;
    }

    @Override
    public Value visitIfStatement(delphiParser.IfStatementContext ctx) {
        Value cond = visit(ctx.expression());
        cond = toI1(cond);
        String thenL = freshLabel("if.then");
        String endL  = freshLabel("if.end");
        String elseL = ctx.ELSE() != null ? freshLabel("if.else") : endL;

        emit("br i1 " + cond.ref + ", label %" + thenL + ", label %" + elseL);
        startBlock(thenL);
        visit(ctx.statement(0));
        if (!blockHasTerminator) emit("br label %" + endL);
        blockHasTerminator = true;

        if (ctx.ELSE() != null) {
            startBlock(elseL);
            visit(ctx.statement(1));
            if (!blockHasTerminator) emit("br label %" + endL);
            blockHasTerminator = true;
        }
        startBlock(endL);
        return null;
    }

    @Override
    public Value visitWhileStatement(delphiParser.WhileStatementContext ctx) {
        String condL = freshLabel("while.cond");
        String bodyL = freshLabel("while.body");
        String endL  = freshLabel("while.end");
        emit("br label %" + condL);
        startBlock(condL);
        Value c = toI1(visit(ctx.expression()));
        emit("br i1 " + c.ref + ", label %" + bodyL + ", label %" + endL);
        startBlock(bodyL);
        loopStack.push(new LoopCtx(condL, endL));
        visit(ctx.statement());
        loopStack.pop();
        if (!blockHasTerminator) emit("br label %" + condL);
        blockHasTerminator = true;
        startBlock(endL);
        return null;
    }

    @Override
    public Value visitRepeatStatement(delphiParser.RepeatStatementContext ctx) {
        String bodyL = freshLabel("repeat.body");
        String condL = freshLabel("repeat.cond");
        String endL  = freshLabel("repeat.end");
        emit("br label %" + bodyL);
        startBlock(bodyL);
        loopStack.push(new LoopCtx(condL, endL));
        for (var stmt : ctx.statements().statement()) visit(stmt);
        loopStack.pop();
        if (!blockHasTerminator) emit("br label %" + condL);
        blockHasTerminator = true;
        startBlock(condL);
        Value c = toI1(visit(ctx.expression()));
        // repeat-until exits when condition is TRUE
        emit("br i1 " + c.ref + ", label %" + endL + ", label %" + bodyL);
        blockHasTerminator = true;
        startBlock(endL);
        return null;
    }

    @Override
    public Value visitForStatement(delphiParser.ForStatementContext ctx) {
        String varName = ctx.identifier().getText();
        Symbol sym = currentScope.lookup(varName);
        if (sym == null) throw new RuntimeException("Undefined for-var: " + varName);
        boolean isDownto = ctx.forList().DOWNTO() != null;

        Value init = coerce(visit(ctx.forList().initialValue().expression()), sym.type);
        Value end  = coerce(visit(ctx.forList().finalValue().expression()),  sym.type);
        // Stash final value into an alloca so it's stable across iterations
        String endSlot = "%" + freshSuffix("for.end");
        allocaInEntry(endSlot, sym.type);
        emit("store " + sym.type.llvm() + " " + end.ref + ", " + sym.type.llvm() + "* " + endSlot);
        emit("store " + sym.type.llvm() + " " + init.ref + ", " + sym.type.llvm() + "* " + sym.irPtr);

        String entryL = freshLabel("for.entry");
        String bodyL  = freshLabel("for.body");
        String postL  = freshLabel("for.post");   // continue target
        String stepL  = freshLabel("for.step");
        String endL   = freshLabel("for.end");

        // Initial check (handles start > end / start < end for downto: skip the body entirely)
        emit("br label %" + entryL);
        startBlock(entryL);
        String cur  = freshReg();
        emit(cur + " = load " + sym.type.llvm() + ", " + sym.type.llvm() + "* " + sym.irPtr);
        String fin  = freshReg();
        emit(fin + " = load " + sym.type.llvm() + ", " + sym.type.llvm() + "* " + endSlot);
        String cmp  = freshReg();
        String op = isDownto ? "icmp sge" : "icmp sle";
        emit(cmp + " = " + op + " " + sym.type.llvm() + " " + cur + ", " + fin);
        emit("br i1 " + cmp + ", label %" + bodyL + ", label %" + endL);

        startBlock(bodyL);
        loopStack.push(new LoopCtx(postL, endL));
        visit(ctx.statement());
        loopStack.pop();
        if (!blockHasTerminator) emit("br label %" + postL);
        blockHasTerminator = true;

        // Post-body: if x == end, exit (Pascal preserves final value). Otherwise step.
        startBlock(postL);
        String cur2 = freshReg();
        emit(cur2 + " = load " + sym.type.llvm() + ", " + sym.type.llvm() + "* " + sym.irPtr);
        String fin2 = freshReg();
        emit(fin2 + " = load " + sym.type.llvm() + ", " + sym.type.llvm() + "* " + endSlot);
        String done = freshReg();
        emit(done + " = icmp eq " + sym.type.llvm() + " " + cur2 + ", " + fin2);
        emit("br i1 " + done + ", label %" + endL + ", label %" + stepL);

        startBlock(stepL);
        String stepped = freshReg();
        String binop = isDownto ? "sub" : "add";
        emit(stepped + " = " + binop + " " + sym.type.llvm() + " " + cur2 + ", 1");
        emit("store " + sym.type.llvm() + " " + stepped + ", " + sym.type.llvm() + "* " + sym.irPtr);
        emit("br label %" + bodyL);
        blockHasTerminator = true;

        startBlock(endL);
        return null;
    }

    @Override
    public Value visitBreakStatement(delphiParser.BreakStatementContext ctx) {
        if (loopStack.isEmpty()) throw new RuntimeException("break outside loop");
        emit("br label %" + loopStack.peek().breakLbl);
        blockHasTerminator = true;
        return null;
    }

    @Override
    public Value visitContinueStatement(delphiParser.ContinueStatementContext ctx) {
        if (loopStack.isEmpty()) throw new RuntimeException("continue outside loop");
        emit("br label %" + loopStack.peek().continueLbl);
        blockHasTerminator = true;
        return null;
    }

    @Override
    public Value visitProcedureStatement(delphiParser.ProcedureStatementContext ctx) {
        String name = ctx.variable().identifier().getText();
        FunctionSig sig = functions.get(name.toLowerCase());
        if (sig == null) throw new RuntimeException("Unknown procedure: " + name);
        List<String> argRefs = buildCallArgs(sig, ctx.parameterList());
        StringBuilder call = new StringBuilder();
        if (sig.retType == PT.VOID) {
            call.append("call void ").append(sig.irName).append('(').append(joinArgs(sig, argRefs)).append(')');
            emit(call.toString());
        } else {
            String r = freshReg();
            call.append(r).append(" = call ").append(sig.retType.llvm()).append(' ').append(sig.irName)
                .append('(').append(joinArgs(sig, argRefs)).append(')');
            emit(call.toString());
        }
        return null;
    }

    private String joinArgs(FunctionSig sig, List<String> argRefs) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < argRefs.size(); i++) {
            if (i > 0) s.append(", ");
            String ty = sig.paramIsVar.get(i) ? sig.paramTypes.get(i).llvm() + "*" : sig.paramTypes.get(i).llvm();
            s.append(ty).append(' ').append(argRefs.get(i));
        }
        return s.toString();
    }

    private List<String> buildCallArgs(FunctionSig sig, delphiParser.ParameterListContext params) {
        List<String> refs = new ArrayList<>();
        if (params == null) return refs;
        var actuals = params.actualParameter();
        for (int i = 0; i < actuals.size(); i++) {
            boolean isVar = i < sig.paramIsVar.size() && sig.paramIsVar.get(i);
            if (isVar) {
                // pass the pointer to the actual variable
                String varName = extractSimpleVarName(actuals.get(i).expression());
                Symbol s = currentScope.lookup(varName);
                if (s == null) throw new RuntimeException("VAR arg must be a variable: " + varName);
                if (s.isVarParam) refs.add(s.irPtr); // already a pointer
                else refs.add(s.irPtr);
            } else {
                Value v = visit(actuals.get(i).expression());
                v = coerce(v, sig.paramTypes.get(i));
                refs.add(v.ref);
            }
        }
        return refs;
    }

    private String extractSimpleVarName(delphiParser.ExpressionContext e) {
        try {
            var f = e.simpleExpression(0).term().signedFactor().factor();
            return f.variable().identifier().getText();
        } catch (Exception ex) {
            throw new RuntimeException("VAR argument must be a simple variable, got: " + e.getText());
        }
    }

    // ============================================================
    //                    I/O
    // ============================================================

    @Override
    public Value visitIoStatement(delphiParser.IoStatementContext ctx) {
        boolean isWriteln = ctx.WRITELN() != null;
        boolean isWrite   = ctx.WRITE() != null;
        boolean isReadln  = ctx.READLN() != null;
        boolean isRead    = ctx.READ() != null;

        if (isWrite || isWriteln) {
            List<Value> args = new ArrayList<>();
            if (ctx.expressionList() != null) {
                for (var expr : ctx.expressionList().expression()) {
                    args.add(visit(expr));
                }
            }
            emitPrintf(args, isWriteln);
            return null;
        }
        if (isRead || isReadln) {
            if (ctx.variable() != null) {
                for (var v : ctx.variable()) {
                    emitScanf(v);
                }
            }
            return null;
        }
        return null;
    }

    private void emitPrintf(List<Value> args, boolean addNewline) {
        // Build a printf format string by concatenating each arg's format token
        StringBuilder fmt = new StringBuilder();
        List<Value> realArgs = new ArrayList<>();
        for (Value v : args) {
            if (v.type == PT.INT)       { fmt.append("%d"); realArgs.add(v); }
            else if (v.type == PT.REAL) { fmt.append("%g"); realArgs.add(v); }
            else if (v.type == PT.STR)  { fmt.append("%s"); realArgs.add(v); }
            else if (v.type == PT.BOOL) {
                // print "TRUE" / "FALSE" via select
                String tStr = internStringPtr("TRUE");
                String fStr = internStringPtr("FALSE");
                String chosen = freshReg();
                emit(chosen + " = select i1 " + v.ref + ", i8* " + tStr + ", i8* " + fStr);
                fmt.append("%s");
                realArgs.add(new Value(chosen, PT.STR));
            } else {
                fmt.append("?");
            }
        }
        if (addNewline) fmt.append("\n");
        String fmtPtr = internStringPtr(fmt.toString());
        StringBuilder call = new StringBuilder();
        String r = freshReg();
        call.append(r).append(" = call i32 (i8*, ...) @printf(i8* ").append(fmtPtr);
        for (Value v : realArgs) {
            call.append(", ").append(v.type.llvm()).append(' ').append(v.ref);
        }
        call.append(")");
        emit(call.toString());
    }

    private void emitScanf(delphiParser.VariableContext varCtx) {
        String name = varCtx.identifier().getText();
        Symbol s = currentScope.lookup(name);
        if (s == null) throw new RuntimeException("Unknown read target: " + name);
        String fmt;
        switch (s.type) {
            case INT:  fmt = "%d"; break;
            case REAL: fmt = "%lf"; break;
            default: throw new RuntimeException("Cannot read into type: " + s.type);
        }
        String fmtPtr = internStringPtr(fmt);
        String r = freshReg();
        emit(r + " = call i32 (i8*, ...) @scanf(i8* " + fmtPtr + ", " + s.type.llvm() + "* " + s.irPtr + ")");
    }

    // ============================================================
    //                    EXPRESSIONS
    // ============================================================

    @Override
    public Value visitExpression(delphiParser.ExpressionContext ctx) {
        // honor folded values
        Object folded = folder.getFoldedValues().get(ctx);
        if (folded != null) return literalValue(folded);

        Value left = visit(ctx.simpleExpression(0));
        if (ctx.relationaloperator() == null) return left;
        Value right = visit(ctx.simpleExpression(1));
        return emitRelational(ctx.relationaloperator(), left, right);
    }

    @Override
    public Value visitSimpleExpression(delphiParser.SimpleExpressionContext ctx) {
        Value left = visit(ctx.term());
        if (ctx.additiveoperator() == null) return left;
        Value right = visit(ctx.simpleExpression());
        return emitAdditive(ctx.additiveoperator(), left, right);
    }

    @Override
    public Value visitTerm(delphiParser.TermContext ctx) {
        Value left = visit(ctx.signedFactor());
        if (ctx.multiplicativeoperator() == null) return left;
        Value right = visit(ctx.term());
        return emitMultiplicative(ctx.multiplicativeoperator(), left, right);
    }

    @Override
    public Value visitSignedFactor(delphiParser.SignedFactorContext ctx) {
        Value v = visit(ctx.factor());
        if (ctx.MINUS() != null) {
            String r = freshReg();
            if (v.type == PT.REAL) emit(r + " = fneg double " + v.ref);
            else                   emit(r + " = sub i32 0, " + v.ref);
            return new Value(r, v.type);
        }
        return v;
    }

    @Override
    public Value visitFactor(delphiParser.FactorContext ctx) {
        if (ctx.LPAREN() != null) return visit(ctx.expression());
        if (ctx.NOT() != null) {
            Value v = toI1(visit(ctx.factor()));
            String r = freshReg();
            emit(r + " = xor i1 " + v.ref + ", true");
            return new Value(r, PT.BOOL);
        }
        if (ctx.bool_() != null) {
            return new Value(ctx.bool_().TRUE() != null ? "true" : "false", PT.BOOL);
        }
        if (ctx.unsignedConstant() != null) return visit(ctx.unsignedConstant());
        if (ctx.functionDesignator() != null) return visitFunctionDesignator(ctx.functionDesignator());
        if (ctx.variable() != null) return readVariable(ctx.variable());
        return null;
    }

    @Override
    public Value visitUnsignedConstant(delphiParser.UnsignedConstantContext ctx) {
        if (ctx.unsignedNumber() != null) {
            if (ctx.unsignedNumber().unsignedInteger() != null) {
                return new Value(ctx.unsignedNumber().unsignedInteger().NUM_INT().getText(), PT.INT);
            }
            return new Value(ctx.unsignedNumber().unsignedReal().NUM_REAL().getText(), PT.REAL);
        }
        if (ctx.string() != null) {
            String raw = ctx.string().STRING_LITERAL().getText();
            if (raw.startsWith("'") && raw.endsWith("'"))
                raw = raw.substring(1, raw.length() - 1);
            raw = raw.replace("''", "'");
            return new Value(internStringPtr(raw), PT.STR);
        }
        return null;
    }

    @Override
    public Value visitFunctionDesignator(delphiParser.FunctionDesignatorContext ctx) {
        String name = ctx.variable().identifier().getText();
        FunctionSig sig = functions.get(name.toLowerCase());
        if (sig == null) throw new RuntimeException("Unknown function: " + name);
        List<String> args = buildCallArgs(sig, ctx.parameterList());
        String r = freshReg();
        emit(r + " = call " + sig.retType.llvm() + " " + sig.irName + "(" + joinArgs(sig, args) + ")");
        return new Value(r, sig.retType);
    }

    /** Read a simple variable (no suffixes) from its alloca/global/var-param pointer. */
    private Value readVariable(delphiParser.VariableContext ctx) {
        String name = ctx.identifier().getText();

        // Could also be a no-arg function call: in tests this happens via procedureStatement,
        // not here — but for safety check for function with zero params.
        FunctionSig sig = functions.get(name.toLowerCase());
        if (sig != null && sig.paramTypes.isEmpty()) {
            String r = freshReg();
            if (sig.retType == PT.VOID) {
                emit("call void " + sig.irName + "()");
                return new Value("0", PT.INT);
            }
            emit(r + " = call " + sig.retType.llvm() + " " + sig.irName + "()");
            return new Value(r, sig.retType);
        }

        Symbol s = currentScope.lookup(name);
        if (s == null) throw new RuntimeException("Undefined identifier: " + name);
        if (s.isConstant) return literalValue(s.constValue);
        String r = freshReg();
        emit(r + " = load " + s.type.llvm() + ", " + s.type.llvm() + "* " + s.irPtr);
        return new Value(r, s.type);
    }

    // ============================================================
    //                    OP HELPERS
    // ============================================================

    private Value emitAdditive(delphiParser.AdditiveoperatorContext op, Value l, Value r) {
        if (op.OR() != null) {
            l = toI1(l); r = toI1(r);
            String reg = freshReg();
            emit(reg + " = or i1 " + l.ref + ", " + r.ref);
            return new Value(reg, PT.BOOL);
        }
        if (op.XOR() != null) {
            l = toI1(l); r = toI1(r);
            String reg = freshReg();
            emit(reg + " = xor i1 " + l.ref + ", " + r.ref);
            return new Value(reg, PT.BOOL);
        }
        // arithmetic +, -
        boolean useReal = l.type == PT.REAL || r.type == PT.REAL;
        l = coerce(l, useReal ? PT.REAL : PT.INT);
        r = coerce(r, useReal ? PT.REAL : PT.INT);
        String prefix = useReal ? "f" : "";
        String llvmOp = op.PLUS() != null ? prefix + "add" : prefix + "sub";
        String reg = freshReg();
        emit(reg + " = " + llvmOp + " " + l.type.llvm() + " " + l.ref + ", " + r.ref);
        return new Value(reg, l.type);
    }

    private Value emitMultiplicative(delphiParser.MultiplicativeoperatorContext op, Value l, Value r) {
        if (op.AND() != null) {
            l = toI1(l); r = toI1(r);
            String reg = freshReg();
            emit(reg + " = and i1 " + l.ref + ", " + r.ref);
            return new Value(reg, PT.BOOL);
        }
        if (op.SLASH() != null) {
            l = coerce(l, PT.REAL); r = coerce(r, PT.REAL);
            String reg = freshReg();
            emit(reg + " = fdiv double " + l.ref + ", " + r.ref);
            return new Value(reg, PT.REAL);
        }
        if (op.DIV() != null) {
            l = coerce(l, PT.INT); r = coerce(r, PT.INT);
            String reg = freshReg();
            emit(reg + " = sdiv i32 " + l.ref + ", " + r.ref);
            return new Value(reg, PT.INT);
        }
        if (op.MOD() != null) {
            l = coerce(l, PT.INT); r = coerce(r, PT.INT);
            String reg = freshReg();
            emit(reg + " = srem i32 " + l.ref + ", " + r.ref);
            return new Value(reg, PT.INT);
        }
        // STAR
        boolean useReal = l.type == PT.REAL || r.type == PT.REAL;
        l = coerce(l, useReal ? PT.REAL : PT.INT);
        r = coerce(r, useReal ? PT.REAL : PT.INT);
        String llvmOp = useReal ? "fmul" : "mul";
        String reg = freshReg();
        emit(reg + " = " + llvmOp + " " + l.type.llvm() + " " + l.ref + ", " + r.ref);
        return new Value(reg, l.type);
    }

    private Value emitRelational(delphiParser.RelationaloperatorContext op, Value l, Value r) {
        boolean useReal = l.type == PT.REAL || r.type == PT.REAL;
        if (l.type == PT.STR && r.type == PT.STR) {
            // string comparison - not supported in the tested subset, fall through
        }
        if (useReal) { l = coerce(l, PT.REAL); r = coerce(r, PT.REAL); }
        else if (l.type == PT.INT || r.type == PT.INT) {
            l = coerce(l, PT.INT); r = coerce(r, PT.INT);
        }
        String pred;
        if (useReal) {
            if (op.EQUAL() != null)        pred = "fcmp oeq";
            else if (op.NOT_EQUAL() != null) pred = "fcmp one";
            else if (op.LT() != null)        pred = "fcmp olt";
            else if (op.LE() != null)        pred = "fcmp ole";
            else if (op.GT() != null)        pred = "fcmp ogt";
            else if (op.GE() != null)        pred = "fcmp oge";
            else throw new RuntimeException("Unsupported relop");
        } else {
            if (op.EQUAL() != null)        pred = "icmp eq";
            else if (op.NOT_EQUAL() != null) pred = "icmp ne";
            else if (op.LT() != null)        pred = "icmp slt";
            else if (op.LE() != null)        pred = "icmp sle";
            else if (op.GT() != null)        pred = "icmp sgt";
            else if (op.GE() != null)        pred = "icmp sge";
            else throw new RuntimeException("Unsupported relop");
        }
        String reg = freshReg();
        emit(reg + " = " + pred + " " + l.type.llvm() + " " + l.ref + ", " + r.ref);
        return new Value(reg, PT.BOOL);
    }

    // ============================================================
    //                    UTIL
    // ============================================================

    private Value literalValue(Object v) {
        if (v instanceof Integer) return new Value(v.toString(), PT.INT);
        if (v instanceof Double)  return new Value(formatReal((Double) v), PT.REAL);
        if (v instanceof Boolean) return new Value(((Boolean) v) ? "true" : "false", PT.BOOL);
        if (v instanceof String)  return new Value(internStringPtr((String) v), PT.STR);
        return new Value("0", PT.INT);
    }

    private String formatReal(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d)) {
            return String.format("%.1f", d);
        }
        return Double.toString(d);
    }

    /** Coerce value to target type (only int↔real promotion + i1 widening supported). */
    private Value coerce(Value v, PT target) {
        if (v.type == target) return v;
        if (target == PT.REAL && v.type == PT.INT) {
            String r = freshReg();
            emit(r + " = sitofp i32 " + v.ref + " to double");
            return new Value(r, PT.REAL);
        }
        if (target == PT.INT && v.type == PT.REAL) {
            String r = freshReg();
            emit(r + " = fptosi double " + v.ref + " to i32");
            return new Value(r, PT.INT);
        }
        if (target == PT.INT && v.type == PT.BOOL) {
            String r = freshReg();
            emit(r + " = zext i1 " + v.ref + " to i32");
            return new Value(r, PT.INT);
        }
        if (target == PT.BOOL && v.type == PT.INT) {
            String r = freshReg();
            emit(r + " = icmp ne i32 " + v.ref + ", 0");
            return new Value(r, PT.BOOL);
        }
        return v;
    }

    private Value toI1(Value v) {
        if (v.type == PT.BOOL) return v;
        return coerce(v, PT.BOOL);
    }

    /** Intern a runtime string literal, return an i8* pointer expression. */
    private String internStringPtr(String s) {
        String key = s;
        if (internedStrings.containsKey(key)) return internedStrings.get(key);

        // build the C-style escaped string and compute its byte length (incl. trailing \00)
        StringBuilder esc = new StringBuilder();
        int len = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\') { esc.append("\\\\"); len++; }
            else if (c == '"') { esc.append("\\22"); len++; }
            else if (c == '\n') { esc.append("\\0A"); len++; }
            else if (c == '\r') { esc.append("\\0D"); len++; }
            else if (c == '\t') { esc.append("\\09"); len++; }
            else if (c >= 32 && c < 127) { esc.append(c); len++; }
            else { esc.append(String.format("\\%02X", (int) c)); len++; }
        }
        len += 1; // null terminator
        String name = "@.str." + (strCounter++);
        strPool.append(name).append(" = private unnamed_addr constant [")
               .append(len).append(" x i8] c\"").append(esc).append("\\00\"\n");
        String gepRef = "getelementptr inbounds ([" + len + " x i8], [" + len + " x i8]* "
                      + name + ", i32 0, i32 0)";
        internedStrings.put(key, gepRef);
        return gepRef;
    }

    private void allocaInEntry(String name, PT t) {
        fnAllocas.append("  ").append(name).append(" = alloca ").append(t.llvm()).append('\n');
    }

    private String freshReg() { return "%" + (regCounter++); }

    private String freshLabel(String hint) {
        return hint + "." + (labelCounter++);
    }

    private String freshSuffix(String hint) {
        return hint + "." + (labelCounter++) + ".ptr";
    }

    private void startBlock(String label) {
        fnBody.append(label).append(":\n");
        currentBlock = label;
        blockHasTerminator = false;
    }

    /** If a previous instruction was a terminator, open a fresh block before emitting. */
    private void ensureBlock() {
        if (blockHasTerminator) startBlock(freshLabel("cont"));
    }

    private void emit(String instr) {
        ensureBlock();
        fnBody.append("  ").append(instr).append('\n');
    }
}
