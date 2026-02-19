package interpreter;

import gen.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;

import java.util.*;

public class DelphiVisitor extends delphiBaseVisitor<Object> {

    private final Map<String, ClassDefinition> classRegistry = new LinkedHashMap<>();

    private final Map<String, List<String>> interfaceRegistry = new LinkedHashMap<>();

    private Environment globalEnv = new Environment(null);

    private Environment currentEnv = globalEnv;

    private ObjectInstance currentSelf = null;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Object visitProgram(delphiParser.ProgramContext ctx) {
        visit(ctx.block());
        return null;
    }

    @Override
    public Object visitBlock(delphiParser.BlockContext ctx) {
        for (var child : ctx.children) {
            if (child instanceof delphiParser.TypeDefinitionPartContext) {
                visitTypeDefinitionPart((delphiParser.TypeDefinitionPartContext) child);
            } else if (child instanceof delphiParser.VariableDeclarationPartContext) {
                visitVariableDeclarationPart((delphiParser.VariableDeclarationPartContext) child);
            } else if (child instanceof delphiParser.ProcedureAndFunctionDeclarationPartContext) {
                registerProcOrFunc((delphiParser.ProcedureAndFunctionDeclarationPartContext) child);
            }
        }
        visit(ctx.compoundStatement());
        return null;
    }

    @Override
    public Object visitTypeDefinitionPart(delphiParser.TypeDefinitionPartContext ctx) {
        for (var td : ctx.typeDefinition()) {
            visit(td);
        }
        return null;
    }

    @Override
    public Object visitTypeDefinition(delphiParser.TypeDefinitionContext ctx) {
        String typeName = ctx.identifier().getText();
        var typeCtx = ctx.type_();
        if (typeCtx != null) {
            if (typeCtx.classType() != null) {
                buildClassDefinition(typeName, typeCtx.classType());
            } else if (typeCtx.interfaceType() != null) {
                buildInterfaceDefinition(typeName, typeCtx.interfaceType());
            }
        }
        return null;
    }

    private void buildClassDefinition(String name, delphiParser.ClassTypeContext ctx) {
        String parent = null;
        if (ctx.identifierList() != null) {
            parent = ctx.identifierList().identifier(0).getText();
        }

        ClassDefinition cd = new ClassDefinition(name, parent);

        if (ctx.classBody() != null) {
            String currentVisibility = "public";
            for (var sectionCtx : ctx.classBody().classMemberSection()) {
                if (sectionCtx.visibility() != null) {
                    currentVisibility = sectionCtx.visibility().getText().toLowerCase()
                            .replace("strict", "").trim();
                }
                for (var memberCtx : sectionCtx.classMember()) {
                    if (memberCtx.variableDeclaration() != null) {
                        var vd = memberCtx.variableDeclaration();
                        String typeName = vd.type_().getText();
                        for (var id : vd.identifierList().identifier()) {
                            cd.fields.put(id.getText().toLowerCase(), typeName);
                            cd.visibility.put(id.getText().toLowerCase(), currentVisibility);
                        }
                    } else if (memberCtx.methodDecl() != null) {
                        var md = memberCtx.methodDecl();
                        String methodName = md.identifier().getText().toLowerCase();
                        cd.visibility.put(methodName, currentVisibility);
                        if (md.methodQualifier() != null) {
                            String q = md.methodQualifier().getText().toLowerCase();
                            if (q.equals("virtual") || q.equals("dynamic") || q.equals("override")) {
                                cd.virtualMethods.add(methodName);
                            }
                        }
                    }
                }
            }
        }
        classRegistry.put(name.toLowerCase(), cd);
    }

    private void buildInterfaceDefinition(String name, delphiParser.InterfaceTypeContext ctx) {
        List<String> methods = new ArrayList<>();
        if (ctx.interfaceBody() != null) {
            for (var md : ctx.interfaceBody().methodDecl()) {
                methods.add(md.identifier().getText().toLowerCase());
            }
        }
        interfaceRegistry.put(name.toLowerCase(), methods);
        ClassDefinition cd = new ClassDefinition(name, null);
        classRegistry.put(name.toLowerCase(), cd);
    }
    @Override
    public Object visitVariableDeclarationPart(delphiParser.VariableDeclarationPartContext ctx) {
        for (var vd : ctx.variableDeclaration()) {
            visit(vd);
        }
        return null;
    }

