package com.example.domatesteshiyeni;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class HistoryDetailActivity extends AppCompatActivity {

    private ImageButton backBtn, shareBtn, deleteBtn;
    private ImageView diagnosisImage;
    private LinearLayout resultContainer;
    private TextView resultIcon, resultStatus, resultDate, resultConfidence;
    private Button viewDetailBtn;

    private String date, status, confidence, imagePath;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        initViews();
        loadData();
        setupClickListeners();
    }

    private void initViews() {
        backBtn = findViewById(R.id.backBtn);
        shareBtn = findViewById(R.id.shareBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        diagnosisImage = findViewById(R.id.diagnosisImage);
        resultContainer = findViewById(R.id.resultContainer);
        resultIcon = findViewById(R.id.resultIcon);
        resultStatus = findViewById(R.id.resultStatus);
        resultDate = findViewById(R.id.resultDate);
        resultConfidence = findViewById(R.id.resultConfidence);
        viewDetailBtn = findViewById(R.id.viewDetailBtn);
    }

    private void loadData() {
        date = getIntent().getStringExtra("date");
        status = getIntent().getStringExtra("status");
        confidence = getIntent().getStringExtra("confidence");
        imagePath = getIntent().getStringExtra("imagePath");
        position = getIntent().getIntExtra("position", -1);

        // UI güncelle
        resultDate.setText(date);
        resultStatus.setText(status);
        resultConfidence.setText("%" + confidence);

        // Fotoğrafı yükle
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                diagnosisImage.setImageBitmap(bitmap);
            }
        }

        // Sonuç rengini ayarla
        boolean isHealthy = status != null && status.contains("Sağlıklı");
        boolean isUnknown = status != null && status.equals("Tanımlanamadı");

        if (isUnknown) {
            resultContainer.setBackgroundResource(R.drawable.result_card_unknown);
            resultIcon.setText("❓");
        } else if (isHealthy) {
            resultContainer.setBackgroundResource(R.drawable.result_card_healthy);
            resultIcon.setText("✅");
        } else {
            resultContainer.setBackgroundResource(R.drawable.result_card_disease);
            resultIcon.setText("⚠️");
        }
    }

    private void setupClickListeners() {
        backBtn.setOnClickListener(v -> finish());

        // Hastalık detaylarına git
        viewDetailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DiseaseDetailActivity.class);
            intent.putExtra("isHealthy", status != null && status.contains("Sağlıklı"));
            intent.putExtra("diseaseName", status);
            intent.putExtra("confidence", Float.parseFloat(confidence));
            startActivity(intent);
        });

        // Paylaş
        shareBtn.setOnClickListener(v -> shareResult());

        // Sil
        deleteBtn.setOnClickListener(v -> confirmDelete());
    }

    private void shareResult() {
        String shareText = "🍅 Domates Teşhis Sonucu\n\n" +
                "📅 Tarih: " + date + "\n" +
                "🔬 Sonuç: " + status + "\n" +
                "📊 Güven: %" + confidence + "\n\n" +
                "Domates Teşhis uygulaması ile analiz edilmiştir.";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        // Fotoğraf varsa ekle
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                try {
                    Uri imageUri = FileProvider.getUriForFile(this,
                            getPackageName() + ".provider", imgFile);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    // Fotoğraf paylaşılamadı, sadece metin paylaş
                }
            }
        }

        startActivity(Intent.createChooser(shareIntent, "Paylaş"));
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Kaydı Sil")
                .setMessage("Bu teşhis kaydı silinecek. Emin misiniz?")
                .setPositiveButton("Sil", (dialog, which) -> {
                    deleteRecord();
                })
                .setNegativeButton("İptal", null)
                .show();
    }

    private void deleteRecord() {
        // Fotoğrafı sil
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                imgFile.delete();
            }
        }

        // Geçmişten sil (pozisyona göre)
        android.content.SharedPreferences prefs = getSharedPreferences("diagnosis_history", MODE_PRIVATE);
        String history = prefs.getString("history", "");

        if (!history.isEmpty() && position >= 0) {
            String[] entries = history.split(";");
            StringBuilder newHistory = new StringBuilder();

            for (int i = 0; i < entries.length; i++) {
                if (i != position && !entries[i].isEmpty()) {
                    newHistory.append(entries[i]).append(";");
                }
            }

            prefs.edit().putString("history", newHistory.toString()).apply();
        }

        Toast.makeText(this, "Kayıt silindi", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK); // Listeyi yenilemesi için
        finish();
    }
}
