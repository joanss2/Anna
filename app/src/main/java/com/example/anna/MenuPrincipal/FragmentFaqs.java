package com.example.anna.MenuPrincipal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anna.Faqs;
import com.example.anna.databinding.ActivityFragmentFaqsBinding;

public class FragmentFaqs extends Fragment {

    private ActivityFragmentFaqsBinding binding;
    private TextView contactlink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ActivityFragmentFaqsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        contactlink = binding.contactusFromFaqs;
        contactlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Faqs.class));
            }
        });

        return root;
    }
}