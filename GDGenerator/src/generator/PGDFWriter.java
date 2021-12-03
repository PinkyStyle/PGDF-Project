package generator;

public class PGDFWriter extends DataWriter{

    public PGDFWriter(){
        this.filename = "sndata.pgdf";
    }

    @Override
    void writeBegin() {
        // Write first line with all properties
        this.writeLine("@id;@label;name;age;location;url;creation;@undirected;@source;@target;");        
    }

    @Override
    void writeBeginPeople() {
        // TODO Auto-generated method stub        
        
    }

    @Override
    void writePerson(String pid, String name, String age, String location) {
        // TODO Auto-generated method stub
        String line = "\""+pid+"\";[\"Person\"];";
        line += "\""+name+"\";";
        if (age.compareTo("?") != 0){
            line += age+";";
        }
        else{
            line += ";";
        }
        if (location.compareTo("?") != 0){
            line += "\""+location+"\";";
        }
        else{
            line += ";";
        }
        this.writeLine(line);
    }

    @Override
    void writeEndPeople() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void writeBeginWebpages() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void writeWebpage(String wpid, String url, String creation) {
        // TODO Auto-generated method stub
        String line = "\""+wpid+"\";"+"[\"Website\"];;;;";
        line += "\""+url+"\";";
        if (creation.compareTo("?") != 0){
            line += "\""+creation+"\";";
        }
        else{
            line += ";";
        }
        this.writeLine(line);
    }

    @Override
    void writeEndWebpages() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void writeBeginFriends() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void writeFriend(String id, String pid1, String pid2) {
        // friend (undirected edge between people)
        String line = "\""+id+"\";[\"Friend\"];;;;;;T;";
        line += "\""+pid1+"\";";
        line += "\""+pid2+"\";";
        this.writeLine(line);
    }

    @Override
    void writeEndFriends() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void writeBeginLikes() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void writeLike(String id, String pid, String wpid) {
        // like (directed edge between a person and a webpage)
        String line = "\""+id+"\";[\"Like\"];;;;;;F;";
        line += "\""+pid+"\";";
        line += "\""+wpid+"\";";
        this.writeLine(line);
    }
    
    @Override
    void writeEndLikes() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void writeEnd() {
        // TODO Auto-generated method stub
        
    }
    
}
