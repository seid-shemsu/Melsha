package com.izhar.melsha.details;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.adapters.CreditedItemAdapter;
import com.izhar.melsha.models.CreditedItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreditedItems extends AppCompatActivity {

    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView no;

    List<CreditedItemModel> items = new ArrayList<>();
    CreditedItemAdapter adapter;
    String cn, from, action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_credited_items);

        cn = getIntent().getExtras().getString("cn");
        from = getIntent().getExtras().getString("from");

        setTitle("Credit " + cn);

        if (from.equalsIgnoreCase("loan")) {
            action = "getCreditItemsA&cna=" + cn;
        } else {
            action = "getCreditItemsB&cnb=" + cn;
        }

        progress = findViewById(R.id.progress);
        no = findViewById(R.id.no);
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        getItems();
    }

    Utils utils = new Utils();

    private void getItems() {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=" + action,
                response -> {
                    try {
                        items.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            CreditedItemModel item = new CreditedItemModel();
                            item.setItem_code(object.getString("Item_code"));
                            item.setPcode(object.getString("pcode"));
                            item.setQuantity(object.getString("quantity"));
                            items.add(item);
                        }
                        adapter = new CreditedItemAdapter(this, items);
                        recycler.setAdapter(adapter);
                        progress.setVisibility(View.GONE);
                        if (items.size() == 0)
                            no.setVisibility(View.VISIBLE);
                        else
                            no.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            error.printStackTrace();
            try {
                progress.setVisibility(View.GONE);
                Snackbar.make(progress, "Unable to load the data.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", v -> getItems())
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}