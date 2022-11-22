package edu.bluejack22_1.kofi.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.bluejack22_1.kofi.fragments.ShopDetailCollectionFragment;

public class ShopPagerAdapter extends FragmentStateAdapter {

    public ShopPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new ShopDetailCollectionFragment();
        Bundle args = new Bundle();
        args.putInt("Review Data", position+1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
