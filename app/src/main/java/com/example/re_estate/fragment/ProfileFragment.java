package com.example.re_estate.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.re_estate.R;
import com.example.re_estate.activity.auth.WelcomeScreen;
import com.example.re_estate.databinding.FragmentProfileBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.tvLogout.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
            dialog.setContentView(R.layout.logout_notice);
            dialog.show();

            dialog.findViewById(R.id.btn_cancel).setOnClickListener(v1 -> dialog.dismiss());
            dialog.findViewById(R.id.btn_logout).setOnClickListener(v1 -> {
                dialog.findViewById(R.id.prog_bar).setVisibility(View.VISIBLE);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                auth.signOut();
                if (user == null) {
                    startActivity(new Intent(getContext(), WelcomeScreen.class));
                    requireActivity().finish();
                }
            });
        });
        return binding.getRoot();
    }
}