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
import com.example.re_estate.databinding.ActivityThirdIntroScreenBinding;

 public class ThirdIntroScreen extends AppCompatActivity {
    ActivityThirdIntroScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.white), getColor(R.color.white)
        ));
        binding = ActivityThirdIntroScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(32, systemBars.top, 32, 40);
            return insets;
        });

        binding.btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterScreen.class));
            finish();
        });
        binding.btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.btnNext.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterScreen.class));
            finish();
        });
    }
}