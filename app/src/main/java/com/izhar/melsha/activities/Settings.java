package com.izhar.melsha.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.izhar.melsha.R;
import com.squareup.picasso.Picasso;

public class Settings extends AppCompatActivity {
    TextInputLayout url_input, name_input;
    EditText editText_url, app_name;
    Button save;
    FloatingActionButton fab;
    TextView name, branch;
    SharedPreferences user, url;
    String b;
    ImageView edit_image, image;
    SharedPreferences sp;
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
        sp = getSharedPreferences("user", MODE_PRIVATE);
        editText_url = findViewById(R.id.url);
        url_input = findViewById(R.id.url_input);
        name_input = findViewById(R.id.name_input);
        app_name = findViewById(R.id.app_name);

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

        //////////////////////////////////////////////////////////////////////////////////////////// APP NAME AND IMAGE

        name_input.setEndIconOnClickListener(v -> {
            if (app_name.isEnabled()){
                if (!app_name.getText().toString().isEmpty()){
                    app_name.setEnabled(false);
                    name_input.setEndIconDrawable(getResources().getDrawable(R.drawable.edit));
                    sp.edit().putString("app_name", app_name.getText().toString()).apply();
                }
            }
            else {
                app_name.setEnabled(true);
                name_input.setEndIconDrawable(getResources().getDrawable(R.drawable.check));
            }


        });
        edit_image = findViewById(R.id.edit_image);
        image = findViewById(R.id.image);
        Picasso.Builder builder = new Picasso.Builder(this);
        if (!sp.getString("image", "").isEmpty()){
            builder.build().load(sp.getString("image", "")).into(image);
        }
        if (!sp.getString("app_name", "").isEmpty()){
            app_name.setText(sp.getString("app_name", ""));
        }
        else
            app_name.setText("Business Gram");
        edit_image.setOnClickListener(v -> {
            askPermission();
        });
        //////////////////////////////////////////////////////////////////////////////////////////// end of APP NAME AND IMAGE
        b = user.getString("branch", "Guest");
        if (!b.equalsIgnoreCase("owner")) {
            findViewById(R.id.name_input).setVisibility(View.GONE);
            findViewById(R.id.edit_image).setVisibility(View.GONE);
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
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);
    }

    Uri imgUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101){
            if (requestCode == RESULT_OK && data != null && data.getData() != null) {
                imgUri = data.getData();
                Picasso.Builder builder = new Picasso.Builder(this);
                builder.listener((picasso, uri, exception) -> Toast.makeText(Settings.this, "" + exception.toString(), Toast.LENGTH_SHORT).show());
                builder.build().load(imgUri).into(image);
                sp.edit().putString("image", imgUri.toString()).apply();
            } else {
                Toast.makeText(this, "" + resultCode, Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 100){
            if (resultCode != RESULT_OK){
                Toast.makeText(this, "Please grant the permission", Toast.LENGTH_SHORT).show();
                askPermission();
            }
        }

    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            openFileChooser();
        else
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
    }
}