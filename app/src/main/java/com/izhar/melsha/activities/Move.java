package com.izhar.melsha.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.models.ItemModel;

public class Move extends AppCompatActivity {
    private EditText code, name, type, model, size, total_quantity, avg_price, quantity, sold_price;
    private AutoCompleteTextView from_store, to_store;
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
        branch = getSharedPreferences("user", MODE_PRIVATE).getString("branch", "Guest");
        setContentView(R.layout.activity_move);
        setTitle("Move");
        item = (ItemModel) getIntent().getExtras().getSerializable("item");
        initialize();
        setData();
        getStores();
        submit.setOnClickListener(v -> {
            if (valid()) {
                if (getBranchQuantity() > 0 && Integer.parseInt(quantity.getText().toString()) <= getBranchQuantity()){
                    progress.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    setMove();
                }

                else
                    Toast.makeText(this, "you don't have enough items on selected store", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMove() {
        Utils utils = new Utils();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=doMoving"+
                "&code=" + item.getCode()+
                "&name=" + item.getName()+
                "&type=" + item.getType()+
                "&model=" + item.getModel()+
                "&quantity=" + item.getQuantity()+
                "&avg_price=" + item.getAvg_price()+
                "&branchTo=" + getToStore()+
                "&branch=" + branch+
                "&sell_quantity=" + Integer.parseInt(quantity.getText().toString())+
                "&branch_quantity=" + getBranchQuantity(),
                response -> {
                    if (!response.startsWith("<")){
                        Toast.makeText(this, "Successfully transferred", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(this, ErrorActivity.class).putExtra("error", response));
                    }
                    onBackPressed();
                }, error -> {
            System.out.println(error.getMessage());
            startActivity(new Intent(this, ErrorActivity.class).putExtra("error", error.getMessage()));
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private String getToStore() {
        switch (from_store.getText().toString()) {
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

    private boolean valid() {
        if (quantity.getText().toString().length() <= 0) {
            Toast.makeText(this, "enter quantity", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (to_store.getText().toString().equalsIgnoreCase(from_store.getText().toString())) {
            Toast.makeText(this, "select different stores", Toast.LENGTH_SHORT).show();
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
        from_store.setAdapter(stores);
        to_store.setAdapter(stores);
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
        from_store = findViewById(R.id.from_store);
        to_store = findViewById(R.id.to_store);
        submit = findViewById(R.id.submit);
        progress = findViewById(R.id.progress);
    }

    private int getBranchQuantity() {
        switch (from_store.getText().toString()) {
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

}