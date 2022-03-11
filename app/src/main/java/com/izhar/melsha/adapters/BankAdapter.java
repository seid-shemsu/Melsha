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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.activities.TransactionsActivity;
import com.izhar.melsha.models.BankModel;

import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.Holder> {
    Context context;
    List<BankModel> banks;
    String branch;
    public BankAdapter(Context context, List<BankModel> banks) {
        this.context = context;
        this.banks = banks;
        branch = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "Guest");
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_bank, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        BankModel bank = banks.get(position);
        holder.name.setText(bank.getName());
        holder.account.setText(bank.getAccount());
        if(branch.equalsIgnoreCase("owner"))
            holder.balance.setText("$" + bank.getBalance());
        else
            holder.balance.setText("$" + 0);

    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, account, balance;
        LinearLayout layout;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            account = itemView.findViewById(R.id.account);
            balance = itemView.findViewById(R.id.balance);
            layout = itemView.findViewById(R.id.layout);
            name.setOnClickListener(this);
            account.setOnClickListener(this);
            balance.setOnClickListener(this);
            layout.setOnClickListener(this);
        }

        String action;
        @Override
        public void onClick(View v) {
            Dialog dialog = new Dialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.transaction);
            dialog.show();
            LinearLayout linear1, linear2;
            linear1 = dialog.findViewById(R.id.linear1);
            linear2 = dialog.findViewById(R.id.linear2);
            Button withdraw = dialog.findViewById(R.id.withdraw);
            Button deposit = dialog.findViewById(R.id.deposit);
            Button submit = dialog.findViewById(R.id.submit);
            Button cancel = dialog.findViewById(R.id.cancel);
            EditText amount = dialog.findViewById(R.id.amount);
            TextView title = dialog.findViewById(R.id.title);

            cancel.setOnClickListener(v1 -> {
                linear2.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);
            });

            withdraw.setOnClickListener(v1 -> {
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                action = "Withdraw";
                submit.setText(action);
                title.setText(action);
            });
            deposit.setOnClickListener(v1 -> {
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                action = "Deposit";
                submit.setText(action);
                title.setText(action);
            });

            submit.setOnClickListener(v1 -> {
                if (action.equalsIgnoreCase("Deposit")){
                    makeTransaction("doDeposit" ,dialog, banks.get(getAdapterPosition()).getCode(), banks.get(getAdapterPosition()).getName(), amount);
                }
                else {
                    if (Integer.parseInt(amount.getText().toString()) > banks.get(getAdapterPosition()).getBalance()){
                        Toast.makeText(context, "Insufficient balance", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        makeTransaction("doWithdraw" ,dialog, banks.get(getAdapterPosition()).getCode(), banks.get(getAdapterPosition()).getName(), amount);
                    }
                }
            });


        }

        Utils utils = new Utils();

        private void makeTransaction(String act, Dialog dialog, String code, String name, EditText amount){
            dialog.setContentView(R.layout.loading);
            System.out.println(utils.getUrl(context) + action);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(context) +
                    "?action=" + act +
                    "&code=" + code +
                    "&name=" + name +
                    "&branch="+ branch +
                    "&amount=" + Integer.parseInt(amount.getText().toString()) ,
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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }
}
