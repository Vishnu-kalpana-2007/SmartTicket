package com.example.smartticket.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smartticket.R;
import com.example.smartticket.database.DatabaseHelper;
import com.example.smartticket.models.Booking;
import com.example.smartticket.models.Museum;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

/**
 * Screen where users configure visit date, select ticket quantities,
 * apply discount coupon, and confirm ticket reservation.
 */
public class BookingActivity extends AppCompatActivity {

    public static final String EXTRA_BOOKING = "extra_booking";
    private static final String VALID_COUPON = "SMART10";
    private static final double DISCOUNT_RATE = 0.10; // 10% discount

    private Museum museum;
    private DatabaseHelper databaseHelper;

    // UI elements
    private TextView tvMuseumName, tvMuseumCity;
    private View layoutDatePicker;
    private TextView tvSelectedDate, tvDateError;
    private TextView tvAdultUnitPrice, tvStudentUnitPrice, tvChildUnitPrice;
    private TextView tvAdultCount, tvStudentCount, tvChildCount, tvTicketCountError;
    private ImageButton btnAdultMinus, btnAdultPlus;
    private ImageButton btnStudentMinus, btnStudentPlus;
    private ImageButton btnChildMinus, btnChildPlus;
    private EditText etCoupon;
    private MaterialButton btnApplyCoupon;
    private TextView tvCouponStatus;
    private TextView tvSubtotal, tvDiscount, tvFinalTotal;
    private MaterialButton btnConfirmBooking;

    // State variables
    private Calendar selectedDateCalendar = null;
    private int adultCount = 0;
    private int studentCount = 0;
    private int childCount = 0;
    private boolean isCouponApplied = false;

    private double currentSubtotal = 0.0;
    private double currentDiscount = 0.0;
    private double currentFinalTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        databaseHelper = new DatabaseHelper(this);

        if (getIntent().hasExtra(MuseumListActivity.EXTRA_MUSEUM)) {
            museum = (Museum) getIntent().getSerializableExtra(MuseumListActivity.EXTRA_MUSEUM);
        }

        if (museum == null) {
            Toast.makeText(this, "Museum details not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        bindMuseumData();
        setupListeners();
        updatePriceCalculations();
    }

    private void initViews() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        tvMuseumName = findViewById(R.id.tv_booking_museum_name);
        tvMuseumCity = findViewById(R.id.tv_booking_museum_city);

        layoutDatePicker = findViewById(R.id.layout_date_picker);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        tvDateError = findViewById(R.id.tv_date_error);

        tvAdultUnitPrice = findViewById(R.id.tv_adult_unit_price);
        tvStudentUnitPrice = findViewById(R.id.tv_student_unit_price);
        tvChildUnitPrice = findViewById(R.id.tv_child_unit_price);

        tvAdultCount = findViewById(R.id.tv_adult_count);
        tvStudentCount = findViewById(R.id.tv_student_count);
        tvChildCount = findViewById(R.id.tv_child_count);
        tvTicketCountError = findViewById(R.id.tv_ticket_count_error);

        btnAdultMinus = findViewById(R.id.btn_adult_minus);
        btnAdultPlus = findViewById(R.id.btn_adult_plus);
        btnStudentMinus = findViewById(R.id.btn_student_minus);
        btnStudentPlus = findViewById(R.id.btn_student_plus);
        btnChildMinus = findViewById(R.id.btn_child_minus);
        btnChildPlus = findViewById(R.id.btn_child_plus);

        etCoupon = findViewById(R.id.et_coupon);
        btnApplyCoupon = findViewById(R.id.btn_apply_coupon);
        tvCouponStatus = findViewById(R.id.tv_coupon_status);

        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvDiscount = findViewById(R.id.tv_discount);
        tvFinalTotal = findViewById(R.id.tv_final_total);

        btnConfirmBooking = findViewById(R.id.btn_confirm_booking);
    }

