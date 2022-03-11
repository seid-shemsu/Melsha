package com.izhar.melsha.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.izhar.melsha.ui.purchased.AllPurchased;
import com.izhar.melsha.ui.purchased.DessiePurchased;
import com.izhar.melsha.ui.purchased.JemmoPurchased;
import com.izhar.melsha.ui.purchased.KorePurchased;

public class PurchasedTabAdapter extends FragmentStatePagerAdapter {

    private int tab_numbers;
    public PurchasedTabAdapter(@NonNull FragmentManager fm, int tab_numbers) {
        super(fm);
        this.tab_numbers = tab_numbers;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllPurchased();
            case 1:
                return new JemmoPurchased();
            case 2:
                return new KorePurchased();
            case 3:
                return new DessiePurchased();
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
                return "ሁሉም";
            case 1:
                return "ሱቅ 1";
            case 2:
                return "ሱቅ 2";
            case 3:
                return "ሱቅ 3";
        }
        return null;
    }
}
