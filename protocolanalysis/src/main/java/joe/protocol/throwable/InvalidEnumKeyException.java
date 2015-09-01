package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/9/1.
 */
public class InvalidEnumKeyException extends Exception {
    public InvalidEnumKeyException() {
        super();
    }

    public InvalidEnumKeyException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidEnumKeyException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidEnumKeyException(Throwable throwable) {
        super(throwable);
    }
}
