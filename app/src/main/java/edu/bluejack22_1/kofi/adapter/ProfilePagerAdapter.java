package edu.bluejack22_1.kofi.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.bluejack22_1.kofi.fragments.ProfileCollectionFragment;

public class ProfilePagerAdapter extends FragmentStateAdapter {
    String id;

    public ProfilePagerAdapter(@NonNull FragmentActivity fragmentActivity, String id) {
        super(fragmentActivity);
        this.id = id;
    }

    public ProfilePagerAdapter(@NonNull Fragment fragment, String id) {
        super(fragment);
        this.id = id;
    }

    public ProfilePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String id) {
        super(fragmentManager, lifecycle);
        this.id = id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new ProfileCollectionFragment();
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
