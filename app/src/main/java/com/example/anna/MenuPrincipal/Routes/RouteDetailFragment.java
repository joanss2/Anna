package com.example.anna.MenuPrincipal.Routes;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.Routes.MyRoutes.ChoiceFragments.CompletedRoutesFragment;
import com.example.anna.MenuPrincipal.Routes.MyRoutes.ChoiceFragments.StartedRoutesFragment;
import com.example.anna.Models.Station;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteDetailFragment extends Fragment implements RouteDetailAdapter.OnStationClickListener {

    private final String nameOfRoute;
    private final String documentID;
    private final String categoryString;
    private TextView numberOfStages;
    private TextView category;
    private GoogleMap gMap;
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Routes");
    private final CollectionReference stationsRef;
    private RouteDetailAdapter routeDetailAdapter;
    private static final int INITIAL_STROKE_WIDTH_PX = 5;


    ////////
    private ArrayList<String> uriStrings;
    private Intent intent;
    private Bundle bundle, received;
    private FragmentManager manager;
    private FragmentTransaction transaction;


    public RouteDetailFragment(String name, String id, String categoryString) {
        this.nameOfRoute = name;
        this.documentID = id;
        this.categoryString = categoryString;
        stationsRef = collectionReference.document(documentID).collection("Stations");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        received = getArguments();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();

                if (received != null) {
                    if (received.getString("from").equals("fromCompleted")) {
                        //trans.add(R.id.main_frame, new CompletedRoutesFragment());
                        trans.replace(R.id.myroutes_choice_clicked_container_view,new CompletedRoutesFragment()).addToBackStack(null);
                    } else if (received.getString("from").equals("fromStarted")) {
                        //trans.add(R.id.main_frame, new StartedRoutesFragment());
                        trans.replace(R.id.myroutes_choice_clicked_container_view,new StartedRoutesFragment()).addToBackStack(null);
                    }
                }
                System.out.println(requireActivity().getSupportFragmentManager().getBackStackEntryCount()+ " AL TORNAR DE ROUTE DETAIL");

                trans.remove(RouteDetailFragment.this);
                manager.popBackStack();
                trans.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routes_clicked, container, false);
        mapAsync(view, savedInstanceState);

        intent = new Intent(getContext(), StationDetail.class);
        bundle = new Bundle();
        uriStrings = new ArrayList<>();
        manager = requireActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();

        RecyclerView listOfStationsRv = view.findViewById(R.id.routesClickedStages);
        numberOfStages = view.findViewById(R.id.routesClickedNumberStages);
        category = view.findViewById(R.id.routesClickedCategory);
        ImageView starCategory = view.findViewById(R.id.routesClickedStar);
        TextView title = view.findViewById(R.id.routesClickedName);
        title.setText(nameOfRoute);

        switch (categoryString) {
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


        Query query = collectionReference.whereEqualTo("name", nameOfRoute);
        fillRouteSpecs(query);


        FirestoreRecyclerOptions<Station> options = new FirestoreRecyclerOptions.Builder<Station>()
                .setQuery(stationsRef, Station.class).build();

        LinearLayoutManager linearLayoutManager = new RoutesWrapContentLinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        listOfStationsRv.setLayoutManager(linearLayoutManager);
        routeDetailAdapter = new RouteDetailAdapter(options, getContext(), this);
        listOfStationsRv.setAdapter(routeDetailAdapter);


        return view;
    }


    private void mapAsync(View view, Bundle savedInstanceState) {


        MapView mapView = view.findViewById(R.id.routesClickedMap);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(googleMap -> {
            gMap = googleMap;
            fillMap(gMap, stationsRef);
        });

    }

    private void fillMap(GoogleMap googleMap, CollectionReference collectionReference) {
        List<Marker> myMarkers = new ArrayList<>();
        List<LatLng> points = new ArrayList<>();
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    String title = Objects.requireNonNull(document.getData().get("name")).toString();
                    GeoPoint geoPoint = document.getGeoPoint("geoLocation");
                    assert geoPoint != null;
                    LatLng position = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                    Marker marker = googleMap.addMarker(new MarkerOptions().position(position)
                            .title(title));
                    assert marker != null;
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    myMarkers.add(marker);

                    points.add(position);
                }

                setCentralStationInRoute(points, googleMap, myMarkers);

            }
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "It has not been able to draw the route on the map, sorry!", Toast.LENGTH_SHORT).show());
    }

    private void setCentralStationInRoute(List<LatLng> points, GoogleMap googleMap, List<Marker> markers) {

        float lowest = 0, aux = 0;
        int index = 0;
        Location location;

        for (int i = 0; i < points.size(); i++) {
            location = createLocation(points.get(i));
            for (int j = 0; j < points.size(); j++) {
                if (j != i) {
                    aux += location.distanceTo(createLocation(points.get(j)));
                }
            }
            if (i == 0) {
                lowest = aux;
                index = i;
            } else {
                if (aux < lowest) {
                    lowest = aux;
                    index = i;
                }
            }
            aux = 0;
        }

        markers.get(index).setIcon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));


        Marker[] array = markers.toArray(new Marker[0]);
        swap(array, 0, index);

        if (array.length > 1)
            orderMarkers(array, markers.indexOf(markers.get(index)), googleMap);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(index), 15));
    }

    private Location createLocation(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    private void orderMarkers(Marker[] markers, int offset, GoogleMap googleMap) {
        float lowest = 0, aux;
        int index = 0;
        Location location;

        for (int i = offset; i < markers.length - 2; i++) {
            location = createLocation(markers[i].getPosition());
            for (int j = offset + 1; j < markers.length; j++) {
                aux = location.distanceTo(createLocation(markers[j].getPosition()));
                if (lowest == 0)
                    lowest = aux;

                if (aux < lowest)
                    lowest = aux;

            }
            lowest = 0;
            swap(markers, i, index);
        }

        drawRoute(googleMap, markers);
    }

    private void swap(Object[] objects, int index1, int index2) {
        Object aux = objects[index1];
        objects[index1] = objects[index2];
        objects[index2] = aux;
    }

    private void drawRoute(GoogleMap googleMap, Marker[] points) {
        PolylineOptions options = new PolylineOptions().geodesic(true).width(INITIAL_STROKE_WIDTH_PX).color(Color.RED);

        for (Marker point : points) {
            options.add(point.getPosition());
        }

        googleMap.addPolyline(options);
    }

    private void fillRouteSpecs(Query query) {
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    numberOfStages.setText(Objects.requireNonNull(document.getData().get("numberOfStages")).toString());
                    category.setText(Objects.requireNonNull(document.getData().get("category")).toString());
                }
            }
        }).addOnFailureListener(e -> {

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        routeDetailAdapter.startListening();
    }


    @Override
    public void onStop() {
        routeDetailAdapter.stopListening();
        super.onStop();
    }


    @Override
    public void onStationClick(Station station) {

        bundle.putString("stationName", station.getName());
        bundle.putString("routeID", documentID);
        bundle.putString("stationKey", station.getKey());
        bundle.putString("stationDescription", station.getDescription());
        bundle.putDouble("latitude",station.getGeoLocation().getLatitude());
        bundle.putDouble("longitude",station.getGeoLocation().getLongitude());


        downloadPictures(station.getName());
    }

    private void downloadPictures(String name) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(name);
        storageReference.listAll().addOnSuccessListener(listResult -> {
            uriStrings.clear();
            for (StorageReference picture : listResult.getItems()) {
                Task<Uri> downloadUrlTask = picture.getDownloadUrl();
                downloadUrlTask.addOnSuccessListener(uri -> {
                    uriStrings.add(uri.toString());
                    System.out.println(uri);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (listResult.getItems().indexOf(picture) == listResult.getItems().size() - 1) {
                            bundle.putStringArrayList("uriStrings", uriStrings);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            //transaction.remove(RouteDetailFragment.this);
                            //transaction.commit();
                            //manager.popBackStack();
                        }
                    }

                });
            }
        });

    }


}
