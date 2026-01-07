package com.example.domatesteshiyeni;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private SwitchCompat darkModeSwitch, notificationSwitch;
    private CardView clearHistoryCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        loadSettings();
        setupClickListeners();
    }

    private void initViews() {
        backBtn = findViewById(R.id.backBtn);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        clearHistoryCard = findViewById(R.id.clearHistoryCard);
    }

    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences("app_settings", MODE_PRIVATE);

        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        boolean notificationsEnabled = prefs.getBoolean("notifications", true);

        darkModeSwitch.setChecked(isDarkMode);
        notificationSwitch.setChecked(notificationsEnabled);
    }

    private void setupClickListeners() {
        backBtn.setOnClickListener(v -> finish());

        // Karanlık mod toggle
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences prefs = getSharedPreferences("app_settings", MODE_PRIVATE);
            prefs.edit().putBoolean("dark_mode", isChecked).apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Bildirim toggle
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences prefs = getSharedPreferences("app_settings", MODE_PRIVATE);
            prefs.edit().putBoolean("notifications", isChecked).apply();

            if (isChecked) {
                Toast.makeText(this, "Bildirimler açıldı", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bildirimler kapatıldı", Toast.LENGTH_SHORT).show();
            }
        });

        // Geçmişi temizle
        clearHistoryCard.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Geçmişi Temizle")
                    .setMessage(
                            "Tüm teşhis geçmişi ve kaydedilen fotoğraflar silinecek. Bu işlem geri alınamaz. Emin misiniz?")
                    .setPositiveButton("Evet, Sil", (dialog, which) -> {
                        // Geçmişi sil
                        SharedPreferences historyPrefs = getSharedPreferences("diagnosis_history", MODE_PRIVATE);
                        historyPrefs.edit().clear().apply();

                        // Kaydedilen fotoğrafları sil
                        java.io.File dir = new java.io.File(getFilesDir(), "diagnosis_images");
                        if (dir.exists()) {
                            java.io.File[] files = dir.listFiles();
                            if (files != null) {
                                for (java.io.File file : files) {
                                    file.delete();
                                }
                            }
                        }

                        Toast.makeText(this, "Geçmiş temizlendi", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("İptal", null)
                    .show();
        });
    }
}
