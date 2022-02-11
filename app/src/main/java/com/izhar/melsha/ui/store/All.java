package com.izhar.melsha.ui.store;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.adapters.SoldAdapter;
import com.izhar.melsha.models.SoldModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class All extends Fragment {

    View root;
    private RecyclerView recycler;
    private TextView no_store;
    private List<SoldModel> solds = new ArrayList<>();
    private SoldAdapter adapter;
    private ProgressBar progress;
    private TextView total_sell, total_profit;
    private int tot_sel=0, tot_profit=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_all, container, false);
        total_profit = root.findViewById(R.id.total_profit);
        total_sell = root.findViewById(R.id.total_sell);
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        no_store = root.findViewById(R.id.no_store);
        progress = root.findViewById(R.id.progress);
        getItems();
        return root;
    }

    void getItems() {
        Utils utils = new Utils();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getAllItemSold",
                response -> {
                    try {
                        solds.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            SoldModel sold = new SoldModel();
                            sold.setId(String.valueOf(object.get("id")));
                            sold.setCode(object.getString("code"));
                            sold.setName(object.getString("name"));
                            sold.setType(object.getString("type"));
                            sold.setModel(object.getString("model"));
                            sold.setDate(object.getString("date"));
                            sold.setFrom_store(object.getString("branch"));
                            if (object.get("size").toString().equalsIgnoreCase("") || object.get("size").toString().startsWith("#")) {
                                sold.setSize("0");
                            } else {
                                sold.setSize(object.getString("size"));
                            }
                            if (object.get("quantity").toString().equalsIgnoreCase("") || object.get("quantity").toString().startsWith("#")) {
                                sold.setQuantity(0);
                            } else {
                                sold.setQuantity(object.getInt("quantity"));
                            }
                            if (object.get("pr_price").toString().equalsIgnoreCase("") || object.get("pr_price").toString().startsWith("#")) {
                                sold.setPurchased_price(0);
                            } else {
                                sold.setPurchased_price(object.getInt("pr_price"));
                            }
                            if (object.get("sell_price").toString().equalsIgnoreCase("") || object.get("sell_price").toString().startsWith("#")) {
                                sold.setSold_price(0);
                            } else {
                                sold.setSold_price(object.getInt("sell_price"));
                                tot_sel+=object.getInt("sell_price");
                            }
                            if (object.get("profit").toString().equalsIgnoreCase("") || object.get("profit").toString().startsWith("#")) {
                                sold.setProfit(0);

                            } else {
                                sold.setProfit(object.getInt("profit"));
                                tot_profit += object.getInt("profit") * object.getInt("quantity");
                            }
                            solds.add(sold);
                        }
                        total_sell.setText("$ " + tot_sel);
                        total_profit.setText("$ " + tot_profit);
                        adapter = new SoldAdapter(getContext(), solds);
                        recycler.setAdapter(adapter);
                        progress.setVisibility(View.GONE);

                        if (solds.size() == 0)
                            no_store.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            error.printStackTrace();
            try {
                Snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getItems())
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    void getItemsByDate(String date) {
        Utils utils = new Utils();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getBranchItemSold&branch=Dessie" +
                "&date=" + date,
                response -> {
                    try {
                        solds.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            SoldModel sold = new SoldModel();
                            sold.setId(String.valueOf(object.get("id")));
                            sold.setCode(object.getString("code"));
                            sold.setName(object.getString("name"));
                            sold.setType(object.getString("type"));
                            sold.setModel(object.getString("model"));
                            sold.setDate(object.getString("date"));
                            sold.setFrom_store(object.getString("branch"));
                            if (object.get("size").toString().equalsIgnoreCase("") || object.get("size").toString().startsWith("#")) {
                                sold.setSize("0");
                            } else {
                                sold.setSize(object.getString("size"));
                            }
                            if (object.get("quantity").toString().equalsIgnoreCase("") || object.get("quantity").toString().startsWith("#")) {
                                sold.setQuantity(0);
                            } else {
                                sold.setQuantity(object.getInt("quantity"));
                            }
                            if (object.get("pr_price").toString().equalsIgnoreCase("") || object.get("pr_price").toString().startsWith("#")) {
                                sold.setPurchased_price(0);
                            } else {
                                sold.setPurchased_price(object.getInt("pr_price"));
                            }
                            if (object.get("sell_price").toString().equalsIgnoreCase("") || object.get("sell_price").toString().startsWith("#")) {
                                sold.setSold_price(0);
                            } else {
                                sold.setSold_price(object.getInt("sell_price"));
                                tot_sel+=object.getInt("sell_price");
                            }
                            if (object.get("profit").toString().equalsIgnoreCase("") || object.get("profit").toString().startsWith("#")) {
                                sold.setProfit(0);

                            } else {
                                sold.setProfit(object.getInt("profit"));
                                tot_profit += object.getInt("profit") * object.getInt("quantity");
                            }
                            solds.add(sold);
                        }
                        total_sell.setText("$ " + tot_sel);
                        total_profit.setText("$ " + tot_profit);
                        adapter = new SoldAdapter(getContext(), solds);
                        recycler.setAdapter(adapter);
                        progress.setVisibility(View.GONE);

                        if (solds.size() == 0)
                            no_store.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            error.printStackTrace();
            try {
                Snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getItems())
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    String today = "";
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.date){
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.date_picker);
            final DatePicker datePicker = dialog.findViewById(R.id.date_picker);
            dialog.findViewById(R.id.ok).setOnClickListener(v -> {
                String day = datePicker.getDayOfMonth() + "";
                String month = datePicker.getMonth()+1 + "";
                if (day.length() == 1 )
                    day = "0" + day;
                if (month.length() == 1 )
                    month = "0" + month;
                today = day + "-" + month + "-" + datePicker.getYear();
                Toast.makeText(getContext(), today, Toast.LENGTH_SHORT).show();
                //getItemsByDate(today);
                dialog.dismiss();
            });
            dialog.show();
        }
        return true;
    }
}