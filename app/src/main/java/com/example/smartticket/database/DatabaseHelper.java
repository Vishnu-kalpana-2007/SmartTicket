package com.example.smartticket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.smartticket.models.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite database helper for managing ticket bookings.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "smartticket.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_BOOKINGS = "bookings";

    // Column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BOOKING_ID = "booking_id";
    public static final String COLUMN_MUSEUM_NAME = "museum_name";
    public static final String COLUMN_VISIT_DATE = "visit_date";
    public static final String COLUMN_ADULT_COUNT = "adult_count";
    public static final String COLUMN_STUDENT_COUNT = "student_count";
    public static final String COLUMN_CHILD_COUNT = "child_count";
    public static final String COLUMN_SUBTOTAL = "subtotal";
    public static final String COLUMN_DISCOUNT = "discount";
    public static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    public static final String COLUMN_STATUS = "status";

    // SQL query to create bookings table
    private static final String CREATE_TABLE_BOOKINGS = "CREATE TABLE " + TABLE_BOOKINGS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BOOKING_ID + " TEXT NOT NULL, "
            + COLUMN_MUSEUM_NAME + " TEXT NOT NULL, "
            + COLUMN_VISIT_DATE + " TEXT NOT NULL, "
            + COLUMN_ADULT_COUNT + " INTEGER NOT NULL, "
            + COLUMN_STUDENT_COUNT + " INTEGER NOT NULL, "
            + COLUMN_CHILD_COUNT + " INTEGER NOT NULL, "
            + COLUMN_SUBTOTAL + " REAL NOT NULL, "
            + COLUMN_DISCOUNT + " REAL NOT NULL, "
            + COLUMN_TOTAL_AMOUNT + " REAL NOT NULL, "
            + COLUMN_STATUS + " TEXT NOT NULL"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOKINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    /**
     * Inserts a new booking into SQLite.
     *
     * @param booking Booking object containing reservation details
     * @return Row ID of inserted booking, or -1 on failure
     */
    public long insertBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_ID, booking.getBookingId());
        values.put(COLUMN_MUSEUM_NAME, booking.getMuseumName());
        values.put(COLUMN_VISIT_DATE, booking.getVisitDate());
        values.put(COLUMN_ADULT_COUNT, booking.getAdultCount());
        values.put(COLUMN_STUDENT_COUNT, booking.getStudentCount());
        values.put(COLUMN_CHILD_COUNT, booking.getChildCount());
        values.put(COLUMN_SUBTOTAL, booking.getSubtotal());
        values.put(COLUMN_DISCOUNT, booking.getDiscount());
        values.put(COLUMN_TOTAL_AMOUNT, booking.getTotalAmount());
        values.put(COLUMN_STATUS, booking.getStatus());

        long result = -1;
        try {
            result = db.insert(TABLE_BOOKINGS, null, values);
            if (result != -1) {
                booking.setId(result);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting booking", e);
        }
        return result;
    }

    /**
     * Retrieves all bookings ordered newest first.
     *
     * @return List of Booking objects
     */
    public List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_BOOKINGS + " ORDER BY " + COLUMN_ID + " DESC";

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String bookingId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID));
                    String museumName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSEUM_NAME));
                    String visitDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VISIT_DATE));
                    int adultCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ADULT_COUNT));
                    int studentCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_COUNT));
                    int childCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHILD_COUNT));
                    double subtotal = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SUBTOTAL));
                    double discount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT));
                    double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));

                    Booking booking = new Booking(id, bookingId, museumName, visitDate,
                            adultCount, studentCount, childCount, subtotal, discount, totalAmount, status);
                    bookingList.add(booking);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error querying bookings", e);
        }

        return bookingList;
    }

    /**
     * Retrieve a specific booking by its unique booking ID.
     */
    public Booking getBookingById(String bookingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKINGS + " WHERE " + COLUMN_BOOKING_ID + " = ? LIMIT 1";

        try (Cursor cursor = db.rawQuery(query, new String[]{bookingId})) {
            if (cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String bId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID));
                String museumName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSEUM_NAME));
                String visitDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VISIT_DATE));
                int adultCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ADULT_COUNT));
                int studentCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_COUNT));
                int childCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHILD_COUNT));
                double subtotal = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SUBTOTAL));
                double discount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT));
                double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));

                return new Booking(id, bId, museumName, visitDate,
                        adultCount, studentCount, childCount, subtotal, discount, totalAmount, status);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error querying booking by ID", e);
        }
        return null;
    }
}
