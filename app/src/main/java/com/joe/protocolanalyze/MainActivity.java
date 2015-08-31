package com.joe.protocolanalyze;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import joe.protocol.main.ProtocolFactory;
import joe.protocol.throwable.ParamsNumNotMatchVarNumException;

public class MainActivity extends AppCompatActivity {

    private String jsonstring = "{\n" +
            "    \"length\": 8,\n" +
            "    \"body\": [\n" +
            "        {\n" +
            "            \"type\": \"constants\",\n" +
            "            \"value\": \"0x83\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"var\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProtocolFactory factory = new ProtocolFactory();
        factory.initProtocol(jsonstring);
        String commands = "";
        try {
            commands = factory.getCommandsString("0xaa");
        } catch (ParamsNumNotMatchVarNumException e) {
            e.printStackTrace();
            System.out.println("joe------" + e.toString());
        }
        System.out.println("joe---------" + commands);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
