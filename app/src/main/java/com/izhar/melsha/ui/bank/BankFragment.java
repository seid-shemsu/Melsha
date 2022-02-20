package com.izhar.melsha.ui.bank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.izhar.melsha.adapters.BankAdapter;
import com.izhar.melsha.models.BankModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BankFragment newInstance(String param1, String param2) {
        BankFragment fragment = new BankFragment();
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
    private FloatingActionButton fab;
    private RecyclerView recycler;
    private TextView no_store;
    private ProgressBar progress;
    List<BankModel> banks = new ArrayList<>();
    BankAdapter adapter;
    Utils utils = new Utils();
    SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_bank, container, false);
        root.findViewById(R.id.transactions).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Transactions.class));
        });

        refresh = root.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(() -> getBanks());
        //refresh.setRefreshing(true);
        fab = root.findViewById(R.id.fab);
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        no_store = root.findViewById(R.id.no_store);
        progress = root.findViewById(R.id.progress);
        fab.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            dialog.setContentView(R.layout.new_bank);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            EditText code = dialog.findViewById(R.id.code);
            EditText name = dialog.findViewById(R.id.name);
            EditText type = dialog.findViewById(R.id.account);

            Button add = dialog.findViewById(R.id.submit);
            ProgressBar progress = dialog.findViewById(R.id.progress);
            add.setOnClickListener(v1 -> {
                if (code.getText().toString().length() > 0 && name.getText().toString().length() > 0 &&
                        type.getText().toString().length() > 0 ) {
                    code.setEnabled(false);
                    name.setEnabled(false);
                    type.setEnabled(false);
                    add.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    BankModel bank = new BankModel();
                    bank.setCode(code.getText().toString());
                    bank.setName(name.getText().toString());
                    bank.setAccount(type.getText().toString());
                    newBank(bank, dialog);
                } else
                    Toast.makeText(getContext(), "enter valid data", Toast.LENGTH_SHORT).show();
            });
        });
        String branch;
        branch = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "Guest");
        if (branch.equalsIgnoreCase("owner")) {
            getBanks();
            fab.setVisibility(View.VISIBLE);
        }
        else
            progress.setVisibility(View.GONE);
        return root;
    }

    void newBank(BankModel bank, BottomSheetDialog dialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=newBankAcc" +
                "&bank_code=" + bank.getCode() +
                "&bank_name=" + bank.getName() +
                "&account_no=" + bank.getAccount(),
                response -> {
                    if (response.startsWith("<")) {
                        System.out.println(response);
                        startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", response));
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }, error -> {
            error.printStackTrace();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    void getBanks() {
        recycler.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getBankList",
                response -> {
                    try {
                        if (!response.startsWith("<")) {
                            banks.clear();
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                BankModel bank = new BankModel();
                                bank.setCode(object.getString("code"));
                                bank.setName(object.getString("name"));
                                bank.setAccount(object.getString("acc"));
                                if (object.get("balance").toString().equalsIgnoreCase("") || object.get("balance").toString().startsWith("#")) {
                                    bank.setBalance(0);
                                } else {
                                    bank.setBalance((object.getInt("balance")));
                                }
                                banks.add(bank);
                            }
                            adapter = new BankAdapter(getContext(), banks);
                            recycler.setAdapter(adapter);
                            recycler.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            refresh.setRefreshing(false);
                            if (banks.size() == 0)
                                no_store.setVisibility(View.VISIBLE);
                            else
                                no_store.setVisibility(View.GONE);
                        }
                        else {
                            startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", response));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", e.toString()));
                    }

                }, error -> {
            error.printStackTrace();
            try {
                progress.setVisibility(View.GONE);
                snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getBanks())
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