package com.example.anna.MenuPrincipal.Profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.CollaboratorMenu;
import com.example.anna.MenuPrincipal.LanguageManager;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.Models.LanguageItem;
import com.example.anna.Models.User;
import com.example.anna.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LanguagesDialog extends DialogFragment implements LanguagesAdapter.LanguageSelectionListener {

    private String[] names;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference ref = database.getReference("users");
    private DatabaseReference refAdmin = database.getReference("collaborators");
    private SharedPreferences sharedPreferences;
    private final int[] icons = {R.drawable.uk, R.drawable.germany, R.drawable.spain, R.drawable.catalunya, R.drawable.france, R.drawable.italia};
    private String currentLanguage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        names = new String[6];
        names[0] = getResources().getString(R.string.english);
        names[1] = getResources().getString(R.string.german);
        names[2] = getResources().getString(R.string.spanish);
        names[3] = getResources().getString(R.string.catalan);
        names[4] = getResources().getString(R.string.french);
        names[5] = getResources().getString(R.string.italian);
        sharedPreferences = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Select a language");

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.language_list, null);
        RecyclerView languagesRv = view.findViewById(R.id.languagesRv);
        languagesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        LanguagesAdapter languagesAdapter = new LanguagesAdapter(getContext(), getList(names, icons), this);
        languagesRv.setAdapter(languagesAdapter);


        builder.setView(view);

        return builder.create();
    }

    private List<LanguageItem> getList(String[] names, int[] icons) {
        List<LanguageItem> auxiliar = new ArrayList<>();
        for (int i = 0; i < names.length; i++)
            auxiliar.add(new LanguageItem(names[i], icons[i]));
        return auxiliar;
    }

    @Override
    public void onLangugeSelected(LanguageItem languageItem) {
        Toast.makeText(getContext(), "SELECTED LANGUAGE " + languageItem.getName(), Toast.LENGTH_SHORT).show();

        if (languageItem.getName().equals(getString(R.string.catalan)))
            currentLanguage = "ca";
        else if (languageItem.getName().equals(getString(R.string.spanish)))
            currentLanguage = "es";
        else if (languageItem.getName().equals(getString(R.string.french)))
            currentLanguage = "fr";
        else if (languageItem.getName().equals(getString(R.string.italian)))
            currentLanguage = "it";
        else if (languageItem.getName().equals(getString(R.string.german)))
            currentLanguage = "de";
        else
            currentLanguage = "en";

        if(sharedPreferences.getString("usertype",null).equals("client")){
            updateValues(ref,1);
        }else{
            updateValues(refAdmin,2);
        }

    }

    private void updateValues(DatabaseReference reference, int option){
        reference.orderByChild("userKey").equalTo(sharedPreferences.getString("userKey",null)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                            snapshot.getRef().child(sharedPreferences.getString("userKey",null)).child("language").setValue(currentLanguage)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            LanguageManager languageManager = new LanguageManager(getContext());
                                            languageManager.updateResource(currentLanguage);
                                            //requireActivity().recreate();
                                            getActivity().finish();
                                            //getActivity().recreate();
                                            if(option==1)
                                                startActivity(new Intent(getContext(),MenuMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                                            else
                                                startActivity(new Intent(getContext(), CollaboratorMenu.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                                        }
                                    });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }


}
