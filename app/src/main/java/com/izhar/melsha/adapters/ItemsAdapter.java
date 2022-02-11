package com.izhar.melsha.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.melsha.R;
import com.izhar.melsha.details.ItemDetail;
import com.izhar.melsha.models.ItemModel;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.Holder>{
    Context context;
    List<ItemModel> items;

    public ItemsAdapter(Context context, List<ItemModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ItemModel item = items.get(position);
        //holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.avg_price.setText("$" + item.getAvg_price());
        //holder.total_capital.setText(String.valueOf(item.getTotal_capital()));
        holder.code.setText(item.getCode());
        /*holder.name.setText(item.getName());
        holder.size.setText(String.valueOf(item.getSize()));
        holder.model.setText(item.getModel());*/
        holder.dessie.setText("Dessie: " + item.getDessie());
        holder.kore.setText("Kore: " + item.getKore());
        holder.jemmo.setText("Jemmo: " + item.getJemmo());
        holder.model.setText(item.getModel());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        /*EditText quantity, avg_price, total_capital, code, name, size, model, dessie, kore, jemmo;
        Button purchase, sell, move;*/
        TextView code, avg_price, dessie, kore, jemmo, model;
        LinearLayout linear;
        public Holder(@NonNull View itemView) {
            super(itemView);
            //quantity = itemView.findViewById(R.id.quantity);
            avg_price = itemView.findViewById(R.id.avg_price);
            model = itemView.findViewById(R.id.model);
            //total_capital = itemView.findViewById(R.id.total_capital);
            code = itemView.findViewById(R.id.code);
            //name = itemView.findViewById(R.id.name);
            //size = itemView.findViewById(R.id.size);
            //model = itemView.findViewById(R.id.model);
            dessie = itemView.findViewById(R.id.dessie);
            kore = itemView.findViewById(R.id.kore);
            jemmo = itemView.findViewById(R.id.jemmo);


            /*purchase = itemView.findViewById(R.id.purchase);
            purchase.setOnClickListener(v -> {
                context.startActivity(new Intent(context, Purchase.class)
                        .putExtra("item", items.get(getAdapterPosition())));
            });

            move = itemView.findViewById(R.id.move);
            move.setOnClickListener(v -> {
                context.startActivity(new Intent(context, Move.class)
                        .putExtra("item", items.get(getAdapterPosition())));
            });

            sell = itemView.findViewById(R.id.sell);
            sell.setOnClickListener(v -> {
                context.startActivity(new Intent(context, Sell.class)
                        .putExtra("item", items.get(getAdapterPosition())));
            });*/

            linear = itemView.findViewById(R.id.linear);
            linear.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ItemDetail.class)
                        .putExtra("item", items.get(getAdapterPosition())));
            });
            dessie.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ItemDetail.class)
                        .putExtra("item", items.get(getAdapterPosition())));
            });
            kore.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ItemDetail.class)
                        .putExtra("item", items.get(getAdapterPosition())));
            });
            jemmo.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ItemDetail.class)
                        .putExtra("item", items.get(getAdapterPosition())));
            });
            code.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ItemDetail.class)
                        .putExtra("item", items.get(getAdapterPosition())));
            });
            avg_price.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ItemDetail.class)
                        .putExtra("item", items.get(getAdapterPosition())));
            });
        }

    }
}