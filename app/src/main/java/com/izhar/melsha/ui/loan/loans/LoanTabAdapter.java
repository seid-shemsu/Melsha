package com.izhar.melsha.ui.loan.loans;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.izhar.melsha.ui.loan.credits.GivenFragment;
import com.izhar.melsha.ui.loan.credits.ReturnedFragment;
import com.izhar.melsha.ui.loan.customers.GiversFragment;
import com.izhar.melsha.ui.loan.customers.TakersFragment;

public class LoanTabAdapter extends FragmentStatePagerAdapter {

    private int tab_numbers;
    private String from;
    public LoanTabAdapter(@NonNull FragmentManager fm, int tab_numbers, String from) {
        super(fm);
        this.tab_numbers = tab_numbers;
        this.from = from;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (from.equalsIgnoreCase("loans")) {
            switch (position) {
                case 0:
                    TakenFragment taken = new TakenFragment();
                    return taken;
                case 1:
                    PayedFragment payed = new PayedFragment();
                    return payed;
                default:
                    return null;
            }
        }
        else {
            switch (position) {
                case 0:
                    GivenFragment given = new GivenFragment();
                    return given;
                case 1:
                    ReturnedFragment returned = new ReturnedFragment();
                    return returned;
                default:
                    return null;
            }
        }
    }

    @Override
    public int getCount() {
        return tab_numbers;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (from.equalsIgnoreCase("loans")){
            switch (position) {
                case 0:
                    return "Taken";
                case 1:
                    return "Paid";
            }
        }
        else {
            switch (position) {
                case 0:
                    return "Given";
                case 1:
                    return "Returned";
            }
        }

        return null;
    }
}
