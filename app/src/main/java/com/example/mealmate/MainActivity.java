package com.example.mealmate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.mealmate.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealmate.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

//    Splash Page
    private int splashScreenTime = 1000;
    private int timeInterval = 100;
    private int progress =0;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar2.setMax(splashScreenTime);
        binding.progressBar2.setProgress(progress);
        handler = new Handler(Looper.getMainLooper());
        runnable = ()-> {
                if (progress < splashScreenTime){
                    progress += timeInterval;
                    binding.progressBar2.setProgress(progress);
                    handler.postDelayed(runnable, timeInterval);
                }else {
                    //Middleware
                    //Following code will check user is logged in or not


                    //Middleware
                    FirebaseApp.initializeApp(this);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //Toast.makeText(MainActivity.this,"Splash Screen Completed",Toast.LENGTH_SHORT).show();
                    startActivity(user!=null? new Intent(MainActivity.this,MainActivity2.class) : new Intent(MainActivity.this,LoginActivity.class) );
                    finish();
                }
        };
        handler.postDelayed(runnable,timeInterval);
    }

    public void basicReadWrite() {
        // [START write_message]
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, Firebase!");}
}