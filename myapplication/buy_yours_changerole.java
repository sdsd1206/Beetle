package com.example.jack.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class buy_yours_changerole extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_yours_changerole);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("切換身分");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_yours_changerole.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        //ImageButton buyerbtn = (ImageButton) findViewById(R.id.tobuyerbtn);
        Button buyerbtn = (Button) findViewById(R.id.tobuyerbtn);
        buyerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent();
                intent.setClass(changeroleActvity.this,MainActivity.class);
                startActivity(intent);
                //利用Toast的靜態函式makeText來建立Toast物件
                Toast toast=Toast.makeText(changeroleActvity.this,"已切換為買家",Toast.LENGTH_LONG);
                //顯示Toast
                toast.show();*/
                Intent intent = new Intent();
                intent.setClass(buy_yours_changerole.this, buy.class);
                intent.putExtra("role", "buyer");
                startActivity(intent);
            }
        });
        //ImageButton sellerbtn = (ImageButton) findViewById(R.id.tosellerbtn);
        Button sellerbtn = (Button) findViewById(R.id.tosellerbtn);
        sellerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_yours_changerole.this, sell.class);
                intent.putExtra("role", "seller");
                startActivity(intent);
            }
        });
    }
   /* @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.tobuyerbtn:
                Intent intent=new Intent();
                intent.setClass(changeroleActvity.this,MainActivity.class);
                startActivity(intent);
            case R.id.tosellerbtn:
                intent=new Intent();
                intent.setClass(changeroleActvity.this,MainActivity.class);
                startActivity(intent);
        }
    }*/
}

