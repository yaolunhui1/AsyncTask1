package com.example.administrator.asynctask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
* 简单的异步处理，不需要借助线程和Handler即可实现
* @author yaolunhui1
* */
public class MainActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etPassWord;
    private ProgressDialog progressDialog;
    private  static final int PROGRESSDIALOG_ID=0;
    private  static final int SERVER_ERROR=1;
    private static  final int NETWORK_ERROR=2;
    private static  final int CANCELLEO=3;
    private static  final int SUCCESS=4;
    private String ServerResponse;
    private  LoginTask loginTask;
    private static final String WEB_SERVICE_URL="www.baidu.com";
    private String UserName;
    private String PassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName=(EditText)findViewById(R.id.etUserName);
        etPassWord=(EditText)findViewById(R.id.etPassWord);

        Button login_button=(Button)findViewById(R.id.onLogin);
        Button Registration_button=(Button)findViewById(R.id.onRegistration);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUserName.getText().toString().isEmpty()
                        || etPassWord.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"please enter username and password",
                    Toast.LENGTH_SHORT).show();

                }else{
                    UserName=etUserName.getText().toString();
                    PassWord=etPassWord.getText().toString();

                    showDialog(PROGRESSDIALOG_ID);
                }

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case PROGRESSDIALOG_ID:
                removeDialog(PROGRESSDIALOG_ID);
                progressDialog=ProgressDialog.show(MainActivity.this,"authenticating",
                        "Please wait...",true,true,new DialogInterface.OnCancelListener(){

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                if(loginTask!=null && loginTask.getStatus()!= AsyncTask.Status.FINISHED)
                                    loginTask.cancel(true);

                            }
                        });
                break;
            default:
                progressDialog=null;
        }

        return progressDialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id){
            case PROGRESSDIALOG_ID:
                if(loginTask!=null && loginTask.getStatus()!=AsyncTask.Status.FINISHED) {
                    loginTask.cancel(true);
                    loginTask = new LoginTask();
                    loginTask.execute();
                }
        }
        super.onPrepareDialog(id, dialog);
    }


class LoginTask extends  AsyncTask<Void,Integer,Void>{


    @Override
    protected Void doInBackground(Void... params) {
        ServerResponse=null;
        try {
            /*
            * 登录
            * */
            String str1=UserName+"&"+PassWord;
            URL url=new URL(WEB_SERVICE_URL);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(str1.length()));
            conn.setConnectTimeout(3000);
            conn.connect();
            OutputStream outputStream=conn.getOutputStream();
            outputStream.write(str1.getBytes());

            if(conn.getResponseCode()==200) {
           /*
           * 发送成功
           * */
                InputStream inStream=conn.getInputStream();
                byte []data=new byte[9999];
                int i=inStream.read(data);
                String str=new String(data,0,i);

            }






        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}








}