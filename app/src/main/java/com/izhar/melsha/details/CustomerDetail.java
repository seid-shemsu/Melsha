package com.izhar.melsha.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.LoanGiverAdapter;
import com.izhar.melsha.adapters.LoanAdapter;
import com.izhar.melsha.models.LoanGiverModel;
import com.izhar.melsha.models.LoanModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetail extends AppCompatActivity {

    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView no;
    List<LoanModel> loans = new ArrayList<>();
    LoanAdapter adapter;

    String pcode, from, action, detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_customer_detail);
        pcode = getIntent().getExtras().getString("pcode");
        from = getIntent().getExtras().getString("from");
        setTitle("Customer " + pcode);

        if (from.equalsIgnoreCase("giver")){
            action = "searchByGiverPersonCode&pcode=" + pcode;
            detail = "loan";
        }
        else {
            action = "searchByTakerPersonCode&pcode=" + pcode;
            detail = "credit";
        }
        progress = findViewById(R.id.progress);
        no = findViewById(R.id.no);
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        getTaken();
    }

    Utils utils = new Utils();
    private void getTaken(){
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=" + action,
                response -> {
                    try {
                        loans.clear();

                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            LoanModel loan = new LoanModel();
                            if (from.equalsIgnoreCase("giver"))
                                loan.setCredit_no(object.getString("cna"));
                            else
                                loan.setCredit_no(object.getString("cnb"));
                            loan.setPerson_code(object.getString("pcode"));
                            loan.setPerson_name(object.getString("pname"));
                            loan.setQuantity(object.getString("quantity"));
                            loan.setAmount(object.getString("amount"));
                            loan.setPayed(object.getString("payed"));
                            loan.setLeft(object.getString("left"));
                            loan.setDate(object.getString("date"));
                            loans.add(loan);
                        }
                        adapter = new LoanAdapter(this, loans, detail);
                        recycler.setAdapter(adapter);
                        progress.setVisibility(View.GONE);
                        if (loans.size() == 0)
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
                        .setAction("Retry", v -> getTaken())
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