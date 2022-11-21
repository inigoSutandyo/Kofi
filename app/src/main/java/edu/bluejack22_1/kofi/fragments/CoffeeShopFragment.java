package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.model.CoffeeShop;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoffeeShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoffeeShopFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CoffeeShop coffeeShop;
    private TextView nameView, addressView, descriptionView;

    public CoffeeShopFragment() {
        // Required empty public constructor
    }

    public CoffeeShopFragment(CoffeeShop coffeeShop) {
        this.coffeeShop = coffeeShop;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoffeeShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoffeeShopFragment newInstance(String param1, String param2) {
        CoffeeShopFragment fragment = new CoffeeShopFragment();
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
        View view = inflater.inflate(R.layout.fragment_coffee_shop, container, false);
        initView(view);
        return view;
    }
    private void initView(View view) {
        nameView = view.findViewById(R.id.detail_shop_name);
        addressView = view.findViewById(R.id.detail_shop_address);
        descriptionView = view.findViewById(R.id.detail_shop_description);

        nameView.setText(coffeeShop.getShopName());;
        addressView.setText(coffeeShop.getShopAddress());
        descriptionView.setText(coffeeShop.getShopDescription());
    }
}