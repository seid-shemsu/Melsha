package com.izhar.melsha.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.models.ItemModel;
import com.izhar.melsha.models.PurchasedModel;
import com.izhar.melsha.models.SoldModel;

public class Sell extends AppCompatActivity {
    private EditText code, name, type, model, size, total_quantity, avg_price, quantity, sold_price;
    private AutoCompleteTextView store;
    private Button submit;
    private ProgressBar progress;
    ItemModel item;
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
        setContentView(R.layout.form_sell_item);
        setTitle("Sell");
        item = (ItemModel) getIntent().getExtras().getSerializable("item");
        initialize();
        setData();
        getStores();
        submit.setOnClickListener(v -> {
            if (valid()) {
                if (getBranchQuantity() > 0 && getBranchQuantity() >= Integer.parseInt(quantity.getText().toString())){
                    progress.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    store.setEnabled(false);
                    sold_price.setEnabled(false);
                    quantity.setEnabled(false);
                    doSelling();
                }
                else {
                    Toast.makeText(this, "You don't have enough item on selected store", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean valid() {
        if (quantity.getText().toString().length() <= 0) {
            return false;
        }
        if (sold_price.getText().toString().length() <= 0) {
            return false;
        }
        return true;
    }

    private void setData() {
        code.setText(item.getCode());
        name.setText(item.getName());
        type.setText(String.valueOf(item.getType()));
        model.setText(String.valueOf(item.getModel()));
        size.setText(String.valueOf(item.getSize()));
        total_quantity.setText(String.valueOf(item.getQuantity()));
        avg_price.setText(String.valueOf(item.getAvg_price()));
    }

    private void getStores() {
        ArrayAdapter<CharSequence> stores = ArrayAdapter.createFromResource(this, R.array.stores, R.layout.list_item);
        store.setAdapter(stores);
    }

    private void initialize() {
        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        model = findViewById(R.id.model);
        size = findViewById(R.id.size);
        quantity = findViewById(R.id.quantity);
        avg_price = findViewById(R.id.avg_price);
        total_quantity = findViewById(R.id.total_quantity);
        store = findViewById(R.id.store);
        sold_price = findViewById(R.id.sold_price);
        submit = findViewById(R.id.submit);
        progress = findViewById(R.id.progress);
    }

    private int getBranchQuantity(){
        switch (store.getText().toString()){
            case "Dessie":
                return item.getDessie();
            case "Kore":
                return item.getKore();
            case "Jemmo":
                return item.getJemmo();
            default:
                return 0;
        }
    }

    Utils utils = new Utils();
    private void doSelling(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=doSelling"+
                "&code=" + item.getCode()+
                "&name=" + item.getName()+
                "&type=" + item.getType()+
                "&model=" + item.getModel()+
                "&quantity=" + item.getQuantity()+
                "&avg_price=" + item.getAvg_price()+
                "&branch=" + store.getText().toString()+
                "&sell_price=" + Integer.parseInt(sold_price.getText().toString())+
                "&sell_quantity=" + Integer.parseInt(quantity.getText().toString())+
                "&branch_quantity=" + getBranchQuantity(),
                response -> {

                        if (response.equalsIgnoreCase("success")){
                            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else {
                            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, ErrorActivity.class).putExtra("error", response));
                            progress.setVisibility(View.GONE);
                            submit.setVisibility(View.VISIBLE);
                        }

                }, error -> {
            System.out.println(error.getMessage());
            onBackPressed();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}