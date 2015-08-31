package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/8/31.
 */
public class NotInitProtocolException extends Exception {
    public NotInitProtocolException() {
        super();
    }

    public NotInitProtocolException(String detailMessage) {
        super(detailMessage);
    }

    public NotInitProtocolException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotInitProtocolException(Throwable throwable) {
        super(throwable);
    }
}
