package com.izhar.melsha.ui.loan.loans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.izhar.melsha.R;
import com.izhar.melsha.ui.loan.customers.CustomerTabAdapter;

public class Loans extends AppCompatActivity {
    private LoanTabAdapter adapter;
    private TabLayout tab;
    private ViewPager view;
    View mIndicator;
    private int indicatorWidth;

    LinearLayout search;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_loans);
        setTitle("Loans");
        search = findViewById(R.id.search);

        mIndicator = findViewById(R.id.indicator);
        tab = findViewById(R.id.tab);
        view = findViewById(R.id.viewpager);
        adapter = new LoanTabAdapter(getSupportFragmentManager(), tab.getTabCount(), "loans");
        view.setAdapter(adapter);
        tab.setupWithViewPager(view);
        //view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset+position) * indicatorWidth ;
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
    }

}