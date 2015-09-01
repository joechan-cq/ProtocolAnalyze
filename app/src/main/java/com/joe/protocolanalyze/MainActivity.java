package com.joe.protocolanalyze;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import joe.protocol.main.ProtocolFactory;
import joe.protocol.throwable.BodyNumsNotMatchDefLength;
import joe.protocol.throwable.ConstantsValueNullException;
import joe.protocol.throwable.InvalidEnumKeyException;
import joe.protocol.throwable.NotInitProtocolException;
import joe.protocol.throwable.ParamNumNotMatchVarNumException;
import joe.protocol.utils.HexUtils;

public class MainActivity extends AppCompatActivity {

    private String jsonstring = "{\n" +
            "\"length\": 7,\n" +
            "\"body\": [{\n" +
            "\"type\": \"constants\",\n" +
            "\"value\": \"0xa5\"\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"constants\",\n" +
            "\"value\": \"0xb5\"\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"var\"\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"var\"\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"constants\",\n" +
            "\"value\": \"0xc5\"\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"enum\",\n" +
            "\"valueMap\": {\n" +
            "\"powerup\": \"0x11\",\n" +
            "\"powerdown\": \"0x22\"\n" +
            "}\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"constants\",\n" +
            "\"value\": \"0xa5\"\n" +
            "}\n" +
            "]\n" +
            "}";

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        ProtocolFactory factory = new ProtocolFactory();
        try {
            factory.initProtocol(jsonstring);
        } catch (ConstantsValueNullException | BodyNumsNotMatchDefLength e) {
            e.printStackTrace();
        }
        try {
            byte[] commands;
            commands = factory.getBytesCommands("0xaa", "0xbb", "powerup");
            tv.setText(HexUtils.bytesToHexString(commands));
        } catch (ParamNumNotMatchVarNumException | NotInitProtocolException | InvalidEnumKeyException e) {
            e.printStackTrace();
        }
    }
}
