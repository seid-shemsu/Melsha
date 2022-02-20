package com.izhar.melsha.ui.bank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.adapters.TransactionAdapter;
import com.izhar.melsha.models.BankModel;
import com.izhar.melsha.models.TransactionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WithdrawFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WithdrawFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WithdrawFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WithdrawFragment newInstance(String param1, String param2) {
        WithdrawFragment fragment = new WithdrawFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View root;
    Utils utils = new Utils();
    TextInputLayout search_by;
    EditText search;
    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView no;
    List<TransactionModel> transactions = new ArrayList<>();
    List<TransactionModel> filteredTransactions = new ArrayList<>();
    TransactionAdapter adapter;
    BankModel bank;
    String branch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_payed, container, false);
        branch = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "Guest");

        search = root.findViewById(R.id.search);
        search_by = root.findViewById(R.id.search_by);

        progress = root.findViewById(R.id.progress);
        no = root.findViewById(R.id.no);
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (search.getText().toString().isEmpty())
                    adapter = new TransactionAdapter(getContext(), transactions);
                else {
                    filteredTransactions.clear();
                    for (TransactionModel transaction : transactions) {
                        if (transaction.getCode().toLowerCase().contains(search.getText().toString().toLowerCase()))
                            filteredTransactions.add(transaction);
                    }
                    adapter = new TransactionAdapter(getContext(), filteredTransactions);
                }
                recycler.removeAllViews();
                recycler.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getTransactions();
        return root;
    }

    private void getTransactions() {
        recycler.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getWithdraw" +
                "&branch=" + branch,
                response -> {
                    try {
                        transactions.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            TransactionModel transaction = new TransactionModel();
                            transaction.setId(object.getString("WN"));
                            transaction.setCode(object.getString("code"));
                            transaction.setBank(object.getString("name"));
                            transaction.setDate(object.getString("date"));
                            transaction.setType("Withdraw");
                            if (object.get("amount").toString().equalsIgnoreCase("") || object.get("amount").toString().startsWith("#")) {
                                transaction.setAmount(0);
                            } else {
                                transaction.setAmount((object.getInt("amount")));
                            }
                            transactions.add(transaction);
                        }
                        adapter = new TransactionAdapter(getContext(), transactions);
                        recycler.setAdapter(adapter);
                        progress.setVisibility(View.GONE);
                        recycler.setVisibility(View.VISIBLE);
                        if (transactions.isEmpty())
                            no.setVisibility(View.VISIBLE);
                        else
                            no.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", e.toString()));
                    }

                }, error -> {
            error.printStackTrace();
            try {
                progress.setVisibility(View.GONE);
                snackbar.make(progress, "Unable to load the data.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getTransactions())
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    Snackbar snackbar;
}