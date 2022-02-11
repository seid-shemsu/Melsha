package com.izhar.melsha.ui.loan.customers;

import android.app.Dialog;
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
import com.google.android.material.textfield.TextInputLayout;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.models.ItemModel;
import com.izhar.melsha.models.LoanTakerModel;
import com.izhar.melsha.models.PurchasedModel;
import com.izhar.melsha.models.SoldModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GiveLoan extends AppCompatActivity {
    EditText credit_number, person_code, person_phone, person_name, quantity, amount;
    LoanTakerModel taker;
    FloatingActionButton fab;
    Button submit;
    LinearLayout linear;
    Map<String, ItemModel> items = new HashMap<>();
    Map<String, SoldModel> soldModelMap = new HashMap<>();
    JSONArray jSold;
    ArrayAdapter<CharSequence> stores;
    AutoCompleteTextView store;
    Dialog dialog;
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
        setTitle("Give Credit");

        dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);

        stores = ArrayAdapter.createFromResource(this, R.array.stores, R.layout.list_item);
        taker = (LoanTakerModel) getIntent().getExtras().getSerializable("taker");
        store = findViewById(R.id.store);
        store.setAdapter(stores);
        person_code = findViewById(R.id.person_code);
        person_name = findViewById(R.id.person_name);
        person_phone = findViewById(R.id.person_phone);
        quantity = findViewById(R.id.quantity);
        person_code.setText(taker.getCode());
        person_name.setText(taker.getName());
        person_phone.setText(taker.getPhone());
        amount = findViewById(R.id.amount);
        linear = findViewById(R.id.linear);
        fab = findViewById(R.id.fab);
        submit = findViewById(R.id.submit);
        fab.setOnClickListener(v -> {
            View item = getLayoutInflater().inflate(R.layout.single_take_loan, null, false);
            ImageView cancel = item.findViewById(R.id.cancel);
            EditText code = item.findViewById(R.id.code);
            ImageView check = item.findViewById(R.id.check);
            check.setOnClickListener(v1 -> {
                code.setEnabled(false);
                addItems(code.getText().toString(), check, code);
                check.setVisibility(View.GONE);
            });
            linear.addView(item);
            cancel.setOnClickListener(v2 -> {
                items.remove(code.getText().toString());
                soldModelMap.remove(code.getText().toString());
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
                            getSold();
                            giveLoan(person_code.getText().toString(),
                                    person_name.getText().toString(),
                                    person_phone.getText().toString(),
                                    amount.getText().toString(), quantity.getText().toString(), array.toString(), jSold.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void getSold() throws JSONException {
        for (int i = 0; i < linear.getChildCount(); i++) {
            View view = linear.getChildAt(i);
            EditText code, amount, quantity;
            code = view.findViewById(R.id.code);
            amount = view.findViewById(R.id.amount);
            quantity = view.findViewById(R.id.quantity);
            SoldModel soldModel = new SoldModel();
            soldModel.setFrom_store(store.getText().toString());
            soldModel.setSold_price(Integer.parseInt(amount.getText().toString()));
            soldModel.setQuantity(Integer.parseInt(quantity.getText().toString()));
            soldModelMap.put(code.getText().toString(), soldModel);
        }
        Set<String> keys = items.keySet();
        jSold = new JSONArray();
        for (String key : keys) {
            ItemModel item = items.get(key);
            SoldModel soldModel = soldModelMap.get(key);
            JSONObject object = new JSONObject();
            object.put("code", item.getCode());
            object.put("name", item.getName());
            object.put("model", item.getModel());
            object.put("type", item.getType());
            object.put("size", item.getSize());
            object.put("quantity", item.getQuantity());
            object.put("avg_price", item.getAvg_price());
            object.put("branch_quantity", getBranchQuantity(item, soldModel.getFrom_store()));
            object.put("sell_price", soldModel.getSold_price());
            object.put("sell_quantity", soldModel.getQuantity());
            object.put("branch", soldModel.getFrom_store());
            jSold.put(object);
        }
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
                            icon.setImageDrawable(getResources().getDrawable(R.drawable.help));
                            items.put(co, null);
                            code.setEnabled(true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        items.put(co, null);
                    }
                }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "network error", Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private boolean isValid() {
        if (person_name.getText().toString().isEmpty()){
            Toast.makeText(this, "specify persons' name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (person_code.getText().toString().isEmpty()){
            Toast.makeText(this, "specify persons' code", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (amount.getText().toString().isEmpty()){
            Toast.makeText(this, "specify credit amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (store.getText().toString().isEmpty()){
            Toast.makeText(this, "specify store", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (linear.getChildCount() == 0){
            Toast.makeText(this, "add some item", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (items.size() == 0){
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

    private boolean isValid3(){
        int tot_amount = 0, tot_quantity = 0;
        for (int i = 0; i < linear.getChildCount(); i++) {
            View view = linear.getChildAt(i);
            EditText amount, quantity;
            amount = view.findViewById(R.id.amount);
            quantity = view.findViewById(R.id.quantity);
            tot_amount += Integer.parseInt(amount.getText().toString()) * Integer.parseInt(quantity.getText().toString());
            tot_quantity += Integer.parseInt(quantity.getText().toString());
        }
        if (tot_quantity != Integer.parseInt(quantity.getText().toString())){
            Toast.makeText(this, "biased quantity", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tot_amount != Integer.parseInt(amount.getText().toString())){
            Toast.makeText(this, "biased amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
    private void giveLoan(String code, String name, String phone, String amount, String quantity, String items, String jSold){
        String link = utils.getUrl(this) + "?action=takingLoanA" +
                "&pcode=" + code +
                "&pname=" + name +
                "&pphone=" + phone +
                "&ca_amount=" + amount +
                "&ca_quantity=" + quantity +
                "&JSB=" + items +
                "&jSold=" + jSold;
        System.out.println(link);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=givingLoan"+
                "&pcode=" + code+
                "&pname=" + name+
                "&pphone=" + phone+
                "&cb_amount=" + amount+
                "&cb_quantity=" + quantity+
                "&JSB=" + items+
                "&jSold=" + jSold,
                response -> {
                    if (response.contains("Successfully")){
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        onBackPressed();
                    }
                    else {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                        dialog.dismiss();
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