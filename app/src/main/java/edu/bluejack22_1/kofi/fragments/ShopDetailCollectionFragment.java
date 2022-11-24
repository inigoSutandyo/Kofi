package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CoffeeAdapter;
import edu.bluejack22_1.kofi.adapter.ReviewAdapter;
import edu.bluejack22_1.kofi.controller.CoffeeController;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeListener;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeShopListener;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.interfaces.listeners.UserListener;
import edu.bluejack22_1.kofi.model.Coffee;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopDetailCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopDetailCollectionFragment extends Fragment implements
        RecyclerViewInterface,
        CoffeeListener,
        ReviewListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ReviewController reviewController;
    private CoffeeController coffeeController;
    private RecyclerView rv;
    private ReviewAdapter reviewAdapter;
    private CoffeeAdapter coffeeAdapter;
    private String id;
    private int key;
    private ArrayList<Coffee> coffees;
    private ArrayList<Review> reviews;
    private TextView eReviewText;
    private RatingBar ratingBar;
    private Button addReviewBtn;
    public ShopDetailCollectionFragment() {
        // Required empty public constructor
        reviewController = new ReviewController();
        coffeeController = new CoffeeController();
    }

    public static ShopDetailCollectionFragment newInstance(String param1, String param2) {
        ShopDetailCollectionFragment fragment = new ShopDetailCollectionFragment();
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
        return inflater.inflate(R.layout.fragment_shop_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eReviewText = view.findViewById(R.id.text_review);
        ratingBar = view.findViewById(R.id.rating);
        addReviewBtn = view.findViewById(R.id.btn_review);
        Bundle args = getArguments();
        key = args.getInt("KEY");
        id = args.getString("DATA");

        rv = view.findViewById(R.id.detail_shop_list);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        if (key == 1) {
            eReviewText.setVisibility(View.INVISIBLE);
            ratingBar.setVisibility(View.INVISIBLE);
            addReviewBtn.setVisibility(View.INVISIBLE);
            initCoffees();
        } else {
            initReviews();
        }
    }

    private void initReviews() {
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this.getContext(), reviews, this);
        rv.setAdapter(reviewAdapter);
        reviewController.getReviews(id, this);
    }

    private void initCoffees() {
        coffees = new ArrayList<>();
        coffeeAdapter = new CoffeeAdapter(this.getContext(), coffees, this);
        rv.setAdapter(coffeeAdapter);
        coffeeController.getCoffees(id, this);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onCompleteCoffee(DocumentSnapshot docSnap) {

    }

    @Override
    public void onCompleteCoffeeCollection(QuerySnapshot querySnap) {
        for (QueryDocumentSnapshot documentSnapshot: querySnap) {
            String name = (String) documentSnapshot.getData().get("name");
            long price = (Long) documentSnapshot.getData().get("price");
            coffees.add(new Coffee(name,price,documentSnapshot.getId()));
        }
    }

    @Override
    public void onSuccessCoffee() {

    }

    @Override
    public void onSuccessUpdateCoffee(Coffee coffee) {

    }

    @Override
    public void onCompleteReview(DocumentSnapshot docSnap) {

    }

    @Override
    public void onCompleteReviewCollection(QuerySnapshot querySnap) {
        for (QueryDocumentSnapshot document : querySnap) {
            String content = (String) document.getData().get("content");

            String rating = (String) document.getData().get("rating");
            Double ratingD = Double.parseDouble(rating);

            DocumentReference userRef = (DocumentReference) document.getData().get("user");
            String reviewId = document.getId();
            Review rev = new Review(content, ratingD, reviewId);
            reviewController.addUserByRef(userRef, rev, reviewAdapter);
            reviews.add(rev);
        }
    }

    @Override
    public void onSuccessUpdateReview(Review review) {

    }

    @Override
    public void onSuccessReview() {

    }

}