package com.izhar.melsha.ui.bank;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.izhar.melsha.ui.loan.customers.GiversFragment;
import com.izhar.melsha.ui.loan.customers.TakersFragment;

public class TransactionTabAdapter extends FragmentStatePagerAdapter {

    private int tab_numbers;

    public TransactionTabAdapter(@NonNull FragmentManager fm, int tab_numbers) {
        super(fm);
        this.tab_numbers = tab_numbers;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DepositFragment();
            case 1:
                return new WithdrawFragment();
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
                return "Deposit";
            case 1:
                return "Withdraw";
        }
        return null;
    }
}