    @Override
    public Object visitVariableDeclaration(delphiParser.VariableDeclarationContext ctx) {
        String typeName = ctx.type_().getText().toLowerCase();
        Object defaultVal = defaultForType(typeName);
        for (var id : ctx.identifierList().identifier()) {
            currentEnv.define(id.getText(), defaultVal);
        }
        return null;
    }

    private Object defaultForType(String type) {
        switch (type) {
            case "integer": return 0;
            case "real":    return 0.0;
            case "boolean": return false;
            case "char":    return '\0';
            case "string":  return "";
            default:        return null;
        }
    }

    private void registerProcOrFunc(delphiParser.ProcedureAndFunctionDeclarationPartContext ctx) {
        var pod = ctx.procedureOrFunctionDeclaration();
        if (pod.procedureDeclaration() != null) {
            registerProcedure(pod.procedureDeclaration());
        } else if (pod.functionDeclaration() != null) {
            registerFunction(pod.functionDeclaration());
        } else if (pod.constructorDeclaration() != null) {
            registerConstructor(pod.constructorDeclaration());
        } else if (pod.destructorDeclaration() != null) {
            registerDestructor(pod.destructorDeclaration());
        }
    }

    private void registerProcedure(delphiParser.ProcedureDeclarationContext ctx) {
        var qmn = ctx.qualifiedMethodName();
        if (qmn.identifier().size() == 2) {
            String className  = qmn.identifier(0).getText().toLowerCase();
            String methodName = qmn.identifier(1).getText().toLowerCase();
            ClassDefinition cd = classRegistry.get(className);
            if (cd != null) cd.methods.put(methodName, ctx);
        } else {
            String name = qmn.identifier(0).getText().toLowerCase();
            globalEnv.define(name, ctx);
        }
    }

    private void registerFunction(delphiParser.FunctionDeclarationContext ctx) {
        var qmn = ctx.qualifiedMethodName();
        if (qmn.identifier().size() == 2) {
            String className  = qmn.identifier(0).getText().toLowerCase();
            String methodName = qmn.identifier(1).getText().toLowerCase();
            ClassDefinition cd = classRegistry.get(className);
            if (cd != null) cd.methods.put(methodName, ctx);
        } else {
            String name = qmn.identifier(0).getText().toLowerCase();
            globalEnv.define(name, ctx);
        }
    }

    private void registerConstructor(delphiParser.ConstructorDeclarationContext ctx) {
        var qmn = ctx.qualifiedMethodName();
        String className  = qmn.identifier(0).getText().toLowerCase();
        String methodName = (qmn.identifier().size() == 2)
                ? qmn.identifier(1).getText().toLowerCase()
                : "create";
        ClassDefinition cd = classRegistry.get(className);
        if (cd != null) cd.constructors.put(methodName, ctx);
        else {
            globalEnv.define(className + "." + methodName, ctx);
        }
    }

    private void registerDestructor(delphiParser.DestructorDeclarationContext ctx) {
        var qmn = ctx.qualifiedMethodName();
        String className  = qmn.identifier(0).getText().toLowerCase();
        String methodName = (qmn.identifier().size() == 2)
                ? qmn.identifier(1).getText().toLowerCase()
                : "destroy";
        ClassDefinition cd = classRegistry.get(className);
        if (cd != null) cd.destructors.put(methodName, ctx);
    }

    @Override
    public Object visitCompoundStatement(delphiParser.CompoundStatementContext ctx) {
        visit(ctx.statements());
        return null;
    }

    @Override
    public Object visitStatements(delphiParser.StatementsContext ctx) {
        for (var stmt : ctx.statement()) visit(stmt);
        return null;
    }

    @Override
    public Object visitStatement(delphiParser.StatementContext ctx) {
        return visit(ctx.unlabelledStatement());
    }

    @Override
    public Object visitUnlabelledStatement(delphiParser.UnlabelledStatementContext ctx) {
        if (ctx.simpleStatement() != null)     return visit(ctx.simpleStatement());
        if (ctx.structuredStatement() != null) return visit(ctx.structuredStatement());
        return null;
    }

