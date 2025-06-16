package com.example.re_estate.adapter;

import static com.example.re_estate.misc.FirebaseUtil.userDoc;
import static com.example.re_estate.misc.FirebaseUtil.userId;
import static com.example.re_estate.misc.Utilities.formatPrice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.re_estate.R;
import com.example.re_estate.database.Property;
import com.example.re_estate.database.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.MessageFormat;
import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.propertyHolder> {

    Context context;
    List<Property> properties;
    onItemClickListener listener;
    User user;

    public RecommendedAdapter(Context context, List<Property> properties, onItemClickListener listener) {
        this.context = context;
        this.properties = properties;
        this.listener = listener;
    }

    @NonNull
    @Override
    public propertyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recommended_properties, parent, false);
        return new propertyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull propertyHolder holder, int position) {
        Property property = properties.get(position);
        Glide.with(context).load(property.getImages().get(0)).into(holder.property_image);
        holder.property_price.setText(MessageFormat.format("${0}", formatPrice(property.getPrice())));
        holder.property_category.setText(property.getCategory());
        holder.property_name.setText(property.getTitle());
        holder.property_location.setText(MessageFormat.format("{0}, {1}", property.getState(), property.getCountry()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(property));
        userDoc(userId()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(User.class);
                List<String> favorites = user.getFavorites();
                if (favorites.contains(property.getPropertyId())) {
                    holder.add_fav.setImageResource(R.drawable.favorite);
                } else {
                    holder.add_fav.setImageResource(R.drawable.favorite_border);
                }
            }
        });
        holder.add_fav.setOnClickListener(v -> {
            // Add to favorites
            if (user.getFavorites().contains(property.getPropertyId())) {
                user.getFavorites().remove(property.getPropertyId());
                holder.add_fav.setImageResource(R.drawable.favorite_border);
            } else {
                user.getFavorites().add(property.getPropertyId());
                holder.add_fav.setImageResource(R.drawable.favorite);
            }
            userDoc(userId()).update("favorites", user.getFavorites());
        });
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public interface onItemClickListener {
        void onItemClick(Property property);
    }

    public static class propertyHolder extends RecyclerView.ViewHolder {
        ImageView property_image, add_fav;
        TextView property_price, property_category, property_name, property_location;
        public propertyHolder(@NonNull View itemView) {
            super(itemView);
            property_image = itemView.findViewById(R.id.property_image);
            add_fav = itemView.findViewById(R.id.add_fav);
            property_price = itemView.findViewById(R.id.property_price);
            property_category = itemView.findViewById(R.id.property_category);
            property_name = itemView.findViewById(R.id.property_name);
            property_location = itemView.findViewById(R.id.property_location);
        }
    }
}
