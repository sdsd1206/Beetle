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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
public class sell_selldata_order_biddinglive extends Fragment {

    ListView list;
    private newadapter adapter;
    private DatabaseReference ref;
    private ArrayList<Order2> data = new ArrayList();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String[] key = new String[100];

    public sell_selldata_order_biddinglive() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.sell_selldata_order_biddinglive, container, false);
        list = (ListView) v.findViewById(R.id.list);
        adapter = new newadapter(getContext());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Click item" + i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), sell_selldata_order_detail.class);
                intent.putExtra("data",data.get(i));
                intent.putExtra("key",key[i]);
                startActivity(intent);
            }
        });
        getdata();
        return v;
    }

    public void getdata() {
        if(user==null) return;
        String uid = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference("sell_order_blive").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            int cnt = 0;
            Order2 order2 = new Order2();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

//                    for(DataSnapshot ds2 : ds.getChildren())
//                    {
                    key[cnt] = ds.getKey();
                    order2 = ds.getValue(Order2.class);
                    order2.setUid(ds.getKey());
                    data.add(order2);
                    cnt++;
//                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {
        TextView tx1;
        TextView tx2;
        TextView tx3;
        TextView tx4;
        TextView tx5;
        TextView tx6;
        TextView tx7;
        TextView tx8;
    }

    public class newadapter extends BaseAdapter {
        private Context mContext;
        private String[] s;

        public newadapter(Context mContext) {
            super();
            this.s = s;
            this.mContext = mContext;

        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.sell_selldata_order_orderitem, null);
                holder = new ViewHolder();
                holder.tx1 = (TextView) convertView.findViewById(R.id.tv1);
                holder.tx2 = (TextView) convertView.findViewById(R.id.tv2);
                holder.tx3 = (TextView) convertView.findViewById(R.id.tv3);
                holder.tx4 = (TextView) convertView.findViewById(R.id.tv4);
                holder.tx5 = (TextView) convertView.findViewById(R.id.tv5);
                holder.tx6 = (TextView) convertView.findViewById(R.id.tv6);
                holder.tx7 = (TextView) convertView.findViewById(R.id.tv7);
                holder.tx8 = (TextView) convertView.findViewById(R.id.tv8);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tx5.setText(data.get(position).getName());
            holder.tx6.setText(data.get(position).getBuyer());
            holder.tx7.setText(data.get(position).getAddress());
            holder.tx8.setText(data.get(position).getShipping());
//            holder.igb.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(),sell_selldata_good_additem.class);
//                    startActivity(intent);
//                }//end onClick
//            });
            return convertView;
        }
    }
}
