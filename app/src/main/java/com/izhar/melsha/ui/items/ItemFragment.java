package com.izhar.melsha.ui.items;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.adapters.ItemsAdapter;
import com.izhar.melsha.adapters.LoanGiverAdapter;
import com.izhar.melsha.models.ItemModel;
import com.izhar.melsha.models.LoanGiverModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {
    View root;
    private FloatingActionButton fab;
    private RecyclerView recycler;
    private TextView no_store;
    private ProgressBar progress;
    private List<ItemModel> items = new ArrayList<>();
    private List<ItemModel> filteredItems = new ArrayList<>();
    private ItemsAdapter adapter;
    EditText search;
    Utils utils = new Utils();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_item, container, false);
        fab = root.findViewById(R.id.fab);
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        no_store = root.findViewById(R.id.no_store);
        progress = root.findViewById(R.id.progress);
        fab.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            dialog.setContentView(R.layout.form_add_item);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            EditText code = dialog.findViewById(R.id.code);
            EditText name = dialog.findViewById(R.id.name);
            EditText type = dialog.findViewById(R.id.type);
            EditText model = dialog.findViewById(R.id.model);
            EditText size = dialog.findViewById(R.id.size);
            Button add = dialog.findViewById(R.id.submit);
            ProgressBar progress = dialog.findViewById(R.id.progress);
            add.setOnClickListener(v1 -> {
                if (code.getText().toString().length() > 0 && name.getText().toString().length() > 0 &&
                        type.getText().toString().length() > 0 && model.getText().toString().length() > 0 &&
                        size.getText().toString().length() > 0) {
                    code.setEnabled(false);
                    name.setEnabled(false);
                    type.setEnabled(false);
                    model.setEnabled(false);
                    size.setEnabled(false);

                    add.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    ItemModel item = new ItemModel();
                    item.setCode(code.getText().toString());
                    item.setName(name.getText().toString());
                    item.setType(type.getText().toString());
                    item.setModel(model.getText().toString());
                    item.setSize((size.getText().toString()));
                    /*FirebaseDatabase.getInstance().getReference("Items").child(id).setValue(item)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Item Added", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });*/
                    newItems(item, dialog);
                } else
                    Toast.makeText(getContext(), "enter valid data", Toast.LENGTH_SHORT).show();
            });
        });

        search = root.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (search.getText().toString().isEmpty()) {
                    adapter = new ItemsAdapter(getContext(), items);
                } else {
                    filteredItems.clear();
                    for (ItemModel item : items) {
                        if (item.getModel().toLowerCase().contains(search.getText().toString().toLowerCase()) ||
                            item.getCode().toLowerCase().contains(search.getText().toString().toLowerCase()))
                            filteredItems.add(item);
                    }
                    adapter = new ItemsAdapter(getContext(), filteredItems);
                }
                recycler.removeAllViews();
                recycler.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getItems();
        return root;
    }

    void getItems() {
        recycler.removeAllViews();
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getItems",
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                        startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", e.toString()));
                    }

                }, error -> {
            error.printStackTrace();
            try {
                progress.setVisibility(View.GONE);
                snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getItems())
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    Snackbar snackbar;

    void newItems(ItemModel item, BottomSheetDialog dialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=newItems" +
                "&code=" + item.getCode() +
                "&name=" + item.getName() +
                "&type=" + item.getType() +
                "&model=" + item.getModel() +
                "&size=" + item.getSize(),
                response -> {
                    if (response.contains("Successfully")) {
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            error.printStackTrace();
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            snackbar.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}