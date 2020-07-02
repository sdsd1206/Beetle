package com.example.jack.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class sell_selldata_good extends Fragment {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //在這裡創user user 傳值給additem
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference ref;
    private newadapter adapter;
    private GridView grid;
    private ArrayList<item> data = new ArrayList();
    String tempmail;
    TextView sellername;
    ImageView sellerface;

    public sell_selldata_good() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.sell_selldata_good, container, false);
        grid = (GridView) v.findViewById(R.id.grid);
        adapter = new newadapter(getContext());
        grid.setAdapter(adapter);

        sellername = (TextView) v.findViewById(R.id.sell_user_tx);
        sellerface = (ImageView) v.findViewById(R.id.sell_user_img);
        getuser();
        getdata();
        Button add = (Button) v.findViewById(R.id.additembutton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), sell_selldata_good_additem.class);
                intent.putExtra("seller", sellername.getText());
                startActivity(intent);
            }//end onClick
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        DatabaseReference Ref = database.getReference("user");

        return v;
    }

    private void download(final ImageView im, final String pic) {
        StorageReference mStorageRef = storage.getReference();
        final StorageReference ref2 = mStorageRef;
        ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity())
                        .load(pic)
                        .into(im);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }


    public void getdata() {
        ref = FirebaseDatabase.getInstance().getReference("good").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            item item;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    item = ds.getValue(item.class);
                    data.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getuser() {
        if (user == null) {return;}
        sellername.setText(user.getDisplayName());
        ref = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            user databaseuser;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseuser = (user) dataSnapshot.getValue(user.class);
                download(sellerface,databaseuser.getFace());//沒辦法....

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {
        public ImageButton igb;
        public Button bt;
    }

    public class newadapter extends BaseAdapter {
        private Context mContext;

        public newadapter(Context mContext) {
            super();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.sell_selldata_good_item, null);
                holder.bt = (Button) convertView.findViewById(R.id.button26);
                holder.igb = (ImageButton) convertView.findViewById(R.id.imageButton);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(getActivity()) //圖片
                    .load(data.get(position).getPic())
                    .into(holder.igb);
            holder.igb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), sell_selldata_order_itemdetail.class);
                    intent.putExtra("seller", data.get(position).getSeller());
                    intent.putExtra("item", data.get(position).getName());
                    intent.putExtra("data", data.get(position));
                    startActivity(intent);
                }//end onClick
            });

            holder.bt.setOnClickListener(new View.OnClickListener() {
                View dia = inflater.inflate(R.layout.sell_selldata_del_dialog, null);

                @Override
                public void onClick(View view) {
                    //產生視窗物件
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setView(dia)
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    int cnt = position;
//                                    int size = data.size();
//                                    int start = 0;
//                                    ArrayList<item> a = new ArrayList<>();
//                                    for (int i = cnt + 1; i < size; i++) {
//                                        a.add((item) data.get(i));
//                                        data.remove(i);
//                                    }
//                                    for (int i = 0; i < a.size(); i++) {
//                                        data.add(a.get(i));
//                                    }

                                    ref = FirebaseDatabase.getInstance().getReference("good").child(user.getUid());
                                    ref.child(data.get(position).getKey()).removeValue();
                                    //data.remove(position);
                                    Toast.makeText(getContext(), "已刪除", Toast.LENGTH_SHORT).show();
                                    onResume();
                                    //想辦法刪adapter
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(order_detail.this, "已修改" , Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }//end onClick
            });
            return convertView;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
}
