package com.example.re_estate.activity.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.re_estate.R;
import com.example.re_estate.databinding.ActivityWelcomeScreenBinding;

public class WelcomeScreen extends AppCompatActivity {
    ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getResources().getColor(R.color.off_white),
                getResources().getColor(R.color.off_white)
        ));
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnCreate.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeScreen.this, FirstIntroScreen.class));
        });

        binding.btnLog.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeScreen.this, LoginScreen.class));
        });
    }
}