    private void bindMuseumData() {
        tvMuseumName.setText(museum.getName());
        tvMuseumCity.setText(museum.getCity());

        tvAdultUnitPrice.setText(String.format(Locale.US, "$%.2f each", museum.getAdultPrice()));
        tvStudentUnitPrice.setText(String.format(Locale.US, "$%.2f each", museum.getStudentPrice()));
        tvChildUnitPrice.setText(String.format(Locale.US, "$%.2f each", museum.getChildPrice()));
    }

    private void setupListeners() {
        // Date Picker
        layoutDatePicker.setOnClickListener(v -> showDatePicker());

        // Adult ticket counter
        btnAdultPlus.setOnClickListener(v -> {
            adultCount++;
            tvAdultCount.setText(String.valueOf(adultCount));
            tvTicketCountError.setVisibility(View.GONE);
            updatePriceCalculations();
        });

        btnAdultMinus.setOnClickListener(v -> {
            if (adultCount > 0) {
                adultCount--;
                tvAdultCount.setText(String.valueOf(adultCount));
                updatePriceCalculations();
            }
        });

        // Student ticket counter
        btnStudentPlus.setOnClickListener(v -> {
            studentCount++;
            tvStudentCount.setText(String.valueOf(studentCount));
            tvTicketCountError.setVisibility(View.GONE);
            updatePriceCalculations();
        });

        btnStudentMinus.setOnClickListener(v -> {
            if (studentCount > 0) {
                studentCount--;
                tvStudentCount.setText(String.valueOf(studentCount));
                updatePriceCalculations();
            }
        });

        // Child ticket counter
        btnChildPlus.setOnClickListener(v -> {
            childCount++;
            tvChildCount.setText(String.valueOf(childCount));
            tvTicketCountError.setVisibility(View.GONE);
            updatePriceCalculations();
        });

        btnChildMinus.setOnClickListener(v -> {
            if (childCount > 0) {
                childCount--;
                tvChildCount.setText(String.valueOf(childCount));
                updatePriceCalculations();
            }
        });

        // Apply Coupon
        btnApplyCoupon.setOnClickListener(v -> handleCouponApply());

        // Confirm Booking
        btnConfirmBooking.setOnClickListener(v -> handleBookingConfirmation());
    }

    private void showDatePicker() {
        final Calendar now = Calendar.getInstance();
        int year = (selectedDateCalendar != null) ? selectedDateCalendar.get(Calendar.YEAR) : now.get(Calendar.YEAR);
        int month = (selectedDateCalendar != null) ? selectedDateCalendar.get(Calendar.MONTH) : now.get(Calendar.MONTH);
        int day = (selectedDateCalendar != null) ? selectedDateCalendar.get(Calendar.DAY_OF_MONTH) : now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, pickedYear, pickedMonth, pickedDay) -> {
            Calendar pickedCal = Calendar.getInstance();
            pickedCal.set(pickedYear, pickedMonth, pickedDay, 0, 0, 0);
            pickedCal.set(Calendar.MILLISECOND, 0);

            Calendar todayCal = Calendar.getInstance();
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);
            todayCal.set(Calendar.MILLISECOND, 0);

            if (pickedCal.before(todayCal)) {
                tvDateError.setText("Visit date cannot be in the past");
                tvDateError.setVisibility(View.VISIBLE);
                selectedDateCalendar = null;
                tvSelectedDate.setText("");
                return;
            }

