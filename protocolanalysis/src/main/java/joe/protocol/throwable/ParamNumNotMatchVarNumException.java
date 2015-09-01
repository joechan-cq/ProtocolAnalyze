package joe.protocol.throwable;

/**
 * Description
 * Created by chenqiao on 2015/8/31.
 */
public class ParamNumNotMatchVarNumException extends Exception {
    public ParamNumNotMatchVarNumException() {
        super();
    }

    public ParamNumNotMatchVarNumException(String detailMessage) {
        super(detailMessage);
    }

    public ParamNumNotMatchVarNumException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParamNumNotMatchVarNumException(Throwable throwable) {
        super(throwable);
    }
}
