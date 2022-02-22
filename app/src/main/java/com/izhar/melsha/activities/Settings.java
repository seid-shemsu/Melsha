package com.izhar.melsha.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.izhar.melsha.R;

public class Settings extends AppCompatActivity {
    TextInputLayout url_input;
    EditText editText_url;
    Button save;
    FloatingActionButton fab;
    TextView name, branch;
    SharedPreferences user, url;
    String b;
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
        setContentView(R.layout.activity_settings);

        editText_url = findViewById(R.id.url);
        url_input = findViewById(R.id.url_input);
        save = findViewById(R.id.save);
        fab = findViewById(R.id.fab);
        name = findViewById(R.id.name);
        branch = findViewById(R.id.branch);
        user = getSharedPreferences("user", MODE_PRIVATE);
        url = getSharedPreferences("url", MODE_PRIVATE);
        name.setText(user.getString("name", "Guest"));
        branch.setText(user.getString("branch", "Guest"));
        editText_url.setText(url.getString("url", ""));
        url_input.setEndIconOnClickListener(v -> {
            save.setVisibility(View.VISIBLE);
            editText_url.setEnabled(true);
        });

        b = user.getString("branch", "Guest");
        if (!b.equalsIgnoreCase("owner")) {
            findViewById(R.id.name_input).setVisibility(View.GONE);
        }
        save.setOnClickListener(v -> {
            if (!editText_url.getText().toString().isEmpty()) {
                url.edit().putString("url", editText_url.getText().toString()).apply();
                save.setVisibility(View.GONE);
                editText_url.setEnabled(false);
            }
        });

        fab.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.confirmation);
            dialog.show();
            TextView message = dialog.findViewById(R.id.message);
            message.setText("You are going to Log out?");
            ImageView icon = dialog.findViewById(R.id.icon);
            icon.setImageDrawable(getResources().getDrawable(R.drawable.power));
            Button cancel = dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            Button logout = dialog.findViewById(R.id.confirm);
            logout.setText("LogOut");
            logout.setOnClickListener(v2 -> {
                user.edit().clear().apply();
                dialog.dismiss();
                startActivity(new Intent(this, Login.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            });
        });
    }
}