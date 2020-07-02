package com.example.jack.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class buy_yours_waitgoods extends AppCompatActivity {

    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private ListView mListView;
    //private List<Map<String, Object>> data;
    private Toolbar toolbar;
    private List<buyergoods> data = new ArrayList<>();
    private buyergoods buyergoods = new buyergoods();
    private WaitAdapter<buyergoods> adapter;
    private String[] key = new String[50];
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_yours_waitgoods);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("等待收貨");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_yours_waitgoods.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        getData();
    }

    /*private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        // 看你有幾組資料
        for (int i = 1; i <= 3; i++) {
            map = new HashMap<>();
            map.put("no", i + "");
            map.put("image", "");
            map.put("itemname", "品名: " + "甜甜圈");
            map.put("price", "價格: " + 100);
            map.put("sellername", "賣家名稱: " + "傅淑婷");
            list.add(map);
        }
        return list;
    }*/

    private void getData() {

        mListView = findViewById(R.id.waitlist);
        adapter = new WaitAdapter<>();
        mListView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("buy_order_waiting").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    buyergoods = ds.getValue(buyergoods.class);
                    data.add(buyergoods);
                    adapter.notifyDataSetChanged();
                    key[i] = ds.getKey();
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef=storageRef.child("productpic");
        String filename="apple.jpg";
        imageRef.child(filename);*/
    }

    private class WaitAdapter<T> extends BaseAdapter {
        @Override
        public int getCount() {          // getCount() 就是可以取得到底有多少列的方法
            return data.size();
        }

        @Override
        public Object getItem(int position) {       // 要取得某一列的內容 就是使用 getItem() 這個方法
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {       // 取得某一列的 id 就使用 getItemId() 這個方法
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {     // 修改某一列 View 的內容就是利用 getView() 這個方法
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.buy_yours_waitgoods_item, null);
                holder = new ViewHolder();
                holder.image = (ImageView) v.findViewById(R.id.confirmimage);
                holder.name = (TextView) v.findViewById(R.id.nametv);
                holder.price = (TextView) v.findViewById(R.id.pricetv);
                holder.sellername = (TextView) v.findViewById(R.id.sellernametv);
                holder.cencel = (Button) v.findViewById(R.id.changebtn);
                holder.cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(buy_yours_waitgoods.this);
                        builder.setMessage("確定要刪除?");
                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                final DatabaseReference ref = database.getReference("buy_order_waiting").child(fuser.getUid());
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //int item=position+1;
                                        //ref.child(item+"").removeValue();
                                        ref.child(key[position]).removeValue();
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                //利用Toast的靜態函式makeText來建立Toast物件
                                Toast toast = Toast.makeText(buy_yours_waitgoods.this, "刪除成功", Toast.LENGTH_LONG);
                                //顯示Toast
                                toast.show();
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
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            /*holder.name.setText(data.get(position).get("itemname").toString());
            holder.price.setText(data.get(position).get("price").toString());
            holder.sellername.setText(data.get(position).get("sellername").toString());*/

            holder.name.setText("品名:" + data.get(position).getName());
            holder.price.setText("價格:" + data.get(position).getPrice());
            holder.sellername.setText("賣家:" + data.get(position).getSeller());
            Glide.with(buy_yours_waitgoods.this)
                    .load(data.get(position).getPrice())
                    .into(holder.image);

            return v;
        }


        class ViewHolder {
            public ImageView image;
            public TextView name;
            public TextView price;
            public TextView sellername;
            public Button cencel;
        }
    }
}
