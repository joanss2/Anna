package com.example.anna.MenuPrincipal.MyRoutes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.RouteChoice;
import com.example.anna.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyRoutes extends Fragment implements MyRoutesChoiceAdapter.OnChoiceClickListener {

    private RecyclerView recyclerView;
    private List<RouteChoice> choiceList;
    private MyRoutesChoiceAdapter adapter;
    private String[] titles = {"STARTED ROUTES", "ROUTES TO DISCOVER", "COMPLETED ROUTES"};
    private int[] imageIDs = {R.drawable.routestarted, R.drawable.routetodiscover, R.drawable.routecompleted};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routechoice, container, false);

        recyclerView = view.findViewById(R.id.routechoiceRecyclerView);
        choiceList = initializeList(titles,imageIDs);
        System.out.println(choiceList.toString());
        adapter = new MyRoutesChoiceAdapter(getContext(),choiceList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    }
}
