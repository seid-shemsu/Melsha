package com.izhar.melsha.details;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.models.ItemModel;
import com.izhar.melsha.models.PurchasedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PurchasedDetail extends AppCompatActivity {
    EditText quantity, purchased_price, code, name, size, model, store, date;
    Button delete;
    PurchasedModel purchased;
    Utils utils = new Utils();

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
        setContentView(R.layout.activity_purchased_detail);
        purchased = (PurchasedModel) getIntent().getExtras().getSerializable("purchased");
        setTitle(purchased.getCode());
        quantity = findViewById(R.id.quantity);
        purchased_price = findViewById(R.id.purchased_price);
        store = findViewById(R.id.store);
        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        size = findViewById(R.id.size);
        model = findViewById(R.id.model);
        date = findViewById(R.id.date);
        delete = findViewById(R.id.delete);
        quantity.setText(String.valueOf(purchased.getQuantity()));
        purchased_price.setText(String.valueOf(purchased.getPurchased_price()));
        store.setText(String.valueOf(purchased.getStore()));
        code.setText(purchased.getCode());
        name.setText(purchased.getName());
        size.setText(String.valueOf(purchased.getSize()));
        model.setText(purchased.getModel());
        date.setText(purchased.getDate().substring(0, purchased.getDate().indexOf('T')));
        delete.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.confirmation);
            dialog.show();
            TextView message = dialog.findViewById(R.id.message);
            message.setText("Are you sure to delete this item?");
            ImageView icon = dialog.findViewById(R.id.icon);
            icon.setImageDrawable(this.getResources().getDrawable(R.drawable.delete));
            Button cancel = dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            Button delete = dialog.findViewById(R.id.confirm);
            delete.setText("Delete");
            delete.setOnClickListener(v1 -> {
                dialog.setContentView(R.layout.loading);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                        "?action=getSingleItem" +
                        "&code=" + purchased.getCode(),
                        response -> {
                            try {
                                JSONArray array = new JSONArray(response);
                                JSONObject object = array.getJSONObject(0);
                                int total_quantity = object.getInt(purchased.getStore());
                                if (total_quantity >= purchased.getQuantity()) {
                                    deletePurchased(purchased.getId());
                                    dialog.dismiss();
                                    finish();
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(this, "unable to delete", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }, error -> {
                    error.printStackTrace();
                    Snackbar.make(delete, "Unable to delete.", Snackbar.LENGTH_LONG)
                            .setAction("Ok", v2 -> {

                            })
                            .show();

                });
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            });

        });
    }

    private void deletePurchased(String id) {
        Utils utils = new Utils();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=deletePurchasedItem" +
                "&id=" + id,
                response -> {
                    Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show();

                }, error -> {
            System.out.println(error.getMessage());
            Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}