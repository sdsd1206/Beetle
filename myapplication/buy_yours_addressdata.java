package com.example.jack.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class buy_yours_addressdata extends AppCompatActivity {

    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private Toolbar toolbar;
    TextView postnum;
    TextView homeaddress;
    TextView convname;
    TextView branch;
    buyeraddress buyeraddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_yours_addressdata);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("地址資料");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_yours_addressdata.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        Button addbtn = (Button) findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_yours_addressdata.this, buy_yours_changeaddress.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("address").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postnum = (TextView) findViewById(R.id.postnum);
                homeaddress = (TextView) findViewById(R.id.homeaddress);
                convname = (TextView) findViewById(R.id.convname);
                branch = (TextView) findViewById(R.id.branch);


                String p = dataSnapshot.child("homeaddress").child("postno").getValue(String.class);
                String h = dataSnapshot.child("homeaddress").child("address").getValue(String.class);
                String c = dataSnapshot.child("storeaddress").child("store").getValue(String.class);
                String b = dataSnapshot.child("storeaddress").child("branchname").getValue(String.class);

                postnum.setText("郵遞區號:" + p);
                homeaddress.setText("地址:" + h);
                convname.setText("超商名稱:" + c);
                branch.setText("分店名稱:" + b);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}