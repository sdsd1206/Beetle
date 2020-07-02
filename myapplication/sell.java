package com.example.jack.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class sell extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment sell_selldata, sell_live, sell_mail, sell_yours;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);

        sell_selldata = new sell_selldata();
        sell_live = new sell_live();
        sell_mail = new mail_fragment();
        sell_yours = new sell_yours();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
                .add(R.id.frame_container, sell_selldata)
                .add(R.id.frame_container, sell_live)
                .add(R.id.frame_container, sell_mail)
                .add(R.id.frame_container, sell_yours)
                .hide(sell_live)
                .hide(sell_mail)
                .hide(sell_yours)
                .addToBackStack(null)
                .commit();
        active = sell_selldata;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.selldata:
                        fragmentTransaction.hide(active)
                                .show(sell_selldata)
                                .addToBackStack(null)
                                .commit();
                        active = sell_selldata;
                        break;
                    case R.id.live:
                        fragmentTransaction.hide(active)
                                .show(sell_live)
                                .addToBackStack(null)
                                .commit();
                        active = sell_live;
                        break;
                    case R.id.mail:
                        fragmentTransaction.hide(active)
                                .show(sell_mail)
                                .addToBackStack(null)
                                .commit();
                        active = sell_mail;
                        break;
                    case R.id.your:
                        fragmentTransaction.hide(active)
                                .show(sell_yours)
                                .addToBackStack(null)
                                .commit();
                        active = sell_yours;
                        break;
                }
                return true;
            }
        });
    }
}
