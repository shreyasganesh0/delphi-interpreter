// Generated from delphi.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class delphiParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PROGRAM=1, BEGIN=2, END=3, TRY=4, USES=5, RAISES=6, UNIT=7, CONST=8, VAR=9, 
		TYPE=10, STRING=11, RESOURCESTRING=12, ARRAY=13, CLASS=14, INHERITED=15, 
		INTERFACE=16, DISPINTERFACE=17, IMPLEMENTATION=18, INITIALIZATION=19, 
		FINALIZATION=20, IS=21, IN=22, AS=23, WHILE=24, FOR=25, OF=26, TO=27, 
		IF=28, THEN=29, ELSE=30, MOD=31, XOR=32, DIV=33, NOT=34, OR=35, AND=36, 
		WITH=37, GOTO=38, UNTIL=39, DOWNTO=40, CONSTRUCTOR=41, DESTRUCTOR=42, 
		INLINE=43, PROPERTY=44, ASM=45, FILE=46, SET=47, EXPORTS=48, SHL=49, SHR=50, 
		NIL=51, PACKED=52, THREADVAR=53, FINALLY=54, DEPRECATED=55, PLATFORM=56, 
		LIBRARY=57, INTEGER=58, EXTENDED=59, PLUS=60, MINUS=61, SLASH=62, CARAT=63, 
		EQUAL=64, INEQ=65, GREATER=66, GREATEREQ=67, LESSER=68, LESSEREQ=69, POUND=70, 
		DOLLAR=71, AMPERSAND=72, AT=73, SEMI=74, COLON=75, ASSIGN=76, DOT=77, 
		COMMA=78, CURLO=79, CURLC=80, CIRCO=81, CIRCC=82, CIRCDOTO=83, CIRCDOTC=84, 
		CIRCSTARO=85, CIRCSTARC=86, SQUAREO=87, SQUAREC=88, STAR=89, QOUTE=90, 
		ID=91, STR=92, INT=93, WS=94;
	public static final int
		RULE_programRule = 0, RULE_block = 1, RULE_varDeclBlock = 2, RULE_statement = 3, 
		RULE_functionCall = 4, RULE_assignment = 5, RULE_varDecl = 6, RULE_type = 7, 
		RULE_expr = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"programRule", "block", "varDeclBlock", "statement", "functionCall", 
			"assignment", "varDecl", "type", "expr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'program'", "'begin'", "'end'", "'try'", "'uses'", "'raises'", 
			"'unit'", "'const'", "'var'", "'type'", "'string'", "'resourcestring'", 
			"'array'", "'class'", "'inherited'", "'interface'", "'dispinterface'", 
			"'implementation'", "'initialization'", "'finalization'", "'is'", "'in'", 
			"'as'", "'while'", "'for'", "'of'", "'to'", "'if'", "'then'", "'else'", 
			"'mod'", "'xor'", "'div'", "'not'", "'or'", "'and'", "'with'", "'goto'", 
			"'until'", "'downto'", "'constructor'", "'destructor'", "'inline'", "'property'", 
			"'asm'", "'file'", "'set'", "'exports'", "'shl'", "'shr'", "'nil'", "'packed'", 
			"'threadvar'", "'finally'", "'deprecated'", "'platform'", "'library'", 
			"'Integer'", "'Extended'", "'+'", "'-'", "'/'", "'^'", "'='", "'<>'", 
			"'>'", "'>='", "'<'", "'<='", "'#'", "'$'", "'&'", "'@'", "';'", "':'", 
			"':='", "'.'", "','", "'{'", "'}'", "'('", "')'", "'(.'", "'.)'", "'(*'", 
			"'*)'", "'['", "']'", "'*'", "'''"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PROGRAM", "BEGIN", "END", "TRY", "USES", "RAISES", "UNIT", "CONST", 
			"VAR", "TYPE", "STRING", "RESOURCESTRING", "ARRAY", "CLASS", "INHERITED", 
			"INTERFACE", "DISPINTERFACE", "IMPLEMENTATION", "INITIALIZATION", "FINALIZATION", 
			"IS", "IN", "AS", "WHILE", "FOR", "OF", "TO", "IF", "THEN", "ELSE", "MOD", 
			"XOR", "DIV", "NOT", "OR", "AND", "WITH", "GOTO", "UNTIL", "DOWNTO", 
			"CONSTRUCTOR", "DESTRUCTOR", "INLINE", "PROPERTY", "ASM", "FILE", "SET", 
			"EXPORTS", "SHL", "SHR", "NIL", "PACKED", "THREADVAR", "FINALLY", "DEPRECATED", 
			"PLATFORM", "LIBRARY", "INTEGER", "EXTENDED", "PLUS", "MINUS", "SLASH", 
			"CARAT", "EQUAL", "INEQ", "GREATER", "GREATEREQ", "LESSER", "LESSEREQ", 
			"POUND", "DOLLAR", "AMPERSAND", "AT", "SEMI", "COLON", "ASSIGN", "DOT", 
			"COMMA", "CURLO", "CURLC", "CIRCO", "CIRCC", "CIRCDOTO", "CIRCDOTC", 
			"CIRCSTARO", "CIRCSTARC", "SQUAREO", "SQUAREC", "STAR", "QOUTE", "ID", 
			"STR", "INT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "delphi.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public delphiParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramRuleContext extends ParserRuleContext {
		public TerminalNode PROGRAM() { return getToken(delphiParser.PROGRAM, 0); }
		public TerminalNode ID() { return getToken(delphiParser.ID, 0); }
		public TerminalNode SEMI() { return getToken(delphiParser.SEMI, 0); }
		public TerminalNode DOT() { return getToken(delphiParser.DOT, 0); }
		public TerminalNode EOF() { return getToken(delphiParser.EOF, 0); }
		public VarDeclBlockContext varDeclBlock() {
			return getRuleContext(VarDeclBlockContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ProgramRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).enterProgramRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).exitProgramRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof delphiVisitor ) return ((delphiVisitor<? extends T>)visitor).visitProgramRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramRuleContext programRule() throws RecognitionException {
		ProgramRuleContext _localctx = new ProgramRuleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programRule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			match(PROGRAM);
			setState(19);
			match(ID);
			setState(20);
			match(SEMI);
			setState(22);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(21);
				varDeclBlock();
				}
			}

			setState(25);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==BEGIN) {
				{
				setState(24);
				block();
				}
			}

			setState(27);
			match(DOT);
			setState(28);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode BEGIN() { return getToken(delphiParser.BEGIN, 0); }
		public TerminalNode END() { return getToken(delphiParser.END, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof delphiVisitor ) return ((delphiVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			match(BEGIN);
			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(31);
				statement();
				}
				}
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(37);
			match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclBlockContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(delphiParser.VAR, 0); }
		public List<VarDeclContext> varDecl() {
			return getRuleContexts(VarDeclContext.class);
		}
		public VarDeclContext varDecl(int i) {
			return getRuleContext(VarDeclContext.class,i);
		}
		public VarDeclBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).enterVarDeclBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).exitVarDeclBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof delphiVisitor ) return ((delphiVisitor<? extends T>)visitor).visitVarDeclBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclBlockContext varDeclBlock() throws RecognitionException {
		VarDeclBlockContext _localctx = new VarDeclBlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_varDeclBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			match(VAR);
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(40);
				varDecl();
				}
				}
				setState(45);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(delphiParser.SEMI, 0); }
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof delphiVisitor ) return ((delphiVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_statement);
		try {
			setState(52);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(46);
				assignment();
				setState(47);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(49);
				functionCall();
				setState(50);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(delphiParser.ID, 0); }
		public TerminalNode CIRCO() { return getToken(delphiParser.CIRCO, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CIRCC() { return getToken(delphiParser.CIRCC, 0); }
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof delphiVisitor ) return ((delphiVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			match(ID);
			setState(55);
			match(CIRCO);
			setState(56);
			expr(0);
			setState(57);
			match(CIRCC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignmentContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(delphiParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(delphiParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).exitAssignment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof delphiVisitor ) return ((delphiVisitor<? extends T>)visitor).visitAssignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(ID);
			setState(60);
			match(ASSIGN);
			setState(61);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(delphiParser.ID, 0); }
		public TerminalNode COLON() { return getToken(delphiParser.COLON, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(delphiParser.SEMI, 0); }
		public TerminalNode PLATFORM() { return getToken(delphiParser.PLATFORM, 0); }
		public TerminalNode DEPRECATED() { return getToken(delphiParser.DEPRECATED, 0); }
		public TerminalNode LIBRARY() { return getToken(delphiParser.LIBRARY, 0); }
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).enterVarDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).exitVarDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof delphiVisitor ) return ((delphiVisitor<? extends T>)visitor).visitVarDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_varDecl);
		int _la;
		try {
			setState(74);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(63);
				match(ID);
				setState(64);
				match(COLON);
				setState(65);
				type();
				setState(66);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				match(ID);
				setState(69);
				match(COLON);
				setState(70);
				type();
				setState(71);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 252201579132747776L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(72);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode EXTENDED() { return getToken(delphiParser.EXTENDED, 0); }
		public TerminalNode INTEGER() { return getToken(delphiParser.INTEGER, 0); }
		public TerminalNode STRING() { return getToken(delphiParser.STRING, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof delphiVisitor ) return ((delphiVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 864691128455137280L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(delphiParser.INT, 0); }
		public TerminalNode STR() { return getToken(delphiParser.STR, 0); }
		public TerminalNode ID() { return getToken(delphiParser.ID, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode STAR() { return getToken(delphiParser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(delphiParser.SLASH, 0); }
		public TerminalNode PLUS() { return getToken(delphiParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(delphiParser.MINUS, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof delphiListener ) ((delphiListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof delphiVisitor ) return ((delphiVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 16;
		enterRecursionRule(_localctx, 16, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				}
				break;
			case 2:
				{
				setState(79);
				match(INT);
				}
				break;
			case 3:
				{
				setState(80);
				match(STR);
				}
				break;
			case 4:
				{
				setState(81);
				match(ID);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(92);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(90);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(84);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(85);
						_la = _input.LA(1);
						if ( !(_la==SLASH || _la==STAR) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(86);
						expr(7);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(87);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(88);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(89);
						expr(6);
						}
						break;
					}
					} 
				}
				setState(94);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 8:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 6);
		case 1:
			return precpred(_ctx, 5);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001^`\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002"+
		"\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005"+
		"\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007"+
		"\b\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u0017\b"+
		"\u0000\u0001\u0000\u0003\u0000\u001a\b\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0005\u0001!\b\u0001\n\u0001\f\u0001$\t"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0005\u0002*\b"+
		"\u0002\n\u0002\f\u0002-\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0003\u00035\b\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0003\u0006K\b\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0003\bS\b\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0005\b[\b\b\n\b\f\b^\t\b\u0001\b\u0000\u0001\u0010\t\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0000\u0004\u0001\u000079\u0002\u0000\u000b\u000b"+
		":;\u0002\u0000>>YY\u0001\u0000<=a\u0000\u0012\u0001\u0000\u0000\u0000"+
		"\u0002\u001e\u0001\u0000\u0000\u0000\u0004\'\u0001\u0000\u0000\u0000\u0006"+
		"4\u0001\u0000\u0000\u0000\b6\u0001\u0000\u0000\u0000\n;\u0001\u0000\u0000"+
		"\u0000\fJ\u0001\u0000\u0000\u0000\u000eL\u0001\u0000\u0000\u0000\u0010"+
		"R\u0001\u0000\u0000\u0000\u0012\u0013\u0005\u0001\u0000\u0000\u0013\u0014"+
		"\u0005[\u0000\u0000\u0014\u0016\u0005J\u0000\u0000\u0015\u0017\u0003\u0004"+
		"\u0002\u0000\u0016\u0015\u0001\u0000\u0000\u0000\u0016\u0017\u0001\u0000"+
		"\u0000\u0000\u0017\u0019\u0001\u0000\u0000\u0000\u0018\u001a\u0003\u0002"+
		"\u0001\u0000\u0019\u0018\u0001\u0000\u0000\u0000\u0019\u001a\u0001\u0000"+
		"\u0000\u0000\u001a\u001b\u0001\u0000\u0000\u0000\u001b\u001c\u0005M\u0000"+
		"\u0000\u001c\u001d\u0005\u0000\u0000\u0001\u001d\u0001\u0001\u0000\u0000"+
		"\u0000\u001e\"\u0005\u0002\u0000\u0000\u001f!\u0003\u0006\u0003\u0000"+
		" \u001f\u0001\u0000\u0000\u0000!$\u0001\u0000\u0000\u0000\" \u0001\u0000"+
		"\u0000\u0000\"#\u0001\u0000\u0000\u0000#%\u0001\u0000\u0000\u0000$\"\u0001"+
		"\u0000\u0000\u0000%&\u0005\u0003\u0000\u0000&\u0003\u0001\u0000\u0000"+
		"\u0000\'+\u0005\t\u0000\u0000(*\u0003\f\u0006\u0000)(\u0001\u0000\u0000"+
		"\u0000*-\u0001\u0000\u0000\u0000+)\u0001\u0000\u0000\u0000+,\u0001\u0000"+
		"\u0000\u0000,\u0005\u0001\u0000\u0000\u0000-+\u0001\u0000\u0000\u0000"+
		"./\u0003\n\u0005\u0000/0\u0005J\u0000\u000005\u0001\u0000\u0000\u0000"+
		"12\u0003\b\u0004\u000023\u0005J\u0000\u000035\u0001\u0000\u0000\u0000"+
		"4.\u0001\u0000\u0000\u000041\u0001\u0000\u0000\u00005\u0007\u0001\u0000"+
		"\u0000\u000067\u0005[\u0000\u000078\u0005Q\u0000\u000089\u0003\u0010\b"+
		"\u00009:\u0005R\u0000\u0000:\t\u0001\u0000\u0000\u0000;<\u0005[\u0000"+
		"\u0000<=\u0005L\u0000\u0000=>\u0003\u0010\b\u0000>\u000b\u0001\u0000\u0000"+
		"\u0000?@\u0005[\u0000\u0000@A\u0005K\u0000\u0000AB\u0003\u000e\u0007\u0000"+
		"BC\u0005J\u0000\u0000CK\u0001\u0000\u0000\u0000DE\u0005[\u0000\u0000E"+
		"F\u0005K\u0000\u0000FG\u0003\u000e\u0007\u0000GH\u0007\u0000\u0000\u0000"+
		"HI\u0005J\u0000\u0000IK\u0001\u0000\u0000\u0000J?\u0001\u0000\u0000\u0000"+
		"JD\u0001\u0000\u0000\u0000K\r\u0001\u0000\u0000\u0000LM\u0007\u0001\u0000"+
		"\u0000M\u000f\u0001\u0000\u0000\u0000NS\u0006\b\uffff\uffff\u0000OS\u0005"+
		"]\u0000\u0000PS\u0005\\\u0000\u0000QS\u0005[\u0000\u0000RN\u0001\u0000"+
		"\u0000\u0000RO\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000RQ\u0001"+
		"\u0000\u0000\u0000S\\\u0001\u0000\u0000\u0000TU\n\u0006\u0000\u0000UV"+
		"\u0007\u0002\u0000\u0000V[\u0003\u0010\b\u0007WX\n\u0005\u0000\u0000X"+
		"Y\u0007\u0003\u0000\u0000Y[\u0003\u0010\b\u0006ZT\u0001\u0000\u0000\u0000"+
		"ZW\u0001\u0000\u0000\u0000[^\u0001\u0000\u0000\u0000\\Z\u0001\u0000\u0000"+
		"\u0000\\]\u0001\u0000\u0000\u0000]\u0011\u0001\u0000\u0000\u0000^\\\u0001"+
		"\u0000\u0000\u0000\t\u0016\u0019\"+4JRZ\\";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}