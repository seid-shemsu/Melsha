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
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.details.PurchasedDetail;
import com.izhar.melsha.models.PurchasedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PurchasedAdapter extends RecyclerView.Adapter<PurchasedAdapter.Holder> {
    Context context;
    List<PurchasedModel> purchases;
    Utils utils;

    public PurchasedAdapter(Context context, List<PurchasedModel> purchases) {
        this.context = context;
        this.purchases = purchases;
        utils = new Utils();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_purchased, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PurchasedModel purchased = purchases.get(position);
        holder.quantity.setText(String.valueOf(purchased.getQuantity()));
        holder.purchased_price.setText("$" + purchased.getPurchased_price());
        holder.code.setText(purchased.getCode());
        holder.branch.setText(getStore(purchased));

        /*holder.name.setText(purchased.getName());
        holder.size.setText(String.valueOf(purchased.getSize()));
        holder.model.setText(purchased.getModel());
        holder.date.setText(purchased.getDate().substring(0, purchased.getDate().indexOf('T')));*/
    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        /*EditText quantity, purchased_price, code, name, size, model, store, date;
        Button delete, move;*/
        TextView code, quantity, branch, purchased_price;
        LinearLayout linear;
        public Holder(@NonNull View itemView) {
            super(itemView);
            linear = itemView.findViewById(R.id.linear);
            quantity = itemView.findViewById(R.id.quantity);
            purchased_price = itemView.findViewById(R.id.purchased_price);
            branch = itemView.findViewById(R.id.branch);
            code = itemView.findViewById(R.id.code);
            linear.setOnClickListener(v -> {
                context.startActivity(new Intent(context, PurchasedDetail.class)
                        .putExtra("purchased", purchases.get(getAdapterPosition())));
            });
            code.setOnClickListener(v -> {
                context.startActivity(new Intent(context, PurchasedDetail.class)
                        .putExtra("purchased", purchases.get(getAdapterPosition())));
            });
            quantity.setOnClickListener(v -> {
                context.startActivity(new Intent(context, PurchasedDetail.class)
                        .putExtra("purchased", purchases.get(getAdapterPosition())));
            });
            branch.setOnClickListener(v -> {
                context.startActivity(new Intent(context, PurchasedDetail.class)
                        .putExtra("purchased", purchases.get(getAdapterPosition())));
            });
            purchased_price.setOnClickListener(v -> {
                context.startActivity(new Intent(context, PurchasedDetail.class)
                        .putExtra("purchased", purchases.get(getAdapterPosition())));
            });
        }
    }

    private void deletePurchased(String id) {
        Utils utils = new Utils();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(context) +
                "?action=deletePurchasedItem" +
                "&id=" + id,
                response -> {
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();

                }, error -> {
            System.out.println(error.getMessage());
            Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void setDeleted(int adapterPosition) {
        purchases.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(0, purchases.size());
    }

    private String getStore(PurchasedModel purchased) {
        switch ((purchased.getStore())){
            case "Jemmo":
                return "ሱቅ 1";
            case "Kore":
                return "ሱቅ 2";
            case "Dessie":
                return "ሱቅ 3";
            default:
                return "";
        }
    }
}
