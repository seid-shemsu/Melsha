package com.izhar.melsha.ui.loan.loans;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.models.LoanGiverModel;
import com.izhar.melsha.models.LoanModel;
import com.izhar.melsha.models.LoanTakerModel;

public class PayLoan extends AppCompatActivity {
    EditText pname, pcode, pphone, cna, amount;
    Utils utils = new Utils();
    LoanModel loan;
    LoanGiverModel giver;
    LoanTakerModel taker;
    String from, action;
    Button pay;

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
        setContentView(R.layout.activity_pay_loan);

        loan = (LoanModel) getIntent().getExtras().getSerializable("loan");
        giver = (LoanGiverModel) getIntent().getExtras().getSerializable("giver");
        taker = (LoanTakerModel) getIntent().getExtras().getSerializable("taker");
        from = getIntent().getExtras().getString("from");
        pay = findViewById(R.id.pay);
        pname = findViewById(R.id.pname);
        pcode = findViewById(R.id.pcode);
        pphone = findViewById(R.id.pphone);
        amount = findViewById(R.id.amount);

        if (from.equalsIgnoreCase("givers")) {
            setTitle("Pay Loan");
            pay.setText("pay");
            pname.setText(giver.getName());
            pcode.setText(giver.getCode());
            pphone.setText(giver.getPhone());
            action = "returnToGiver";
        } else {
            pay.setText("get paid");
            setTitle("Get Paid");
            pname.setText(taker.getName());
            pcode.setText(taker.getCode());
            pphone.setText(taker.getPhone());
            action = "receivingFromTaker";
        }

        pay.setOnClickListener(v -> {
            if (!amount.getText().toString().isEmpty()) {
                if (from.equalsIgnoreCase("givers")) {
                    if ((giver.getLeft()) < Integer.parseInt(amount.getText().toString())) {
                        Toast.makeText(this, "Paying amount is greater then what is left.", Toast.LENGTH_SHORT).show();
                    } else {
                        pay();
                    }
                } else {
                    if ((taker.getLeft()) < Integer.parseInt(amount.getText().toString())) {
                        Toast.makeText(this, "Paying amount is greater then what is left.", Toast.LENGTH_SHORT).show();
                    } else {
                        getPaid();
                    }
                }
            }
        });
    }

    private void pay() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=" + action +
                "&pcode=" + pcode.getText().toString() +
                "&pname=" + pname.getText().toString() +
                "&pphone=" + pphone.getText().toString() +
                "&ra_amount=" + amount.getText().toString()/*+
                "&cna=" + loan.getCredit_no()*/,
                response -> {
                    if (response.startsWith("<")) {
                        System.out.println(response);
                        startActivity(new Intent(this, ErrorActivity.class).putExtra("error", response));
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, error -> {
            System.out.println(error.getMessage());
            onBackPressed();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getPaid() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=" + action +
                "&pcode=" + pcode.getText().toString() +
                "&pname=" + pname.getText().toString() +
                "&pphone=" + pphone.getText().toString() +
                "&rb_amount=" + amount.getText().toString()/*+
                "&cnb=" + loan.getCredit_no()*/,
                response -> {
                    if (response.contains("Successfully")) {
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        startActivity(new Intent((this), ErrorActivity.class).putExtra("error", response));
                        System.out.println(response);
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }, error -> {

            startActivity(new Intent((this), ErrorActivity.class).putExtra("error", error.getMessage()));
            System.out.println(error.getMessage());
            onBackPressed();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}