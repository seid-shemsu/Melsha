package com.izhar.melsha.ui.loan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.izhar.melsha.R;
import com.izhar.melsha.ui.loan.credits.Credits;
import com.izhar.melsha.ui.loan.customers.Customers;
import com.izhar.melsha.ui.loan.loans.Loans;

public class LoanMain extends Fragment {
  /*  TextView text_get_loan, text_take_loan, text_give_loan, text_pay_loan;
    ImageView img_get_loan, img_take_loan, img_give_loan, img_pay_loan;
    LinearLayout card_get_loan, card_take_loan, card_give_loan, card_pay_loan;*/

    View root;
    String branch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_loan, container, false);

        branch = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "Guest");
        if (!branch.equalsIgnoreCase("owner"))
            root.findViewById(R.id.card_loan).setVisibility(View.GONE);

        root.findViewById(R.id.customers).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Customers.class));
        });
        root.findViewById(R.id.loans).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Loans.class));
        });
        root.findViewById(R.id.credits).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Credits.class));
        });


        return root;
    }


}