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
import com.izhar.melsha.details.PurchasedDetail;
import com.izhar.melsha.details.SoldDetail;
import com.izhar.melsha.models.PurchasedModel;
import com.izhar.melsha.models.SoldModel;

import java.util.List;

public class SoldAdapter extends RecyclerView.Adapter<SoldAdapter.Holder> {
    Context context;
    List<SoldModel> solds;

    public SoldAdapter(Context context, List<SoldModel> solds) {
        this.context = context;
        this.solds = solds;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_sold, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SoldModel sold = solds.get(position);
        holder.quantity.setText(String.valueOf(sold.getQuantity()));
        holder.profit.setText("$" + String.valueOf(sold.getProfit()));
        holder.code.setText(sold.getCode());
        holder.branch.setText(getStore(sold));
        holder.model.setText(sold.getModel());
    }

    @Override
    public int getItemCount() {
        return solds.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        /*EditText quantity, purchased_price, code, name, size, model, store, date, profit, sold_price;
        Button delete;*/
        TextView quantity, profit, branch, code, model;
        LinearLayout linear;
        public Holder(@NonNull View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.quantity);
            code = itemView.findViewById(R.id.code);
            profit = itemView.findViewById(R.id.profit);
            branch = itemView.findViewById(R.id.branch);
            linear = itemView.findViewById(R.id.linear);
            model = itemView.findViewById(R.id.model);

            quantity.setOnClickListener(v -> {
                context.startActivity(new Intent(context, SoldDetail.class)
                        .putExtra("sold", solds.get(getAdapterPosition())));
            });
            code.setOnClickListener(v -> {
                context.startActivity(new Intent(context, SoldDetail.class)
                        .putExtra("sold", solds.get(getAdapterPosition())));
            });
            profit.setOnClickListener(v -> {
                context.startActivity(new Intent(context, SoldDetail.class)
                        .putExtra("sold", solds.get(getAdapterPosition())));
            });
            branch.setOnClickListener(v -> {
                context.startActivity(new Intent(context, SoldDetail.class)
                        .putExtra("sold", solds.get(getAdapterPosition())));
            });
            linear.setOnClickListener(v -> {
                context.startActivity(new Intent(context, SoldDetail.class)
                        .putExtra("sold", solds.get(getAdapterPosition())));
            });
        }
    }


    private void setDeleted(int adapterPosition) {
        solds.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(0, solds.size());
    }
    private String getStore(SoldModel soldModel) {
        switch ((soldModel.getFrom_store())){
            case "Jemmo":
                return "ሱቅ 1";
            case "Kore":
                return "ሱቅ 3";
            case "Dessie":
                return "ሱቅ 2";
            default:
                return "";
        }
    }
}
