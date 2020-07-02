package com.example.jack.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class sell_selldata_order_itemdetail extends AppCompatActivity {

    private Toolbar toolbar;
    private item data;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference ref;
    ImageView imitem;
    ImageView imseller;
    TextView itemprove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_selldata_order_itemdetail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("商品資料");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(sell_selldata_order_itemdetail.this, sell_selldata_order_detail.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        imitem = findViewById(R.id.imageView7);
        imseller = findViewById(R.id.imageView9);
        itemprove = findViewById(R.id.textView29);

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

        download(imitem, data.getPic()); //商品圖片

        itemprove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(sell_selldata_order_itemdetail.this);
                final View dia = inflater.inflate(R.layout.item_prove_alertdia, null);
                ImageView prove;
                final AlertDialog alertDialog = new AlertDialog.Builder(sell_selldata_order_itemdetail.this)
                        .setView(dia)
                        .setTitle("商品貨源證明：")
                        .setMessage("有問題請聯繫賣家")
                        .setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(sell_selldata_order_itemdetail.this, "關閉", Toast.LENGTH_SHORT).show();
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

    public void getdata() {
        ref = FirebaseDatabase.getInstance().getReference("user").child(data.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            user databaseuser;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseuser = (user) dataSnapshot.getValue(user.class);
                download(imseller, databaseuser.getFace());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
