package com.example.smartticket.models;

import java.io.Serializable;

/**
 * Model representing a confirmed ticket booking.
 */
public class Booking implements Serializable {
    private long id;
    private String bookingId;
    private String museumName;
    private String visitDate;
    private int adultCount;
    private int studentCount;
    private int childCount;
    private double subtotal;
    private double discount;
    private double totalAmount;
    private String status;

    public Booking() {
    }

    public Booking(String bookingId, String museumName, String visitDate,
                   int adultCount, int studentCount, int childCount,
                   double subtotal, double discount, double totalAmount, String status) {
        this.bookingId = bookingId;
        this.museumName = museumName;
        this.visitDate = visitDate;
        this.adultCount = adultCount;
        this.studentCount = studentCount;
        this.childCount = childCount;
        this.subtotal = subtotal;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Booking(long id, String bookingId, String museumName, String visitDate,
                   int adultCount, int studentCount, int childCount,
                   double subtotal, double discount, double totalAmount, String status) {
        this.id = id;
        this.bookingId = bookingId;
        this.museumName = museumName;
        this.visitDate = visitDate;
        this.adultCount = adultCount;
        this.studentCount = studentCount;
        this.childCount = childCount;
        this.subtotal = subtotal;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getMuseumName() {
        return museumName;
    }

    public void setMuseumName(String museumName) {
        this.museumName = museumName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalTickets() {
        return adultCount + studentCount + childCount;
    }
}
