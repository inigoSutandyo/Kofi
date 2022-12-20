package edu.bluejack22_1.kofi.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.bluejack22_1.kofi.LoginActivity;
import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.controller.UserController;
import edu.bluejack22_1.kofi.databinding.FragmentUpdateProfileBinding;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.UserListener;
import edu.bluejack22_1.kofi.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfileFragment extends Fragment implements FragmentInterface, UserListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Uri ImageUri;
    ActivityResultLauncher<String> mImage;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FragmentUpdateProfileBinding binding;
    EditText eFullname, eAddress;
    Button updateBtn;
    UserController controller;
    User userData;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button deleteAccBtn;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }
    public UpdateProfileFragment(User user){
        userData = user;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateProfileFragment newInstance(String param1, String param2) {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
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

        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);
        eFullname = binding.txtUpdateName;
        controller = new UserController();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        eAddress = binding.txtUpdateAddress;
        updateBtn = binding.btnUpdateProfile;
        storage = FirebaseStorage.getInstance();
        currentUser = mAuth.getCurrentUser();
        deleteAccBtn = binding.btnDeleteAcc;
        storageReference = storage.getReference().child("images/"+ currentUser.getUid());
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this.getActivity(), gso);
        Glide.with(binding.getRoot()).load(userData.getImageUrl()).placeholder(R.drawable.default_profile).into(binding.updateProfileImage);

        eFullname.setText(userData.getFullName());
        eAddress.setText(userData.getAddress());


        mImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>(){
            @Override
            public void onActivityResult(Uri result) {
                binding.updateProfileImage.setImageURI(result);
                ImageUri = result;
            }
        });
        binding.updateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImage.launch("image/*");
            }
        });

        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eFullname.getText().toString();
                String address = eAddress.getText().toString();
                updateProfile(currentUser.getUid(), name, address, ImageUri);
            }
        });

        deleteAccBtn.setOnClickListener(v -> {
            controller.deleteUser(this);
        });

        return binding.getRoot();
    }

    public void updateProfile(String uid, String fullName, String address, Uri uri){
        controller = new UserController();
        controller.updateUser(uid, fullName, address, uri, this);
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void returnFragment() {

    }

    @Override
    public void onCompleteUser(DocumentSnapshot docSnap) {}

    @Override
    public void onCompleteUserCollection(QuerySnapshot querySnap) {}

    @Override
    public void onSuccessUpdateUser(User user) {
        User.setCurrentUser(user);
        replaceFragment(new ProfileFragment());
    }

    @Override
    public void onSuccessUser() {
        getActivity().finish();
        mAuth.signOut();
        gsc.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        User.setCurrentUser(null);
    }
}