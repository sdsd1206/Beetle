package com.example.jack.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class mail_fragment extends Fragment {
    private MyAdapter adapter;
    private DatabaseReference ref;
    private DatabaseReference ref2;
    private DatabaseReference ref3;
    private String uid;
    private chatmsg cc2;
    private user u3;
    private String u;
    private String u2;
    private FirebaseUser user;
    private List<String> uu = new ArrayList<>();
    private List<user> uf = new ArrayList<>();
    private ArrayList<String> saveid = new ArrayList<>();
    private ArrayList<chatmsg> chat = new ArrayList<>();
    private ListView listView;

    public mail_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        listView = (ListView) getView().findViewById(R.id.list3);
        getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mail, container, false);
    }

    private void getUid() {
        adapter = new MyAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatroomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", chat.get(position).youid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (user != null) {
            ref = FirebaseDatabase.getInstance().getReference("chatmsg").child(uid + "");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chat.clear();
                    uu.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        for (DataSnapshot ds2 : ds.getChildren())
                            cc2 = ds2.getValue(chatmsg.class);
                        chat.add(cc2);
                        uu.add(cc2.youid);
                    }
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < uu.size(); i++) {
                        ref2 = FirebaseDatabase.getInstance().getReference("user").child(uu.get(i));
                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                u3 = dataSnapshot.getValue(user.class);
                                uf.add(u3);
                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }

    static class ViewHolder {
        public ImageView img1;
        public TextView tv1;
        public TextView tv2;
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
            return chat.size();
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
                convertView = mInflater.inflate(R.layout.mail_item, null);
                holder.img1 = (ImageView) convertView.findViewById(R.id.ppic);
                holder.tv1 = (TextView) convertView.findViewById(R.id.name);
                holder.tv2 = (TextView) convertView.findViewById(R.id.context);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(getActivity())
                    .load(uf.get(position).getFace())
                    .into(holder.img1);

            holder.tv1.setText(uf.get(position).getNickname());
            holder.tv2.setText(chat.get(position).getRoomcontext());


            return convertView;
        }
    }
}
