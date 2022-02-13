package com.izhar.melsha.ui.purchased;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.adapters.PurchasedAdapter;
import com.izhar.melsha.models.PurchasedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllPurchased extends Fragment {
    View root;
    private RecyclerView recycler;
    private TextView no_store;
    private List<PurchasedModel> purchases = new ArrayList<>();
    private PurchasedAdapter adapter;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_all_purchased, container, false);
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        no_store = root.findViewById(R.id.no_store);
        progress = root.findViewById(R.id.progress);
        getPurchased();
        return root;
    }

    void getPurchased() {
        Utils utils = new Utils();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getAllItemPurchased",
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            PurchasedModel purchased = new PurchasedModel();
                            purchased.setId(String.valueOf(object.get("id")));
                            purchased.setCode(object.getString("code"));
                            purchased.setName(object.getString("name"));
                            purchased.setType(object.getString("type"));
                            purchased.setModel(object.getString("model"));
                            purchased.setDate(object.getString("date"));
                            purchased.setStore(object.getString("branch"));
                            if (object.get("size").toString().equalsIgnoreCase("")) {
                                purchased.setSize("0");
                            } else {
                                purchased.setSize(object.getString("size"));
                            }
                            if (object.get("quantity").toString().equalsIgnoreCase("")) {
                                purchased.setQuantity(0);
                            } else {
                                purchased.setQuantity(object.getInt("quantity"));
                            }
                            if (object.get("pr_price").toString().equalsIgnoreCase("")) {
                                purchased.setPurchased_price(0);
                            } else {
                                purchased.setPurchased_price(object.getInt("pr_price"));
                            }

                            purchases.add(purchased);
                        }
                        adapter = new PurchasedAdapter(getContext(), purchases);
                        recycler.setAdapter(adapter);
                        progress.setVisibility(View.GONE);
                        if (purchases.size() == 0) {
                            no_store.setVisibility(View.VISIBLE);
                        }
                        else
                            no_store.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", e.toString()));
                    }

                }, error -> {
            Snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                    .setAction("Retry", v -> {
                        getPurchased();
                    })
                    .show();
            error.printStackTrace();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}