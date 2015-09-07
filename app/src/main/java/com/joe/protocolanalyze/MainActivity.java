package com.joe.protocolanalyze;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import joe.protocol.main.ProtocolFactory;
import joe.protocol.throwable.BodyLengthNotMatchDefLenException;
import joe.protocol.throwable.ConstantsValueNullException;
import joe.protocol.throwable.InvalidEnumKeyException;
import joe.protocol.throwable.NotInitProtocolException;
import joe.protocol.throwable.ParamNumNotMatchVarNumException;
import joe.protocol.utils.HexUtils;

public class MainActivity extends AppCompatActivity {

    private String jsonstring = "{\n" +
            "    \"length\": 15,\n" +
            "    \"body\": [\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0xaa\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x09\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x01\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x01\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x01\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x01\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0xff\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"CRC8\",\n" +
            "            \"offset\": 1,\n" +
            "            \"len\": 12\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x55\"\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        ProtocolFactory factory = new ProtocolFactory();
        try {
            factory.initProtocol(jsonstring);
        } catch (ConstantsValueNullException | BodyLengthNotMatchDefLenException e) {
            e.printStackTrace();
        }
        try {
            byte[] commands;
            commands = factory.getBytesCommands();
            tv.setText(HexUtils.bytesToHexString(commands));
        } catch (ParamNumNotMatchVarNumException | NotInitProtocolException | InvalidEnumKeyException e) {
            e.printStackTrace();
        }
    }
}
