package com.izhar.melsha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.melsha.R;
import com.izhar.melsha.models.CreditedItemModel;
import com.izhar.melsha.models.ItemModel;

import java.util.List;

public class CreditedItemAdapter extends RecyclerView.Adapter<CreditedItemAdapter.Holder> {
    Context context;
    List<CreditedItemModel> items;

    public CreditedItemAdapter(Context context, List<CreditedItemModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_credited_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CreditedItemModel item = items.get(position);
        holder.quantity.setText("Quantity: " + item.getQuantity());
        holder.item_code.setText("Item code: "+ item.getItem_code());
        holder.pcode.setText("P.Code: " + item.getPcode());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView item_code, pcode, quantity;
        public Holder(@NonNull View itemView) {
            super(itemView);
            item_code = itemView.findViewById(R.id.item_code);
            pcode = itemView.findViewById(R.id.pcode);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}
