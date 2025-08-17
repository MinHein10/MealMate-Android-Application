package com.example.mealmate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mealmate.databinding.ActivitySendMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivitySendMessage extends AppCompatActivity {

    private ActivitySendMessageBinding binding;
    private List<String> ingredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ingredients = getIntent().getStringArrayListExtra("ingredients");
        if (ingredients != null && !ingredients.isEmpty()) {
//            Toast.makeText(this, "Ingredients: " + ingredients, Toast.LENGTH_LONG).show();
            StringBuilder ingredientsText = new StringBuilder();
            for (String ingredient : ingredients) {
                ingredientsText.append("- ").append(ingredient).append("\n");
            }
            binding.tvIngredientsList.setText(ingredientsText.toString().trim());

            // Pre-fill the message EditText with the ingredients list
            binding.etMessage.setText(ingredientsText.toString().trim());
        } else {
            ingredients = new ArrayList<>();
            Toast.makeText(this, "No ingredients found.", Toast.LENGTH_SHORT).show();
        }


        binding.btnSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ActivitySendMessage.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    sendSMS();
                }else{
                    ActivityCompat.requestPermissions(ActivitySendMessage.this,new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });

        binding.btnBackMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySendMessage.this,MainActivity2.class));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode==100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendSMS();
        }else{
            Toast.makeText(this,"Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

private void sendSMS() {
    String textPhone = binding.etPhoneNumber.getText().toString();
    String textMessage = binding.etMessage.getText().toString().trim();

    if (!textPhone.isEmpty() && !textMessage.isEmpty()) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(textPhone, null, textMessage, null, null);
            Toast.makeText(this, "SMS sent successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    } else {
        Toast.makeText(this, "Please enter phone number and message", Toast.LENGTH_SHORT).show();
    }
}


}

