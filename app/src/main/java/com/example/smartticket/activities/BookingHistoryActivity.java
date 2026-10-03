package com.example.smartticket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartticket.R;
import com.example.smartticket.adapters.BookingHistoryAdapter;
import com.example.smartticket.database.DatabaseHelper;
import com.example.smartticket.models.Booking;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Screen displaying previous ticket bookings retrieved from SQLite database.
 */
public class BookingHistoryActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView rvBookings;
    private View layoutEmptyState;
    private BookingHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        databaseHelper = new DatabaseHelper(this);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings();
    }

    private void initViews() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        rvBookings = findViewById(R.id.rv_booking_history);
        rvBookings.setLayoutManager(new LinearLayoutManager(this));

        layoutEmptyState = findViewById(R.id.layout_empty_state);

        MaterialButton btnEmptyExplore = findViewById(R.id.btn_empty_explore);
        if (btnEmptyExplore != null) {
            btnEmptyExplore.setOnClickListener(v -> {
                Intent intent = new Intent(BookingHistoryActivity.this, MuseumListActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void loadBookings() {
        List<Booking> bookings = databaseHelper.getAllBookings();

        if (bookings == null || bookings.isEmpty()) {
            rvBookings.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvBookings.setVisibility(View.VISIBLE);

            if (adapter == null) {
                adapter = new BookingHistoryAdapter(bookings);
                rvBookings.setAdapter(adapter);
            } else {
                adapter.updateData(bookings);
            }
        }
    }
}
