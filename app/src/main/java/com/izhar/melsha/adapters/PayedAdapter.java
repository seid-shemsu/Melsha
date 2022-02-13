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
import com.izhar.melsha.models.PayedModel;

import java.util.List;

public class PayedAdapter extends RecyclerView.Adapter<PayedAdapter.Holder> {
    Context context;
    List<PayedModel> payeds;
    String from;
    public PayedAdapter(Context context, List<PayedModel> payeds, String from) {
        this.context = context;
        this.payeds = payeds;
        this.from = from;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_payed, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PayedModel payed = payeds.get(position);
        holder.rcn.setText(payed.getCRN());
        holder.tcn.setText(payed.getTCN());
        holder.date.setText(payed.getDate());
        holder.pname.setText(payed.getPname());
        holder.pcode.setText(payed.getPcode());
        holder.birr.setText("$ " + payed.getBirr());
    }

    @Override
    public int getItemCount() {
        return payeds.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rcn, tcn, date, pname, pcode, birr;
        RelativeLayout relative1, relative2;
        public Holder(@NonNull View itemView) {
            super(itemView);
            rcn = itemView.findViewById(R.id.rcn);
            tcn = itemView.findViewById(R.id.tcn);
            pname = itemView.findViewById(R.id.pname);
            pcode = itemView.findViewById(R.id.pcode);
            date = itemView.findViewById(R.id.date);
            birr = itemView.findViewById(R.id.birr);
            relative1 = itemView.findViewById(R.id.relative1);
            relative2 = itemView.findViewById(R.id.relative2);

            relative1.setOnClickListener(this);
            rcn.setOnClickListener(this);
            tcn.setOnClickListener(this);
            pname.setOnClickListener(this);
            pcode.setOnClickListener(this);
            date.setOnClickListener(this);
            birr.setOnClickListener(this);
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
            action.setText("Delete");
            detail.setVisibility(View.GONE);
            action.setOnClickListener(v1 -> {
                if (from.equalsIgnoreCase("loan")){
                    delete("deleteLoanBycrn&crn=" + payeds.get(getAdapterPosition()).getCRN());
                }
                else {
                    delete("deleteCreditBycrn&crn=" + payeds.get(getAdapterPosition()).getCRN());
                }
            });
        }
    }
    Utils utils = new Utils();
    private void delete(String action){
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        System.out.println(utils.getUrl(context) + action);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(context) +
                "?action=" + action,
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
