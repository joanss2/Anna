package com.example.anna.MenuPrincipal.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.anna.Alerts.ActionForbiddenAlert;
import com.example.anna.Alerts.AlertManager;
import com.example.anna.Models.HotNews;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class MoreDialog extends DialogFragment {

    private SharedPreferences userInfoPrefs;
    private HotNews hotNews;
    private AlertManager alertManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        alertManager = new AlertManager((AppCompatActivity) requireActivity());
    }



    public MoreDialog(HotNews hotNews) {
        this.hotNews = hotNews;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.options))
                .setItems(R.array.string_more, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!hotNews.getAuthor().equals(userInfoPrefs.getString("userKey", null)))
                            alertManager.showAlert(new ActionForbiddenAlert(requireContext()));
                        else{
                            if (which == 0) {
                                /*EDIT*/
                                editAd(hotNews);
                            } else {
                                /*DELETE*/
                                deleteAd(hotNews);

                            }
                        }

                    }
                });
        return builder.create();

    }

    private void editAd(HotNews hotNewslocal) {
        Intent intent = new Intent(requireContext(),AdEdition.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",hotNewslocal.getTitle());
        bundle.putString("description",hotNewslocal.getDescription());
        bundle.putString("date",hotNewslocal.getEndDate().toString());
        bundle.putString("adKey",hotNewslocal.getKey());
        bundle.putString("authorKey",hotNewslocal.getAuthor());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void deleteAd(HotNews hotNews) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Confirmation")
        .setMessage("Are you sure you want to delete this ad ? It will be deleted from our databases as well.")
        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmDelete(hotNews);
            }
        }).create().show();
    }

    private void confirmDelete(HotNews hotNews) {
        FirebaseFirestore.getInstance().collection("Advertisements").document(hotNews.getAuthor())
                .collection("AdsOfUser").document(hotNews.getKey()).delete().addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseFirestore.getInstance().collection("AllAds").whereEqualTo("key",hotNews.getKey()).get().addOnCompleteListener(
                                new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            String id = task.getResult().getDocuments().get(0).getId();
                                            FirebaseFirestore.getInstance().collection("AllAds").document(id).delete().addOnCompleteListener(
                                                    new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseStorage.getInstance().getReference("advertisements").child(hotNews.getKey()).delete().addOnCompleteListener(
                                                                    new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                        }
                                                                    }
                                                            );
                                                        }
                                                    }
                                            );
                                        }
                                    }
                                }
                        );
                    }
                }
        );
    }
}
