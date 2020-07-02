package com.example.jack.myapplication;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class sell_selldata extends Fragment {

    private List<Fragment> mFragments;
    private MyFragmentPagerAdapter adapter;
    private TabLayout tabLayout;
    private Context con;
    private ViewPager viewPager;
    private String[] mTabTitles = {"商品", "訂單資訊"};

    public sell_selldata() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.sell_selldata, container, false);
        con = getActivity();
        tabLayout = (TabLayout) v.findViewById(R.id.tab);
        viewPager = (ViewPager) v.findViewById(R.id.vp);
        initFragment();

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            FragmentTransaction beginTransaction = getChildFragmentManager().beginTransaction();
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                beginTransaction.show(mFragments.get((int) tab.getPosition()));
//                beginTransaction.addToBackStack(mFragments.get(tab.getPosition()).toString());
//                beginTransaction = getChildFragmentManager().beginTransaction();
//                beginTransaction.commit();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                beginTransaction.show(mFragments.get((int) tab.getPosition()));
//                beginTransaction.addToBackStack("sellf");
//                beginTransaction = getChildFragmentManager().beginTransaction();
//                beginTransaction.commit();
//            }
//        });

        //初始化Adapter
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), con);
        // 设置adapter将ViewPager和Fragment关联起来
        viewPager.setAdapter(adapter);
        // 将TabLayout和ViewPager关联，达到TabLayout和Viewpager、Fragment联动的效果
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }


    private void initFragment() {
        sell_selldata_good goodf = new sell_selldata_good();
        sell_selldata_order orderf = new sell_selldata_order();

        mFragments = new ArrayList<>();
        mFragments.add(goodf);
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
