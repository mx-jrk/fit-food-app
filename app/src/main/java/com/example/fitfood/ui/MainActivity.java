package com.example.fitfood.ui;

import static androidx.navigation.ui.BottomNavigationViewKt.setupWithNavController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.data.repositories.UserRepository;
import com.example.fitfood.databinding.ActivityMainBinding;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    static BottomNavigationView bottomNavigationView;
    UserViewModel userViewModel;
    PlanRepository planRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Hiding Bar
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}


        //Activating Bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        setupWithNavController(bottomNavigationView, navController);

        planRepository = new PlanRepository(getApplication());

        //First Launch checking
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity user) {
                if (user == null){
                    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.loginOrSignupFragment);
                }
                else {
                    planRepository.getAllPlans().observe(MainActivity.this, new Observer<List<PlanEntity>>() {
                        @Override
                        public void onChanged(List<PlanEntity> planEntities) {
                            for (PlanEntity plan : planEntities){
                                if (plan.id == user.PlanId){
                                    user.Plan = plan;
                                    return;
                                }
                            }
                        }
                    });
                    userViewModel.my_user = user;
                }
            }
        });

        //Hiding BottomNavigation
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.homeFragment || destination.getId() == R.id.shoppingListFragment || destination.getId() == R.id.profileFragment) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                } else {
                    bottomNavigationView.setVisibility(View.GONE);
                }
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this, userViewModel.my_user.Name, Toast.LENGTH_SHORT).show();
        userViewModel.update();
    }
}