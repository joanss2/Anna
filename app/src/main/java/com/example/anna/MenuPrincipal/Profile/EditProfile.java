package com.example.anna.MenuPrincipal.Profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.anna.MenuPrincipal.Faqs.FragmentFaqs;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.example.anna.Register.MainActivity;
import com.example.anna.databinding.EditProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;

public class EditProfile extends AppCompatActivity {

    private EditText userName;
    private TextView changePassword, userEmail;
    private SharedPreferences userInfoPrefs;
    private SharedPreferences.Editor userInfoEditor;
    private ImageView profileImageView, backArrow;
    private EditProfileBinding binding;
    private FloatingActionButton editButton;
    private boolean clicked;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
    private final DatabaseReference reference = database.getReference("users");
    private final DatabaseReference referenceAdmin = database.getReference("collaborators");
    private StorageReference storageReference;
    private ActivityResultLauncher<String> selectImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
        storageReference = FirebaseStorage.getInstance().getReference("users").child(userInfoPrefs.getString("userKey", null));
        userInfoEditor = userInfoPrefs.edit();

        binding = EditProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        userName = binding.profileUsername;
        userEmail = binding.editprofileUseremail;
        profileImageView = binding.editProfileUserImage;
        backArrow = binding.editProfileArrowBack;
        changePassword = binding.editprofileCambiarcontra;
        editButton = binding.editnamebutton;

        initializeUserName();
        initializeUserEmail();
        initializeUserPicture(storageReference);

        selectImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Glide.with(getApplicationContext()).load(result).into(profileImageView);
                storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Profile picture changed successfully!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Could not change profile picture. Try later. ",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePicture();
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked) {
                    userName.setEnabled(false);
                    clicked = false;
                    editButton.setImageResource(R.drawable.ic_edit);
                    changedUsername();
                    Toast.makeText(getApplicationContext(), "Username edited!", Toast.LENGTH_LONG).show();
                } else {
                    userName.setEnabled(true);
                    clicked = true;
                    editButton.setImageResource(R.drawable.ic_tick);
                }

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
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(profileImageView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        int errorCode = ((StorageException) exception).getErrorCode();
                        if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                            profileImageView.setImageResource(R.drawable.user_icon);
                        }

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

        if(userInfoPrefs.getString("usertype",null).equals("client")){
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
        if(userInfoPrefs.getString("usertype",null).equals("collaborator")){
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




    /*

        private String urlPicture;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPrefs.edit();
        clicked = false;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ActivityFragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        userName = binding.profileUsername;
        userName.setClickable(false);
        userName.setEnabled(false);
        userName.setText(userInfoPrefs.getString("username", null));
        userMail = binding.profileUseremail;
        userMail.setText(userInfoPrefs.getString("email", null));
        userMail.setClickable(false);
        userMail.setEnabled(false);
        userTel = binding.profileUsertelefono;
        userTel.setText(userInfoPrefs.getString("usertel", null));
        profileImageView = binding.profileImageView;
        urlPicture = userInfoPrefs.getString("fotourl", null);//
        Glide.with(getContext()).load(urlPicture).into(profileImageView);

        editButton = (FloatingActionButton) root.findViewById(R.id.editnamebutton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked) {
                    userName.setEnabled(false);
                    clicked = false;
                    editButton.setImageResource(R.drawable.ic_edit);
                    changedUsername();
                    Toast.makeText(getContext(), "Username edited!", Toast.LENGTH_LONG).show();
                } else {
                    userName.setEnabled(true);
                    clicked = true;
                    editButton.setImageResource(R.drawable.ic_tick);
                }

            }
        });


        return root;
    }

    public void changedUsername() {
        userInfoEditor.remove("username");
        userInfoEditor.putString("username", userName.getText().toString());
        userInfoEditor.commit();
        Query query = reference.orderByChild("email").equalTo(FragmentProfile.this.userInfoPrefs.getString("email", null));
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

     */
}