    @Override
    public Object visitSimpleStatement(delphiParser.SimpleStatementContext ctx) {
        if (ctx.assignmentStatement() != null) return visit(ctx.assignmentStatement());
        if (ctx.ioStatement() != null)         return visit(ctx.ioStatement());
        if (ctx.inheritedStatement() != null)  return visit(ctx.inheritedStatement());
        if (ctx.procedureStatement() != null)  return visit(ctx.procedureStatement());
        if (ctx.gotoStatement() != null)       return null; 
        return null;
    }


    @Override
    public Object visitAssignmentStatement(delphiParser.AssignmentStatementContext ctx) {
        Object value = visit(ctx.expression());
        assignToVariable(ctx.variable(), value);
        return null;
    }

    private void assignToVariable(delphiParser.VariableContext ctx, Object value) {
        var suffixes = ctx.variableSuffix();
        String baseName = ctx.identifier() != null
                ? ctx.identifier().getText()
                : "Self";

        if (suffixes.isEmpty()) {
            if (baseName.equalsIgnoreCase("result")) {
                currentEnv.set("result", value);
                return;
            }
            if (currentSelf != null && currentSelf.hasField(baseName)) {
                currentSelf.setField(baseName, value);
                return;
            }
            currentEnv.set(baseName, value);
            return;
        }

        Object container = resolveVariableBase(baseName);
        for (int i = 0; i < suffixes.size() - 1; i++) {
            container = applySuffix(container, suffixes.get(i));
        }

        var lastSuffix = suffixes.get(suffixes.size() - 1);
        if (lastSuffix.DOT() != null) {
            String field = lastSuffix.identifier().getText();
            if (container instanceof ObjectInstance) {
                ((ObjectInstance) container).setField(field, value);
            } else {
                throw new RuntimeException("Cannot set field on non-object: " + baseName);
            }
        } else if (lastSuffix.LBRACK() != null || lastSuffix.LBRACK2() != null) {
            if (container instanceof Object[]) {
                int idx = toInt(visit(lastSuffix.expression(0))) - 1; 
                ((Object[]) container)[idx] = value;
            }
        }
    }

    @Override
    public Object visitIoStatement(delphiParser.IoStatementContext ctx) {
        String op = ctx.getStart().getText().toUpperCase();
        switch (op) {
            case "WRITELN":
            case "WRITE": {
                if (ctx.expressionList() != null) {
                    StringBuilder sb = new StringBuilder();
                    for (var e : ctx.expressionList().expression()) {
                        sb.append(stringify(visit(e)));
                    }
                    if (op.equals("WRITELN")) System.out.println(sb);
                    else                      System.out.print(sb);
                } else {
                    if (op.equals("WRITELN")) System.out.println();
                }
                break;
            }
            case "READLN":
            case "READ": {
                if (ctx.variable() != null) {
                    for (var v : ctx.variable()) {
                        String line = scanner.nextLine().trim();
                        Object value = parseInput(line);
                        assignToVariable(v, value);
                    }
                } else {
                    if (scanner.hasNextLine()) scanner.nextLine(); 
                }
                break;
            }
        }
        return null;
    }

    private Object parseInput(String line) {
        try { return Integer.parseInt(line); } catch (NumberFormatException e1) {}
        try { return Double.parseDouble(line); } catch (NumberFormatException e2) {}
        if (line.equalsIgnoreCase("true"))  return true;
        if (line.equalsIgnoreCase("false")) return false;
        return line;
    }

    @Override
    public Object visitInheritedStatement(delphiParser.InheritedStatementContext ctx) {
        if (currentSelf == null) return null;
        ClassDefinition cd = currentSelf.classDef;
        if (cd.parentClassName == null) return null;
        ClassDefinition parent = classRegistry.get(cd.parentClassName.toLowerCase());
        if (parent == null) return null;

        String methodName;
        List<Object> args = new ArrayList<>();

        if (ctx.identifier() != null) {
            methodName = ctx.identifier().getText().toLowerCase();
            if (ctx.parameterList() != null) {
                for (var ap : ctx.parameterList().actualParameter()) {
                    args.add(visit(ap.expression()));
                }
            }
        } else {
            return null;
        }

        ParserRuleContext body = parent.lookupMethod(methodName, classRegistry);
        if (body != null) {
            return callMethod(currentSelf, body, args);
        }
        return null;
    }

