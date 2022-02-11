package com.izhar.melsha.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.izhar.melsha.activities.Move;
import com.izhar.melsha.activities.Purchase;
import com.izhar.melsha.R;
import com.izhar.melsha.activities.Sell;
import com.izhar.melsha.models.ItemModel;

public class ItemDetail extends AppCompatActivity {
    EditText quantity, avg_price, total_capital, code, name, size, model, dessie, kore, jemmo;
    Button purchase, sell, move;
    ItemModel item;

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
        setContentView(R.layout.activity_item_detail);
        item = (ItemModel) getIntent().getExtras().getSerializable("item");
        setTitle(item.getCode());
        quantity = findViewById(R.id.quantity);
        avg_price = findViewById(R.id.avg_price);
        total_capital = findViewById(R.id.total_capital);
        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        size = findViewById(R.id.size);
        model = findViewById(R.id.model);
        dessie = findViewById(R.id.dessie);
        kore = findViewById(R.id.kore);
        jemmo = findViewById(R.id.jemmo);
        quantity.setText(String.valueOf(item.getQuantity()));
        avg_price.setText(String.valueOf(item.getAvg_price()));
        total_capital.setText(String.valueOf(item.getTotal_capital()));
        code.setText(item.getCode());
        name.setText(item.getName());
        size.setText(String.valueOf(item.getSize()));
        model.setText(item.getModel());
        dessie.setText(String.valueOf(item.getDessie()));
        kore.setText(String.valueOf(item.getKore()));
        jemmo.setText(String.valueOf(item.getJemmo()));

        purchase = findViewById(R.id.purchase);
        purchase.setOnClickListener(v -> {
            startActivity(new Intent(this, Purchase.class)
                    .putExtra("item", item));
        });

        move = findViewById(R.id.move);
        move.setOnClickListener(v -> {
            startActivity(new Intent(this, Move.class)
                    .putExtra("item", item));
        });

        sell = findViewById(R.id.sell);
        sell.setOnClickListener(v -> {
            startActivity(new Intent(this, Sell.class)
                    .putExtra("item", item));
        });
    }
}