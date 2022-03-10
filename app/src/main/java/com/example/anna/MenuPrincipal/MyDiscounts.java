package com.example.anna.MenuPrincipal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anna.R;

public class MyDiscounts extends Fragment {

    private CustomAdapter myDiscountsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_fragment_mydiscounts, container, false);

        GridView gridMyDiscounts = (GridView) root.findViewById(R.id.mydiscountsgrid);
        myDiscountsAdapter = new CustomAdapter();
        gridMyDiscounts.setAdapter(myDiscountsAdapter);
        return root;

    }

    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
