package com.example.domatesteshiyeni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecycler;
    private TextView emptyText;
    private ImageButton backBtn, clearBtn;
    private List<HistoryItem> historyList = new ArrayList<>();
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initViews();
        loadHistory();
        setupClickListeners();
    }

    private void initViews() {
        historyRecycler = findViewById(R.id.historyRecycler);
        emptyText = findViewById(R.id.emptyText);
        backBtn = findViewById(R.id.backBtn);
        clearBtn = findViewById(R.id.clearBtn);

        historyRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Adapter'ı listener ile oluştur
        adapter = new HistoryAdapter(historyList, (item, position) -> {
            // Detay ekranına git
            Intent intent = new Intent(this, HistoryDetailActivity.class);
            intent.putExtra("date", item.date);
            intent.putExtra("status", item.status);
            intent.putExtra("confidence", item.confidence);
            intent.putExtra("imagePath", item.imagePath);
            intent.putExtra("position", position);
            startActivityForResult(intent, 100);
        });

        historyRecycler.setAdapter(adapter);
    }

    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences("diagnosis_history", MODE_PRIVATE);
        String history = prefs.getString("history", "");

        historyList.clear();

        if (!history.isEmpty()) {
            String[] entries = history.split(";");
            for (String entry : entries) {
                if (!entry.isEmpty()) {
                    String[] parts = entry.split("\\|");
                    if (parts.length >= 3) {
                        String date = parts[0];
                        String status = parts[1];
                        String confidence = parts[2];
                        String imagePath = parts.length > 3 ? parts[3] : "";
                        historyList.add(new HistoryItem(date, status, confidence, imagePath));
                    }
                }
            }
        }

        updateUI();
    }

    private void updateUI() {
        if (historyList.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            historyRecycler.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            historyRecycler.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    private void setupClickListeners() {
        backBtn.setOnClickListener(v -> finish());

        clearBtn.setOnClickListener(v -> {
            if (historyList.isEmpty()) {
                Toast.makeText(this, "Geçmiş zaten boş", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Geçmişi Temizle")
                    .setMessage("Tüm teşhis geçmişi silinecek. Emin misiniz?")
                    .setPositiveButton("Evet", (dialog, which) -> {
                        SharedPreferences prefs = getSharedPreferences("diagnosis_history", MODE_PRIVATE);
                        prefs.edit().clear().apply();
                        historyList.clear();
                        updateUI();
                        Toast.makeText(this, "Geçmiş temizlendi", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("İptal", null)
                    .show();
        });
    }

    // Inner class for history item
    public static class HistoryItem {
        public String date;
        public String status;
        public String confidence;
        public String imagePath;

        public HistoryItem(String date, String status, String confidence, String imagePath) {
            this.date = date;
            this.status = status;
            this.confidence = confidence;
            this.imagePath = imagePath;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Kayıt silindi, listeyi yenile
            loadHistory();
        }
    }
}
