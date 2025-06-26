package com.example.re_estate.activity;

import static com.example.re_estate.misc.FirebaseUtil.cardCol;

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
import com.example.re_estate.adapter.CardAdapter;
import com.example.re_estate.database.Card;
import com.example.re_estate.databinding.ActivityPaymentMethodScreenBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodScreen extends AppCompatActivity {
    ActivityPaymentMethodScreenBinding binding;
    List<Card> cards = new ArrayList<>();
    CardAdapter adapter;

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

        getAllCards();

        adapter = new CardAdapter(this, cards, card -> {
            Intent intent = new Intent(this, AddCardScreen.class);
            intent.putExtra("type", "Edit Card");
            intent.putExtra("card", card);
            startActivity(intent);
        });
        binding.cardRv.setAdapter(adapter);

        binding.addCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCardScreen.class);
            intent.putExtra("type", "Add Card");
            startActivity(intent);
        });
        binding.btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllCards();
    }

    private void getAllCards() {
        cardCol().orderBy("lastUsed", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cards.clear();
                        QuerySnapshot snapshots = task.getResult();
                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentSnapshot snapshot : snapshots) {
                                cards.add(snapshot.toObject(Card.class));
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}