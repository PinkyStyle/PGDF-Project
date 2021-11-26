package ANTLR4Files;

import PropertyGraphDF.Edge;
import PropertyGraphDF.Node;
import PropertyGraphDF.Property;
import java.util.ArrayList;
import java.util.List;

public class GraphGenerator extends PropertyGraphDFBaseVisitor{


    private List<Node> nodes;
    private List<Edge> edges;
    private List<String> nodeProperties;
    private List<String> edgeProperties;

    public ArrayList getNodeProperties(){
        return (ArrayList) this.nodeProperties;
    }

    public ArrayList getNodes(){
        return (ArrayList) this.nodes;
    }

    public ArrayList getEdgeProperties(){
        return (ArrayList) this.edgeProperties;
    }

    public ArrayList getEdges(){
        return (ArrayList) this.edges;
    }

    @Override
    public Object visitNodeFile(PropertyGraphDFParser.NodeFileContext ctx) {
        this.nodes = new ArrayList<>();
        this.nodeProperties = new ArrayList<>();
        return super.visitNodeFile(ctx);
    }

    @Override
    public Object visitFullFile(PropertyGraphDFParser.FullFileContext ctx) {
        this.nodes = new ArrayList<>();
        this.nodeProperties = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.edgeProperties = new ArrayList<>();
        return super.visitFullFile(ctx);
    }

    @Override
    public Object visitFf(PropertyGraphDFParser.FfContext ctx) {
        return super.visitFf(ctx);
    }

    @Override
    public Object visitNodeProp(PropertyGraphDFParser.NodePropContext ctx) {
        this.nodeProperties = new ArrayList<>();
        for(int i = 0; i < ctx.newPropN().size(); i++){
            String prop = (String) visit(ctx.newPropN(i));
            this.nodeProperties.add(prop);
        }
        return super.visitNodeProp(ctx);
    }

    @Override
    public Object visitStandardProps(PropertyGraphDFParser.StandardPropsContext ctx) {
        this.nodeProperties = new ArrayList<>();
        this.edgeProperties = new ArrayList<>();
        for(int i = 0; i < ctx.newPropN().size(); i++){
            String prop = (String) visit(ctx.newPropN(i));
            this.nodeProperties.add(prop);
        }
        for(int i = 0; i < ctx.newPropE().size(); i++){
            String prop = (String) visit(ctx.newPropE(i));
            this.edgeProperties.add(prop);
        }
        return super.visitStandardProps(ctx);
    }

    @Override
    public String visitNewPropN(PropertyGraphDFParser.NewPropNContext ctx) {
        return ctx.PROPNAME().getText();
    }

    @Override
    public String visitNewPropE(PropertyGraphDFParser.NewPropEContext ctx) {
        return ctx.PROPNAME().getText();
    }

    @Override
    public Object visitDeclaration(PropertyGraphDFParser.DeclarationContext ctx) {
        String id = ctx.STRING().getText();
        id = id.substring(1,id.length()-1);
        ArrayList<String> labels = visitLabel(ctx.label());
        if(ctx.value().size() > this.nodeProperties.size()){
            //Edge
            String source = (String) visit(ctx.value(this.nodeProperties.size()+1));
            String target = (String) visit(ctx.value(this.nodeProperties.size()+2));
            Edge edge = new Edge(id,source,target);
            edge.setUndirected((Boolean) visit(ctx.value(this.nodeProperties.size())));
            int count = 0;
            for(String l:labels){
                edge.addLabel(l);
            }
            for(int i = this.nodeProperties.size()+3; i < ctx.value().size(); i++){
                if(ctx.value(i).getClass() == PropertyGraphDFParser.ListValueContext.class){
                    ArrayList<String> aux = (ArrayList<String>) visit(ctx.value(i));
                    Property prop = new Property(this.edgeProperties.get(count), aux);
                    edge.addProperty(prop);
                }
                else if(ctx.value(i).getClass() == PropertyGraphDFParser.BooleanContext.class){
                    boolean aux = (boolean) visit(ctx.value(i));
                    String value = "";
                    if(aux){
                        value = "true";
                    }
                    else{
                        value = "false";
                    }
                    String finalValue = value;
                    Property prop = new Property(this.edgeProperties.get(count),new ArrayList<>(){{add(finalValue);}});
                    edge.addProperty(prop);
                }
                //es lista
                else{
                    String value = (String) visit(ctx.value(i));
                    if(value != ""){
                        Property prop = new Property(this.edgeProperties.get(count),new ArrayList<>(){{add(value);}});
                        edge.addProperty(prop);
                    }
                }
                count++;
            }
            this.edges.add(edge);
        }
        else{
            //Node
            Node node = new Node(id);
            for(String l:labels){
                node.addLabel(l);
            }
            int count = 0;
            for(int i = 0; i < ctx.value().size(); i++){
                if(this.nodeProperties.size() < i){
                    break;
                }
                if(ctx.value(i).getClass() == PropertyGraphDFParser.ListValueContext.class){
                    ArrayList<String> aux = (ArrayList<String>) visit(ctx.value(i));
                    Property prop = new Property(this.nodeProperties.get(count), aux);
                    node.addProperty(prop);
                }
                else if(ctx.value(i).getClass() == PropertyGraphDFParser.BooleanContext.class){
                    boolean aux = (boolean) visit(ctx.value(i));
                    String value = "";
                    if(aux){
                        value = "true";
                    }
                    else{
                        value = "false";
                    }
                    String finalValue = value;
                    Property prop = new Property(this.nodeProperties.get(count),new ArrayList<>(){{add(finalValue);}});
                    node.addProperty(prop);
                }
                //es lista
                else{
                    String value = (String) visit(ctx.value(i));
                    if(value != ""){
                        Property prop = new Property(this.nodeProperties.get(count),new ArrayList<>(){{add(value);}});
                        node.addProperty(prop);
                    }
                }
                count++;
            }
            this.nodes.add(node);
        }
        return super.visitDeclaration(ctx);
    }

