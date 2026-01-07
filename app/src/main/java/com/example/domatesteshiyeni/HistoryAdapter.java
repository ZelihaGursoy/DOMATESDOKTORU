package com.example.domatesteshiyeni;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryActivity.HistoryItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(HistoryActivity.HistoryItem item, int position);
    }

    public HistoryAdapter(List<HistoryActivity.HistoryItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    // Eski constructor'ı destekle (geriye uyumluluk)
    public HistoryAdapter(List<HistoryActivity.HistoryItem> items) {
        this.items = items;
        this.listener = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryActivity.HistoryItem item = items.get(position);

        holder.itemDate.setText(item.date);
        holder.itemStatus.setText(item.status);
        holder.itemConfidence.setText("%" + item.confidence);

        boolean isHealthy = item.status.contains("Sağlıklı");
        boolean isUnknown = item.status.equals("Tanımlanamadı");

        if (isUnknown) {
            holder.itemIcon.setText("❓");
            holder.itemIcon.setBackgroundResource(R.drawable.result_card_unknown);
            holder.itemConfidence.setTextColor(holder.itemView.getContext().getColor(R.color.accent));
        } else if (isHealthy) {
            holder.itemIcon.setText("✅");
            holder.itemIcon.setBackgroundResource(R.drawable.result_card_healthy);
            holder.itemConfidence.setTextColor(holder.itemView.getContext().getColor(R.color.healthy_start));
        } else {
            holder.itemIcon.setText("⚠️");
            holder.itemIcon.setBackgroundResource(R.drawable.result_card_disease);
            holder.itemConfidence.setTextColor(holder.itemView.getContext().getColor(R.color.disease_start));
        }

        // Fotoğrafı yükle
        if (item.imagePath != null && !item.imagePath.isEmpty()) {
            File imgFile = new File(item.imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.itemImage.setImageBitmap(bitmap);
            } else {
                holder.itemImage.setImageResource(R.drawable.image_placeholder);
            }
        } else {
            holder.itemImage.setImageResource(R.drawable.image_placeholder);
        }

        // Tıklama
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemIcon, itemStatus, itemDate, itemConfidence;
        ImageView itemImage;

        ViewHolder(View view) {
            super(view);
            itemIcon = view.findViewById(R.id.itemIcon);
            itemStatus = view.findViewById(R.id.itemStatus);
            itemDate = view.findViewById(R.id.itemDate);
            itemConfidence = view.findViewById(R.id.itemConfidence);
            itemImage = view.findViewById(R.id.itemImage);
        }
    }
}
