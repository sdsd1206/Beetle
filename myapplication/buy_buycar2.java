package com.example.jack.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class buy_buycar2 extends AppCompatActivity {


    private Date currentTime = Calendar.getInstance().getTime();
    private ListView list;
    private Buycar2Adapter adapter;
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
    private DatabaseReference ref5;
    private DatabaseReference ref6;
    private DatabaseReference ref7;
    private DatabaseReference ref8;
    private user u;
    private user u2;
    private ArrayList<user> uu = new ArrayList<>();
    private buyerpay p;
    private address add;
    private FirebaseUser user;
    private String uid;
    TextView name;
    TextView phonenum;
    TextView adress;
    TextView creditcardnum;
    EditText edit;
    TextView sum;
    private int cnt = 0;
    private int allprice = 0;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_buycar2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("確認訂單");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_buycar2.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);

            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        Button sent = (Button) findViewById(R.id.button4);
        list = (ListView) findViewById(R.id.buycar2list);
        name = (TextView) findViewById(R.id.textView7);
        phonenum = (TextView) findViewById(R.id.textView8);
        adress = (TextView) findViewById(R.id.textView6);
        creditcardnum = (TextView) findViewById(R.id.textView5);
        Intent IntentObj = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = IntentObj.getExtras();
        Button addr = (Button) findViewById(R.id.button3);
        Button nna = (Button) findViewById(R.id.button1);
        Button pph = (Button) findViewById(R.id.button2);
        Button ccd = (Button) findViewById(R.id.button5);
        productNo = bundle.getIntegerArrayList("productNo");
        productnum = bundle.getStringArrayList("productnum");
        ref = FirebaseDatabase.getInstance().getReference("buycar").child(uid + "");
        ref3 = FirebaseDatabase.getInstance().getReference("address").child(uid + "").child("homeaddress").child("address");
        ref5 = FirebaseDatabase.getInstance().getReference("address").child(uid + "").child("homeaddress").child("address");
        ref4 = FirebaseDatabase.getInstance().getReference("pay").child(uid + "");
        ref2 = FirebaseDatabase.getInstance().getReference("user").child(uid + "");
        getData();
        buynum();
        addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeuserdata();
            }
        });
        nna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeuserdatau();
            }
        });
        pph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeuserdatau2();
            }
        });
        ccd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeuserdatap();
            }
        });
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    ref7 = FirebaseDatabase.getInstance().getReference("sell_order_store");
                    ref6 = FirebaseDatabase.getInstance().getReference("buy_order_waiting");

                    for (int i = 0; i < buycarlist.size(); i++) {
                        String key = ref.push().getKey();
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String date = sDateFormat.format(new java.util.Date());
                        ref7.child(uid).child(key).child("name").setValue(buycarlist.get(i).name);
                        ref7.child(uid).child(key).child("pic").setValue(buycarlist.get(i).pic);
                        ref7.child(uid).child(key).child("quantity").setValue(buycarlist2.get(i) + "");
                        ref7.child(uid).child(key).child("price").setValue(buycarlist.get(i).price);
                        ref7.child(uid).child(key).child("address").setValue(address);
                        ref7.child(uid).child(key).child("time").setValue(date + "");
                        ref7.child(uid).child(key).child("state").setValue("未付款");
                        ref7.child(uid).child(key).child("shipping").setValue("待出貨");
                        ref7.child(uid).child(key).child("buyer").setValue(user.getDisplayName() + "");


                        ref6.child(uid).child(key).child("name").setValue(buycarlist.get(i).name);
                        ref6.child(uid).child(key).child("pic").setValue(buycarlist.get(i).pic);
                        ref6.child(uid).child(key).child("quantity").setValue(buycarlist2.get(i) + "");
                        ref6.child(uid).child(key).child("price").setValue(buycarlist.get(i).price);
                        ref6.child(uid).child(key).child("address").setValue(address);
                        ref6.child(uid).child(key).child("time").setValue(date + "");
                        ref6.child(uid).child(key).child("shipping").setValue("待出貨");
                        //ref6.child(uid).child(key).child("seller").setValue(user.getEmail()+"");
                        ref6.child(uid).child(key).child("seller").setValue(uu.get(i).getMail() + "");
                    }
                }
                Intent intent = new Intent(buy_buycar2.this, buy_buycar3.class);
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("productNo", productNo);
                bundle.putStringArrayList("productnum", productnum);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        sum = findViewById(R.id.textView27);
    }

    private void computesum(String num, String price, int cont) {
        if (cnt < cont) {
            int a = Integer.parseInt(num) * Integer.parseInt(price);
            allprice += a;
            sum.setText("總金額:" + allprice);
            cnt++;
        }
    }

    private void setseller(Buycar b) {

        ref8 = FirebaseDatabase.getInstance().getReference("user").child(b.uid);
        ref8.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    u2 = dataSnapshot.getValue(user.class);
                    uu.add(u2);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void buynum() {
        buycarlist2.clear();
        for (int i = 0; i < productNo.size(); i++) {
            if (productNo.get(i) == 1) {
                buycarlist2.add(productnum.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void changeuserdata() {
        edit = new EditText(buy_buycar2.this);
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                address = dataSnapshot.getValue(String.class);
                edit.setText(address);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AlertDialog.Builder dialog = new AlertDialog.Builder(buy_buycar2.this);
        dialog.setTitle("修改地址：");
        dialog.setView(edit);
        dialog.setCancelable(false);  // disable click back button
        dialog.setPositiveButton("確認修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("address", edit.getText().toString());//前面的字是child後面的字是要修改的value值
                ref5.updateChildren(childUpdates);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    private void changeuserdatap() {
        edit = new EditText(buy_buycar2.this);
        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                p = dataSnapshot.getValue(buyerpay.class);
                edit.setText(p.getCardno());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AlertDialog.Builder dialog = new AlertDialog.Builder(buy_buycar2.this);
        dialog.setTitle("修改卡號：");
        dialog.setView(edit);
        dialog.setCancelable(false);  // disable click back button
        dialog.setPositiveButton("確認修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("cardno", edit.getText().toString());//前面的字是child後面的字是要修改的value值
                ref4.updateChildren(childUpdates);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    private void changeuserdatau() {
        edit = new EditText(buy_buycar2.this);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(user.class);
                edit.setText(u.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AlertDialog.Builder dialog = new AlertDialog.Builder(buy_buycar2.this);
        dialog.setTitle("修改名字：");
        dialog.setView(edit);
        dialog.setCancelable(false);  // disable click back button
        dialog.setPositiveButton("確認修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("name", edit.getText().toString());//前面的字是child後面的字是要修改的value值
                ref2.updateChildren(childUpdates);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    private void changeuserdatau2() {
        edit = new EditText(buy_buycar2.this);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(user.class);
                edit.setText(u.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AlertDialog.Builder dialog = new AlertDialog.Builder(buy_buycar2.this);
        dialog.setTitle("修改電話：");
        dialog.setView(edit);
        dialog.setCancelable(false);  // disable click back button
        dialog.setPositiveButton("確認修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("phone", edit.getText().toString());//前面的字是child後面的字是要修改的value值
                ref2.updateChildren(childUpdates);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    public void getData() {
        adapter = new Buycar2Adapter(this);
        list.setAdapter(adapter);
        if (user != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    buycarlist.clear();
                    int i = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (productNo.get(i) == 0) {

                        } else {
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


            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String ass = "名字:";
                    String aa = "電話:";
                    u = dataSnapshot.getValue(user.class);
                    name.setText(ass + ":" + u.getName());
                    phonenum.setText(aa + ":" + u.getPhone());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            ref3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String asas = "配送地址:";
                    address = dataSnapshot.getValue(String.class);
                    adress.setText(asas + ":" + address);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            ref4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String as = "付費卡號:";
                    p = dataSnapshot.getValue(buyerpay.class);
                    creditcardnum.setText(as + p.getCardno());
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

    public class Buycar2Adapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        private Buycar2Adapter(Context context) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if (convertView == null) {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.buy_buycar2_item, null);
                holder.img1 = (ImageButton) convertView.findViewById(R.id.imageButton3);
                holder.tv1 = (TextView) convertView.findViewById(R.id.textView5);
                holder.tv2 = (TextView) convertView.findViewById(R.id.textView19);
                holder.tv3 = (TextView) convertView.findViewById(R.id.textView20);
                holder.tv4 = (TextView) convertView.findViewById(R.id.textView25);
                holder.b1 = (Button) convertView.findViewById(R.id.button17);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            setseller(buycarlist.get(position));
            computesum(buycarlist2.get(position), buycarlist.get(position).price, adapter.getCount());
            Glide.with(buy_buycar2.this)
                    .load(buycarlist.get(position).pic)
                    .into(holder.img1);
            holder.tv1.setText(buycarlist.get(position).name);
            holder.tv2.setText(buycarlist.get(position).price);
            holder.tv3.setText(buycarlist.get(position).sellername);
            holder.tv4.setText(buycarlist2.get(position));
            holder.b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buycarlist.remove(position);
                    buycarlist2.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });


            return convertView;
        }
    }
}
