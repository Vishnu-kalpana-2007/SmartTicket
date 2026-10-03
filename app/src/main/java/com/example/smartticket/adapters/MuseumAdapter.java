package com.example.smartticket.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartticket.R;
import com.example.smartticket.models.Museum;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying list of museums in a RecyclerView.
 */
public class MuseumAdapter extends RecyclerView.Adapter<MuseumAdapter.MuseumViewHolder> {

    public interface OnMuseumClickListener {
        void onBookNowClick(Museum museum);
    }

    private final List<Museum> museumList;
    private final OnMuseumClickListener listener;

    public MuseumAdapter(List<Museum> museumList, OnMuseumClickListener listener) {
        this.museumList = museumList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MuseumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_museum, parent, false);
        return new MuseumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MuseumViewHolder holder, int position) {
        Museum museum = museumList.get(position);
        holder.tvName.setText(museum.getName());
        holder.tvCity.setText(museum.getCity());
        holder.tvDescription.setText(museum.getDescription());
        holder.tvAdultPrice.setText(String.format(Locale.US, "$%.2f", museum.getAdultPrice()));
        holder.tvStudentPrice.setText(String.format(Locale.US, "$%.2f", museum.getStudentPrice()));
        holder.tvChildPrice.setText(String.format(Locale.US, "$%.2f", museum.getChildPrice()));

        holder.btnBookNow.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookNowClick(museum);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookNowClick(museum);
            }
        });
    }

    @Override
    public int getItemCount() {
        return museumList != null ? museumList.size() : 0;
    }

    static class MuseumViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCity, tvDescription;
        TextView tvAdultPrice, tvStudentPrice, tvChildPrice;
        MaterialButton btnBookNow;

        public MuseumViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_museum_name);
            tvCity = itemView.findViewById(R.id.tv_museum_city);
            tvDescription = itemView.findViewById(R.id.tv_museum_description);
            tvAdultPrice = itemView.findViewById(R.id.tv_adult_price);
            tvStudentPrice = itemView.findViewById(R.id.tv_student_price);
            tvChildPrice = itemView.findViewById(R.id.tv_child_price);
            btnBookNow = itemView.findViewById(R.id.btn_book_now);
        }
    }
}
