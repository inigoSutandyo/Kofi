package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CoffeeShopAdapter;
import edu.bluejack22_1.kofi.controller.CoffeeShopController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeShopListener;
import edu.bluejack22_1.kofi.model.CoffeeShop;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchShopFragment extends Fragment implements CoffeeShopListener, RecyclerViewInterface, FragmentInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SearchView searchView;
    private CoffeeShopController coffeeShopController;
    private CoffeeShopAdapter coffeeShopAdapter;
    private ArrayList<CoffeeShop> coffeeShops;
    private RecyclerView recyclerView;
    private Button popular, newest, rating;
    private String search;
    public SearchShopFragment() {
        // Required empty public constructor
        coffeeShops = new ArrayList<>();
        coffeeShopController = new CoffeeShopController();
    }

    public static SearchShopFragment newInstance(String param1, String param2) {
        SearchShopFragment fragment = new SearchShopFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_shop, container, false);

        searchView = view.findViewById(R.id.search_view);
        Bundle args = getArguments();
        search = args.getString("SEARCH");
        searchView.setQuery(search, true);
        CoffeeShopListener listener = this;
        coffeeShopController.getCoffeeShopList(listener);
        coffeeShopAdapter = new CoffeeShopAdapter(this.getContext(), coffeeShops, this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search = s;
                coffeeShops.clear();
                coffeeShopAdapter.notifyDataSetChanged();
                coffeeShopController.getCoffeeShopList(listener);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        newest = view.findViewById(R.id.btn_newest);
        popular = view.findViewById(R.id.btn_popular);
        rating = view.findViewById(R.id.btn_rating);

        selectButton(newest);

        newest.setOnClickListener(v -> {
            if (coffeeShops.size() < 1) return;
            selectButton(newest);
            sortByDate();
        });

        popular.setOnClickListener(v -> {
            if (coffeeShops.size() < 1) return;
            selectButton(popular);
            sortByPopularity();
        });

        rating.setOnClickListener(v -> {
            if (coffeeShops.size() < 1) return;
            selectButton(rating);
            sortByRating();
        });
        return view;
    }

    private void sortByRating() {
        Collections.sort(coffeeShops, ratingComparator);
        coffeeShopAdapter.notifyDataSetChanged();
    }

    private void sortByPopularity() {
        Collections.sort(coffeeShops, popularComparator);
        coffeeShopAdapter.notifyDataSetChanged();
    }

    private void sortByDate() {
        Collections.sort(coffeeShops, dateComparator);
        coffeeShopAdapter.notifyDataSetChanged();
    }

    private void setAllButtonBackgroundWhite() {
        newest.setBackgroundTintList(getContext().getColorStateList(R.color.white));
        popular.setBackgroundTintList(getContext().getColorStateList(R.color.white));
        rating.setBackgroundTintList(getContext().getColorStateList(R.color.white));

    }

    private void selectButton(Button b) {
        setAllButtonBackgroundWhite();
        b.setBackgroundTintList(getContext().getColorStateList(R.color.cream));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.search_list);

        recyclerView.setAdapter(coffeeShopAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onCompleteShop(DocumentSnapshot docSnap) {}

    @Override
    public void onCompleteShopCollection(QuerySnapshot querySnap) {
        for (QueryDocumentSnapshot document : querySnap) {
            String name = (String) document.getData().get("shopName");
            if (name.toLowerCase().contains(search.toLowerCase())) {
                CoffeeShop coffeeShop = document.toObject(CoffeeShop.class);
                coffeeShops.add(coffeeShop);
            }
        }
        sortByDate();
    }

    @Override
    public void onSuccessUpdateShop(CoffeeShop coffeeShop) {}

    @Override
    public void onSuccessShop() {}

    @Override
    public void onDeleteShop() {}

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void returnFragment() {}

    @Override
    public void onItemClick(int position) {
        Log.d("COFFEE", "onItemClick: " + position);
        CoffeeShop coffeeShop = coffeeShops.get(position);
        Fragment fragment = new CoffeeShopFragment();
        Bundle args = new Bundle();
        args.putString("SHOP_ID", coffeeShop.getShopId());
        fragment.setArguments(args);
        replaceFragment(fragment);
    }

    @Override
    public void onClickDelete(int position) {}

    /*--- COMPARATOR  --- */

    Comparator<CoffeeShop> popularComparator = new Comparator<CoffeeShop>() {
        @Override
        public int compare(CoffeeShop c1, CoffeeShop c2) {
            int r1 = c1.getReviewCount();
            int r2 = c2.getReviewCount();

            // DESC
            return r2-r1;
        }
    };


    Comparator<CoffeeShop> dateComparator = new Comparator<CoffeeShop>() {
        @Override
        public int compare(CoffeeShop c1, CoffeeShop c2) {
            Timestamp t1 = c1.getCreatedAt();
            Timestamp t2 = c2.getCreatedAt();

            // DESC
            return t2.toDate().compareTo(t1.toDate());
        }
    };

    Comparator<CoffeeShop> ratingComparator = new Comparator<CoffeeShop>() {
        @Override
        public int compare(CoffeeShop c1, CoffeeShop c2) {
            Double rating1 = c1.getAverageRating();
            Double rating2 = c2.getAverageRating();
            // DESC
            return Double.compare(rating2, rating1);
        }
    };
}