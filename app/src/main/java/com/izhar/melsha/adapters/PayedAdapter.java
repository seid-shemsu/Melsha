package com.izhar.melsha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.melsha.R;
import com.izhar.melsha.models.PayedModel;

import java.util.List;

public class PayedAdapter extends RecyclerView.Adapter<PayedAdapter.Holder> {
    Context context;
    List<PayedModel> payeds;

    public PayedAdapter(Context context, List<PayedModel> payeds) {
        this.context = context;
        this.payeds = payeds;
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

    class Holder extends RecyclerView.ViewHolder {
        TextView rcn, tcn, date, pname, pcode, birr;
        public Holder(@NonNull View itemView) {
            super(itemView);
            rcn = itemView.findViewById(R.id.rcn);
            tcn = itemView.findViewById(R.id.tcn);
            date = itemView.findViewById(R.id.date);
            pname = itemView.findViewById(R.id.pname);
            pcode = itemView.findViewById(R.id.pcode);
            birr = itemView.findViewById(R.id.birr);
        }
    }
}
