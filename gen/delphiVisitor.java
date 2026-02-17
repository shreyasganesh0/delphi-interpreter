// Generated from delphi.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link delphiParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface delphiVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link delphiParser#programRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramRule(delphiParser.ProgramRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link delphiParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(delphiParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link delphiParser#varDeclBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclBlock(delphiParser.VarDeclBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link delphiParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(delphiParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link delphiParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(delphiParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link delphiParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(delphiParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link delphiParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(delphiParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link delphiParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(delphiParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link delphiParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(delphiParser.ExprContext ctx);
}