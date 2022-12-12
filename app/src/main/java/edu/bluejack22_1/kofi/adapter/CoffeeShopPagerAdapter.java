package edu.bluejack22_1.kofi.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.bluejack22_1.kofi.fragments.ShopCollectionFragment;

public class CoffeeShopPagerAdapter extends FragmentStateAdapter {
    private String id;
    public CoffeeShopPagerAdapter(@NonNull FragmentActivity fragmentActivity, String id) {
        super(fragmentActivity);
        this.id = id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new ShopCollectionFragment();
        Bundle args = new Bundle();
        args.putInt("KEY", position+1);
        args.putString("DATA", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
