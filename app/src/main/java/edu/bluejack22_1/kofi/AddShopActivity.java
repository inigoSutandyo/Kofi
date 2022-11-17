package edu.bluejack22_1.kofi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.bluejack22_1.kofi.controller.CoffeeShopController;


public class AddShopActivity extends AppCompatActivity {
    Button addBtn;
    EditText eShopName, eShopAddress, eShopDescription;
    String ShopName, ShopAddress, ShopDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        addBtn = findViewById(R.id.btn_add_shop);
        eShopName = findViewById(R.id.txt_shop_name);
        eShopAddress = findViewById(R.id.txt_shop_address);
        eShopDescription = findViewById(R.id.txt_shop_description);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateShop()){
                    addShop();
                }
            }
        });
    }

    private boolean validateShop(){
        ShopName = eShopName.getText().toString();
        ShopAddress = eShopAddress.getText().toString();
        ShopDescription = eShopDescription.getText().toString();
        boolean check = true;
        if(ShopName.length() == 0){
            eShopName.setError("Shop Name Must Be filled");
            check = false;
        }
        if(ShopAddress.length() == 0){
            eShopAddress.setError("Shop Address Must Be filled");
            check = false;
        }
        if(ShopDescription.length() == 0){
            eShopDescription.setError("Shop Description Must be filled");
            check = false;
        }
        return check;
    }

    private void addShop(){
        CoffeeShopController shopcontroller = new CoffeeShopController();
        shopcontroller.addCoffeeShop(ShopName, ShopAddress, ShopDescription);
        Toast.makeText(this, "Successfully add shop",
                Toast.LENGTH_LONG).show();
    }

}