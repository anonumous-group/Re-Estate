package com.example.re_estate.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.re_estate.R;
import com.example.re_estate.databinding.ActivityMainScreenBinding;
import com.example.re_estate.fragment.ChatFragment;
import com.example.re_estate.fragment.ExploreFragment;
import com.example.re_estate.fragment.FavoriteFragment;
import com.example.re_estate.fragment.HomeFragment;

public class MainScreen extends AppCompatActivity {
    ActivityMainScreenBinding binding;
    private HomeFragment homeFragment;
    private ExploreFragment exploreFragment;
    private FavoriteFragment favoriteFragment;
    private ChatFragment chatFragment;
    private Fragment activeFragment; // To keep track of the currently active fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.light_white), getColor(R.color.light_white)
        ));
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Set the initial fragment (e.g., HomeFragment)
        if (savedInstanceState == null) {
            // Initialize fragments.
            homeFragment = new HomeFragment();
            exploreFragment = new ExploreFragment();
            favoriteFragment = new FavoriteFragment();
            chatFragment = new ChatFragment();

            // Add all fragments initially and hide them, show the first one
            activeFragment = homeFragment;
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.frame_layout, homeFragment);
            transaction.add(R.id.frame_layout, exploreFragment);
            transaction.add(R.id.frame_layout, favoriteFragment);
            transaction.add(R.id.frame_layout, chatFragment);
            transaction.hide(exploreFragment);
            transaction.hide(favoriteFragment);
            transaction.hide(chatFragment);
            transaction.show(homeFragment);
            transaction.commit();
        }
        else {
            // Re-acquire fragment instances after a configuration change (if needed)
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("1");
            exploreFragment = (ExploreFragment) getSupportFragmentManager().findFragmentByTag("2");
            favoriteFragment = (FavoriteFragment) getSupportFragmentManager().findFragmentByTag("3");
            chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentByTag("4");
            // Determine active fragment based on saved instance state if you save it, or default to home
            // For simplicity, we'll re-select the bottom nav item which will trigger the show/hide logic
        }

        binding.bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                fragment = homeFragment;
            } else if (itemId == R.id.explore) {
                fragment = exploreFragment;
            } else if (itemId == R.id.favorite) {
                fragment = favoriteFragment;
            } else if (itemId == R.id.chat) {
                fragment = chatFragment;
            }

            if (fragment != null && fragment != activeFragment) {
                loadFragment(fragment);
            }

            return true;
        });


        // Ensure the correct fragment is shown if the activity is recreated
        // and an item was already selected.
        // Or select the initial one
        if (savedInstanceState == null) {
            binding.bottomNav.setSelectedItemId(R.id.home); // Or your default
        }


    }

    private void loadFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (activeFragment != null) {
            transaction.hide(activeFragment);
        }
        transaction.show(fragment);
        transaction.commit();
        activeFragment = fragment;
    }
}