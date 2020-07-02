package com.example.jack.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class buy_live_normal_live extends AppCompatActivity {

    private String count;
    private DatabaseReference ref;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private String suid;
    private ArrayList<Chat> mchat = new ArrayList<>();
    private ArrayList<user> muser = new ArrayList<>();
    private ArrayList<user> su = new ArrayList<>();
    private ArrayList<address> maddress = new ArrayList<>();
    private ArrayList<live_room> rooms = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> gData = new ArrayList<>();
    private ArrayList<ArrayList<order>> iData = new ArrayList<>();
    private ArrayList<order> lData = new ArrayList<>();
    private MyBaseExpandableListAdapter adapter;
    private ExpandableListView exlist;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private CountDownTimer mCountDownTimer;
    private EditText edittext;
    private TextView tf_second, textview1, shop;
    private Button check, money;
    private ImageView shopimg;
    String tmp; // 暫存文字訊息
    Socket clientSocket;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    View item;
    public static Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_live_normal_live);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getintent();
        sell();

        ref = FirebaseDatabase.getInstance().getReference("order_nlive").child(suid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    title.add(ds.getKey());
                }
                if (title.size() == 0) {
                    getSupportActionBar().setTitle("請輸入商品名稱");
                } else {
                    String s = title.get(title.size() - 1);
                    String[] ss = s.split(":");
                    getSupportActionBar().setTitle(ss[0] + "/NT$" + ss[1]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getdata();
        exlist = (ExpandableListView) findViewById(R.id.exlist);
        adapter = new MyBaseExpandableListAdapter(this);
        exlist.setAdapter(adapter);
        exlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        exlist.setGroupIndicator(null);
//        int groupCount = exlist.getCount();
//        for (int i = 0; i < groupCount; i++) {
//            exlist.expandGroup(i);
//        }

        if (Vitamio.isInitialized(this)) {
            VideoView vv = (VideoView) findViewById(R.id.vv);
            // rtmp://192.168.3.101:2000/live
            // rtmp://59.115.167.88:2000/live
            vv.setVideoURI(Uri.parse("rtmp://**.***.***.**:2000/live"));
//預設的controller
            MediaController controller = new MediaController(this);
            vv.setMediaController(controller);
            vv.start();
//緩衝監聽
            vv.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
//percent 當前緩衝百分比
                }
            });
            vv.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
//開始緩衝
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                            percentTv.setVisibility(View.VISIBLE);
//                            netSpeedTv.setVisibility(View.VISIBLE);
                            mp.pause();
                            return true;
