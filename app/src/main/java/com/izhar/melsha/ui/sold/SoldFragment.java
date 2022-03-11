package com.izhar.melsha.ui.sold;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.izhar.melsha.R;
import com.izhar.melsha.adapters.TabAdapter;

public class SoldFragment extends Fragment {

    View root;
    TabAdapter tabAdapter;
    TabLayout tab;
    TabItem all, dessie, jemmo, kore;
    ViewPager view;
    View mIndicator;
    private int indicatorWidth;
    String branch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_store, container, false);

        branch = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "Guest");

        tab = root.findViewById(R.id.tab);
        view = root.findViewById(R.id.viewpager);
        all = root.findViewById(R.id.all);
        dessie = root.findViewById(R.id.dessie);
        jemmo = root.findViewById(R.id.jemmo);
        kore = root.findViewById(R.id.kore);
        mIndicator = root.findViewById(R.id.indicator);
        tabAdapter = new TabAdapter(getFragmentManager(), tab.getTabCount(), getContext());
        view.setAdapter(tabAdapter);
        tab.setupWithViewPager(view);
        //view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + position) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tab.post(() -> {
            indicatorWidth = tab.getWidth() / tab.getTabCount();
            //Assign new width
            FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
            indicatorParams.width = indicatorWidth;
            mIndicator.setLayoutParams(indicatorParams);
        });
        return root;
    }
}