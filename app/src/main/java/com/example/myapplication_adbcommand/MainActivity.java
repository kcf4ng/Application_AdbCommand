package com.example.myapplication_adbcommand;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private BufferedReader bufferedReader;

    private String addTimeStamp(String s){
        //get time
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_");
        String msg =format.format(date)+s;
        return msg;
    }
    
    private void  writeLogFile(String s){

        try {

            
            OutputStream streamOut = openFileOutput("Myfile.txt", 0);
            
            streamOut.write(addTimeStamp(s).getBytes("utf-8"));
            streamOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private String readLogFile(){
        try {
            InputStream streamIn = openFileInput("Myfile.txt");
            byte[] bytes = new byte[100];
            int count = streamIn.read(bytes);
            String content = new String (bytes,0,count,"utf-8" );
            
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            
            return "err : "+e;
        } catch (IOException e) {
            e.printStackTrace();
            
            return "err : "+e;
        }
    }
    
    private void execLogcat() {
        try {
            Log.d(TAG, "execLogcat: start");
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes("logcat -v time \n");

            bufferedReader = new BufferedReader(new InputStreamReader( process.getInputStream()));
            Log.d(TAG, "execLogcat: writeBytes exit  START");
            dos.writeBytes("exit\n");
            dos.flush();


        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "executeCommand: "+ e);
        }
    }

    private void readLog(){
        Log.d(TAG, "readLog: start");
        StringBuilder myLog = new StringBuilder();
        String myLogLine;

        try{
            while (null != (myLogLine = bufferedReader.readLine())){
                myLog.append(myLogLine);
                myLog.append("\n");
            }

//            String[] splitLog;
//            splitLog = myLog.toString().split( "\n");

            Log.d(TAG, "readLog: show myLog.toString() : " + myLog.toString());
            textView.setText(myLog.toString());

        }catch(Exception e){

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitialComponent();
    }

    private void InitialComponent() {
        button1 = findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeLogFile("Hello world @string");
            }
        });
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(readLogFile());
            }
        });
        textView = findViewById(R.id.textView);
    }


    Button button1, button2;
    TextView textView;


}
