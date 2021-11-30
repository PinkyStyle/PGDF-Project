package PropertyGraphDF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Matias Pizarro
 */
public class Node {
    private String id;
    private List<String> labels;
    private List<Property> properties;

    /**
     * Constructor.
     * @param id El id del nodo a crear.
     */
    public Node(String id){
        this.id = id;
        this.labels = new ArrayList<>();
        this.properties = new ArrayList<>();
    }

    /**
     * Método que agrega una etiqueta al nodo.
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
     * Método que elimina una etiqueta asociada al nodo.
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
     * Método que agrega una propiedad al nodo.
     * @param property
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
     * Método que elimina una propiedad asociada al nodo.
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
     * Método que, a partir de una llave, verifica si el nodo posee dicha propiedad.
     * @param key
     * @return True si la propiedad existe. False en caso contrario.
     */
    public boolean hasProp(String key){
        if( key.equals("@label")){
            return this.labels.size() > 0;
        }
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
     * Método que retorna un String con las propiedades reservadas del nodo, con el formato de datos usado.
     * @return  El String con las propiedades en el formato de datos.
     */
    public String formatNode(){
        String line = "\"" + this.id+ "\"" + ";";
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
            line = line +"[];";
        }
        return line;
    }

    @Override
    public String toString() {
        return "Node [id=" + id + ", labels=" + labels + ", properties=" + Arrays.asList(properties) + "]";
    }

    //GETTERS Y SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
