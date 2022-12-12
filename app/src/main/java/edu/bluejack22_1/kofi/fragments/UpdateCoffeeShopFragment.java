package edu.bluejack22_1.kofi.fragments;

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
import android.widget.EditText;

import com.bumptech.glide.Glide;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.controller.CoffeeShopController;
import edu.bluejack22_1.kofi.databinding.FragmentCoffeeShopBinding;
import edu.bluejack22_1.kofi.databinding.FragmentUpdateCoffeeShopBinding;
import edu.bluejack22_1.kofi.databinding.FragmentUpdateProfileBinding;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateCoffeeShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateCoffeeShopFragment extends Fragment implements FragmentInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText shopName, shopAddress, shopDescription;
    FragmentUpdateCoffeeShopBinding binding;
    private CoffeeShopController coffeeShopController;
    Uri ImageUri;
    ActivityResultLauncher<String> mImage;
    public UpdateCoffeeShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateCoffeeShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateCoffeeShopFragment newInstance(String param1, String param2) {
        UpdateCoffeeShopFragment fragment = new UpdateCoffeeShopFragment();
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
        binding = FragmentUpdateCoffeeShopBinding.inflate(inflater, container, false);
        shopName = binding.editShopName;
        shopAddress = binding.editShopAddress;
        shopDescription = binding.editShopDescription;
        coffeeShopController = new CoffeeShopController();
        String id = getArguments().getString("SHOP_ID");
        String name = getArguments().getString("NAME");
        String address = getArguments().getString("ADDRESS");
        String description = getArguments().getString("DESCRIPTION");
        String imageUrl = getArguments().getString("IMAGE");
        shopName.setText(name);
        shopAddress.setText(address);
        shopDescription.setText(description);
        Glide.with(binding.getRoot()).load(imageUrl).placeholder(R.drawable.default_profile).into(binding.coffeeshopimage);
        mImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>(){
            @Override
            public void onActivityResult(Uri result) {
                binding.coffeeshopimage.setImageURI(result);
                ImageUri = result;
            }
        });
        binding.coffeeshopimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImage.launch("image/*");
            }
        });
        FragmentInterface listener = this;
        binding.btnUpdateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateShop()){
                    String name = shopName.getText().toString();
                    String address = shopAddress.getText().toString();
                    String description = shopDescription.getText().toString();
                    coffeeShopController.updateCoffeeShop(id, name, address, description, ImageUri, listener);
                }
            }
        });
        return binding.getRoot();
    }

    private boolean validateShop(){
        String name = shopName.getText().toString();
        String address = shopAddress.getText().toString();
        String description = shopDescription.getText().toString();
        boolean check = true;
        if(name.length() == 0){
            shopName.setError("Shop Name Must Be filled");
            check = false;
        }
        if(address.length() == 0){
            shopAddress.setError("Shop Address Must Be filled");
            check = false;
        }
        if(description.length() == 0){
            shopDescription.setError("Shop Description Must be filled");
            check = false;
        }
        return check;
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void returnFragment() {
        String id = getArguments().getString("SHOP_ID");
        CoffeeShopFragment fragment = new CoffeeShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SHOP_ID", id);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }
}