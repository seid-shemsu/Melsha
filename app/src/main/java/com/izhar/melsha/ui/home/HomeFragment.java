package com.izhar.melsha.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private View root;
    private CardView bank, credit, stoke, sells, monthly;
    private String branch;
    private TextView stock, date, unpaid_to_me, unpaid_to_others,
            j_sold, d_sold, k_sold, j_sold_m, d_sold_m, k_sold_m,
            j_profit, d_profit, k_profit, j_profit_m, d_profit_m, k_profit_m,
            j_expense, k_expense, d_expense,
            deposited, un_deposited,
            net_profit;
    private Dialog dialog;
    SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);
        refresh = root.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(() -> {
            if (branch.equalsIgnoreCase("owner"))
                getData();
            else
                refresh.setRefreshing(false);
        });
        date = root.findViewById(R.id.date);
        bank = root.findViewById(R.id.card_bank);
        credit = root.findViewById(R.id.card_credit);
        stoke = root.findViewById(R.id.card_stock);
        sells = root.findViewById(R.id.card_sells);
        monthly = root.findViewById(R.id.card_sells_monthly);

        branch = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "Guest");
        if (branch.equalsIgnoreCase("owner")) {
            bank.setVisibility(View.VISIBLE);
            credit.setVisibility(View.VISIBLE);
            stoke.setVisibility(View.VISIBLE);
            sells.setVisibility(View.VISIBLE);
            monthly.setVisibility(View.VISIBLE);
            unpaid_to_me = root.findViewById(R.id.unpaid_to_me);
            unpaid_to_others = root.findViewById(R.id.unpaid_to_others);
            j_sold = root.findViewById(R.id.jemmo_sold);
            d_sold = root.findViewById(R.id.dessie_sold);
            k_sold = root.findViewById(R.id.kore_sold);
            j_sold_m = root.findViewById(R.id.jemmo_sold_monthly);
            d_sold_m = root.findViewById(R.id.dessie_sold_monthly);
            k_sold_m = root.findViewById(R.id.kore_sold_monthly);

            j_profit = root.findViewById(R.id.jemmo_profit);
            d_profit = root.findViewById(R.id.dessie_profit);
            k_profit = root.findViewById(R.id.kore_profit);

            j_profit_m = root.findViewById(R.id.jemmo_profit_monthly);
            d_profit_m = root.findViewById(R.id.dessie_profit_monthly);
            k_profit_m = root.findViewById(R.id.kore_profit_monthly);

            j_expense = root.findViewById(R.id.jemmo_expense);
            d_expense = root.findViewById(R.id.dessie_expense);
            k_expense = root.findViewById(R.id.kore_expense);

            deposited = root.findViewById(R.id.deposited);
            un_deposited = root.findViewById(R.id.un_deposited);
            stock = root.findViewById(R.id.stock);
            net_profit = root.findViewById(R.id.net_profit);
            getData();
        }
        getDate();
        return root;
    }


    Utils utils = new Utils();

    private void getDate() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getEthiod",
                response -> {
                    if (response.startsWith("<")) {
                        startActivity(new Intent(getActivity().getApplicationContext(), ErrorActivity.class).putExtra("error", response));
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    } else {
                        date.setText("ቀን " + response);
                    }
                }, error -> {
            error.printStackTrace();
            try {
                Toast.makeText(getActivity().getApplicationContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getDashboard",
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject object = array.getJSONObject(0);
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        //yesterday
                        d_sold.setText(decimalFormat.format(object.getInt("shop3s")));
                        j_sold.setText(decimalFormat.format(object.getInt("shop1s")));
                        k_sold.setText(decimalFormat.format(object.getInt("shop2s")));
                        d_profit.setText(decimalFormat.format(object.getInt("shop3p")));
                        j_profit.setText(decimalFormat.format(object.getInt("shop1p")));
                        k_profit.setText(decimalFormat.format(object.getInt("shop2p")));

                        //monthly
                        d_sold_m.setText(decimalFormat.format(object.getInt("mshop3s")));
                        j_sold_m.setText(decimalFormat.format(object.getInt("mshop1s")));
                        k_sold_m.setText(decimalFormat.format(object.getInt("mshop2s")));
                        d_profit_m.setText(decimalFormat.format(object.getInt("mshop3p")));
                        j_profit_m.setText(decimalFormat.format(object.getInt("mshop1p")));
                        k_profit_m.setText(decimalFormat.format(object.getInt("mshop2p")));
                        d_expense.setText(decimalFormat.format(object.getInt("eshop3")));
                        j_expense.setText(decimalFormat.format(object.getInt("eshop1")));
                        k_expense.setText(decimalFormat.format(object.getInt("eshop2")));

                        d_profit_m.setTextColor(getContext().getResources().getColor(R.color.green));
                        j_profit_m.setTextColor(getContext().getResources().getColor(R.color.green));
                        k_profit_m.setTextColor(getContext().getResources().getColor(R.color.green));
                        d_expense.setTextColor(getContext().getResources().getColor(R.color.red));
                        j_expense.setTextColor(getContext().getResources().getColor(R.color.red));
                        k_expense.setTextColor(getContext().getResources().getColor(R.color.red));
                        //profit
                        net_profit.setText(decimalFormat.format(object.getInt("netp")) + "\nETB");

                        deposited.setText(decimalFormat.format(object.getInt("bankb")) + "\nETB");
                        un_deposited.setText(decimalFormat.format(object.getInt("excash")) + "\nETB");
                        stock.setText(decimalFormat.format(object.getInt("stockb")) + "\nETB");
                        unpaid_to_me.setText(decimalFormat.format(object.getInt("creditb")) + "\nETB");
                        unpaid_to_others.setText(decimalFormat.format(object.getInt("loanb")) + "\nETB");
                        refresh.setRefreshing(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        startActivity(new Intent(getContext(), ErrorActivity.class).putExtra("error", e.toString()));
                    }

                }, error -> {
            error.printStackTrace();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}