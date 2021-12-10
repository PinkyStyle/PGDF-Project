package PropertyGraphDF;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    }

    public void exportToJSON(String directory, String fileName) throws IOException {
        if(this.storageType.equals("memory")) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName+".json"),"UTF-8"))) {
                
                StringBuilder sb = new StringBuilder();
                for (Node n : this.nodes) {
                    sb.setLength(0);
                    sb.append("{\"type\":\"node\",\"id\":\"").append(n.getId()).append("\",");

                    if (n.getLabels().size() > 0) {
                        sb.append("\"labels\":[");
                        List<String> labels = n.getLabels();
                        if (labels.size() == 1) {
                            sb.append("\"").append(labels.get(0)).append("\"");
                        } else {
                            for (String s : labels) {
                                sb.append("\"").append(s).append("\",");
                            }
                            sb.delete(sb.length()-1,sb.length());
                        
                        }
                        sb.append("],");
                    }
                    else{
                        sb.append("\"labels\":[\"NODE\"],");
                    }
                    if (n.getProperties().size() > 0) {
                        sb.append("\"properties\":{");
                        List<Property> props = n.getProperties();
                        if (props.size() > 0) {
                            for (int i = 0; i < props.size(); i++) {
                                Property p = props.get(i);
                                sb.append("\"").append(p.getKey()).append("\":");
                                if (p.getValue() == null) {
                                    sb.append("null");
                                } else if (p.getValue().size() > 0) {
                                    //propData = propData + "\"";
                                    int valueSize = 0;
                                    for (int j = 0; j < p.getValue().size(); j++) {
                                        if (j + 1 < p.getValue().size()) {
                                            sb.append("[");
                                        }
                                        if (p.getValue().get(j) == null) {
                                            sb.append("null");
                                            valueSize++;
                                        } else if (p.getValue().get(j).matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                            if (p.getValue().get(j).contains(".")) {
                                                sb.append(Float.parseFloat(p.getValue().get(j)));
                                            } else {
                                                sb.append(Integer.parseInt(p.getValue().get(j)));
                                            }
                                            valueSize++;
                                        } else if (p.getValue().get(j).equals("true") || p.getValue().get(j).equals("TRUE") || p.getValue().get(j).equals("True")) {
                                            sb.append("true");
                                            valueSize++;
                                        } else if (p.getValue().get(j).equals("false") || p.getValue().get(j).equals("FALSE") || p.getValue().get(j).equals("False")) {
                                            sb.append("false");
                                            valueSize++;
                                        } else {
                                            sb.append("\"").append(p.getValue().get(j)).append("\"");
                                            valueSize++;
                                        }
                                        if (valueSize < p.getValue().size()) {
                                            sb.append(",");
                                        } else if (valueSize > 1) {
                                            sb.append("]");
                                        }
                                    }
                                } else {
                                    sb.append("");
                                }
                                if (i + 1 < props.size()) {
                                    sb.append(",");
                                }
                            }                            
                        }
                        sb.append("}");
                    }
                    sb.append("}");
                    writer.write(sb.toString() + "\n");
                }
                sb.setLength(0);
                int idCount = 0;
                for (Edge e : this.edges) {
                    sb.setLength(0);
                    sb.append("\",\"type\":\"relationship\",");

                    StringBuilder pb = new StringBuilder();
                    if (e.getProperties().size() > 0) {
                        pb.append(",\"properties\":{");
                        List<Property> props = e.getProperties();
                        if (props.size() > 0) {
                            for (int i = 0; i < props.size(); i++) {
                                Property p = props.get(i);
                                pb.append("\"").append(p.getKey()).append("\":");
                                if (p.getValue() == null) {
                                    pb.append("null");
                                } else if (p.getValue().size() > 0) {
                                    int valueSize = 0;
                                    for (int j = 0; j < p.getValue().size(); j++) {
                                        if (j + 1 < p.getValue().size()) {
                                            pb.append("[");
                                        }
                                        if (p.getValue().get(j) == null) {
                                            pb.append("null");
                                            valueSize++;
                                        } else if (p.getValue().get(j).matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                            if (p.getValue().get(j).contains(".")) {
                                                pb.append(Float.parseFloat(p.getValue().get(j)));
                                            } else {
                                                pb.append(Integer.parseInt(p.getValue().get(j)));
                                            }
                                            valueSize++;
                                        } else if (p.getValue().get(j).equals("true") || p.getValue().get(j).equals("TRUE") || p.getValue().get(j).equals("True")) {
                                            pb.append("true");
                                            valueSize++;
                                        } else if (p.getValue().get(j).equals("false") || p.getValue().get(j).equals("FALSE") || p.getValue().get(j).equals("False")) {
                                            pb.append("false");
                                            valueSize++;
                                        } else {
                                            pb.append("\"").append(p.getValue().get(j)).append("\"");
                                            valueSize++;
                                        }
                                        if (valueSize < p.getValue().size()) {
                                            pb.append(",");
                                        } else if (valueSize > 1) {
                                            pb.append("]");
                                        }
                                    }
                                } else {
                                    pb.append("");
                                }
                                if (i + 1 < props.size()) {
                                    pb.append(",");
                                }
                            }
                            pb.append("},");
                        }
                    }
                    else{
                        pb.append(",\"properties\":{},");
                    }
                    
                    String source = ":{\"id\":\"" + e.getSource() + "\"}";
                    String target = ":{\"id\":\"" + e.getTarget() + "\"}";
                    if (e.getLabels()== null || e.getLabels().isEmpty()){           
                        writer.write("{\"id\":\""+idCount+sb.toString()+"\"label\":\"EDGE\""+pb.toString()+ "\"start\"" + source + ",\"end\"" + target + "}"+"\n");
                        idCount++;
                        if(e.getUndirected() != null && e.getUndirected()){
                            writer.write("{\"id\":\""+idCount+sb.toString()+"\"label\":\"EDGE\""+pb.toString()+ "\"start\"" + target + ",\"end\"" + source + "}"+"\n");
                            idCount++;
                        }
                    }
                    else{
                        for(String s: e.getLabels()){
                            writer.write("{\"id\":\""+idCount+sb.toString()+"\"label\":\""+s+"\""+pb.toString()+ "\"start\"" + source + ",\"end\"" + target + "}"+"\n");
                            idCount++;
                            if(e.getUndirected() != null && e.getUndirected()){
                                writer.write("{\"id\":\""+idCount+sb.toString()+"\"label\":\""+s+"\""+pb.toString()+ "\"start\"" + target + ",\"end\"" + source + "}"+"\n");
                                idCount++;
                            }
                        }
                    }
                }
            }
        }
        else{
            try (Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName+".json"),"UTF-8"));) {
                try (Connection conn = DriverManager.getConnection(this.url)) {
                    Statement stmt = conn.createStatement();

                    String sql = "SELECT * FROM nodeData";                
                    ResultSet rs = stmt.executeQuery(sql);
                    ResultSetMetaData rsmd = rs.getMetaData();

                    List<String> ids = new ArrayList<>();

                    //Exportar datos de nodos como json
                    int idCount = 0;
                    
                    StringBuilder sb = new StringBuilder();
                    while (rs.next()) {
                        sb.setLength(0);
                        //ids.add(rs.getString(1));
                        sb.append("{\"type\":\"node\",\"id\":\"").append(rs.getString(1)).append("\"");
                        String label = rs.getString(2);
                        if (label != null) {
                            if (label.equals("[]")) {
                                sb.append(",\"labels\":[\"NODE\"]");
                            } else if (label.split("").length > 3) {
                                sb.append(",\"labels\":").append(label);
                            }
                        }
                        if (rsmd.getColumnCount() > 2) {
                            sb.append(",\"properties\":{");
                            int notNull = 0;
                            for (int i = 3; i <= rsmd.getColumnCount(); i++) {
                                String value = rs.getString(i);
                                if (value != null) {
                                    notNull++;
                                    sb.append("\"").append(rsmd.getColumnLabel(i)).append("\":");
                                    if (value.startsWith("[") && value.endsWith("]")) {
                                        //pasarlo a lista
                                        if (value.equals("[]")) {
                                            sb.append("[]");
                                        } else if (value.split("").length > 3) {
                                            sb.append(value);
                                        }
                                    } else if (value.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                        if (value.contains(".")) {
                                            sb.append(Float.parseFloat(value));
                                        } else {
                                            sb.append(Integer.parseInt(value));
                                        }
                                    } else if (value.equals("true") || value.equals("TRUE")|| value.equals("True")) {
                                        sb.append("true");
                                    } else if (value.equals("false") || value.equals("FALSE") || value.equals("False")) {
                                        sb.append("false");
                                    } else if( value.equals("Null")){
                                        sb.append("null");
                                    } else{
                                        sb.append("\"").append(value).append("\"");
                               
                                    }
                                }
                                if (i + 1 <= rsmd.getColumnCount() && rs.getString(i + 1) != null && notNull > 0) {
                                    sb.append(",");
                                }
                            }
                            sb.append("}");
                        }
                        sb.append("}");
                        bw.write(sb.toString()+"\n");
                        idCount++;
                    }

                    //Exportar datos de aristas como json
                    sql = "SELECT * FROM edgeData";
                    idCount = 0;
                    rs = stmt.executeQuery(sql);
                    rsmd = rs.getMetaData();
                    while (rs.next()) {
                        sb.setLength(0);
                        
                        sb.append("\",\"type\":\"relationship\",");
                        String label = rs.getString(2);

                        boolean undirected = false;
                        if (rs.getString(3).equals("False")) {
                            undirected = false;
                        } else if (rs.getString(3).equals("True")) {
                            undirected = true;
                        }
                        StringBuilder props = new StringBuilder();
                        if (rsmd.getColumnCount() > 5) {
                            props.append(",\"properties\":{");
                            int notNull = 0;
                            for (int i = 6; i <= rsmd.getColumnCount(); i++) {
                                String value = rs.getString(i);
                                if (value != null) {
                                    notNull++;
                                    props.append("\"").append(rsmd.getColumnLabel(i)).append("\":");
                                    if (value.startsWith("[") && value.endsWith("]")) {
                                        //pasarlo a lista
                                        if (value.equals("[]")) {
                                            props.append("[]");
                                        } else if (value.split("").length > 3) {
                                            props.append(value);
                                        }
                                    } else if (value.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                        if (value.contains(".")) {
                                            props.append(Float.parseFloat(value));
                                        } else {
                                            props.append(Integer.parseInt(value));
                                        }
                                    } else if (value.equals("true") || value.equals("TRUE")) {
                                        props.append("true");
                                    } else if (value.equals("false") || value.equals("FALSE")) {
                                        props.append("false");
                                    } else {
                                        props.append("\"").append(value).append("\"");
                                    }                                    
                                }
                                if (i + 1 <= rsmd.getColumnCount() && rs.getString(i + 1) != null && notNull > 0) {
                                    props.append(",");
                                }
                            }
                            props.append("},");
                        }

                        String source = ":{";
                        String idSource = rs.getString(4);
                        String target = ":{";
                        String idTarget = rs.getString(5);

                        source = source + "\"id\":\"" + idSource + "\"}";
                        target = target + "\"id\":\"" + idTarget + "\"}";
                        String actualLabel;
                        if (label.equals("[]") || label == null) {
                            actualLabel = "\"label\":\"EDGE\"";
                            bw.write("{\"id\":\""+idCount+sb.toString()+actualLabel+props.toString()+ "\"start\"" + source + ",\"end\"" + target + "}"+"\n");
                            idCount++;
                            if (undirected) {
                                bw.write("{\"id\":\""+idCount+sb.toString()+actualLabel+props.toString()+ "\"start\"" + target + ",\"end\"" + source + "}"+"\n");
                                idCount++;
                            }
                        } else if (label.split("").length > 3) {
                            String aux = label.substring(1, label.length() - 1);
                            String[] names = aux.split(",");
                            for (int i = 0; i < names.length; i++) {
                                actualLabel = "\"label\":";
                                actualLabel = actualLabel + names[i];
                                bw.write("{\"id\":\""+idCount+sb.toString()+actualLabel+props.toString()+ "\"start\"" + source + ",\"end\"" + target + "}"+"\n");
                                idCount++;
                                if (undirected) {
                                    bw.write("{\"id\":\""+idCount+sb.toString()+actualLabel+props.toString()+ "\"start\"" + target + ",\"end\"" + source + "}"+"\n");
                                    idCount++;
                                }
                            }
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
            try( FileWriter writer = new FileWriter(graphFile);
                BufferedWriter bw = new BufferedWriter(writer)){
                try (Connection conn = DriverManager.getConnection(this.url)) {
                    Statement stmt = conn.createStatement();
                    String sql = "SELECT * FROM nodeData";
                    ResultSet rs = stmt.executeQuery(sql);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    StringBuilder sb = new StringBuilder();
                    while(rs.next()){
                        sb.setLength(0);
                        //add id
                        sb.append(rs.getString(1));

                        //add labels
                        sb.append(rs.getString(2).replaceAll("\"",""));

                        //add props if it has
                        if(rsmd.getColumnCount() > 2){
                            int props = 0;
                            for(int i = 3; i <= rsmd.getColumnCount(); i++){
                                String value = rs.getString(i);
                                if(value != null && !value.isBlank()){
                                    if(props == 0){
                                        props ++;
                                        sb.append(":{");
                                    }
                                    sb.append(rsmd.getColumnLabel(i)+":");
                                    if (value.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                        if(value.contains(".")){
                                            sb.append(Float.parseFloat(value));
                                        }
                                        else{
                                            sb.append(Integer.parseInt(value));
                                        }
                                    }
                                    else if(value.equals("True")){
                                        sb.append("true");
                                    }
                                    else if(value.equals("False")){
                                        sb.append("false");
                                    }
                                    else if(value.equals("Null")){
                                        sb.append("null");
                                    }
                                    else if(value.startsWith("[") && value.endsWith("]")){
                                        sb.append(value);
                                    }
                                    else{
                                        sb.append("\""+value+"\"");
                                    }
                                }
                                if(i+1 <= rsmd.getColumnCount() && rs.getString(i + 1) != null && props > 0){
                                    sb.append(",");
                                }
                            }
                            sb.append("}\n");
                        }
                        bw.write(sb.toString());
                    }

                    sql = "SELECT * FROM edgeData";
                    rs = stmt.executeQuery(sql);
                    rsmd = rs.getMetaData();

                    while(rs.next()){
                        sb.setLength(0);
                        //add source
                        sb.append("("+ rs.getString(4) + ")-[");
                        //add labels

                        if(!rs.getString(2).equals("[]")){
                            String labels = rs.getString(2).replaceAll("\"","");
                            sb.append(labels, 1, labels.length()-1);
                        }
                        //add props
                        if(rsmd.getColumnCount() > 5){
                            int props = 0;
                            for(int i = 6; i <= rsmd.getColumnCount(); i++){
                                String value = rs.getString(i);
                                if(value != null && !value.isBlank()){
                                    if(props == 0){
                                        props ++;
                                        sb.append(" {");
                                    }
                                    sb.append(rsmd.getColumnLabel(i)+":");
                                    if (value.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                        if(value.contains(".")){
                                            sb.append(Float.parseFloat(value));
                                        }
                                        else{
                                            sb.append(Integer.parseInt(value));
                                        }
                                    }
                                    else if(value.equals("True")){
                                        sb.append("true");
                                    }
                                    else if(value.equals("False")){
                                        sb.append("false");
                                    }
                                    else if(value.equals("Null")){
                                        sb.append("null");
                                    }
                                    else if(value.startsWith("[") && value.endsWith("]")){
                                        sb.append(value);
                                    }
                                    else{
                                        sb.append("\""+value+"\"");
                                    }
                                }
                                if(i+1 <= rsmd.getColumnCount() && rs.getString(i + 1) != null && props > 0){
                                    sb.append(",");
                                }
                            }
                            if(props > 0){
                                sb.append("}");
                            }
                        }

                        //add direction
                        if(rs.getString(3).equals("False")){
                            sb.append("]->");
                        }
                        else{
                            sb.append("]-");
                        }
                        //add target

                        sb.append("("+ rs.getString(5) + ")");
                        bw.write(sb.toString()+"\n");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void exportToGraphML(String directory, String fileName) {
        if(this.storageType.equals("memory")){
            try( Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName+".graphml"),"UTF-8"))){
                //header
                w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                w.write("\n<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");
                w.write("\n\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
                w.write("\n\txsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns");
                w.write("\n\thttp://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
                w.write("\n<graph id=\"G\" edgedefault=\"undirected\">\n");
                StringBuilder sb = new StringBuilder();
                //props
                for(String s: this.nodeProperties){
                    sb.append("<key attr.name=\"").append(s).append("\" attr.type=\"string\" for=\"node\" id=\"").append(s).append("\"/>\n");
                }
                w.write(sb.toString());
                sb.setLength(0);
                for(String s: this.edgeProperties){
                    sb.append("<key attr.name=\"").append(s).append("\" attr.type=\"string\" for=\"edge\" id=\"").append(s).append("\"/>\n");
                }
                w.write(sb.toString());
                sb.setLength(0);
                //nodos
                for(Node node: this.nodes){
                    sb.append("<node id=\"").append(node.getId()).append("\">\n");
                    //label
                    List<String> labels = node.getLabels();
                    if(labels.size() > 0 ){
                        sb.append("<data key=\"labels\">[");
                        int ready = 0;
                        for(String l : labels){
                            sb.append("\"").append(l).append("\"");
                            ready++;
                            if(ready < labels.size()){
                                sb.append(",");
                            }
                        }
                        sb.append("]</data>\n");
                    }                                        
                    //props
                    List<Property> props = node.getProperties();
                    Iterator<Property> it2 = props.iterator();
                    while(it2.hasNext()){
                        Property p = it2.next();                        
                                          
                        ArrayList<String> value = new ArrayList(p.getValue());
                        
                        if( value.size() >= 1){
                            sb.append("<data key=\"").append(p.getKey()).append("\">");      
                            String data = "";
                            int ready = 0;
                            if(value.size() > 1){
                                sb.append("[");
                            }                            
                            for(String v : value){
                                if(v == null){
                                    sb.append("Null");
                                }
                                else if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                    if(v.contains(".")){
                                        sb.append(Float.parseFloat(v));
                                    }
                                    else{
                                        sb.append(Integer.parseInt(v));
                                    }
                                }
                                else if(v.equals("true") || v.equals("TRUE")){
                                    sb.append("True");
                                }
                                else if(v.equals("false") || v.equals("FALSE")){
                                    sb.append("False");
                                }

                                else{
                                    sb.append("\"").append(v).append("\"");
                                }
                                ready++;
                                if(value.size()>1 && ready< value.size()){
                                   sb.append(",");
                                }
                            }
                            if(value.size() > 1){
                                sb.append("]");
                            }
                            sb.append("</data>\n");
                        }
                    }
                    sb.append("</node>\n");
                    w.write(sb.toString());
                    sb.setLength(0);
                }
                sb.setLength(0);
                //aristas
                for(Edge edge:this.edges){
                    sb.append("<edge id=\"").append(edge.getId()).append("\" ");
                    if(!edge.getUndirected()){
                        sb.append("directed=\"true\" ");
                    }
                    sb.append("source=\"").append(edge.getSource()).append("\" target=\"").append(edge.getTarget()).append("\"/>\n");
                    
                     //label
                    List<String> labels = edge.getLabels();
                    if(labels.size() > 0 ){
                        sb.append("<data key=\"labels\">[");
                        int ready = 0;
                        for(String l : labels){
                            sb.append("\"").append(l).append("\"");
                            ready++;
                            if(ready < labels.size()){
                                sb.append(",");
                            }
                        }
                        sb.append("]</data>\n");
                    }
                    //props
                    List<Property> props = edge.getProperties();
                    Iterator<Property> it2 = props.iterator();
                    while(it2.hasNext()){
                        Property p = it2.next();                        
                                          
                        ArrayList<String> value = new ArrayList(p.getValue());
                        
                        if( value.size() >= 1){
                            sb.append("<data key=\"").append(p.getKey()).append("\">");      
                            String data = "";
                            int ready = 0;
                            if(value.size() > 1){
                                sb.append("[");
                            }                            
                            for(String v : value){
                                if(v == null){
                                    sb.append("Null");
                                }
                                else if(v.matches("[-+]?[0-9]*\\.?[0-9]+")){
                                    if(v.contains(".")){
                                        sb.append(Float.parseFloat(v));
                                    }
                                    else{
                                        sb.append(Integer.parseInt(v));
                                    }
                                }
                                else if(v.equals("true") || v.equals("TRUE")){
                                    sb.append("True");
                                }
                                else if(v.equals("false") || v.equals("FALSE")){
                                    sb.append("False");
                                }

                                else{
                                    sb.append("\"").append(v).append("\"");
                                }
                                ready++;
                                if(value.size()>1 && ready< value.size()){
                                   sb.append(",");
                                }
                            }
                            if(value.size() > 1){
                                sb.append("]");
                            }
                            sb.append("</data>\n");
                        }
                    }
                    sb.append("</edge>\n");
                    w.write(sb.toString());
                    sb.setLength(0);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } 
        }
        else{
            try( Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName+".graphml"),"UTF-8"))){
                try (Connection conn = DriverManager.getConnection(this.url)) {
                    w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    w.write("\n<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");
                    w.write("\n\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
                    w.write("\n\txsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns");
                    w.write("\n\thttp://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
                    w.write("\n<graph id=\"G\" edgedefault=\"undirected\">\n");
                    
                    Statement stmt = conn.createStatement();
                    StringBuilder sb = new StringBuilder();
                    
                    //Get node props
                    String sql = "SELECT * from nodeData LIMIT 0";
                    ResultSet rs = stmt.executeQuery(sql);
                    ResultSetMetaData rsmd = rs.getMetaData();                                                                                                                      
                    for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                        switch(rsmd.getColumnLabel(i)){
                            case("nodeID"):
                            case("nodeLabel"):
                                break;
                            default:
                                sb.append("<key attr.name=\"").append(rsmd.getColumnLabel(i)).append("\" attr.type=\"string\" for=\"node\" id=\"").append(rsmd.getColumnLabel(i)).append("\"/>\n");
                                break;
                        }
                    }
                    w.write(sb.toString());
                    sb.setLength(0);
                    
                    //get edge props
                    sql = "SELECT * from edgeData LIMIT 0";
                    rs = stmt.executeQuery(sql);
                    rsmd = rs.getMetaData();
                    for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                        switch(rsmd.getColumnLabel(i)){
                            case("edgeID"):
                            case("edgeLabel"):
                            case("_undirected"):
                            case("_source"):
                            case("_target"):
                                break;
                            default:
                                sb.append("<key attr.name=\"").append(rsmd.getColumnLabel(i)).append("\" attr.type=\"string\" for=\"edge\" id=\"").append(rsmd.getColumnLabel(i)).append("\"/>\n");
                                break;
                        }
                    }
                    w.write(sb.toString());
                    sb.setLength(0);
                    
                    //get nodes
                    sql = "SELECT * FROM nodeData";
                    rs = stmt.executeQuery(sql);
                    rsmd = rs.getMetaData();
                    while(rs.next()){
                        sb.append("<node id=\"").append(rs.getString(1)).append("\">\n");
                        sb.append("<data key=\"labels\">").append(rs.getString(2)).append("</data>\n");
                        for(int i = 3; i <= rsmd.getColumnCount();i++){
                            String data = rs.getString(i);
                            if(data != null){
                                sb.append("<data key=\"").append(rsmd.getColumnLabel(i)).append("\">");      
                                if(data.equals("Null")){
                                    sb.append("Null");
                                }
                                else if(data.equals("true")){
                                    sb.append("True");
                                }
                                else if(data.equals("false")){
                                    sb.append("False");
                                }
                                else if(data.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                    if (data.contains(".")) {
                                        sb.append(Float.parseFloat(data));
                                    } else {
                                        sb.append(Integer.parseInt(data));
                                    }
                                }
                                else if(data.startsWith("[") && data.endsWith("]")){
                                    sb.append(data);
                                }
                                else{
                                    sb.append("\"").append(data).append("\"");
                                }
                                sb.append("</data>\n");
                            }
                        }
                        sb.append("</node>\n");
                        w.write(sb.toString());
                        sb.setLength(0);
                    }
                    
                    //get edges
                    sql = "SELECT * FROM edgeData";
                    rs = stmt.executeQuery(sql);
                    rsmd = rs.getMetaData();
                    while(rs.next()){
                        sb.append("<edge id=\"").append(rs.getString(1)).append("\" ");
                        if(!rs.getString(3).equals("True")){
                            sb.append("directed=\"true\" ");
                        }
                        sb.append("source=\"").append(rs.getString(4)).append("\" target=\"").append(rs.getString(5)).append("\"/>\n");

                         //label
                        String labels = rs.getString(2);
                        if(!labels.equals("[]")){
                            sb.append("<data key=\"labels\">").append(labels).append("</data>\n");
                        }
                        for(int i = 6; i <= rsmd.getColumnCount();i++){
                            String data = rs.getString(i);
                            if(data != null){
                                sb.append("<data key=\"").append(rsmd.getColumnLabel(i)).append("\">");      
                                if(data.equals("Null")){
                                    sb.append("Null");
                                }
                                else if(data.equals("true")){
                                    sb.append("True");
                                }
                                else if(data.equals("false")){
                                    sb.append("False");
                                }
                                else if(data.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                                    if (data.contains(".")) {
                                        sb.append(Float.parseFloat(data));
                                    } else {
                                        sb.append(Integer.parseInt(data));
                                    }
                                }
                                else if(data.startsWith("[") && data.endsWith("]")){
                                    sb.append(data);
                                }
                                else{
                                    sb.append("\"").append(data).append("\"");
                                }
                                sb.append("</data>\n");
                            }
                        }
                        sb.append("</edge>\n");
                        w.write(sb.toString());
                        sb.setLength(0);
                    }                    
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
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
/*                    stmt.execute("PRAGMA locking_mode = EXCLUSIVE");
                    stmt.execute("PRAGMA temp_store = MEMORY");*/
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
