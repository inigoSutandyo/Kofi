package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CoffeeAdapter;
import edu.bluejack22_1.kofi.adapter.ReviewAdapter;
import edu.bluejack22_1.kofi.controller.CoffeeController;
import edu.bluejack22_1.kofi.controller.LikeController;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeListener;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
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
        ReviewListener,
        FragmentInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
    private RatingBar ratingBar;
    private FloatingActionButton fab;

    public ShopDetailCollectionFragment() {
        // Required empty public constructor
        reviewController = new ReviewController();
        coffeeController = new CoffeeController();
        reviews = new ArrayList<>();
        coffees = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.fragment_shop_collection, container, false);
        fab = view.findViewById(R.id.fab_shop);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("SHOP_ID", id);
                Fragment fragment = new AddCoffeeFragment();
                fragment.setArguments(args);
                replaceFragment(fragment);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        key = args.getInt("KEY");
        id = args.getString("DATA");
        ratingBar = view.findViewById(R.id.rating);
        rv = view.findViewById(R.id.detail_shop_list);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        initCollections();

        if (key == 1) {
            showCoffees();
        } else {
            showReviews();
        }

        ratingBar = view.findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Fragment addReviewFragment = new AddReviewFragment();
                Bundle args = new Bundle();
                args.putString("SHOP_ID", id);
                args.putFloat("RATING", ratingBar.getRating());
                addReviewFragment.setArguments(args);
                replaceFragment(addReviewFragment);
            }
        });
    }

    private void initCollections() {
        reviewController.getReviews(id, this);
        coffeeController.getCoffees(id, this);
        coffeeAdapter = new CoffeeAdapter(this.getContext(), coffees, this);
        reviewAdapter = new ReviewAdapter(this.getContext(), reviews, this, this);
    }

    private void showReviews() {
        fab.setVisibility(View.GONE);
        ratingBar.setVisibility(View.VISIBLE);
        rv.setAdapter(reviewAdapter);

    }

    private void showCoffees() {
        if (!User.getCurrentUser().getRole().equals("Admin")) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
        ratingBar.setVisibility(View.GONE);
        rv.setAdapter(coffeeAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Fragment reviewFragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString("SHOP_ID", id);
        args.putString("REVIEW_ID",reviews.get(position).getReviewId());
        reviewFragment.setArguments(args);
        replaceFragment(reviewFragment);
    }

    @Override
    public void onClickDelete(int position) {
        ReviewController controller = new ReviewController();
        controller.deleteReview(id, reviews.get(position), this);
    }

    @Override
    public void onCompleteCoffee(DocumentSnapshot docSnap) {}

    @Override
    public void onCompleteCoffeeCollection(QuerySnapshot querySnap) {
        for (QueryDocumentSnapshot documentSnapshot: querySnap) {
            Coffee coffee = documentSnapshot.toObject(Coffee.class);
            coffees.add(coffee);
        }
        coffeeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessCoffee() {}

    @Override
    public void onSuccessUpdateCoffee(Coffee coffee) {}

    @Override
    public void onCompleteReview(DocumentSnapshot docSnap) {}

    @Override
    public void onCompleteReviewCollection(QuerySnapshot querySnap) {
        for (QueryDocumentSnapshot document : querySnap) {
            Review rev = document.toObject(Review.class);
            reviews.add(rev);
        }
        reviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessUpdateReview(Review review) {}

    @Override
    public void onLikedReview(int position) {
        LikeController likeController = new LikeController();
        String userid = User.getCurrentUser().getUserId();
        if(!reviews.get(position).getLikers().contains(userid)){
            likeController.likeReview(id, User.getCurrentUser().getUserId(), reviews.get(position), this);
        } else{
            likeController.dislikeReview(id, User.getCurrentUser().getUserId(), reviews.get(position), this);
        }
    }

    @Override
    public void onSuccessReview() {
        returnFragment();
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
        Fragment coffeeShopFragment = new CoffeeShopFragment();
        Bundle args = new Bundle();
        args.putString("SHOP_ID", id);
        coffeeShopFragment.setArguments(args);
        replaceFragment(coffeeShopFragment);
    }
}