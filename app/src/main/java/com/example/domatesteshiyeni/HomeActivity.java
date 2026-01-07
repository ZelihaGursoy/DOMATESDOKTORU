package com.example.domatesteshiyeni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.File;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private CardView diagnoseCard, historyCard, libraryCard, statsCard;
    private CardView tipCard, lastDiagnosisCard;
    private ImageButton settingsBtn;
    private TextView tipText;
    private TextView lastDiagnosisStatus, lastDiagnosisDate, lastDiagnosisConfidence;
    private ImageView lastDiagnosisImage;

    // Günlük ipuçları
    private static final String[] TIPS = {
            "Domates yapraklarını her gün kontrol edin. Erken teşhis, hastalığın yayılmasını önler!",
            "Sabah saatlerinde sulama yapın. Yaprakların gece ıslak kalması hastalıklara davetiye çıkarır.",
            "Bitkileriniz arasında yeterli mesafe bırakın. İyi havalandırma hastalıkları önler.",
            "Alt yaprakları düzenli olarak temizleyin. Hastalıklar genellikle alt yapraklardan başlar.",
            "Domates bitkilerinizi destekleyin. Yerden yüksekte tutmak hastalık riskini azaltır.",
            "Hasta yaprakları hemen uzaklaştırın ve imha edin. Kompost yapmayın!",
            "Sulama yaparken yaprakları ıslatmamaya özen gösterin. Damla sulama idealdir.",
            "Bitkilerinize düzenli potasyum verin. Bu bitki bağışıklığını güçlendirir.",
            "Ekim rotasyonu uygulayın. Aynı yere her yıl domates ekmeyin.",
            "Sertifikalı tohumlar kullanın. Hastalıksız tohumlar sağlıklı bitkiler demektir."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupClickListeners();
        setRandomTip();
        loadLastDiagnosis();
        animateCards();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLastDiagnosis();
    }

    private void initViews() {
        diagnoseCard = findViewById(R.id.diagnoseCard);
        historyCard = findViewById(R.id.historyCard);
        libraryCard = findViewById(R.id.libraryCard);
        statsCard = findViewById(R.id.statsCard);
        tipCard = findViewById(R.id.tipCard);
        lastDiagnosisCard = findViewById(R.id.lastDiagnosisCard);
        settingsBtn = findViewById(R.id.settingsBtn);
        tipText = findViewById(R.id.tipText);
        lastDiagnosisStatus = findViewById(R.id.lastDiagnosisStatus);
        lastDiagnosisDate = findViewById(R.id.lastDiagnosisDate);
        lastDiagnosisConfidence = findViewById(R.id.lastDiagnosisConfidence);
        lastDiagnosisImage = findViewById(R.id.lastDiagnosisImage);
    }

    private void setupClickListeners() {
        // Teşhis Et
        diagnoseCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Geçmiş
        historyCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });

        // Hastalık Kütüphanesi
        libraryCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, DiseaseLibraryActivity.class);
            startActivity(intent);
        });

        // İstatistikler
        statsCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
        });

        // Ayarlar
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        // Son teşhis kartına tıklama
        lastDiagnosisCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void setRandomTip() {
        // Günün ipucunu göster (gün başına bir ipucu)
        int dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR);
        int tipIndex = dayOfYear % TIPS.length;
        tipText.setText(TIPS[tipIndex]);
    }

    private void loadLastDiagnosis() {
        SharedPreferences prefs = getSharedPreferences("diagnosis_history", MODE_PRIVATE);
        String history = prefs.getString("history", "");

        if (!history.isEmpty()) {
            String[] entries = history.split(";");
            if (entries.length > 0 && !entries[0].isEmpty()) {
                String[] parts = entries[0].split("\\|");
                if (parts.length >= 3) {
                    String date = parts[0];
                    String status = parts[1];
                    String confidence = parts[2];
                    String imagePath = parts.length > 3 ? parts[3] : null;

                    lastDiagnosisCard.setVisibility(View.VISIBLE);
                    lastDiagnosisDate.setText(date);
                    lastDiagnosisStatus.setText(status);
                    lastDiagnosisConfidence.setText("%" + confidence);

                    // Renk ayarla
                    boolean isHealthy = status.contains("Sağlıklı");
                    lastDiagnosisConfidence.setTextColor(
                            getColor(isHealthy ? R.color.healthy_start : R.color.disease_start));

                    // Fotoğrafı yükle
                    if (imagePath != null && !imagePath.isEmpty()) {
                        File imgFile = new File(imagePath);
                        if (imgFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            lastDiagnosisImage.setImageBitmap(bitmap);
                        }
                    }
                }
            }
        } else {
            lastDiagnosisCard.setVisibility(View.GONE);
        }
    }

    private void animateCards() {
        // Kartları sırayla animate et
        CardView[] cards = { tipCard, diagnoseCard, historyCard, libraryCard, statsCard };

        for (int i = 0; i < cards.length; i++) {
            CardView card = cards[i];
            card.setAlpha(0f);
            card.setTranslationY(50f);
            card.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(400)
                    .setStartDelay(i * 100L)
                    .start();
        }
    }
}
