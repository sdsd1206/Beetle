package com.example.jack.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class buy_yours_addpay extends AppCompatActivity {

    private DatabaseReference ref;
    String Filename = "cardnum";
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_yours_addpay);

        Button sbmbtn = (Button) findViewById(R.id.submitbtn);
        sbmbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_yours_addpay.this, buy_yours_paydata.class);
                startActivity(intent);
            }
        });

        Button addbtn = (Button) findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*new Thread(new Runnable(){
                    @Override
                    public void run(){
                        EditText cn1 = (EditText) findViewById(R.id.cardnum1);
                        EditText cn2 = (EditText) findViewById(R.id.cardnum2);
                        EditText cn3 = (EditText) findViewById(R.id.cardnum3);
                        EditText cn4 = (EditText) findViewById(R.id.cardnum4);
                        EditText exdy = (EditText) findViewById(R.id.expirydateyear);
                        EditText exd = (EditText) findViewById(R.id.expirydate);
                        EditText sn = (EditText) findViewById(R.id.safenum);
                        String cn = cn1.getText().toString() + cn2.getText().toString() + cn3.getText().toString() + cn4.getText().toString();
                        String ed = exdy.getText().toString() + "-" + exd.getText().toString();


                        String[] data = {cn, ed, sn.getText().toString()};
                        sqlserver.connection("addpay", data);
                    }
                }).start();*/


                /*//利用Toast的靜態函式makeText來建立Toast物件
                Toast toast = Toast.makeText(addpayActivity.this, "新增成功", Toast.LENGTH_LONG);
                //顯示Toast
                toast.show();*/


                /*try{
                    String[] data={cn,ed,sn.getText().toString()};
                    sqlserver.connection("addpay",data);
                    //利用Toast的靜態函式makeText來建立Toast物件
                    Toast toast=Toast.makeText(addpayActivity.this,"新增成功",Toast.LENGTH_LONG);
                    //顯示Toast
                    toast.show();
                }
                catch(Exception e){
                    //利用Toast的靜態函式makeText來建立Toast物件
                    Toast toast=Toast.makeText(addpayActivity.this,"新增失敗",Toast.LENGTH_LONG);
                    //顯示Toast
                    toast.show();

                    e.printStackTrace();
                }*/
                EditText cardname = (EditText) findViewById(R.id.cardname);
                EditText cn1 = (EditText) findViewById(R.id.cardnum1);
                EditText cn2 = (EditText) findViewById(R.id.cardnum2);
                EditText cn3 = (EditText) findViewById(R.id.cardnum3);
                EditText cn4 = (EditText) findViewById(R.id.cardnum4);
                EditText exdy = (EditText) findViewById(R.id.expirydateyear);
                EditText exd = (EditText) findViewById(R.id.expirydate);
                EditText sn = (EditText) findViewById(R.id.safenum);
                String cn = cn1.getText().toString() + "-" + cn2.getText().toString() + "-" + cn3.getText().toString() + "-" + cn4.getText().toString();
                String ed = exdy.getText().toString() + "-" + exd.getText().toString();

                //必須完成Firebase Setup後才能使用
                //取得Firebase連結(連接資料庫)
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Firebase的某個目錄
                DatabaseReference credit = database.getReference("pay");
                buyerpay data = new buyerpay(cardname.getText().toString(), cn, ed, sn.getText().toString());
                credit.child(fuser.getUid()).setValue(data);

                //利用Toast的靜態函式makeText來建立Toast物件
                Toast toast = Toast.makeText(buy_yours_addpay.this, "新增成功", Toast.LENGTH_LONG);
                //顯示Toast
                toast.show();

                Intent intent = new Intent();
                intent.setClass(buy_yours_addpay.this, buy_yours_paydata.class);
                startActivity(intent);


            }
        });

    }
}
