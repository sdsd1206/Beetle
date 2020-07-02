package com.example.jack.myapplication;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class sell_info extends AppCompatActivity {

    private List<Fragment> mFragments;
    private String[] mTabTitles = {"商品", "訂單資訊"};
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_info);

//       myactionbar();

        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.vp);

        initFragment();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                beginTransaction.show(mFragments.get((int) tab.getPosition()));
                beginTransaction.addToBackStack(mFragments.get(tab.getPosition()).toString());
                beginTransaction = getSupportFragmentManager().beginTransaction();
                beginTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                beginTransaction.show(mFragments.get((int) tab.getPosition()));
                beginTransaction.addToBackStack("sellf");
                beginTransaction = getSupportFragmentManager().beginTransaction();
                beginTransaction.commit();
            }
        });

        //初始化Adapter
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        // 设置adapter将ViewPager和Fragment关联起来
        viewPager.setAdapter(adapter);
        // 将TabLayout和ViewPager关联，达到TabLayout和Viewpager、Fragment联动的效果
        tabLayout.setupWithViewPager(viewPager);

    }

    public void myactionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher_round);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //viewpag 支援左右滑動切換view 不會用XD
    /* 初始化Fragment*/
    private void initFragment() {
        sell_selldata_good itemf = new sell_selldata_good();
        sell_selldata_order orderf = new sell_selldata_order();
        // 将三个fragment放入List里面管理，方便使用
        mFragments = new ArrayList<>();
        mFragments.add(itemf);
        mFragments.add(orderf);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context context1;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context1 = context;
        }

        @Override
        public Fragment getItem(int position) {
            // 获取指定位置的Fragment对象
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            // ViewPager管理页面的数量
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // 设置indicator的标题（TabLayout中tab的标题）
            return mTabTitles[position];
        }
    }
}
