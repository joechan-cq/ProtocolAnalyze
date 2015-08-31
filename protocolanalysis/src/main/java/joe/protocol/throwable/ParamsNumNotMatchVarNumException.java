package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/8/31.
 */
public class ParamsNumNotMatchVarNumException extends Exception {
    public ParamsNumNotMatchVarNumException() {
        super();
    }

    public ParamsNumNotMatchVarNumException(String detailMessage) {
        super(detailMessage);
    }

    public ParamsNumNotMatchVarNumException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParamsNumNotMatchVarNumException(Throwable throwable) {
        super(throwable);
    }
}
