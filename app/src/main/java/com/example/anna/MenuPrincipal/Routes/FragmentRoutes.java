package com.example.anna.MenuPrincipal.Routes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.anna.MenuPrincipal.Home.FragmentHome;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.Models.RouteModel;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class FragmentRoutes extends Fragment implements RoutesAdapter.OnRoutesClick{

    private final CollectionReference routesReference = FirebaseFirestore.getInstance().collection("Routes");
    private RoutesAdapter routesAdapter;
    private FragmentRoutes fragmentRoutes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentRoutes = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routes_all_fragment,container,false);

        ////////////////
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        System.out.println("back stack in Routesfragment: "+fragmentManager.getBackStackEntryCount());
        ///////////////

        RecyclerView routesRv = view.findViewById(R.id.routesAllRv);
        RoutesWrapContentLinearLayoutManager layoutManager = new RoutesWrapContentLinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
        routesRv.setLayoutManager(layoutManager);

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

        routesReference.whereEqualTo("name",routeModel.getName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for(QueryDocumentSnapshot document: task.getResult()){
                    assert activity != null;
                    FragmentManager manager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.replace(R.id.main_frame, new RoutesClickedFragment(routeModel.getName(),document.getId(),routeModel.getCategory()));
                    trans.remove(this);
                    manager.popBackStack();
                    trans.commit();
                }
            }
        }).addOnFailureListener(e -> {

        });

    }

}
