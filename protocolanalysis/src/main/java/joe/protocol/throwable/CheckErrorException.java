package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/9/7.
 */
public class CheckErrorException extends Exception {
    public CheckErrorException() {
        super();
    }

    public CheckErrorException(String detailMessage) {
        super(detailMessage);
    }

    public CheckErrorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CheckErrorException(Throwable throwable) {
        super(throwable);
    }
}
