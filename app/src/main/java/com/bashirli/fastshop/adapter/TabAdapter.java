package com.bashirli.fastshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bashirli.fastshop.view.fragment.AllCatFragment;
import com.bashirli.fastshop.view.fragment.MenFragment;
import com.bashirli.fastshop.view.fragment.ScreenFragment;
import com.bashirli.fastshop.view.fragment.WomenFragment;

public class TabAdapter extends FragmentStateAdapter {
    public TabAdapter(@NonNull ScreenFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new AllCatFragment();
            case 1: return new MenFragment();
            case 2: return new WomenFragment();
            default: return new AllCatFragment();

        }
    }

    @Override
    public int getItemCount() {
        return  3;
    }
}
