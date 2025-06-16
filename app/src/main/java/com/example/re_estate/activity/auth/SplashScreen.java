package com.example.re_estate.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.re_estate.R;
import com.example.re_estate.activity.MainScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(
                getResources().getColor(R.color.blue)
        ));
        setContentView(R.layout.activity_splash_screen);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            //go to welcome screen
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
                finish();
            }, 1000);
        } else {
            //check if user completed their registration in their last session
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {
                        //delete user's account
                        user.delete().addOnSuccessListener(unused -> {
                            FirebaseAuth.getInstance().signOut();
                            //go to welcome screen
                            new Handler().postDelayed(() -> {
                                startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
                                finish();
                            }, 1000);
                        });
                    } else {
                        //go to main screen
                        new Handler().postDelayed(() -> {
                            startActivity(new Intent(SplashScreen.this, MainScreen.class));
                            finish();
                        }, 1000);
                    }
                }
            });
        }
    }
}