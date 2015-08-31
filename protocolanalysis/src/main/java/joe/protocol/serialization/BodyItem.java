package joe.protocol.serialization;

/**
 * Description
 * Created by chenqiao on 2015/8/31.
 */
public class BodyItem {

    private String type;

    private String value;

    private String formula;

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

    public void setValue(String value) {
        this.value = value;
    }

    public BodyItem(String type, String value, String formula) {
        this.type = type;
        this.value = value;
        this.formula = formula;
    }

    public BodyItem() {
    }
}
