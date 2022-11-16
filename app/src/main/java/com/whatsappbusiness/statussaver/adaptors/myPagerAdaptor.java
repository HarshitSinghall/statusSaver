package com.whatsappbusiness.statussaver.adaptors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.whatsappbusiness.statussaver.fragments.imagesFragment;
import com.whatsappbusiness.statussaver.fragments.videoFragment;
import com.whatsappbusiness.statussaver.fragments.savedFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class myPagerAdaptor extends FragmentStateAdapter {

    public myPagerAdaptor(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new imagesFragment();
            case 1:
                return new videoFragment();
            case 2:
                return new savedFragment();
            default:
                return new imagesFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
