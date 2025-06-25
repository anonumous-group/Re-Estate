package com.example.re_estate.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.re_estate.R;
import com.example.re_estate.activity.payment.AddCardScreen;
import com.example.re_estate.databinding.ActivityPaymentMethodScreenBinding;

public class PaymentMethodScreen extends AppCompatActivity {
    ActivityPaymentMethodScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.white), getColor(R.color.white)
        ));
        binding = ActivityPaymentMethodScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(32, systemBars.top, 32, 0);
            return insets;
        });

        binding.addCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCardScreen.class);
            intent.putExtra("type", "Add Card");
            startActivity(intent);
        });
    }
}