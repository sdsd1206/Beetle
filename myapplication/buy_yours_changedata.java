package com.example.jack.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

public class buy_yours_changedata extends AppCompatActivity {

    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref;
    private EditText nameedt;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_yours_changedata);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("更改資料");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_yours_changedata.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        Button sbmbtn = (Button) findViewById(R.id.submitbtn);
        sbmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(buy_yours_changedata.this);
                builder.setMessage("確定要更改?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
                        nameedt = findViewById(R.id.newname);
                        String nname = nameedt.getText().toString();
                        ref.child("nickname").setValue(nname);
                        Toast toast = Toast.makeText(buy_yours_changedata.this, "更改成功", Toast.LENGTH_LONG);
                        toast.show();
                        /*EditText nameedt = (EditText) findViewById(R.id.newname);
                        String nname = nameedt.getText().toString();
                        Intent intent = new Intent();
                                /*intent.setClass(changedataActivity.this,MainActivity.class);
                                intent.putExtra("newname",nname);
                                startActivity(intent);
                                Toast toast=Toast.makeText(changedataActivity.this,"更改完成",Toast.LENGTH_LONG);
                                toast.show();
                                finish();
                        intent.setClass(buy_yours_changedata.this, sqlserver.class);
                        intent.putExtra("newname", nname);
                        startActivity(intent);*/
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();

            }
        });
    }
}
