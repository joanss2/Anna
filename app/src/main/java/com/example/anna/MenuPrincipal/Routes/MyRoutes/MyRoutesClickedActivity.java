package com.example.anna.MenuPrincipal.Routes.MyRoutes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anna.MenuPrincipal.Routes.MyRoutes.ChoiceFragments.CompletedRoutesFragment;
import com.example.anna.MenuPrincipal.Routes.MyRoutes.ChoiceFragments.StartedRoutesFragment;
import com.example.anna.MenuPrincipal.Routes.MyRoutes.ChoiceFragments.ToDiscoverFragment;
import com.example.anna.R;

public class MyRoutesClickedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_routes_clicked_choice_activity);
        String choiceType = getIntent().getStringExtra("choice");


        switch (choiceType) {
            case "STARTED ROUTES":
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.myroutes_choice_clicked_container_view, StartedRoutesFragment.class, null)
                        .commit();
                break;
            case "ROUTES TO DISCOVER":
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.myroutes_choice_clicked_container_view, ToDiscoverFragment.class, null)
                        .commit();
                break;
            case "COMPLETED ROUTES":
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.myroutes_choice_clicked_container_view, CompletedRoutesFragment.class, null)
                        .commit();
                break;
        }

    }
}
