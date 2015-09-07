package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/9/7.
 */
public class CmdLengthNotMatchDefLenException extends Exception {
    public CmdLengthNotMatchDefLenException() {
        super();
    }

    public CmdLengthNotMatchDefLenException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CmdLengthNotMatchDefLenException(String detailMessage) {
        super(detailMessage);
    }

    public CmdLengthNotMatchDefLenException(Throwable throwable) {
        super(throwable);
    }
}
