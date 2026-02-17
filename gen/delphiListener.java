// Generated from delphi.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link delphiParser}.
 */
public interface delphiListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link delphiParser#programRule}.
	 * @param ctx the parse tree
	 */
	void enterProgramRule(delphiParser.ProgramRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#programRule}.
	 * @param ctx the parse tree
	 */
	void exitProgramRule(delphiParser.ProgramRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(delphiParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(delphiParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#varDeclBlock}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclBlock(delphiParser.VarDeclBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#varDeclBlock}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclBlock(delphiParser.VarDeclBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(delphiParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(delphiParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(delphiParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(delphiParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(delphiParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(delphiParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(delphiParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(delphiParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(delphiParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(delphiParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link delphiParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(delphiParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link delphiParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(delphiParser.ExprContext ctx);
}