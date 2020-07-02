package com.example.jack.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

public class buy_live_bidding_live extends AppCompatActivity {

    private int cnt = 0;
    private String sec = "未設定";
    private int max = 0;
    private String count = "0";
    private DatabaseReference ref;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private String suid;
    private ArrayList<Chat> mchat = new ArrayList<>();
    private ArrayList<user> su = new ArrayList<>();
    private ArrayList<live_room> rooms = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<user> muser = new ArrayList<>();
    private ArrayList<address> maddress = new ArrayList<>();
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
    private TextView tf_second, textview1, textview2, textview3, shop;
    private Button check, money;
    private ImageView shopimg, prove;

    View item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_live_bidding_live);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Getintent();
        Sell();

        ref = FirebaseDatabase.getInstance().getReference("order_blive").child(suid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    title.add(ds.getKey());
                }
                if (title.size() == 0) {
                    getSupportActionBar().setTitle("請輸入商品名稱");
                } else {
                    getSupportActionBar().setTitle(title.get(title.size() - 1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Database();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Getdata();

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

        Vitamio();
        Init();
        Chat();
        Money();
        Getclock();
    }

    //    Vitamio撥放器初始化
    public void Vitamio() {
        if (Vitamio.isInitialized(this)) {
            VideoView vv = (VideoView) findViewById(R.id.vv);
            // rtmp://***.***.***.***:2000/live
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
    }

    //    宣告
    public void Init() {
        edittext = (EditText) findViewById(R.id.editText2);
        textview1 = (TextView) findViewById(R.id.textView6);
        textview1.setMovementMethod(ScrollingMovementMethod.getInstance());
        textview2 = (TextView) findViewById(R.id.textView7);
        textview2.setMovementMethod(ScrollingMovementMethod.getInstance());
        textview3 = (TextView) findViewById(R.id.top);
        shop = (TextView) findViewById(R.id.shop);
        shopimg = (ImageView) findViewById(R.id.shopimg);
        prove = (ImageView) findViewById(R.id.prove);
        check = (Button) findViewById(R.id.button2);
        check.setOnClickListener(checklistener);
        money = (Button) findViewById(R.id.money);
        money.setOnClickListener(moneylistener);
        tf_second = (TextView) findViewById(R.id.tf_second);
    }

    //    送出聊天訊息
    private View.OnClickListener checklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ref = FirebaseDatabase.getInstance().getReference("chat").child(suid);
            ref.push().setValue(new Chat(muser.get(0).getNickname(), edittext.getText().toString()));
            edittext.setText("");
        }
    };

    //    競標鍵
    private View.OnClickListener moneylistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            item = LayoutInflater.from(buy_live_bidding_live.this).inflate(R.layout.dialog_money, null);
            new AlertDialog.Builder(buy_live_bidding_live.this)
                    .setTitle("出標金額:")
                    .setView(item)
                    .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ref = FirebaseDatabase.getInstance().getReference("money").child(suid);
                            EditText editText = (EditText) item.findViewById(R.id.count);
                            editText.setHint("請輸入出標金額");
                            count = editText.getText().toString();
                            ref.push().setValue(new Chat(muser.get(0).getNickname(), count));
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

    //    訂單確認
    public void Check() {
        if (cnt == 0) {
            final String mtitle = title.get(title.size() - 1);
            final String price = "" + max;
            AlertDialog.Builder dialog = new AlertDialog.Builder(buy_live_bidding_live.this);
            dialog.setTitle("訂單編號：");
            dialog.setMessage("賣家名稱:" + su.get(0).getName() + "\n"
                    + "商品名稱:" + mtitle + "\n"
                    + "總價:" + "NT$" + max + "\n"
                    + "數量:" + "1");
            dialog.setCancelable(false);  // disable click back button
            dialog.setPositiveButton("訂單送出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lData.add(new order(mtitle, "1", price));
                    iData.add(lData);
                    adapter.notifyDataSetChanged();
                    exlist.collapseGroup(0);
                    exlist.expandGroup(0);

//                初始化lData
                    lData = new ArrayList<>();
                    lData.add(new order("商品名稱", "數量", "價錢"));

//                取買家個人資料
                    ref = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final user user;
                            user = dataSnapshot.getValue(user.class);

                            //      將訂單寫入order_blive給賣家
                            ref = FirebaseDatabase.getInstance().getReference("order_blive").child(suid).child(mtitle);
                            ref.child(fuser.getUid()).setValue(new order(user.getName(), "1", price));

                            //      取買家地址
                            ref = FirebaseDatabase.getInstance().getReference("address").child(fuser.getUid()).child("homeaddress");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    address address;
                                    address = dataSnapshot.getValue(address.class);
                                    maddress.add(address);

//                         將訂單寫入sell_order_blive給賣家
                                    sorder sorder = new sorder(address.getAddress(), user.getMail(),
                                            mtitle, "jpg", price, "1", "待出貨", "未付款", "2019/8/23");
                                    ref = FirebaseDatabase.getInstance().getReference("sell_order_blive").child(suid);
                                    ref.push().setValue(sorder);

//                          將訂單寫入buy_order_watting給買家本人
                                    border border = new border(address.getAddress(), mtitle,
                                            "jpg", price, "1", su.get(0).getName(), "2019/8/23");
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

                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }
    }

    //    初始化展開式清單
    public void Getdata() {
        gData.add("訂單");
        lData.add(new order("商品名稱", "數量", "價錢"));
        iData.add(lData);
    }

    //    取得賣家UID
    public void Getintent() {
        Intent intent = getIntent();
        if (intent != null) {
            suid = intent.getStringExtra("uid");
        }
    }

    //    取得倒數時間
    public void Getclock() {
        cnt = 0;
        ref = FirebaseDatabase.getInstance().getReference("clock").child(suid).child("sec");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sec = dataSnapshot.getValue(String.class);
                if (sec.equals("未設定")) {
                    return;
                }

                if (cnt == 0) {
                    int s = Integer.parseInt(sec);
                    mCountDownTimer = new CountDownTimer(s * 1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            tf_second.setText("" + (millisUntilFinished / 1000));
                        }

                        public void onFinish() {
                            tf_second.setText("時間到囉!");

                            if (tf_second.getText().equals("時間到囉!")) {
                                String[] who = textview3.getText().toString().split(":");
                                if (who[0].equals(muser.get(0).getNickname())) {
                                    Check();
                                    cnt++;
                                }
                            }
                        }
                    }.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //    資料庫查詢
    public void Database() {
        //                取買家個人資料
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

        //                取賣家個人資料
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

    //    取得房間資訊
    public void Sell() {
        ref = FirebaseDatabase.getInstance().getReference("live_room").child(suid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                live_room room;
                room = dataSnapshot.getValue(live_room.class);
                rooms.add(room);
                shop.setText(rooms.get(0).getRoom_name());
                Glide.with(buy_live_bidding_live.this)
                        .load(rooms.get(0).getRoom_pic())
                        .into(shopimg);
                Glide.with(buy_live_bidding_live.this)
                        .load(rooms.get(0).getProve())
                        .into(prove);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //    取得聊天紀錄
    public void Chat() {
        ref = FirebaseDatabase.getInstance().getReference("chat").child(suid);
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

    //    取得競標金額紀錄
    //    更新最高價者
    public void Money() {
        ref = FirebaseDatabase.getInstance().getReference("money").child(suid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textview2.setText("");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat;
                    chat = ds.getValue(Chat.class);
                    if (chat != null) {
                        if (chat.getName().equals("-1")) {
                            textview2.append(chat.getMsg() + "\n");
                            continue;
                        }
                        int p = Integer.parseInt(chat.getMsg());
                        if (chat.getName().equals(suid)) {
//                            賣家設定起標價
                            max = p;
                            textview3.setText("起標價-->" + max);
                        } else {
//                            是否更新最高價者
                            if (max < p) {
                                max = p;
                                textview3.setText("" + chat.getName() + ":" + max);
                            }
//                            將競標價寫入金額區
                            textview2.append(chat.getName() + ":" + chat.getMsg() + "\n");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //    主列表項目
    static class ViewHolderGroup {
        private TextView tv_group_name;
    }

    //    子列表項目
    static class ViewHolderItem {
        private TextView tv_name;
        private TextView tv_count;
        private TextView tv_price;
    }

    //    展開式清單
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

        //        取得主列表
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

        //        取得子列表
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolderItem itemHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.buy_live_bidding_live_item, parent, false);
                itemHolder = new ViewHolderItem();
                itemHolder.tv_name = (TextView) convertView.findViewById(R.id.textView1);
                itemHolder.tv_count = (TextView) convertView.findViewById(R.id.textView2);
                itemHolder.tv_price = (TextView) convertView.findViewById(R.id.textView3);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ViewHolderItem) convertView.getTag();
            }
            itemHolder.tv_name.setText(iData.get(groupPosition).get(childPosition).getName());
            itemHolder.tv_count.setText(iData.get(groupPosition).get(childPosition).getCount());
            itemHolder.tv_price.setText(iData.get(groupPosition).get(childPosition).getTotal());
            return convertView;
        }

        //設置子列表是否可選中
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
}
