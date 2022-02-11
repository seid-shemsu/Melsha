package com.izhar.melsha.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.izhar.melsha.ui.purchased.AllPurchased;
import com.izhar.melsha.ui.purchased.DessiePurchased;
import com.izhar.melsha.ui.purchased.JemmoPurchased;
import com.izhar.melsha.ui.purchased.KorePurchased;
import com.izhar.melsha.ui.store.Dessie;
import com.izhar.melsha.ui.store.Jemmo;
import com.izhar.melsha.ui.store.Kore;

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
