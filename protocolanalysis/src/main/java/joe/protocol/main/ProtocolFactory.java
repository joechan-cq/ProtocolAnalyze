package joe.protocol.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.List;

import joe.protocol.serialization.BodyItem;
import joe.protocol.serialization.Protocol;
import joe.protocol.throwable.ParamsNumNotMatchVarNumException;

/**
 * Description
 * Created by chenqiao on 2015/8/28.
 */
public class ProtocolFactory {

    private static final String LENGTH = "length";

    private static final String BODY = "body";

    private static final String BODY_ITEM = "bodyitem";

    private static final String TYPE = "type";

    private static final String VALUE = "value";

    private static final String FORMULA = "formula";

    private static final String CONSTANTS = "constants";

    private static final String VAR = "var";

    private static final String VAR_SYMBOL = "??";

    private Protocol protocol;

    //  变量数量
    private int varNums = 0;

    //  字符串形式协议内容
    private String stringCommand = "";

    public ProtocolFactory() {
        protocol = new Protocol();
    }

    public boolean initProtocol(File file) {
        return true;
    }

    public void initProtocol(String jsonString) {
        jsonString = jsonString.replace(" ", "").toLowerCase();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        int length = jsonObject.getIntValue(LENGTH);
        protocol.setLength(length);
        System.out.println("joe-------init start");
        varNums = 0;
        stringCommand = "";
        List<BodyItem> items;
        String bodyarray = jsonObject.getString(BODY);
        items = JSONArray.parseArray(bodyarray, BodyItem.class);
        System.out.println("joe-------parse array finished:" + items.size());
        for (int i = 0; i < items.size(); i++) {
            BodyItem item = items.get(i);
            if (item.getType().equals(VAR)) {
                varNums++;
                stringCommand = stringCommand + " " + VAR_SYMBOL;
            }
            if (item.getType().equals(CONSTANTS)) {
                stringCommand = stringCommand + " " + item.getValue();
            }
        }
        protocol.setBody(items);
    }

    public String getCommandsString(String... params) throws ParamsNumNotMatchVarNumException {
        if (params.length < varNums) {
            throw new ParamsNumNotMatchVarNumException();
        }
        for (String cmd : params) {
            stringCommand = stringCommand.replaceFirst(VAR_SYMBOL, cmd);
        }
        return stringCommand.trim();
    }
}