            selectedDateCalendar = pickedCal;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            tvSelectedDate.setText(sdf.format(pickedCal.getTime()));
            tvDateError.setVisibility(View.GONE);
        }, year, month, day);

        // Disallow selecting past dates in the dialog widget
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void handleCouponApply() {
        String enteredCode = etCoupon.getText().toString().trim();
        if (TextUtils.isEmpty(enteredCode)) {
            isCouponApplied = false;
            tvCouponStatus.setText("Please enter a coupon code");
            tvCouponStatus.setTextColor(ContextCompat.getColor(this, R.color.error_red));
            tvCouponStatus.setVisibility(View.VISIBLE);
            updatePriceCalculations();
            return;
        }

        if (VALID_COUPON.equalsIgnoreCase(enteredCode)) {
            isCouponApplied = true;
            tvCouponStatus.setText("✓ Coupon SMART10 applied! 10% discount active.");
            tvCouponStatus.setTextColor(ContextCompat.getColor(this, R.color.secondary));
            tvCouponStatus.setVisibility(View.VISIBLE);
        } else {
            isCouponApplied = false;
            tvCouponStatus.setText("Invalid coupon. Use code SMART10 for 10% off.");
            tvCouponStatus.setTextColor(ContextCompat.getColor(this, R.color.error_red));
            tvCouponStatus.setVisibility(View.VISIBLE);
        }

        updatePriceCalculations();
    }

    /**
     * Dynamically calculates subtotal, discount, and final total amounts.
     */
    private void updatePriceCalculations() {
        currentSubtotal = (adultCount * museum.getAdultPrice())
                + (studentCount * museum.getStudentPrice())
                + (childCount * museum.getChildPrice());

        if (isCouponApplied && currentSubtotal > 0) {
            currentDiscount = currentSubtotal * DISCOUNT_RATE;
        } else {
            currentDiscount = 0.0;
        }

        currentFinalTotal = Math.max(0.0, currentSubtotal - currentDiscount);

        tvSubtotal.setText(String.format(Locale.US, "$%.2f", currentSubtotal));
        tvDiscount.setText(String.format(Locale.US, "-$%.2f", currentDiscount));
        tvFinalTotal.setText(String.format(Locale.US, "$%.2f", currentFinalTotal));
    }

    /**
     * Validates input rules and processes reservation.
     */
    private void handleBookingConfirmation() {
        boolean isValid = true;

        // 1. Validate Visit Date
        if (selectedDateCalendar == null || TextUtils.isEmpty(tvSelectedDate.getText().toString())) {
            tvDateError.setText("Please select a visit date");
            tvDateError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            Calendar todayCal = Calendar.getInstance();
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);
            todayCal.set(Calendar.MILLISECOND, 0);

            if (selectedDateCalendar.before(todayCal)) {
                tvDateError.setText("Visit date cannot be in the past");
                tvDateError.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                tvDateError.setVisibility(View.GONE);
            }
        }

        // 2. Validate Ticket Quantities (must be at least 1 ticket selected)
        int totalTickets = adultCount + studentCount + childCount;
        if (totalTickets <= 0) {
            tvTicketCountError.setText("Please select at least one ticket to proceed");
            tvTicketCountError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            tvTicketCountError.setVisibility(View.GONE);
        }

        // 3. Ensure quantities are non-negative
        if (adultCount < 0 || studentCount < 0 || childCount < 0) {
            tvTicketCountError.setText("Ticket quantities cannot be negative");
            tvTicketCountError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, "Please resolve the errors above", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique booking ID (e.g. ST-849201)
        int randomSuffix = 100000 + new Random().nextInt(900000);
        String bookingId = "ST-" + randomSuffix;
        String visitDate = tvSelectedDate.getText().toString().trim();
        String status = "Confirmed";

        Booking booking = new Booking(
                bookingId,
                museum.getName(),
                visitDate,
                adultCount,
                studentCount,
                childCount,
                currentSubtotal,
                currentDiscount,
                currentFinalTotal,
                status
        );

        // Save into SQLite
        long insertResult = databaseHelper.insertBooking(booking);
        if (insertResult == -1) {
            Toast.makeText(this, "Failed to save booking. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Navigate to ConfirmationActivity
        Intent intent = new Intent(BookingActivity.this, ConfirmationActivity.class);
        intent.putExtra(EXTRA_BOOKING, booking);
        startActivity(intent);
        finish();
    }
}
