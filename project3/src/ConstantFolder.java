import gen.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;

public class ConstantFolder extends delphiBaseVisitor<Object> {

    private final Map<String, Object> constants = new LinkedHashMap<>();
    private final Map<ParserRuleContext, Object> foldedValues = new LinkedHashMap<>();
    private final List<String> trace = new ArrayList<>();

    public Map<ParserRuleContext, Object> getFoldedValues() { return foldedValues; }
    public Map<String, Object> getConstants() { return constants; }
    public List<String> getTrace() { return trace; }

    @Override
    public Object visitProgram(delphiParser.ProgramContext ctx) {
        visit(ctx.block());
        return null;
    }

    @Override
    public Object visitBlock(delphiParser.BlockContext ctx) {
        for (var child : ctx.children) {
            if (child instanceof delphiParser.ConstantDefinitionPartContext) {
                visit((delphiParser.ConstantDefinitionPartContext) child);
            }
        }
        if (ctx.compoundStatement() != null) visit(ctx.compoundStatement());
        return null;
    }

    @Override
    public Object visitConstantDefinitionPart(delphiParser.ConstantDefinitionPartContext ctx) {
        for (var cd : ctx.constantDefinition()) {
            String name = cd.identifier().getText().toLowerCase();
            Object value = evalConstant(cd.constant());
            if (value != null) constants.put(name, value);
        }
        return null;
    }

    private Object evalConstant(delphiParser.ConstantContext ctx) {
        Object base = null;
        if (ctx.unsignedNumber() != null) {
            if (ctx.unsignedNumber().unsignedInteger() != null)
                base = Integer.parseInt(ctx.unsignedNumber().unsignedInteger().NUM_INT().getText());
            else
                base = Double.parseDouble(ctx.unsignedNumber().unsignedReal().NUM_REAL().getText());
        } else if (ctx.identifier() != null) {
            base = constants.get(ctx.identifier().getText().toLowerCase());
        } else if (ctx.string() != null) {
            String raw = ctx.string().STRING_LITERAL().getText();
            if (raw.startsWith("'") && raw.endsWith("'"))
                raw = raw.substring(1, raw.length() - 1);
            base = raw.replace("''", "'");
        }
        if (base != null && ctx.sign() != null && ctx.sign().MINUS() != null) {
            if (base instanceof Double) return -(double)(Double) base;
            if (base instanceof Integer) return -(int)(Integer) base;
        }
        return base;
    }

