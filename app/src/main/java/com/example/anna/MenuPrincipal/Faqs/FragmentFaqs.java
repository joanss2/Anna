package com.example.anna.MenuPrincipal.Faqs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anna.databinding.ActivityFragmentFaqsBinding;

public class FragmentFaqs extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        com.example.anna.databinding.ActivityFragmentFaqsBinding binding = ActivityFragmentFaqsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        TextView contactlink = binding.contactusFromFaqs;
        contactlink.setOnClickListener(v -> startActivity(new Intent(getActivity(), Faqs.class)));

        return root;
    }
}