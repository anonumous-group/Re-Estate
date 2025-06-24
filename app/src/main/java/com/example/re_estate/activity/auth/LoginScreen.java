package com.example.re_estate.activity.auth;

import static com.example.re_estate.misc.Utilities.sendMessage;
import static com.example.re_estate.misc.Utilities.setInProgress;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.re_estate.R;
import com.example.re_estate.activity.MainScreen;
import com.example.re_estate.databinding.ActivityLoginScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {
    ActivityLoginScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.white), getColor(R.color.white)
        ));
        binding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(32, systemBars.top, 32, 40);
            return insets;
        });

        setInProgress(binding.btnSignup, binding.progBar, false);

        binding.tvSignIn.setOnClickListener(v -> startActivity(new Intent(this, RegisterScreen.class)));
        binding.btnSignup.setOnClickListener(v -> loginUser());
        binding.forgotPass.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordScreen.class)));
    }

    private void loginUser() {
        String email = binding.email.getText().toString();
        String pass = binding.pass.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            sendMessage(this, "Please fill all fields");
            return;
        }

        setInProgress(binding.btnSignup, binding.progBar, true);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(this, MainScreen.class));
                finish();
            } else {
                setInProgress(binding.btnSignup, binding.progBar, false);
                sendMessage(this, task.getException().getMessage());
            }
        });

    }
}