    @Override
    public Object visitCompoundStatement(delphiParser.CompoundStatementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitStatements(delphiParser.StatementsContext ctx) {
        for (var stmt : ctx.statement()) visit(stmt);
        return null;
    }

    @Override
    public Object visitExpression(delphiParser.ExpressionContext ctx) {
        Object left = visitSimpleExpression(ctx.simpleExpression(0));
        if (ctx.relationaloperator() != null) {
            Object right = visitSimpleExpression(ctx.simpleExpression(1));
            if (left != null && right != null) {
                Object result = foldRelational(ctx.relationaloperator(), left, right);
                if (result != null) {
                    foldedValues.put(ctx, result);
                    String msg = ctx.getText() + " => " + result + " [FOLDED]";
                    trace.add(msg);
                    System.out.println(msg);
                    return result;
                }
            }
            return null;
        }
        if (left != null) {
            foldedValues.put(ctx, left);
            if (ctx.simpleExpression(0).additiveoperator() != null) {
                String msg = ctx.getText() + " => " + left + " [FOLDED]";
                trace.add(msg);
                System.out.println(msg);
            }
        }
        return left;
    }

    @Override
    public Object visitSimpleExpression(delphiParser.SimpleExpressionContext ctx) {
        Object left = visitTerm(ctx.term());
        if (ctx.additiveoperator() == null) return left;
        Object right = visitSimpleExpression(ctx.simpleExpression());
        if (left == null || right == null) return null;
        return foldAdditive(ctx.additiveoperator(), left, right);
    }

    @Override
    public Object visitTerm(delphiParser.TermContext ctx) {
        Object left = visitSignedFactor(ctx.signedFactor());
        if (ctx.multiplicativeoperator() == null) return left;
        Object right = visitTerm(ctx.term());
        if (left == null || right == null) return null;
        return foldMultiplicative(ctx.multiplicativeoperator(), left, right);
    }

    @Override
    public Object visitSignedFactor(delphiParser.SignedFactorContext ctx) {
        Object val = visitFactor(ctx.factor());
        if (val == null) return null;
        if (ctx.MINUS() != null) {
            if (val instanceof Double) return -(double)(Double) val;
            if (val instanceof Integer) return -(int)(Integer) val;
        }
        return val;
    }

    @Override
    public Object visitFactor(delphiParser.FactorContext ctx) {
        if (ctx.LPAREN() != null) return visitExpression(ctx.expression());
        if (ctx.unsignedConstant() != null) return visitUnsignedConstant(ctx.unsignedConstant());
        if (ctx.bool_() != null) return ctx.bool_().TRUE() != null;
        if (ctx.variable() != null) {
            var varCtx = ctx.variable();
            if (varCtx.identifier() != null && varCtx.variableSuffix().isEmpty()) {
                String name = varCtx.identifier().getText().toLowerCase();
                if (constants.containsKey(name)) return constants.get(name);
            }
        }
        return null;
    }

    @Override
    public Object visitUnsignedConstant(delphiParser.UnsignedConstantContext ctx) {
        if (ctx.unsignedNumber() != null) {
            if (ctx.unsignedNumber().unsignedInteger() != null)
                return Integer.parseInt(ctx.unsignedNumber().unsignedInteger().NUM_INT().getText());
            return Double.parseDouble(ctx.unsignedNumber().unsignedReal().NUM_REAL().getText());
        }
        if (ctx.string() != null) {
            String raw = ctx.string().STRING_LITERAL().getText();
            if (raw.startsWith("'") && raw.endsWith("'"))
                raw = raw.substring(1, raw.length() - 1);
            return raw.replace("''", "'");
        }
        return null;
    }

    private Object foldAdditive(delphiParser.AdditiveoperatorContext op, Object l, Object r) {
        if (op.PLUS() != null) {
            if (l instanceof String || r instanceof String) return String.valueOf(l) + String.valueOf(r);
            if (l instanceof Double || r instanceof Double) return toDouble(l) + toDouble(r);
            return toInt(l) + toInt(r);
        }
        if (op.MINUS() != null) {
            if (l instanceof Double || r instanceof Double) return toDouble(l) - toDouble(r);
            return toInt(l) - toInt(r);
        }
        if (op.OR() != null) return toBool(l) || toBool(r);
        return null;
    }

    private Object foldMultiplicative(delphiParser.MultiplicativeoperatorContext op, Object l, Object r) {
        if (op.STAR() != null) {
            if (l instanceof Double || r instanceof Double) return toDouble(l) * toDouble(r);
            return toInt(l) * toInt(r);
        }
        if (op.SLASH() != null) return toDouble(l) / toDouble(r);
        if (op.DIV() != null) return toInt(l) / toInt(r);
        if (op.MOD() != null) return toInt(l) % toInt(r);
        if (op.AND() != null) return toBool(l) && toBool(r);
        return null;
    }

    private Object foldRelational(delphiParser.RelationaloperatorContext op, Object l, Object r) {
        if (op.EQUAL() != null) return objectsEqual(l, r);
        if (op.NOT_EQUAL() != null) return !objectsEqual(l, r);
        if (op.LT() != null) return toDouble(l) < toDouble(r);
        if (op.LE() != null) return toDouble(l) <= toDouble(r);
        if (op.GT() != null) return toDouble(l) > toDouble(r);
        if (op.GE() != null) return toDouble(l) >= toDouble(r);
        return null;
    }

    private int toInt(Object v) {
        if (v instanceof Integer) return (Integer) v;
        if (v instanceof Double) return (int)(double)(Double) v;
        if (v instanceof Boolean) return ((Boolean) v) ? 1 : 0;
        return 0;
    }

    private double toDouble(Object v) {
        if (v instanceof Double) return (Double) v;
        if (v instanceof Integer) return (double)(Integer) v;
        return 0.0;
    }

    private boolean toBool(Object v) {
        if (v instanceof Boolean) return (Boolean) v;
        if (v instanceof Integer) return (Integer) v != 0;
        return false;
    }

    private boolean objectsEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        if (a instanceof Number && b instanceof Number) return toDouble(a) == toDouble(b);
        return a.equals(b);
    }
}
