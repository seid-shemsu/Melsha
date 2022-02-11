package com.izhar.melsha.ui.loan;

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
    TextView customers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_loan, container, false);
        root.findViewById(R.id.customers).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Customers.class));
        });
        root.findViewById(R.id.loans).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Loans.class));
        });
        root.findViewById(R.id.credits).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Credits.class));
        });
        /*text_get_loan = root.findViewById(R.id.text_get_loan);
        img_get_loan  = root.findViewById(R.id.img_get_loan);
        card_get_loan = root.findViewById(R.id.card_get_loan);
        text_get_loan.setOnClickListener(v -> getLoan());
        img_get_loan.setOnClickListener(v -> getLoan());
        card_get_loan.setOnClickListener(v -> getLoan());


        text_take_loan = root.findViewById(R.id.text_take_loan);
        img_take_loan  = root.findViewById(R.id.img_take_loan);
        card_take_loan = root.findViewById(R.id.card_take_loan);
        text_take_loan.setOnClickListener(v -> takeLoan());
        img_take_loan.setOnClickListener(v -> takeLoan());
        card_take_loan.setOnClickListener(v -> takeLoan());

        text_give_loan = root.findViewById(R.id.text_give_loan);
        img_give_loan  = root.findViewById(R.id.img_give_loan);
        card_give_loan = root.findViewById(R.id.card_give_loan);
        text_give_loan.setOnClickListener(v -> giveLoan());
        img_give_loan.setOnClickListener(v -> giveLoan());
        card_give_loan.setOnClickListener(v -> giveLoan());

        text_pay_loan = root.findViewById(R.id.text_pay_loan);
        img_pay_loan  = root.findViewById(R.id.img_pay_loan);
        card_pay_loan = root.findViewById(R.id.card_pay_loan);
        text_pay_loan.setOnClickListener(v -> payLoan());
        img_pay_loan.setOnClickListener(v -> payLoan());
        card_pay_loan.setOnClickListener(v -> payLoan());

        root.findViewById(R.id.given).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), LoanGiven.class));
        });

        root.findViewById(R.id.taken).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), LoanTaken.class));
        });

        root.findViewById(R.id.peoples).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), LoanPeoples.class));
        });*/


        return root;
    }


}