    /*@Override
    public Object visitNodeDeclaration(PropertyGraphDFParser.NodeDeclarationContext ctx) {
        String id = ctx.STRING().getText();
        id = id.substring(1,id.length()-1);
        Node node = new Node(id);
        ArrayList<String> labels = visitLabel(ctx.label());
        for(String l:labels){
            node.addLabel(l);
        }
        int nothing = 0;
        for(int i = 0; i < (ctx.value().size() - this.edgeProperties.size() -3); i++){
            if(this.nodeProperties.size() < i){
                break;
            }
            if(ctx.value(i).getClass() == PropertyGraphDFParser.ListValueContext.class){
                ArrayList<String> aux = (ArrayList<String>) visit(ctx.value(i));
                Property prop = new Property(this.nodeProperties.get(i), aux);
                node.addProperty(prop);
            }
            else if(ctx.value(i).getClass() == PropertyGraphDFParser.BooleanContext.class){
                boolean aux = (boolean) visit(ctx.value(i));
                String value = "";
                if(aux){
                    value = "true";
                }
                else{
                    value = "false";
                }
                String finalValue = value;
                Property prop = new Property(this.nodeProperties.get(i),new ArrayList<>(){{add(finalValue);}});
                node.addProperty(prop);
            }
            //es lista
            else{
                String value = (String) visit(ctx.value(i));
                if(value != ""){
                    Property prop = new Property(this.nodeProperties.get(i),new ArrayList<>(){{add(value);}});
                    node.addProperty(prop);
                }
                else{
                    nothing++;
                    if(nothing == this.nodeProperties.size()){
                        visitEdgeDeclaration(ctx.setParent(PropertyGraphDFParser.EdgeDeclarationContext ))
                    }
                }

            }
        }
        this.nodes.add(node);
        return null;
    }*/

    /*@Override
    public Object visitEdgeDeclaration(PropertyGraphDFParser.EdgeDeclarationContext ctx) {
        String id = ctx.STRING(0).getText();
        id = id.substring(1,id.length()-1);
        String source = ctx.STRING(1).getText();
        source = source.substring(1,source.length()-1);
        String target = ctx.STRING(2).getText();
        target = target.substring(1,target.length()-1);
        Edge edge = new Edge(id,source,target);
        ArrayList<String> labels = visitLabel(ctx.label());
        for(String l:labels){
            edge.addLabel(l);
        }
        edge.setUndirected((Boolean) visit(ctx.booleanValue()));
        for(int i = 0; i < ctx.value().size(); i++){
            if(ctx.value(i).getClass() == PropertyGraphDFParser.ListValueContext.class){
                ArrayList<String> aux = (ArrayList<String>) visit(ctx.value(i));
                Property prop = new Property(this.edgeProperties.get(i), aux);
                edge.addProperty(prop);
            }
            else if(ctx.value(i).getClass() == PropertyGraphDFParser.BooleanContext.class){
                boolean aux = (boolean) visit(ctx.value(i));
                String value = "";
                if(aux){
                    value = "true";
                }
                else{
                    value = "false";
                }
                String finalValue = value;
                Property prop = new Property(this.edgeProperties.get(i),new ArrayList<>(){{add(finalValue);}});
                edge.addProperty(prop);
            }
            //es lista
            else{
                String value = (String) visit(ctx.value(i));
                if(value != ""){
                    Property prop = new Property(this.edgeProperties.get(i),new ArrayList<>(){{add(value);}});
                    edge.addProperty(prop);
                }
            }
        }
        this.edges.add(edge);
        return null;
    }*/

    @Override
    public ArrayList<String> visitLabel(PropertyGraphDFParser.LabelContext ctx) {
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < ctx.STRING().size(); i++){
            String value = ctx.STRING(i).getText();
            value = value.substring(1,value.length()-1);
            list.add(value);
        }
        return list;
    }

    @Override
    public List<String> visitList(PropertyGraphDFParser.ListContext ctx) {
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < ctx.STRING().size(); i++){
            String value = ctx.STRING(i).getText();
            value = value.substring(1,value.length()-1);
            list.add(value);
        }
        return list;
    }

    @Override
    public String visitString(PropertyGraphDFParser.StringContext ctx) {
        String value = ctx.STRING().getText();
        return value.substring(1, value.length()-1);
    }

    @Override
    public Boolean visitBooleanValue(PropertyGraphDFParser.BooleanValueContext ctx) {
        //System.out.println(ctx.getText());
        if(ctx.getText().equals("T")){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String visitNumber(PropertyGraphDFParser.NumberContext ctx) {
        //System.out.println(ctx.NUMBER().getText());
        return ctx.NUMBER().getText();
    }

    @Override
    public String visitNullValue(PropertyGraphDFParser.NullValueContext ctx) {
        return null;
    }

    @Override
    public Object visitNothing(PropertyGraphDFParser.NothingContext ctx) {
        return "";
    }
}
