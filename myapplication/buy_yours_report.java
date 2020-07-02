package com.example.jack.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class buy_yours_report extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_yours_report);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("聯絡客服");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_yours_report.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        Button sbmbtn = (Button) findViewById(R.id.submitbtn);
        sbmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(buy_yours_report.this);
                builder.setMessage("您的意見已經傳送成功,\n" +
                        "謝謝您。")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*Toast toast=Toast.makeText(reportActivity.this,"提交成功!",Toast.LENGTH_LONG);
                                toast.show();
                                finish();*/
                                Intent intent = new Intent();
                                intent.setClass(buy_yours_report.this, buy.class);
                                startActivity(intent);
                            }
                        });

                builder.create().show();
            }
        });
    }
}
