package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/9/1.
 */
public class BodyNumsNotMatchDefLength extends Exception {
    public BodyNumsNotMatchDefLength() {
        super();
    }

    public BodyNumsNotMatchDefLength(String detailMessage) {
        super(detailMessage);
    }

    public BodyNumsNotMatchDefLength(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BodyNumsNotMatchDefLength(Throwable throwable) {
        super(throwable);
    }
}
