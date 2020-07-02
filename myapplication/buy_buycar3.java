package com.example.jack.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.List;

public class buy_buycar3 extends AppCompatActivity {

    private ListView list;
    private Buycar3Adapter adapter;
    private Toolbar toolbar;
    private List<Buycar> buycarlist = new ArrayList<>();
    private List<String> buycarlist2 = new ArrayList<>();
    private Buycar buycar;
    private DatabaseReference ref;
    private ArrayList<Integer> productNo = new ArrayList<>();
    private ArrayList<String> productnum = new ArrayList<>();
    private DatabaseReference ref2;
    private DatabaseReference ref3;
    private DatabaseReference ref4;
    private address add;
    private user u;
    private buyerpay p;
    private FirebaseUser user;
    private int allprice = 0;
    TextView sum;
    TextView name;
    TextView phonenum;
    TextView adress;
    TextView creditcardnum;
    private int cnt = 0;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_buycar3);
        allprice = 0;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("訂單送出");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_buycar3.this, buy.class);
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("productNo", productNo);
                bundle.putStringArrayList("productnum", productnum);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        sum = (TextView) findViewById(R.id.textView27);
        list = (ListView) findViewById(R.id.buycar3list);
        Intent IntentObj = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = IntentObj.getExtras();
        name = (TextView) findViewById(R.id.textView7);
        phonenum = (TextView) findViewById(R.id.textView8);
        adress = (TextView) findViewById(R.id.textView6);
        creditcardnum = (TextView) findViewById(R.id.textView5);
        productNo = bundle.getIntegerArrayList("productNo");
        productnum = bundle.getStringArrayList("productnum");
        getData();
        buynum();
    }

    private void computesum(String num, String price, int cont) {
        if (cnt < cont) {
            int a = Integer.parseInt(num) * Integer.parseInt(price);
            allprice += a;
            sum.setText("總金額:" + allprice);
            cnt++;
        }
    }

    private void buynum() {
        for (int i = 0; i < productNo.size(); i++) {
            if (productNo.get(i) == 1) {
                buycarlist2.add(productnum.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void getData() {
        adapter = new Buycar3Adapter(this);
        list.setAdapter(adapter);
        if (user != null) {
            String uid = user.getUid();
            ref = FirebaseDatabase.getInstance().getReference("buycar").child(uid + "");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    buycarlist.clear();
                    int i = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (productNo.get(i) == 1) {
                            buycar = ds.getValue(Buycar.class);
                            buycarlist.add(buycar);
                        }
                        i++;
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            ref2 = FirebaseDatabase.getInstance().getReference("user").child(uid + "");
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    u = dataSnapshot.getValue(user.class);
                    name.setText(name.getText() + ":" + u.getName());
                    phonenum.setText(phonenum.getText() + ":" + u.getPhone());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            ref3 = FirebaseDatabase.getInstance().getReference("address").child(uid + "").child("homeaddress").child("address");
            ref3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    address = dataSnapshot.getValue(String.class);
                    adress.setText(adress.getText() + ":" + address);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            ref4 = FirebaseDatabase.getInstance().getReference("pay").child(uid + "");
            ref4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    p = dataSnapshot.getValue(buyerpay.class);
                    creditcardnum.setText(creditcardnum.getText() + ":" + p.getCardno());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    static class ViewHolder {
        public ImageButton img1;
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
        public Button b1;
    }

    public class Buycar3Adapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        private Buycar3Adapter(Context context) {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            //How many items are in the data set represented by this Adapter.
            //在此适配器中所代表的数据集中的条目数
            return buycarlist.size();
        }

        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return null;
        }

        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }

        //Get a View that displays the data at the specified position in the data set.
        //获取一个在数据集中指定索引的视图来显示数据
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if (convertView == null) {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.buy_buycar3_item, null);
                holder.img1 = (ImageButton) convertView.findViewById(R.id.imageButton3);
                holder.tv1 = (TextView) convertView.findViewById(R.id.textView5);
                holder.tv2 = (TextView) convertView.findViewById(R.id.textView19);
                holder.tv3 = (TextView) convertView.findViewById(R.id.textView20);
                holder.tv4 = (TextView) convertView.findViewById(R.id.textView25);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            computesum(buycarlist2.get(position), buycarlist.get(position).price, adapter.getCount());
            Glide.with(buy_buycar3.this)
                    .load(buycarlist.get(position).pic)
                    .into(holder.img1);
            holder.tv1.setText(buycarlist.get(position).name);//(buycarlist.get(position).name);
            holder.tv2.setText(buycarlist.get(position).price);//(((int)buycarlist.get(position).price)+"");
            holder.tv3.setText(buycarlist.get(position).sellername + "");//(buycarlist.get(position).sellername);
            holder.tv4.setText(buycarlist2.get(position));
            return convertView;
        }
    }
}
