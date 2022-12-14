package edu.bluejack22_1.kofi.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import edu.bluejack22_1.kofi.databinding.FragmentAddReviewBinding;
import edu.bluejack22_1.kofi.databinding.FragmentUpdateProfileBinding;
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
    private ImageView back, image;
    private String shopID;
    private ActivityResultLauncher<String> mImage;
    private FragmentAddReviewBinding binding;
    private Uri imageUri;

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
        binding = FragmentAddReviewBinding.inflate(inflater, container, false);
        Bundle args = getArguments();
        Float rating = args.getFloat("RATING");
        shopID = args.getString("SHOP_ID");

        ratingBar = binding.ratingReview;
        editReview = binding.textReview;
        button = binding.btnReview;
        back = binding.backAddReview;
        ratingBar.setRating(rating);
        image = binding.imageReview;

        onClickButton();
        onClickImage();

        return binding.getRoot();
    }

    private void onClickButton() {
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
    }

    private void onClickImage() {
        mImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                image.setImageURI(result);
                imageUri = result;
            }
        });
        image.setOnClickListener(view1 -> {
            mImage.launch("image/*");
        });

    }

    private void addReview() {
        Double ratingD = Double.valueOf(ratingBar.getRating());
        String content = editReview.getText().toString();
        if (content.trim().isEmpty()) {
            Log.d("REVIEW", "cannot be empty");
        } else {
            ReviewController controller = new ReviewController();
            controller.addReview(content.trim(), ratingD, shopID, imageUri, this);
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
    public void onLikedReview(int position) {

    }

    @Override
    public void onSuccessReview() {
        returnFragment();
    }
}