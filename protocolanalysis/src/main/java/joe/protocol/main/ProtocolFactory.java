package joe.protocol.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import joe.protocol.serialization.BodyItem;
import joe.protocol.serialization.Protocol;
import joe.protocol.throwable.BodyLengthNotMatchDefLenException;
import joe.protocol.throwable.CheckErrorException;
import joe.protocol.throwable.CmdLengthNotMatchDefLenException;
import joe.protocol.throwable.ConstantsErrorException;
import joe.protocol.throwable.ConstantsValueNullException;
import joe.protocol.throwable.InvalidEnumKeyException;
import joe.protocol.throwable.InvalidEnumValueException;
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

    //  1
    private static final String CONSTANTS = "constants";
    //  2
    private static final String VAR = "var";
    //  3
    private static final String ENUM = "enum";
    //  4
    private static final String CRC8 = "crc8";
    //  5
    private static final String CRC16 = "crc16";

    private static final byte DEFAULT_VALUE = 0x00;

    //  变量替代符
    private static final String VAR_SYMBOL = "vr";
    //  枚举替代符
    private static final String ENUM_SYMBOL = "em";
    //  CRC8替代符
    private static final String CRC8_SYMBOL = "crc8";
    //  CRC16替代符
    private static final String CRC16_SYMBOL = "crc16";

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

    public boolean initProtocol(String filePath, String fileName) throws ConstantsValueNullException, IOException, BodyLengthNotMatchDefLenException {
        File file = new File(filePath, fileName);
        return initProtocol(file);
    }

    public boolean initProtocol(File file) throws IOException, ConstantsValueNullException, BodyLengthNotMatchDefLenException {
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

    public void initProtocol(String jsonString) throws ConstantsValueNullException, BodyLengthNotMatchDefLenException {
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
            throw new BodyLengthNotMatchDefLenException("protocol body length does not match the defined length");
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
                        throw new ConstantsValueNullException("constants type does not allow the null value");
                    }
                } else {
                    throw new ConstantsValueNullException("constants type does not allow the null value");
                }
            }
            if (item.getType().equals(CRC8)) {
                cmdTypes[i] = 4;
                stringCommand = stringCommand + " " + CRC8_SYMBOL;
                bytesCommand[i] = DEFAULT_VALUE;
            }
