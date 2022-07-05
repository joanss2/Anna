package com.example.anna.MenuPrincipal.Profile;


import static com.example.anna.Register.MainActivity.viewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.MenuPrincipal.MyDiscounts.FragmentMyDiscounts;
import com.example.anna.MenuPrincipal.MyDiscounts.MyDiscountsAdapter;
import com.example.anna.MenuPrincipal.MyRoutes.FragmentMyRoutes;
import com.example.anna.R;
import com.example.anna.databinding.ActivityFragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class FragmentProfile extends Fragment {

    private ActivityFragmentProfileBinding binding;
    private ViewPager2 viewPager2;
    private ImageView profileImageView;
    private ImageButton menu;
    private Button button;
    private TextView nOfDiscounts, nOfRoutes, profileName;
    private TabLayout tabLayout;
    private ProfileMenu profileMenu;
    private SharedPreferences userInfoPrefs;
    private SharedPreferences.Editor userInfoEditor;
    private FragmentStateAdapter pagerAdapter;
    private final int NUM_PAGES = 2;
    private final int icons [] ={R.drawable.ic_discount, R.drawable.ic_myroutes};
    private final String tabsNames[]={"My discounts","My Routes"};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPrefs.edit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = ActivityFragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        viewPager2 = binding.viewpagerprofile;
        profileImageView = binding.profileImageView;
        button = binding.editProfileButton;
        nOfDiscounts = binding.profileNumberDiscounts;
        nOfRoutes = binding.profileNumberRoutes;
        tabLayout = binding.profileTablayout;
        profileName = binding.profileName;
        menu = binding.menuProfile;

        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setIcon(icons[position]).setText(tabsNames[position])
        ).attach();

        profileName.setText(userInfoPrefs.getString("username",null));
        fillActivitiesInfo(userInfoPrefs.getString("userKey",null));


        String urlPicture = userInfoPrefs.getString("fotourl", null);//
        Glide.with(getContext()).load(urlPicture).into(profileImageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileMenu = new ProfileMenu();
                profileMenu.show(requireActivity().getSupportFragmentManager(),"bottomSheetSettings");
            }
        });

        return root;
    }


    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @Override
        public Fragment createFragment(int position) {
            if(position ==0){
                return new FragmentMyDiscounts();
            }else{
                return new FragmentMyRoutes();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    private void fillActivitiesInfo(String key){
        CollectionReference discountsUsedReference = FirebaseFirestore.getInstance().collection("DiscountsUsed");
        CollectionReference routesReference = FirebaseFirestore.getInstance().collection("Routes");

        discountsUsedReference.document(key).collection("DiscountsReferenceList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                    nOfDiscounts.setText(String.valueOf(task.getResult().size()));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        routesReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                    nOfRoutes.setText(String.valueOf(task.getResult().size()));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    /*

    private EditText userName, userMail, userTel;
    private SharedPreferences userInfoPrefs;
    private SharedPreferences.Editor userInfoEditor;
    private ActivityFragmentProfileBinding binding;
    private ImageView profileImageView;
    private String urlPicture;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private FloatingActionButton editButton;
    private boolean clicked;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
    private final DatabaseReference reference = database.getReference("users");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPrefs.edit();
        clicked = false;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ActivityFragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        userName = binding.profileUsername;
        userName.setClickable(false);
        userName.setEnabled(false);
        userName.setText(userInfoPrefs.getString("username", null));
        userMail = binding.profileUseremail;
        userMail.setText(userInfoPrefs.getString("email", null));
        userMail.setClickable(false);
        userMail.setEnabled(false);
        userTel = binding.profileUsertelefono;
        userTel.setText(userInfoPrefs.getString("usertel", null));
        profileImageView = binding.profileImageView;
        urlPicture = userInfoPrefs.getString("fotourl", null);//
        Glide.with(getContext()).load(urlPicture).into(profileImageView);

        editButton = (FloatingActionButton) root.findViewById(R.id.editnamebutton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked) {
                    userName.setEnabled(false);
                    clicked = false;
                    editButton.setImageResource(R.drawable.ic_edit);
                    changedUsername();
                    Toast.makeText(getContext(), "Username edited!", Toast.LENGTH_LONG).show();
                } else {
                    userName.setEnabled(true);
                    clicked = true;
                    editButton.setImageResource(R.drawable.ic_tick);
                }

            }
        });


        return root;
    }

    public void changedUsername() {
        userInfoEditor.remove("username");
        userInfoEditor.putString("username", userName.getText().toString());
        userInfoEditor.commit();
        Query query = reference.orderByChild("email").equalTo(FragmentProfile.this.userInfoPrefs.getString("email", null));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        DatabaseReference dr = database.getReference("users/" + ds.getKey());
                        dr.child("username").setValue(userName.getText().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

     */
}
