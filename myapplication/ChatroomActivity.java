package com.example.jack.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatroomActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String uid;
    private user u;
    private String u2;
    private DatabaseReference ref;
    private DatabaseReference ref2;
    private DatabaseReference ref3;
    private DatabaseReference ref4;
    private DatabaseReference ref5;
    private List<chatmsg> msgList = new ArrayList<>();
    private EditText inputTest;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private FirebaseUser user;
    private chatmsg c;
    private String youid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        //各种赋值初始化
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        inputTest = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        Intent IntentObj = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = IntentObj.getExtras();
        youid = bundle.getString("uid");
        //创建一个LinearLayoutManager（线性布局）对象将它设置到RecyclerView
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutmanager);
        //调用构造方法创造实例,参数消息集合
        getData();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String date = sDateFormat.format(new java.util.Date());
                ref2 = FirebaseDatabase.getInstance().getReference("chatmsg");
                String key = ref.push().getKey();
                ref2.child(uid).child(youid).child(key).child("youid").setValue(youid);
                ref2.child(uid).child(youid).child(key).child("who").setValue("1");
                ref2.child(uid).child(youid).child(key).child("roomcontext").setValue(inputTest.getText().toString());
                ref2.child(uid).child(youid).child(key).child("time").setValue(date);
                ref2.child(uid).child(youid).child(key).child("username").setValue(u.getName());
                ref3 = FirebaseDatabase.getInstance().getReference("chatmsg");
                ref3.child(youid).child(uid).child(key).child("youid").setValue(uid);
                ref3.child(youid).child(uid).child(key).child("who").setValue("0");
                ref3.child(youid).child(uid).child(key).child("roomcontext").setValue(inputTest.getText().toString());
                ref3.child(youid).child(uid).child(key).child("time").setValue(date);
                ref3.child(youid).child(uid).child(key).child("username").setValue(u.getName());
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                inputTest.setText("");
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ref5 = FirebaseDatabase.getInstance().getReference("user").child(youid).child("nickname");
        ref5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u2 = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(u2);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatroomActivity.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);

            }
        });
    }

    private void getData() {
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        ref = FirebaseDatabase.getInstance().getReference("chatmsg").child(uid + "").child(youid + "");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    c = ds.getValue(chatmsg.class);
                    msgList.add(c);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref4 = FirebaseDatabase.getInstance().getReference("user").child(uid);
        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(user.class);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
