package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CoffeeShopAdapter;
import edu.bluejack22_1.kofi.adapter.SliderAdapter;
import edu.bluejack22_1.kofi.controller.CoffeeShopController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeShopListener;
import edu.bluejack22_1.kofi.controller.model.CoffeeShop;
import edu.bluejack22_1.kofi.controller.model.SliderData;
import edu.bluejack22_1.kofi.controller.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements
        RecyclerViewInterface,
        FragmentInterface,
        CoffeeShopListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CoffeeShopController coffeeShopController;
    private RecyclerView recyclerView;
    private ArrayList<CoffeeShop> coffeeShops;
    private CoffeeShopAdapter coffeeAdapter;
    private SearchView searchView;

//    private ProgressBar tempBar;
    public HomeFragment() {
        // Required empty public constructor
        coffeeShopController = new CoffeeShopController();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters



    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.home_search);

        if(User.getCurrentUser().getRole().equals("Admin")){
            FloatingActionButton fab = view.findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replaceFragment(new AddCoffeeShopFragment());
                }
            });
        }
        initSlider(view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Fragment searchFragment = new SearchFragment();
                Bundle args = new Bundle();
                args.putString("SEARCH", s);
                searchFragment.setArguments(args);
                replaceFragment(searchFragment);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCoffeeShopsList(super.getView());
    }


    private void initCoffeeShopsList(View view) {
        coffeeShops = new ArrayList<>();

        recyclerView = view.findViewById(R.id.coffee_shop_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        coffeeAdapter = new CoffeeShopAdapter(this.getContext(), coffeeShops, this);

        recyclerView.setAdapter(coffeeAdapter);
        coffeeShopController.getCoffeeShopList(this);
    }

    private String url1 = "https://firebasestorage.googleapis.com/v0/b/tpaandroid-8e254.appspot.com/o/images%2Fcoffeebanner.png?alt=media&token=11c7f754-eb9c-4d2f-9277-f9ddcb57c4a1";
    private String url2 = "https://firebasestorage.googleapis.com/v0/b/tpaandroid-8e254.appspot.com/o/images%2Fcoffeebanner2.png?alt=media&token=8a53c293-fd48-4c23-b5b9-b1bcb3cd2164";
    private String url3 = "https://firebasestorage.googleapis.com/v0/b/tpaandroid-8e254.appspot.com/o/images%2Fcoffeebanner3.png?alt=media&token=516aa633-ea15-46af-95df-b4d47feadc12";

    private void initSlider(View view) {
        ArrayList<SliderData> sliderList = new ArrayList<>();

        sliderList.add(new SliderData(url1));
        sliderList.add(new SliderData(url2));
        sliderList.add(new SliderData(url3));


        SliderView sliderView = view.findViewById(R.id.imageSlider);


        SliderAdapter sliderAdapter = new SliderAdapter(this.getActivity(), sliderList);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }


    @Override
    public void onItemClick(int position) {
        Log.d("Coffee", coffeeShops.get(position).getShopName());
        Fragment fragment = new CoffeeShopFragment();
        Bundle args = new Bundle();
        args.putString("SHOP_ID", coffeeShops.get(position).getShopId());
        fragment.setArguments(args);
        replaceFragment(fragment);
    }

    @Override
    public void onClickDelete(int position) {

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

    }

    @Override
    public void onCompleteShop(DocumentSnapshot docSnap) {

    }

    @Override
    public void onCompleteShopCollection(QuerySnapshot querySnap) {
        for (QueryDocumentSnapshot document : querySnap) {
            String name = (String) document.getData().get("shopName");
            String address = (String) document.getData().get("shopAddress");
            String description = (String) document.getData().get("shopDescription");
            String picture = (String) document.getData().get("imageUrl");
            coffeeShops.add(new CoffeeShop(name,address,description, document.getId(), picture));
        }
        coffeeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessUpdateShop(CoffeeShop coffeeShop) {

    }

    @Override
    public void onSuccessShop() {

    }

    @Override
    public void onDeleteShop() {

    }
}