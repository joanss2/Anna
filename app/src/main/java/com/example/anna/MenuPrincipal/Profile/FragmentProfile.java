package com.example.anna.MenuPrincipal.Profile;

import android.content.Context;
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
import com.example.anna.MenuPrincipal.MyDiscounts.FragmentMyDiscounts;
import com.example.anna.MenuPrincipal.MyRoutes.FragmentMyRoutes;
import com.example.anna.R;
import com.example.anna.databinding.ActivityFragmentProfileBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class FragmentProfile extends Fragment {

    private TextView nOfDiscounts;
    private TextView nOfRoutes;
    private ProfileMenu profileMenu;
    private SharedPreferences userInfoPrefs;
    private final int[] icons = {R.drawable.ic_discount, R.drawable.ic_myroutes};
    private final String[] tabsNames = {"My discounts", "My Routes"};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        com.example.anna.databinding.ActivityFragmentProfileBinding binding = ActivityFragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        ViewPager2 viewPager2 = binding.viewpagerprofile;
        ImageView profileImageView = binding.profileImageView;
        Button button = binding.editProfileButton;
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
        fillActivitiesInfo(userInfoPrefs.getString("userKey", null));


        String urlPicture = userInfoPrefs.getString("fotourl", null);
        Glide.with(requireContext()).load(urlPicture).into(profileImageView);
        button.setOnClickListener(v -> {
        });

        menu.setOnClickListener(v -> {
            profileMenu = new ProfileMenu();
            profileMenu.show(requireActivity().getSupportFragmentManager(), "bottomSheetSettings");
        });

        return root;
    }


    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new FragmentMyDiscounts();
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
        CollectionReference routesReference = FirebaseFirestore.getInstance().collection("Routes");

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

}
