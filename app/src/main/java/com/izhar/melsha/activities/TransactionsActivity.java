package com.izhar.melsha.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.izhar.melsha.adapters.TransactionAdapter;
import com.izhar.melsha.models.BankModel;
import com.izhar.melsha.models.TransactionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView no;
    List<TransactionModel> transactions = new ArrayList<>();
    TransactionAdapter adapter;
    BankModel bank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_transactions);

        bank = (BankModel) getIntent().getExtras().getSerializable("bank");
        setTitle(bank.getName() + " Transaction");
        progress = findViewById(R.id.progress);
        no = findViewById(R.id.no);
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        getTransactions("getDeposits&code=" + bank.getCode());
    }
    Utils utils = new Utils();
    private void getTransactions(final String action){
        recycler.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=" + action,
                response -> {
                    try {
                        transactions.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            TransactionModel transaction = new TransactionModel();
                            transaction.setBank(object.getString("bank"));
                            transaction.setDate(object.getString("date"));
                            transaction.setId(object.getString("id"));
                            transaction.setType(object.getString("type"));
                            if (object.get("amount").toString().equalsIgnoreCase("") || object.get("amount").toString().startsWith("#")) {
                                transaction.setAmount(0);
                            } else {
                                transaction.setAmount((object.getInt("amount")));
                            }
                            transactions.add(transaction);
                        }
                        adapter = new TransactionAdapter(this, transactions);
                        recycler.setAdapter(adapter);
                        progress.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        startActivity(new Intent(this, ErrorActivity.class).putExtra("error", e.toString()));
                    }

                }, error -> {
            error.printStackTrace();
            try {
                progress.setVisibility(View.GONE);
                snackbar.make(progress, "Unable to load the data.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getTransactions(action))
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue((this));
        requestQueue.add(stringRequest);
    }
    Snackbar snackbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deposit){
            //getTransactions("getDeposits&code=" + bank.getCode());
            Toast.makeText(this, "deposit", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.withdraw){
            //getTransactions("getWithdraws&code=" + bank.getCode());
            Toast.makeText(this, "withdraw", Toast.LENGTH_SHORT).show();
        }
        else
            onBackPressed();
        return true;
    }
}