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

        Intent intentBreakfast = new Intent(this, NotificationReceiver.class);
        intentBreakfast.putExtra("id", 0);
        intentBreakfast.putExtra("time", "завтрака!");
        PendingIntent pendingIntentBreakfast = PendingIntent.getBroadcast(this, 0, intentBreakfast, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentLunch = new Intent(this, NotificationReceiver.class);
        intentLunch.putExtra("id", 1);
        intentLunch.putExtra("time", "обеда!");
        PendingIntent pendingIntentLunch = PendingIntent.getBroadcast(this, 1, intentLunch, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentSnack = new Intent(this, NotificationReceiver.class);
        intentSnack.putExtra("id", 2);
        intentSnack.putExtra("time", "перекуса!");
        PendingIntent pendingIntentSnack = PendingIntent.getBroadcast(this, 2, intentSnack, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentDinner = new Intent(this, NotificationReceiver.class);
        intentDinner.putExtra("id", 3);
        intentDinner.putExtra("time", "ужина!");
        PendingIntent pendingIntentDinner= PendingIntent.getBroadcast(this, 3, intentDinner, PendingIntent.FLAG_UPDATE_CURRENT);




        // Завтрак
        Calendar breakfastCalendar = Calendar.getInstance();
        AlarmManager alarmManagerBreakfast = (AlarmManager) getSystemService(ALARM_SERVICE);
        breakfastCalendar.set(Calendar.HOUR_OF_DAY, 7);
        breakfastCalendar.set(Calendar.MINUTE, 0);
        breakfastCalendar.set(Calendar.SECOND, 0);
        alarmManagerBreakfast.setRepeating(AlarmManager.RTC_WAKEUP, breakfastCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentBreakfast);

        // Обед
        Calendar lunchCalendar = Calendar.getInstance();
        AlarmManager alarmManagerLunch = (AlarmManager) getSystemService(ALARM_SERVICE);
        lunchCalendar.set(Calendar.HOUR_OF_DAY, 13);
        lunchCalendar.set(Calendar.MINUTE, 0);
        lunchCalendar.set(Calendar.SECOND, 0);
        alarmManagerLunch.setRepeating(AlarmManager.RTC_WAKEUP, lunchCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentLunch);

        // Перекус
        Calendar snackCalendar = Calendar.getInstance();
        AlarmManager alarmManagerSnack = (AlarmManager) getSystemService(ALARM_SERVICE);
        snackCalendar.set(Calendar.HOUR_OF_DAY, 15);
        snackCalendar.set(Calendar.MINUTE, 0);
        snackCalendar.set(Calendar.SECOND, 0);
        alarmManagerSnack.setRepeating(AlarmManager.RTC_WAKEUP, snackCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentSnack);

        // Ужин
        Calendar dinnerCalendar = Calendar.getInstance();
        AlarmManager alarmManagerDinner = (AlarmManager) getSystemService(ALARM_SERVICE);
        dinnerCalendar.set(Calendar.HOUR_OF_DAY, 18);
        dinnerCalendar.set(Calendar.MINUTE, 0);
        dinnerCalendar.set(Calendar.SECOND, 0);
        alarmManagerDinner.setRepeating(AlarmManager.RTC_WAKEUP, dinnerCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentDinner);

    }


    @Override
    protected void onPause() {
        super.onPause();
           userViewModel.my_user.LastChangeDate = new Date().toString();
       userViewModel.my_user.LastChangeDateInt = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
       userViewModel.update();
       userViewModel.loadDataToFirebaseCloud();
    }
}