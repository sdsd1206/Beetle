package com.example.jack.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class buy_store_itemdetail extends AppCompatActivity {

    Button back;
    private Toolbar toolbar;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    Spinner spinner;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private item data;
    private String quancount = "";
    String face = "";
    private DatabaseReference ref;
    ImageView imitem;
    ImageView imseller;
    TextView itemprove;
    private Button chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_store_itemdetail);

        imitem = findViewById(R.id.imageView7);
        imseller = findViewById(R.id.imageView9);
        itemprove = findViewById(R.id.textView30);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("商品資訊");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_store_itemdetail.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });
        spinner = (Spinner) findViewById(R.id.buy_detaispinner);
        setspinner();

        Intent msg = this.getIntent();//取得傳遞過來的資料,商品資料
        final String name = msg.getStringExtra("seller");
        TextView seller = (TextView) findViewById(R.id.item_seller);
        seller.setText("賣家：" + name);

        final String item = msg.getStringExtra("item");

        TextView info = (TextView) findViewById(R.id.item_detail_tx);
        try {
            data = (item) msg.getSerializableExtra("data");
            info.setText(item + "\n\n" + "售價：" + data.getPrice() + "\n" + data.getInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //建立user物件
        getdata();

        chat = (Button) findViewById(R.id.button20);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("uid", data.getUid());
                intent.setClass(buy_store_itemdetail.this, ChatroomActivity.class);
                startActivity(intent);
            }
        });


        Button buy = (Button) findViewById(R.id.button24);

        buy.setOnClickListener(new View.OnClickListener() {
            LayoutInflater inflater = LayoutInflater.from(buy_store_itemdetail.this);
            final View dia = inflater.inflate(R.layout.buy_store_detail_buy, null);
            EditText count = (EditText) dia.findViewById(R.id.editText1);
            TextView addr = dia.findViewById(R.id.editText);
            public void onClick(View view) {
                count.setText(quancount + "");
                final AlertDialog alertDialog = new AlertDialog.Builder(buy_store_itemdetail.this)
                        .setView(dia)
                        .setTitle("購買明細：")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (data.getUid() != null) {
                                    addorder(addr.getText() + "", count.getText() + "");
                                    Toast.makeText(buy_store_itemdetail.this, "已建立訂單", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(buy_store_itemdetail.this, "失敗", Toast.LENGTH_SHORT).show();
                                }
                                onResume();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(buy_store_itemdetail.this, "取消", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }//end onClick
        });

        Button car = (Button) findViewById(R.id.button23);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcar();
                Toast.makeText(getApplicationContext(), "已加入購物車", Toast.LENGTH_SHORT).show();

            }//end onClick
        });

        download(imitem, data.getPic()); //商品圖片

        itemprove.setOnClickListener(new View.OnClickListener() {
            LayoutInflater inflater = LayoutInflater.from(buy_store_itemdetail.this);
            final View dia = inflater.inflate(R.layout.item_prove_alertdia, null);
            ImageView prove;

            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(buy_store_itemdetail.this)
                        .setView(dia)
                        .setTitle("商品貨源證明：")
                        .setMessage("有問題請聯繫賣家")
                        .setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(buy_store_itemdetail.this, "關閉", Toast.LENGTH_SHORT).show();
                                onResume();
                                dialog.dismiss();
                            }
                        })
                        .show();
                prove = dia.findViewById(R.id.imageView6);
                download(prove, data.getProve());
            }//end onClick
        });

    }

    public void addorder(String addr, String quan) {//新增
        FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String t = df.format(c.getTime());


        DatabaseReference ref = database.getReference("buy_order_waiting");
        Order1 buyorder = new Order1(data.getName(), addr, data.getPic(), data.getPrice(), quan, data.getSeller(), t);
        if (user != null) {
            ref.child(user.getUid()).push().setValue(buyorder);
        }
        DatabaseReference monthslaes = database.getReference("good"); //更改資料庫monthsales
        int monthsal = Integer.parseInt(data.getMonthsales()) + Integer.parseInt(quan);
        monthslaes.child(data.getUid()).child(data.getKey()).child("monthsales").setValue(monthsal + "");

        DatabaseReference ref2 = database.getReference("sell_order_store");
        if (user != null) {
            Order2 sellorder = new Order2(data.getName(), addr, "未付款", "未出貨", data.getPic()
                    , data.getPrice(), quan, user.getDisplayName(), t);
            ref2.child(data.getUid()).push().setValue(sellorder);
        }

    }

    public void addcar() {
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = database1.getReference("buycar");
        String key = ref1.child(user.getUid()).push().getKey();
        Buycar buycar = new Buycar(data.getName(), data.getSeller(), data.getPrice(), data.getPic(), user.getUid(), key);
//        buycar.setUid(data.getUid());
        if (user != null) {
            ref1.child(user.getUid()).child(key).setValue(buycar);
        }
    }

    private void download(final ImageView im, final String pic) {
        StorageReference mStorageRef = storage.getReference();
        final StorageReference ref2 = mStorageRef;
        ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(pic)
                        .into(im);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    private void download1(final ImageView im) {  //寫死(已改沒再用)
        StorageReference mStorageRef = storage.getReference();
        final StorageReference ref2 = mStorageRef.child("userface/tiger_PNG23227.png");
        ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(ref2)
                        .into(im);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    public void setspinner() {
        String[] quan = new String[40];
        for (int i = 0; i < quan.length; i++) {
            quan[i] = i + 1 + "";
        }
        ArrayAdapter<String> quantity = new ArrayAdapter<String>(buy_store_itemdetail.this,
                android.R.layout.simple_spinner_dropdown_item,
                quan);
        spinner.setAdapter(quantity);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quancount = spinner.getSelectedItem().toString() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getdata() {
        ref = FirebaseDatabase.getInstance().getReference("user").child(data.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            user databaseuser;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseuser = (user) dataSnapshot.getValue(user.class);
                download(imseller, databaseuser.getFace());

                face = databaseuser.getFace();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
