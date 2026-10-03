package com.example.smartticket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartticket.R;
import com.example.smartticket.models.Booking;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

/**
 * Screen presenting confirmed digital pass details to user after successful reservation.
 */
public class ConfirmationActivity extends AppCompatActivity {

    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        if (getIntent().hasExtra(BookingActivity.EXTRA_BOOKING)) {
            booking = (Booking) getIntent().getSerializableExtra(BookingActivity.EXTRA_BOOKING);
        }

        if (booking == null) {
            Toast.makeText(this, "Booking details missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindConfirmationData();
        setupActions();
    }

    private void bindConfirmationData() {
        TextView tvBookingId = findViewById(R.id.tv_confirm_booking_id);
        TextView tvStatus = findViewById(R.id.tv_confirm_status);
        TextView tvMuseumName = findViewById(R.id.tv_confirm_museum_name);
        TextView tvDate = findViewById(R.id.tv_confirm_date);
        TextView tvAdultTickets = findViewById(R.id.tv_confirm_adult_tickets);
        TextView tvStudentTickets = findViewById(R.id.tv_confirm_student_tickets);
        TextView tvChildTickets = findViewById(R.id.tv_confirm_child_tickets);
        TextView tvSubtotal = findViewById(R.id.tv_confirm_subtotal);
        TextView tvDiscount = findViewById(R.id.tv_confirm_discount);
        TextView tvTotal = findViewById(R.id.tv_confirm_total);

        tvBookingId.setText(String.format("#%s", booking.getBookingId()));
        tvStatus.setText(booking.getStatus());
        tvMuseumName.setText(booking.getMuseumName());
        tvDate.setText(booking.getVisitDate());

        tvAdultTickets.setText(String.valueOf(booking.getAdultCount()));
        tvStudentTickets.setText(String.valueOf(booking.getStudentCount()));
        tvChildTickets.setText(String.valueOf(booking.getChildCount()));

        tvSubtotal.setText(String.format(Locale.US, "$%.2f", booking.getSubtotal()));
        tvDiscount.setText(String.format(Locale.US, "-$%.2f", booking.getDiscount()));
        tvTotal.setText(String.format(Locale.US, "$%.2f", booking.getTotalAmount()));
    }

    private void setupActions() {
        MaterialButton btnViewBookings = findViewById(R.id.btn_view_my_bookings);
        MaterialButton btnBackToHome = findViewById(R.id.btn_back_to_home);

        btnViewBookings.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmationActivity.this, BookingHistoryActivity.class);
            startActivity(intent);
            finish();
        });

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmationActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
