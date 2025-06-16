package com.example.re_estate.activity.auth;

import static com.example.re_estate.misc.PasswordValidator.getPasswordError;
import static com.example.re_estate.misc.PasswordValidator.isValidPassword;
import static com.example.re_estate.misc.Utilities.setInProgress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.re_estate.R;
import com.example.re_estate.databinding.ActivityRegisterScreenBinding;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class RegisterScreen extends AppCompatActivity {
    ActivityRegisterScreenBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.white), getColor(R.color.white)
        ));
        binding = ActivityRegisterScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(32, systemBars.top, 32, 32);
            return insets;
        });

        binding.btnSignup.setOnClickListener(v -> {
            String name = binding.name.getText().toString().trim();
            String email = binding.email.getText().toString();
            String pass = binding.pass.getText().toString().trim();
            boolean terms = binding.terms.isChecked();

            if (name.isEmpty()) {
                binding.name.setError("Name is required");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.email.setError("Invalid email address");
                return;
            }

            if (!isValidPassword(pass)) {
                binding.errorMessage.setVisibility(View.VISIBLE);
                binding.errorMessage.setText(getPasswordError(pass));
                return;
            }

            if (!terms) {
                binding.terms.setChecked(true);
            }

            setInProgress(binding.btnSignup, binding.progBar, true);
            registerUser(name, email, pass);
        });
        binding.tvSignIn.setOnClickListener(v -> startActivity(new Intent(this, LoginScreen.class)));
    }

    private void registerUser(String name, String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            setInProgress(binding.btnSignup, binding.progBar, false);
            if (!task.isSuccessful()) {
                try {
                    binding.errorMessage.setVisibility(View.VISIBLE);
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseNetworkException e) {
                    binding.errorMessage.setText("No internet connection");
                } catch (FirebaseAuthWeakPasswordException e) {
                    binding.errorMessage.setText("Password does not meet the requirements");
                } catch (Exception e) {
                    binding.errorMessage.setText(e.getMessage());
                }
            } else {
                Intent intent = new Intent(this, CompleteProfileScreen.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });
    }
}