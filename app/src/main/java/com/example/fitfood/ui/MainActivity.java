package com.example.fitfood.ui;

import static androidx.navigation.ui.BottomNavigationViewKt.setupWithNavController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.fitfood.R;
import com.example.fitfood.data.NotificationReceiver;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.databinding.ActivityMainBinding;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;
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

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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

        createNotificationChannel();
        Intent intentBreakfast = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntentBreakfast = PendingIntent.getBroadcast(this, 0, intentBreakfast, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentLunch = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntentLunch = PendingIntent.getBroadcast(this, 1, intentLunch, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentSnack = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntentSnack = PendingIntent.getBroadcast(this, 2, intentSnack, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentDinner = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntentDinner= PendingIntent.getBroadcast(this, 3, intentDinner, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentBreakfast);

        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentLunch);

        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentSnack);

        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentDinner);
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notifyChannel", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
           userViewModel.my_user.LastChangeDate = new Date().toString();
       userViewModel.my_user.LastChangeDateInt = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
       userViewModel.update();
    }
}