package com.example.re_estate.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Card implements Parcelable {
    String cardType, cardName, cardNumber, cardExpiry, cardSecurity, cardId;
    int cardBackground;
    Timestamp timeAdded, lastUsed;

    public Card() {
    }

    public Card(String cardType, String cardName, String cardNumber, String cardExpiry,
                String cardSecurity, String cardId, int cardBackground, Timestamp timeAdded, Timestamp lastUsed) {
        this.cardType = cardType;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardSecurity = cardSecurity;
        this.cardId = cardId;
        this.cardBackground = cardBackground;
        this.timeAdded = timeAdded;
        this.lastUsed = lastUsed;
    }

    protected Card(Parcel in) {
        cardType = in.readString();
        cardName = in.readString();
        cardNumber = in.readString();
        cardExpiry = in.readString();
        cardSecurity = in.readString();
        cardId = in.readString();
        cardBackground = in.readInt();
        timeAdded = in.readParcelable(Timestamp.class.getClassLoader());
        lastUsed = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public String getCardSecurity() {
        return cardSecurity;
    }

    public void setCardSecurity(String cardSecurity) {
        this.cardSecurity = cardSecurity;
    }

    public String getCardId()  {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getCardBackground() {
        return cardBackground;
    }

    public void setCardBackground(int cardBackground) {
        this.cardBackground = cardBackground;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public Timestamp getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Timestamp lastUsed) {
        this.lastUsed = lastUsed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(cardType);
        dest.writeString(cardName);
        dest.writeString(cardNumber);
        dest.writeString(cardExpiry);
        dest.writeString(cardSecurity);
        dest.writeString(cardId);
        dest.writeInt(cardBackground);
        dest.writeParcelable(timeAdded, flags);
        dest.writeParcelable(lastUsed, flags);
    }
}
