package com.izhar.melsha.details;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.models.SoldModel;

public class SoldDetail extends AppCompatActivity {
    EditText quantity, purchased_price, code, name, size, model, store, date, profit, sold_price;
    Button delete;
    SoldModel sold;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sold_detail);
        sold = (SoldModel) getIntent().getExtras().getSerializable("sold");
        setTitle(sold.getCode());
        quantity = findViewById(R.id.quantity);
        purchased_price = findViewById(R.id.purchased_price);
        store = findViewById(R.id.store);
        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        size = findViewById(R.id.size);
        model = findViewById(R.id.model);
        date = findViewById(R.id.date);
        profit = findViewById(R.id.profit);
        sold_price = findViewById(R.id.sold_price);
        delete = findViewById(R.id.delete);
        quantity.setText(String.valueOf(sold.getQuantity()));
        purchased_price.setText(String.valueOf(sold.getPurchased_price()));
        store.setText(String.valueOf(sold.getFrom_store()));
        code.setText(sold.getCode());
        name.setText(sold.getName());
        size.setText(String.valueOf(sold.getSize()));
        model.setText(sold.getModel());
        profit.setText(String.valueOf(sold.getProfit()));
        sold_price.setText(String.valueOf(sold.getSold_price()));
        date.setText(sold.getDate().substring(0, sold.getDate().indexOf('T')));
        delete.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.confirmation);
            dialog.show();
            TextView message = dialog.findViewById(R.id.message);
            message.setText("Are you sure to delete this item?");
            ImageView icon = dialog.findViewById(R.id.icon);
            icon.setImageDrawable(this.getResources().getDrawable(R.drawable.delete));
            Button cancel = dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            Button delete = dialog.findViewById(R.id.confirm);
            delete.setText("Delete");
            delete.setOnClickListener(v1 -> {
                deleteSold(sold.getId());
                dialog.dismiss();
            });
        });
    }
    private void deleteSold(String id) {
        Utils utils = new Utils();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=deleteSoldItem"+
                "&id=" + id,
                response -> {
                    Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }, error -> {
            System.out.println(error.getMessage());
            Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}