    @Override
    public Object visitProcedureStatement(delphiParser.ProcedureStatementContext ctx) {
        return callProcedureOrMethod(ctx.variable(), ctx.parameterList());
    }

    private Object callProcedureOrMethod(delphiParser.VariableContext varCtx,
                                          delphiParser.ParameterListContext paramCtx) {
        List<Object> args = new ArrayList<>();
        if (paramCtx != null) {
            for (var ap : paramCtx.actualParameter()) args.add(visit(ap.expression()));
        }
        return dispatchCall(varCtx, args);
    }

    private Object dispatchCall(delphiParser.VariableContext varCtx, List<Object> args) {
        String baseName = (varCtx.identifier() != null)
                ? varCtx.identifier().getText()
                : "Self";
        var suffixes = varCtx.variableSuffix();

        if (suffixes.isEmpty()) {
            return callNamed(baseName, args);
        }

        var lastSuffix = suffixes.get(suffixes.size() - 1);
        if (lastSuffix.DOT() != null) {
            String methodName = lastSuffix.identifier().getText();

            Object receiver;
            if (suffixes.size() == 1) {
                receiver = resolveVariableBase(baseName);
            } else {
                receiver = resolveVariableBase(baseName);
                for (int i = 0; i < suffixes.size() - 1; i++) {
                    receiver = applySuffix(receiver, suffixes.get(i));
                }
            }

            ClassDefinition baseCd = classRegistry.get(baseName.toLowerCase());
            if (baseCd != null && (receiver == null || !(receiver instanceof ObjectInstance))) {
                return dispatchConstructorOrStatic(baseCd, methodName, args);
            }

            if (receiver instanceof ObjectInstance) {
                return callObjectMethod((ObjectInstance) receiver, methodName, args);
            }
            throw new RuntimeException("Attempt to call method '" + methodName
                    + "' on non-object: " + receiver);
        }

        return null;
    }

    private Object callNamed(String name, List<Object> args) {
        switch (name.toLowerCase()) {
            case "inc":
                if (!args.isEmpty()) {
                }
                return null;
            case "dec": return null;
            case "succ": return args.isEmpty() ? 0 : toInt(args.get(0)) + 1;
            case "pred": return args.isEmpty() ? 0 : toInt(args.get(0)) - 1;
            case "abs":  return args.isEmpty() ? 0 : Math.abs(toDouble(args.get(0)));
            case "sqr":  { double v = toDouble(args.isEmpty() ? 0 : args.get(0)); return v * v; }
            case "sqrt": return Math.sqrt(toDouble(args.isEmpty() ? 0 : args.get(0)));
            case "round":return (int) Math.round(toDouble(args.isEmpty() ? 0 : args.get(0)));
            case "trunc":return (int)(toDouble(args.isEmpty() ? 0 : args.get(0)));
            case "ord":  { Object a = args.isEmpty() ? 0 : args.get(0);
                           if (a instanceof Character) return (int)(Character)a;
                           if (a instanceof Boolean) return ((Boolean)a) ? 1 : 0;
                           return toInt(a); }
            case "chr":  return (char)(toInt(args.isEmpty() ? 0 : args.get(0)));
            case "length":{ Object a = args.isEmpty() ? "" : args.get(0);
                            return a instanceof String ? ((String)a).length() : 0; }
            case "inttostr": return String.valueOf(toInt(args.isEmpty() ? 0 : args.get(0)));
            case "strtoint": return Integer.parseInt(stringify(args.isEmpty() ? "0" : args.get(0)));
        }

        if (globalEnv.has(name)) {
            Object stored = globalEnv.get(name);
            if (stored instanceof ParserRuleContext) {
                return callMethod(null, (ParserRuleContext) stored, args);
            }
        }

        if (currentSelf != null) {
            ParserRuleContext selfBody = currentSelf.classDef.lookupMethod(name, classRegistry);
            if (selfBody != null) {
                return callMethod(currentSelf, selfBody, args);
            }
        }

        ClassDefinition cd = classRegistry.get(name.toLowerCase());
        if (cd != null) {
            return dispatchConstructorOrStatic(cd, "create", args);
        }

        throw new RuntimeException("Undefined procedure/function: " + name);
    }

