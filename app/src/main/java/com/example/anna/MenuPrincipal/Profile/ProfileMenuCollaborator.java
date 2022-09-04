package com.example.anna.MenuPrincipal.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.Faqs.FragmentFaqs;
import com.example.anna.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;
import java.util.List;

public class ProfileMenuCollaborator extends BottomSheetDialogFragment implements ProfileMenuAdapter.OnSettingClickListener {


    private Context context;

    public ProfileMenuCollaborator(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String[] itemsArray = {context.getResources().getString(R.string.language),
                context.getResources().getString(R.string.privacy),
                context.getResources().getString(R.string.helpFAQS)};

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
        switch (currentSetting) {
            case "Idioma":
            case "Language":
                LanguagesDialog languagesDialog = new LanguagesDialog();
                languagesDialog.show(requireActivity().getSupportFragmentManager(), null);
            case "Privacy":
                break;

            case "Ajuda/FAQS":
            case "Help/FAQS":
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.collaborator_main_frame, new FragmentFaqs()).addToBackStack(null).commit();
                this.dismiss();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
