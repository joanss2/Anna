package com.example.anna.MenuPrincipal.Routes.MyRoutes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.Routes.RouteDetailFragment;
import com.example.anna.MenuPrincipal.Routes.RoutesWrapContentLinearLayoutManager;
import com.example.anna.Models.RouteChoice;
import com.example.anna.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentMyRoutes extends Fragment implements MyRoutesChoiceAdapter.OnChoiceClickListener {



    private final String[] titles = {"STARTED ROUTES", "COMPLETED ROUTES"};
    private final int[] imageIDs = {R.drawable.routestarted, R.drawable.routecompleted};
    private final CollectionReference routesReference = FirebaseFirestore.getInstance().collection("Routes");
    private FragmentManager manager;
    private FragmentTransaction trans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = requireActivity().getSupportFragmentManager();
        trans = manager.beginTransaction();
    }

    /*

    ActivityResultLauncher<Intent> typeOfRouteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if(result.getResultCode()==18){
            System.out.println("CODE 18");

        }

        if(result.getResultCode()==11){

            //TO ROUTES CLICKED FRAGMENT
            assert result.getData() != null;
            String name = result.getData().getStringExtra("name");
            String category = result.getData().getStringExtra("category");
            routesReference.whereEqualTo("name",name).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){

                        Bundle bundle = new Bundle();
                        bundle.putString("from","fromCompleted");
                        RouteDetailFragment routeDetailFragment = new RouteDetailFragment(name,document.getId(),category);
                        routeDetailFragment.setArguments(bundle);

                        //trans.add(R.id.main_frame, routeDetailFragment);
                        //trans.replace(R.id.main_frame, routeDetailFragment);
                        //assert getParentFragment() != null;
                        //trans.remove(getParentFragment());
                        //manager.popBackStack();
                        trans.commit();

                    }
                }
            }).addOnFailureListener(e -> {

            });
        }else if(result.getResultCode()==22){
            assert result.getData() != null;
            String name = result.getData().getStringExtra("name");
            String category = result.getData().getStringExtra("category");
            routesReference.whereEqualTo("name",name).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){

                        Bundle bundle = new Bundle();
                        bundle.putString("from","fromStarted");
                        RouteDetailFragment routeDetailFragment = new RouteDetailFragment(name,document.getId(),category);
                        routeDetailFragment.setArguments(bundle);
                        trans.add(R.id.main_frame, routeDetailFragment);System.out.println(name+" "+document.getId()+" "+category);
                        //trans.replace(R.id.main_frame, routeDetailFragment);
                        //assert getParentFragment() != null;
                        //trans.remove(getParentFragment());
                        //manager.popBackStack();
                        trans.commit();

                    }
                }
            }).addOnFailureListener(e -> {

            });
        }
    });

     */


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routechoice, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.routechoiceRecyclerView);
        List<RouteChoice> choiceList = initializeList(titles, imageIDs);
        MyRoutesChoiceAdapter adapter = new MyRoutesChoiceAdapter(getContext(), choiceList, this);
        recyclerView.setLayoutManager(new RoutesWrapContentLinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        return view;

    }

    public List<RouteChoice> initializeList(String [] titles, int [] imageIDs){
        List<RouteChoice> list = new ArrayList<>();

        for (int i=0; i<titles.length; i++){
            list.add(new RouteChoice(titles[i],imageIDs[i]));
        }
        return list;
    }

    @Override
    public void onChoiceClick(String title) {
        Intent intent = new Intent(getContext(), MyRoutesClickedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("choice",title);
        intent.putExtras(bundle);
        startActivity(intent);
        //typeOfRouteLauncher.launch(intent);

    }
}
