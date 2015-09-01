package joe.protocol.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import joe.protocol.serialization.BodyItem;
import joe.protocol.serialization.Protocol;
import joe.protocol.throwable.BodyNumsNotMatchDefLength;
import joe.protocol.throwable.ConstantsValueNullException;
import joe.protocol.throwable.InvalidEnumKeyException;
import joe.protocol.throwable.NotInitProtocolException;
import joe.protocol.throwable.ParamNumNotMatchVarNumException;
import joe.protocol.utils.HexUtils;

/**
 * Description
 * Created by chenqiao on 2015/8/28.
 */
public class ProtocolFactory {

    private static final String LENGTH = "length";

    private static final String BODY = "body";

    private static final String TYPE = "type";

    private static final String VALUE = "value";

    private static final String FORMULA = "formula";

    //  1
    private static final String CONSTANTS = "constants";
    //  2
    private static final String VAR = "var";
    //  3
    private static final String ENUM = "enum";

    private static final byte DEFAULT_VALUE = 0x00;

    //  变量替代符
    private static final String VAR_SYMBOL = "vr";
    //  枚举替代符
    private static final String ENUM_SYMBOL = "em";

    private Protocol protocol;

    //  变量数量
    private int varNums = 0;

    private int[] cmdTypes;

    //  字符串形式协议内容
    private String stringCommand = "";

    //  byte数组形式命令
    private byte[] bytesCommand;

    public ProtocolFactory() {
        protocol = new Protocol();
    }

    public boolean initProtocol(String filePath, String fileName) throws ConstantsValueNullException, IOException, BodyNumsNotMatchDefLength {
        File file = new File(filePath, fileName);
        return initProtocol(file);
    }

    public boolean initProtocol(File file) throws IOException, ConstantsValueNullException, BodyNumsNotMatchDefLength {
        String jsonStr = "";
        FileInputStream in = new FileInputStream(file);
        int size = in.available();
        byte[] buffer = new byte[size];
        in.read(buffer);
        in.close();
        jsonStr = new String(buffer, "UTF-8");
        initProtocol(jsonStr);
        return true;
    }

    public void initProtocol(String jsonString) throws ConstantsValueNullException, BodyNumsNotMatchDefLength {
        jsonString = jsonString.replace(" ", "").toLowerCase();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        int length = jsonObject.getIntValue(LENGTH);
        protocol.setLength(length);

        cmdTypes = new int[length];
        varNums = 0;
        stringCommand = "";
        bytesCommand = new byte[length];

        List<BodyItem> items;
        String bodyarray = jsonObject.getString(BODY);
        items = JSONArray.parseArray(bodyarray, BodyItem.class);
        if (items.size() != length) {
            throw new BodyNumsNotMatchDefLength();
        }

        for (int i = 0; i < items.size(); i++) {
            BodyItem item = items.get(i);
            if (item.getType().equals(VAR) || item.getType().equals(ENUM)) {
                varNums++;
                //  添加变量标识
                if (item.getType().equals(VAR)) {
                    cmdTypes[i] = 2;
                    stringCommand = stringCommand + " " + VAR_SYMBOL;
                } else {
                    cmdTypes[i] = 3;
                    stringCommand = stringCommand + " " + ENUM_SYMBOL;
                }

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
                cmdTypes[i] = 1;
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

    /**
     * 获取字符串形式的命令
     *
     * @param params 不定长参数
     * @return 字符串表示的命令
     * @throws ParamNumNotMatchVarNumException 传入参数数量小于（变量+枚举）数量
     * @throws NotInitProtocolException        没有初始化协议
     * @throws InvalidEnumKeyException         非法的枚举值
     */
    public String getCommandsString(String... params) throws ParamNumNotMatchVarNumException, NotInitProtocolException, InvalidEnumKeyException {
        if (!protocol.isValid()) {
            throw new NotInitProtocolException();
        }
        if (params.length < varNums) {
            throw new ParamNumNotMatchVarNumException();
        }
        int point = 0;
        for (int i = 0; i < protocol.getLength(); i++) {
            switch (cmdTypes[i]) {
                case 1:
                    break;
                case 2:
                    stringCommand = stringCommand.replaceFirst(VAR_SYMBOL, params[point]);
                    point++;
                    break;
                case 3:
                    if (protocol.getBody().get(i).getValueMap().containsKey(params[point])) {
                        String value = protocol.getBody().get(i).getValueMap().get(params[point]);
                        stringCommand = stringCommand.replaceFirst(ENUM_SYMBOL, value);
                        point++;
                    } else {
                        throw new InvalidEnumKeyException();
                    }
                    break;
            }
        }
        return stringCommand.toLowerCase().trim();
    }

    /**
     * 获取byte数组形式的命令
     *
     * @param params 可变命令的byte形式（枚举类型也会被直接填充）
     * @return byte数组命令
     * @throws NotInitProtocolException        未初始化协议
     * @throws ParamNumNotMatchVarNumException 传入参数数量小于（变量+枚举）数量
     */
    public byte[] getBytesCommands(byte[] params) throws NotInitProtocolException, ParamNumNotMatchVarNumException {
        if (!protocol.isValid()) {
            throw new NotInitProtocolException();
        }
        if (params.length < varNums) {
            throw new ParamNumNotMatchVarNumException();
        }
        int point = 0;
        for (int i = 0; i < protocol.getLength(); i++) {
            switch (cmdTypes[i]) {
                case 1:
                    break;
                case 2:
                case 3:
                    bytesCommand[i] = params[point];
                    point++;
                    break;
            }
        }
        return bytesCommand;
    }

    /**
     * 获取byte形式命令
     *
     * @param params 不定长的字符串命令
     * @return byte数组命令
     * @throws NotInitProtocolException        未初始化协议
     * @throws ParamNumNotMatchVarNumException 传入参数数量小于（变量+枚举）数量
     * @throws InvalidEnumKeyException         非法枚举值
     */
    public byte[] getBytesCommands(String... params) throws NotInitProtocolException, ParamNumNotMatchVarNumException, InvalidEnumKeyException {
        if (!protocol.isValid()) {
            throw new NotInitProtocolException();
        }
        if (params.length < varNums) {
            throw new ParamNumNotMatchVarNumException();
        }
        int point = 0;
        for (int i = 0; i < protocol.getLength(); i++) {
            switch (cmdTypes[i]) {
                case 1:
                    break;
                case 2:
                    String value = params[point].toLowerCase().trim();
                    value = value.contains("0x") ? value.replace("0x", "") : value;
                    bytesCommand[i] = HexUtils.hexStr2Bytes(value)[0];
                    point++;
                    break;
                case 3:
                    String key = params[point];
                    if (protocol.getBody().get(i).getValueMap().containsKey(key)) {
                        String map_value = protocol.getBody().get(i).getValueMap().get(key).toLowerCase().trim();
                        map_value = map_value.contains("0x") ? map_value.replace("0x", "") : map_value;
                        bytesCommand[i] = HexUtils.hexStr2Bytes(map_value)[0];
                        point++;
                    } else {
                        throw new InvalidEnumKeyException();
                    }
                    break;
            }
        }
        return bytesCommand;
    }
}