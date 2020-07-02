package com.example.jack.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class buy extends AppCompatActivity {

    private DatabaseReference ref;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private BottomNavigationView bottomNavigationView;
    Fragment home_Fragment, car_fragment, live_fragment, notify_fragment, set_fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    Fragment active;
    private ArrayList<user> data = new ArrayList<>();
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy);

        database();

        home_Fragment = new buy_store();
        car_fragment = new buy_buycar();
        live_fragment = new buy_live();
        notify_fragment = new mail_fragment();
        set_fragment = new buy_yours();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

//        if (nickname == null) {
//            fragmentTransaction
//                    .add(R.id.frame_container, home_Fragment)
//                    .add(R.id.frame_container, car_fragment)
//                    .add(R.id.frame_container, live_fragment)
//                    .add(R.id.frame_container, notify_fragment)
//                    .add(R.id.frame_container, set_fragment)
//                    .hide(car_fragment)
//                    .hide(live_fragment)
//                    .hide(notify_fragment)
//                    .hide(home_Fragment)
//                    .addToBackStack(null)
//                    .commit();
//            //setactive();
//            active = set_fragment;
//            Intent intent = new Intent();
//            intent.setClass(buy.this, buy_yours_changedata.class);
//            startActivity(intent);
//        } else {
//            fragmentTransaction
//                    .add(R.id.frame_container, home_Fragment)
//                    .add(R.id.frame_container, car_fragment)
//                    .add(R.id.frame_container, live_fragment)
//                    .add(R.id.frame_container, notify_fragment)
//                    .add(R.id.frame_container, set_fragment)
//                    .hide(car_fragment)
//                    .hide(live_fragment)
//                    .hide(notify_fragment)
//                    .hide(set_fragment)
//                    .addToBackStack(null)
//                    .commit();
//            //setactive();
//            active = home_Fragment;
//        }

        fragmentTransaction
                .add(R.id.frame_container, home_Fragment)
                .add(R.id.frame_container, car_fragment)
                .add(R.id.frame_container, live_fragment)
                .add(R.id.frame_container, notify_fragment)
                .add(R.id.frame_container, set_fragment)
                .hide(car_fragment)
                .hide(live_fragment)
                .hide(notify_fragment)
                .hide(set_fragment)
                .addToBackStack(null)
                .commit();
        //setactive();
        active = home_Fragment;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.shop:
                        fragmentTransaction.hide(active)
                                .show(home_Fragment)
                                .addToBackStack(null)
                                .commit();
                        active = home_Fragment;
                        break;
                    case R.id.car:
                        fragmentTransaction.hide(active)
                                .show(car_fragment)
                                .addToBackStack(null)
                                .commit();
                        active = car_fragment;
                        break;
                    case R.id.live:
                        fragmentTransaction.hide(active)
                                .show(live_fragment)
                                .addToBackStack(null)
                                .commit();
                        active = live_fragment;
//                        Intent intent = new Intent();
//                        intent.setClass(buy.this, live_activity.class);
//                        startActivity(intent);
                        break;
                    case R.id.mail:
                        fragmentTransaction.hide(active)
                                .show(notify_fragment)
                                .addToBackStack(null)
                                .commit();
                        active = notify_fragment;
                        break;
                    case R.id.your:
                        fragmentTransaction.hide(active)
                                .show(set_fragment)
                                .addToBackStack(null)
                                .commit();
                        active = set_fragment;
                        break;
                }
                return true;
            }
        });


    }

    public void database() {
        ref = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user user;
                user = dataSnapshot.getValue(user.class);
                data.add(user);
                nickname = user.getNickname();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


//    public void setactive() {
//        Bundle bundle = getIntent().getExtras();
//        if (bundle == null) {
//            active = home_Fragment;
//            return;
//        }
//        String mfragment = bundle.getString("fragment");
//        switch (mfragment) {
//            case "home":
//                active = home_Fragment;
//                break;
//            case "buy_buycar":
//                active = car_fragment;
//                break;
//            case "notify":
//                active = notify_fragment;
//                break;
//            case "set":
//                active = set_fragment;
//                break;
//            default:
//                active = home_Fragment;
//        }
//    }
}




