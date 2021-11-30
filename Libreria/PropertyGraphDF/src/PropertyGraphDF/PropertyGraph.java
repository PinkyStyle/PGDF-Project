package PropertyGraphDF;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ANTLR4Files.GraphGenerator;
import ANTLR4Files.ParserToDatabase;
import ANTLR4Files.PropertyGraphDFLexer;
import ANTLR4Files.PropertyGraphDFParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author Matias Pizarro
 */
public class PropertyGraph {
    private String storageType;
    private String url;
    private List<Node> nodes;
    private List<Edge> edges;
    private List<String> nodeProperties;
    private List<String> edgeProperties;

    /**
     * Constructor
     */
    public PropertyGraph(){
        this.storageType = "memory";
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.nodeProperties = new ArrayList<>();
        this.edgeProperties = new ArrayList<>();
    }

        public PropertyGraph(String storageType){
        if(storageType.equals("memory") || storageType.equals("disk")){
            this.storageType = storageType;
            if(this.storageType.equals("disk")){
                File oldDatabase = new File("pgdf.db");
                oldDatabase.delete();
                this.url = "jdbc:sqlite:pgdf.db";

                try (Connection conn = DriverManager.getConnection(url);
                     Statement stmt = conn.createStatement()) {


                    String sql = "CREATE TABLE IF NOT EXISTS nodeData (\n"
                            + "	nodeID text PRIMARY KEY,\n"
                            + "	nodeLabel \n"
                            + ");";
                    stmt.execute(sql);
                    sql = "CREATE TABLE IF NOT EXISTS edgeData (\n"
                            + "	edgeID text PRIMARY KEY,\n"
                            + "	edgeLabel \n,"
                            + "	_undirected \n,"
                            + "	_source \n,"
                            + "	_target \n"
                            + ");";
                    stmt.execute(sql);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            else{
                this.nodes = new ArrayList<>();
                this.edges = new ArrayList<>();
                this.nodeProperties = new ArrayList<>();
                this.edgeProperties = new ArrayList<>();
            }
        }
        else{
            throw new IllegalArgumentException("Storage type can only be 'memory' or 'disk'");
        }
    }

    public boolean addNode(Node node) {
        if(this.storageType.equals("memory")){
            if(!this.nodes.contains(node)){
                this.nodes.add(node);
                for(Property prop:node.getProperties()){
                    if(!this.nodeProperties.contains(prop.getKey())){
                        this.nodeProperties.add(prop.getKey());
                    }
                }
                return true;
            }
        }
        else{
            //agregar objeto nodo a la bd
            String sql = "SELECT * from nodeData LIMIT 0";
            ArrayList<String> columns = new ArrayList<>();
            //obtener columnas existentes
            try (Connection conn = DriverManager.getConnection(this.url);
            Statement stmt = conn.createStatement()){
                ResultSet rs = stmt.executeQuery(sql);
                ResultSetMetaData mrs = rs.getMetaData();
                for(int i = 1; i <= mrs.getColumnCount(); i++) {
                    columns.add(mrs.getColumnLabel(i));
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            //comparar con propiedades del nodo
            ArrayList<String> newProps = new ArrayList<>();
            String propNames = "";
            String newValues = "";
            for(int i = 0; i < node.getProperties().size(); i++){
                if(!columns.contains(node.getProperties().get(i).getKey())){
                    newProps.add(node.getProperties().get(i).getKey());
                }
                if(i == 0){
                    propNames = propNames + ",";
                    newValues = newValues + ",";
                }
                propNames = propNames + node.getProperties().get(i).getKey();
                newValues = newValues + "?";
                if(i +1 < node.getProperties().size()){
                    propNames = propNames +",";
                    newValues = newValues +",";
                }
            }
            //agregar nuevas columnas
            sql = "ALTER TABLE nodeData ADD COLUMN ";
            if(!newProps.isEmpty()){
                try (Connection conn = DriverManager.getConnection(this.url);
                     Statement stmt = conn.createStatement()){
                    for(String s: newProps){
                        stmt.execute(sql+s+";");
                    }
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

            //insertar nodo en tabla
            sql = "INSERT INTO nodeData(nodeID,nodeLabel"+propNames+") VALUES (?,?"+newValues+")";
            try (Connection conn = DriverManager.getConnection(this.url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, node.getId());
                pstmt.setString(2, node.getLabels().toString());
                int i = 3;
                for(Property prop: node.getProperties()){
                    String declaration = "";
                    ArrayList<String> value = new ArrayList(prop.getValue());
                    if( value.size() >= 1){
                        String data = "";
                        int ready = 0;
                        if(value.size() <= 1){
                            data = "";
                        }
                        else{
                            data = "[";
                        }
                        for(String v : value){
                            if(v == null){
                                data = data + "N";
                            }
                            else if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                if(v.contains(".")){
                                    data = data + Float.parseFloat(v);
                                }
                                else{
                                    data = data + Integer.parseInt(v);
                                }
                            }
                            else if(v.equals("true") || v.equals("TRUE")){
                                data = data + "T";
                            }
                            else if(v.equals("false") || v.equals("FALSE")){
                                data = data + "F";
                            }

                            else{
                                data = data + "\"" + v + "\"";
                            }
                            ready++;
                            if(value.size()>1 && ready< value.size()){
                                data = data+",";
                            }
                        }
                        if(value.size() > 1){
                            data = data+"]";
                        }
                        declaration = declaration + data;
                    }
                    pstmt.setString(i,declaration);
                    i++;
                }
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean removeNode(Node node){
        if(this.storageType.equals("memory")){
            if(this.nodes.contains(node)){
                this.nodes.remove(node);
                this.nodeProperties = new ArrayList<>();
                for(Node n: this.nodes){
                    for(Property prop: n.getProperties()){
                        if(!this.nodeProperties.contains(prop.getKey())){
                            this.nodeProperties.add(prop.getKey());
                        }
                    }
                }
                return true;
            }
        }
        else{
            //eliminar nodo de bd
        }
        return false;
    }

    public boolean addEdge(Edge edge){
        if(this.storageType.equals("memory")){
            if(!this.edges.contains(edge)){
                this.edges.add(edge);
                for(Property prop:edge.getProperties()){
                    if(!this.edgeProperties.contains(prop.getKey())){
                        this.edgeProperties.add(prop.getKey());
                    }

                }
                return true;
            }
        }
        else{
            //agregar arista a bd
            //agregar objeto nodo a la bd
            String sql = "SELECT * from edgeData LIMIT 0";
            ArrayList<String> columns = new ArrayList<>();
            //obtener columnas existentes
            try (Connection conn = DriverManager.getConnection(this.url);
                 Statement stmt = conn.createStatement()){
                ResultSet rs = stmt.executeQuery(sql);
                ResultSetMetaData mrs = rs.getMetaData();
                for(int i = 1; i <= mrs.getColumnCount(); i++) {
                    columns.add(mrs.getColumnLabel(i));
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            //comparar con propiedades del nodo
            ArrayList<String> newProps = new ArrayList<>();
            String propNames = "";
            String newValues = "";
            for(int i = 0; i < edge.getProperties().size(); i++){
                if(!columns.contains(edge.getProperties().get(i).getKey())){
                    newProps.add(edge.getProperties().get(i).getKey());
                }
                if(i == 0){
                    propNames = propNames + ",";
                    newValues = newValues + ",";
                }
                propNames = propNames + edge.getProperties().get(i).getKey();
                newValues = newValues + "?";
                if(i +1 < edge.getProperties().size()){
                    propNames = propNames +",";
                    newValues = newValues +",";
                }
            }

            sql = "ALTER TABLE edgeData ADD COLUMN ";
            if(!newProps.isEmpty()){
                try (Connection conn = DriverManager.getConnection(this.url);
                     Statement stmt = conn.createStatement()){
                    for(String s: newProps){
                        stmt.execute(sql+s+";");
                    }
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }

            sql = "INSERT INTO edgeData(edgeID,edgeLabel,_undirected,_source,_target"+propNames+") VALUES (?,?,?,?,?"+newValues+")";
            try (Connection conn = DriverManager.getConnection(this.url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, edge.getId());
                pstmt.setString(2, edge.getLabels().toString());
                pstmt.setBoolean(3,edge.getUndirected());
                pstmt.setString(4,edge.getSource());
                pstmt.setString(5,edge.getTarget());

                int i = 6;
                for(Property prop: edge.getProperties()){
                    String declaration = "";
                    ArrayList<String> value = new ArrayList(prop.getValue());
                    if( value.size() >= 1){
                        String data = "";
                        int ready = 0;
                        if(value.size() <= 1){
                            data = "";
                        }
                        else{
                            data = "[";
                        }
                        for(String v : value){
                            if(v == null){
                                data = data + "N";
                            }
                            else if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                if(v.contains(".")){
                                    data = data + Float.parseFloat(v);
                                }
                                else{
                                    data = data + Integer.parseInt(v);
                                }
                            }
                            else if(v.equals("true") || v.equals("TRUE")){
                                data = data + "T";
                            }
                            else if(v.equals("false") || v.equals("FALSE")){
                                data = data + "F";
                            }

                            else{
                                data = data + "\"" + v + "\"";
                            }
                            ready++;
                            if(value.size()>1 && ready< value.size()){
                                data = data+",";
                            }
                        }
                        if(value.size() > 1){
                            data = data+"]";
                        }
                        declaration = declaration + data;
                    }
                    pstmt.setString(i,declaration);
                    i++;
                }
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean removeEdge(Edge edge){
        if(this.storageType.equals("memory")){
            if(this.edges.contains(edge)){
                this.edges.remove(edge);
                this.edgeProperties = new ArrayList<>();
                for(Edge e: this.edges){
                    for(Property prop: e.getProperties()){
                        if(!this.edgeProperties.contains(prop.getKey())){
                            this.edgeProperties.add(prop.getKey());
                        }
                    }
                }
                return true;
            }
        }
        else{
            //eliminar arista de bd
        }
        return false;
    }

    public void export(String directory, String fileName) throws IOException{
        File graphFile = new File (directory+fileName+".pgdf");
        if(this.storageType.equals("memory")){
            try(FileWriter writer = new FileWriter(graphFile)){
                String firstLine = "@id;@label;";

                for(String name: this.nodeProperties){
                    firstLine = firstLine+name+";";
                }
                firstLine = firstLine + "@undirected;@source;@target;";
                for(String name: this.edgeProperties){
                    firstLine = firstLine+name+";";
                }
                writer.write(firstLine+"\n");

                //Escribir declaracion de nodos
                for(Node node: this.getNodes()){
                    String declaration = node.formatNode();
                    for(String prop: this.nodeProperties){
                        if(node.hasProp(prop)){
                            ArrayList<String> value = new ArrayList(node.getValue(prop));
                            if(value.isEmpty()){
                                declaration = declaration + "";
                            }
                            else if( value.size() >= 1){
                                String data = "";
                                int ready = 0;
                                if(value.size() <= 1){
                                    data = "";
                                }
                                else{
                                    data = "[";
                                }
                                for(String v : value){
                                    if(v == null){
                                        data = data + "N";
                                    }
                                    else if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                        if(v.contains(".")){
                                            data = data + Float.parseFloat(v);
                                        }
                                        else{
                                            data = data + Integer.parseInt(v);
                                        }
                                    }
                                    else if(v.equals("true") || v.equals("TRUE")){
                                        data = data + "T";
                                    }
                                    else if(v.equals("false") || v.equals("FALSE")){
                                        data = data + "F";
                                    }

                                    else{
                                        data = data + "\"" + v + "\"";
                                    }
                                    ready++;
                                    if(value.size()>1 && ready< value.size()){
                                        data = data+",";
                                    }
                                }
                                if(value.size() > 1){
                                    data = data+"]";
                                }
                                declaration = declaration + data + ";";
                            }
                        }
                        else{
                            declaration = declaration + ";";
                        }
                    }
                    writer.write(declaration+"\n");
                }

                //Escribir declaracion de aristas
                for(Edge edge: this.getEdges()){
                    String[] aux = edge.formatEdge().split(";");
                    String declaration = aux[0] +";"+ aux[1] +";";
                    for(int i = 0; i < this.nodeProperties.size(); i++){
                        declaration = declaration + ";";
                    }
                    declaration = declaration + aux[2] +";"+ aux[3]+";" + aux[4]+";";
                    for(String prop: this.edgeProperties){
                        if(edge.hasProp(prop)){
                            ArrayList<String> value = new ArrayList(edge.getValue(prop));
                            if(value.isEmpty()){
                                declaration = declaration + "";
                            }
                            else if( value.size() >= 1){
                                String data = "";
                                int ready = 0;
                                if(value.size() == 1){
                                    data = "";
                                }
                                else{
                                    data = "[";
                                }
                                for(String v : value){
                                    if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                        if(v.contains(".")){
                                            data = data + Float.parseFloat(v);
                                        }
                                        else{
                                            data = data + Integer.parseInt(v);
                                        }
                                    }
                                    else if(v.equals("true") || v.equals("TRUE")){
                                        data = data + "T";
                                    }
                                    else if(v.equals("false") || v.equals("FALSE")){
                                        data = data + "F";
                                    }
                                    else if(v.equals(null)){
                                        data = data + "N";
                                    }
                                    else{
                                        data = data + "\"" + v + "\"";
                                    }
                                    ready++;
                                    if(value.size()>1 && ready< value.size()){
                                        data = data+",";
                                    }
                                }
                                if(value.size() > 1){
                                    data = data+"]";
                                }
                                declaration = declaration + data + ";";
                            }
                        }
                        else{
                            declaration = declaration + ";";
                        }
                    }
                    writer.write(declaration+"\n");
                }
            }
        }
        else{
            try(FileWriter writer = new FileWriter(graphFile)){
                String header = "";
                String sql;
                int numNodeProps = 0;
                //Generar encabezado del archivo
                try (Connection conn = DriverManager.getConnection(this.url);
                     Statement stmt = conn.createStatement()){
                    sql = "SELECT * from nodeData LIMIT 0";
                    ResultSet rs = stmt.executeQuery(sql);
                    ResultSetMetaData mrs = rs.getMetaData();
                    for(int i = 1; i <= mrs.getColumnCount(); i++) {
                        switch(mrs.getColumnLabel(i)){
                            case("nodeID"):
                                header = header + "@id;";
                                break;
                            case("nodeLabel"):
                                header = header + "@label;";
                                break;
                            default:
                                header = header + mrs.getColumnLabel(i) +";";
                                numNodeProps++;
                                break;
                        }
                    }
                    sql = "SELECT * from edgeData LIMIT 0";
                    rs = stmt.executeQuery(sql);
                    mrs = rs.getMetaData();
                    for(int i = 1; i <= mrs.getColumnCount(); i++) {
                        switch(mrs.getColumnLabel(i)){
                            case("edgeID"):
                            case("edgeLabel"):
                                break;
                            case("_undirected"):
                                header = header + "@undirected;";
                                break;
                            case("_source"):
                                header = header + "@source;";
                                break;
                            case("_target"):
                                header = header + "@target;";
                                break;
                            default:
                                header = header + mrs.getColumnLabel(i) +";";
                                break;
                        }
                    }
                    writer.write(header+"\n");
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                //Obtener datos de los nodos y exportar
                sql = "SELECT * FROM nodeData";
                try (Connection conn = DriverManager.getConnection(this.url);
                     Statement stmt  = conn.createStatement();
                     ResultSet rs    = stmt.executeQuery(sql)){
                    ResultSetMetaData rsmd = rs.getMetaData();
                    // loop through the result set
                    while (rs.next()) {
                        String declaration = "";
                        declaration = declaration + "\"" +rs.getString(1)+"\"" +";" +rs.getString(2) + ";";
                        for(int i = 3; i <= rsmd.getColumnCount();i++){
                            String data = rs.getString(i);
                            if(data != null){
                                if(data.equals("Null")){
                                    declaration = declaration +"N;";
                                }
                                else if(data.equals("true")){
                                    declaration = declaration +"T;";
                                }
                                else if(data.equals("false")){
                                    declaration = declaration +"F;";
                                }
                                else if(data.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                    if (data.contains(".")) {
                                        declaration = declaration + Float.parseFloat(data) +";";
                                    } else {
                                        declaration = declaration + Integer.parseInt(data) +";";
                                    }
                                }
                                else if(data.startsWith("[") && data.endsWith("]")){
                                    declaration = declaration + data + ";";
                                }
                                else{
                                    declaration = declaration + "\""+data+"\"" + ";";
                                }

                            }
                            else{
                                declaration = declaration +";";
                            }
                        }
                        writer.write(declaration+"\n");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                //Obtener datos de las aristas y exportar
                sql = "SELECT * FROM edgeData";
                try (Connection conn = DriverManager.getConnection(this.url);
                     Statement stmt  = conn.createStatement();
                     ResultSet rs    = stmt.executeQuery(sql)){
                    ResultSetMetaData rsmd = rs.getMetaData();
                    // loop through the result set
                    while (rs.next()) {
                        String declaration = "";
                        declaration = declaration + "\""+rs.getString(1)+"\"" +";" +rs.getString(2) + ";";
                        for(int i = 0; i < numNodeProps; i++){
                            declaration = declaration +";";
                        }
                        if(rs.getString(3).equals("true")){
                            declaration = declaration + "T;";
                        }else{
                            declaration = declaration + "F;";
                        }

                        declaration = declaration + "\""+rs.getString(4)+"\"" +";";
                        declaration = declaration + "\""+rs.getString(5)+"\"" +";";

                        for(int i = 6; i <= rsmd.getColumnCount();i++){
                            String data = rs.getString(i);
                            if(data != null){
                                if(data.equals("Null")){
                                    declaration = declaration +"N;";
                                }
                                else if(data.equals("true")){
                                    declaration = declaration +"T;";
                                }
                                else if(data.equals("false")){
                                    declaration = declaration +"F;";
                                }
                                else if(data.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                    if (data.contains(".")) {
                                        declaration = declaration + Float.parseFloat(data) +";";
                                    } else {
                                        declaration = declaration + Integer.parseInt(data) +";";
                                    }
                                }
                                else if(data.startsWith("[") && data.endsWith("]")){
                                    declaration = declaration + data + ";";
                                }
                                else{
                                    declaration = declaration + "\""+data+"\"" + ";";
                                }

                            }
                            else{
                                declaration = declaration +";";
                            }
                        }
                        writer.write(declaration+"\n");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

/*        File nodeFile = new File(directory+nodeFileName+".pgdf");
        try (FileWriter writer = new FileWriter(nodeFile)) {
            String firstLine = "@id;@label;";

            for(String name: this.nodeProperties){
                firstLine = firstLine+name+";";
            }
            writer.write(firstLine+"\n");

            for(Node node: this.getNodes()){
                String declaration = node.formatNode();
                for(String prop: this.nodeProperties){
                    if(node.hasProp(prop)){
                        ArrayList<String> value = new ArrayList(node.getValue(prop));
                        if(value.isEmpty()){
                            declaration = declaration + "";
                        }
                        else if( value.size() >= 1){
                            String data = "";
                            int ready = 0;
                            if(value.size() <= 1){
                                data = "";  
                            }
                            else{
                                data = "[";
                            }
                            for(String v : value){
                                if(v == null){
                                    data = data + "N";
                                }
                                else if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                    if(v.contains(".")){
                                        data = data + Float.parseFloat(v);
                                    }
                                    else{
                                        data = data + Integer.parseInt(v);
                                    }
                                }
                                else if(v.equals("true") || v.equals("TRUE")){
                                    data = data + "T";
                                }
                                else if(v.equals("false") || v.equals("FALSE")){
                                    data = data + "F";
                                }

                                else{
                                    data = data + "\"" + v + "\"";
                                }
                                ready++;
                                if(value.size()>1 && ready< value.size()){
                                    data = data+",";
                                }
                            }
                            if(value.size() > 1){
                                data = data+"]";
                            }
                            declaration = declaration + data + ";";
                        }
                    }
                    else{
                        declaration = declaration + ";";
                    }
                }
                writer.write(declaration+"\n");
            }
        }

        File edgeFile = new File(directory+edgeFileName+".pgdf");
        try (FileWriter writer = new FileWriter(edgeFile)) {
            List<String> properties = this.edgeProperties;
            String firstLine = "@id;@undirected;@label;@source;@target;";

            for(String name: properties){
                firstLine = firstLine+name+";";
            }
            writer.write(firstLine+"\n");

            for(Edge edge: this.edges){
                String declaration = edge.formatEdge();
                for(String prop: this.edgeProperties){
                    if(edge.hasProp(prop)){
                        ArrayList<String> value = new ArrayList(edge.getValue(prop));
                        if(value.isEmpty()){
                            declaration = declaration + "";
                        }
                        else if( value.size() >= 1){
                            String data = "";
                            int ready = 0;
                            if(value.size() == 1){
                                data = "";
                            }
                            else{
                                data = "[";
                            }
                            for(String v : value){
                                if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                    if(v.contains(".")){
                                        data = data + Float.parseFloat(v);
                                    }
                                    else{
                                        data = data + Integer.parseInt(v);
                                    }
                                }
                                else if(v.equals("true") || v.equals("TRUE")){
                                    data = data + "T";
                                }
                                else if(v.equals("false") || v.equals("FALSE")){
                                    data = data + "F";
                                }
                                else if(v.equals(null)){
                                    data = data + "N";
                                }
                                else{
                                    data = data + "\"" + v + "\"";
                                }
                                ready++;
                                if(value.size()>1 && ready< value.size()){
                                    data = data+",";
                                }
                            }
                            if(value.size() > 1){
                                data = data+"]";
                            }
                            declaration = declaration + data + ";";
                        }
                    }
                    else{
                        declaration = declaration + ";";
                    }
                }
                writer.write(declaration+"\n");
            }
        }*/
    }

    public void exportToJSON(String directory, String fileName) throws IOException {
        File graphFile = new File (directory+fileName+".json");
        if(this.storageType.equals("memory")) {
            try (FileWriter writer = new FileWriter(graphFile)) {

                int count = 0;
                for (Node n : this.nodes) {
                    String line = "{\"type\":\"node\",\"id\":\"" + count + "\",";

                    if (n.getLabels().size() > 0) {
                        line = line + "\"labels\":[";
                        List<String> labels = n.getLabels();
                        if (labels.size() == 1) {
                            line = line + "\"" + labels.get(0) + "\"";
                        } else {
                            for (String s : labels) {
                                line = line + "\"" + s + "\",";
                            }
                            line = line.substring(0, line.length() - 1);
                        }
                        line = line + "],";
                    }
                    if (n.getProperties().size() > 0) {
                        line = line + "\"properties\":{";
                        List<Property> props = n.getProperties();
                        if (props.size() > 0) {
                            String propData = "";
                            for (int i = 0; i < props.size(); i++) {
                                Property p = props.get(i);
                                propData = propData + "\"" + p.getKey() + "\":";
                                if (p.getValue() == null) {
                                    propData = propData + "null";
                                } else if (p.getValue().size() > 0) {
                                    //propData = propData + "\"";
                                    int valueSize = 0;
                                    for (int j = 0; j < p.getValue().size(); j++) {
                                        if (j + 1 < p.getValue().size()) {
                                            propData = propData + "[";
                                        }
                                        if (p.getValue().get(j) == null) {
                                            propData = propData + "null";
                                            valueSize++;
                                        } else if (p.getValue().get(j).matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                            if (p.getValue().get(j).contains(".")) {
                                                propData = propData + Float.parseFloat(p.getValue().get(j));
                                            } else {
                                                propData = propData + Integer.parseInt(p.getValue().get(j));
                                            }
                                            valueSize++;
                                        } else if (p.getValue().get(j).equals("true") || p.getValue().get(j).equals("TRUE") || p.getValue().get(j).equals("True")) {
                                            propData = propData + "true";
                                            valueSize++;
                                        } else if (p.getValue().get(j).equals("false") || p.getValue().get(j).equals("FALSE") || p.getValue().get(j).equals("False")) {
                                            propData = propData + "false";
                                            valueSize++;
                                        } else {
                                            propData = propData + "\"" + p.getValue().get(j) + "\"";
                                            valueSize++;
                                        }
                                        if (valueSize < p.getValue().size()) {
                                            propData = propData + ",";
                                        } else if (valueSize > 1) {
                                            propData = propData + "]";
                                        }
                                    }
                                } else {
                                    propData = propData + "";
                                }
                                if (i + 1 < props.size()) {
                                    propData = propData + ",";
                                }
                            }
                            line = line + propData;
                        }
                        line = line + "}";
                    }
                    line = line + "}";
                    writer.write(line + "\n");
                    count++;
                }
                count = 0;
                boolean multipleLabels = false;
                for (Edge e : this.edges) {
                    String line = "{\"id\":\"" + count + "\",\"type\":\"relationship\",";

                    if (e.getLabels().size() > 0) {
                        line = line + "\"label\":";
                        List<String> labels = e.getLabels();
                        line = line + "\"" + labels.get(0) + "\"";
                        if (e.getLabels().size() > 1) {
                            multipleLabels = true;
                        }
                        line = line + ",";
                    } else {
                        line = line + "\"label\":\"EDGE\",";
                    }
                    if (e.getProperties().size() > 0) {
                        line = line + "\"properties\":{";
                        List<Property> props = e.getProperties();
                        if (props.size() > 0) {
                            String propData = "";
                            for (int i = 0; i < props.size(); i++) {
                                Property p = props.get(i);
                                propData = propData + "\"" + p.getKey() + "\":";
                                if (p.getValue() == null) {
                                    propData = propData + "null";
                                } else if (p.getValue().size() > 0) {
                                    //propData = propData + "\"";
                                    int valueSize = 0;
                                    for (int j = 0; j < p.getValue().size(); j++) {
                                        if (j + 1 < p.getValue().size()) {
                                            propData = propData + "[";
                                        }
                                        if (p.getValue().get(j) == null) {
                                            propData = propData + "null";
                                            valueSize++;
                                        } else if (p.getValue().get(j).matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                            if (p.getValue().get(j).contains(".")) {
                                                propData = propData + Float.parseFloat(p.getValue().get(j));
                                            } else {
                                                propData = propData + Integer.parseInt(p.getValue().get(j));
                                            }
                                            valueSize++;
                                        } else if (p.getValue().get(j).equals("true") || p.getValue().get(j).equals("TRUE") || p.getValue().get(j).equals("True")) {
                                            propData = propData + "true";
                                            valueSize++;
                                        } else if (p.getValue().get(j).equals("false") || p.getValue().get(j).equals("FALSE") || p.getValue().get(j).equals("False")) {
                                            propData = propData + "false";
                                            valueSize++;
                                        } else {
                                            propData = propData + "\"" + p.getValue().get(j) + "\"";
                                            valueSize++;
                                        }
                                        if (valueSize < p.getValue().size()) {
                                            propData = propData + ",";
                                        } else if (valueSize > 1) {
                                            propData = propData + "]";
                                        }
                                    }
                                } else {
                                    propData = propData + "";
                                }
                                if (i + 1 < props.size()) {
                                    propData = propData + ",";
                                }
                            }
                            line = line + propData + "},";
                        }
                    }

                    String start = e.getSource();
                    line = line + "\"start\":{";
                    for (int i = 0; i < this.nodes.size(); i++) {
                        if (this.nodes.get(i).getId().equals(start)) {
                            line = line + "\"id\":" + "\"" + i + "\",";
                            if (this.nodes.get(i).getLabels().size() > 0) {
                                line = line + "\"labels\":[";
                                List<String> labels = this.nodes.get(i).getLabels();
                                if (labels.size() == 1) {
                                    line = line + "\"" + labels.get(0) + "\"";
                                } else {
                                    for (String s : labels) {
                                        line = line + "\"" + s + "\",";
                                    }
                                    line = line.substring(0, line.length() - 1);
                                }
                                line = line + "]},";
                            } else {
                                line = line.substring(0, line.length() - 1) + "},";
                            }
                        }
                    }
                    String end = e.getTarget();
                    line = line + "\"end\":{";
                    for (int i = 0; i < this.nodes.size(); i++) {
                        if (this.nodes.get(i).getId().equals(end)) {
                            line = line + "\"id\":" + "\"" + i + "\",";
                            if (this.nodes.get(i).getLabels().size() > 0) {
                                line = line + "\"labels\":[";
                                List<String> labels = this.nodes.get(i).getLabels();
                                if (labels.size() == 1) {
                                    line = line + "\"" + labels.get(0) + "\"";
                                } else {
                                    for (String s : labels) {
                                        line = line + "\"" + s + "\",";
                                    }
                                    line = line.substring(0, line.length() - 1);
                                }
                                line = line + "]}";
                            } else {
                                line = line.substring(0, line.length() - 1) + "}";
                            }
                        }
                    }
                    line = line + "}";
                    //writer.write(line+"\n");
                    ArrayList<String> duplicateLabels = new ArrayList<>();
                    duplicateLabels.add(line);
                    if (multipleLabels) {
                        int numLabels = e.getLabels().size();
                        for (int i = 1; i < numLabels; i++) {
                            String newLine = line.replaceFirst("\"label\":\"" + e.getLabels().get(0) + "\"", "\"label\":\"" + e.getLabels().get(i) + "\"");
                            newLine = newLine.replaceFirst("\"" + count + "\"", "\"" + (count + i) + "\"");
                            duplicateLabels.add(newLine);
                        }
                        count = count + numLabels - 1;
                    }
                    if (e.getUndirected() != null && e.getUndirected()) {
                        int numLines = duplicateLabels.size();
                        for (int i = 0; i < numLines; i++) {
                            String newLine = duplicateLabels.get(i);
                            newLine = newLine.replace("\"end\"", "\"start\"");
                            newLine = newLine.replaceFirst("\"start\"", "\"end\"");
                            newLine = newLine.replaceFirst("\"" + (count - numLines + 1) + "\"", "\"" + (count + 1) + "\"");
                            duplicateLabels.add(newLine);
                            count++;
                        }
                    }
                    for (String s : duplicateLabels) {
                        writer.write(s + "\n");
                    }
                    count++;
                }
            }
        }
        else{
            int count = 0;
            String sql;
            try (FileWriter writer = new FileWriter(graphFile)) {
                //Exportar datos de nodos como json
                sql = "SELECT * FROM nodeData";
                try (Connection conn = DriverManager.getConnection(this.url);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    // loop through the result set
                    while (rs.next()) {
                        String declaration = "{\"type\":\"node\",\"id\":\"" + count + "\"";
                        String label = rs.getString(2);
                        if(label != null){
                            if(label.equals("[]")){
                                declaration = declaration + ",\"labels\":[\"NODE\"]";
                            }
                            else if(label.split("").length > 3){
                                String aux = label.substring(1,label.length()-1);
                                String[] names = aux.split(",");
                                for(int i = 0; i < names.length; i++){
                                    if(i == 0){
                                        declaration = declaration + ",\"labels\":[";
                                    }
                                    declaration = declaration + "\"" + names[i] + "\"";
                                    if(i+1 < names.length){
                                        declaration = declaration + ",";
                                    }
                                }
                                declaration = declaration +"]";
                            }
                        }
                        if(rsmd.getColumnCount() > 2){
                            declaration = declaration + ",\"properties\":{";
                            int notNull = 0;
                            for (int i = 3; i <= rsmd.getColumnCount(); i++) {
                                String value = rs.getString(i);
                                if (value != null) {
                                    notNull++;
                                    String data = "\"" + rsmd.getColumnLabel(i) + "\":";
                                    if(value.startsWith("[") && value.endsWith("]")){
                                        //pasarlo a lista
                                        if(value.equals("[]")){
                                            data = data + "[]";
                                        }
                                        else if(value.split("").length > 3){
                                            String aux = value.substring(1,value.length()-1);
                                            String[] names = aux.split(",");
                                            for(int j = 0; j < names.length; j++){
                                                if(j == 0){
                                                    data = data + "[";
                                                }
                                                data = data + "\"" + names[j] + "\"";
                                                if(j+1 < names.length){
                                                    data = data + ",";
                                                }
                                            }
                                            data = data +"]";
                                        }
                                    }
                                    else if(value.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                        if(value.contains(".")){
                                            data = data + Float.parseFloat(value);
                                        }
                                        else{
                                            data = data + Integer.parseInt(value);
                                        }
                                    }
                                    else if(value.equals("true") || value.equals("TRUE")){
                                        data = data + "T";
                                    }
                                    else if(value.equals("false") || value.equals("FALSE")){
                                        data = data + "F";
                                    }
                                    else{
                                        data = data + "\"" + value + "\"";
                                    }
                                    declaration = declaration + data;
                                }
                                if(i+1 <= rsmd.getColumnCount() && rs.getString(i+1) != null && notNull > 0){
                                    declaration = declaration + ",";
                                }
                            }
                            declaration = declaration + "}";
                        }
                        declaration = declaration + "}";
                        writer.write(declaration + "\n");
                        count++;
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                //Exportar datos de aristas como json

                sql = "SELECT * FROM edgeData";
                count = 0;
                try (Connection conn = DriverManager.getConnection(this.url);
                     Statement stmt  = conn.createStatement();
                     ResultSet rs    = stmt.executeQuery(sql)){
                    ResultSetMetaData rsmd = rs.getMetaData();
                    // loop through the result set
                    while (rs.next()) {
                        String id = "{\"id\":\"" ;
                        String declaration = "\",\"type\":\"relationship\",";
                        String label = rs.getString(2);

                        boolean undirected = false;
                        if(rs.getInt(3) == 0){
                            undirected = false;
                        }
                        else if(rs.getInt(3) == 1){
                            undirected = true;
                        }
                        String props = "";
                        if(rsmd.getColumnCount() > 5){
                            props = props + ",\"properties\":{";
                            int notNull = 0;
                            for (int i = 6; i <= rsmd.getColumnCount(); i++) {
                                String value = rs.getString(i);
                                if (value != null) {
                                    notNull++;
                                    String data = "\"" + rsmd.getColumnLabel(i) + "\":";
                                    if(value.startsWith("[") && value.endsWith("]")){
                                        //pasarlo a lista
                                        if(value.equals("[]")){
                                            data = data + "[]";
                                        }
                                        else if(value.split("").length > 3){
                                            String aux = value.substring(1,value.length()-1);
                                            String[] names = aux.split(",");
                                            for(int j = 0; j < names.length; j++){
                                                if(j == 0){
                                                    data = data + "[";
                                                }
                                                data = data + "\"" + names[j] + "\"";
                                                if(j+1 < names.length){
                                                    data = data + ",";
                                                }
                                            }
                                            data = data +"]";
                                        }
                                    }
                                    else if(value.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                        if(value.contains(".")){
                                            data = data + Float.parseFloat(value);
                                        }
                                        else{
                                            data = data + Integer.parseInt(value);
                                        }
                                    }
                                    else if(value.equals("true") || value.equals("TRUE")){
                                        data = data + "T";
                                    }
                                    else if(value.equals("false") || value.equals("FALSE")){
                                        data = data + "F";
                                    }
                                    else{
                                        data = data + "\"" + value + "\"";
                                    }
                                    props = props + data;
                                }
                                if(i+1 <= rsmd.getColumnCount() && rs.getString(i+1) != null && notNull > 0){
                                    props = props + ",";
                                }
                            }
                            props = props + "},";
                        }

                        String source = ":{";
                        String idSource = rs.getString(4);
                        String target = ":{";
                        String idTarget = rs.getString(5);
                        boolean s = false;
                        boolean t = false;
                        sql = "SELECT * FROM nodeData";
                        try (
                             Statement stmt2  = conn.createStatement();
                             ResultSet nodes    = stmt2.executeQuery(sql)){
                            while (nodes.next()) {
                                if (nodes.getString(1).equals(idSource)) {
                                    source = source + "\"id\":\"" + (nodes.getRow()-1) + "\"}";
                                    String nodeLab = nodes.getString(2);
                                    /*if (nodeLab != null) {
                                        if (nodeLab.equals("[]")) {
                                            source = source + ",\"labels\":[\"NODE\"]}";
                                        } else if (nodeLab.split("").length > 3) {
                                            String aux = nodeLab.substring(1, nodeLab.length() - 1);
                                            String[] names = aux.split(",");
                                            for (int i = 0; i < names.length; i++) {
                                                if (i == 0) {
                                                    source = source + ",\"labels\":[";
                                                }
                                                source = source + "\"" + names[i] + "\"";
                                                if (i + 1 < names.length) {
                                                    source = source + ",";
                                                }
                                            }
                                            source = source + "]}";
                                        }
                                    }*/
                                    s = true;
                                } else if (nodes.getString(1).equals(idTarget)) {
                                    target = target + "\"id\":\"" + (nodes.getRow()-1) + "\"}";
                                    String nodeLab = nodes.getString(2);
                                   /* if (nodeLab != null) {
                                        if (nodeLab.equals("[]")) {
                                            target = target + ",\"labels\":[\"NODE\"]}";
                                        } else if (nodeLab.split("").length > 3) {
                                            String aux = nodeLab.substring(1, nodeLab.length() - 1);
                                            String[] names = aux.split(",");
                                            for (int i = 0; i < names.length; i++) {
                                                if (i == 0) {
                                                    target = target + ",\"labels\":[";
                                                }
                                                target = target + "\"" + names[i] + "\"";
                                                if (i + 1 < names.length) {
                                                    target = target + ",";
                                                }
                                            }
                                            target = target + "]}";
                                        }
                                    }*/
                                    t = true;
                                }
                                if (t && s) {
                                    break;
                                }
                            }
                            String actualLabel;
                            String finalLine;
                            if (label.equals("[]") || label == null) {
                                actualLabel = "\"label\":\"EDGE\"";
                                finalLine = id + count + declaration + actualLabel + props + "\"start\"" + source + ",\"end\"" + target + "}";
                                writer.write(finalLine + "\n");
                                count++;
                                if (undirected) {
                                    finalLine = id + count + declaration + actualLabel + props +"\"start\"" + target + ",\"end\"" + source + "}";
                                    writer.write(finalLine + "\n");
                                    count++;
                                }
                            } else if (label.split("").length > 3) {
                                String aux = label.substring(1, label.length() - 1);
                                String[] names = aux.split(",");
                                for (int i = 0; i < names.length; i++) {
                                    actualLabel = "\"label\":";
                                    actualLabel = actualLabel + "\"" + names[i] + "\"";
                                    finalLine = id + count + declaration + actualLabel + props + "\"start\"" + source + ",\"end\"" + target + "}";
                                    writer.write(finalLine + "\n");
                                    count++;
                                    if (undirected) {
                                        finalLine = id + count + declaration + actualLabel + props +"\"start\"" + target + ",\"end\"" + source + "}";
                                        writer.write(finalLine + "\n");
                                        count++;
                                    }
                                }
                            }
                        }catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }


        }
    }

    public void exportToYARSPG(String directory, String fileName){
        File graphFile = new File (directory+fileName+".ypg");
        if(this.storageType.equals("memory")){
            try (FileWriter writer = new FileWriter(graphFile)) {
                for(Node n: this.nodes){
                    String line = "";
                    line += n.getId();
                    int count = 0;
                    List<String> lab = n.getLabels();
                    Iterator<String> it = lab.iterator();
                    line+="[";
                    while(it.hasNext()){
                        String l = it.next();
                        count ++;
                        if(count < lab.size()){
                            line += l + ",";
                        }
                        else{
                            line += l;
                        }
                    }
                    line+="]:{";

                    count = 0;
                    List<Property> props = n.getProperties();
                    Iterator<Property> it2 = props.iterator();
                    while(it2.hasNext()){
                        Property p = it2.next();
                        line += p.getKey()+":";
                        count++;

                        ArrayList<String> value = new ArrayList(p.getValue());
                        if(value.isEmpty()){
                            line += "";
                        }
                        else if( value.size() >= 1){
                            String data = "";
                            int ready = 0;
                            if(value.size() <= 1){
                                data = "";
                            }
                            else{
                                data = "[";
                            }
                            for(String v : value){
                                if(v == null){
                                    data = data + "N";
                                }
                                else if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                    if(v.contains(".")){
                                        data = data + Float.parseFloat(v);
                                    }
                                    else{
                                        data = data + Integer.parseInt(v);
                                    }
                                }
                                else if(v.equals("true") || v.equals("TRUE")){
                                    data = data + "T";
                                }
                                else if(v.equals("false") || v.equals("FALSE")){
                                    data = data + "F";
                                }

                                else{
                                    data = data + "\"" + v + "\"";
                                }
                                ready++;
                                if(value.size()>1 && ready< value.size()){
                                    data = data+",";
                                }
                            }
                            if(value.size() > 1){
                                data = data+"]";
                            }
                            line += data;
                        }
                        if(count < props.size()){
                            line +=   ",";
                        }
                    }
                    line += "}";
                    writer.write(line + "\n");
                }
                for(Edge e: this.edges){
                    String line = "";
                    line += "(" + e.getSource() + ")";

                    line += "-[";
                    List<String> lab = e.getLabels();
                    Iterator<String> it = lab.iterator();
                    int count = 0;
                    while(it.hasNext()){
                        String l = it.next();
                        count ++;
                        if(count < lab.size()){
                            line += l + ",";
                        }
                        else{
                            line += l +" ";
                        }
                    }

                    List<Property> props = e.getProperties();
                    if(!props.isEmpty()){
                        line += "{";
                        Iterator<Property> it2 = props.iterator();
                        while(it2.hasNext()){
                            Property p = it2.next();
                            line += p.getKey()+":";
                            count++;

                            ArrayList<String> value = new ArrayList(p.getValue());
                            if(value.isEmpty()){
                                line += "";
                            }
                            else if( value.size() >= 1){
                                String data = "";
                                int ready = 0;
                                if(value.size() <= 1){
                                    data = "";
                                }
                                else{
                                    data = "[";
                                }
                                for(String v : value){
                                    if(v == null){
                                        data = data + "N";
                                    }
                                    else if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                        if(v.contains(".")){
                                            data = data + Float.parseFloat(v);
                                        }
                                        else{
                                            data = data + Integer.parseInt(v);
                                        }
                                    }
                                    else if(v.equals("true") || v.equals("TRUE")){
                                        data = data + "T";
                                    }
                                    else if(v.equals("false") || v.equals("FALSE")){
                                        data = data + "F";
                                    }

                                    else{
                                        data = data + "\"" + v + "\"";
                                    }
                                    ready++;
                                    if(value.size()>1 && ready< value.size()){
                                        data = data+",";
                                    }
                                }
                                if(value.size() > 1){
                                    data = data+"]";
                                }
                                line += data;
                            }
                            if(count < props.size()){
                                line +=   ",";
                            }
                        }

                        line += "}";
                    }

                    if(e.getUndirected()){
                        line += "]-";
                    }
                    else{
                        line += "]->";
                    }
                    line += "(" + e.getTarget() + ")";

                    writer.write(line + "\n");
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        else{

        }
    }

    public void exportToGraphML(String directory, String fileName){
        File graphFile = new File (directory+fileName+".graphml");
    }

    public void importData(String dataFile) throws IOException, SQLException {
        try(BufferedReader br = new BufferedReader(new FileReader(dataFile))){
            String line;
            if(this.storageType.equals("memory")){
                ArrayList<String> nodeProp = new ArrayList<>();
                ArrayList<String> edgeProp = new ArrayList<>();
                int numLine = 0;
                while((line = br.readLine()) != null){
                    if(numLine == 0){
                        boolean edge = false;
                        String[] props = line.split(";");
                        if( props[0].equals("@id") && props[1].equals("@label")){
                            int count = 2;
                            while(count < props.length){
                                if(props[count].equals("@undirected") && props[count+1].equals("@source") && props[count+2].equals("@target")){
                                    edge = true;
                                    count+=3;
                                    continue;
                                }
                                if(!edge){
                                    nodeProp.add(props[count]);
                                    this.nodeProperties.add(props[count]);
                                }
                                else{
                                    edgeProp.add(props[count]);
                                    this.edgeProperties.add(props[count]);
                                }
                                count++;
                            }
                        }
                        else{
                            System.out.println("Error at line " + numLine+". Wrong format.");
                        }
                    }
                    else{
                        String[] data = line.split(";");
                        if(data.length < (5+nodeProp.size())){
                            //ES NODO
                            Node n = new Node(data[0].substring(1,data[0].length()-1));
                            String labels = data[1];
                            if(!labels.equals("[]") || !labels.equals("")){
                                labels = labels.substring(1,labels.length()-1);
                                String[] names = labels.split(",");
                                for(String s: names){
                                    n.addLabel(s.substring(1,s.length()-1));
                                }
                            }
                            for(int i = 2; i < data.length; i++){
                                if(!data[i].isBlank()){
                                    ArrayList<String> pv = new ArrayList<>();
                                    if(data[i].startsWith("[") && data[i].endsWith("]")){
                                        String[] d = data[i].substring(1,data[i].length()-1).split(",");
                                        for(String s : d){
                                            if(s.equals("N")){
                                                pv.add(null);
                                            }
                                            else if(s.equals("T")){
                                                pv.add("TRUE");
                                            }
                                            else if(s.equals("F")){
                                                pv.add("FALSE");
                                            }
                                            else if(s.startsWith("\"") && s.endsWith("\"")){
                                                pv.add(s.substring(1,s.length()-1));
                                            }
                                            else{
                                                pv.add(s);
                                            }
                                        }
                                    }
                                    else{
                                        if(data[i].equals("N")){
                                            pv.add(null);
                                        }
                                        else if(data[i].equals("T")){
                                            pv.add("TRUE");
                                        }
                                        else if(data[i].equals("F")){
                                            pv.add("FALSE");
                                        }
                                        else if(data[i].startsWith("\"") && data[i].endsWith("\"")){
                                            pv.add(data[i].substring(1,data[i].length()-1));
                                        }
                                        else if (data[i].matches("[-+]?[0-9]*\\.?[0-9]+")){
                                            pv.add(data[i]);
                                        }
                                        else{
                                            throw new IllegalArgumentException("Not valid value at line "+ numLine+ "value "+(i+2));
                                        }
                                    }
                                    Property p = new Property(nodeProp.get(i-2),pv);
                                    n.addProperty(p);
                                }
                            }
                            this.nodes.add(n);
                        }
                        else{
                            //EDGE
                            String id = data[0];
                            id = id.substring(1,id.length()-1);

                            String source = data[nodeProp.size()+3];
                            source = source.substring(1,source.length()-1);

                            String target = data[nodeProp.size()+4];
                            target = target.substring(1,target.length()-1);

                            Edge e = new Edge(id,source,target);

                            String undirected = data[nodeProp.size()+2];
                            if(undirected.equals("T")){
                                e.changeDirection(true);
                            }
                            else if(undirected.equals("F")){
                                e.changeDirection(false);
                            }
                            String labels = data[1];
                            if(!labels.equals("[]") && !labels.equals("")){
                                labels = labels.substring(1,labels.length()-1);
                                String[] names = labels.split(",");
                                for(String s: names){
                                    e.addLabel(s.substring(1,s.length()-1));
                                }
                            }
                            for(int i = 5+nodeProp.size(); i < data.length; i++){
                                if(!data[i].isBlank()){
                                    ArrayList<String> pv = new ArrayList<>();
                                    if(data[i].startsWith("[") && data[i].endsWith("]")){
                                        String[] d = data[i].substring(1,data[i].length()-1).split(",");
                                        for(String s : d){
                                            if(s.equals("N")){
                                                pv.add(null);
                                            }
                                            else if(s.equals("T")){
                                                pv.add("TRUE");
                                            }
                                            else if(s.equals("F")){
                                                pv.add("FALSE");
                                            }
                                            else if(s.startsWith("\"") && s.endsWith("\"")){
                                                pv.add(s.substring(1,s.length()-1));
                                            }
                                            else{
                                                pv.add(s);
                                            }
                                        }
                                    }
                                    else{
                                        if(data[i].equals("N")){
                                            pv.add(null);
                                        }
                                        else if(data[i].equals("T")){
                                            pv.add("TRUE");
                                        }
                                        else if(data[i].equals("F")){
                                            pv.add("FALSE");
                                        }
                                        else if (data[i].matches("[-+]?[0-9]*\\.?[0-9]+")){
                                            pv.add(data[i]);
                                        }
                                        else if(data[i].startsWith("\"") && data[i].endsWith("\"")){
                                            pv.add(data[i].substring(1,data[i].length()-1));
                                        }
                                        else{
                                            throw new IllegalArgumentException("Not valid value at line "+ numLine+ "value "+(i));
                                        }
                                    }
                                    Property p = new Property(edgeProp.get(i-5-nodeProp.size()),pv);
                                    e.addProperty(p);
                                }
                            }
                            this.edges.add(e);
                        }
                    }
                    numLine++;
                }
            }
            else{
                int numLine = 0;
                try (Connection conn = DriverManager.getConnection(this.url)) {
                    Statement stmt = conn.createStatement();
                    stmt.execute("PRAGMA synchronous = OFF");
                    stmt.execute("PRAGMA journal_mode = OFF");
                    stmt.execute("PRAGMA locking_mode = EXCLUSIVE");
                    stmt.execute("PRAGMA temp_store = MEMORY");
                    conn.setAutoCommit(false);

                    int numNodeProps = 0;
                    StringBuilder newNode = new StringBuilder("INSERT INTO nodeData(nodeID,nodeLabel");
                    StringBuilder newEdge = new StringBuilder("INSERT INTO edgeData(edgeID,edgeLabel,_undirected,_source,_target");

                    StringBuilder stmtNodeValues = new StringBuilder("VALUES (?,?");
                    StringBuilder stmtEdgeValues = new StringBuilder("VALUES (?,?,?,?,?");

                    PreparedStatement pstmtN = null;
                    PreparedStatement pstmtE = null;
                    while ((line = br.readLine()) != null) {
                        if (numLine == 0) {
                            String[] props = line.split(";");
                            if(props[0].equals("@id") && props[1].equals("@label")){

                                boolean edge = false;
                                String sqlNode = "ALTER TABLE nodeData ADD COLUMN ";
                                String sqlEdge = "ALTER TABLE edgeData ADD COLUMN ";
                                for(int i = 2; i < props.length; i++){
                                    if(props[i].equals("@undirected") && props[i+1].equals("@source") && props[i+2].equals("@target")){
                                        i+=2;
                                        edge = true;
                                        newNode.append(") ");
                                        continue;
                                    }
                                    if(!edge){
                                        stmt.execute(sqlNode+"'"+props[i]+"'");
                                        numNodeProps++;
                                        newNode.append(","+"'"+props[i]+"'");
                                        stmtNodeValues.append(",?");
                                    }
                                    else{
                                        stmt.execute(sqlEdge+"'"+props[i]+"'");
                                        newEdge.append(","+"'"+props[i]+"'");
                                        stmtEdgeValues.append(",?");
                                    }
                                }
                                newEdge.append(") ");
                                stmtNodeValues.append(")");
                                stmtEdgeValues.append(")");
                                pstmtN = conn.prepareStatement(newNode.toString() + stmtNodeValues.toString());
                                pstmtE = conn.prepareStatement(newEdge.toString() + stmtEdgeValues.toString());
                            }
                            else{
                                //no esta en el orden @id;@label;
                                System.out.println("wrong format");
                                break;
                            }

                        }
                        else {
                            String[] values = line.split(";",-1);
                            if(values.length < (2+numNodeProps +3)){
                                //ES NODO
                                String id = values[0].substring(1,values[0].length()-1);
                                pstmtN.setString(1,id);
                                pstmtN.setString(2,values[1]);
                                for(int i = 2; i < values.length -1; i++){
                                    if(!values[i].isBlank()){
                                        if(values[i].equals("T")){
                                            pstmtN.setString(i+1,"True");
                                        }
                                        else if(values[i].equals("F")){
                                            pstmtN.setString(i+1,"False");
                                        }
                                        else if(values[i].equals("N")){
                                            pstmtN.setString(i+1,"Null");
                                        }
                                        else if(values[i].startsWith("\"") && values[i].endsWith("\"")){
                                            String clean = values[i].substring(1,values[i].length()-1);
                                            pstmtN.setString(i+1,clean);
                                        }
                                        else{
                                            pstmtN.setString(i+1,values[i]);
                                        }
                                    }
                                    else{
                                        pstmtN.setNull(i+1,java.sql.Types.NULL);
                                    }
                                }
                                pstmtN.executeUpdate();
                            }
                            else{
                                //ES ARISTA
                                String id = values[0].substring(1,values[0].length()-1);
                                pstmtE.setString(1,id);
                                pstmtE.setString(2,values[1]);

                                if(values[2+numNodeProps].equals("T")){
                                    pstmtE.setString(3,"True");
                                }
                                else{
                                    pstmtE.setString(3,"False");
                                }

                                StringBuilder source = new StringBuilder().append(values[3+numNodeProps],1,values[3+numNodeProps].length()-1);
                                pstmtE.setString(4,source.toString());

                                StringBuilder target = new StringBuilder().append(values[4+numNodeProps],1,values[4+numNodeProps].length()-1);
                                pstmtE.setString(5,target.toString());

                                int count = 6;
                                for(int i = 5+numNodeProps; i < values.length -1; i++){
                                    if(!values[i].isBlank()){
                                        if(values[i].equals("T")){
                                            pstmtE.setString(count,"True");
                                        }
                                        else if(values[i].equals("F")){
                                            pstmtE.setString(count,"False");
                                        }
                                        else if(values[i].equals("N")){
                                            pstmtE.setString(count,"Null");
                                        }
                                        else if(values[i].startsWith("\"") && values[i].endsWith("\"")){
                                            String clean = values[i].substring(1,values[i].length()-1);
                                            pstmtE.setString(count, clean);
                                        }
                                        else{
                                            pstmtE.setString(count,values[i]);
                                        }
                                    }
                                    else{
                                        pstmtE.setNull(count,java.sql.Types.NULL);
                                    }
                                    count++;
                                }
                                pstmtE.executeUpdate();
                            }
                        }
                        if( (numLine % 100000) == 0 ){
                            conn.commit();
                        }
                        numLine++;
                    }
                    conn.commit();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }


    }

    public void exportPGExample() throws IOException{
        PropertyGraph pg = new PropertyGraph();
        ArrayList<String> prop = new ArrayList<>();

        //Primer nodo
        Node n1 = new Node("Author1");
        n1.addLabel("Author");
        prop.add("John");
        n1.addProperty(new Property("fname",prop));
        prop.clear();
        prop.add("Smith");
        n1.addProperty(new Property("lname",prop));
        prop.clear();

        //Segundo nodo
        Node n2 = new Node("Author2");
        n2.addLabel("Author");
        prop.add("Alice");
        n2.addProperty(new Property("fname",prop));
        prop.clear();
        prop.add("Brown");
        n2.addProperty(new Property("lname",prop));
        prop.clear();

        //Tercer nodo
        Node n3 = new Node("EI01");
        n3.addLabel("Entry");
        n3.addLabel("InProc");
        prop.add("Serialization for...");
        n3.addProperty(new Property("title",prop));
        prop.clear();
        prop.add("10");
        n3.addProperty(new Property("numpages",prop));
        prop.clear();
        prop.add("Graph Database");
        n3.addProperty(new Property("keyword",prop));
        prop.clear();

        //Cuarto nodo
        Node n4 = new Node("EA01");
        n4.addLabel("Entry");
        n4.addLabel("Article");
        prop.add("Property Graph...");
        n4.addProperty(new Property("title",prop));
        prop.clear();
        prop.add("10");
        n4.addProperty(new Property("numpages",prop));
        prop.clear();
        prop.add("Query");
        prop.add("Graph");
        n4.addProperty(new Property("keyword",prop));
        prop.clear();

        //Quinto nodo
        Node n5 = new Node("Proc01");
        n5.addLabel("Proceedings");
        prop.add("BDAS");
        n5.addProperty(new Property("title",prop));
        prop.clear();
        prop.add("2018");
        n5.addProperty(new Property("year",prop));
        prop.clear();
        prop.add("May");
        n5.addProperty(new Property("month",prop));
        prop.clear();

        //Sexto nodo
        Node n6 = new Node("Jour01");
        n6.addLabel("Journal");
        prop.add("J. DB");
        n6.addProperty(new Property("title",prop));
        prop.clear();
        prop.add("2020");
        n6.addProperty(new Property("year",prop));
        prop.clear();
        prop.add("30");
        n6.addProperty(new Property("vol",prop));
        prop.clear();


        //Primer arista
        Edge e1 = new Edge("e1",n3.getId(),n1.getId());
        e1.addLabel("has_author");
        prop.add("1");
        e1.addProperty(new Property("order",prop));
        prop.clear();

        //Segunda arista
        Edge e2 = new Edge("e2",n3.getId(),n2.getId());
        e2.addLabel("has_author");
        prop.add("2");
        e2.addProperty(new Property("order",prop));
        prop.clear();

        //Tercera arista
        Edge e3 = new Edge("e3",n4.getId(),n2.getId());
        e3.addLabel("has_author");
        prop.add("1");
        e3.addProperty(new Property("order",prop));
        prop.clear();

        //Cuarta arista
        Edge e4 = new Edge("e4",n4.getId(),n3.getId());
        e4.addLabel("cites");

        //Quinta arista
        Edge e5 = new Edge("e5",n1.getId(),n5.getId());
        e5.addLabel("booktitle");
        prop.add("111-121");
        e5.addProperty(new Property("pages",prop));
        prop.clear();

        //Sexta arista
        Edge e6 = new Edge("e6",n4.getId(),n6.getId());
        e6.addLabel("published_in");
        prop.add("222-232");
        e6.addProperty(new Property("pages",prop));
        prop.clear();


        pg.addNode(n1);
        pg.addNode(n2);
        pg.addNode(n3);
        pg.addNode(n4);
        pg.addNode(n5);
        pg.addNode(n6);

        pg.addEdge(e1);
        pg.addEdge(e2);
        pg.addEdge(e3);
        pg.addEdge(e4);
        pg.addEdge(e5);
        pg.addEdge(e6);

        pg.export("","example");
    }

    //GETTERS Y SETTERS

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}
