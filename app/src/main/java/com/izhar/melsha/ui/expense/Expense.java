package com.izhar.melsha.ui.expense;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.adapters.BankAdapter;
import com.izhar.melsha.adapters.ExpenseAdapter;
import com.izhar.melsha.models.BankModel;
import com.izhar.melsha.models.ExpenseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Expense#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Expense extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Expense() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Expense.
     */
    // TODO: Rename and change types and number of parameters
    public static Expense newInstance(String param1, String param2) {
        Expense fragment = new Expense();
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
    FloatingActionButton fab;
    List<ExpenseModel> expenses = new ArrayList<>();
    List<ExpenseModel> expenses1 = new ArrayList<>();
    Dialog dialog;
    private ExpenseAdapter adapter;
    private RecyclerView recycler;
    private TextView no_store;
    private ProgressBar progress;
    String branch;
    SwipeRefreshLayout refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        branch = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "guest");
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_expense, container, false);
        refresh = root.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(() -> getExpenses());
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        no_store = root.findViewById(R.id.no_store);
        progress = root.findViewById(R.id.progress);
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            dialog.setContentView(R.layout.add_expenses);
            dialog.show();
            dialog.setCancelable(true);
            FloatingActionButton fab1 = dialog.findViewById(R.id.fab);
            LinearLayout linear;
            Button submit;
            linear = dialog.findViewById(R.id.linear);
            submit = dialog.findViewById(R.id.submit);

            fab1.setOnClickListener(v1 -> {
                View item = getLayoutInflater().inflate(R.layout.single_expense, null, false);
                linear.addView(item);
            });

            submit.setOnClickListener(v1 -> {
                boolean valid = true;
                for (int i = 0; i < linear.getChildCount(); i++) {
                    View view = linear.getChildAt(i);
                    EditText type, amount;
                    type = view.findViewById(R.id.type);
                    amount = view.findViewById(R.id.amount);
                    String t = type.getText().toString();
                    String a = amount.getText().toString();
                    if (t.isEmpty() || a.isEmpty())
                        valid = false;
                    ExpenseModel expense = new ExpenseModel();
                    expense.setReason(t);
                    expenses1.add(expense);
                }
                if (expenses1.size() == 0)
                    Toast.makeText(getContext(), "please add expense", Toast.LENGTH_SHORT).show();
                else if (!valid)
                    Toast.makeText(getContext(), "there is empty value", Toast.LENGTH_SHORT).show();
                else {
                    dialog.setContentView(R.layout.loading);
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        try {
                            View view = linear.getChildAt(i);
                            EditText type, amount;
                            type = view.findViewById(R.id.type);
                            amount = view.findViewById(R.id.amount);
                            String ty = type.getText().toString();
                            String am = amount.getText().toString();
                            JSONObject item = new JSONObject();
                            item.put("name", ty);
                            item.put("branch", branch);
                            item.put("amount", Integer.parseInt(am));
                            array.put(item);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    addExpense(array.toString());
                }
            });

        });
        getExpenses();
        return root;
    }

    Utils utils = new Utils();
    private void addExpense(String items) {
        dialog.show();
        dialog.setCancelable(false);
        System.out.println(items);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=doDailyEx" +
                "&branch=" + branch+
                "&jExpense=" + items,
                response -> {
                    if (response.startsWith("<")) {
                        startActivity(new Intent((getContext()), ErrorActivity.class).putExtra("error", response));
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    } else {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
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

    private void getExpenses() {
        recycler.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getDailyEx" +
                "&branch=" + branch,
                response -> {
                    try {
                        expenses.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            ExpenseModel expense = new ExpenseModel();
                            expense.setReason(object.getString("name"));
                            expense.setDate(object.getString("date"));
                            expense.setBranch(object.getString("branch"));
                            expense.setNumber(object.getString("num"));
                            if (object.get("amount").toString().equalsIgnoreCase("") || object.get("amount").toString().startsWith("#")) {
                                expense.setAmount(0);
                            } else {
                                expense.setAmount((object.getInt("amount")));
                            }
                            expenses.add(expense);
                        }
                        refresh.setRefreshing(false);
                        adapter = new ExpenseAdapter(getContext(), expenses, "daily");
                        recycler.setAdapter(adapter);
                        recycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        if (expenses.size() == 0)
                            no_store.setVisibility(View.VISIBLE);
                        else
                            no_store.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", e.toString()));
                    }

                }, error -> {
            error.printStackTrace();
            try {
                progress.setVisibility(View.GONE);
                snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getExpenses())
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