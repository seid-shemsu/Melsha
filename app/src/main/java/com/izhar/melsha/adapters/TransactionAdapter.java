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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.models.BankModel;
import com.izhar.melsha.models.TransactionModel;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.Holder> {
    Context context;
    List<TransactionModel> transactions;

    public TransactionAdapter(Context context, List<TransactionModel> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TransactionModel bank = transactions.get(position);
        holder.date.setText(bank.getDate());
        holder.bank.setText(bank.getBank());
        holder.amount.setText(bank.getAmount());
        holder.type.setText(bank.getType());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date, bank, amount, type;
        public Holder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            bank = itemView.findViewById(R.id.bank);
            amount = itemView.findViewById(R.id.amount);
            type = itemView.findViewById(R.id.type);
        }

        String action;
        @Override
        public void onClick(View v) {
            Dialog dialog = new Dialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.confirmation);
            dialog.show();

            Button confirm = dialog.findViewById(R.id.confirm);
            confirm.setText("Delete");
            Button cancel = dialog.findViewById(R.id.cancel);
            confirm.setOnClickListener(v1 -> {
                TransactionModel transaction = transactions.get(getAdapterPosition());
                if(transaction.getType().equalsIgnoreCase("deposit")){
                    delete("deleteDeposit&num=" + transaction.getId() + "&bank_code=" + transaction.getBank() + "&amount=" + transaction.getAmount(), dialog);
                }
                else {
                    delete("deleteWithdraw&num=" + transaction.getId() + "&bank_code=" + transaction.getBank() + "&amount=" + transaction.getAmount(), dialog);
                }
            });
            cancel.setOnClickListener(v1 -> dialog.dismiss());
        }

        Utils utils = new Utils();

        private void delete(String action, Dialog dialog){
            dialog.setContentView(R.layout.loading);
            System.out.println(utils.getUrl(context) + action);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(context) +
                    "?action=" + action ,
                    response -> {
                        if (response.startsWith("<")){
                            dialog.dismiss();
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            System.out.println(response);
                            context.startActivity(new Intent(context, ErrorActivity.class).putExtra("error", response));
                        }
                        else {
                            dialog.dismiss();
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                error.printStackTrace();
                dialog.dismiss();
                context.startActivity(new Intent(context, ErrorActivity.class).putExtra("error", error.toString()));

            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }
}
