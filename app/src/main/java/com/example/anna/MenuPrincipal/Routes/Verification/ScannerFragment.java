package com.example.anna.MenuPrincipal.Routes.Verification;

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
import com.example.anna.Models.RouteModel;
import com.example.anna.Models.Station;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ScannerFragment extends Fragment {

    private CodeScanner mCodeScanner;
    private final int CAMERA_REQUEST_CODE = 101;
    private final String stationKey;
    private final String routeKey;
    private int routeStages;
    private Station objectStation;
    private SharedPreferences userInfoPrefs;
    private RouteModel routeNeeded;
    private Map<String, Object> map1;

    public ScannerFragment(String stationKey, String routeKey) {
        this.stationKey = stationKey;
        this.routeKey = routeKey;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
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
        if (resultat.equals(stationKey)) {
            setRouteAsVerified(stationKey, routeKey);
            new StationConfirmationDialog(this).show(getChildFragmentManager(), "STATION VERIFIED");
        } else {
            new StationRejectionDialog(this).show(getChildFragmentManager(), "STATION NOT RECOGNIZED");
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


    private void setRouteAsVerified(String stationKey, String routeKey) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Routes").document(routeKey).collection("Stations");
        DocumentReference stationRef = reference.document(stationKey);

        stationRef.get().addOnSuccessListener(documentSnapshot -> {
            objectStation = documentSnapshot.toObject(Station.class);
            checkUserStartedRoutes(objectStation);
        });
    }

    private void checkUserStartedRoutes(Station station) {

        CollectionReference routesReference = FirebaseFirestore.getInstance().collection("StartedRoutes").document(userInfoPrefs.getString("userKey", null)).collection("Routes");
        routesReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        ////////////// NO EXISTEIX LA COL·LECCIO ROUTES A STARTED ROUTES PER A L'USUARI EN CONCRET  //////////////
                        Map<String, String> map = new HashMap<>();
                        map.put("key", userInfoPrefs.getString("userKey", null));
                        map.put("username", userInfoPrefs.getString("username", null));
                        FirebaseFirestore.getInstance().collection("StartedRoutes").document(userInfoPrefs.getString("userKey", null)).set(map).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        DocumentReference startedRouteDocument = FirebaseFirestore.getInstance().collection("StartedRoutes").document(userInfoPrefs.getString("userKey", null))
                                                .collection("Routes").document(routeKey);
                                        startedRouteDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (!task.getResult().exists()) {
                                                        ///////////// EXISTEIX ROUTES PER A L'USUARI PERO NO AQUESTA RUTA /////////////
                                                        System.out.println("NO EXISTE ESTA RUTA TODAVIA");
                                                        FirebaseFirestore.getInstance().collection("Routes").document(routeKey).get().addOnSuccessListener(
                                                                new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        routeNeeded = documentSnapshot.toObject(RouteModel.class);
                                                                        map1 = new HashMap<>();
                                                                        map1.put("category", routeNeeded.getCategory());
                                                                        map1.put("key", routeNeeded.getKey());
                                                                        map1.put("name", routeNeeded.getName());
                                                                        map1.put("numberOfStages", routeNeeded.getNumberOfStages());
                                                                        startedRouteDocument.set(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                startedRouteDocument.collection("Stations").document(stationKey).set(station).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful())
                                                                                            //////// PRIMERA ESTACIO VISITADA DE LA RUTA ////////
                                                                                            Toast.makeText(getContext(), "Station visited in Route just Started", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                        );
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                        );
                    } else {
                        System.out.println("CHECK ALREADY VISITED");
                        checkAlreadyVisitedStation(objectStation);
                    }
                }
            }
        });



        /*
        CollectionReference startedRouteCollection = FirebaseFirestore.getInstance().collection("StartedRoutes").document(userInfoPrefs.getString("userKey", null))
                .collection(routeKey);
        startedRouteCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("key", userInfoPrefs.getString("userKey", null));
                    map.put("username", userInfoPrefs.getString("username", null));
                    DocumentReference userOfRouteRef = FirebaseFirestore.getInstance().collection("StartedRoutes").document(userInfoPrefs.getString("userKey", null));
                    userOfRouteRef.set(map).addOnSuccessListener(
                            unused -> userOfRouteRef.collection(routeKey).document(stationKey).set(station).addOnSuccessListener(unused1 -> {
                                Toast.makeText(getContext(), "Station visited in Route just Started", Toast.LENGTH_LONG).show();
                                mCodeScanner.stopPreview();
                            })
                    );
                } else {
                    checkAlreadyVisitedStation(objectStation, startedRouteCollection);
                }
            }
        });

         */
    }

    private void checkAlreadyVisitedStation(Station station) {//, DocumentReference startedRouteCollection) {

        DocumentReference startedRouteCollection = FirebaseFirestore.getInstance().collection("StartedRoutes").document(userInfoPrefs.getString("userKey", null))
                .collection("Routes").document(routeKey);

        startedRouteCollection.collection("Stations").document(stationKey).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("AQUI ARRIBO");
                if (task.getResult().exists()) {
                    Toast.makeText(getContext(), "STATION ALREADY VISITED, LET'S GO FOR THE NEXT", Toast.LENGTH_LONG).show();
                    mCodeScanner.stopPreview();
                } else {
                    startedRouteCollection.collection("Stations").document(stationKey).set(station).addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "STATION VERIFIED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                        checkForCompletedRoute(startedRouteCollection);
                    });
                }
            }
        });



        /*
        startedRouteCollection.document(stationKey).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    Toast.makeText(getContext(), "STATION ALREADY VISITED, LET'S GO FOR THE NEXT", Toast.LENGTH_LONG).show();
                    mCodeScanner.stopPreview();
                } else {
                    startedRouteCollection.document(stationKey).set(station).addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "STATION VERIFIED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                        checkForCompletedRoute(startedRouteCollection);
                    });
                }
            }
        });
        
         */
    }

    private void checkForCompletedRoute(DocumentReference startedRouteCollection) {
        FirebaseFirestore.getInstance().collection("Routes").document(routeKey).get().addOnSuccessListener(documentSnapshot -> {
            RouteModel routeModel = documentSnapshot.toObject(RouteModel.class);
            assert routeModel != null;
            routeStages = routeModel.getNumberOfStages();
            startedRouteCollection.collection("Stations").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().size() == routeStages) {
                        System.out.println("Congratulations, ROUTE " + routeModel.getName() + " completed!!!");
                        Map<String, String> map = new HashMap<>();
                        map.put("key", userInfoPrefs.getString("userKey", null));
                        map.put("username", userInfoPrefs.getString("username", null));
                        DocumentReference completedRef = FirebaseFirestore.getInstance().collection("CompletedRoutes").document(userInfoPrefs.getString("userKey", null));
                        completedRef.set(map)
                                .addOnSuccessListener(unused ->
                                        completedRef.collection("Routes").document(routeKey).set(map1).addOnSuccessListener(
                                                unused1 -> {
                                                    Toast.makeText(getContext(), "Route " + routeModel.getName() + " added to routes completed", Toast.LENGTH_SHORT).show();
                                                    mCodeScanner.stopPreview();
                                                }
                                        ));
                    } else {
                        mCodeScanner.stopPreview();
                    }
                } else {
                    mCodeScanner.stopPreview();
                }
            });
        });
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
