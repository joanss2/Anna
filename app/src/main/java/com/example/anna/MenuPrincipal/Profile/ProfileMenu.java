package com.example.anna.MenuPrincipal.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.anna.MenuPrincipal.Faqs.FragmentFaqs;
import com.example.anna.MenuPrincipal.MyDiscounts.FragmentMyDiscounts;
import com.example.anna.MenuPrincipal.MyRoutes.FragmentMyRoutes;
import com.example.anna.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.Arrays;
import java.util.List;

public class ProfileMenu extends BottomSheetDialogFragment implements ProfileMenuAdapter.OnSettingClickListener {


    private final String [] itemsArray = {"Language","MyDiscounts","MyRoutes","Network Settings","Privacy","Help/FAQS"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_menu, container, false);
        RecyclerView listView = view.findViewById(R.id.profileMenuListview);

        List<String> itemsList = Arrays.asList(itemsArray);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProfileMenuAdapter profileMenuAdapter = new ProfileMenuAdapter(itemsList, getContext(), this);

        listView.setAdapter(profileMenuAdapter);

        return view;
    }

    @Override
    public void onSettingClick(String currentSetting) {
        switch (currentSetting){
            case "Language":
            case "Privacy":
            case "Network Settings":
                Toast.makeText(getContext(), "HOLE", Toast.LENGTH_SHORT).show();
                break;
            case "MyDiscounts":
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new FragmentMyDiscounts()).addToBackStack(null).commit();
                break;
            case "MyRoutes":
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentMyRoutes()).addToBackStack(null).commit();
                break;
            case "Help/FAQS":
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentFaqs()).addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
