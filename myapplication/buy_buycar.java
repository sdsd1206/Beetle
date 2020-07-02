package com.example.jack.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class buy_buycar extends Fragment {
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private ListView data;
    private MyAdapter adapter;
    private Button ensure;
    private Toolbar toolbar;
    private Buycar buycar;
    private FirebaseUser user;
    private List<Buycar> buycarlist = new ArrayList<>();
    private ArrayList<Integer> productNo = new ArrayList<>();
    private ArrayList<String> productnum = new ArrayList<>();
    private String uid;

    public buy_buycar() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        toolbar = getView().findViewById(R.id.toolbar2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("購物車");
        setHasOptionsMenu(true);

        storage = FirebaseStorage.getInstance();
        data = (ListView) getActivity().findViewById(R.id.list);
        getData();

        ensure = (Button) getView().findViewById(R.id.button11);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), buy_buycar2.class);
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("productNo", productNo);
                bundle.putStringArrayList("productnum", productnum);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void setProductNo(int a) {
        if (productNo.isEmpty()) {
            for (int i = 0; i < a; i++) {
                productNo.add(0);
                productnum.add("1");
            }
        } else if (productnum.size() < a) {
            productNo.add(0);
            productnum.add("1");
        }

    }

    private void spichange(String s, int a) {
        productnum.add(a, s);
        productnum.remove(a + 1);
    }

    private void getchoicenum(Boolean b, int a) {
        if (b == true) {
            productNo.add(a, 1);
            productNo.remove(a + 1);
        }
    }

    private void getData() {
        adapter = new MyAdapter(getActivity());
        data.setAdapter(adapter);
        if (user != null) {

            ref = FirebaseDatabase.getInstance().getReference("buycar").child(uid + "");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    buycarlist.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        buycar = ds.getValue(Buycar.class);
                        buycarlist.add(buycar);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.buy_buycar, container, false);
    }

    static class ViewHolder {
        public ImageButton img1;
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public CheckBox cb1;
        public Spinner sp1;
        public Button b1;
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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if (convertView == null) {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.buy_buycar_item, null);
                holder.img1 = (ImageButton) convertView.findViewById(R.id.imageButton3);
                holder.tv1 = (TextView) convertView.findViewById(R.id.textView5);
                holder.tv2 = (TextView) convertView.findViewById(R.id.textView19);
                holder.tv3 = (TextView) convertView.findViewById(R.id.textView20);
                holder.cb1 = (CheckBox) convertView.findViewById(R.id.checkBox);
                holder.sp1 = (Spinner) convertView.findViewById(R.id.spinner);
                holder.b1 = (Button) convertView.findViewById(R.id.button17);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final int rep = position;
            setProductNo(adapter.getCount());
            ;
            holder.tv1.setText(buycarlist.get(position).name);
            holder.tv2.setText(buycarlist.get(position).price);
            holder.tv3.setText(buycarlist.get(position).sellername);
            String[] ss = new String[50];
            for (int i = 0; i < 50; i++) {
                ss[i] = (i + 1) + "";
            }
            ArrayAdapter<String> numList = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    ss);
            holder.sp1.setAdapter(numList);
            Glide.with(getActivity())
                    .load(buycarlist.get(position).pic)
                    .into(holder.img1);
            holder.b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ref = FirebaseDatabase.getInstance().getReference("buycar").child(uid + "");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                buycar = ds.getValue(Buycar.class);
                                if (buycar.getKey() == buycarlist.get(position).key) {
                                    ref.child(buycar.getKey()).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });
            final ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            holder.sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    spichange(finalHolder.sp1.getSelectedItem().toString(), rep);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            holder.cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    getchoicenum(isChecked, position);

                }


            });

            return convertView;
        }
    }
}
