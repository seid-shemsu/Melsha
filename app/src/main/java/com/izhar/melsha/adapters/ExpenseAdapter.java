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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.izhar.melsha.models.ExpenseModel;
import com.izhar.melsha.models.PayedModel;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.Holder> {
    Context context;
    List<ExpenseModel> expenses;
    public ExpenseAdapter(Context context, List<ExpenseModel> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_expense, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ExpenseModel expense = expenses.get(position);
        holder.type.setText(expense.getReason());
        holder.amount.setText(""+expense.getAmount());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        EditText type, amount;
        LinearLayout linear;
        ImageView edit, delete;
        public Holder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            type.setEnabled(false);
            amount = itemView.findViewById(R.id.amount);
            amount.setEnabled(false);
            linear = itemView.findViewById(R.id.linear);
            linear.setVisibility(View.VISIBLE);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(v -> {
                System.out.println(expenses.get(getAdapterPosition()).getNumber());
                System.out.println(expenses.get(getAdapterPosition()).getReason());
                System.out.println(expenses.get(getAdapterPosition()).getAmount());
                doDelete(expenses.get(getAdapterPosition()).getNumber());
            });
            edit.setOnClickListener(v -> {
                if (type.isEnabled()){
                    type.setEnabled(false);
                    amount.setEnabled(false);
                    edit.setImageDrawable(context.getResources().getDrawable(R.drawable.edit));
                    doEdit(expenses.get(getAdapterPosition()).getNumber(),
                           expenses.get(getAdapterPosition()).getReason(),
                           Integer.parseInt((amount.getText().toString())));
                }
                else {
                    type.setEnabled(true);
                    amount.setEnabled(true);
                    edit.setImageDrawable(context.getResources().getDrawable(R.drawable.check));
                }
            });
        }

    }

    private void doEdit(String number, String reason, int amount) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        System.out.println(number);
        System.out.println(reason);
        System.out.println(amount);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(context) +
                "?action=updateDailyEx" +
                "&num=" + number +
                "&name=" + reason +
                "&amount=" + amount,
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

    private void doDelete(String number) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(context) +
                "?action=deleteExpense&num=" + number,
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

    Utils utils = new Utils();
}