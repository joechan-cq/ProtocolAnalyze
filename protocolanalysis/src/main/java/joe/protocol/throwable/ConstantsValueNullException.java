package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/8/31.
 */
public class ConstantsValueNullException extends Exception {
    public ConstantsValueNullException() {
        super();
    }

    public ConstantsValueNullException(String detailMessage) {
        super(detailMessage);
    }

    public ConstantsValueNullException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ConstantsValueNullException(Throwable throwable) {
        super(throwable);
    }
}
