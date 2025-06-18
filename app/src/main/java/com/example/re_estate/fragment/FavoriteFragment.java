package com.example.re_estate.fragment;

import static com.example.re_estate.databinding.FragmentFavoriteBinding.inflate;
import static com.example.re_estate.misc.FirebaseUtil.favCol;
import static com.example.re_estate.misc.Utilities.sendMessage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.re_estate.R;
import com.example.re_estate.adapter.FavoriteAdapter;
import com.example.re_estate.database.Property;
import com.example.re_estate.databinding.FragmentFavoriteBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
        // Required empty public constructor
    }
    FragmentFavoriteBinding binding;
    String queryText = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflate(inflater, container, false);

        binding.btnOpenSearch.setOnClickListener(v -> {
            binding.searchContainer.setVisibility(View.VISIBLE);
            binding.searchBar.requestFocus();
            binding.toolbar.setVisibility(View.GONE);
        });

        binding.btnClose.setOnClickListener(v -> {
            binding.searchContainer.setVisibility(View.GONE);
            binding.toolbar.setVisibility(View.VISIBLE);
            showFav();
        });

        binding.btnSearch.setOnClickListener(v -> {
            queryText = binding.searchBar.getText().toString();
            showFav();
        });

        showFav();

        return binding.getRoot();
    }

    private void showFav() {
        Query query;

        if (queryText.isEmpty()) {
            query = favCol().orderBy("createdAt", Query.Direction.DESCENDING);
        } else {
            query = favCol().whereEqualTo("title", queryText).orderBy("createdAt", Query.Direction.DESCENDING);
        }

        List<Property> properties = new ArrayList<>();
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                if (snapshots != null && !snapshots.isEmpty()) {
                    for (QueryDocumentSnapshot snapshot : snapshots) {
                        Property property = snapshot.toObject(Property.class);
                        properties.add(property);
                    }

                    binding.noFav.setVisibility(View.GONE);
                    FavoriteAdapter adapter = new FavoriteAdapter(getContext(), properties, property -> {});
                    binding.favList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    binding.noFav.setVisibility(View.VISIBLE);
                    if (queryText.isEmpty()) {
                        binding.noFav.setText(snapshots.isEmpty() ? "No favorites" : "No results found");
                    } else {
                        binding.noFav.setText(MessageFormat.format("No results found for {0}", queryText));
                    }
                }
            }
        });
    }
}