package com.izhar.melsha.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.melsha.R;
import com.izhar.melsha.details.CreditedItems;
import com.izhar.melsha.models.LoanModel;
import com.izhar.melsha.ui.loan.loans.PayLoan;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.Holder> {
    Context context;
    List<LoanModel> loans;
    String from;

    public LoanAdapter(Context context, List<LoanModel> loans, String from) {
        this.context = context;
        this.loans = loans;
        this.from = from;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_loan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LoanModel loan = loans.get(position);
        holder.credit_no.setText(loan.getCredit_no());
        holder.person_name.setText(loan.getPerson_name());
        holder.person_code.setText(loan.getPerson_code());
        holder.quantity.setText(loan.getQuantity());
        holder.amount.setText(loan.getAmount());
        holder.payed.setText(loan.getPayed());
        holder.left.setText(loan.getLeft());

        try {
            holder.date.setText(loan.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView credit_no, date, person_name, person_code, quantity, amount, payed, left;
        RelativeLayout relative1;
        LinearLayout relative2;

        public Holder(@NonNull View itemView) {
            super(itemView);
            credit_no = itemView.findViewById(R.id.credit_no);
            date = itemView.findViewById(R.id.date);
            person_name = itemView.findViewById(R.id.person_name);
            person_code = itemView.findViewById(R.id.person_code);
            quantity = itemView.findViewById(R.id.quantity);
            amount = itemView.findViewById(R.id.amount);
            payed = itemView.findViewById(R.id.payed);
            left = itemView.findViewById(R.id.left);

            relative1 = itemView.findViewById(R.id.relative1);
            relative2 = itemView.findViewById(R.id.linear1);

            credit_no.setOnClickListener(this);
            date.setOnClickListener(this);
            person_name.setOnClickListener(this);
            person_code.setOnClickListener(this);
            quantity.setOnClickListener(this);
            amount.setOnClickListener(this);
            payed.setOnClickListener(this);
            left.setOnClickListener(this);
            relative1.setOnClickListener(this);
            relative2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Dialog dialog = new Dialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.customer_menu);
            dialog.show();
            Button detail = dialog.findViewById(R.id.detail);
            Button action = dialog.findViewById(R.id.action);
            if (from.equalsIgnoreCase("loan"))
                action.setText("Pay loan");
            else if (from.equalsIgnoreCase("credit"))
                action.setText("get  paid");

            action.setOnClickListener(v1 -> {
                dialog.dismiss();
                context.startActivity(new Intent(context, PayLoan.class)
                        .putExtra("from", from)
                        .putExtra("loan", loans.get(getAdapterPosition())));
            });

            detail.setOnClickListener(v1 -> {
                dialog.dismiss();

                context.startActivity(new Intent(context, CreditedItems.class)
                        .putExtra("cn", loans.get(getAdapterPosition()).getCredit_no())
                        .putExtra("from", from));

            });
        }
    }

}
