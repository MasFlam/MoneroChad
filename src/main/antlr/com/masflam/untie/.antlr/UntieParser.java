// Generated from /home/masflam/coding/java/monerochad/src/main/antlr/com/masflam/untie/Untie.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class UntieParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PLUS=1, MINUS=2, MULT=3, DIV=4, POW=5, LPAREN=6, RPAREN=7, EQ=8, WHERE=9, 
		NUMBER=10, SYMBOL=11, WS=12;
	public static final int
		RULE_input = 0, RULE_expr = 1, RULE_where = 2, RULE_plusMinus = 3, RULE_multDiv = 4, 
		RULE_unaryPlusMinus = 5, RULE_pow = 6, RULE_atom = 7, RULE_literal = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"input", "expr", "where", "plusMinus", "multDiv", "unaryPlusMinus", "pow", 
			"atom", "literal"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'+'", "'-'", "'*'", "'/'", "'^'", "'('", "')'", "'='", "'where'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PLUS", "MINUS", "MULT", "DIV", "POW", "LPAREN", "RPAREN", "EQ", 
			"WHERE", "NUMBER", "SYMBOL", "WS"
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
	public String getGrammarFileName() { return "Untie.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public UntieParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class InputContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(UntieParser.EOF, 0); }
		public InputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input; }
	}

	public final InputContext input() throws RecognitionException {
		InputContext _localctx = new InputContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			expr();
			setState(19);
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

	public static class ExprContext extends ParserRuleContext {
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21);
			where(0);
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

	public static class WhereContext extends ParserRuleContext {
		public PlusMinusContext plusMinus() {
			return getRuleContext(PlusMinusContext.class,0);
		}
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public TerminalNode WHERE() { return getToken(UntieParser.WHERE, 0); }
		public TerminalNode SYMBOL() { return getToken(UntieParser.SYMBOL, 0); }
		public TerminalNode EQ() { return getToken(UntieParser.EQ, 0); }
		public WhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_where; }
	}

	public final WhereContext where() throws RecognitionException {
		return where(0);
	}

	private WhereContext where(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		WhereContext _localctx = new WhereContext(_ctx, _parentState);
		WhereContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_where, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(24);
			plusMinus(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(33);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new WhereContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_where);
					setState(26);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(27);
					match(WHERE);
					setState(28);
					match(SYMBOL);
					setState(29);
					match(EQ);
					setState(30);
					plusMinus(0);
					}
					} 
				}
				setState(35);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
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

	public static class PlusMinusContext extends ParserRuleContext {
		public Token PLUS;
		public List<Token> ops = new ArrayList<Token>();
		public Token MINUS;
		public Token _tset29;
		public List<MultDivContext> multDiv() {
			return getRuleContexts(MultDivContext.class);
		}
		public MultDivContext multDiv(int i) {
			return getRuleContext(MultDivContext.class,i);
		}
		public PlusMinusContext plusMinus() {
			return getRuleContext(PlusMinusContext.class,0);
		}
		public List<TerminalNode> PLUS() { return getTokens(UntieParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(UntieParser.PLUS, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(UntieParser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(UntieParser.MINUS, i);
		}
		public PlusMinusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plusMinus; }
	}

	public final PlusMinusContext plusMinus() throws RecognitionException {
		return plusMinus(0);
	}

	private PlusMinusContext plusMinus(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PlusMinusContext _localctx = new PlusMinusContext(_ctx, _parentState);
		PlusMinusContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_plusMinus, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(37);
			multDiv(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(48);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PlusMinusContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_plusMinus);
					setState(39);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(42); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(40);
							((PlusMinusContext)_localctx)._tset29 = _input.LT(1);
							_la = _input.LA(1);
							if ( !(_la==PLUS || _la==MINUS) ) {
								((PlusMinusContext)_localctx)._tset29 = (Token)_errHandler.recoverInline(this);
							}
							else {
								if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
								_errHandler.reportMatch(this);
								consume();
							}
							((PlusMinusContext)_localctx).ops.add(((PlusMinusContext)_localctx)._tset29);
							setState(41);
							multDiv(0);
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(44); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					} 
				}
				setState(50);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
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

	public static class MultDivContext extends ParserRuleContext {
		public Token MULT;
		public List<Token> ops = new ArrayList<Token>();
		public Token DIV;
		public Token _tset29;
		public List<UnaryPlusMinusContext> unaryPlusMinus() {
			return getRuleContexts(UnaryPlusMinusContext.class);
		}
		public UnaryPlusMinusContext unaryPlusMinus(int i) {
			return getRuleContext(UnaryPlusMinusContext.class,i);
		}
		public MultDivContext multDiv() {
			return getRuleContext(MultDivContext.class,0);
		}
		public List<TerminalNode> MULT() { return getTokens(UntieParser.MULT); }
		public TerminalNode MULT(int i) {
			return getToken(UntieParser.MULT, i);
		}
		public List<TerminalNode> DIV() { return getTokens(UntieParser.DIV); }
		public TerminalNode DIV(int i) {
			return getToken(UntieParser.DIV, i);
		}
		public MultDivContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multDiv; }
	}

	public final MultDivContext multDiv() throws RecognitionException {
		return multDiv(0);
	}

	private MultDivContext multDiv(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		MultDivContext _localctx = new MultDivContext(_ctx, _parentState);
		MultDivContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_multDiv, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(52);
			unaryPlusMinus();
			}
			_ctx.stop = _input.LT(-1);
			setState(63);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new MultDivContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_multDiv);
					setState(54);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(57); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(55);
							((MultDivContext)_localctx)._tset29 = _input.LT(1);
							_la = _input.LA(1);
							if ( !(_la==MULT || _la==DIV) ) {
								((MultDivContext)_localctx)._tset29 = (Token)_errHandler.recoverInline(this);
							}
							else {
								if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
								_errHandler.reportMatch(this);
								consume();
							}
							((MultDivContext)_localctx).ops.add(((MultDivContext)_localctx)._tset29);
							setState(56);
							unaryPlusMinus();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(59); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					} 
				}
				setState(65);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
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

	public static class UnaryPlusMinusContext extends ParserRuleContext {
		public Token op;
		public PowContext pow() {
			return getRuleContext(PowContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(UntieParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(UntieParser.MINUS, 0); }
		public UnaryPlusMinusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryPlusMinus; }
	}

	public final UnaryPlusMinusContext unaryPlusMinus() throws RecognitionException {
		UnaryPlusMinusContext _localctx = new UnaryPlusMinusContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_unaryPlusMinus);
		int _la;
		try {
			setState(69);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PLUS:
			case MINUS:
				enterOuterAlt(_localctx, 1);
				{
				setState(66);
				((UnaryPlusMinusContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
					((UnaryPlusMinusContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(67);
				pow(0);
				}
				break;
			case LPAREN:
			case NUMBER:
			case SYMBOL:
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				pow(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class PowContext extends ParserRuleContext {
		public List<AtomContext> atom() {
			return getRuleContexts(AtomContext.class);
		}
		public AtomContext atom(int i) {
			return getRuleContext(AtomContext.class,i);
		}
		public PowContext pow() {
			return getRuleContext(PowContext.class,0);
		}
		public List<TerminalNode> POW() { return getTokens(UntieParser.POW); }
		public TerminalNode POW(int i) {
			return getToken(UntieParser.POW, i);
		}
		public PowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pow; }
	}

	public final PowContext pow() throws RecognitionException {
		return pow(0);
	}

	private PowContext pow(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PowContext _localctx = new PowContext(_ctx, _parentState);
		PowContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_pow, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(72);
			atom();
			}
			_ctx.stop = _input.LT(-1);
			setState(83);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PowContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_pow);
					setState(74);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(77); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(75);
							match(POW);
							setState(76);
							atom();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(79); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					} 
				}
				setState(85);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
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

	public static class AtomContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(UntieParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(UntieParser.RPAREN, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_atom);
		try {
			setState(91);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(86);
				match(LPAREN);
				setState(87);
				expr();
				setState(88);
				match(RPAREN);
				}
				break;
			case NUMBER:
			case SYMBOL:
				enterOuterAlt(_localctx, 2);
				{
				setState(90);
				literal();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode SYMBOL() { return getToken(UntieParser.SYMBOL, 0); }
		public TerminalNode NUMBER() { return getToken(UntieParser.NUMBER, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			_la = _input.LA(1);
			if ( !(_la==NUMBER || _la==SYMBOL) ) {
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return where_sempred((WhereContext)_localctx, predIndex);
		case 3:
			return plusMinus_sempred((PlusMinusContext)_localctx, predIndex);
		case 4:
			return multDiv_sempred((MultDivContext)_localctx, predIndex);
		case 6:
			return pow_sempred((PowContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean where_sempred(WhereContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean plusMinus_sempred(PlusMinusContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean multDiv_sempred(MultDivContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean pow_sempred(PowContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\16b\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2\3\2"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4\"\n\4\f\4\16\4%\13\4\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\6\5-\n\5\r\5\16\5.\7\5\61\n\5\f\5\16\5\64\13\5\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\6\6<\n\6\r\6\16\6=\7\6@\n\6\f\6\16\6C\13\6\3\7"+
		"\3\7\3\7\5\7H\n\7\3\b\3\b\3\b\3\b\3\b\3\b\6\bP\n\b\r\b\16\bQ\7\bT\n\b"+
		"\f\b\16\bW\13\b\3\t\3\t\3\t\3\t\3\t\5\t^\n\t\3\n\3\n\3\n\2\6\6\b\n\16"+
		"\13\2\4\6\b\n\f\16\20\22\2\5\3\2\3\4\3\2\5\6\3\2\f\r\2a\2\24\3\2\2\2\4"+
		"\27\3\2\2\2\6\31\3\2\2\2\b&\3\2\2\2\n\65\3\2\2\2\fG\3\2\2\2\16I\3\2\2"+
		"\2\20]\3\2\2\2\22_\3\2\2\2\24\25\5\4\3\2\25\26\7\2\2\3\26\3\3\2\2\2\27"+
		"\30\5\6\4\2\30\5\3\2\2\2\31\32\b\4\1\2\32\33\5\b\5\2\33#\3\2\2\2\34\35"+
		"\f\4\2\2\35\36\7\13\2\2\36\37\7\r\2\2\37 \7\n\2\2 \"\5\b\5\2!\34\3\2\2"+
		"\2\"%\3\2\2\2#!\3\2\2\2#$\3\2\2\2$\7\3\2\2\2%#\3\2\2\2&\'\b\5\1\2\'(\5"+
		"\n\6\2(\62\3\2\2\2),\f\4\2\2*+\t\2\2\2+-\5\n\6\2,*\3\2\2\2-.\3\2\2\2."+
		",\3\2\2\2./\3\2\2\2/\61\3\2\2\2\60)\3\2\2\2\61\64\3\2\2\2\62\60\3\2\2"+
		"\2\62\63\3\2\2\2\63\t\3\2\2\2\64\62\3\2\2\2\65\66\b\6\1\2\66\67\5\f\7"+
		"\2\67A\3\2\2\28;\f\4\2\29:\t\3\2\2:<\5\f\7\2;9\3\2\2\2<=\3\2\2\2=;\3\2"+
		"\2\2=>\3\2\2\2>@\3\2\2\2?8\3\2\2\2@C\3\2\2\2A?\3\2\2\2AB\3\2\2\2B\13\3"+
		"\2\2\2CA\3\2\2\2DE\t\2\2\2EH\5\16\b\2FH\5\16\b\2GD\3\2\2\2GF\3\2\2\2H"+
		"\r\3\2\2\2IJ\b\b\1\2JK\5\20\t\2KU\3\2\2\2LO\f\4\2\2MN\7\7\2\2NP\5\20\t"+
		"\2OM\3\2\2\2PQ\3\2\2\2QO\3\2\2\2QR\3\2\2\2RT\3\2\2\2SL\3\2\2\2TW\3\2\2"+
		"\2US\3\2\2\2UV\3\2\2\2V\17\3\2\2\2WU\3\2\2\2XY\7\b\2\2YZ\5\4\3\2Z[\7\t"+
		"\2\2[^\3\2\2\2\\^\5\22\n\2]X\3\2\2\2]\\\3\2\2\2^\21\3\2\2\2_`\t\4\2\2"+
		"`\23\3\2\2\2\13#.\62=AGQU]";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}