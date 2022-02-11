package com.izhar.melsha.ui.dailySells;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;

public class DailySells extends Fragment {
    View root;
    EditText amount;
    TextInputLayout amount_input;
    Button save;
    String user_branch;

    Utils utils = new Utils();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_daily_sells, container, false);
        user_branch = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("branch", "");
        amount = root.findViewById(R.id.amount);
        amount_input = root.findViewById(R.id.amount_input);
        save = root.findViewById(R.id.save);
        //getData();
        amount_input.setEndIconOnClickListener(v -> {
            amount.setEnabled(true);
            save.setVisibility(View.VISIBLE);
        });

        save.setOnClickListener(v -> {
            if (!amount.getText().toString().isEmpty()) {
                Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.loading);
                dialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                        "?action=doDailySells" +
                        "&branch=" + user_branch +
                        "&amount=" + amount.getText().toString(),
                        response -> {
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            System.out.println(response);
                            amount.setEnabled(false);
                            dialog.dismiss();

                        }, error -> {
                    System.out.println(error.getMessage());
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });
        return root;
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(getContext()) +
                "?action=getDailySells" +
                "&branch=" + user_branch,
                response -> {
                    amount.setText(response);
                }, error -> {
            error.printStackTrace();
            Toast.makeText(getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}