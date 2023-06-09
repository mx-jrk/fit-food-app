package com.example.fitfood.ui;

import static androidx.navigation.ui.BottomNavigationViewKt.setupWithNavController;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitfood.R;
import com.example.fitfood.data.NotificationReceiver;
import com.example.fitfood.databinding.ActivityMainBinding;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    BottomNavigationView bottomNavigationView;

    UserViewModel userViewModel;

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
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        setupWithNavController(bottomNavigationView, navController);


        //Hiding BottomNavigation
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId() == R.id.homeFragment || destination.getId() == R.id.shoppingListFragment || destination.getId() == R.id.profileFragment) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });


        //Installing Notifications
        Intent intentBreakfast = new Intent(this, NotificationReceiver.class);
        intentBreakfast.putExtra("id", 0);
        intentBreakfast.putExtra("time", "завтрака!");
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntentBreakfast = PendingIntent.getBroadcast(this, 0, intentBreakfast, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentLunch = new Intent(this, NotificationReceiver.class);
        intentLunch.putExtra("id", 1);
        intentLunch.putExtra("time", "обеда!");
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntentLunch = PendingIntent.getBroadcast(this, 1, intentLunch, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentSnack = new Intent(this, NotificationReceiver.class);
        intentSnack.putExtra("id", 2);
        intentSnack.putExtra("time", "перекуса!");
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntentSnack = PendingIntent.getBroadcast(this, 2, intentSnack, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentDinner = new Intent(this, NotificationReceiver.class);
        intentDinner.putExtra("id", 3);
        intentDinner.putExtra("time", "ужина!");
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntentDinner= PendingIntent.getBroadcast(this, 3, intentDinner, PendingIntent.FLAG_UPDATE_CURRENT);


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


    //Writing data to ROOM and Firebase
    @Override
    protected void onPause() {
        super.onPause();
        userViewModel.my_user.LastChangeDate = new Date().toString();
       userViewModel.my_user.LastChangeDateInt = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
       if (hasConnection(this)){
           userViewModel.uploadDataToFirebase();
           userViewModel.my_user.isLoadedToCloud = true;
       }
       else {
           userViewModel.my_user.isLoadedToCloud = false;
           Toast.makeText(this, "Не удалось загрузить данные в облако. Но они сохранены на вашем устройстве!", Toast.LENGTH_LONG).show();
       }
        userViewModel.update();

    }

    //Method of checking the availability of an Internet connection
    private boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
    }
}