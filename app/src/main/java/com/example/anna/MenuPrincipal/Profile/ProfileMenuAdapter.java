package com.example.anna.MenuPrincipal.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.R;

import java.util.List;

public class ProfileMenuAdapter extends RecyclerView.Adapter<ProfileMenuAdapter.MenuItemHolder> {

    private final Context context;
    private final List<String> settingsList;
    private final OnSettingClickListener onSettingClickListener;

    public ProfileMenuAdapter(List<String> settingsList, Context context, OnSettingClickListener onSettingClickListener){
        this.context = context;
        this.settingsList = settingsList;
        this.onSettingClickListener = onSettingClickListener;
    }

    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_menu_item, parent, false);
        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemHolder holder, int position) {
        String current = settingsList.get(position);
        holder.bind(current);
    }


    @Override
    public int getItemCount() {
        return this.settingsList.size();
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textView;

        public MenuItemHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.profileItemTextView);
            itemView.setOnClickListener(this);
        }
        public void bind(String currentSetting){
            this.textView.setText(currentSetting);
        }

        @Override
        public void onClick(View v) {
            onSettingClickListener.onSettingClick(textView.getText().toString());
            System.out.println(textView.getText().toString());
        }
    }

    public interface OnSettingClickListener{
        void onSettingClick(String currentSetting);
    }
}
