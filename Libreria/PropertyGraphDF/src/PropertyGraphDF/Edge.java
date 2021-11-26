package PropertyGraphDF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Matias Pizarro
 */
public class Edge {
    private String id;
    private Boolean undirected;
    private List<String> labels;
    private String source;
    private String target;
    private List<Property> properties;

    /**
     * Constructor.
     * @param id El id de la arista a crear.
     * @param source El id del nodo de origen de la arista.
     * @param target El id del nodo de destino de la arista.
     */
    public Edge(String id, String source, String target) {
        this.id = id;
        this.labels = new ArrayList<>();
        this.source = source;
        this.target = target;
        this.properties = new ArrayList<>();
        this.undirected = false;
    }

    /**
     * Método que agrega una etiqueta a la arista.
     * @param label EL nombre de la etiqueta a agregar.
     * @return True si se agrego la etiqueta con exito. False en caso contrario.
     */
    public boolean addLabel(String label){
        if(!this.labels.contains(label)){
            this.labels.add(label);
            return true;
        }
        return false;
    }

    /**
     * Método que elimina una etiqueta asociada a la arista.
     * @param label El nombre de la etiqueta a eliminar.
     * @return True si se elimino la etiqueta con exito. False en caso contrario.
     */
    public boolean removeLabel(String label){
        if(this.labels.contains(label)){
            this.labels.remove(label);
            return true;
        }
        return false;
    }

    /**
     * Metodo que cambia la direccion de la arista. Solo acepta los valores 0 y 1, donde 0 indica que es "No Dirigido", y 1 indica que es "Dirigido".
     * @param direction Direccion de la arista (0 o 1)
     * @return True si se cambio con exito. False si se ingreso un valor no valido.
     */
    public void changeDirection(boolean direction){
        this.undirected = direction;
    }

    /**
     * Método que agrega una propiedad a la arista.
     * @param property La propiedad que se desea añadir a la arista.
     * @return 0 si la propiedad no existia y se agrego correctamente. 1 si la propiedad existia y se reemplazo el valor para dicha llave.
     */
    public boolean addProperty(Property property){
        if(!this.properties.contains(property)){
            this.properties.add(property);
            return true;
        }
        return false;
    }

    /**
     * MÃ©todo que elimina una propiedad asociada a la arista.
     * @param property
     * @return True si se elimino la propiedad. False en caso de que la propiedad no existiera (para este nodo en especifico).
     */
    public boolean removeProperty(Property property){
        if(this.properties.contains(property)){
            this.properties.remove(property);
            return true;
        }
        return false;
    }

    /**
     * MÃ©todo que, a partir de una llave, verifica si la arista posee dicha propiedad.
     * @param key
     * @return True si la propiedad existe. False en caso contrario.
     */
    public boolean hasProp(String key){
        return this.properties.stream().anyMatch(prop -> (prop.getKey().equals(key)));
    }

    public List<String> getValue(String key){
        for(Property prop: this.properties){
            if(prop.getKey().equals(key)){
                return prop.getValue();
            }
        }
        return null;
    }

    /**
     * MÃ©todo que retorna un String con las propiedades reservadar de la arista, con el formato de datos usado.
     * @return  El String con las propiedades en el formato de datos.
     */
    public String formatEdge(){
        String line = "\"" + this.id + "\"" +";";
        if(this.labels.size() > 0 ){
            String names = "[";
            int ready = 0;
            for(String l : this.labels){
                names = names + "\"" + l + "\"";
                ready++;
                if(ready < this.labels.size()){
                    names = names + ",";
                }
            }
            names = names + "]";
            line = line + names +";";
        }
        else{
            line = line + "[];";
        }
        if(this.undirected != null){
            if(undirected){
                line = line + "T;";
            }
            else{
                line = line + "F;";
            }
        }
        else{
            line = line + ";";
        }

        line = line + "\"" + this.source + "\"" + ";" + "\"" + this.target+ "\"" + ";";
        return line;
    }

    @Override
    public String toString() {
        return "Edge [id=" + id + ", undirected=" + undirected +", labels=" + labels +  ", source=" + source
                + ", target=" + target + ", properties=" + Arrays.asList(properties) +"]";
    }

    //GETTERS Y SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getUndirected() {
        return undirected;
    }

    public void setUndirected(boolean undirected) {
        this.undirected = undirected;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
