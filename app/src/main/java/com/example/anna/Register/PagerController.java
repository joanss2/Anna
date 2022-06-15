package com.example.anna.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.anna.Register.FragmentsViewPager.SignInFragment;
import com.example.anna.Register.FragmentsViewPager.SignUpFragment;

public class PagerController extends FragmentPagerAdapter {

    int numOfTabs;
    String mail;

    public PagerController(@NonNull FragmentManager fm, int behavior, String mail) {
        super(fm, behavior);
        this.numOfTabs = behavior;
        this.mail = mail;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SignUpFragment();
            case 1:
                SignInFragment signInFragment = new SignInFragment();

                if(this.mail!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("emailfromsignup", this.mail);
                    signInFragment.setArguments(bundle);

                }
                return signInFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
