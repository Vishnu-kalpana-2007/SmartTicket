package com.example.smartticket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartticket.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

/**
 * Home screen for SmartTicket application.
 * Serves as the primary entry point to explore museums and view bookings.
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupViews();
    }

    private void setupViews() {
        MaterialCardView cardExplore = findViewById(R.id.card_explore_museums);
        MaterialButton btnExplore = findViewById(R.id.btn_explore_museums);
        MaterialCardView cardBookings = findViewById(R.id.card_my_bookings);
        MaterialButton btnBookings = findViewById(R.id.btn_my_bookings);

        View.OnClickListener exploreClickListener = v -> {
            Intent intent = new Intent(HomeActivity.this, MuseumListActivity.class);
            startActivity(intent);
        };

        View.OnClickListener bookingsClickListener = v -> {
            Intent intent = new Intent(HomeActivity.this, BookingHistoryActivity.class);
            startActivity(intent);
        };

        if (cardExplore != null) cardExplore.setOnClickListener(exploreClickListener);
        if (btnExplore != null) btnExplore.setOnClickListener(exploreClickListener);
        if (cardBookings != null) cardBookings.setOnClickListener(bookingsClickListener);
        if (btnBookings != null) btnBookings.setOnClickListener(bookingsClickListener);
    }
}
