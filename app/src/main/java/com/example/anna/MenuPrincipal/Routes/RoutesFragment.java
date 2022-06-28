package com.example.anna.MenuPrincipal.Routes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.MyRoutes.ChoiceFragments.ToDiscoverFragment;
import com.example.anna.Models.RouteModel;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RoutesFragment extends Fragment implements RoutesAdapter.OnRoutesClick{

    private final CollectionReference routesReference = FirebaseFirestore.getInstance().collection("Routes");
    private List<RouteModel> routeModelList;
    private RecyclerView routesRv;
    private RoutesAdapter routesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routes_all_fragment,container,false);

        routesRv = view.findViewById(R.id.routesAllRv);
        routesRv.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions<RouteModel> options =
                new FirestoreRecyclerOptions.Builder<RouteModel>()
                .setQuery(routesReference, RouteModel.class).build();

        routesAdapter = new RoutesAdapter(options,this);
        routesRv.setAdapter(routesAdapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        routesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        routesAdapter.stopListening();
    }

    @Override
    public void onRoutesClick(RouteModel routeModel) {
        Toast.makeText(getContext(), "I HAVE BEEN CLICKED", Toast.LENGTH_LONG).show();
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        routesReference.whereEqualTo("name",routeModel.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.wrapper, new RoutesClickedFragment(routeModel.getName(),document.getId(),routeModel.getCategory()))
                                .addToBackStack(null).commit();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
