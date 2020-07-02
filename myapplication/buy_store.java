package com.example.jack.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
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


public class buy_store extends Fragment {

    private TextView tx7;
    private TextView tx8;
    private TextView tx9;
    private TextView tx10;
    private TextView tx11;
    private TextView tx12;
    private TextView tx1;
    private TextView tx2;
    private TextView tx3;
    private TextView tx4;
    private TextView tx5;
    private TextView tx6;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private ArrayList img = new ArrayList();
    //    private newadapter adapter;
    private DatabaseReference ref;
    private ArrayList<item> data = new ArrayList<>();
    private ArrayList<item> hotdata = new ArrayList<>();
    private Toolbar toolbar;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    //private OnFragmentInteractionListener mListener;

    public buy_store() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = getActivity().findViewById(R.id.toolbar1);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("賣場");
        setHasOptionsMenu(true);

        getdata();


        String[] tx = {"hotRanking", "Random Item"};

//        adapter = new newadapter(getActivity());
//        this.setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.buy_store, container, false);

        setgrid(v);

        tx7 = (TextView) v.findViewById(R.id.textView57);
        tx8 = (TextView) v.findViewById(R.id.textView58);
        tx9 = (TextView) v.findViewById(R.id.textView59);
        tx10 = (TextView) v.findViewById(R.id.textView60);
        tx11 = (TextView) v.findViewById(R.id.textView61);
        tx12 = (TextView) v.findViewById(R.id.textView62);

        tx1 = v.findViewById(R.id.textView31);
        tx2 = v.findViewById(R.id.textView32);
        tx3 = v.findViewById(R.id.textView33);
        tx4 = v.findViewById(R.id.textView34);
        tx5 = v.findViewById(R.id.textView35);
        tx6 = v.findViewById(R.id.textView36);

        Button bt1 = (Button) v.findViewById(R.id.storebtn1);
        Button bt2 = (Button) v.findViewById(R.id.storebtn2);
        Button bt3 = (Button) v.findViewById(R.id.storebtn3);
        Button bt4 = (Button) v.findViewById(R.id.storebtn4);
        Button bt5 = (Button) v.findViewById(R.id.storebtn5);
        Button bt6 = (Button) v.findViewById(R.id.storebtn6);
        Button bt7 = (Button) v.findViewById(R.id.storebtn7);
        Button bt8 = (Button) v.findViewById(R.id.storebtn8);


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "隨機賣場");
                intent.putExtra("data", data);
                startActivity(intent);
            }//end onClick
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "隨機賣場");
                intent.putExtra("data", data);
                startActivity(intent);
            }//end onClick
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "隨機賣場");
                intent.putExtra("data", data);
                startActivity(intent);
            }//end onClick
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "隨機賣場");
                intent.putExtra("data", data);
                startActivity(intent);
            }//end onClick
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "隨機賣場");
                intent.putExtra("data", data);
                startActivity(intent);
            }//end onClick
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "隨機賣場");
                intent.putExtra("data", data);
                startActivity(intent);
            }//end onClick
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "隨機賣場");
                intent.putExtra("data", data);
                startActivity(intent);
            }//end onClick
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "隨機賣場");
                intent.putExtra("data", data);
                startActivity(intent);
            }//end onClick
        });
        //onCreate(savedInstanceState);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void getdata() {
        ref = FirebaseDatabase.getInstance().getReference("good");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int cnt = 1;
                int cnt2 = 0;
                item item;
                item hot;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds2 : ds.getChildren()) {
                        item = ds2.getValue(item.class);
                        hot = ds2.getValue(item.class);
                        data.add(item);
                        hotdata.add(hot);
                    }
                }

                //hot排列monthsales
                item temp;
                int size = hotdata.size();
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size - 1; j++) {
                        if (Integer.parseInt(hotdata.get(j).getMonthsales()) < Integer.parseInt(hotdata.get(j + 1).getMonthsales())) {
                            System.out.println("gotdata, " + hotdata.get(j));
                            temp = hotdata.get(j);
                            hotdata.set(j, hotdata.get(j + 1));
                            hotdata.set(j + 1, temp);
                        }
                    }
                }

                TextView[] txhotprice = {tx1, tx3, tx5};
                TextView[] txhotname = {tx2, tx4, tx6};

                TextView[] txrandomprice = {tx7, tx9, tx11};
                TextView[] txrandomname = {tx8, tx10, tx12};
                for (int i = 0; i < txrandomprice.length; i++) { //待改善
                    txrandomprice[i].setText(data.get(i).getPrice());
                    txrandomname[i].setText(data.get(i).getName());
                    txhotprice[i].setText(hotdata.get(i).getPrice());
                    txhotname[i].setText(hotdata.get(i).getName());
                }

                for (int i = 0; i < data.size(); i++) {
                    if (i < 3) {
                        download((ImageView) img.get(i), hotdata.get(i));
                    }
                    cnt2 = i + 3;
                    if (cnt2 < 6) {
                        download((ImageView) img.get(i + 3), data.get(i));
                    }
                }

//                data.notify();
//                data2.notify();
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void download(final ImageView im, final item itemimg) {
        StorageReference mStorageRef = storage.getReference();
        final StorageReference ref2 = mStorageRef;
        ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity())
                        .load(itemimg.getPic())
                        .into(im);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

