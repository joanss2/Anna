package com.example.anna.MenuPrincipal.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.Faqs.FragmentFaqs;
import com.example.anna.MenuPrincipal.Home.FragmentHome;
import com.example.anna.MenuPrincipal.MyDiscounts.FragmentMyDiscounts;
import com.example.anna.MenuPrincipal.MyRoutes.FragmentMyRoutes;
import com.example.anna.MenuPrincipal.Routes.FragmentRoutes;
import com.example.anna.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileMenu extends BottomSheetDialogFragment implements ProfileMenuAdapter.OnSettingClickListener {


    private RecyclerView listView;
    private ProfileMenuAdapter profileMenuAdapter;
    private String [] itemsArray = {"Language","MyDiscounts","MyRoutes","Network Settings","Privacy","Help/FAQS"};
    private List<String> itemsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_menu, container, false);
        listView = view.findViewById(R.id.profileMenuListview);

        itemsList = Arrays.asList(itemsArray);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileMenuAdapter = new ProfileMenuAdapter(itemsList,getContext(),this);

        listView.setAdapter(profileMenuAdapter);

        return view;
    }

    @Override
    public void onSettingClick(String currentSetting) {
        switch (currentSetting){
            case "Language":
                Toast.makeText(getContext(), "HOLA", Toast.LENGTH_SHORT).show();
                break;
            case "MyDiscounts":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new FragmentMyDiscounts()).addToBackStack(null).commit();
                break;
            case "MyRoutes":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentMyRoutes()).addToBackStack(null).commit();
                break;
            case "Network Settings":
                Toast.makeText(getContext(), "HOLA", Toast.LENGTH_SHORT).show();
                break;
            case "Privacy":
                Toast.makeText(getContext(), "HOLA", Toast.LENGTH_SHORT).show();
                break;
            case "Help/FAQS":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentFaqs()).addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

/*

    private BottomSheetDialog menuDialog;
    private Context context;
    private ListView listView;
    private List<String> items;
    private String [] itemsArray = {"Language","MyDiscounts","MyRoutes","Network Settings","Privacy","Help/FAQS"};

    public ProfileMenu(Context context){

        this.context = context;
        this.menuDialog = new BottomSheetDialog(context);
        this.menuDialog.setContentView(R.layout.profile_menu);
        this.listView = menuDialog.findViewById(R.id.profileMenuListview);


        this.items = Arrays.asList(itemsArray);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(context,R.layout.profile_menu_item, items);
        listView.setAdapter(itemsAdapter);


        menuDialog.show();

    }

     */
}
