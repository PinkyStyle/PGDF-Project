// Generated from C:/Users/cacac/Documents/CosasU/Proyectos/PropertyGraphDF/IntelliJ/Libreria/PropertyGraphDF\PropertyGraphDF.g4 by ANTLR 4.9.1
package ANTLR4Files;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PropertyGraphDFLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, PROPNAME=11, STRING=12, NUMBER=13, INT=14, FLOAT=15, NL=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "PROPNAME", "STRING", "NUMBER", "INT", "FLOAT", "DIGIT", "NL"
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


	public PropertyGraphDFLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PropertyGraphDF.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\22\u0089\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b"+
		"\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\6\fa\n\f\r\f\16\fb\3\r\3\r\7\rg\n\r"+
		"\f\r\16\rj\13\r\3\r\3\r\3\16\3\16\5\16p\n\16\3\17\6\17s\n\17\r\17\16\17"+
		"t\3\20\6\20x\n\20\r\20\16\20y\3\20\3\20\7\20~\n\20\f\20\16\20\u0081\13"+
		"\20\3\21\3\21\3\22\5\22\u0086\n\22\3\22\3\22\3h\2\23\3\3\5\4\7\5\t\6\13"+
		"\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\2#\22\3\2\4"+
		"\6\2//C\\aac|\3\2\62;\2\u008e\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2#\3\2\2\2\3%\3\2\2\2\5\61\3\2\2\2\7N\3\2\2\2\tP\3\2\2\2\13"+
		"R\3\2\2\2\rT\3\2\2\2\17V\3\2\2\2\21Y\3\2\2\2\23[\3\2\2\2\25]\3\2\2\2\27"+
		"`\3\2\2\2\31d\3\2\2\2\33o\3\2\2\2\35r\3\2\2\2\37w\3\2\2\2!\u0082\3\2\2"+
		"\2#\u0085\3\2\2\2%&\7B\2\2&\'\7k\2\2\'(\7f\2\2()\7=\2\2)*\7B\2\2*+\7n"+
		"\2\2+,\7c\2\2,-\7d\2\2-.\7g\2\2./\7n\2\2/\60\7=\2\2\60\4\3\2\2\2\61\62"+
		"\7B\2\2\62\63\7w\2\2\63\64\7p\2\2\64\65\7f\2\2\65\66\7k\2\2\66\67\7t\2"+
		"\2\678\7g\2\289\7e\2\29:\7v\2\2:;\7g\2\2;<\7f\2\2<=\7=\2\2=>\7B\2\2>?"+
		"\7u\2\2?@\7q\2\2@A\7w\2\2AB\7t\2\2BC\7e\2\2CD\7g\2\2DE\7=\2\2EF\7B\2\2"+
		"FG\7v\2\2GH\7c\2\2HI\7t\2\2IJ\7i\2\2JK\7g\2\2KL\7v\2\2LM\7=\2\2M\6\3\2"+
		"\2\2NO\7=\2\2O\b\3\2\2\2PQ\7]\2\2Q\n\3\2\2\2RS\7.\2\2S\f\3\2\2\2TU\7_"+
		"\2\2U\16\3\2\2\2VW\7]\2\2WX\7_\2\2X\20\3\2\2\2YZ\7V\2\2Z\22\3\2\2\2[\\"+
		"\7H\2\2\\\24\3\2\2\2]^\7P\2\2^\26\3\2\2\2_a\t\2\2\2`_\3\2\2\2ab\3\2\2"+
		"\2b`\3\2\2\2bc\3\2\2\2c\30\3\2\2\2dh\7$\2\2eg\13\2\2\2fe\3\2\2\2gj\3\2"+
		"\2\2hi\3\2\2\2hf\3\2\2\2ik\3\2\2\2jh\3\2\2\2kl\7$\2\2l\32\3\2\2\2mp\5"+
		"\35\17\2np\5\37\20\2om\3\2\2\2on\3\2\2\2p\34\3\2\2\2qs\5!\21\2rq\3\2\2"+
		"\2st\3\2\2\2tr\3\2\2\2tu\3\2\2\2u\36\3\2\2\2vx\5!\21\2wv\3\2\2\2xy\3\2"+
		"\2\2yw\3\2\2\2yz\3\2\2\2z{\3\2\2\2{\177\7\60\2\2|~\5!\21\2}|\3\2\2\2~"+
		"\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080 \3\2\2\2\u0081\177"+
		"\3\2\2\2\u0082\u0083\t\3\2\2\u0083\"\3\2\2\2\u0084\u0086\7\17\2\2\u0085"+
		"\u0084\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0088\7\f"+
		"\2\2\u0088$\3\2\2\2\n\2bhoty\177\u0085\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}