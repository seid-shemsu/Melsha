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
import com.izhar.melsha.MainActivity;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.models.ItemModel;
import com.izhar.melsha.models.PurchasedModel;

import java.util.ArrayList;
import java.util.List;

public class Purchase extends AppCompatActivity {
    private EditText code, name, type, model, size, quantity, purchased_price;
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
        setContentView(R.layout.form_purchase_item);
        setTitle("Purchase");
        initialize();
        getStores();
        item = (ItemModel) getIntent().getExtras().getSerializable("item");
        setData();
        submit.setOnClickListener(v -> {
            if (valid()) {
                submit.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                doPurchasing();
            } else {
                Toast.makeText(this, "invalid input", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean valid() {
        if (purchased_price.getText().toString().length() <= 0){
            return false;
        }
        if (store.getText().toString().length() <= 0){
            return false;
        }
        if (quantity.getText().toString().length() <= 0){
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
        purchased_price = findViewById(R.id.purchased_price);
        store = findViewById(R.id.store);
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
    private void doPurchasing(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=doPurchasing"+
                "&code=" + item.getCode()+
                "&name=" + item.getName()+
                "&type=" + item.getType()+
                "&model=" + item.getModel()+
                "&quantity=" + item.getQuantity()+
                "&avg_price=" + item.getAvg_price()+
                "&branch=" + store.getText().toString()+
                "&pr_quantity=" + Integer.parseInt(quantity.getText().toString())+
                "&pr_price=" + Integer.parseInt(purchased_price.getText().toString())+
                "&branch_quantity=" + getBranchQuantity(),
                response -> {

                    if (response.equalsIgnoreCase("success")){
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                    else {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
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