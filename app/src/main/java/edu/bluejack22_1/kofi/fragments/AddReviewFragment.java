package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.model.Review;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReviewFragment extends Fragment implements FragmentInterface, ReviewListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RatingBar ratingBar;
    private EditText editReview;
    private Button button;
    private ImageView back;
    private String shopID;

    public AddReviewFragment() {
        // Required empty public constructor
    }

    public static AddReviewFragment newInstance(String param1, String param2) {
        AddReviewFragment fragment = new AddReviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);

        Bundle args = getArguments();
        Float rating = args.getFloat("RATING");
        shopID = args.getString("SHOP_ID");

        ratingBar = view.findViewById(R.id.rating_review);
        editReview = view.findViewById(R.id.text_review);
        button = view.findViewById(R.id.btn_review);
        back = view.findViewById(R.id.back_add_review);
        ratingBar.setRating(rating);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReview();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnFragment();
            }
        });
        return  view;
    }

    private void addReview() {
        Double ratingD = Double.valueOf(ratingBar.getRating());
        String content = editReview.getText().toString();
        if (content.trim().isEmpty()) {
            Log.d("REVIEW", "cannot be empty");
        } else {
            ReviewController controller = new ReviewController();
            controller.addReview(content.trim(), ratingD, shopID, this);
            Log.d("REVIEW", "success");
        }
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
        args.putString("SHOP_ID", shopID);
        coffeeShopFragment.setArguments(args);
        replaceFragment(coffeeShopFragment);
    }

    @Override
    public void onCompleteReview(DocumentSnapshot docSnap) {

    }

    @Override
    public void onCompleteReviewCollection(QuerySnapshot querySnap) {

    }

    @Override
    public void onSuccessUpdateReview(Review review) {}

    @Override
    public void onSuccessReview() {
        returnFragment();
    }
}