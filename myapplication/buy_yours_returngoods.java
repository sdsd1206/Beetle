package com.example.jack.myapplication;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class buy_yours_returngoods extends AppCompatActivity {

    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private ListView mListView;
    //private List<Map<String, Object>> data;
    private ArrayList<buyergoods> data = new ArrayList<>();
    private buyergoods buyergoods = new buyergoods();
    private ReturnAdapter<buyergoods> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_yours_returngoods);


        mListView = (ListView) findViewById(R.id.returnlist);
        //mListView.setAdapter(new ReturnAdapter());
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
            map.put("itemname", "品名: "+"甜甜圈");
            map.put("price", "價格: "+100);
            map.put("sellername", "賣家名稱: "+"傅淑婷");
            list.add(map);
        }
        return list;
    }*/

    private void getData() {
        adapter = new ReturnAdapter<>();
        mListView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("buy_order_return").child(fuser.getUid());
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

    private class ReturnAdapter<T> extends BaseAdapter {
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
        public View getView(int position, View convertView, ViewGroup parent) {     // 修改某一列 View 的內容就是利用 getView() 這個方法
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.buy_yours_returngoods_item, null);
                holder = new ViewHolder();
                holder.image = (ImageView) v.findViewById(R.id.confirmimage);
                holder.name = (TextView) v.findViewById(R.id.nametv);
                holder.price = (TextView) v.findViewById(R.id.pricetv);
                holder.sellername = (TextView) v.findViewById(R.id.sellernametv);
                holder.status = (TextView) v.findViewById(R.id.status);
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
            holder.status.setText(data.get(position).getStatus());

            Glide.with(buy_yours_returngoods.this)
                    .load(data.get(position).getPic())
                    .into(holder.image);
            return v;
        }


        class ViewHolder {
            public ImageView image;
            public TextView name;
            public TextView price;
            public TextView sellername;
            public TextView status;
        }
    }
}