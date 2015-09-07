package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/9/7.
 */
public class ConstantsErrorException extends Exception {
    public ConstantsErrorException() {
        super();
    }

    public ConstantsErrorException(String detailMessage) {
        super(detailMessage);
    }

    public ConstantsErrorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ConstantsErrorException(Throwable throwable) {
        super(throwable);
    }
}
