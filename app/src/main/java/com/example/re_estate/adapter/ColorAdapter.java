package com.example.re_estate.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.re_estate.R;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.colorHolder> {
    Context context;
    int[] colors;
    colorClickListener listener;

    public ColorAdapter(Context context, int[] colors, colorClickListener listener) {
        this.context = context;
        this.colors = colors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public colorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_bg, parent, false);
        return new colorHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull colorHolder holder, int position) {
        int color = colors[position];
        holder.color.setBackgroundTintList(ColorStateList.valueOf(color));
        holder.color.setOnClickListener(v -> {
            listener.onClick(color);
        });
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    public interface colorClickListener{
        void onClick(int color);
    }

    public static class colorHolder extends RecyclerView.ViewHolder{
        ImageView color;
        public colorHolder(@NonNull View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.card_color);
        }
    }
}
