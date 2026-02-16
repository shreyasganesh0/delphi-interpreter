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
		INLINE=43, PROPERTY=44, ASM=45, FILE=46, LIBRARY=47, SET=48, EXPORTS=49, 
		SHL=50, SHR=51, NIL=52, PACKED=53, THREADVAR=54, FINALLY=55, PLUS=56, 
		MINUS=57, SLASH=58, CARAT=59, EQUAL=60, INEQ=61, GREATER=62, GREATEREQ=63, 
		LESSER=64, LESSEREQ=65, POUND=66, DOLLAR=67, AMPERSAND=68, AT=69, SEMI=70, 
		ASSIGN=71, DOT=72, COMMA=73, CURLO=74, CURLC=75, CIRCO=76, CIRCC=77, CIRCDOTO=78, 
		CIRCDOTC=79, CIRCSTARO=80, CIRCSTARC=81, SQUAREO=82, SQUAREC=83, STAR=84, 
		QOUTE=85, ID=86, INT=87;
	public static final int
		RULE_programRule = 0, RULE_block = 1, RULE_statement = 2, RULE_assignment = 3, 
		RULE_expr = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"programRule", "block", "statement", "assignment", "expr"
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
			"'asm'", "'file'", "'library'", "'set'", "'exports'", "'shl'", "'shr'", 
			"'nil'", "'packed'", "'threadvar'", "'finally'", "'+'", "'-'", "'/'", 
			"'^'", "'='", "'<>'", "'>'", "'>='", "'<'", "'<='", "'#'", "'$'", "'&'", 
			"'@'", "';'", "':='", "'.'", "','", "'{'", "'}'", "'('", "')'", "'(.'", 
			"'.)'", "'(*'", "'*)'", "'['", "']'", "'*'", "'''"
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
			"CONSTRUCTOR", "DESTRUCTOR", "INLINE", "PROPERTY", "ASM", "FILE", "LIBRARY", 
			"SET", "EXPORTS", "SHL", "SHR", "NIL", "PACKED", "THREADVAR", "FINALLY", 
			"PLUS", "MINUS", "SLASH", "CARAT", "EQUAL", "INEQ", "GREATER", "GREATEREQ", 
			"LESSER", "LESSEREQ", "POUND", "DOLLAR", "AMPERSAND", "AT", "SEMI", "ASSIGN", 
			"DOT", "COMMA", "CURLO", "CURLC", "CIRCO", "CIRCC", "CIRCDOTO", "CIRCDOTC", 
			"CIRCSTARO", "CIRCSTARC", "SQUAREO", "SQUAREC", "STAR", "QOUTE", "ID", 
			"INT"
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
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode DOT() { return getToken(delphiParser.DOT, 0); }
		public TerminalNode EOF() { return getToken(delphiParser.EOF, 0); }
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10);
			match(PROGRAM);
			setState(11);
			match(ID);
			setState(12);
			match(SEMI);
			setState(13);
			block();
			setState(14);
			match(DOT);
			setState(15);
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
			setState(17);
			match(BEGIN);
			setState(21);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(18);
				statement();
				}
				}
				setState(23);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(24);
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
	public static class StatementContext extends ParserRuleContext {
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(delphiParser.SEMI, 0); }
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
		enterRule(_localctx, 4, RULE_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			assignment();
			setState(27);
			match(SEMI);
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
		enterRule(_localctx, 6, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			match(ID);
			setState(30);
			match(ASSIGN);
			setState(31);
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
	public static class ExprContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(delphiParser.INT, 0); }
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
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(36);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				{
				setState(34);
				match(INT);
				}
				break;
			case ID:
				{
				setState(35);
				match(ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(46);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(44);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(38);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(39);
						_la = _input.LA(1);
						if ( !(_la==SLASH || _la==STAR) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(40);
						expr(5);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(41);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(42);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(43);
						expr(4);
						}
						break;
					}
					} 
				}
				setState(48);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
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
		case 4:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001W2\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002"+
		"\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0005\u0001\u0014\b\u0001\n\u0001\f\u0001\u0017"+
		"\t\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u0004%\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0005\u0004-\b\u0004\n\u0004\f\u00040\t"+
		"\u0004\u0001\u0004\u0000\u0001\b\u0005\u0000\u0002\u0004\u0006\b\u0000"+
		"\u0002\u0002\u0000::TT\u0001\u0000890\u0000\n\u0001\u0000\u0000\u0000"+
		"\u0002\u0011\u0001\u0000\u0000\u0000\u0004\u001a\u0001\u0000\u0000\u0000"+
		"\u0006\u001d\u0001\u0000\u0000\u0000\b$\u0001\u0000\u0000\u0000\n\u000b"+
		"\u0005\u0001\u0000\u0000\u000b\f\u0005V\u0000\u0000\f\r\u0005F\u0000\u0000"+
		"\r\u000e\u0003\u0002\u0001\u0000\u000e\u000f\u0005H\u0000\u0000\u000f"+
		"\u0010\u0005\u0000\u0000\u0001\u0010\u0001\u0001\u0000\u0000\u0000\u0011"+
		"\u0015\u0005\u0002\u0000\u0000\u0012\u0014\u0003\u0004\u0002\u0000\u0013"+
		"\u0012\u0001\u0000\u0000\u0000\u0014\u0017\u0001\u0000\u0000\u0000\u0015"+
		"\u0013\u0001\u0000\u0000\u0000\u0015\u0016\u0001\u0000\u0000\u0000\u0016"+
		"\u0018\u0001\u0000\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0018"+
		"\u0019\u0005\u0003\u0000\u0000\u0019\u0003\u0001\u0000\u0000\u0000\u001a"+
		"\u001b\u0003\u0006\u0003\u0000\u001b\u001c\u0005F\u0000\u0000\u001c\u0005"+
		"\u0001\u0000\u0000\u0000\u001d\u001e\u0005V\u0000\u0000\u001e\u001f\u0005"+
		"G\u0000\u0000\u001f \u0003\b\u0004\u0000 \u0007\u0001\u0000\u0000\u0000"+
		"!\"\u0006\u0004\uffff\uffff\u0000\"%\u0005W\u0000\u0000#%\u0005V\u0000"+
		"\u0000$!\u0001\u0000\u0000\u0000$#\u0001\u0000\u0000\u0000%.\u0001\u0000"+
		"\u0000\u0000&\'\n\u0004\u0000\u0000\'(\u0007\u0000\u0000\u0000(-\u0003"+
		"\b\u0004\u0005)*\n\u0003\u0000\u0000*+\u0007\u0001\u0000\u0000+-\u0003"+
		"\b\u0004\u0004,&\u0001\u0000\u0000\u0000,)\u0001\u0000\u0000\u0000-0\u0001"+
		"\u0000\u0000\u0000.,\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000"+
		"/\t\u0001\u0000\u0000\u00000.\u0001\u0000\u0000\u0000\u0004\u0015$,.";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}