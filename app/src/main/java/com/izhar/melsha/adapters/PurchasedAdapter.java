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
        holder.branch.setText(String.valueOf(purchased.getStore()));

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
            /*name = itemView.findViewById(R.id.name);
            size = itemView.findViewById(R.id.size);
            model = itemView.findViewById(R.id.model);
            date = itemView.findViewById(R.id.date);
            delete = itemView.findViewById(R.id.delete);
            move = itemView.findViewById(R.id.move);

            delete.setOnClickListener(v -> {
                Dialog dialog = new Dialog(context);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirmation);
                dialog.show();
                TextView message = dialog.findViewById(R.id.message);
                message.setText("Are you sure to delete this item?");
                ImageView icon = dialog.findViewById(R.id.icon);
                icon.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
                Button cancel = dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });
                Button delete = dialog.findViewById(R.id.confirm);
                delete.setText("Delete");
                delete.setOnClickListener(v1 -> {
                    dialog.setContentView(R.layout.loading);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(context) +
                            "?action=getSingleItem" +
                            "&code=" + purchases.get(getAdapterPosition()).getCode(),
                            response -> {
                                try {
                                    JSONArray array = new JSONArray(response);
                                    JSONObject object = array.getJSONObject(0);
                                    int total_quantity = object.getInt(purchases.get(getAdapterPosition()).getStore());
                                    if (total_quantity >= purchases.get(getAdapterPosition()).getQuantity()) {
                                        deletePurchased(purchases.get(getAdapterPosition()).getId());
                                        setDeleted(getAdapterPosition());
                                        dialog.dismiss();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(context, "unable to delete", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }, error -> {
                        error.printStackTrace();
                        Snackbar.make(delete, "Unable to delete.", Snackbar.LENGTH_LONG)
                                .setAction("Ok", v2 -> {

                                })
                                .show();

                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                });

            });*/

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
}
