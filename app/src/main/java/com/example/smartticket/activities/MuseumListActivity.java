package com.example.smartticket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartticket.R;
import com.example.smartticket.adapters.MuseumAdapter;
import com.example.smartticket.models.Museum;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen displaying the catalog of museums available for booking.
 */
public class MuseumListActivity extends AppCompatActivity {

    public static final String EXTRA_MUSEUM = "extra_museum";

    private RecyclerView rvMuseums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_list);

        initViews();
        loadMuseums();
    }

    private void initViews() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        rvMuseums = findViewById(R.id.rv_museums);
        rvMuseums.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMuseums() {
        List<Museum> museums = new ArrayList<>();
        museums.add(new Museum(
                1,
                "National Museum",
                "New Delhi",
                "Rich collection of historical artifacts and national treasures spanning 5,000 years of civilization.",
                15.00,
                10.00,
                5.00
        ));
        museums.add(new Museum(
                2,
                "Science Museum",
                "Bengaluru",
                "Interactive science exhibits, planetarium shows, and cutting-edge technological innovations.",
                18.00,
                12.00,
                6.00
        ));
        museums.add(new Museum(
                3,
                "Modern Art Museum",
                "Mumbai",
                "Contemporary visual installations, avant-garde paintings, and thought-provoking modern sculptures.",
                20.00,
                14.00,
                8.00
        ));
        museums.add(new Museum(
                4,
                "History Museum",
                "Kolkata",
                "Ancient relics, rare cultural manuscripts, natural history fossils, and vintage heritage archives.",
                12.00,
                8.00,
                4.00
        ));
        museums.add(new Museum(
                5,
                "City Heritage Museum",
                "Jaipur",
                "Royal architectural marvels, royal armory, exquisite textiles, and centuries of folk artisan heritage.",
                16.00,
                10.00,
                5.00
        ));

        MuseumAdapter adapter = new MuseumAdapter(museums, museum -> {
            Intent intent = new Intent(MuseumListActivity.this, BookingActivity.class);
            intent.putExtra(EXTRA_MUSEUM, museum);
            startActivity(intent);
        });

        rvMuseums.setAdapter(adapter);
    }
}
