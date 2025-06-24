package com.example.re_estate.activity.auth;

import static com.example.re_estate.misc.Utilities.sendMessage;
import static com.example.re_estate.misc.Utilities.setInProgress;

import android.os.Bundle;
import android.util.Patterns;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.re_estate.R;
import com.example.re_estate.databinding.ActivityForgotPasswordScreenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordScreen extends AppCompatActivity {
    ActivityForgotPasswordScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.white), getColor(R.color.white)
        ));
        binding = ActivityForgotPasswordScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(32, systemBars.top, 32, 40);
            return insets;
        });

        setInProgress(binding.btnReset, binding.progBar, false);

        binding.btnReset.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = binding.email.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.setError("Invalid email");
            return;
        }

        setInProgress(binding.btnReset, binding.progBar, true);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> finish())
                .addOnFailureListener(e -> {
                    sendMessage(this, e.getMessage());
                    setInProgress(binding.btnReset, binding.progBar, false);
                });
    }
}