//緩衝結束
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                            percentTv.setVisibility(View.GONE);
//                            netSpeedTv.setVisibility(View.GONE);
                            mp.start();
                            return true;
                    }
                    return false;
                }
            });
        }

        init();
        database();
        chat();

    }

    public void init() {
        edittext = (EditText) findViewById(R.id.editText2);
        textview1 = (TextView) findViewById(R.id.textView6);
        textview1.setMovementMethod(ScrollingMovementMethod.getInstance());
        shop = (TextView) findViewById(R.id.shop);
        shopimg = (ImageView) findViewById(R.id.shopimg);
        check = (Button) findViewById(R.id.button2);
        check.setOnClickListener(checklistener);
        money = (Button) findViewById(R.id.money);
        money.setOnClickListener(moneylistener);
    }

    private View.OnClickListener checklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ref = FirebaseDatabase.getInstance().getReference("chat_normal").child(suid);
            ref.push().setValue(new Chat(muser.get(0).getNickname(), edittext.getText().toString()));
            edittext.setText("");
        }
    };

    private View.OnClickListener moneylistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            item = LayoutInflater.from(buy_live_normal_live.this).inflate(R.layout.dialog_money, null);
            new AlertDialog.Builder(buy_live_normal_live.this)
                    .setTitle("請輸入下標數量")
                    .setView(item)
                    .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText = (EditText) item.findViewById(R.id.count);
                            count = editText.getText().toString();
                            check();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    };

    public void check() {
        final String s = getSupportActionBar().getTitle().toString();
        final String[] ss = s.split("/");
        final String[] ps = ss[1].split("\\$");
        final String price = "" + Integer.parseInt(ps[1]) * Integer.parseInt(count);
        AlertDialog.Builder dialog = new AlertDialog.Builder(buy_live_normal_live.this);
        dialog.setTitle("訂單編號：");
        dialog.setMessage("賣家名稱:" + su.get(0).getName() + "\n"
                + "商品名稱:" + ss[0] + "\n"
                + "總價:NT$" + price + "\n"
                + "數量:" + count);
        dialog.setCancelable(false);  // disable click back button
        dialog.setPositiveButton("訂單送出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                order order = new order(ss[0], count, price);
                lData.add(order);
                iData.add(lData);
                adapter.notifyDataSetChanged();
                exlist.collapseGroup(0);
                exlist.expandGroup(0);

                lData = new ArrayList<>();

                //                取買家個人資料
                ref = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final user user;
                        user = dataSnapshot.getValue(user.class);
                        muser.add(user);

                        //      將訂單寫入order_nlive給賣家
                        ref = FirebaseDatabase.getInstance().getReference("order_nlive").child(suid).child(ss[0] + ":" + ps[1]);
                        ref.child(fuser.getUid()).setValue(new order(user.getName(), count, price));

                        //      取買家地址
                        ref = FirebaseDatabase.getInstance().getReference("address").child(fuser.getUid()).child("homeaddress");
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                address address;
                                address = dataSnapshot.getValue(address.class);
                                maddress.add(address);

//                         將訂單寫入sell_order_nlive給賣家
                                sorder sorder = new sorder(address.getAddress(), user.getMail(),
                                        ss[0], "jpg", price, count, "待出貨", "未付款", "2019");
                                ref = FirebaseDatabase.getInstance().getReference("sell_order_nlive").child(suid);
                                ref.push().setValue(sorder);

//                          將訂單寫入buy_order_waiting給買家本人
                                border border = new border(address.getAddress(), ss[0],
                                        "jpg", price, count, su.get(0).getName(), "2019");
                                ref = FirebaseDatabase.getInstance().getReference("buy_order_waiting").child(fuser.getUid());
                                ref.push().setValue(border);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                ok();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    public void ok() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(buy_live_normal_live.this);
        dialog.setTitle("交易成功!");
        dialog.setCancelable(false);  // disable click back button
        dialog.setPositiveButton("好耶!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    public void getdata() {
        gData.add("訂單");
        lData.add(new order("商品名稱", "數量", "小計"));
        iData.add(lData);
    }

    //    取得賣家UID
    public void getintent() {
        Intent intent = getIntent();
        if (intent != null) {
            suid = intent.getStringExtra("uid");
        }
    }

    public void database() {
        //                取得取買家個人資料
        ref = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user user;
                user = dataSnapshot.getValue(user.class);
                muser.add(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //                取得賣家個人資料
        ref = FirebaseDatabase.getInstance().getReference("user").child(suid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user user;
                user = dataSnapshot.getValue(user.class);
                su.add(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sell() {

        ref = FirebaseDatabase.getInstance().getReference("nlive_room").child(suid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                live_room room;
                room = dataSnapshot.getValue(live_room.class);
                rooms.add(room);
                shop.setText(rooms.get(0).getRoom_name());
                Glide.with(buy_live_normal_live.this)
                        .load(rooms.get(0).getRoom_pic())
                        .into(shopimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void chat() {
        //                取得聊天紀錄
        ref = FirebaseDatabase.getInstance().getReference("chat_normal").child(suid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textview1.setText("");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat;
                    chat = ds.getValue(Chat.class);
                    mchat.add(chat);
                    textview1.append(chat.getName() + ":" + chat.getMsg() + "\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolderGroup {
        private TextView tv_group_name;
    }

    static class ViewHolderItem {
        private TextView tv_name;
        private TextView tv_count;
        private TextView tv_price;
    }

    public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

        private Context mContext;

        public MyBaseExpandableListAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getGroupCount() {
            return gData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return iData.get(groupPosition).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return gData.get(groupPosition);
        }

        @Override
        public order getChild(int groupPosition, int childPosition) {
            return iData.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            ViewHolderGroup groupHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.buy_live_bidding_live_group, parent, false);
                groupHolder = new ViewHolderGroup();
                groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.textView1);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (ViewHolderGroup) convertView.getTag();
            }
            groupHolder.tv_group_name.setText(gData.get(groupPosition).toString());
            return convertView;
        }

        //取得显示给定分组给定子位置的数据用的视图
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolderItem itemHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.buy_live_bidding_live_item, parent, false);
                itemHolder = new ViewHolderItem();
//                itemHolder.img_icon = (TextView) convertView.findViewById(R.id.img_icon);
                itemHolder.tv_name = (TextView) convertView.findViewById(R.id.textView1);
                itemHolder.tv_count = (TextView) convertView.findViewById(R.id.textView2);
                itemHolder.tv_price = (TextView) convertView.findViewById(R.id.textView3);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ViewHolderItem) convertView.getTag();
            }
//            itemHolder.img_icon.setImageResource(iData.get(groupPosition).get(childPosition).getiId());
            itemHolder.tv_name.setText(iData.get(groupPosition).get(childPosition).getName());
            itemHolder.tv_count.setText(iData.get(groupPosition).get(childPosition).getCount());
            itemHolder.tv_price.setText(iData.get(groupPosition).get(childPosition).getTotal());
            return convertView;
        }

        //设置子列表是否可选中
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
}

