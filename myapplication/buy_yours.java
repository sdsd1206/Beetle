package com.example.jack.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.data.model.User;
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
public class buy_yours extends Fragment {

    private String[] list;
    private ArrayList<Integer> data = new ArrayList<>();
    private TextView username;
    private TextView point;
    private ImageView pic;
    private String photo;
    DatabaseReference ref;
    private ListView listView;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private MyAdapter adapter;

    public buy_yours() {

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new MyAdapter(getContext());
        list = new String[listViewData.length / 2];
        for (int i = 0; i < list.length; i++) {
            list[i] = (String) listViewData[i * 2];
            data.add(img[i]);
        }
        // (3)建立 ArrayAdapter 將剛剛處理好的 list 資料放入
        // android.R.layout.simple_list_item_1 為選擇的樣式"此樣式為android提供的樣式"
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list
////        );
        // (4)將顯示畫面上的 list 與 Activity 串聯
        //list1=(ListView)view.findViewById(R.id.list);

        // ListView 的使用需要搭配 Adapter , Adapter 是用來連接資料和 ListView 的,
        // Adapter 除了會用到 ListView 中,會另外用到還有 Spinner(下拉式選單)的這個元件
        // (5)透過 ListView 的 setAdapter() 方法,將串聯資料的 Adapter 與 ListView 串聯起來

        listView = (ListView) getView().findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), (Class<?>) listViewData[position * 2 + 1]);
                startActivity(intent);
            }
        });

        Button returnbtn = (Button) getView().findViewById(R.id.returnbtn);
        Button waitbtn = (Button) getView().findViewById(R.id.waitgdbtn);
        Button conbtn = (Button) getView().findViewById(R.id.checkgdbtn);
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), buy_yours_returngoods.class));
            }
        });
        waitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), buy_yours_waitgoods.class));
            }
        });
        conbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), buy_yours_confirmgoods.class));
            }
        });

        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.buy_yours, container, false);
        // Required empty public constructor
        // (2)list顯示的文字部分處理"偶數部分為顯示文字"放到CharSequence[] 中


        // (6)實作點選清單後轉換到各自的Activity範例畫面
        // ListView 上設定 OnItemClickListener
        // 按下選單名稱指向相關的應用程式 Class
        /*list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getActivity(),(Class<?>)listViewData[position*2+1]);
                startActivity(intent);
            }
        });*/

        pic = (ImageView) view.findViewById(R.id.photo);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("user").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = (TextView) view.findViewById(R.id.username);
                point = (TextView) view.findViewById(R.id.creditpt);
                String newname = dataSnapshot.child("nickname").getValue(String.class);
                String cpoint = dataSnapshot.child("points").getValue(String.class);
                username.setText("暱稱:" + newname);
                point.setText("信用積分:" + cpoint);
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

        return view;
    }

    private void getData() {
        username = getActivity().findViewById(R.id.name);
        ref = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child("nickname").getValue(String.class);
                username.setText("暱稱:" + n);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Object[] listViewData = {
            "更改資料", buy_yours_changedata.class,
            "付款資料", buy_yours_paydata.class,
            "地址資料", buy_yours_addressdata.class,
            "切換身分", buy_yours_changerole.class,
            "聯絡客服", buy_yours_report.class
    };

    private int[] img = {
            R.drawable.datachange,
            R.drawable.paydata,
            R.drawable.addressdata,
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
