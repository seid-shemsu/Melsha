package com.izhar.melsha.ui.loan.customers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.izhar.melsha.R;

public class LoanPeoples extends AppCompatActivity {
    private CustomerTabAdapter adapter;
    private TabLayout tab;
    private ViewPager view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_peoples);
        tab = findViewById(R.id.tab);
        view = findViewById(R.id.viewpager);
        adapter = new CustomerTabAdapter(getSupportFragmentManager(), tab.getTabCount());
        view.setAdapter(adapter);
        view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
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
    }
}