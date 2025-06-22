package com.example.re_estate.fragment;

import static com.example.re_estate.databinding.FragmentFavoriteBinding.inflate;
import static com.example.re_estate.misc.FirebaseUtil.favCol;
import static com.example.re_estate.misc.FirebaseUtil.favDoc;
import static com.example.re_estate.misc.Utilities.sendMessage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.re_estate.R;
import com.example.re_estate.adapter.FavoriteAdapter;
import com.example.re_estate.database.Property;
import com.example.re_estate.databinding.FragmentFavoriteBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
            queryText = "";
            binding.searchBar.setText("");
            binding.searchBar.clearFocus();
            showFav();
        });

        binding.btnSearch.setOnClickListener(v -> {
            queryText = binding.searchBar.getText().toString();
            binding.favList.setAdapter(null);
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
                    FavoriteAdapter adapter = new FavoriteAdapter(getContext(), properties, property -> {
                        openFavoriteSheet(property);
                    });
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
            } else {
                Log.e("FavoriteFragment", "Error getting documents: ", task.getException());
            }
        });
    }

    private void openFavoriteSheet(Property property) {
        BottomSheetDialog favDialog = new BottomSheetDialog(requireContext());
        favDialog.setContentView(R.layout.favorite_sheet);
        favDialog.show();

        favDialog.findViewById(R.id.btn_close).setOnClickListener(v -> favDialog.dismiss());

        favDialog.findViewById(R.id.view).setOnClickListener(v -> {
            favDialog.dismiss();
            sendMessage(requireContext(), property.getTitle());
        });

        favDialog.findViewById(R.id.remove).setOnClickListener(v -> {
            openRemoveSheet(property);
            favDialog.dismiss();
        });
    }

    private void openRemoveSheet(Property property) {
        BottomSheetDialog removeSheet = new BottomSheetDialog(requireContext());
        removeSheet.setContentView(R.layout.remove_favorite_sheet);
        removeSheet.show();

        removeSheet.findViewById(R.id.btn_cancel).setOnClickListener(v -> removeSheet.dismiss());
        removeSheet.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            removeSheet.findViewById(R.id.prog_bar).setVisibility(View.VISIBLE);
            removeSheet.findViewById(R.id.buttons).setVisibility(View.GONE);

            favDoc(property.getPropertyId()).delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    removeSheet.dismiss();
                    showFav();
                } else {
                    Log.e("FavoriteFragment", "Error deleting document: ", task.getException());
                }
            });
        });
    }
}