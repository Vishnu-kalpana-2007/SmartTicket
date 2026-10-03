package com.example.smartticket.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartticket.R;
import com.example.smartticket.models.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying previous bookings in a RecyclerView.
 */
public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingHistoryAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList != null ? bookingList : new ArrayList<>();
    }

    public void updateData(List<Booking> newBookings) {
        this.bookingList = newBookings != null ? newBookings : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_history, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvBookingId.setText(String.format("#%s", booking.getBookingId()));
        holder.tvMuseumName.setText(booking.getMuseumName());
        holder.tvDate.setText(booking.getVisitDate());
        holder.tvStatus.setText(booking.getStatus());

        int totalTickets = booking.getTotalTickets();
        String ticketLabel = totalTickets == 1 ? "1 Ticket" : totalTickets + " Tickets";
        String summary = String.format(Locale.US, "%s (A:%d, S:%d, C:%d)",
                ticketLabel, booking.getAdultCount(), booking.getStudentCount(), booking.getChildCount());
        holder.tvTicketsSummary.setText(summary);

        holder.tvTotalAmount.setText(String.format(Locale.US, "$%.2f", booking.getTotalAmount()));
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingId, tvStatus, tvMuseumName, tvDate, tvTicketsSummary, tvTotalAmount;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tv_history_booking_id);
            tvStatus = itemView.findViewById(R.id.tv_history_status);
            tvMuseumName = itemView.findViewById(R.id.tv_history_museum_name);
            tvDate = itemView.findViewById(R.id.tv_history_date);
            tvTicketsSummary = itemView.findViewById(R.id.tv_history_tickets_summary);
            tvTotalAmount = itemView.findViewById(R.id.tv_history_total_amount);
        }
    }
}
