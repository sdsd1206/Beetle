package com.example.jack.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class test extends AppCompatActivity {

    private EditText edtname, edttext;
    private TextView textview1;
    private Button button1;
    String tmp; // 暫存文字訊息
    Socket clientSocket;

    public static Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        edtname = (EditText) findViewById(R.id.edtname);
        edttext = (EditText) findViewById(R.id.edttext);
        button1 = (Button) findViewById(R.id.button1);
        textview1 = (TextView) findViewById(R.id.textView1);
        button1.setOnClickListener(btnlistener);

        // 啟動執行緒
        new Thread(new Runnable() {
            @Override
            public void run() {
                // server端的IP
                InetAddress serverIp;
                try {
                    // 以內定(本機電腦端)IP為Server端
                    serverIp = InetAddress.getByName("192.168.3.101");
                    int serverPort = 5050;
                    clientSocket = new Socket(serverIp, serverPort);

                    // 取得網路輸入串流
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            clientSocket.getInputStream()));

                    // 當連線後
                    while (clientSocket.isConnected()) {
                        // 取得網路訊息
                        tmp = br.readLine();

                        // 如果不是空訊息則
                        if (tmp != null) {
                            // 顯示新的訊息
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 加入新訊息並換行
                                    textview1.append(tmp + "\n");
                                }
                            });
                        }
                    }

                } catch (IOException e) {

                }
            }
        }).start();
    }

    private View.OnClickListener btnlistener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (clientSocket.isConnected()) {
                        BufferedWriter bw;
                        try {
                            // 取得網路輸出串流
                            bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                            // 寫入訊息
                            bw.write(edtname.getText() + ":" + edttext.getText() + "\n");

                            // 立即發送
                            bw.flush();
                        } catch (IOException e) {

                        }
                        // 將文字方塊清空
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // 加入新訊息並換行
                                edttext.setText("");
                            }
                        });
                    }
                }
            }).start();
        }
    };
}
