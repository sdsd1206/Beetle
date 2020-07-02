package com.example.jack.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class live_activity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager myViewPager;
    private TabLayout tabLayout;
    private String[] title = {"競標", "一般"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        myViewPager = (ViewPager) findViewById(R.id.myViewPager);
        tabLayout = (TabLayout) findViewById(R.id.TabLayout);
        setViewPager();
        tabLayout.setupWithViewPager(myViewPager);
        setTabLayoutIcon();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                String active = "";
                switch (menuItem.getItemId()) {
                    case R.id.shop:
                        active = "home";
                        break;
                    case R.id.car:
                        active = "buy_buycar";
                        break;
                    case R.id.mail:
                        active = "notify";
                        break;
                    case R.id.your:
                        active = "set";
                        break;
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("fragment", active);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
    }

    public void setTabLayoutIcon() {
        for (int i = 0; i < title.length; i++) {
            tabLayout.getTabAt(i).setText(title[i]);
        }

    }

    private void setViewPager() {
        buy_live_bidding myFragment1 = new buy_live_bidding();
        buy_live_normal myFragment2 = new buy_live_normal();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(myFragment1);
        fragmentList.add(myFragment2);
        ViewPagerFragmentAdapter myFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
    }

    public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
}