    private Object dispatchConstructorOrStatic(ClassDefinition cd, String methodName, List<Object> args) {
        if (methodName.equalsIgnoreCase("create") || cd.constructors.containsKey(methodName.toLowerCase())) {
            ObjectInstance obj = new ObjectInstance(cd, classRegistry);
            String ctorKey = methodName.toLowerCase();
            ParserRuleContext ctorBody = cd.constructors.get(ctorKey);
            if (ctorBody == null && cd.constructors.containsKey("create")) {
                ctorBody = cd.constructors.get("create");
            }
            if (ctorBody != null) {
                callMethod(obj, ctorBody, args);
            }
            else if (cd.parentClassName != null) {
                ClassDefinition parent = classRegistry.get(cd.parentClassName.toLowerCase());
                if (parent != null) {
                    ctorBody = parent.constructors.get(ctorKey);
                    if (ctorBody != null) callMethod(obj, ctorBody, args);
                }
            }
            return obj;
        }
        ParserRuleContext body = cd.lookupMethod(methodName, classRegistry);
        if (body != null) {
            ObjectInstance temp = new ObjectInstance(cd, classRegistry);
            return callMethod(temp, body, args);
        }
        throw new RuntimeException("Unknown class member: " + cd.name + "." + methodName);
    }

    private Object callObjectMethod(ObjectInstance obj, String methodName, List<Object> args) {
        if (methodName.equalsIgnoreCase("free") || methodName.equalsIgnoreCase("destroy")) {
            ClassDefinition cd = obj.classDef;
            ParserRuleContext dtor = cd.destructors.get("destroy");
            if (dtor == null) dtor = cd.destructors.get("free");
            if (dtor != null) callMethod(obj, dtor, args);
            return null;
        }
        ParserRuleContext body = obj.classDef.lookupMethod(methodName, classRegistry);
        if (body == null) throw new RuntimeException(
                "Method '" + methodName + "' not found on " + obj.classDef.name);
        return callMethod(obj, body, args);
    }

    private Object callMethod(ObjectInstance self, ParserRuleContext bodyCtx, List<Object> args) {
        Environment saved = currentEnv;
        ObjectInstance savedSelf = currentSelf;

        Environment callEnv = new Environment(globalEnv);
        currentEnv = callEnv;
        currentSelf = self;

        if (self != null) {
            callEnv.define("self", self);
        }

        try {
            if (bodyCtx instanceof delphiParser.ProcedureDeclarationContext) {
                var ctx = (delphiParser.ProcedureDeclarationContext) bodyCtx;
                bindParameters(ctx.formalParameterList(), args, callEnv);
                visitBlock(ctx.block());
                return null;

            } else if (bodyCtx instanceof delphiParser.FunctionDeclarationContext) {
                var ctx = (delphiParser.FunctionDeclarationContext) bodyCtx;
                String resultName = ctx.qualifiedMethodName().identifier(
                        ctx.qualifiedMethodName().identifier().size() - 1).getText();
                callEnv.define("result", defaultForType(ctx.resultType().typeIdentifier().getText().toLowerCase()));
                bindParameters(ctx.formalParameterList(), args, callEnv);
                visitBlock(ctx.block());
                return callEnv.get("result");

            } else if (bodyCtx instanceof delphiParser.ConstructorDeclarationContext) {
                var ctx = (delphiParser.ConstructorDeclarationContext) bodyCtx;
                bindParameters(ctx.formalParameterList(), args, callEnv);
                visitBlock(ctx.block());
                return self;

            } else if (bodyCtx instanceof delphiParser.DestructorDeclarationContext) {
                var ctx = (delphiParser.DestructorDeclarationContext) bodyCtx;
                bindParameters(ctx.formalParameterList(), args, callEnv);
                visitBlock(ctx.block());
                return null;
            }
        } catch (ReturnException re) {
            return re.value;
        } finally {
            currentEnv = saved;
            currentSelf = savedSelf;
        }
        return null;
    }

    private void bindParameters(delphiParser.FormalParameterListContext fpl,
                                  List<Object> args, Environment env) {
        if (fpl == null) return;
        int argIdx = 0;
        for (var section : fpl.formalParameterSection()) {
            var pg = section.parameterGroup();
            if (pg == null) continue;
            for (var id : pg.identifierList().identifier()) {
                Object val = argIdx < args.size() ? args.get(argIdx++) : null;
                env.define(id.getText(), val);
            }
        }
    }

