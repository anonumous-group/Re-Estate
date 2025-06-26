package com.example.re_estate.activity.payment;

import static com.example.re_estate.misc.FirebaseUtil.cardDoc;
import static com.example.re_estate.misc.Utilities.sendMessage;
import static com.example.re_estate.misc.Utilities.setInProgress;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.re_estate.R;
import com.example.re_estate.adapter.ColorAdapter;
import com.example.re_estate.database.Card;
import com.example.re_estate.databinding.ActivityAddCardScreenBinding;
import com.example.re_estate.misc.CardValidator;
import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.Locale;
import java.util.Random;

public class AddCardScreen extends AppCompatActivity {
    ActivityAddCardScreenBinding binding;
    String type;
    ColorAdapter adapter;
    int cardBackground;
    Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.white), getColor(R.color.white)
        ));
        binding = ActivityAddCardScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        cardBackground = getColor(R.color.blue);
        int[] colors = {getColor(R.color.blue), getColor(R.color.red), getColor(R.color.gray), getColor(R.color.off_white)};
        type = getIntent().getStringExtra("type");
        card = getIntent().getParcelableExtra("card");
        adapter = new ColorAdapter(this, colors, color -> {
            binding.card.setBackgroundTintList(ColorStateList.valueOf(color));
            cardBackground = color;
        });
        adapter.notifyDataSetChanged();

        if (card != null) {
            setCard();
        }

        binding.layoutType.setText(type);
        binding.backgroundRv.setAdapter(adapter);
        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.cardName.setText(s.toString().toUpperCase(Locale.getDefault()));
                } else {
                    binding.cardName.setText("YOUR NAME");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cardNumber = s.toString().replaceAll("\\s", "");
                binding.cardType.setText(CardValidator.detectCardType(cardNumber));

                String formattedNumber = CardValidator.formatCardNumber(cardNumber);
                binding.cardNumber.removeTextChangedListener(this);
                binding.cardNumber.setText(formattedNumber);
                binding.cardNumber.setSelection(formattedNumber.length());
                if (!cardNumber.isEmpty()) {
                    binding.number.setText(binding.cardNumber.getText().toString());
                } else {
                    binding.number.setText("---- ---- ---- ----");
                }
                binding.cardNumber.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.cardExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String expiryNumber = s.toString().replaceAll("\\s", "");
                String formattedExpiry = CardValidator.formatCardExpiry(expiryNumber);
                binding.cardExpiry.removeTextChangedListener(this);
                binding.cardExpiry.setText(formattedExpiry);
                binding.cardExpiry.setSelection(formattedExpiry.length());
                if (!expiryNumber.isEmpty()) {
                    binding.expiry.setText(binding.cardExpiry.getText().toString());
                } else {
                    binding.expiry.setText("--/--");
                }
                binding.cardExpiry.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.btnNext.setOnClickListener(v -> saveCardInfo());
    }

    private void setCard() {
        binding.name.setText(card.getCardName());
        binding.cardName.setText(card.getCardName());
        binding.cardNumber.setText(card.getCardNumber());
        binding.number.setText(card.getCardNumber());
        binding.cardExpiry.setText(card.getCardExpiry());
        binding.expiry.setText(card.getCardExpiry());
        binding.cardSecurity.setText(card.getCardSecurity());
        binding.cardType.setText(card.getCardType());
        binding.card.setBackgroundTintList(ColorStateList.valueOf(card.getCardBackground()));
        binding.btnNext.setText("UPDATE");
        cardBackground = card.getCardBackground();
    }

    private void saveCardInfo() {
        String cardType = binding.cardType.getText().toString();
        String cardName = binding.name.getText().toString().trim();
        String cardNumber = binding.cardNumber.getText().toString().trim();
        String cardExpiry = binding.cardExpiry.getText().toString();
        String cardSecurity = binding.cardSecurity.getText().toString();
        String cardId;
        if (card != null ) {
            cardId = card.getCardId();
        } else {
            cardId = cardId();
        }

        if (!cardType.equals("VISA") && !cardType.equals("MASTERCARD")) {
            sendMessage(this, "Card type not recognized \nCheck your card number and try again");
            binding.cardNumber.requestFocus();
            return;
        }

        if (cardNumber.length() < 19) {
            binding.cardNumber.requestFocus();
            return;
        }

        if (!CardValidator.isValidCardNumber(cardNumber)) {
            sendMessage(this, "Invalid card!!!");
            binding.cardNumber.requestFocus();
            return;
        }

        if (cardExpiry.length() < 5) {
            binding.cardExpiry.requestFocus();
            return;
        }

        if (cardSecurity.length() < 3) {
            binding.cardSecurity.requestFocus();
            return;
        }

        setInProgress(binding.btnNext, binding.progBar, true);

        Card card = new Card(cardType, cardName, cardNumber, cardExpiry, cardSecurity, cardId,
                cardBackground, Timestamp.now(), Timestamp.now());
        cardDoc(cardId).set(card).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
            } else {
                sendMessage(this, task.getException().getMessage());
            }
        });
    }

    private String cardId() {
        StringBuilder builder = new StringBuilder();
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890$&";
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
            builder.append(letters.charAt(random.nextInt(letters.length())));
        }

        return builder.toString();
    }
}