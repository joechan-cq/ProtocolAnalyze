package joe.protocol.serialization;

import java.util.List;

/**
 * Description
 * Created by chenqiao on 2015/8/31.
 */
public class Protocol {

    private int length;

    private List<BodyItem> body;

    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    private boolean isValid = false;

    public List<BodyItem> getBody() {
        return body;
    }

    public void setBody(List<BodyItem> body) {
        this.body = body;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Protocol() {
    }
}