    @Override
    public Object visitIfStatement(delphiParser.IfStatementContext ctx) {
        boolean cond = toBool(visit(ctx.expression()));
        if (cond) {
            visit(ctx.statement(0));
        } else if (ctx.statement().size() > 1) {
            visit(ctx.statement(1));
        }
        return null;
    }

    @Override
    public Object visitWhileStatement(delphiParser.WhileStatementContext ctx) {
        while (toBool(visit(ctx.expression()))) {
            visit(ctx.statement());
        }
        return null;
    }

    @Override
    public Object visitRepeatStatement(delphiParser.RepeatStatementContext ctx) {
        do {
            visit(ctx.statements());
        } while (!toBool(visit(ctx.expression())));
        return null;
    }

    @Override
    public Object visitForStatement(delphiParser.ForStatementContext ctx) {
        String varName = ctx.identifier().getText();
        int start = toInt(visit(ctx.forList().initialValue()));
        int end   = toInt(visit(ctx.forList().finalValue()));
        boolean downTo = ctx.forList().DOWNTO() != null;

        if (!downTo) {
            for (int i = start; i <= end; i++) {
                currentEnv.set(varName, i);
                visit(ctx.statement());
            }
        } else {
            for (int i = start; i >= end; i--) {
                currentEnv.set(varName, i);
                visit(ctx.statement());
            }
        }
        return null;
    }

    @Override
    public Object visitCaseStatement(delphiParser.CaseStatementContext ctx) {
        Object sel = visit(ctx.expression());
        for (var elem : ctx.caseListElement()) {
            for (var c : elem.constList().constant()) {
                Object cv = visit(c);
                if (objectsEqual(sel, cv)) {
                    visit(elem.statement());
                    return null;
                }
            }
        }
        if (ctx.statements() != null) visit(ctx.statements());
        return null;
    }

    @Override
    public Object visitWithStatement(delphiParser.WithStatementContext ctx) {
        Object obj = visit(ctx.recordVariableList().variable(0));
        if (obj instanceof ObjectInstance) {
            ObjectInstance inst = (ObjectInstance) obj;
            ObjectInstance savedSelf = currentSelf;
            currentSelf = inst;
            visit(ctx.statement());
            currentSelf = savedSelf;
        } else {
            visit(ctx.statement());
        }
        return null;
    }

    @Override
    public Object visitExpression(delphiParser.ExpressionContext ctx) {
        Object left = visit(ctx.simpleExpression(0));
        if (ctx.relationaloperator() == null) return left;
        Object right = visit(ctx.simpleExpression(1));
        return applyRelational(ctx.relationaloperator(), left, right);
    }

    private Object applyRelational(delphiParser.RelationaloperatorContext op, Object l, Object r) {
        if (op.EQUAL()     != null) return objectsEqual(l, r);
        if (op.NOT_EQUAL() != null) return !objectsEqual(l, r);
        double ld = toDouble(l), rd = toDouble(r);
        if (op.LT() != null) return ld < rd;
        if (op.LE() != null) return ld <= rd;
        if (op.GT() != null) return ld > rd;
        if (op.GE() != null) return ld >= rd;
        if (op.IS() != null) {
            if (l instanceof ObjectInstance && r instanceof ClassDefinition)
                return ((ObjectInstance) l).classDef == r;
            return false;
        }
        return false;
    }

    @Override
    public Object visitSimpleExpression(delphiParser.SimpleExpressionContext ctx) {
        Object left = visit(ctx.term());
        if (ctx.additiveoperator() == null) return left;
        Object right = visit(ctx.simpleExpression());
        return applyAdditive(ctx.additiveoperator(), left, right);
    }

    private Object applyAdditive(delphiParser.AdditiveoperatorContext op, Object l, Object r) {
        if (op.OR()  != null) return toBool(l) || toBool(r);
        if (op.XOR() != null) return toBool(l) ^ toBool(r);
        if (op.PLUS() != null) {
            if (l instanceof String || r instanceof String)
                return stringify(l) + stringify(r);
            if (l instanceof Double || r instanceof Double)
                return toDouble(l) + toDouble(r);
            return toInt(l) + toInt(r);
        }
        if (op.MINUS() != null) {
            if (l instanceof Double || r instanceof Double) return toDouble(l) - toDouble(r);
            return toInt(l) - toInt(r);
        }
        return 0;
    }

