package com.example.jack.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class sell_selldata_order extends Fragment {

    private FragmentTransaction beginTransaction;
    sell_selldata_order_normalstore nomalstore;
    sell_selldata_order_normallive nomallive;
    sell_selldata_order_biddinglive racelive;
    Fragment before;

    public sell_selldata_order() {
        // Required empty public constructor
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            beginTransaction = getFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_nstore:
                    beginTransaction.hide(before);
                    beginTransaction.show(nomalstore);
                    beginTransaction.addToBackStack(null);
                    beginTransaction.commit();
                    before = nomalstore;
                    break;
                case R.id.navigation_rlive:
                    beginTransaction.hide(before);
                    beginTransaction.show(racelive);
                    beginTransaction.addToBackStack(null);
                    beginTransaction.commit();
                    before = racelive;
                    break;
                case R.id.navigation_nlive:
                    beginTransaction.hide(before);
                    beginTransaction.show(nomallive);
                    beginTransaction.addToBackStack(null);
                    beginTransaction.commit();
                    before = nomallive;
                    break;
            }
            return true;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View v = inflater.inflate(R.layout.sell_selldata_order, container, false);
        initial();
        BottomNavigationView navigation = (BottomNavigationView) v.findViewById(R.id.order_na);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return v;
    }

    private void initial() {
        nomallive = new sell_selldata_order_normallive();
        nomalstore = new sell_selldata_order_normalstore();
        racelive = new sell_selldata_order_biddinglive();

        beginTransaction = getFragmentManager().beginTransaction();
        beginTransaction.add(R.id.order, nomalstore);
        beginTransaction.add(R.id.order, racelive);
        beginTransaction.add(R.id.order, nomallive);
        beginTransaction.hide(racelive);
        beginTransaction.hide(nomallive);
        beginTransaction.addToBackStack(null);//返回到上一个顯示的fragment
        beginTransaction.commit();//每一個事物最后操作必須是commit（），否则看不見效果
        before = nomalstore;
    }
}
