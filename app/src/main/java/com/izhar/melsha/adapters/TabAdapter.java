package com.izhar.melsha.adapters;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.izhar.melsha.ui.store.All;
import com.izhar.melsha.ui.store.Dessie;
import com.izhar.melsha.ui.store.Jemmo;
import com.izhar.melsha.ui.store.Kore;

public class TabAdapter extends FragmentStatePagerAdapter {
    private final int tab_numbers;
    private String branch;
    Context context;
    public TabAdapter(@NonNull FragmentManager fm, int tab_numbers, Context context) {
        super(fm);
        this.tab_numbers = tab_numbers;
        this.context =  context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        branch = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "Guest");
        switch (position){
            case 0:
                return new All();
            case 1:
                return new Jemmo();
            case 2:
                return new Kore();
            case 3:
                return new Dessie();
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
                return "All";
            case 1:
                return "Jemmo";
            case 2:
                return "Kore";
            case 3:
                return "Dessie";
        }
        return null;
    }
}
