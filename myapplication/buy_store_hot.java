package com.example.jack.myapplication;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Random;

public class buy_store_hot extends AppCompatActivity {

    private DatabaseReference ref;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private listadapter adapterlist;
    private gridadapter adaptergrid;
    MyListView list;
    MyGridView grid;
    private ArrayList<item> data = new ArrayList<>();
    private ArrayList<item> data2 = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_store_hot);

        Intent title = this.getIntent();
        String barname = title.getStringExtra("barname");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(barname);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_store_hot.this, buy.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        getdata();
        list = (MyListView) findViewById(R.id.list);
        adapterlist = new listadapter(buy_store_hot.this);
        list.setAdapter(adapterlist);

        grid = (MyGridView) findViewById(R.id.grid);
        adaptergrid = new gridadapter(buy_store_hot.this);
        grid.setAdapter(adaptergrid);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_search, menu);

        MenuItem menuSearchItem = menu.findItem(R.id.action_search);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuSearchItem.getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // 這邊讓icon可以還原到搜尋的icon
        searchView.setIconifiedByDefault(true);
        return true;
    }

    public void getdata() {
//        ref = FirebaseDatabase.getInstance().getReference("good");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int cnt = 1;
//                item item;
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    for(DataSnapshot ds2 : ds.getChildren())
//                    {
//                        item = ds2.getValue(item.class);
//
//                        if (cnt <= 3) { //random就亂放
//                            data.add(item);
//                        } else {
//                            data2.add(item);
//                        }
//                        cnt++;
//                    }
//
//                }
//                adapterlist.notifyDataSetChanged();
//                adaptergrid.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        Intent msg = this.getIntent();
        ArrayList<item> arrraydata = (ArrayList<item>) msg.getSerializableExtra("data");
        for (int i = 0; i < arrraydata.size(); i++) {
            if (i < 3) {
                data.add(arrraydata.get(i));
            } else {
                data2.add(arrraydata.get(i));
            }
        }
    }

//    private void download(final ImageButton im){
//        StorageReference mStorageRef = storage.getReference();
//        final StorageReference ref2 = mStorageRef.child("productpic/apple.jpg");
//        ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(getApplicationContext())
//                        .using(new FirebaseImageLoader())
//                        .load(ref2)
//                        .into(im);
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//
//            }
//        });
//    }

    public class listadapter extends BaseAdapter {
        private Context context;

        public listadapter(Context context) {
            super();
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            Object item = (Object) findViewById(position);
            return item;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            // Context 動態放入mainActivity
            LayoutInflater inflater = LayoutInflater.from(context);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.buy_store_hot_item1, null);
                holder = new ViewHolder();
                holder.imb = (ImageButton) convertView.findViewById(R.id.hot_imgb);
                holder.num = (TextView) convertView.findViewById(R.id.hot_itemnum);
                holder.item = (TextView) convertView.findViewById(R.id.text_itemname);
                holder.price = (TextView) convertView.findViewById(R.id.text_price);
                holder.seller = (TextView) convertView.findViewById(R.id.text_seller);
                holder.sellnum = (TextView) convertView.findViewById(R.id.text_sellnum);
                holder.hot_lout = (LinearLayout) convertView.findViewById(R.id.hot_lout_v);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.num.setText(position + 1 + "");
            holder.item.setText("商品名稱:" + data.get(position).getName());
            holder.price.setText("價錢:$" + data.get(position).getPrice());
            holder.seller.setText("賣家名稱:" + data.get(position).getSeller());
            holder.sellnum.setText("月銷數量:" + data.get(position).getMonthsales());
            Glide.with(getApplicationContext()) //圖片
                    .load(data.get(position).getPic())
                    .into(holder.imb);
            holder.imb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(buy_store_hot.this, buy_store_itemdetail.class);
                    /*Object seller = (TextView) getItem(R.id.text_seller);
                    ((TextView) seller).getText();*/
                    intent.putExtra("seller", data.get(position).getSeller());
                    intent.putExtra("item", data.get(position).getName());
                    intent.putExtra("data", data.get(position));
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    public static class ViewHolder {
        TableRow hot_tr;
        ImageButton imb;
        TextView num;
        TextView item;
        TextView price;
        TextView seller;
        TextView sellnum;
        LinearLayout hot_lout;

        TableLayout tl;
        TableRow hot_g_tr;
        Space s1;

    }

    public class gridadapter extends BaseAdapter {

        private Context context1;

        public gridadapter(Context context1) {
            super();
            this.context1 = context1;
        }

        @Override
        public int getCount() {
            return data2.size();
        }

        @Override
        public Object getItem(int position) {
            Object item = (Object) findViewById(position);
            return item;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder1 = null;
            LayoutInflater inflater = LayoutInflater.from(context1);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.buy_store_hot_gridlist, null);
                holder1 = new ViewHolder();
                holder1.imb = (ImageButton) convertView.findViewById(R.id.hot_grid_imgb);
                holder1.num = (TextView) convertView.findViewById(R.id.hot_grid_num);
                holder1.item = (TextView) convertView.findViewById(R.id.grid_text_itemname);
                holder1.price = (TextView) convertView.findViewById(R.id.grid_text_price);
                holder1.seller = (TextView) convertView.findViewById(R.id.grid_text_seller);
                holder1.sellnum = (TextView) convertView.findViewById(R.id.grid_text_sellnum);
                holder1.hot_lout = (LinearLayout) convertView.findViewById(R.id.grid_lout_v);
                holder1.s1 = (Space) convertView.findViewById(R.id.space_1);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder) convertView.getTag();
            }
            holder1.num.setText((position + 4) + "");
            holder1.item.setText("商品名稱:" + data2.get(position).getName());
            holder1.price.setText("價錢:$" + data2.get(position).getPrice());
            holder1.seller.setText("賣家名稱:" + data2.get(position).getSeller());
            holder1.sellnum.setText("月銷數量:" + data2.get(position).getMonthsales());
            Glide.with(getApplicationContext()) //圖片
                    .load(data2.get(position).getPic())
                    .into(holder1.imb);
            holder1.imb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(buy_store_hot.this, buy_store_itemdetail.class);
                    intent.putExtra("seller", data2.get(position).getSeller());
                    intent.putExtra("item", data2.get(position).getName());
                    intent.putExtra("data", data2.get(position));
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}


