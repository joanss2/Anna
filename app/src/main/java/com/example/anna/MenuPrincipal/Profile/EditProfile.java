package com.example.anna.MenuPrincipal.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.example.anna.R;
import com.example.anna.databinding.EditProfileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

public class EditProfile extends AppCompatActivity {

    private EditText userName;
    private TextView changePassword, userEmail;
    private SharedPreferences userInfoPrefs;
    private SharedPreferences.Editor userInfoEditor;
    private ImageView profileImageView;
    private FloatingActionButton editButton;
    private boolean clicked;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
    private final DatabaseReference reference = database.getReference("users");
    private final DatabaseReference referenceAdmin = database.getReference("collaborators");
    private StorageReference storageUserReference, storageCollaboratorReference;
    private ActivityResultLauncher<String> selectImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
        storageUserReference = FirebaseStorage.getInstance().getReference("users").child(userInfoPrefs.getString("userKey", null));
        storageCollaboratorReference = FirebaseStorage.getInstance().getReference("collaborators").child(userInfoPrefs.getString("userKey", null));
        userInfoEditor = userInfoPrefs.edit();

        com.example.anna.databinding.EditProfileBinding binding = EditProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        userName = binding.profileUsername;
        userEmail = binding.editprofileUseremail;
        profileImageView = binding.editProfileUserImage;
        ImageView backArrow = binding.editProfileArrowBack;
        changePassword = binding.editprofileCambiarcontra;
        editButton = binding.editnamebutton;

        initializeUserName();
        initializeUserEmail();
        if (userInfoPrefs.getString("usertype", null).equals("collaborator"))
            initializeUserPicture(storageCollaboratorReference);
        else
            initializeUserPicture(storageUserReference);


        selectImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Glide.with(getApplicationContext()).load(result).into(profileImageView);
            if (userInfoPrefs.getString("usertype", null).equals("collaborator")) {
                storageCollaboratorReference.putFile(result).addOnSuccessListener(taskSnapshot -> Toast.makeText(getApplicationContext(), getString(R.string.userPictureChanged), Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getString(R.string.userPictureNotChanged), Toast.LENGTH_SHORT).show());
            } else {
                storageUserReference.putFile(result).addOnSuccessListener(taskSnapshot -> Toast.makeText(getApplicationContext(), getString(R.string.userPictureChanged), Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getString(R.string.userPictureNotChanged), Toast.LENGTH_SHORT).show());
            }

        });
        profileImageView.setOnClickListener(v -> changePicture());
        backArrow.setOnClickListener(v -> finish());

        editButton.setOnClickListener(view -> {
            if (clicked) {
                userName.setEnabled(false);
                clicked = false;
                editButton.setImageResource(R.drawable.ic_edit);
                changedUsername();
                Toast.makeText(getApplicationContext(), getString(R.string.usernameEdited), Toast.LENGTH_LONG).show();
            } else {
                userName.setEnabled(true);
                clicked = true;
                editButton.setImageResource(R.drawable.ic_tick);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordDialog dialog = new ChangePasswordDialog();
                dialog.show(getSupportFragmentManager(), "CHANGE PASSWORD");
            }
        });


    }


    public void initializeUserName() {
        userName.setEnabled(false);
        userName.setText(userInfoPrefs.getString("username", null));
    }

    public void initializeUserEmail() {
        userEmail.setText(userInfoPrefs.getString("email", null));
    }

    public void initializeUserPicture(StorageReference storageReference) {

        storageReference.getDownloadUrl()
                .addOnSuccessListener(uri -> Glide.with(getApplicationContext()).load(uri).into(profileImageView))
                .addOnFailureListener(exception -> {
                    int errorCode = ((StorageException) exception).getErrorCode();
                    if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        profileImageView.setImageResource(R.drawable.user_icon);
                    }

                });
    }

    public void changePicture() {
        selectImage.launch("image/*");
    }

    public void changedUsername() {
        userInfoEditor.remove("username");
        userInfoEditor.putString("username", userName.getText().toString());
        userInfoEditor.commit();

        if (userInfoPrefs.getString("usertype", null).equals("client")) {
            Query query = reference.orderByChild("email").equalTo(this.userInfoPrefs.getString("email", null));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            DatabaseReference dr = database.getReference("users/" + ds.getKey());
                            dr.child("username").setValue(userName.getText().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        if (userInfoPrefs.getString("usertype", null).equals("collaborator")) {
            Query query = referenceAdmin.orderByChild("email").equalTo(this.userInfoPrefs.getString("email", null));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            DatabaseReference dr = database.getReference("collaborators/" + ds.getKey());
                            dr.child("username").setValue(userName.getText().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }

}
