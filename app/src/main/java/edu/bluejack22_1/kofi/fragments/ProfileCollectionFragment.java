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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CoffeeShopAdapter;
import edu.bluejack22_1.kofi.adapter.ReviewAdapter;
import edu.bluejack22_1.kofi.controller.CoffeeShopController;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.controller.UserController;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeShopListener;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.interfaces.listeners.UserListener;
import edu.bluejack22_1.kofi.model.CoffeeShop;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileCollectionFragment extends Fragment implements
        UserListener,
        ReviewListener,
        CoffeeShopListener,
        RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int key;
    private String id;
    private User user;
    private ArrayList<Review> reviews;
    private ArrayList<CoffeeShop> coffeeShops;
    private UserController userController;
    private ReviewController reviewController;
    private CoffeeShopController coffeeShopController;
    private ReviewAdapter reviewAdapter;
    private CoffeeShopAdapter coffeeShopAdapter;
    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileCollectionFragment() {
        // Required empty public constructor
        userController = new UserController();
        reviewController = new ReviewController();
        coffeeShopController = new CoffeeShopController();

        reviews = new ArrayList<>();
        coffeeShops = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileCollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileCollectionFragment newInstance(String param1, String param2) {
        ProfileCollectionFragment fragment = new ProfileCollectionFragment();
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
        return inflater.inflate(R.layout.fragment_profile_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        key = args.getInt("KEY");
        id = args.getString("DATA");
        reviewAdapter = new ReviewAdapter(this.getContext(), reviews, this, this);
        coffeeShopAdapter = new CoffeeShopAdapter(this.getContext(), coffeeShops, this);
        recyclerView = view.findViewById(R.id.profile_collection_recycler);
        userController.getUserById(User.getCurrentUser().getUserId(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        if (key == 1) {
            showCoffees();
        } else {
            showReviews();
        }
    }

    private void showCoffees() {
        recyclerView.setAdapter(coffeeShopAdapter);
    }

    private void showReviews() {
        recyclerView.setAdapter(reviewAdapter);
    }

    @Override
    public void onCompleteUser(DocumentSnapshot docSnap) {
        this.user = docSnap.toObject(User.class);
        user.setUserId(docSnap.getId());
        reviewController.getMyReviews(user, this);
        coffeeShopController.getFavoriteShops(user, this);
    }

    @Override
    public void onCompleteUserCollection(QuerySnapshot querySnap) {}

    @Override
    public void onSuccessUpdateUser(User user) {}

    @Override
    public void onSuccessUser() {}

    @Override
    public void onCompleteReview(DocumentSnapshot docSnap) {
        Review review = docSnap.toObject(Review.class);
        if (review == null || review.getUser() == null) {
            return;
        }
        reviews.add(review);

        reviewAdapter.notifyDataSetChanged();;
    }

    @Override
    public void onCompleteReviewCollection(QuerySnapshot querySnap) {}

    @Override
    public void onSuccessUpdateReview(Review review) {}

    @Override
    public void onLikedReview(int position) {}

    @Override
    public void onSuccessReview() {}

    @Override
    public void onCompleteShop(DocumentSnapshot docSnap) {
        CoffeeShop cf = docSnap.toObject(CoffeeShop.class);
        if (cf == null) {
            return;
        }
        coffeeShops.add(cf);
        coffeeShopAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleteShopCollection(QuerySnapshot querySnap) {}

    @Override
    public void onSuccessUpdateShop(CoffeeShop coffeeShop) {}

    @Override
    public void onSuccessShop() {}

    @Override
    public void onDeleteShop() {}

    @Override
    public void onItemClick(int position) {}

    @Override
    public void onClickDelete(int position) {}
}