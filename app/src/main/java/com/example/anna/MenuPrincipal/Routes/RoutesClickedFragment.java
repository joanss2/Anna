package com.example.anna.MenuPrincipal.Routes;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.RouteChoice;
import com.example.anna.Models.RouteModel;
import com.example.anna.Models.Station;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class RoutesClickedFragment extends Fragment implements RoutesClickedAdapter.OnStationClickListener {

    private RecyclerView listOfStationsrV;
    private final String nameOfRoute;
    private String documentID, categorystring;
    private MapView mapView;
    private Query query;
    private TextView numberOfStages, category;
    private GoogleMap gMap;
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Routes");
    private CollectionReference stationsRef;
    private RoutesClickedAdapter routesClickedAdapter;
    private static final int INITIAL_STROKE_WIDTH_PX = 5;


    public RoutesClickedFragment(String name, String id, String categorystring) {
        this.nameOfRoute = name;
        this.documentID = id;
        this.categorystring = categorystring;
        stationsRef = collectionReference.document(documentID).collection("Stations");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routes_clicked, container, false);
        mapAsync(view, savedInstanceState);

        listOfStationsrV = view.findViewById(R.id.routesClickedStages);
        numberOfStages = view.findViewById(R.id.routesClickedNumberStages);
        category = view.findViewById(R.id.routesClickedCategory);
        ImageView starCategory = view.findViewById(R.id.routesClickedStar);

        switch (categorystring) {
            case "gold":
                starCategory.setImageResource(R.drawable.ic_star_gold);
                break;
            case "silver":
                starCategory.setImageResource(R.drawable.ic_star_silver);
                break;
            case "bronze":
                starCategory.setImageResource(R.drawable.ic_star_bronze);
                break;
            default:
                break;
        }


        query = collectionReference.whereEqualTo("name", nameOfRoute);
        fillRouteSpecs(query);




        FirestoreRecyclerOptions<Station> options = new FirestoreRecyclerOptions.Builder<Station>()
                .setQuery(stationsRef, Station.class).build();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        listOfStationsrV.setLayoutManager(linearLayoutManager);
        routesClickedAdapter = new RoutesClickedAdapter(options, getContext(), this);
        listOfStationsrV.setAdapter(routesClickedAdapter);


        return view;
    }

    private void mapAsync(View view, Bundle savedInstanceState) {


        mapView = view.findViewById(R.id.routesClickedMap);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                gMap = googleMap;
                fillMap(gMap,stationsRef);
            }
        });

    }

    private void fillMap(GoogleMap googleMap, CollectionReference collectionReference){
        List<LatLng> points = new ArrayList<>();
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){

                        String title = document.getData().get("name").toString();
                        GeoPoint geoPoint = document.getGeoPoint("geoLocation");
                        LatLng position = new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());

                        points.add(position);
                        googleMap.addMarker(new MarkerOptions().position(position).title(title)).setIcon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    }

                    setCentralStationInRoute(points, googleMap);
                    drawRoute(googleMap, points);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"It has not been able to draw the route on the map, sorry!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCentralStationInRoute(List<LatLng> points, GoogleMap googleMap){

        float lowest = 0, aux = 0;
        int index =0;
        Location location = new Location("");
        Location otherLocation = new Location("");

        for(int i=0; i<points.size(); i++){
            location = createLocation(points.get(i));
            for(int j=0; j<points.size(); j++){
                if(j!=i){
                    aux += location.distanceTo(createLocation(points.get(j)));
                }
            }
            if(i==0) {
                lowest = aux;
                index = i;
            }else{
                if(aux < lowest) {
                    lowest = aux;
                    index = i;
                }
            }
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(index)));
    }

    private Location createLocation(LatLng latLng){
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    private void drawRoute(GoogleMap googleMap, List<LatLng> points){
        PolylineOptions options = new PolylineOptions().geodesic(true).width(INITIAL_STROKE_WIDTH_PX).color(Color.RED);
        for(LatLng latLng: points){
            options.add(latLng);
        }
        Polyline line = googleMap.addPolyline(options);
    }

    private void fillRouteSpecs(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        numberOfStages.setText(document.getData().get("stages").toString());
                        category.setText(document.getData().get("category").toString());
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        routesClickedAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        routesClickedAdapter.stopListening();
    }

    @Override
    public void onStationClick(Station station) {
        Intent intent = new Intent(getContext(), StationDetail.class);
        Bundle bundle = new Bundle();
        bundle.putString("stationName", station.getName());
        bundle.putString("routeID",documentID);
        intent.putExtras(bundle);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        startActivity(intent);
        transaction.remove(this);
        transaction.commit();
        manager.popBackStack();
    }
}
