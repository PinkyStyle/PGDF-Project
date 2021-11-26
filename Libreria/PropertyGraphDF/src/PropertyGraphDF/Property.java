package PropertyGraphDF;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matias Pizarro
 */
public class Property {
    private String key;
    private List<String> value;

    public Property(String key, ArrayList<String> value){
        this.key = key;
        this.value = new ArrayList<>(value);
    }

    public void addValue(String newValue){
        this.value.add(newValue);
    }

    /**
     *  Método que permite eliminar un valor asociado a la propiedad.
     * @param value
     * @return
     */
    public boolean removeValue(String value){
        if(this.value.contains(value)){
            this.value.remove(value);
            return true;
        }
        return false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }


}
