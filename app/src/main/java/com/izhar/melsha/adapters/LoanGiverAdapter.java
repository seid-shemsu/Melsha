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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.melsha.R;
import com.izhar.melsha.details.CustomerDetail;
import com.izhar.melsha.models.LoanGiverModel;
import com.izhar.melsha.ui.loan.customers.TakeLoan;

import java.util.List;

public class LoanGiverAdapter extends RecyclerView.Adapter<LoanGiverAdapter.Holder> {
    Context context;
    List<LoanGiverModel> givers;

    public LoanGiverAdapter(Context context, List<LoanGiverModel> givers) {
        this.context = context;
        this.givers = givers;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_loan_giver, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LoanGiverModel giver = givers.get(position);
        holder.code.setText(giver.getCode());
        holder.name.setText(giver.getName());
        //holder.phone.setText(giver.getPhone());
        holder.total.setText("$ " + giver.getTotal());
        holder.payed.setText("$ " + giver.getPayed());
        holder.left.setText("$ " + giver.getLeft());
        holder.letter.setText(giver.getName().substring(0,1));
    }

    @Override
    public int getItemCount() {
        return givers.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView code, name, total, payed, left, letter;
        LinearLayout layout;
        public Holder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            letter = itemView.findViewById(R.id.letter);
            code = itemView.findViewById(R.id.code);
            name = itemView.findViewById(R.id.name);
            //phone = itemView.findViewById(R.id.phone);
            total = itemView.findViewById(R.id.total);
            payed = itemView.findViewById(R.id.payed);
            left = itemView.findViewById(R.id.left);
            total.setOnClickListener(this);
            payed.setOnClickListener(this);
            left.setOnClickListener(this);
            name.setOnClickListener(this);
            code.setOnClickListener(this);
            layout.setOnClickListener(this);
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
            action.setText("TAKE LOAN");
            action.setOnClickListener(v1 -> {
                dialog.dismiss();
                context.startActivity(new Intent(context, TakeLoan.class).putExtra("giver", givers.get(getAdapterPosition())));
            });

            detail.setOnClickListener(v1 -> {
                dialog.dismiss();
                context.startActivity(new Intent(context, CustomerDetail.class)
                        .putExtra("from", "giver")
                        .putExtra("pcode", givers.get(getAdapterPosition()).getCode()));
            });
        }
    }
}