//    Glide.with(getActivity())
//            .load(buycarlist.get(position).pic)
//            .into(holder.img1);
    //如果有放下載網址可用這版

    public void setgrid(View v) {

        Button more1 = v.findViewById(R.id.store_morebutton);
        Button more2 = v.findViewById(R.id.store_morebutton2);
        img1 = v.findViewById(R.id.sell_user_img);
        img2 = v.findViewById(R.id.imageView12);
        img3 = v.findViewById(R.id.imageView13);
        img4 = v.findViewById(R.id.imageView14);
        img5 = v.findViewById(R.id.imageView15);
        img6 = v.findViewById(R.id.imageView16);

        img.add(img1);
        img.add(img2);
        img.add(img3);
        img.add(img4);
        img.add(img5);
        img.add(img6);


        more1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "熱銷商品");
                intent.putExtra("data", hotdata);
                startActivity(intent);
            }//end onClick
        });
        more2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_hot.class);
                intent.putExtra("barname", "隨機賣場");
                intent.putExtra("data", data);
                startActivity(intent);
            }//end onClick
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_itemdetail.class);
                intent.putExtra("seller", hotdata.get(0).getSeller());
                intent.putExtra("item", tx2.getText());
                intent.putExtra("data", hotdata.get(0));
                startActivity(intent);
            }//end onClick
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_itemdetail.class);
                intent.putExtra("seller", hotdata.get(1).getSeller());
                intent.putExtra("item", tx4.getText());
                intent.putExtra("data", hotdata.get(1));
                startActivity(intent);
            }//end onClick
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_itemdetail.class);
                intent.putExtra("seller", hotdata.get(2).getSeller());
                intent.putExtra("item", tx6.getText());
                intent.putExtra("data", hotdata.get(2));
                startActivity(intent);
            }//end onClick
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_itemdetail.class);
                intent.putExtra("seller", data.get(0).getSeller());
                intent.putExtra("item", tx8.getText());
                intent.putExtra("data", data.get(0));
                startActivity(intent);
            }//end onClick
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_itemdetail.class);
                intent.putExtra("seller", data.get(1).getSeller());
                intent.putExtra("item", tx10.getText());
                intent.putExtra("data", data.get(1));
                startActivity(intent);
            }//end onClick
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), buy_store_itemdetail.class);
                intent.putExtra("seller", data.get(2).getSeller());
                intent.putExtra("item", tx12.getText());
                intent.putExtra("data", data.get(2));
                startActivity(intent);
            }//end onClick
        });

//        tx1.setText("商品價格：");
//        tx2.setText("Nike");
//        tx3.setText("商品價格：");
//        tx4.setText("book");
//        tx5.setText("商品價格：");
//        tx6.setText("apple");

//        tx7.setText("商品價格：");
//        tx8.setText("shoe");
//        tx9.setText("商品價格：");
//        tx10.setText("book");
//        tx11.setText("商品價格：");
//        tx12.setText("ball");

    }

//    static class ViewHolder {
//        TextView tx1;
//        TextView tx2;
//        TextView tx3;
//        TextView tx4;
//        TextView tx5;
//        TextView tx6;
//        TextView tx7;
//        Button bt;
//        ImageView imv1;
//        ImageView imv2;
//        ImageView imv3;
//    }

//    public class newadapter extends BaseAdapter {
//        private Context mContext;
//
//        public newadapter(Context c) {
//            mContext = c;
//        }
//
//        @Override
//        public int getCount() {
//            return data.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = LayoutInflater.from(mContext);
//            ViewHolder holder = null;
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.buy_store_item, null);
//                holder = new ViewHolder();
//                holder.imv1 = (ImageView) convertView.findViewById(R.id.imageView3);
//                holder.imv2 = (ImageView) convertView.findViewById(R.id.imageView4);
//                holder.imv3 = (ImageView) convertView.findViewById(R.id.imageView5);
//                holder.tx1 = (TextView) convertView.findViewById(R.id.textView);
//                holder.tx2 = (TextView) convertView.findViewById(R.id.textView13);
//                holder.tx3 = (TextView) convertView.findViewById(R.id.textView14);
//                holder.tx4 = (TextView) convertView.findViewById(R.id.textView15);
//                holder.tx5 = (TextView) convertView.findViewById(R.id.textView16);
//                holder.tx6 = (TextView) convertView.findViewById(R.id.textView17);
//                holder.tx7 = (TextView) convertView.findViewById(R.id.textView18);
//                holder.bt = (Button) convertView.findViewById(R.id.more);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            holder.tx2.setText("$" + data.get(position).getPrice());
//            holder.tx3.setText("$");
//            holder.tx4.setText("$");
//            holder.tx5.setText(data.get(position).getName());
//            holder.tx6.setText("");
//            holder.tx7.setText("");
//            holder.bt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), buy_store_hot.class);
//                    startActivity(intent);
//                }//end onClick
//            });
//            holder.imv1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), buy_store_itemdetail.class);
//                    startActivity(intent);
//                }//end onClick
//            });
//            holder.imv2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), buy_store_itemdetail.class);
//                    startActivity(intent);
//                }//end onClick
//            });
//            holder.imv3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), buy_store_itemdetail.class);
//                    startActivity(intent);
//                }//end onClick
//            });
//            return convertView;
//        }
//    }
}
