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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class buy_yours_confirmgoods extends AppCompatActivity {

    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private ListView mListView;
    //private List<Map<String, Object>> data;
    private Toolbar toolbar;
    private List<buyergoods> data = new ArrayList<>();
    private buyergoods buyergoods = new buyergoods();
    private ConfirmAdapter<buyergoods> adapter;
    private FirebaseStorage storage;
    private buyergoods temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_yours_confirmgoods);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("確認收貨");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_yours_confirmgoods.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        getData();
        mListView = (ListView) findViewById(R.id.confirmlist);
        mListView.setAdapter(new ConfirmAdapter());

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
        adapter = new ConfirmAdapter<>();
        mListView = findViewById(R.id.confirmlist);
        mListView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("buy_order_confirm").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    buyergoods = ds.getValue(buyergoods.class);
                    data.add(buyergoods);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class ConfirmAdapter<T> extends BaseAdapter {
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
                v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.buy_yours_confirmgoods_item, null);
                holder = new ViewHolder();
                holder.image = (ImageView) v.findViewById(R.id.confirmimage);
                holder.name = (TextView) v.findViewById(R.id.nametv);
                holder.price = (TextView) v.findViewById(R.id.pricetv);
                holder.sellername = (TextView) v.findViewById(R.id.sellernametv);
                holder.change = (Button) v.findViewById(R.id.changebtn);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            /*switch (position){
                case 0:
                    holder.image.setImageResource(R.drawable.donut);
                    String n=holder.name.getText().toString();
                    holder.name.setText(n+"甜甜圈");
                    String p=holder.price.getText().toString();
                    holder.price.setText(p+100);
                    String sn=holder.sellername.getText().toString();
                    holder.sellername.setText(sn+"傅淑婷");
            }*/
            holder.name.setText("品名:" + data.get(position).getName());
            holder.price.setText("價格:" + data.get(position).getPrice());
            holder.sellername.setText("賣家名稱:" + data.get(position).getSeller());
            holder.change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(buy_yours_confirmgoods.this);
                    builder.setMessage("確定要退換貨?");
                    builder.setPositiveButton("換貨", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference confirm = database.getReference("buy_order_confirm").child(fuser.getUid());
                            confirm.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int item = position + 1;
                                    temp = dataSnapshot.child(item + "").getValue(buyergoods.class);
                                    temp.setStatus("換貨");
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference returngoods = database.getReference("buy_order_return").child(fuser.getUid());
                                    returngoods.push().setValue(temp);
                                    //利用Toast的靜態函式makeText來建立Toast物件
                                    Toast toast = Toast.makeText(buy_yours_confirmgoods.this, "已加入替換", Toast.LENGTH_LONG);
                                    //顯示Toast
                                    toast.show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    builder.setNegativeButton("退貨", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference confirm = database.getReference("buy_order_confirm").child(fuser.getUid());
                            confirm.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int item = position + 1;
                                    temp = dataSnapshot.child(item + "").getValue(buyergoods.class);
                                    temp.setStatus("退貨");
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference changegoods = database.getReference("buy_order_return").child(fuser.getUid());
                                    changegoods.push().setValue(temp);
                                    //利用Toast的靜態函式makeText來建立Toast物件
                                    Toast toast = Toast.makeText(buy_yours_confirmgoods.this, "已加入退貨", Toast.LENGTH_LONG);
                                    //顯示Toast
                                    toast.show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    builder.create().show();
                }
            });

            //download(holder.image);

            Glide.with(buy_yours_confirmgoods.this)
                    .load(data.get(position).getPrice())
                    .into(holder.image);
            return v;
        }


        class ViewHolder {
            public ImageView image;
            public TextView name;
            public TextView price;
            public TextView sellername;
            public Button change;
        }
    }
}

