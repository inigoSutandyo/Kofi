package edu.bluejack22_1.kofi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CoffeeShopActivity extends AppCompatActivity {

    private String name, address, description;
    private TextView nameView, addressView, descriptionView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_shop);

        name = getIntent().getStringExtra("NAME");
        address = getIntent().getStringExtra("ADDRESS");
        description = getIntent().getStringExtra("DESCRIPTION");
        initView();
    }

    private void initView() {
        nameView = findViewById(R.id.detail_shop_name);
        addressView = findViewById(R.id.detail_shop_address);
        descriptionView = findViewById(R.id.detail_shop_description);

        nameView.setText(name);;
        addressView.setText(address);
        descriptionView.setText(description);
    }
}