package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CoffeeShopPagerAdapter;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.model.CoffeeShop;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoffeeShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoffeeShopFragment extends Fragment implements FragmentInterface {
    FirebaseFirestore db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CoffeeShop coffeeShop;
    private TextView nameView, addressView, descriptionView, ratingView;
    private ImageView shopImageView, shopEditView;

    private CoffeeShopPagerAdapter coffeeShopPagerAdapter;
    private ViewPager2 viewPager2;
    public CoffeeShopFragment() {
        // Required empty public constructor
        db = FirebaseFirestore.getInstance();
    }



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
        nameView = view.findViewById(R.id.detail_shop_name);
        addressView = view.findViewById(R.id.detail_shop_address);
        descriptionView = view.findViewById(R.id.detail_shop_description);
        shopImageView = view.findViewById(R.id.shopimage);
        ratingView = view.findViewById(R.id.detail_shop_rating);
        shopEditView = view.findViewById(R.id.detail_shop_edit);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String shopId = args.getString("SHOP_ID");
        setShopView(shopId, view);

        coffeeShopPagerAdapter = new CoffeeShopPagerAdapter(this.getActivity(), shopId);
        viewPager2 = view.findViewById(R.id.detail_shop_pager);
        viewPager2.setAdapter(coffeeShopPagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.detail_shop_tab);

        new TabLayoutMediator(tabLayout, viewPager2, (tab,position) -> tab.setText(position == 1 ? "Review" : "Menu")).attach();

    }

    private void setShopView(String shopId, View view) {
        DocumentReference docRef = db.collection("coffeeshop").document(shopId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot docSnap = task.getResult();
                    if (docSnap.exists()) {
                        coffeeShop = task.getResult().toObject(CoffeeShop.class);
                        populateView(view);
                    }
                } else {
                    Log.d("Coffee", "Document read failed");
                }
            }
        });
    }

    private void populateView(View view) {
        nameView.setText(coffeeShop.getShopName());
        addressView.setText(coffeeShop.getShopAddress());
        descriptionView.setText(coffeeShop.getShopDescription());
        ratingView.setText( String.format("%.1f", coffeeShop.getAverageRating()));
        shopEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("SHOP_ID", coffeeShop.getShopId());
                bundle.putString("NAME", coffeeShop.getShopName());
                bundle.putString("ADDRESS", coffeeShop.getShopAddress());
                bundle.putString("DESCRIPTION", coffeeShop.getShopDescription());
                bundle.putString("IMAGE", coffeeShop.getImageUrl());
                UpdateCoffeeShopFragment updateshop = new UpdateCoffeeShopFragment();
                updateshop.setArguments(bundle);
                replaceFragment(updateshop);
            }
        });
        Glide.with(view).load(coffeeShop.getImageUrl()).placeholder(R.drawable.item_place_holder).into(shopImageView);
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void returnFragment() {

    }
}