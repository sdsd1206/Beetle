package com.example.jack.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class sell_live extends Fragment {

    private DatabaseReference ref;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    public static final String RTMPURL_MESSAGE = "rtmppush.hx.com.rtmppush.rtmpurl";
    private Button normal, bidding;
    private String rtmpUrl = "rtmp://36.230.155.25:2000/live";

    public sell_live() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        normal = (Button) getView().findViewById(R.id.button2);
        bidding = (Button) getView().findViewById(R.id.button);

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Normal();
                Intent intent = new Intent();
                intent.setClass(getActivity(), sell_live_normal.class);
                intent.putExtra(StartActivity.RTMPURL_MESSAGE, rtmpUrl);
                startActivity(intent);
            }
        });

        bidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bidding();
                Intent intent = new Intent();
                intent.setClass(getActivity(), sell_live_bidding.class);
                intent.putExtra(StartActivity.RTMPURL_MESSAGE, rtmpUrl);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sell_live, container, false);
    }

    public void Bidding() {
        //        將資料寫入live_room
        ref = FirebaseDatabase.getInstance().getReference("live_room").child(fuser.getUid());
        ref.child("room_name").setValue("Jack的家");
        ref.child("tag").setValue("3C");
        ref.child("uid").setValue(fuser.getUid());
    }

    public void Normal() {
        //        將資料寫入live_room
        ref = FirebaseDatabase.getInstance().getReference("nlive_room").child(fuser.getUid());
        ref.child("room_name").setValue("Tim的家");
        ref.child("tag").setValue("配件");
        ref.child("uid").setValue(fuser.getUid());
    }
}

