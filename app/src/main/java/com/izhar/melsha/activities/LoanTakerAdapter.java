package com.izhar.melsha.activities;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.melsha.R;
import com.izhar.melsha.details.CustomerDetail;
import com.izhar.melsha.models.LoanTakerModel;
import com.izhar.melsha.ui.loan.customers.GiveLoan;
import com.izhar.melsha.ui.loan.customers.TakeLoan;

import java.util.List;

public class LoanTakerAdapter extends RecyclerView.Adapter<LoanTakerAdapter.Holder> {
    Context context;
    List<LoanTakerModel> takers;

    public LoanTakerAdapter(Context context, List<LoanTakerModel> takers) {
        this.context = context;
        this.takers = takers;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_loan_giver, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LoanTakerModel taker = takers.get(position);
        holder.code.setText(taker.getCode());
        holder.name.setText(taker.getName());
        //holder.phone.setText(taker.getPhone());
        holder.total.setText("$ " + taker.getTotal());
        holder.payed.setText("$ " + taker.getReturned());
        holder.left.setText("$ " + taker.getLeft());
        try{
            holder.letter.setText(taker.getName().substring(0,1));
        }
        catch (Exception e){
            holder.letter.setText(".");
        }
    }

    @Override
    public int getItemCount() {
        return takers.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView code, name, total, payed, left, letter;
        Button pay, take, delete;
        LinearLayout linaer;
        public Holder(@NonNull View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.letter);
            code = itemView.findViewById(R.id.code);
            name = itemView.findViewById(R.id.name);
            //phone = itemView.findViewById(R.id.phone);
            total = itemView.findViewById(R.id.total);
            payed = itemView.findViewById(R.id.payed);
            left = itemView.findViewById(R.id.left);
            linaer = itemView.findViewById(R.id.linear1);
            total.setOnClickListener(this);
            payed.setOnClickListener(this);
            left.setOnClickListener(this);
            name.setOnClickListener(this);
            code.setOnClickListener(this);
            linaer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Dialog dialog = new Dialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.customer_menu);
            dialog.show();
            TextView detail = dialog.findViewById(R.id.detail);
            TextView action = dialog.findViewById(R.id.action);
            action.setText("Give Credit");
            action.setOnClickListener(v1 -> {
                dialog.dismiss();
                context.startActivity(new Intent(context, GiveLoan.class).putExtra("taker", takers.get(getAdapterPosition())));
            });

            detail.setOnClickListener(v1 -> {
                dialog.dismiss();
                context.startActivity(new Intent(context, CustomerDetail.class)
                        .putExtra("from", "taker")
                        .putExtra("pcode", takers.get(getAdapterPosition()).getCode()));
            });
        }
    }
}
