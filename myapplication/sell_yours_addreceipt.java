package com.example.jack.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sell_yours_addreceipt extends AppCompatActivity {

    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_yours_addreceipt);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("新增資料");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(sell_yours_addreceipt.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);

            }
        });

        Button add = (Button) findViewById(R.id.addbtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText bank = (EditText) findViewById(R.id.bank);
                EditText branch = (EditText) findViewById(R.id.branch);
                EditText accountno = (EditText) findViewById(R.id.accountno);
                EditText accountname = (EditText) findViewById(R.id.accountname);

                //必須完成Firebase Setup後才能使用
                //取得Firebase連結(連接資料庫)
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Firebase的某個目錄
                DatabaseReference credit = database.getReference("sellerbank");
                sellercredit data = new sellercredit(accountno.getText().toString(), bank.getText() + "銀行",
                        branch.getText() + "分行", accountname.getText().toString());
                credit.child(fuser.getUid()).setValue(data);

                //利用Toast的靜態函式makeText來建立Toast物件
                Toast toast = Toast.makeText(sell_yours_addreceipt.this, "新增成功", Toast.LENGTH_LONG);
                //顯示Toast
                toast.show();
                Intent intent = new Intent();
                intent.setClass(sell_yours_addreceipt.this, sell_yours_receipt.class);
                startActivity(intent);
            }
        });
    }


}