//            if (item.getType().equals(CRC16)) {
//                cmdTypes[i] = 5;
//                stringCommand = stringCommand + " " + CRC16_SYMBOL;
//                bytesCommand[i] = DEFAULT_VALUE;
//            }
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
            throw new NotInitProtocolException("protocol has not been initialized");
        }
        if (params.length < varNums) {
            throw new ParamNumNotMatchVarNumException("the num of params is smaller than def var nums");
        }
        byte[] tempbyte = bytesCommand;
        int point = 0;
        for (int i = 0; i < protocol.getLength(); i++) {
            switch (cmdTypes[i]) {
                case 1:
                    break;
                case 2:
                    stringCommand = stringCommand.replaceFirst(VAR_SYMBOL, params[point]);
                    String value1 = params[point].toLowerCase().trim();
                    value1 = value1.contains("0x") ? value1.replace("0x", "") : value1;
                    tempbyte[i] = HexUtils.hexStr2Bytes(value1)[0];
                    point++;
                    break;
                case 3:
                    if (protocol.getBody().get(i).getValueMap().containsKey(params[point])) {
                        String value2 = protocol.getBody().get(i).getValueMap().get(params[point]);
                        stringCommand = stringCommand.replaceFirst(ENUM_SYMBOL, value2);
                        String map_value = value2.toLowerCase().trim();
                        map_value = map_value.contains("0x") ? map_value.replace("0x", "") : map_value;
                        tempbyte[i] = HexUtils.hexStr2Bytes(map_value)[0];
                        point++;
                    } else {
                        throw new InvalidEnumKeyException("invalid enum value");
                    }
                    break;
                case 4: //CRC8校验
                    int offset8 = protocol.getBody().get(i).getOffset();
                    int len8 = protocol.getBody().get(i).getLen();
                    byte[] crc8 = new byte[1];
                    crc8[0] = joe.protocol.utils.CRC8.calcCrc8(tempbyte, offset8, len8);
                    tempbyte[i] = crc8[0];
                    stringCommand = stringCommand.replaceFirst(CRC8_SYMBOL, HexUtils.bytesToHexString(crc8));
                    break;
//                case 5: //CRC16校验
//                    int offset16 = protocol.getBody().get(i).getOffset();
//                    int len16 = protocol.getBody().get(i).getLen();
//                    byte[] crc16 = new byte[1];
//                    crc16[0] = joe.protocol.utils.CRC8.calcCrc8(tempbyte, offset16, len16);
//                    tempbyte[i] = crc16[0];
//                    stringCommand = stringCommand.replaceFirst(CRC16_SYMBOL, HexUtils.bytesToHexString(crc16));
//                    break;
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
            throw new NotInitProtocolException("protocol has not been initialized");
        }
        if (params.length < varNums) {
            throw new ParamNumNotMatchVarNumException("the num of params is smaller than def var nums");
        }
        byte[] tempbyte = bytesCommand;
        int point = 0;
        for (int i = 0; i < protocol.getLength(); i++) {
            switch (cmdTypes[i]) {
                case 1:
                    break;
                case 2:
                case 3:
                    tempbyte[i] = params[point];
                    point++;
                    break;
                case 4:
                    int offset8 = protocol.getBody().get(i).getOffset();
                    int len8 = protocol.getBody().get(i).getLen();
                    byte crc8 = joe.protocol.utils.CRC8.calcCrc8(tempbyte, offset8, len8);
                    tempbyte[i] = crc8;
                    break;
//                case 5:
//                    int offset16 = protocol.getBody().get(i).getOffset();
//                    int len16 = protocol.getBody().get(i).getLen();
//                    byte crc16 = joe.protocol.utils.CRC8.calcCrc8(tempbyte, offset16, len16);
//                    tempbyte[i] = crc16;
//                    break;
            }
        }
        return tempbyte;
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
            throw new NotInitProtocolException("protocol has not been initialized");
        }
        if (params.length < varNums) {
            throw new ParamNumNotMatchVarNumException("the num of params is smaller than def var nums");
        }
        byte[] tempbyte = bytesCommand;
        int point = 0;
        for (int i = 0; i < protocol.getLength(); i++) {
            switch (cmdTypes[i]) {
                case 1:
                    break;
                case 2:
                    String value = params[point].toLowerCase().trim();
                    value = value.contains("0x") ? value.replace("0x", "") : value;
                    tempbyte[i] = HexUtils.hexStr2Bytes(value)[0];
                    point++;
                    break;
                case 3:
                    String key = params[point];
                    if (protocol.getBody().get(i).getValueMap().containsKey(key)) {
                        String map_value = protocol.getBody().get(i).getValueMap().get(key).toLowerCase().trim();
                        map_value = map_value.contains("0x") ? map_value.replace("0x", "") : map_value;
                        tempbyte[i] = HexUtils.hexStr2Bytes(map_value)[0];
                        point++;
                    } else {
                        throw new InvalidEnumKeyException("invalid enum value");
                    }
                    break;
                case 4:
                    int offset8 = protocol.getBody().get(i).getOffset();
                    int len8 = protocol.getBody().get(i).getLen();
                    byte crc8 = joe.protocol.utils.CRC8.calcCrc8(tempbyte, offset8, len8);
                    tempbyte[i] = crc8;
                    break;
//                case 5:
//                    int offset16 = protocol.getBody().get(i).getOffset();
//                    int len16 = protocol.getBody().get(i).getLen();
//                    byte crc16 = joe.protocol.utils.CRC8.calcCrc8(tempbyte, offset16, len16);
//                    tempbyte[i] = crc16;
//                    break;
            }
        }
        return tempbyte;
    }

    public org.json.JSONArray analyzeCommands(byte[] cmds) throws CmdLengthNotMatchDefLenException, ConstantsErrorException, JSONException, InvalidEnumValueException, CheckErrorException {
        if (cmds.length < protocol.getLength()) {
            throw new CmdLengthNotMatchDefLenException("bytes length is smaller than def body len");
        }
        org.json.JSONArray body = new org.json.JSONArray();
        org.json.JSONObject it = null;
        for (int i = 0; i < protocol.getLength(); i++) {
            BodyItem item = protocol.getBody().get(i);
            it = new org.json.JSONObject();
            if (item.getType().equals(CONSTANTS)) {
                String tmp = item.getValue().toLowerCase().trim().replace("0x", "");
                byte value = HexUtils.hexStr2Bytes(tmp)[0];
                if (value != cmds[i]) {
                    throw new ConstantsErrorException("byte does not equal the constants value");
                } else {
                    it.put("type", CONSTANTS);
                    it.put("value", "0x" + tmp);
                }
            }
            if (item.getType().equals(VAR)) {
                String value = String.format("0x%s", HexUtils.bytesToHexString(new byte[]{cmds[i]}).toLowerCase().trim());
                it.put("type", VAR);
                it.put("value", value);
            }
            if (item.getType().equals(ENUM)) {
                String key = "";
                String value = String.format("0x%s", HexUtils.bytesToHexString(new byte[]{cmds[i]}).toLowerCase().trim());
                if (item.getValueMap().containsValue(value)) {
                    for (String j : item.getValueMap().keySet()) {
                        if (item.getValueMap().get(j).equals(value)) {
                            key = j;
                            break;
                        }
                    }
                } else {
                    throw new InvalidEnumValueException("invalid enum value");
                }
                it.put("type", ENUM);
                it.put("value", key);
            }
            if (item.getType().equals(CRC8)) {
                int offset8 = item.getOffset();
                int len8 = item.getLen();
                byte crc8 = joe.protocol.utils.CRC8.calcCrc8(cmds, offset8, len8);
                if (crc8 == cmds[i]) {
                    it.put("type", CRC8);
                    it.put("value", "0x" + HexUtils.bytesToHexString(new byte[]{crc8}).toLowerCase().trim());
                } else {
                    throw new CheckErrorException("crc8 check is error");
                }
            }
            body.put(it);
        }
        return body;
    }
}