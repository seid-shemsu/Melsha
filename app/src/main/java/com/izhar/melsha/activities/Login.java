package com.izhar.melsha.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.izhar.melsha.MainActivity;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText username, password;
    private Button login;
    TextView forgot;
    SharedPreferences sharedPreferences, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        forgot = findViewById(R.id.forgot);
        forgot.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.loading);
            dialog.show();
            new Handler().postDelayed(() -> {
                Toast.makeText(this, "you will be contacted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }, 4000);
        });
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login.setOnClickListener(v -> {
            if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                Dialog dialog = new Dialog(this);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.loading);
                dialog.show();
                authenticate(dialog);
            }
        });
        if (getSharedPreferences("url", MODE_PRIVATE).getString("url", "empty").equalsIgnoreCase("empty")){
            Dialog dialog = new Dialog(this);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.edit_url);
            dialog.show();
            EditText url = dialog.findViewById(R.id.url);
            Button save = dialog.findViewById(R.id.save);
            save.setOnClickListener(v -> {
                if (!url.getText().toString().isEmpty()){
                    getSharedPreferences("url", MODE_PRIVATE).edit().putString("url", url.getText().toString()).apply();
                    dialog.dismiss();
                }
            });
        }
    }

    Utils utils = new Utils();

    private void authenticate(Dialog dialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl(this) +
                "?action=doLogin" +
                "&username=" + username.getText().toString() +
                "&password=" + password.getText().toString(),
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject object = array.getJSONObject(0);
                        if (object.getInt("status") == 1) {
                            sharedPreferences.edit().putString("branch", object.getString("branch")).apply();
                            sharedPreferences.edit().putString("name", object.getString("name")).apply();
                            sharedPreferences.edit().putBoolean("logged", true).apply();
                            dialog.dismiss();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(this, "invalid username or password", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(this, "invalid username or password", Toast.LENGTH_SHORT).show();
                    }

                }, error -> {
            error.printStackTrace();
            dialog.dismiss();
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}