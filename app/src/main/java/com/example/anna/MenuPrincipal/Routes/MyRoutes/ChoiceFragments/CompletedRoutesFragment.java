package com.example.anna.MenuPrincipal.Routes.MyRoutes.ChoiceFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.MenuPrincipal.Routes.RouteDetailFragment;
import com.example.anna.MenuPrincipal.Routes.RoutesAdapter;
import com.example.anna.MenuPrincipal.Routes.RoutesWrapContentLinearLayoutManager;
import com.example.anna.Models.RouteModel;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class CompletedRoutesFragment extends Fragment implements RoutesAdapter.OnRoutesClickListener {

    private RoutesAdapter routesAdapter;
    private CollectionReference completedReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        completedReference = FirebaseFirestore.getInstance().collection("CompletedRoutes").document(userInfoPrefs.getString("userKey", null))
                .collection("Routes");
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                //Intent intent = new Intent();
                //requireActivity().setResult(18, intent);

                //Intent intent = new Intent(getContext(), MenuMainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
                requireActivity().finish();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completed_routes, container, false);


        RecyclerView completedRv = view.findViewById(R.id.completedRoutesRv);

        completedRv.setLayoutManager(new RoutesWrapContentLinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        FirestoreRecyclerOptions<RouteModel> options =
                new FirestoreRecyclerOptions.Builder<RouteModel>()
                        .setQuery(completedReference, RouteModel.class).build();

        routesAdapter = new RoutesAdapter(options, this);
        completedRv.setAdapter(routesAdapter);

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
/*
            Intent intent = new Intent();
            intent.putExtra("name", routeModel.getName());
            intent.putExtra("category", routeModel.getCategory());
            requireActivity().setResult(11, intent);
            requireActivity().finish();

 */
        Bundle bundle = new Bundle();
        bundle.putString("from","fromCompleted");
        RouteDetailFragment routeDetailFragment = new RouteDetailFragment(routeModel.getName(),routeModel.getKey(),routeModel.getCategory());
        routeDetailFragment.setArguments(bundle);

        FragmentManager manager = requireActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.myroutes_choice_clicked_container_view,routeDetailFragment, null).addToBackStack(null);
        manager.popBackStack();

        trans.commit();

    }
}
