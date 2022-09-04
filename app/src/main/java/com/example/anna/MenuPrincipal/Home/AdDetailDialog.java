package com.example.anna.MenuPrincipal.Home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.anna.Models.Discount;
import com.example.anna.Models.HotNews;
import com.example.anna.Models.User;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;

import java.text.SimpleDateFormat;

public class AdDetailDialog extends DialogFragment {

    private HotNews hotNews;
    private Uri uriAd, uriUser;
    private String authorName;

    public AdDetailDialog(HotNews hotNews, Uri uriAd, Uri uriUser, String authorName) {
        this.hotNews = hotNews;
        this.uriAd = uriAd;
        this.uriUser = uriUser;
        this.authorName = authorName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.ad_detail, null);

        builder.setView(view);

        ImageView adImage = view.findViewById(R.id.adDetailImage);
        TextView title = view.findViewById(R.id.adDetailTitle);
        TextView description = view.findViewById(R.id.adDetailDescription);
        TextView date = view.findViewById(R.id.adDetailDate);
        TextView ownerUsername = view.findViewById(R.id.adDetailOwnerUsername);
        ImageView ownerImage = view.findViewById(R.id.adDetailOwnerImage);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date.setText(simpleDateFormat.format(hotNews.getEndDate()));
        title.setText(hotNews.getTitle());
        description.setText(hotNews.getDescription());
        ownerUsername.setText(authorName);

        Glide.with(requireContext()).load(uriAd).into(adImage);

        if(uriUser == null){
            ownerImage.setImageResource(R.drawable.user_icon);
        }else{
            Glide.with(requireContext()).load(uriUser).into(ownerImage);
        }

        return builder.create();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_detail, container, false);
    }



}
