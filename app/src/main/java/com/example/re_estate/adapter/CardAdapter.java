package com.example.re_estate.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.re_estate.R;
import com.example.re_estate.database.Card;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.colorHolder> {
    Context context;
    List<Card> cards;
    colorClickListener listener;

    public CardAdapter(Context context, List<Card> cards, colorClickListener listener) {
        this.context = context;
        this.cards = cards;
        this.listener = listener;
    }

    @NonNull
    @Override
    public colorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new colorHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull colorHolder holder, int position) {
        Card card = cards.get(position);
        holder.color.setBackgroundTintList(ColorStateList.valueOf(card.getCardBackground()));
        holder.color.setText(card.getCardType());
        holder.number.setText(card.getCardNumber());
        holder.itemView.setOnClickListener(v -> listener.onClick(card));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public interface colorClickListener{
        void onClick(Card card);
    }

    public static class colorHolder extends RecyclerView.ViewHolder{
        TextView number, color;
        public colorHolder(@NonNull View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.card_color);
            number = itemView.findViewById(R.id.card_number);
        }
    }
}
