package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/9/1.
 */
public class BodyLengthNotMatchDefLenException extends Exception {
    public BodyLengthNotMatchDefLenException() {
        super();
    }

    public BodyLengthNotMatchDefLenException(String detailMessage) {
        super(detailMessage);
    }

    public BodyLengthNotMatchDefLenException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BodyLengthNotMatchDefLenException(Throwable throwable) {
        super(throwable);
    }
}
