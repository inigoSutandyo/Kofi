package edu.bluejack22_1.kofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import edu.bluejack22_1.kofi.LoginActivity;
import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CoffeeShopPagerAdapter;
import edu.bluejack22_1.kofi.adapter.ProfilePagerAdapter;
import edu.bluejack22_1.kofi.adapter.ReviewAdapter;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.interfaces.listeners.UserListener;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements
        FragmentInterface,
        RecyclerViewInterface,
        ReviewListener,
        UserListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    FirebaseStorage storage;
    StorageReference storageReference;

    private TextView nameTxt, emailTxt, addressTxt, editProfileBtn;
    private ImageView profileImage, logoutBtn;
    public User tempUser;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private ReviewAdapter reviewAdapter;
    private ReviewController reviewController;
    private RecyclerView recyclerView;
    private ArrayList<Review> reviews;

    private ProfilePagerAdapter profilePagerAdapter;
    private ViewPager2 viewPager2;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
        tempUser = User.getCurrentUser();
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
        currentUser = mAuth.getCurrentUser();
        storageReference = storage.getReference().child("images/"+ currentUser.getUid());
        nameTxt.setText(tempUser.getFullName());
        emailTxt.setText(tempUser.getEmail());
        addressTxt.setText(tempUser.getAddress());
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this.getActivity(), gso);
        Glide.with(view).load(tempUser.getImageUrl()).placeholder(R.drawable.default_profile).into(profileImage);

        editProfileBtn = view.findViewById(R.id.edit_profile_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference ref = db.collection("users").document(currentUser.getUid());
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
                logout();
            }
        });



        return view;
    }

    private void logout() {
        getActivity().finish();
        mAuth.signOut();
        gsc.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        User.setCurrentUser(null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this.getContext(), reviews, this, this);
        reviewController = new ReviewController();
        recyclerView = view.findViewById(R.id.profile_review_list);
        recyclerView.setAdapter(reviewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        reviewController.getMyReviews(tempUser, this);

        profilePagerAdapter = new ProfilePagerAdapter(this, currentUser.getUid());
        viewPager2 = view.findViewById(R.id.profile_pager);
        viewPager2.setAdapter(profilePagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.profile_tab);
        if(Locale.getDefault().toString().equals("en_US")) {
            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(position == 1 ? "Reviews" : "Favorites")).attach();
        } else{
            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(position == 1 ? "Ulasan" : "Favorit")).attach();
        }
    }

    @Override
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void returnFragment() {}

    @Override
    public void onItemClick(int position) {}

    @Override
    public void onClickDelete(int position) {}

    @Override
    public void onCompleteReview(DocumentSnapshot docSnap) {
        if (docSnap.exists()) {
            Review rev = docSnap.toObject(Review.class);
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
    public void onLikedReview(int position) {

    }

    @Override
    public void onSuccessReview() {

    }

    @Override
    public void onCompleteUser(DocumentSnapshot docSnap) {}

    @Override
    public void onCompleteUserCollection(QuerySnapshot querySnap) {}

    @Override
    public void onSuccessUpdateUser(User user) {}

    @Override
    public void onSuccessUser() {
        logout();
    }
}