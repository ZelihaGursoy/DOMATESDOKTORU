package com.example.domatesteshiyeni;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private TextView totalDiagnosisCount, healthyCount, diseaseCount;
    private TextView mostCommonDisease, mostCommonCount, healthRatioText;
    private ProgressBar healthRatioBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initViews();
        loadStatistics();
        setupClickListeners();
    }

    private void initViews() {
        backBtn = findViewById(R.id.backBtn);
        totalDiagnosisCount = findViewById(R.id.totalDiagnosisCount);
        healthyCount = findViewById(R.id.healthyCount);
        diseaseCount = findViewById(R.id.diseaseCount);
        mostCommonDisease = findViewById(R.id.mostCommonDisease);
        mostCommonCount = findViewById(R.id.mostCommonCount);
        healthRatioText = findViewById(R.id.healthRatioText);
        healthRatioBar = findViewById(R.id.healthRatioBar);
    }

    private void loadStatistics() {
        SharedPreferences prefs = getSharedPreferences("diagnosis_history", MODE_PRIVATE);
        String history = prefs.getString("history", "");

        int total = 0;
        int healthy = 0;
        int disease = 0;
        Map<String, Integer> diseaseCounts = new HashMap<>();

        if (!history.isEmpty()) {
            String[] entries = history.split(";");
            for (String entry : entries) {
                if (!entry.isEmpty()) {
                    String[] parts = entry.split("\\|");
                    if (parts.length >= 2) {
                        total++;
                        String status = parts[1];

                        if (status.contains("Sağlıklı")) {
                            healthy++;
                        } else if (!status.equals("Tanımlanamadı")) {
                            disease++;
                            // Hastalık sayısını artır
                            diseaseCounts.put(status, diseaseCounts.getOrDefault(status, 0) + 1);
                        }
                    }
                }
            }
        }

        // UI güncelle
        totalDiagnosisCount.setText(String.valueOf(total));
        healthyCount.setText(String.valueOf(healthy));
        diseaseCount.setText(String.valueOf(disease));

        // En çok görülen hastalık
        if (!diseaseCounts.isEmpty()) {
            String mostCommon = "";
            int maxCount = 0;
            for (Map.Entry<String, Integer> entry : diseaseCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostCommon = entry.getKey();
                }
            }
            mostCommonDisease.setText(mostCommon);
            mostCommonCount.setText(maxCount + " kez tespit edildi");
        } else if (healthy > 0) {
            mostCommonDisease.setText("Sağlıklı");
            mostCommonCount.setText(healthy + " kez tespit edildi");
        } else {
            mostCommonDisease.setText("Henüz teşhis yok");
            mostCommonCount.setText("");
        }

        // Sağlık oranı
        if (total > 0) {
            int healthRatio = (int) ((healthy * 100.0) / total);
            healthRatioText.setText("%" + healthRatio);
            healthRatioBar.setProgress(healthRatio);
        } else {
            healthRatioText.setText("%0");
            healthRatioBar.setProgress(0);
        }
    }

    private void setupClickListeners() {
        backBtn.setOnClickListener(v -> finish());
    }
}