    @Override
    public Object visitTerm(delphiParser.TermContext ctx) {
        Object left = visit(ctx.signedFactor());
        if (ctx.multiplicativeoperator() == null) return left;
        Object right = visit(ctx.term());
        return applyMultiplicative(ctx.multiplicativeoperator(), left, right);
    }

    private Object applyMultiplicative(delphiParser.MultiplicativeoperatorContext op, Object l, Object r) {
        if (op.AND() != null) return toBool(l) && toBool(r);
        if (op.STAR()  != null) {
            if (l instanceof Double || r instanceof Double) return toDouble(l) * toDouble(r);
            return toInt(l) * toInt(r);
        }
        if (op.SLASH() != null) return toDouble(l) / toDouble(r);
        if (op.DIV()   != null) return toInt(l) / toInt(r);
        if (op.MOD()   != null) return toInt(l) % toInt(r);
        if (op.SHL()   != null) return toInt(l) << toInt(r);
        if (op.SHR()   != null) return toInt(l) >> toInt(r);
        return 0;
    }

    @Override
    public Object visitSignedFactor(delphiParser.SignedFactorContext ctx) {
        Object val = visit(ctx.factor());
        if (ctx.MINUS() != null) {
            if (val instanceof Double) return -(double)(Double) val;
            return -toInt(val);
        }
        return val;
    }

    @Override
    public Object visitFactor(delphiParser.FactorContext ctx) {
        if (ctx.LPAREN() != null)          return visit(ctx.expression());
        if (ctx.functionDesignator() != null) return visit(ctx.functionDesignator());
        if (ctx.unsignedConstant() != null)   return visit(ctx.unsignedConstant());
        if (ctx.bool_() != null)              return visit(ctx.bool_());
        if (ctx.NIL() != null)                return null;
        if (ctx.NOT() != null)                return !toBool(visit(ctx.factor()));
        if (ctx.set_() != null)               return visit(ctx.set_());
        if (ctx.variable() != null)           return visit(ctx.variable());
        return null;
    }

    @Override
    public Object visitFunctionDesignator(delphiParser.FunctionDesignatorContext ctx) {
        List<Object> args = new ArrayList<>();
        if (ctx.parameterList() != null) {
            for (var ap : ctx.parameterList().actualParameter()) args.add(visit(ap.expression()));
        }
        return dispatchCall(ctx.variable(), args);
    }

    @Override
    public Object visitBool_(delphiParser.Bool_Context ctx) {
        return ctx.TRUE() != null;
    }

    @Override
    public Object visitUnsignedConstant(delphiParser.UnsignedConstantContext ctx) {
        if (ctx.unsignedNumber() != null) return visit(ctx.unsignedNumber());
        if (ctx.string() != null)          return evalString(ctx.string().STRING_LITERAL().getText());
        if (ctx.constantChr() != null) {
            int n = Integer.parseInt(ctx.constantChr().unsignedInteger().NUM_INT().getText());
            return (char) n;
        }
        return null;
    }

    @Override
    public Object visitUnsignedNumber(delphiParser.UnsignedNumberContext ctx) {
        if (ctx.unsignedInteger() != null)
            return Integer.parseInt(ctx.unsignedInteger().NUM_INT().getText());
        return Double.parseDouble(ctx.unsignedReal().NUM_REAL().getText());
    }

    @Override
    public Object visitConstant(delphiParser.ConstantContext ctx) {
        Object base;
        if (ctx.unsignedNumber() != null) base = visit(ctx.unsignedNumber());
        else if (ctx.identifier() != null) {
            String id = ctx.identifier().getText();
            base = globalEnv.has(id) ? globalEnv.get(id) : 0;
        }
        else if (ctx.string() != null) base = evalString(ctx.string().STRING_LITERAL().getText());
        else if (ctx.constantChr() != null) {
            int n = Integer.parseInt(ctx.constantChr().unsignedInteger().NUM_INT().getText());
            base = (char) n;
        }
        else base = 0;

        if (ctx.sign() != null && ctx.sign().MINUS() != null) {
            if (base instanceof Double) return -(double)(Double)base;
            return -toInt(base);
        }
        return base;
    }

