package com.real_estate.realestate_dtt_sindhu.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.real_estate.realestate_dtt_sindhu.R;

public class HouseOverviewActivity extends AppCompatActivity {


    private final BottomNavigationView.OnItemSelectedListener navListener =
            (BottomNavigationView.OnNavigationItemSelectedListener) item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(HouseOverviewActivity.this, HouseOverviewActivity.class);
                        startActivity(intent);
                        finish();
                        selectedFragment = new HouseListFragment();
                        break;
                    case R.id.info:
                        selectedFragment = new AboutTabFragment();
                        break;
                }

                assert selectedFragment != null;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                return true;
            };

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_screen);
        getSupportActionBar().hide(); // hide the title bar
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new HouseListFragment()).commit();
    }
}