package com.izhar.melsha.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.izhar.melsha.MainActivity;
import com.izhar.melsha.R;
import com.izhar.melsha.activities.Login;

public class Splash extends AppCompatActivity {

    //Animation animBlink;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            if (getSharedPreferences("user", MODE_PRIVATE).getBoolean("logged", false))
                startActivity(new Intent(this, MainActivity.class));
            else
                startActivity(new Intent(this, Login.class));
            finish();
        }, 500);
    }

}