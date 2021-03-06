package com.example.anna.Register;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.anna.Register.FragmentsViewPager.SignInFragment;
import com.example.anna.Register.FragmentsViewPager.SignUpFragment;

public class PagerController extends FragmentPagerAdapter {

    int numOfTabs;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new SignUpFragment();
        }
        return new SignInFragment();
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
