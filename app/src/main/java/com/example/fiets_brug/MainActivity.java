package com.example.fiets_brug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.jar.JarEntry;

public class MainActivity extends AppCompatActivity {
    //UI Element
    Button ButtonUp;
    Button ButtonDown;
    EditText txtAddress;
    Socket myAppSocket = null;
    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButtonUp = (Button) findViewById(R.id.buttonUp);
        ButtonDown = (Button) findViewById(R.id.buttonDown);
        txtAddress = (EditText) findViewById(R.id.ipAddress);

        ButtonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Up";
                Socket_AsyncTask cmd_increase_piston = new Socket_AsyncTask();
                cmd_increase_piston.execute();
            }
        });
        ButtonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Down";
                Socket_AsyncTask cmd_increase_piston = new Socket_AsyncTask();
                cmd_increase_piston.execute();
            }
        });


    }
    public void getIPandPort()
    {
        String IPandPort = txtAddress.getText().toString();
        Log.d("MYTEST","IP String: "+ IPandPort);
        String temp[]= IPandPort.split(":");
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);
        Log.d("MY TEST","IP: " +wifiModuleIp);
        Log.d("MY TEST","PORT: " +wifiModulePort);
    }
    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>
    {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(MainActivity.wifiModuleIp);
                socket = new java.net.Socket(inetAddress,MainActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
            return null;
        }
    }
}