package ANTLR4Files;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParserToDatabase extends PropertyGraphDFBaseVisitor{

    private List<String> nodeProperties;
    private List<String> edgeProperties;
    private String url = "jdbc:sqlite:pgdf.db";
    private String nodePropSQL = "";
    private String edgePropSQL = "";
    private String edgeNewValues = "";
    private String nodeNewValues = "";

    @Override
    public Object visitNodeFile(PropertyGraphDFParser.NodeFileContext ctx) {
        this.nodeProperties = new ArrayList<>();
        return super.visitNodeFile(ctx);
    }

    @Override
    public Object visitFullFile(PropertyGraphDFParser.FullFileContext ctx) {
        this.nodeProperties = new ArrayList<>();
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
        String sql = "";
        try (Connection conn = DriverManager.getConnection(this.url);
             Statement stmt = conn.createStatement()){
            sql = "ALTER TABLE nodeData ADD COLUMN ";
            for(int i = 0; i < ctx.newPropN().size(); i++){
                String prop = (String) visit(ctx.newPropN(i));
                stmt.execute(sql+"'"+prop+"'"+";");
                if(i == 0){
                    this.nodePropSQL = this.nodePropSQL +",";
                    this.nodeNewValues = this.nodeNewValues +",";
                }
                this.nodeProperties.add(prop);
                this.nodePropSQL = this.nodePropSQL + "'"+prop+"'";
                this.nodeNewValues = this.nodeNewValues + "?";
                if(i +1 < ctx.newPropN().size()){
                    this.nodePropSQL = this.nodePropSQL +",";
                    this.nodeNewValues = this.nodeNewValues + ",";
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return super.visitNodeProp(ctx);
    }

    @Override
    public Object visitStandardProps(PropertyGraphDFParser.StandardPropsContext ctx) {
        this.nodeProperties = new ArrayList<>();
        this.edgeProperties = new ArrayList<>();
        String sql = "";
        try (Connection conn = DriverManager.getConnection(this.url);
             Statement stmt = conn.createStatement()){
            sql = "ALTER TABLE nodeData ADD COLUMN ";

            for(int i = 0; i < ctx.newPropN().size(); i++){
                String prop = (String) visit(ctx.newPropN(i));
                stmt.execute(sql+"'"+prop+"'"+";");
                if(i == 0){
                    this.nodePropSQL = this.nodePropSQL +",";
                    this.nodeNewValues = this.nodeNewValues +",";
                }
                this.nodeProperties.add(prop);
                this.nodePropSQL = this.nodePropSQL + "'"+prop+"'";
                this.nodeNewValues = this.nodeNewValues + "?";
                if(i +1 < ctx.newPropN().size()){
                    this.nodePropSQL = this.nodePropSQL +",";
                    this.nodeNewValues = this.nodeNewValues + ",";
                }
            }
            sql = "ALTER TABLE edgeData ADD COLUMN ";
            for(int i = 0; i < ctx.newPropE().size(); i++){
                String prop = (String) visit(ctx.newPropE(i));
                stmt.execute(sql+"'"+prop+"'"+";");
                if(i == 0){
                    this.edgePropSQL = this.edgePropSQL +",";
                    this.edgeNewValues = this.edgeNewValues +",";
                }
                this.edgeProperties.add(prop);
                this.edgePropSQL = this.edgePropSQL + "'"+prop+"'";
                this.edgeNewValues = this.edgeNewValues + "?";
                if(i +1 < ctx.newPropE().size()){
                    this.edgePropSQL = this.edgePropSQL +",";
                    this.edgeNewValues = this.edgeNewValues + ",";
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
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
            String sql = "INSERT INTO edgeData(edgeID,edgeLabel,_undirected,_source,_target"+this.edgePropSQL+") VALUES (?,?,?,?,?"+this.edgeNewValues+")";

            String source = (String) visit(ctx.value(this.nodeProperties.size()+1));
            String target = (String) visit(ctx.value(this.nodeProperties.size()+2));
            Boolean undirected = (Boolean) visit(ctx.value(this.nodeProperties.size()));
            /*Edge edge = new Edge(id,source,target);
            edge.setUndirected((Boolean) visit(ctx.value(this.nodeProperties.size())));*/
            try (Connection conn = DriverManager.getConnection(this.url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                String labelValue = "[";
                for(int j = 0; j < labels.size(); j++){
                    labelValue = labelValue + "\"" + labels.get(j) + "\"";
                    if(j+1 < labels.size()){
                        labelValue = labelValue + ",";
                    }
                }
                labelValue = labelValue + "]";
                pstmt.setString(2, labelValue);
                String undirectedValue = "";
                if(undirected){
                    undirectedValue = "true";
                }
                else{
                    undirectedValue = "false";
                }
                pstmt.setString(3,undirectedValue);
                pstmt.setString(4,source);
                pstmt.setString(5,target);
                int count = 0;
                for(int i = this.nodeProperties.size()+3; i < ctx.value().size(); i++){
                    if(ctx.value(i).getClass() == PropertyGraphDFParser.ListValueContext.class){
                        ArrayList<String> aux = (ArrayList<String>) visit(ctx.value(i));
                        String auxValue = "[";
                        for(int j = 0; j < aux.size(); j++){
                            auxValue = auxValue + "\"" + aux.get(j) + "\"";
                            if(j+1 < aux.size()){
                                auxValue = auxValue + ",";
                            }
                        }
                        auxValue = auxValue + "]";
                        pstmt.setString(count+6,auxValue);
                        /*Property prop = new Property(this.edgeProperties.get(count), aux);
                        edge.addProperty(prop);*/
                    }
                    else if(ctx.value(i).getClass() == PropertyGraphDFParser.BooleanContext.class){
                        boolean aux = (boolean) visit(ctx.value(i));
                        String auxValue = "";
                        if(aux){
                            auxValue = "true";
                        }
                        else{
                            auxValue = "false";
                        }
                        pstmt.setString(count+6,auxValue);
                        /*String value = "";
                        if(aux){
                            value = "true";
                        }
                        else{
                            value = "false";
                        }
                        String finalValue = value;
                        Property prop = new Property(this.edgeProperties.get(count),new ArrayList<>(){{add(finalValue);}});
                        edge.addProperty(prop);*/
                    }
                    //es lista
                    else{
                        String value = (String) visit(ctx.value(i));
                        if(value != ""){
                            if(value == null){
                                value = "Null";
                            }
                            pstmt.setString(count+6,value);
                            /*Property prop = new Property(this.nodeProperties.get(count),new ArrayList<>(){{add(value);}});
                            node.addProperty(prop);*/
                        }
                    }
                    count++;
                }
                pstmt.executeUpdate();
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            String sql = "INSERT INTO nodeData(nodeID,nodeLabel"+this.nodePropSQL+") VALUES (?,?"+this.nodeNewValues+")";
            //Node
            //Node node = new Node(id);
            try (Connection conn = DriverManager.getConnection(this.url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1,id);
                String labelValue = "[";
                for(int j = 0; j < labels.size(); j++){
                    labelValue = labelValue + "\"" + labels.get(j) + "\"";
                    if(j+1 < labels.size()){
                        labelValue = labelValue + ",";
                    }
                }
                labelValue = labelValue + "]";
                pstmt.setString(2, labelValue);

                int count = 0;
                for(int i = 0; i < ctx.value().size(); i++){
                    if(this.nodeProperties.size() < i){
                        break;
                    }
                    if(ctx.value(i).getClass() == PropertyGraphDFParser.ListValueContext.class){
                        ArrayList<String> aux = (ArrayList<String>) visit(ctx.value(i));
                        String auxValue = "[";
                        for(int j = 0; j < aux.size(); j++){
                            auxValue = auxValue + "\"" + aux.get(j) + "\"";
                            if(j+1 < aux.size()){
                                auxValue = auxValue + ",";
                            }
                        }
                        auxValue = auxValue + "]";
                        pstmt.setString(count+3,auxValue);
                        /*Property prop = new Property(this.nodeProperties.get(count), aux);
                        node.addProperty(prop);*/
                    }
                    else if(ctx.value(i).getClass() == PropertyGraphDFParser.BooleanContext.class){
                        boolean aux = (boolean) visit(ctx.value(i));
                        String auxValue = "";
                        if(aux){
                            auxValue = "true";
                        }
                        else{
                            auxValue = "false";
                        }
                        pstmt.setString(count+3,auxValue);
                        /*String value = "";
                        if(aux){
                            value = "true";
                        }
                        else{
                            value = "false";
                        }
                        String finalValue = value;
                        Property prop = new Property(this.nodeProperties.get(count),new ArrayList<>(){{add(finalValue);}});
                        node.addProperty(prop);*/
                    }
                    else{
                        String value = (String) visit(ctx.value(i));
                        if(value != ""){
                            if(value == null){
                                value = "Null";
                            }
                            pstmt.setString(count+3,value);
                            /*Property prop = new Property(this.nodeProperties.get(count),new ArrayList<>(){{add(value);}});
                            node.addProperty(prop);*/
                        }
                    }
                    count++;
                }
                pstmt.executeUpdate();
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }

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
