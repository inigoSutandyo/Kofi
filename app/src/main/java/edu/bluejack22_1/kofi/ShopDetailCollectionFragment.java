package edu.bluejack22_1.kofi;

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

import java.util.ArrayList;

import edu.bluejack22_1.kofi.adapter.ReviewAdapter;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.model.Review;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopDetailCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopDetailCollectionFragment extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ReviewController reviewController;
    private RecyclerView rv;
    private ReviewAdapter reviewAdapter;
    private String id;
    private int key;

    public ShopDetailCollectionFragment() {
        // Required empty public constructor
        reviewController = new ReviewController();
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
        return inflater.inflate(R.layout.fragment_review_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        key = args.getInt("KEY");
        id = args.getString("DATA");
        if (key == 1) {
            initReviews(view);
        } else {
            Log.d("Coffee", "Not Review");
        }
    }

    private void initReviews(View view) {
        ArrayList<Review> reviews = new ArrayList<>();
        rv = view.findViewById(R.id.detail_shop_list);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        reviewAdapter = new ReviewAdapter(this.getContext(), reviews, this);

        rv.setAdapter(reviewAdapter);
        Log.d("Coffee", "ID = " + id);
        reviewController.populateReviews(id, reviews, reviewAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }
}