package joe.protocol.serialization;

import java.util.HashMap;

/**
 * Description
 * Created by chenqiao on 2015/8/31.
 */
public class BodyItem {

    private String type;

    private String value;

    private String defaultvalue;

    private String formula;

    private HashMap<String, String> valueMap;

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HashMap<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(HashMap<String, String> valueMap) {
        this.valueMap = valueMap;
    }

    public BodyItem(String type, String value, String defaultvalue, String formula, HashMap<String, String> valueMap) {
        this.type = type;
        this.value = value;
        this.defaultvalue = defaultvalue;
        this.formula = formula;
        this.valueMap = valueMap;
    }

    public BodyItem() {
    }
}
