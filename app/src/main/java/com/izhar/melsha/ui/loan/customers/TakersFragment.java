package com.izhar.melsha.ui.loan.customers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.adapters.LoanTakerAdapter;
import com.izhar.melsha.models.LoanTakerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TakersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TakersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TakersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Takers.
     */
    // TODO: Rename and change types and number of parameters
    public static TakersFragment newInstance(String param1, String param2) {
        TakersFragment fragment = new TakersFragment();
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
    List<LoanTakerModel> takers = new ArrayList<>();
    LoanTakerAdapter adapter;
    List<LoanTakerModel> filteredTakers = new ArrayList<>();
    EditText search;
    String branch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        root = inflater.inflate(R.layout.fragment_takers, container, false);
        branch = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "Guest");
        root.findViewById(R.id.fab).setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.form_loan_new);
            dialog.show();
            TextView actor = dialog.findViewById(R.id.actor);
            actor.setText("New Loan Taker");
            EditText code, name, phone;
            Button reg = dialog.findViewById(R.id.save);
            code = dialog.findViewById(R.id.code);
            getCode(code);
            name = dialog.findViewById(R.id.name);
            phone = dialog.findViewById(R.id.phone);
            reg.setOnClickListener(v1 -> {
                if (name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || code.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Invalid form", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setContentView(R.layout.loading);
                    dialog.setCancelable(false);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                            "?action=newLoanTaker" +
                            "&pcode=" + code.getText().toString() +
                            "&pname=" + name.getText().toString().replace(" ", "_") +
                            "&pphone=" + phone.getText().toString().trim() +
                            "&branch=" + branch,
                            response -> {

                                if (response.contains("Successfully")) {
                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                    getTakers();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                                    System.out.println(response);
                                    dialog.setContentView(R.layout.form_loan_new);
                                    dialog.setCancelable(true);
                                }

                            }, error -> {
                        System.out.println(error.getMessage());
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest);
                }
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
                    adapter = new LoanTakerAdapter(getContext(), takers);
                } else {
                    filteredTakers.clear();
                    for (LoanTakerModel taker : takers) {
                        try {
                            if (taker.getLeft() >= Integer.parseInt(search.getText().toString())){
                                filteredTakers.add(taker);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (taker.getName().toLowerCase().contains(search.getText().toString().toLowerCase())){
                            filteredTakers.add(taker);
                        }
                    }
                    adapter = new LoanTakerAdapter(getContext(), filteredTakers);
                }
                recycler.removeAllViews();
                recycler.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        progress = root.findViewById(R.id.progress);
        no = root.findViewById(R.id.no_data);
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        getTakers();
        return root;
    }

    Utils utils = new Utils();

    private void getCode(EditText code) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getTakerPersonCode" +
                "&branch=" + branch,
                response -> {
                    if (response.startsWith("<")){
                        System.out.println(response);
                    }
                    else
                        code.setText(response);
                }, error -> {
            System.out.println(error.getMessage());
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getTakers(){
        progress.setVisibility(View.VISIBLE);
        recycler.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getAllLoanTaker" +
                "&branch=" + branch,
                response -> {
                    try {
                        takers.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            LoanTakerModel taker = new LoanTakerModel();
                            taker.setCode(object.getString("pcode"));
                            taker.setName(object.getString("pname"));
                            taker.setPhone(object.getString("pphone"));
                            if (object.getString("amount").equalsIgnoreCase(""))
                                taker.setTotal(0);
                            else
                                taker.setTotal(object.getInt("amount"));
                            if (object.getString("left").equalsIgnoreCase(""))
                                taker.setLeft(0);
                            else
                                taker.setLeft(object.getInt("left"));
                            if (object.getString("payed").equalsIgnoreCase(""))
                                taker.setReturned(0);
                            else
                                taker.setReturned(object.getInt("payed"));
                            takers.add(taker);
                        }
                        adapter = new LoanTakerAdapter(getContext(), takers);
                        recycler.setAdapter(adapter);
                        recycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        if (takers.size() == 0)
                            no.setVisibility(View.VISIBLE);
                        else
                            no.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            error.printStackTrace();
            try {
                startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", error.getMessage()));
                progress.setVisibility(View.GONE);
                Snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getTakers())
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
            getTakers();
        return false;
    }

}
