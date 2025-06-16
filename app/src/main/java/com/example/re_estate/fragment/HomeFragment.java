package com.example.re_estate.fragment;

import static com.example.re_estate.databinding.FragmentHomeBinding.inflate;
import static com.example.re_estate.misc.FirebaseUtil.propertyCol;
import static com.example.re_estate.misc.FirebaseUtil.userDoc;
import static com.example.re_estate.misc.FirebaseUtil.userId;
import static com.example.re_estate.misc.Utilities.sendMessage;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.re_estate.R;
import com.example.re_estate.adapter.PropertyAdapter;
import com.example.re_estate.adapter.RecommendedAdapter;
import com.example.re_estate.database.Property;
import com.example.re_estate.database.User;
import com.example.re_estate.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    FragmentHomeBinding binding;
    User user;
    String city, state, country;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflate(inflater, container, false);

        showLocation();

        return binding.getRoot();
    }

    private void showLocation() {
        userDoc(userId()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(User.class);
                GeoPoint geoPoint = user.getGeoPoint();

                if (geoPoint != null && getContext() != null) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                geoPoint.getLatitude(),
                                geoPoint.getLongitude(),
                                1); // Get a single address

                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            // You can now use the Address object
                            // String state = address.getAdminArea();
                            state = address.getAdminArea();
                            // String country = address.getCountryName();
                            country = address.getCountryName();
                            // String city = address.getLocality();
                            city = address.getLocality();
                            binding.location.setText(MessageFormat.format("{0}, {1}", state, country)); // Assuming you have a TextView with id location

                            // You can access other address components like:
                            // String postalCode = address.getPostalCode();
                            // String knownName = address.getFeatureName(); // eg. landmark name

                            getRecommendedProperties();

                            getNearbyProperties();

                        } else {
                            // No address found for the coordinates
                            Toast.makeText(getContext(), "Address not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        // Handle potential I/O errors (e.g., network issues)
                        Log.e("LocationError", "Error getting address: " + e.getMessage());
                        Toast.makeText(getContext(), "Error getting address: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void getRecommendedProperties() {
        // Get recommended properties from Firestore
        List<Property> recommendedProperties = new ArrayList<>();
        //Set search query
        Query query = propertyCol().where(Filter.or(
                        Filter.equalTo("city", city),
                        Filter.equalTo("state", state),
                        Filter.equalTo("country", country)
                )).orderBy("views", Query.Direction.DESCENDING)
                .limit(20);
        // Execute the query
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                if (snapshots != null && !snapshots.isEmpty()) {
                    for (QueryDocumentSnapshot snapshot : snapshots) {
                        Property property = snapshot.toObject(Property.class);
                        recommendedProperties.add(property);
                    }
                    // Shuffle the list of properties
                    Collections.shuffle(recommendedProperties);
                    // Limit the list to 6 properties
                    if (recommendedProperties.size() > 6) {
                        recommendedProperties.subList(6, recommendedProperties.size()).clear();
                    }
                    RecommendedAdapter adapter = new RecommendedAdapter(getContext(), recommendedProperties, property -> {
                                sendMessage(getContext(), property.getPropertyId());
                    });
                    binding.recommendList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("FirestoreError", "No recommended properties found");
                }
            } else {
                Log.e("FirestoreError", "Error getting recommended properties: " + task.getException().getMessage());
            }
        });
    }

    private void getNearbyProperties() {
        // Get recommended properties from Firestore
        List<Property> nearbyProperties = new ArrayList<>();
        //Set search query
        Query query = propertyCol().where(Filter.or(
                        Filter.equalTo("city", city),
                        Filter.equalTo("state", state)
                )).orderBy("views", Query.Direction.DESCENDING)
                .limit(40);
        // Execute the query
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                if (snapshots != null && !snapshots.isEmpty()) {
                    for (QueryDocumentSnapshot snapshot : snapshots) {
                        Property property = snapshot.toObject(Property.class);
                        nearbyProperties.add(property);
                    }
                    // Shuffle the list of properties
                    Collections.shuffle(nearbyProperties);
                    // Limit the list to 20 properties
                    if (nearbyProperties.size() > 20) {
                        nearbyProperties.subList(20, nearbyProperties.size()).clear();
                    }
                    PropertyAdapter adapter = new PropertyAdapter(getContext(), nearbyProperties, property -> {
                        sendMessage(getContext(), property.getPropertyId());
                    });
                    binding.nearbyList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("FirestoreError", "No recommended properties found");
                }
            } else {
                Log.e("FirestoreError", "Error getting recommended properties: " + task.getException().getMessage());
            }
        });
    }
}