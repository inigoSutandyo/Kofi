package edu.bluejack22_1.kofi.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.bluejack22_1.kofi.MainActivity;
import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.controller.CoffeeShopController;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeShopListener;
import edu.bluejack22_1.kofi.model.CoffeeShop;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCoffeeShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCoffeeShopFragment extends Fragment implements CoffeeShopListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button addBtn;
    EditText eShopName, eShopAddress, eShopDescription;
    String ShopName, ShopAddress, ShopDescription;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCoffeeShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCoffeeShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCoffeeShopFragment newInstance(String param1, String param2) {
        AddCoffeeShopFragment fragment = new AddCoffeeShopFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_coffee_shop, container, false);
        addBtn = view.findViewById(R.id.btn_add_shop);
        eShopName = view.findViewById(R.id.txt_shop_name);
        eShopAddress = view.findViewById(R.id.txt_shop_address);
        eShopDescription = view.findViewById(R.id.txt_shop_description);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateShop()){
                    addShop();
                }
            }
        });
        return view;
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
        shopcontroller.addCoffeeShop(ShopName, ShopAddress, ShopDescription, this);
    }

    @Override
    public void onCompleteShop(DocumentSnapshot docSnap) {

    }

    @Override
    public void onCompleteShopCollection(QuerySnapshot querySnap) {

    }

    @Override
    public void onSuccessUpdateShop(CoffeeShop coffeeShop) {

    }

    @Override
    public void onSuccessShop() {
        Activity activity = this.getActivity();
        activity.finish();
        Intent mainIntent = new Intent(activity, MainActivity.class);
        activity.startActivity(mainIntent);
    }
}