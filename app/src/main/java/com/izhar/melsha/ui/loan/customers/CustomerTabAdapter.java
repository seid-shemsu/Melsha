package com.izhar.melsha.ui.loan.customers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CustomerTabAdapter extends FragmentStatePagerAdapter {

    private int tab_numbers;

    public CustomerTabAdapter(@NonNull FragmentManager fm, int tab_numbers) {
        super(fm);
        this.tab_numbers = tab_numbers;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GiversFragment givers = new GiversFragment();
                return givers;
            case 1:
                TakersFragment takers = new TakersFragment();
                return takers;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tab_numbers;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Givers";
            case 1:
                return "Takers";
        }
        return null;
    }
}
