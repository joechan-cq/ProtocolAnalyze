package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/9/1.
 */
public class InvalidEnumValueException extends Exception {
    public InvalidEnumValueException() {
        super();
    }

    public InvalidEnumValueException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidEnumValueException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidEnumValueException(Throwable throwable) {
        super(throwable);
    }
}
