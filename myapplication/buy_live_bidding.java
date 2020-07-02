package com.example.jack.myapplication;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class buy_live_bidding extends Fragment {

    private store store;
    private DatabaseReference ref;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private MyAdapter adapter;
    private ArrayList<live_room> data = new ArrayList<>();
    private ArrayList<String> no = new ArrayList<>();
    private ListView list;

    public buy_live_bidding() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getData();
        list = getView().findViewById(R.id.list);
        adapter = new MyAdapter(getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), buy_live_bidding_live.class);
                intent.putExtra("uid", no.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.buy_live_bidding, container, false);
    }

    private void getData() {
        ref = FirebaseDatabase.getInstance().getReference("live_room");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    live_room room;
                    room = ds.getValue(live_room.class);
                    no.add(room.getUid());
                    data.add(room);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {
        public ImageView img1;
        public ImageView img2;
        public TextView s_name;
        private TextView tag;
        //        public Button follow;
        public Button report;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        private MyAdapter(Context context) {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            //How many items are in the data set represented by this Adapter.
            //在此适配器中所代表的数据集中的条目数
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return data.get(position);
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
                convertView = mInflater.inflate(R.layout.buy_live_item, null);
                holder.img1 = (ImageView) convertView.findViewById(R.id.imageView3);
                holder.img2 = (ImageView) convertView.findViewById(R.id.imageView2);
                holder.s_name = (TextView) convertView.findViewById(R.id.textView2);
                holder.tag = (TextView) convertView.findViewById(R.id.tag);
//                holder.follow = (Button) convertView.findViewById(R.id.button9);
                holder.report = (Button) convertView.findViewById(R.id.button10);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(getActivity())
                    .load(data.get(position).getRoom_pic())
                    .into(holder.img2);

            Glide.with(getActivity())
                    .load(data.get(position).getProve())
                    .into(holder.img1);
            // holder.img1.setImageResource((Integer)data.get(position).get("img1"));
            // holder.img2.setImageResource((Integer)data.get(position).get("img2"));
            holder.s_name.setText((String) data.get(position).getRoom_name());
            holder.tag.setText("類型:" + (String) data.get(position).getTag());
//            holder.follow.setText("追隨");
//            holder.follow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
            holder.report.setText("");
            holder.report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return convertView;
        }
    }
}
