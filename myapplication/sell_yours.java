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
import android.widget.ArrayAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class sell_yours extends Fragment {

    private String[] list;
    private ArrayList<Integer> data = new ArrayList<>();
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private ListView listView;
    TextView sellername;
    private ImageView pic;
    private String photo;
    private MyAdapter adapter;

    public sell_yours() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new MyAdapter(getContext());
        list = new String[listViewData.length / 2];
        for (int i = 0; i < list.length; i++) {
            list[i] = (String) listViewData[i * 2];
            data.add(img[i]);
        }

        listView = (ListView) getActivity().findViewById(R.id.list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                getActivity(), android.R.layout.simple_list_item_1, list
//        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    Uri uri = Uri.parse("https://member.gsscloud.com/cas/login?locale=zh-TW&service=https%3A%2F%2Fwww.gsscloud.com%2Ftw%2Fvital%2Fcrm%3Fserver%3D1");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                Intent intent = new Intent(getActivity(), (Class<?>) listViewData[position * 2 + 1]);
                startActivity(intent);
            }
        });

        pic = (ImageView) getView().findViewById(R.id.photo);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("user").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sellername = (TextView) getView().findViewById(R.id.name);
                String s = dataSnapshot.child("seller_nickname").getValue(String.class);
                sellername.setText("賣家名稱:" + s);
                photo = dataSnapshot.child("face").getValue(String.class);
                Glide.with(getActivity())
                        //.using(new FirebaseImageLoader())
                        .load(photo)
                        .into(pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sell_yours, container, false);
    }

    private Object[] listViewData = {
            "資料更改", sell_yours_datachange.class,
            "收據資料", sell_yours_receipt.class,
            "切換身分", sell_yours_changerole.class,
            "Vital CRM", ""
    };

    private int[] img = {
            R.drawable.datachange,
            R.drawable.receipt,
            R.drawable.changerole,
            R.drawable.y_report
    };

    static class ViewHolder {
        public ImageView imageView;
        public TextView tv1;
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
                convertView = mInflater.inflate(R.layout.buy_yours_item, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
                holder.tv1 = (TextView) convertView.findViewById(R.id.textView1);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imageView.setImageResource(img[position]);
            holder.tv1.setText(list[position]);
            return convertView;
        }
    }
}
