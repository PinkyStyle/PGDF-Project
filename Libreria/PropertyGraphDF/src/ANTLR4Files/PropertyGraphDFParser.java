// Generated from C:/Users/cacac/Documents/CosasU/Proyectos/PropertyGraphDF/IntelliJ/Libreria/PropertyGraphDF\PropertyGraphDF.g4 by ANTLR 4.9.1
package ANTLR4Files;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PropertyGraphDFParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, PROPNAME=11, STRING=12, NUMBER=13, INT=14, FLOAT=15, NL=16;
	public static final int
		RULE_file = 0, RULE_nf = 1, RULE_ff = 2, RULE_nodeProp = 3, RULE_standardProps = 4, 
		RULE_newPropN = 5, RULE_newPropE = 6, RULE_declaration = 7, RULE_label = 8, 
		RULE_value = 9, RULE_list = 10, RULE_booleanValue = 11, RULE_nullValue = 12;
	private static String[] makeRuleNames() {
		return new String[] {
			"file", "nf", "ff", "nodeProp", "standardProps", "newPropN", "newPropE", 
			"declaration", "label", "value", "list", "booleanValue", "nullValue"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'@id;@label;'", "'@undirected;@source;@target;'", "';'", "'['", 
			"','", "']'", "'[]'", "'T'", "'F'", "'N'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "PROPNAME", 
			"STRING", "NUMBER", "INT", "FLOAT", "NL"
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
	public String getGrammarFileName() { return "PropertyGraphDF.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PropertyGraphDFParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class FileContext extends ParserRuleContext {
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
	 
		public FileContext() { }
		public void copyFrom(FileContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FullFileContext extends FileContext {
		public FfContext ff() {
			return getRuleContext(FfContext.class,0);
		}
		public FullFileContext(FileContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitFullFile(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NodeFileContext extends FileContext {
		public NfContext nf() {
			return getRuleContext(NfContext.class,0);
		}
		public NodeFileContext(FileContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitNodeFile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		try {
			setState(28);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				_localctx = new NodeFileContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(26);
				nf();
				}
				break;
			case 2:
				_localctx = new FullFileContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(27);
				ff();
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

	public static class NfContext extends ParserRuleContext {
		public NodePropContext nodeProp() {
			return getRuleContext(NodePropContext.class,0);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<TerminalNode> NL() { return getTokens(PropertyGraphDFParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(PropertyGraphDFParser.NL, i);
		}
		public NfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nf; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitNf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NfContext nf() throws RecognitionException {
		NfContext _localctx = new NfContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_nf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			nodeProp();
			setState(36);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==STRING) {
				{
				{
				setState(31);
				declaration();
				setState(32);
				match(NL);
				}
				}
				setState(38);
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

	public static class FfContext extends ParserRuleContext {
		public StandardPropsContext standardProps() {
			return getRuleContext(StandardPropsContext.class,0);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<TerminalNode> NL() { return getTokens(PropertyGraphDFParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(PropertyGraphDFParser.NL, i);
		}
		public FfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ff; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitFf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FfContext ff() throws RecognitionException {
		FfContext _localctx = new FfContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_ff);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			standardProps();
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==STRING) {
				{
				{
				setState(40);
				declaration();
				setState(41);
				match(NL);
				}
				}
				setState(47);
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

	public static class NodePropContext extends ParserRuleContext {
		public TerminalNode NL() { return getToken(PropertyGraphDFParser.NL, 0); }
		public List<NewPropNContext> newPropN() {
			return getRuleContexts(NewPropNContext.class);
		}
		public NewPropNContext newPropN(int i) {
			return getRuleContext(NewPropNContext.class,i);
		}
		public NodePropContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodeProp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitNodeProp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodePropContext nodeProp() throws RecognitionException {
		NodePropContext _localctx = new NodePropContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_nodeProp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(T__0);
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PROPNAME) {
				{
				{
				setState(49);
				newPropN();
				}
				}
				setState(54);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(55);
			match(NL);
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

	public static class StandardPropsContext extends ParserRuleContext {
		public TerminalNode NL() { return getToken(PropertyGraphDFParser.NL, 0); }
		public List<NewPropNContext> newPropN() {
			return getRuleContexts(NewPropNContext.class);
		}
		public NewPropNContext newPropN(int i) {
			return getRuleContext(NewPropNContext.class,i);
		}
		public List<NewPropEContext> newPropE() {
			return getRuleContexts(NewPropEContext.class);
		}
		public NewPropEContext newPropE(int i) {
			return getRuleContext(NewPropEContext.class,i);
		}
		public StandardPropsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_standardProps; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitStandardProps(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StandardPropsContext standardProps() throws RecognitionException {
		StandardPropsContext _localctx = new StandardPropsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_standardProps);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			match(T__0);
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PROPNAME) {
				{
				{
				setState(58);
				newPropN();
				}
				}
				setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(64);
			match(T__1);
			setState(68);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PROPNAME) {
				{
				{
				setState(65);
				newPropE();
				}
				}
				setState(70);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(71);
			match(NL);
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

	public static class NewPropNContext extends ParserRuleContext {
		public TerminalNode PROPNAME() { return getToken(PropertyGraphDFParser.PROPNAME, 0); }
		public NewPropNContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newPropN; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitNewPropN(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewPropNContext newPropN() throws RecognitionException {
		NewPropNContext _localctx = new NewPropNContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_newPropN);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			match(PROPNAME);
			setState(74);
			match(T__2);
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

	public static class NewPropEContext extends ParserRuleContext {
		public TerminalNode PROPNAME() { return getToken(PropertyGraphDFParser.PROPNAME, 0); }
		public NewPropEContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newPropE; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitNewPropE(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewPropEContext newPropE() throws RecognitionException {
		NewPropEContext _localctx = new NewPropEContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_newPropE);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			match(PROPNAME);
			setState(77);
			match(T__2);
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

	public static class DeclarationContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(PropertyGraphDFParser.STRING, 0); }
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_declaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			match(STRING);
			setState(80);
			match(T__2);
			setState(81);
			label();
			setState(82);
			match(T__2);
			setState(88);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << STRING) | (1L << NUMBER))) != 0)) {
				{
				{
				setState(83);
				value();
				setState(84);
				match(T__2);
				}
				}
				setState(90);
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

	public static class LabelContext extends ParserRuleContext {
		public List<TerminalNode> STRING() { return getTokens(PropertyGraphDFParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(PropertyGraphDFParser.STRING, i);
		}
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_label);
		int _la;
		try {
			setState(102);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(91);
				match(T__3);
				setState(92);
				match(STRING);
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(93);
					match(T__4);
					setState(94);
					match(STRING);
					}
					}
					setState(99);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(100);
				match(T__5);
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(101);
				match(T__6);
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

	public static class ValueContext extends ParserRuleContext {
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
	 
		public ValueContext() { }
		public void copyFrom(ValueContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NullContext extends ValueContext {
		public NullValueContext nullValue() {
			return getRuleContext(NullValueContext.class,0);
		}
		public NullContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitNull(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberContext extends ValueContext {
		public TerminalNode NUMBER() { return getToken(PropertyGraphDFParser.NUMBER, 0); }
		public NumberContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ListValueContext extends ValueContext {
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public ListValueContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitListValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringContext extends ValueContext {
		public TerminalNode STRING() { return getToken(PropertyGraphDFParser.STRING, 0); }
		public StringContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanContext extends ValueContext {
		public BooleanValueContext booleanValue() {
			return getRuleContext(BooleanValueContext.class,0);
		}
		public BooleanContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NothingContext extends ValueContext {
		public NothingContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitNothing(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_value);
		try {
			setState(110);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				_localctx = new StringContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(104);
				match(STRING);
				}
				break;
			case T__7:
			case T__8:
				_localctx = new BooleanContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(105);
				booleanValue();
				}
				break;
			case NUMBER:
				_localctx = new NumberContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(106);
				match(NUMBER);
				}
				break;
			case T__9:
				_localctx = new NullContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(107);
				nullValue();
				}
				break;
			case T__3:
			case T__6:
				_localctx = new ListValueContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(108);
				list();
				}
				break;
			case T__2:
				_localctx = new NothingContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
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

	public static class ListContext extends ParserRuleContext {
		public List<TerminalNode> STRING() { return getTokens(PropertyGraphDFParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(PropertyGraphDFParser.STRING, i);
		}
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_list);
		int _la;
		try {
			setState(123);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				match(T__3);
				setState(113);
				match(STRING);
				setState(118);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(114);
					match(T__4);
					setState(115);
					match(STRING);
					}
					}
					setState(120);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(121);
				match(T__5);
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(122);
				match(T__6);
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

	public static class BooleanValueContext extends ParserRuleContext {
		public BooleanValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanValue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitBooleanValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanValueContext booleanValue() throws RecognitionException {
		BooleanValueContext _localctx = new BooleanValueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_booleanValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			_la = _input.LA(1);
			if ( !(_la==T__7 || _la==T__8) ) {
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

	public static class NullValueContext extends ParserRuleContext {
		public NullValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nullValue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PropertyGraphDFVisitor ) return ((PropertyGraphDFVisitor<? extends T>)visitor).visitNullValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NullValueContext nullValue() throws RecognitionException {
		NullValueContext _localctx = new NullValueContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_nullValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			match(T__9);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\22\u0084\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\5\2\37\n\2\3\3\3\3\3\3\3\3\7\3"+
		"%\n\3\f\3\16\3(\13\3\3\4\3\4\3\4\3\4\7\4.\n\4\f\4\16\4\61\13\4\3\5\3\5"+
		"\7\5\65\n\5\f\5\16\58\13\5\3\5\3\5\3\6\3\6\7\6>\n\6\f\6\16\6A\13\6\3\6"+
		"\3\6\7\6E\n\6\f\6\16\6H\13\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\7\tY\n\t\f\t\16\t\\\13\t\3\n\3\n\3\n\3\n\7\nb\n\n\f"+
		"\n\16\ne\13\n\3\n\3\n\5\ni\n\n\3\13\3\13\3\13\3\13\3\13\3\13\5\13q\n\13"+
		"\3\f\3\f\3\f\3\f\7\fw\n\f\f\f\16\fz\13\f\3\f\3\f\5\f~\n\f\3\r\3\r\3\16"+
		"\3\16\3\16\2\2\17\2\4\6\b\n\f\16\20\22\24\26\30\32\2\3\3\2\n\13\2\u0086"+
		"\2\36\3\2\2\2\4 \3\2\2\2\6)\3\2\2\2\b\62\3\2\2\2\n;\3\2\2\2\fK\3\2\2\2"+
		"\16N\3\2\2\2\20Q\3\2\2\2\22h\3\2\2\2\24p\3\2\2\2\26}\3\2\2\2\30\177\3"+
		"\2\2\2\32\u0081\3\2\2\2\34\37\5\4\3\2\35\37\5\6\4\2\36\34\3\2\2\2\36\35"+
		"\3\2\2\2\37\3\3\2\2\2 &\5\b\5\2!\"\5\20\t\2\"#\7\22\2\2#%\3\2\2\2$!\3"+
		"\2\2\2%(\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\'\5\3\2\2\2(&\3\2\2\2)/\5\n\6\2"+
		"*+\5\20\t\2+,\7\22\2\2,.\3\2\2\2-*\3\2\2\2.\61\3\2\2\2/-\3\2\2\2/\60\3"+
		"\2\2\2\60\7\3\2\2\2\61/\3\2\2\2\62\66\7\3\2\2\63\65\5\f\7\2\64\63\3\2"+
		"\2\2\658\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\679\3\2\2\28\66\3\2\2\29"+
		":\7\22\2\2:\t\3\2\2\2;?\7\3\2\2<>\5\f\7\2=<\3\2\2\2>A\3\2\2\2?=\3\2\2"+
		"\2?@\3\2\2\2@B\3\2\2\2A?\3\2\2\2BF\7\4\2\2CE\5\16\b\2DC\3\2\2\2EH\3\2"+
		"\2\2FD\3\2\2\2FG\3\2\2\2GI\3\2\2\2HF\3\2\2\2IJ\7\22\2\2J\13\3\2\2\2KL"+
		"\7\r\2\2LM\7\5\2\2M\r\3\2\2\2NO\7\r\2\2OP\7\5\2\2P\17\3\2\2\2QR\7\16\2"+
		"\2RS\7\5\2\2ST\5\22\n\2TZ\7\5\2\2UV\5\24\13\2VW\7\5\2\2WY\3\2\2\2XU\3"+
		"\2\2\2Y\\\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2[\21\3\2\2\2\\Z\3\2\2\2]^\7\6\2\2"+
		"^c\7\16\2\2_`\7\7\2\2`b\7\16\2\2a_\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2"+
		"\2df\3\2\2\2ec\3\2\2\2fi\7\b\2\2gi\7\t\2\2h]\3\2\2\2hg\3\2\2\2i\23\3\2"+
		"\2\2jq\7\16\2\2kq\5\30\r\2lq\7\17\2\2mq\5\32\16\2nq\5\26\f\2oq\3\2\2\2"+
		"pj\3\2\2\2pk\3\2\2\2pl\3\2\2\2pm\3\2\2\2pn\3\2\2\2po\3\2\2\2q\25\3\2\2"+
		"\2rs\7\6\2\2sx\7\16\2\2tu\7\7\2\2uw\7\16\2\2vt\3\2\2\2wz\3\2\2\2xv\3\2"+
		"\2\2xy\3\2\2\2y{\3\2\2\2zx\3\2\2\2{~\7\b\2\2|~\7\t\2\2}r\3\2\2\2}|\3\2"+
		"\2\2~\27\3\2\2\2\177\u0080\t\2\2\2\u0080\31\3\2\2\2\u0081\u0082\7\f\2"+
		"\2\u0082\33\3\2\2\2\16\36&/\66?FZchpx}";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}