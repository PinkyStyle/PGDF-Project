// Generated from C:/Users/cacac/Documents/CosasU/Proyectos/PropertyGraphDF/IntelliJ/Libreria/PropertyGraphDF\PropertyGraphDF.g4 by ANTLR 4.9.1
package ANTLR4Files;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PropertyGraphDFParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PropertyGraphDFVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code NodeFile}
	 * labeled alternative in {@link PropertyGraphDFParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeFile(PropertyGraphDFParser.NodeFileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FullFile}
	 * labeled alternative in {@link PropertyGraphDFParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullFile(PropertyGraphDFParser.FullFileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#nf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNf(PropertyGraphDFParser.NfContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#ff}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFf(PropertyGraphDFParser.FfContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#nodeProp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeProp(PropertyGraphDFParser.NodePropContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#standardProps}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandardProps(PropertyGraphDFParser.StandardPropsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#newPropN}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewPropN(PropertyGraphDFParser.NewPropNContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#newPropE}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewPropE(PropertyGraphDFParser.NewPropEContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(PropertyGraphDFParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(PropertyGraphDFParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link PropertyGraphDFParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(PropertyGraphDFParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link PropertyGraphDFParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(PropertyGraphDFParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link PropertyGraphDFParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(PropertyGraphDFParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Null}
	 * labeled alternative in {@link PropertyGraphDFParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull(PropertyGraphDFParser.NullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListValue}
	 * labeled alternative in {@link PropertyGraphDFParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListValue(PropertyGraphDFParser.ListValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Nothing}
	 * labeled alternative in {@link PropertyGraphDFParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNothing(PropertyGraphDFParser.NothingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(PropertyGraphDFParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#booleanValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanValue(PropertyGraphDFParser.BooleanValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PropertyGraphDFParser#nullValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullValue(PropertyGraphDFParser.NullValueContext ctx);
}