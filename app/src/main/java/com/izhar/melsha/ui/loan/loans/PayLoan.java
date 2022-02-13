package com.izhar.melsha.ui.loan.loans;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.models.LoanModel;

public class PayLoan extends AppCompatActivity {
    EditText pname, pcode, pphone, cna, amount;
    Utils utils = new Utils();
    LoanModel loan;
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
        setTitle("Pay Loan");
        pay = findViewById(R.id.pay);
        pname = findViewById(R.id.pname);
        pcode = findViewById(R.id.pcode);
        pphone = findViewById(R.id.pphone);
        cna = findViewById(R.id.cna);
        amount = findViewById(R.id.amount);
        loan = (LoanModel) getIntent().getExtras().getSerializable("loan");
        from = getIntent().getExtras().getString("from");
        pname.setText(loan.getPerson_name());
        pcode.setText(loan.getPerson_code());
        cna.setText(loan.getCredit_no());

        if (from.equalsIgnoreCase("credit")){
            pay.setText("get paid");
            setTitle("Get Paid");
        }
        pay.setOnClickListener(v -> {
            if (!amount.getText().toString().isEmpty()){
                if (from.equalsIgnoreCase("loan")) {
                    if (Integer.parseInt(loan.getLeft()) < Integer.parseInt(amount.getText().toString())){
                        Toast.makeText(this, "Paying amount is greater then what is left.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        pay();
                    }
                }
                else {
                    if (Integer.parseInt(loan.getLeft()) < Integer.parseInt(amount.getText().toString())){
                        Toast.makeText(this, "Paying amount is greater then what is left.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        getPaid();
                    }
                }
            }
        });
    }

    private void pay(){
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=returnToGiver"+
                "&pcode=" + loan.getPerson_code()+
                "&pname=" + loan.getPerson_name()+
                "&pphone=" + "000000"+
                "&ra_amount=" + amount.getText().toString()+
                "&cna=" + loan.getCredit_no(),
                response -> {
                    if (response.contains("Successfully")){
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        System.out.println(response);
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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

    private void getPaid(){
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=receivingFromTaker"+
                "&pcode=" + loan.getPerson_code()+
                "&pname=" + loan.getPerson_name()+
                "&pphone=" + "000000"+
                "&rb_amount=" + amount.getText().toString()+
                "&cnb=" + loan.getCredit_no(),
                response -> {
                    if (response.contains("Successfully")){
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else {
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