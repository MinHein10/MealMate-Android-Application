package com.example.mealmate;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mealmate.databinding.ActivityRegisterBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSignup.setOnClickListener(view->signup());
        binding.tvLogin.setOnClickListener(view->finish());
    }

    private void signup(){
        String name = Objects.requireNonNull(binding.registerName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.registerEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.registerPassword.getText()).toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please enter your name, email and password", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }else if(password.length()<6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
            createNewUser(name,email,password);
        }
    }

    private void createNewUser(String name,String email,String password){
        FirebaseApp.initializeApp(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task->{
            if (task.isSuccessful()){
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}