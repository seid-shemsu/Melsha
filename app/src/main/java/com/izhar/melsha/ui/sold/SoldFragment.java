package com.izhar.melsha.ui.sold;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.adapters.ItemsAdapter;
import com.izhar.melsha.models.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SoldFragment extends Fragment {
    View root;
    private RecyclerView recycler;
    private TextView no_store;
    private ProgressBar progress;
    private TextInputLayout text_field;
    private EditText search;
    private RelativeLayout relative;
    private List<ItemModel> items = new ArrayList<>();
    private ItemsAdapter adapter;
    Utils utils = new Utils();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_sold, container, false);
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        no_store = root.findViewById(R.id.no_store);
        progress = root.findViewById(R.id.progress);
        relative = root.findViewById(R.id.relative);
        text_field = root.findViewById(R.id.text_field);
        search = root.findViewById(R.id.search);
        text_field.setEndIconOnClickListener(v -> {
            getItem();
        });
        return root;
    }

    void getItem() {
        if (search.getText().toString().length() > 0) {
            relative.setVisibility(View.VISIBLE);
            recycler.removeAllViews();
            progress.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                    "?action=getSingleItem" +
                    "&code=" + search.getText().toString(),
                    response -> {
                        try {
                            items.clear();
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                ItemModel item = new ItemModel();
                                item.setCode(object.getString("code"));
                                item.setName(object.getString("name"));
                                item.setType(object.getString("type"));
                                item.setModel(object.getString("model"));
                                if (object.get("size").toString().equalsIgnoreCase("") || object.get("size").toString().startsWith("#")) {
                                    item.setSize("0");
                                } else {
                                    item.setSize((object.getString("size")));
                                }
                                if (object.get("quantity").toString().equalsIgnoreCase("") || object.get("quantity").toString().startsWith("#")) {
                                    item.setQuantity(0);
                                } else {
                                    item.setQuantity(object.getInt("quantity"));
                                }
                                if (object.get("avg_price").toString().equalsIgnoreCase("") || object.get("avg_price").toString().startsWith("#")) {
                                    item.setAvg_price(0);
                                } else {
                                    item.setAvg_price(object.getInt("avg_price"));
                                }
                                if (object.get("tot_capital").toString().equalsIgnoreCase("") || object.get("tot_capital").toString().startsWith("#")) {
                                    item.setTotal_capital(0);
                                } else {
                                    item.setTotal_capital(object.getInt("tot_capital"));
                                }
                                if (object.get("Jemmo").toString().equalsIgnoreCase("") || object.get("Jemmo").toString().startsWith("#")) {
                                    item.setJemmo(0);
                                } else {
                                    item.setJemmo(object.getInt("Jemmo"));
                                }
                                if (object.get("Dessie").toString().equalsIgnoreCase("") || object.get("Dessie").toString().startsWith("#")) {
                                    item.setDessie(0);

                                } else {
                                    item.setDessie(object.getInt("Dessie"));

                                }
                                if (object.get("Kore").toString().equalsIgnoreCase("") || object.get("Kore").toString().startsWith("#")) {
                                    item.setKore(0);
                                } else {
                                    item.setKore(object.getInt("Kore"));
                                }
                                items.add(item);
                            }
                            adapter = new ItemsAdapter(getContext(), items);
                            recycler.setAdapter(adapter);
                            progress.setVisibility(View.GONE);
                            if (items.size() == 0) {
                                no_store.setText("There is no item with code " + search.getText().toString());
                                no_store.setVisibility(View.VISIBLE);
                            } else
                                no_store.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, error -> {
                error.printStackTrace();
                try {
                    Snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                            .setAction("Retry", v -> {
                                getItem();
                            })
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(getContext(), "Please enter item code", Toast.LENGTH_SHORT).show();
        }
    }

}