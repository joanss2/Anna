package com.example.anna.MenuPrincipal.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.anna.MenuPrincipal.Discounts.MyDiscounts.FragmentMyDiscounts;

import com.example.anna.MenuPrincipal.Routes.MyRoutes.FragmentMyRoutes;
import com.example.anna.R;
import com.example.anna.databinding.ActivityFragmentProfileBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;


public class FragmentProfile extends Fragment {

    private TextView nOfDiscounts, nOfRoutes;
    private ProfileMenu profileMenu;
    private SharedPreferences userInfoPrefs;
    private final int[] icons = {R.drawable.ic_discount, R.drawable.ic_myroutes};
    private final String[] tabsNames = {"My discounts", "My Routes"};
    private StorageReference storageReference;
    private String authorKey;
    public static Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        authorKey = userInfoPrefs.getString("userKey", null);
        storageReference = FirebaseStorage.getInstance().getReference("users").child(authorKey);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        com.example.anna.databinding.ActivityFragmentProfileBinding binding = ActivityFragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        ViewPager2 viewPager2 = binding.viewpagerprofile;
        ImageView profileImageView = binding.profileImageView;
        Button editProfileButton = binding.editProfileButton;
        nOfDiscounts = binding.profileNumberDiscounts;
        nOfRoutes = binding.profileNumberRoutes;
        TabLayout tabLayout = binding.profileTablayout;
        TextView profileName = binding.profileName;
        ImageButton menu = binding.menuProfile;

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setIcon(icons[position]).setText(tabsNames[position])
        ).attach();

        profileName.setText(userInfoPrefs.getString("username", null));
        fillActivitiesInfo(authorKey);

        loadPicture(storageReference, profileImageView);
        editProfileButton.setOnClickListener(v -> startActivity(new Intent(getContext(), EditProfile.class)));

        menu.setOnClickListener(v -> {
            profileMenu = new ProfileMenu();
            profileMenu.show(requireActivity().getSupportFragmentManager(), "bottomSheetSettings");
        });

        return root;
    }



    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        Fragment fragment;

        public ScreenSlidePagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
            this.fragment = fragment;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new FragmentMyDiscounts(fragment.getView());
            } else {
                return new FragmentMyRoutes();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    private void fillActivitiesInfo(String key) {
        CollectionReference discountsUsedReference = FirebaseFirestore.getInstance().collection("DiscountsUsed");
        CollectionReference routesReference = FirebaseFirestore.getInstance().collection("CompletedRoutes");

        discountsUsedReference.document(key).collection("DiscountsReferenceList").get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                nOfDiscounts.setText(String.valueOf(task.getResult().size()));
        }).addOnFailureListener(e -> {

        });

        routesReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                nOfRoutes.setText(String.valueOf(task.getResult().size()));
        }).addOnFailureListener(e -> {

        });
    }
    private void loadPicture(StorageReference storageReference, ImageView userPicture) {

        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(FragmentProfile.context).load(uri).into(userPicture)).addOnFailureListener(e -> {
            int errorCode = ((StorageException) e).getErrorCode();
            if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                userPicture.setImageResource(R.drawable.user_icon);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println(requireActivity().getSupportFragmentManager().getBackStackEntryCount()+ " AL resume DE profile");

    }
}
