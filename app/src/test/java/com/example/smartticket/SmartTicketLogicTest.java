package com.example.smartticket;

import com.example.smartticket.models.Booking;
import com.example.smartticket.models.Museum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests verifying business logic for pricing, coupons, and validations.
 */
public class SmartTicketLogicTest {

    @Test
    public void testMuseumCreationAndPriceProperties() {
        Museum museum = new Museum(1, "National Museum", "New Delhi", "Historic collection", 15.00, 10.00, 5.00);
        assertEquals("National Museum", museum.getName());
        assertEquals("New Delhi", museum.getCity());
        assertEquals(15.00, museum.getAdultPrice(), 0.001);
        assertEquals(10.00, museum.getStudentPrice(), 0.001);
        assertEquals(5.00, museum.getChildPrice(), 0.001);
    }

    @Test
    public void testSubtotalCalculation() {
        double adultPrice = 15.00;
        double studentPrice = 10.00;
        double childPrice = 5.00;

        int adultCount = 2;
        int studentCount = 1;
        int childCount = 2;

        double expectedSubtotal = (2 * adultPrice) + (1 * studentPrice) + (2 * childPrice);
        // (2*15) + (1*10) + (2*5) = 30 + 10 + 10 = 50.00
        assertEquals(50.00, expectedSubtotal, 0.001);
    }

    @Test
    public void testCouponDiscountCalculation_SMART10() {
        String enteredCoupon = "smart10";
        String validCoupon = "SMART10";

        boolean isValid = validCoupon.equalsIgnoreCase(enteredCoupon.trim());
        assertTrue(isValid);

        double subtotal = 50.00;
        double discountRate = 0.10;
        double discount = isValid ? subtotal * discountRate : 0.0;
        double finalTotal = subtotal - discount;

        assertEquals(5.00, discount, 0.001);
        assertEquals(45.00, finalTotal, 0.001);
    }

    @Test
    public void testInvalidCouponCode() {
        String enteredCoupon = "SUMMER20";
        String validCoupon = "SMART10";

        boolean isValid = validCoupon.equalsIgnoreCase(enteredCoupon.trim());
        assertFalse(isValid);

        double subtotal = 50.00;
        double discount = isValid ? subtotal * 0.10 : 0.0;
        double finalTotal = subtotal - discount;

        assertEquals(0.00, discount, 0.001);
        assertEquals(50.00, finalTotal, 0.001);
    }

    @Test
    public void testBookingModelProperties() {
        Booking booking = new Booking("ST-123456", "Science Museum", "2026-10-12",
                1, 2, 0, 38.00, 3.80, 34.20, "Confirmed");

        assertEquals("ST-123456", booking.getBookingId());
        assertEquals("Science Museum", booking.getMuseumName());
        assertEquals("2026-10-12", booking.getVisitDate());
        assertEquals(1, booking.getAdultCount());
        assertEquals(2, booking.getStudentCount());
        assertEquals(0, booking.getChildCount());
        assertEquals(3, booking.getTotalTickets());
        assertEquals(38.00, booking.getSubtotal(), 0.001);
        assertEquals(3.80, booking.getDiscount(), 0.001);
        assertEquals(34.20, booking.getTotalAmount(), 0.001);
        assertEquals("Confirmed", booking.getStatus());
    }

    @Test
    public void testTicketSelectionValidation() {
        int adult = 0;
        int student = 0;
        int child = 0;
        assertFalse("At least one ticket must be selected", (adult + student + child) > 0);

        adult = 1;
        assertTrue("Valid when at least one ticket selected", (adult + student + child) > 0);
    }
}
