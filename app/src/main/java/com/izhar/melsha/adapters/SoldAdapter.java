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
        holder.branch.setText(String.valueOf(sold.getFrom_store()));
        holder.model.setText(sold.getModel());
        /*holder.purchased_price.setText(String.valueOf(sold.getPurchased_price()));
        holder.name.setText(sold.getName());
        holder.size.setText(String.valueOf(sold.getSize()));
        holder.model.setText(sold.getModel());
        holder.sold_price.setText(String.valueOf(sold.getSold_price()));
        holder.date.setText(sold.getDate().substring(0, sold.getDate().indexOf('T')));*/
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
            /*purchased_price = itemView.findViewById(R.id.purchased_price);
            name = itemView.findViewById(R.id.name);
            size = itemView.findViewById(R.id.size);
            model = itemView.findViewById(R.id.model);
            date = itemView.findViewById(R.id.date);
            sold_price = itemView.findViewById(R.id.sold_price);
            delete = itemView.findViewById(R.id.delete);
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
                    deleteSold(solds.get(getAdapterPosition()).getId());
                    dialog.dismiss();
                    setDeleted(getAdapterPosition());
                });
            });*/
        }
    }

    private void deleteSold(String id) {
        Utils utils = new Utils();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(context) +
                "?action=deleteSoldItem"+
                "&id=" + id,
                response -> {
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();

                }, error -> {
            System.out.println(error.getMessage());
            Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void setDeleted(int adapterPosition) {
        solds.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(0, solds.size());

    }
}
