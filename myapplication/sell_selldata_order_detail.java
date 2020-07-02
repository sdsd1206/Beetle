package com.example.jack.myapplication;

import android.content.Context;
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
import android.widget.AdapterView;
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

public class sell_selldata_order_detail extends AppCompatActivity {
    ListView list;
    newadapter adapter;
    Button delbt;
    TextView transtx, cashtx, cashstate, transstate;
    Order2 orderdata;
    private ArrayList<Order2> data = new ArrayList();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_selldata_order_detail);

        list = (ListView) findViewById(R.id.orderdetaillist);
        adapter = new newadapter(sell_selldata_order_detail.this);
        list.setAdapter(adapter);

        Intent msg = this.getIntent();
        orderdata = (Order2) msg.getSerializableExtra("data");
        key = (String) msg.getStringExtra("key");

        myactionbar();

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(sell_selldata_order_detail.this, "Click item" + i, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(sell_selldata_order_detail.this, sell_selldata_order_itemdetail.class);
//                intent.putExtra("orderdata",orderdata);
//                startActivity(intent);
//                finish();
//            }
//        });


        //以下要改
        TextView total = findViewById(R.id.textView2);
        total.setText("total: $ " + Integer.parseInt(orderdata.getPrice()) * Integer.parseInt(orderdata.getQuantity()) + "");
        TextView tx1 = findViewById(R.id.textView9);
        TextView tx2 = findViewById(R.id.textView13);
        TextView tx3 = findViewById(R.id.textView14);
        cashstate = findViewById(R.id.textView15);
        transstate = findViewById(R.id.textView22);
        tx1.setText(orderdata.getBuyer());
        tx2.setText(orderdata.getAddress());
        tx3.setText(orderdata.getTime());
        cashstate.setText(orderdata.getState());
        transstate.setText(orderdata.getShipping());


        delbt = (Button) findViewById(R.id.button);
        delbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = sell_selldata_order_detail.this.getLayoutInflater();
                View dia = inflater.inflate(R.layout.sell_selldata_del_dialog, null);
                new AlertDialog.Builder(sell_selldata_order_detail.this)
                        .setView(dia)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (user == null) return;
                                String uid = user.getUid();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
                                DatabaseReference ref = database.getReference("sell_order_store");
                                ref.child(uid).child(key).removeValue();
                                Toast.makeText(sell_selldata_order_detail.this, "已刪除", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(sell_selldata_order_detail.this, sell_info.class);
//                                  intent.putExtra("num", ((TextView) getItem(R.id.text_seller)).getText());
                                startActivity(intent);
                                onResume();
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) { //一樣要修改成回上一頁
//                                Toast.makeText(order_detail.this, "已修改" , Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        })
                        .show();//呈現對話視窗
            }//end onClick
        });
        transtx = (TextView) findViewById(R.id.textView24);
        transtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = sell_selldata_order_detail.this.getLayoutInflater();
                View dia = inflater.inflate(R.layout.sell_selldata_del_dialog, null);
                new AlertDialog.Builder(sell_selldata_order_detail.this)
                        .setTitle("確定改為已出貨?")
                        .setView(dia)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                transstate.setText("已出貨");
                                changedatabase("shipping", transstate.getText() + "");
                                Toast.makeText(sell_selldata_order_detail.this, "已修改", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(order_detail.this, "已修改" , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();//呈現對話視窗

            }//end onClick
        });
        cashtx = (TextView) findViewById(R.id.textView23);
        cashtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = sell_selldata_order_detail.this.getLayoutInflater();
                View dia = inflater.inflate(R.layout.sell_selldata_del_dialog, null);
                new AlertDialog.Builder(sell_selldata_order_detail.this)
                        .setTitle("確定修改成已付款?")
                        .setView(dia)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cashstate.setText("已付款");
                                changedatabase("state", cashstate.getText() + "");
                                Toast.makeText(sell_selldata_order_detail.this, "已修改", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(order_detail.this, "已修改" , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();//呈現對話視窗

            }//end onClick
        });
    }

    public void changedatabase(String node, String changetx) {
        if (user == null) return;
        String uid = user.getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        DatabaseReference ref = database.getReference("sell_order_store");
        ref.child(uid).child(key).child(node).setValue(changetx);

    }

    public void myactionbar() //actionbar
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("一般賣場訂單資訊");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    static class ViewHolder {
        public TextView tx1;
        public TextView tx2;
        public TextView tx3;
        public ImageView img;
    }

    public class newadapter extends BaseAdapter {
        private Context mContext;

        public newadapter(Context mContext) {
            super();
            this.mContext = mContext;

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.sell_selldata_order_detail_item, null);
                holder = new ViewHolder();
                holder.tx1 = (TextView) convertView.findViewById(R.id.orderdetail_tx);
                holder.tx2 = (TextView) convertView.findViewById(R.id.orderdetail_tx2);
                holder.tx3 = (TextView) convertView.findViewById(R.id.orderdetail_tx3);
                holder.img = (ImageView) convertView.findViewById(R.id.orderdetail_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tx1.setText("商品名稱：　" + orderdata.getName());
            holder.tx2.setText("商品價格：　" + orderdata.getPrice());
            holder.tx3.setText("購買數量：　" + orderdata.getQuantity());
            Glide.with(getApplicationContext()) //圖片
                    .load(orderdata.getPic())
                    .into(holder.img);
            return convertView;
        }
    }
}
