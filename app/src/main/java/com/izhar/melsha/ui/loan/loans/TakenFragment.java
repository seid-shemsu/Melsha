package com.izhar.melsha.ui.loan.loans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.izhar.melsha.adapters.LoanAdapter;
import com.izhar.melsha.models.LoanModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TakenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TakenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TakenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TakenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TakenFragment newInstance(String param1, String param2) {
        TakenFragment fragment = new TakenFragment();
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
    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView no;
    List<LoanModel> loans = new ArrayList<>();
    List<LoanModel> filteredLoans = new ArrayList<>();
    LoanAdapter adapter;

    TextInputLayout search_by;
    EditText search;
    String action = "getAllCreditTaking", branch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        root = inflater.inflate(R.layout.fragment_taken, container, false);
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

                if (search.getText().toString().isEmpty()) {
                    adapter = new LoanAdapter(getContext(), loans, "loan");
                } else {
                    filteredLoans.clear();
                    for (LoanModel loan : loans) {
                        if (loan.getPerson_code().toLowerCase().contains(search.getText().toString().toLowerCase()) || loan.getCredit_no().toLowerCase().contains(search.getText().toString().toLowerCase()))
                            filteredLoans.add(loan);
                    }
                    adapter = new LoanAdapter(getContext(), filteredLoans, "loan");
                }
                recycler.removeAllViews();
                recycler.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getTaken();
        return root;
    }

    Utils utils = new Utils();

    private void getTaken() {
        recycler.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=" + action +
                "&branch=" + branch,
                response -> {
                    try {
                        loans.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            LoanModel loan = new LoanModel();
                            loan.setCredit_no(object.getString("cna"));
                            loan.setPerson_code(object.getString("pcode"));
                            loan.setPerson_name(object.getString("pname"));
                            loan.setQuantity(object.getString("quantity"));
                            loan.setAmount(object.getString("amount"));
                            loan.setPayed(object.getString("payed"));
                            loan.setLeft(object.getString("left"));
                            loan.setDate(object.getString("date"));
                            loans.add(loan);
                        }
                        adapter = new LoanAdapter(getContext(), loans, "loan");
                        recycler.setAdapter(adapter);
                        recycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        if (loans.size() == 0)
                            no.setVisibility(View.VISIBLE);
                        else
                            no.setVisibility(View.GONE);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            error.printStackTrace();
            try {
                startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", error.getMessage()));
                progress.setVisibility(View.GONE);
                Snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getTaken())
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh)
            getTaken();
        return false;
    }

}