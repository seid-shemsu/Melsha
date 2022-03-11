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
    String branch;
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
        branch = getSharedPreferences("user", MODE_PRIVATE).getString("branch", "Guest");
        if (branch.equalsIgnoreCase("owner"))
            findViewById(R.id.branch_card).setVisibility(View.VISIBLE);
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
        if (quantity.getText().toString().length() <= 0){
            return false;
        }
        if (branch.equalsIgnoreCase("owner")){
            if (store.getText().toString().length() == 0)
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
        String b = branch.equalsIgnoreCase("owner") ? getToStore() : branch;
        switch (b){
            case "ሱቅ 3":
                return item.getDessie();
            case "ሱቅ 2":
                return item.getKore();
            case "ሱቅ 1":
                return item.getJemmo();
            default:
                return 0;
        }
    }

    Utils utils = new Utils();
    private void doPurchasing(){
        System.out.println("-----------------------------------------------------------BRANCH IS -------------------------------------------------------------- " + getBranch());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=doPurchasing"+
                "&code=" + item.getCode()+
                "&name=" + item.getName()+
                "&type=" + item.getType()+
                "&model=" + item.getModel()+
                "&quantity=" + item.getQuantity()+
                "&avg_price=" + item.getAvg_price()+
                "&branch=" + getBranch() +
                "&pr_quantity=" + Integer.parseInt(quantity.getText().toString())+
                "&pr_price=" + Integer.parseInt(purchased_price.getText().toString())+
                "&branch_quantity=" + getBranchQuantity(),
                response -> {
                    if (response.startsWith("<")){
                        startActivity(new Intent(this, ErrorActivity.class).putExtra("error", response));
                        progress.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                        onBackPressed();
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

    private String getBranch() {
        return branch.equalsIgnoreCase("owner") ? getToStore() : branch;
    }
    private String getToStore() {
        switch (store.getText().toString()) {
            case "ሱቅ 3":
                return "Kore";
            case "ሱቅ 2":
                return "Dessie";
            case "ሱቅ 1":
                return "Jemmo";
            default:
                return "";
        }
    }

}