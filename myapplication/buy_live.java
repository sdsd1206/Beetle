package com.example.jack.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class buy_live extends Fragment {

    private String[] mTabTitles = {"競標", "一般"};
    private List<Fragment> mFragments;
    private MyFragmentPagerAdapter fadapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Context con;

    private store store;
    private DatabaseReference ref;
    private Toolbar toolbar;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private List<store> data = new ArrayList<>();
//    private MyAdapter adapter;

    public buy_live() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = getView().findViewById(R.id.toolbar3);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("直播");
        setHasOptionsMenu(true);

        con = getActivity();
        tabLayout = (TabLayout) getView().findViewById(R.id.tab);
        viewPager = (ViewPager) getView().findViewById(R.id.vp);
        initFragment();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //初始化Adapter
        fadapter = new MyFragmentPagerAdapter(getChildFragmentManager(), con);
        // 设置adapter将ViewPager和Fragment关联起来
        viewPager.setAdapter(fadapter);
        // 将TabLayout和ViewPager关联，达到TabLayout和Viewpager、Fragment联动的效果
        tabLayout.setupWithViewPager(viewPager);


//        getData();
//        adapter = new MyAdapter(getActivity());
//        this.setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.buy_live, container, false);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.toolbar_search, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//
//            queryTextListener = new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    Log.i("onQueryTextChange", newText);
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Log.i("onQueryTextSubmit", query);
//
//                    return true;
//                }
//            };
//            searchView.setOnQueryTextListener(queryTextListener);
//        }
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                // Not implemented here
//                return false;
//            default:
//                break;
//        }
//        searchView.setOnQueryTextListener(queryTextListener);
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), buy_live_bidding_live.class);
//        startActivity(intent);
//    }
//
//    private void getData() {
//        ref = FirebaseDatabase.getInstance().getReference("store");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                data.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    store = ds.getValue(store.class);
//                    data.add(store);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    static class ViewHolder {
//        public ImageView img1;
//        public ImageView img2;
//        public TextView s_name;
//        public Button follow;
//        public Button report;
//    }
//
//    public class MyAdapter extends BaseAdapter {
//        private LayoutInflater mInflater = null;
//
//        private MyAdapter(Context context) {
//            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
//            this.mInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            //How many items are in the data set represented by this Adapter.
//            //在此适配器中所代表的数据集中的条目数
//            return data.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // Get the data item associated with the specified position in the data set.
//            //获取数据集中与指定索引对应的数据项
//            return data.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            //Get the row id associated with the specified position in the list.
//            //获取在列表中与指定索引对应的行id
//            return position;
//        }
//
//        //Get a View that displays the data at the specified position in the data set.
//        //获取一个在数据集中指定索引的视图来显示数据
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            ViewHolder holder = null;
//            //如果缓存convertView为空，则需要创建View
//            if (convertView == null) {
//                holder = new ViewHolder();
//                //根据自定义的Item布局加载布局
//                convertView = mInflater.inflate(R.layout.buy_live_item, null);
//                holder.img1 = (ImageView) convertView.findViewById(R.id.imageView3);
//                holder.img2 = (ImageView) convertView.findViewById(R.id.imageButton4);
//                holder.s_name = (TextView) convertView.findViewById(R.id.textView2);
//                holder.follow = (Button) convertView.findViewById(R.id.button9);
//                holder.report = (Button) convertView.findViewById(R.id.button10);
//                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            // holder.img1.setImageResource((Integer)data.get(position).get("img1"));
//            // holder.img2.setImageResource((Integer)data.get(position).get("img2"));
//            holder.s_name.setText((String) data.get(position).getName());
//            holder.follow.setText("追隨");
//            holder.follow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            holder.report.setText("檢舉");
//            holder.report.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            return convertView;
//        }
//    }

    private void initFragment() {
        buy_live_bidding bidding = new buy_live_bidding();
        buy_live_normal normal = new buy_live_normal();

        mFragments = new ArrayList<>();
        mFragments.add(bidding);
        mFragments.add(normal);
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
