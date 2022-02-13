package com.izhar.melsha.ui.loan.customers;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.models.ItemModel;
import com.izhar.melsha.models.LoanGiverModel;
import com.izhar.melsha.models.PurchasedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TakeLoan extends AppCompatActivity {
    EditText credit_number, person_code, person_phone, person_name, quantity, amount;
    LoanGiverModel giver;
    FloatingActionButton fab;
    Button submit;
    LinearLayout linear;
    ArrayAdapter<CharSequence> stores;
    JSONArray jPurchased;
    Dialog dialog;
    Map<String, ItemModel> items = new HashMap<>();
    Map<String, PurchasedModel> purchasedModelMap = new HashMap<>();
    AutoCompleteTextView store;

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
        setContentView(R.layout.form_loan_take);
        setTitle("Take Loan");
        dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading);
        stores = ArrayAdapter.createFromResource(this, R.array.stores, R.layout.list_item);
        store = findViewById(R.id.store);
        store.setAdapter(stores);
        giver = (LoanGiverModel) getIntent().getExtras().get("giver");
        person_code = findViewById(R.id.person_code);
        person_name = findViewById(R.id.person_name);
        person_phone = findViewById(R.id.person_phone);
        quantity = findViewById(R.id.quantity);
        person_code.setText(giver.getCode());
        person_name.setText(giver.getName());
        person_phone.setText(giver.getPhone());
        amount = findViewById(R.id.amount);
        linear = findViewById(R.id.linear);
        fab = findViewById(R.id.fab);
        submit = findViewById(R.id.submit);
        fab.setOnClickListener(v -> {
            View item = getLayoutInflater().inflate(R.layout.single_take_loan, null, false);
            ImageView cancel = item.findViewById(R.id.cancel);
            EditText code = item.findViewById(R.id.code);
            ImageView check = item.findViewById(R.id.check);

            linear.addView(item);
            check.setOnClickListener(v1 -> {
                code.setEnabled(false);
                addItems(code.getText().toString(), check, code);
            });
            cancel.setOnClickListener(v2 -> {
                items.remove(code.getText().toString());
                purchasedModelMap.remove(code.getText().toString());
                linear.removeView(item);
            });
        });

        submit.setOnClickListener(v -> {
            if (isValid()) {
                if (isValid2()) {
                    if (isValid3()) {
                        try {
                            JSONArray array = new JSONArray();
                            for (int i = 0; i < linear.getChildCount(); i++) {
                                View view = linear.getChildAt(i);
                                EditText code, amount, quantity;
                                code = view.findViewById(R.id.code);
                                amount = view.findViewById(R.id.amount);
                                quantity = view.findViewById(R.id.quantity);

                                String co = code.getText().toString();
                                String am = amount.getText().toString();
                                String qua = quantity.getText().toString();
                                String st = store.getText().toString();
                                JSONObject item = new JSONObject();
                                item.put("code", co);
                                item.put("quantity", Integer.parseInt(qua));
                                item.put("pr_price", am);
                                item.put("pr_branch", st);
                                array.put(item);
                            }
                            getPurchased();
                            takeLoan(person_code.getText().toString(),
                                    person_name.getText().toString(),
                                    person_phone.getText().toString(),
                                    amount.getText().toString(), quantity.getText().toString(), array.toString(), jPurchased.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void getPurchased() throws JSONException {
        for (int i = 0; i < linear.getChildCount(); i++) {
            View view = linear.getChildAt(i);
            EditText code, amount, quantity;
            code = view.findViewById(R.id.code);
            amount = view.findViewById(R.id.amount);
            quantity = view.findViewById(R.id.quantity);
            PurchasedModel purchasedModel = new PurchasedModel();
            purchasedModel.setStore(store.getText().toString());
            purchasedModel.setPurchased_price(Integer.parseInt(amount.getText().toString()));
            purchasedModel.setQuantity(Integer.parseInt(quantity.getText().toString()));
            purchasedModelMap.put(code.getText().toString(), purchasedModel);
        }
        Set<String> keys = items.keySet();
        jPurchased = new JSONArray();
        for (String key : keys) {
            ItemModel item = items.get(key);
            PurchasedModel purchasedModel = purchasedModelMap.get(key);
            JSONObject object = new JSONObject();
            object.put("code", item.getCode());
            object.put("name", item.getName());
            object.put("model", item.getModel());
            object.put("type", item.getType());
            object.put("size", item.getSize());
            object.put("quantity", item.getQuantity());
            object.put("avg_price", item.getAvg_price());
            object.put("branch_quantity", getBranchQuantity(item, purchasedModel.getStore()));
            object.put("pr_price", purchasedModel.getPurchased_price());
            object.put("pr_quantity", purchasedModel.getQuantity());
            object.put("branch", purchasedModel.getStore());
            jPurchased.put(object);
        }
    }

    private boolean isValid() {
        if (person_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "specify persons' name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (person_code.getText().toString().isEmpty()) {
            Toast.makeText(this, "specify persons' code", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (amount.getText().toString().isEmpty()) {
            Toast.makeText(this, "specify credit amount", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (store.getText().toString().isEmpty()) {
            Toast.makeText(this, "specify store", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (linear.getChildCount() == 0) {
            Toast.makeText(this, "add some item", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (items.size() == 0) {
            Toast.makeText(this, "add some item", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < linear.getChildCount(); i++) {
            View view = linear.getChildAt(i);
            EditText code, amount, quantity;
            code = view.findViewById(R.id.code);
            amount = view.findViewById(R.id.amount);
            quantity = view.findViewById(R.id.quantity);

            String co = code.getText().toString();
            String am = amount.getText().toString();
            String qua = quantity.getText().toString();
            if (co.isEmpty() || am.isEmpty() || qua.isEmpty()) {
                Toast.makeText(this, "invalid items' data found", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean isValid2() {
        Set<String> keys = items.keySet();
        for (String key : keys) {
            if (items.get(key) == null) {
                Snackbar.make(store, "item " + key + " is invalid item", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Remove", v -> {
                            items.remove(key);
                        })
                        .show();
                return false;
            }
        }
        return true;
    }

    private boolean isValid3() {
        int tot_amount = 0, tot_quantity = 0, tot_amount1 = 0, tot_quantity1 = 0;
        for (int i = 0; i < linear.getChildCount(); i++) {
            View view = linear.getChildAt(i);
            EditText amount, quantity;
            amount = view.findViewById(R.id.amount);
            quantity = view.findViewById(R.id.quantity);
            tot_amount += Integer.parseInt(amount.getText().toString()) * Integer.parseInt(quantity.getText().toString());
            tot_quantity += Integer.parseInt(quantity.getText().toString());
        }
        if (tot_quantity != Integer.parseInt(quantity.getText().toString())) {
            Toast.makeText(this, "biased quantity", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tot_amount != Integer.parseInt(amount.getText().toString())) {
            Toast.makeText(this, "biased amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addItems(String co, ImageView icon, EditText code) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=getSingleItem" +
                "&code=" + co,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        if (array.length() > 0) {
                            ItemModel item = new ItemModel();
                            JSONObject object = array.getJSONObject(0);
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
                            items.put(co, item);
                            icon.setImageDrawable(getResources().getDrawable(R.drawable.check));

                        } else {
                            items.put(co, null);
                            code.setEnabled(true);
                            icon.setImageDrawable(getResources().getDrawable(R.drawable.help));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        code.setEnabled(true);
                        items.put(co, null);
                    }
                }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "network error", Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private int getBranchQuantity(ItemModel item, String st) {

        switch (st) {
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

    private void takeLoan(String code, String name, String phone, String amount, String quantity, String items, String jPurchased) {
        String link = utils.getUrl(this) + "?action=takingLoanA" +
                "&pcode=" + code +
                "&pname=" + name +
                "&pphone=" + phone +
                "&ca_amount=" + amount +
                "&ca_quantity=" + quantity +
                "&JSA=" + items +
                "&jPurchased=" + jPurchased;
        System.out.println(link);
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=takingLoanA" +
                "&pcode=" + code +
                "&pname=" + name +
                "&pphone=" + phone +
                "&ca_amount=" + amount +
                "&ca_quantity=" + quantity +
                "&JSA=" + items +
                "&jPurchased=" + jPurchased,
                response -> {
                    if (response.contains("Successfully")) {
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        onBackPressed();
                    } else {
                        System.out.println(response);
                        startActivity(new Intent(this, ErrorActivity.class).putExtra("error", response));
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }, error -> {

            startActivity(new Intent((this), ErrorActivity.class).putExtra("error", error.getMessage()));
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