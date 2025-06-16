package com.example.re_estate.fragment;

import static com.example.re_estate.databinding.FragmentExploreBinding.inflate;
import static com.example.re_estate.misc.FirebaseUtil.userDoc;
import static com.example.re_estate.misc.FirebaseUtil.userId;
import static com.example.re_estate.misc.Utilities.getBitmapFromVectorDrawable;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.re_estate.R;
import com.example.re_estate.database.User;
import com.example.re_estate.databinding.FragmentExploreBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ExploreFragment extends Fragment implements OnMapReadyCallback {

    public ExploreFragment() {
        // Required empty public constructor
    }
    FragmentExploreBinding binding;
    private GoogleMap mMap;
    User user;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflate(inflater, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        return binding.getRoot();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        userDoc(userId()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    LatLng latLng = new LatLng(user.getGeoPoint().getLatitude(), user.getGeoPoint().getLongitude());
                    LatLng latLng1 = new LatLng(6.41222, 4.0947);

                    Bitmap bitmap = getBitmapFromVectorDrawable(getContext(), R.drawable.location_on);

                    MarkerOptions options = new MarkerOptions()
                            .position(latLng)
                                    .title("You")
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                    Bitmap bitmap1 = getBitmapFromVectorDrawable(getContext(), R.drawable.home_pin);

                    MarkerOptions options1 = new MarkerOptions()
                            .position(latLng1)
                            .title("Apartment")
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap1));

                    mMap.addMarker(options);
                    mMap.addMarker(options1);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                }
            }
        });
    }
}