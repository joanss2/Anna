package com.example.anna.MenuPrincipal.Routes.MyRoutes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private final String[] titles = {"STARTED ROUTES", "ROUTES TO DISCOVER", "COMPLETED ROUTES"};
    private final int[] imageIDs = {R.drawable.routestarted, R.drawable.routetodiscover, R.drawable.routecompleted};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routechoice, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.routechoiceRecyclerView);
        List<RouteChoice> choiceList = initializeList(titles, imageIDs);
        System.out.println(choiceList.toString());
        MyRoutesChoiceAdapter adapter = new MyRoutesChoiceAdapter(getContext(), choiceList, this);
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
