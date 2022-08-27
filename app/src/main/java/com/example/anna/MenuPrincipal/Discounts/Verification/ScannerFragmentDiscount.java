package com.example.anna.MenuPrincipal.Discounts.Verification;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.MenuPrincipal.ScanResponseDialog;
import com.example.anna.Models.Discount;
import com.example.anna.Models.RouteModel;
import com.example.anna.Models.Station;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ScannerFragmentDiscount extends Fragment{

    private CodeScanner mCodeScanner;
    private final int CAMERA_REQUEST_CODE = 101;
    private int routeStages;
    private Station objectStation;
    private SharedPreferences userInfoPrefs;
    private String discountKey;
    private RouteModel routeNeeded;
    private Map<String, Object> map1;
    private CollectionReference discountsByUserReference;
    private DocumentReference userDocumentReference;
    private String userKey, username;

    public ScannerFragmentDiscount(String discountKey){
        this.discountKey = discountKey;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userKey = userInfoPrefs.getString("userKey", null);
        username = userInfoPrefs.getString("username",null);
        userDocumentReference = FirebaseFirestore.getInstance().collection("DiscountsUsed").document(userKey);
        discountsByUserReference = userDocumentReference.collection("DiscountsReferenceList");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Activity activity = getActivity();

        setUpPermissions();

        View root = inflater.inflate(R.layout.code_scanner, container, false);

        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        assert activity != null;
        mCodeScanner = new CodeScanner(activity, scannerView);

        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);

        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setFlashEnabled(false);

        mCodeScanner.setDecodeCallback(result -> {
            String resultat = result.getText();
            processResultat(resultat);
        });

        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        return root;
    }

    private void processResultat(String resultat) {

        if (resultat.equals(discountKey)) {
            checkAlreadyUsedDiscount();
        } else {
            new ScanResponseDialog(ScannerFragmentDiscount.this,getString(R.string.discountRejected)).show(getChildFragmentManager(), "DISCOUNT NOT RECOGNIZED");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }



    private void checkAlreadyUsedDiscount(){
        discountsByUserReference.whereEqualTo("key",discountKey).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty())
                        createDiscountUsedEntry();
                    else
                        new ScanResponseDialog(ScannerFragmentDiscount.this,getString(R.string.discountAlreadyUsed)).show(getChildFragmentManager(),"DISCOUNT NOT RECOGNIZED");
                }
            }
        });
    }

    public void createDiscountUsedEntry() {

        Discount auxDiscount = new Discount();
        Query query = FirebaseFirestore.getInstance().collection("Discounts").whereEqualTo("key",discountKey);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> map = document.getData();
                    auxDiscount.setName(Objects.requireNonNull(map.get("name")).toString());
                    auxDiscount.setDescription(Objects.requireNonNull(map.get("description")).toString());
                    auxDiscount.setImageRef(Objects.requireNonNull(map.get("imageRef")).toString());
                    auxDiscount.setKey(Objects.requireNonNull(map.get("key")).toString());
                    auxDiscount.setDiscountPercentage(Integer.parseInt(Objects.requireNonNull(map.get("discountPercentage")).toString()));
                    createDocumentToAvoidNonExistent(auxDiscount);
                }
            }
        }).addOnFailureListener(e -> {
        });
    }
    public void createDocumentToAvoidNonExistent(Discount discount){
        Map<String,Object> fieldKey = new HashMap<>();
        fieldKey.put("key",userKey);
        fieldKey.put("username",username);
        userDocumentReference.set(fieldKey).addOnSuccessListener(unused -> userDocumentReference.collection("DiscountsReferenceList")
                .add(discount).addOnSuccessListener(documentReference -> {
                    new ScanResponseDialog(ScannerFragmentDiscount.this,getString(R.string.discountVerified)).show(getChildFragmentManager(),"DISCOUNT APPLIED");
                }));
    }

    private void setUpPermissions() {
        int permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "You need the camera permission to be able to validate a discount or a completed visit", Toast.LENGTH_LONG).show();
            }
        }
    }
}




