package edu.bluejack22_1.kofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import edu.bluejack22_1.kofi.LoginActivity;
import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.ReviewAdapter;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements
        FragmentInterface,
        RecyclerViewInterface,
        ReviewListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentuser;
    FirebaseStorage storage;
    StorageReference storageReference;

    private TextView nameTxt, emailTxt, addressTxt, editProfileBtn;
    private ImageView profileImage, logoutBtn;
    public User tempUser;

    private ReviewAdapter reviewAdapter;
    private ReviewController reviewController;
    private RecyclerView recyclerView;
    private ArrayList<Review> reviews;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(User user) {
        // Required empty public constructor
        tempUser = user;
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameTxt = view.findViewById(R.id.profile_username);
        emailTxt = view.findViewById(R.id.profile_email);
        addressTxt = view.findViewById(R.id.profile_address);
        profileImage = view.findViewById(R.id.profile_image);
        logoutBtn = view.findViewById(R.id.profile_logout);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        currentuser = mAuth.getCurrentUser();
        storageReference = storage.getReference().child("images/"+currentuser.getUid());
        nameTxt.setText(tempUser.getFullName());
        emailTxt.setText(tempUser.getEmail());
        addressTxt.setText(tempUser.getAddress());

        Glide.with(view).load(tempUser.getImageUrl()).placeholder(R.drawable.defaultprofile).into(profileImage);

        editProfileBtn = view.findViewById(R.id.edit_profile_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference ref = db.collection("users").document(currentuser.getUid());
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            tempUser = task.getResult().toObject(User.class);
                            replaceFragment(new UpdateProfileFragment(tempUser));
                        } else {
                            Log.d("User", "Error getting user", task.getException());
                        }
                    }
                });
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this.getContext(), reviews, this);
        reviewController = new ReviewController();
        recyclerView = view.findViewById(R.id.profile_review_list);
        recyclerView.setAdapter(reviewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        reviewController.getMyReviews(tempUser, this);
    }

    @Override
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onCompleteReview(DocumentSnapshot docSnap) {
        if (docSnap.exists()) {
            String content = (String) docSnap.getData().get("content");
            String rating = (String) docSnap.getData().get("rating");
            double ratingD = Double.parseDouble(rating);

            String reviewId = docSnap.getId();
            Review rev = new Review(content, ratingD, reviewId);
            rev.setUser(tempUser);
            reviews.add(rev);
        }
        reviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleteReviewCollection(QuerySnapshot querySnap) {

    }

    @Override
    public void onSuccessUpdateReview(Review review) {

    }

    @Override
    public void onSuccessReview() {

    }
}