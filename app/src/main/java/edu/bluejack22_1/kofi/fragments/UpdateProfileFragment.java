package edu.bluejack22_1.kofi.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.controller.UserController;
import edu.bluejack22_1.kofi.databinding.FragmentUpdateProfileBinding;
import edu.bluejack22_1.kofi.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfileFragment extends Fragment {

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
        storageReference = storage.getReference().child("images/"+ currentUser.getUid());

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(binding.getRoot()).load(uri).placeholder(R.drawable.defaultprofile).into(binding.updateProfileImage);
            }
        });

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
        return binding.getRoot();
    }

    public void updateProfile(String uid, String fullname, String address, Uri uri){
        controller = new UserController();
        controller.UpdateUser(uid, fullname, address, uri, this);
    }


}