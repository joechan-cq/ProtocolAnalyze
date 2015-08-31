package joe.protocol.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import joe.protocol.serialization.BodyItem;
import joe.protocol.serialization.Protocol;
import joe.protocol.throwable.ConstantsValueNullException;
import joe.protocol.throwable.NotInitProtocolException;
import joe.protocol.throwable.ParamsNumNotMatchVarNumException;
import joe.protocol.utils.HexUtils;

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

    private static final byte DEFAULT_VALUE = 0x00;

    private static final String VAR_SYMBOL = "vr";

    private Protocol protocol;

    //  变量数量
    private int varNums = 0;
    //  变量下标指针
    private ArrayList<Integer> varPoints;

    //  字符串形式协议内容
    private String stringCommand = "";

    private byte[] bytesCommand;

    public ProtocolFactory() {
        protocol = new Protocol();
    }

    public boolean initProtocol(File file) {
        return true;
    }

    public void initProtocol(String jsonString) throws ConstantsValueNullException {
        jsonString = jsonString.replace(" ", "").toLowerCase();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        int length = jsonObject.getIntValue(LENGTH);
        protocol.setLength(length);

        varNums = 0;
        varPoints = new ArrayList<>();
        stringCommand = "";
        bytesCommand = new byte[length];
        List<BodyItem> items;
        String bodyarray = jsonObject.getString(BODY);
        items = JSONArray.parseArray(bodyarray, BodyItem.class);

        for (int i = 0; i < items.size(); i++) {
            BodyItem item = items.get(i);
            if (item.getType().equals(VAR)) {

                varNums++;
                //  添加变量标识
                stringCommand = stringCommand + " " + VAR_SYMBOL;
                //  添加变量下标记录
                varPoints.add(i);

                if (item.getDefaultvalue() != null) {
                    String value = item.getDefaultvalue().toLowerCase();
                    if (value.length() > 0) {
                        value = value.contains("0x") ? value.replace("0x", "") : value;
                        //  添加变量默认值
                        bytesCommand[i] = HexUtils.hexStr2Bytes(value)[0];
                    } else {
                        bytesCommand[i] = DEFAULT_VALUE;
                    }
                } else {
                    bytesCommand[i] = DEFAULT_VALUE;
                }
            }
            if (item.getType().equals(CONSTANTS)) {
                stringCommand = stringCommand + " " + item.getValue();
                if (item.getValue() != null) {
                    String value = item.getValue().toLowerCase();
                    if (value.length() > 0) {
                        value = value.contains("0x") ? value.replace("0x", "") : value;
                        //  添加变量默认值
                        bytesCommand[i] = HexUtils.hexStr2Bytes(value)[0];
                    } else {
                        throw new ConstantsValueNullException();
                    }
                } else {
                    throw new ConstantsValueNullException();
                }
            }
        }
        protocol.setBody(items);
        protocol.setIsValid(true);
    }

    public String getCommandsString(String... params) throws ParamsNumNotMatchVarNumException, NotInitProtocolException {
        if (!protocol.isValid()) {
            throw new NotInitProtocolException();
        }
        if (params.length < varNums) {
            throw new ParamsNumNotMatchVarNumException();
        }
        for (String cmd : params) {
            stringCommand = stringCommand.replaceFirst(VAR_SYMBOL, cmd);
        }
        return stringCommand.trim();
    }

    public byte[] getBytesCommands(byte[] params) throws NotInitProtocolException, ParamsNumNotMatchVarNumException {
        if (!protocol.isValid()) {
            throw new NotInitProtocolException();
        }
        if (params.length < varNums) {
            throw new ParamsNumNotMatchVarNumException();
        }
        int i = 0;
        for (byte cmd : params) {
            bytesCommand[varPoints.get(i)] = cmd;
            i++;
        }
        return bytesCommand;
    }

    public byte[] getBytesCommands(String... params) throws NotInitProtocolException, ParamsNumNotMatchVarNumException {
        if (!protocol.isValid()) {
            throw new NotInitProtocolException();
        }
        if (params.length < varNums) {
            throw new ParamsNumNotMatchVarNumException();
        }
        int i = 0;
        for (String cmd : params) {
            String value = cmd.trim().toLowerCase();
            value = value.contains("0x") ? value.replace("0x", "") : value;
            bytesCommand[varPoints.get(i)] = HexUtils.hexStr2Bytes(value)[0];
            i++;
        }
        return bytesCommand;
    }
}