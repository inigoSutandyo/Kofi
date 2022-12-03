package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.controller.CoffeeController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCoffeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCoffeeFragment extends Fragment implements FragmentInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CoffeeController coffeeController;
    private String shopId;
    private TextView nameTxt, priceTxt;
    private Button button;
    private ImageView imageView;

    public AddCoffeeFragment() {
        // Required empty public constructor
        coffeeController = new CoffeeController();
    }

    public static AddCoffeeFragment newInstance(String param1, String param2) {
        AddCoffeeFragment fragment = new AddCoffeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_coffee, container, false);

        Bundle args = getArguments();
        shopId = args.getString("SHOP_ID");

        nameTxt = view.findViewById(R.id.txt_coffee_name);
        priceTxt = view.findViewById(R.id.txt_coffee_price);
        button = view.findViewById(R.id.btn_add_coffee);
        imageView = view.findViewById(R.id.img_coffee);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double d = getDouble(priceTxt.getText().toString().trim());
                if (d == -1) {
                    return;
                }
                coffeeController.addCoffee(shopId, nameTxt.getText().toString(), d);
                returnFragment();
            }
        });

        return view;
    }

    private double getDouble(String price) {
        double d = -1;
        if (price == null) {
            return d;
        }
        try {
            BigDecimal b = new BigDecimal(price);
            d = b.doubleValue();
        } catch (NumberFormatException nfe) {
            return d;
        }
        return d;
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void returnFragment() {
        Fragment fragment = new CoffeeShopFragment();
        Bundle args = new Bundle();
        args.putString("SHOP_ID",shopId);
        fragment.setArguments(args);
        replaceFragment(fragment);
    }
}