    @Override
    public Object visitVariable(delphiParser.VariableContext ctx) {
        String baseName;
        if (ctx.SELF() != null) {
            baseName = "Self";
        } else {
            baseName = ctx.identifier().getText();
        }

        Object value = resolveVariableBase(baseName);
        for (var suffix : ctx.variableSuffix()) {
            value = applySuffix(value, suffix);
        }
        return value;
    }

    private Object resolveVariableBase(String name) {
        if (name.equalsIgnoreCase("self")) {
            return currentSelf;
        }
        if (currentEnv.has(name)) return currentEnv.get(name);
        if (currentSelf != null && currentSelf.hasField(name)) {
            return currentSelf.getField(name);
        }
        if (globalEnv.has(name)) return globalEnv.get(name);
        ClassDefinition cd = classRegistry.get(name.toLowerCase());
        if (cd != null) return cd;
        return null;
    }

    private Object applySuffix(Object container, delphiParser.VariableSuffixContext suffix) {
        if (suffix.DOT() != null) {
            String fieldName = suffix.identifier().getText();
            if (container instanceof ObjectInstance) {
                return ((ObjectInstance) container).getField(fieldName);
            }
            if (container instanceof ClassDefinition) {
                ClassDefinition cd = (ClassDefinition) container;
                String lname = fieldName.toLowerCase();
                if (lname.equals("create") || cd.constructors.containsKey(lname)) {
                    return dispatchConstructorOrStatic(cd, fieldName, new ArrayList<>());
                }
                return cd;
            }
            throw new RuntimeException("Cannot access field '" + fieldName + "' on " + container);
        }
        if (suffix.LBRACK() != null || suffix.LBRACK2() != null) {
            int idx = toInt(visit(suffix.expression(0))) - 1;
            if (container instanceof Object[]) return ((Object[]) container)[idx];
            if (container instanceof String)   return String.valueOf(((String)container).charAt(idx));
        }
        return container;
    }

    private int toInt(Object v) {
        if (v == null)             return 0;
        if (v instanceof Integer)  return (Integer) v;
        if (v instanceof Double)   return (int)(double)(Double) v;
        if (v instanceof Boolean)  return ((Boolean) v) ? 1 : 0;
        if (v instanceof Character)return (int)(Character) v;
        if (v instanceof String)   {
            try { return Integer.parseInt((String) v); } catch (Exception e) { return 0; }
        }
        return 0;
    }

    private double toDouble(Object v) {
        if (v == null)             return 0.0;
        if (v instanceof Double)   return (Double) v;
        if (v instanceof Integer)  return (double)(Integer) v;
        if (v instanceof Boolean)  return ((Boolean) v) ? 1.0 : 0.0;
        if (v instanceof String)   {
            try { return Double.parseDouble((String) v); } catch (Exception e) { return 0.0; }
        }
        return 0.0;
    }

    private boolean toBool(Object v) {
        if (v == null)            return false;
        if (v instanceof Boolean) return (Boolean) v;
        if (v instanceof Integer) return (Integer) v != 0;
        if (v instanceof Double)  return (Double) v != 0.0;
        if (v instanceof String)  return !((String) v).isEmpty();
        return false;
    }

    private String stringify(Object v) {
        if (v == null)             return "nil";
        if (v instanceof Boolean)  return ((Boolean) v) ? "True" : "False";
        if (v instanceof Double) {
            double d = (Double) v;
            if (d == Math.floor(d) && !Double.isInfinite(d)) return String.valueOf((long) d);
            return String.valueOf(d);
        }
        if (v instanceof Character) return String.valueOf(v);
        return String.valueOf(v);
    }

    private boolean objectsEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        if (a instanceof Number && b instanceof Number)
            return toDouble(a) == toDouble(b);
        return a.equals(b);
    }

    private String evalString(String raw) {
        if (raw.startsWith("'") && raw.endsWith("'")) {
            raw = raw.substring(1, raw.length() - 1);
        }
        return raw.replace("''", "'");
    }
}
