package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/9/1.
 */
public class InvalidHexStrException extends Exception {
    public InvalidHexStrException() {
        super();
    }

    public InvalidHexStrException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidHexStrException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidHexStrException(Throwable throwable) {
        super(throwable);
    }
}
