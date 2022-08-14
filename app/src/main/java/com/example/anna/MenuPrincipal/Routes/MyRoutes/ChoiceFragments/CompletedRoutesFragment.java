package com.example.anna.MenuPrincipal.Routes.MyRoutes.ChoiceFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.MenuPrincipal.Profile.FragmentProfile;
import com.example.anna.MenuPrincipal.Routes.MyRoutes.MyRoutesClickedActivity;
import com.example.anna.MenuPrincipal.Routes.RoutesAdapter;
import com.example.anna.MenuPrincipal.Routes.RoutesClickedFragment;
import com.example.anna.MenuPrincipal.Routes.RoutesWrapContentLinearLayoutManager;
import com.example.anna.Models.RouteModel;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class CompletedRoutesFragment extends Fragment implements RoutesAdapter.OnRoutesClickListener {

    private RecyclerView completedRv;
    private RoutesAdapter routesAdapter;
    private SharedPreferences userInfoPrefs;
    private CollectionReference completedReference, routesReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        completedReference = FirebaseFirestore.getInstance().collection("CompletedRoutes").document(userInfoPrefs.getString("userKey", null))
                .collection("Routes");
        routesReference = FirebaseFirestore.getInstance().collection("Routes");
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {


                Intent intent = new Intent(getContext(), MenuMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                requireActivity().finish();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completed_routes, container, false);


        completedRv = view.findViewById(R.id.completedRoutesRv);

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

            Intent intent = new Intent();
            intent.putExtra("name", routeModel.getName());
            intent.putExtra("category", routeModel.getCategory());
            requireActivity().setResult(11, intent);
            requireActivity().finish();

    }
}
