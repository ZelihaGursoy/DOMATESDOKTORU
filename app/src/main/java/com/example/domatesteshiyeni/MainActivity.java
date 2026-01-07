package com.example.domatesteshiyeni;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

// import com.example.domatesteshiyeni.ml.Domatesmodeli; // REPLACED
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import java.nio.MappedByteBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 10;
    private static final int REQUEST_CAMERA = 12;
    private static final int REQUEST_PERMISSION_GALLERY = 100;
    private static final int REQUEST_PERMISSION_CAMERA = 101;

    private Button selectBtn, predictBtn, cameraBtn, detailBtn;
    private ImageButton historyBtn;
    private TextView resultStatus, resultSuggestion, confidencePercent, imageHint;
    private ImageView imgView;
    private CardView resultCard;
    private LinearLayout resultContainer;
    private ProgressBar confidenceBar;
    private TextView resultIcon;

    private Bitmap bitmap;
    private boolean isHealthy = true;
    private float lastConfidence = 0f;
    private String lastSuggestion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = findViewById(R.id.predictBtn);
        cameraBtn = findViewById(R.id.cameraBtn);
        detailBtn = findViewById(R.id.detailBtn);
        historyBtn = findViewById(R.id.historyBtn);

        resultStatus = findViewById(R.id.resultStatus);
        resultSuggestion = findViewById(R.id.resultSuggestion);
        confidencePercent = findViewById(R.id.confidencePercent);
        imageHint = findViewById(R.id.imageHint);
        resultIcon = findViewById(R.id.resultIcon);

        imgView = findViewById(R.id.imageView);
        resultCard = findViewById(R.id.resultCard);
        resultContainer = findViewById(R.id.resultContainer);
        confidenceBar = findViewById(R.id.confidenceBar);
    }

    private void setupClickListeners() {
        // Galeri butonu
        selectBtn.setOnClickListener(v -> {
            if (checkGalleryPermission()) {
                openGallery();
            } else {
                requestGalleryPermission();
            }
        });

        // Kamera butonu
        cameraBtn.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });

        // Teşhis butonu
        predictBtn.setOnClickListener(v -> {
            if (bitmap != null) {
                performDiagnosis();
            } else {
                Toast.makeText(this, "Lütfen önce bir fotoğraf seçin veya çekin!", Toast.LENGTH_SHORT).show();
            }
        });

        // Detay butonu
        detailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DiseaseDetailActivity.class);
            intent.putExtra("isHealthy", isHealthy);
            intent.putExtra("confidence", lastConfidence);
            intent.putExtra("suggestion", lastSuggestion);
            intent.putExtra("diseaseName", detectedDiseaseName);
            startActivity(intent);
        });

        // Geçmiş butonu
        historyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    // ===== İzin Kontrolleri =====

    private boolean checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_MEDIA_IMAGES },
                    REQUEST_PERMISSION_GALLERY);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    REQUEST_PERMISSION_GALLERY);
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.CAMERA },
                REQUEST_PERMISSION_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_PERMISSION_GALLERY) {
                openGallery();
            } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
                openCamera();
            }
        } else {
            Toast.makeText(this, "İzin verilmedi. Lütfen ayarlardan izin verin.", Toast.LENGTH_LONG).show();
        }
    }

    // ===== Galeri ve Kamera =====

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_GALLERY) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgView.setImageBitmap(bitmap);
                    imageHint.setVisibility(View.GONE);
                    resultCard.setVisibility(View.GONE);
                } catch (IOException e) {
                    Toast.makeText(this, "Görsel yüklenemedi", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                imgView.setImageBitmap(bitmap);
                imageHint.setVisibility(View.GONE);
                resultCard.setVisibility(View.GONE);
            }
        }
    }

    // Hastalık sınıfları (model çıktı sırasına göre)
    // PlantVillage dataset sınıfları:
    // 0: Bacterial Spot, 1: Early Blight, 2: Late Blight, 3: Leaf Mold,
    // 4: Septoria Leaf Spot, 5: Spider Mites, 6: Target Spot,
    // 7: Tomato Yellow Leaf Curl Virus, 8: Tomato Mosaic Virus, 9: Healthy
    private static final String[] SINIF_ISIMLERI = {
            "Bakteriyel Leke",
            "Erken Yanıklık",
            "Geç Yanıklık",
            "Yaprak Küfü",
            "Septoria Yaprak Lekesi",
            "Örümcek Akarı",
            "Hedef Leke",
            "Sarı Yaprak Kıvırcıklık Virüsü",
            "Domates Mozaik Virüsü",
            "Sağlıklı"
    };

    // Minimum güven eşiği - bunun altında "tanımlanamadı" diyoruz
    private static final float MIN_CONFIDENCE_THRESHOLD = 0.40f;

    private String detectedDiseaseName = "";

    // ===== Teşhis =====

    private void performDiagnosis() {
        try {
            // 1. Modeli yükle (Assets klasöründen)
            MappedByteBuffer tfliteModel = FileUtil.loadMappedFile(this, "best_float32.tflite");
            Interpreter interpreter = new Interpreter(tfliteModel);

            // 2. Girdi verisini hazırla (224x224, Float32, Normalize 0-1)
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            int imageSize = 224;

            // ByteBuffer oluştur (1 * 224 * 224 * 3 * 4 byte)
            ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            inputBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            scaledBitmap.getPixels(intValues, 0, imageSize, 0, 0, imageSize, imageSize);

            // RGB değerlerini çıkar ve -1 ile 1 arasına normalize et
            // (pixel - 127.5) / 127.5
            for (int pixel : intValues) {
                inputBuffer.putFloat((((pixel >> 16) & 0xFF) - 127.5f) / 127.5f); // R
                inputBuffer.putFloat((((pixel >> 8) & 0xFF) - 127.5f) / 127.5f); // G
                inputBuffer.putFloat(((pixel & 0xFF) - 127.5f) / 127.5f); // B
            }
            inputBuffer.rewind(); // ÖNEMLİ: Buffer'ı başa sar

            // Debug: Tensor bilgilerini logla
            android.util.Log.d("DomatesTeşhis",
                    "Input Tensor Shape: " + java.util.Arrays.toString(interpreter.getInputTensor(0).shape()));
            android.util.Log.d("DomatesTeşhis", "Input Tensor Type: " + interpreter.getInputTensor(0).dataType());

            // 3. Çıktı buffer'ını hazırla (1x10)
            float[][] outputValues = new float[1][10];

            // 4. Tahmini çalıştır
            interpreter.run(inputBuffer, outputValues);

            float[] sonuclar = outputValues[0];

            // Model çıktısının zaten normalize olup olmadığını kontrol et
            float sum = 0;
            boolean allNormalized = true;
            for (float val : sonuclar) {
                sum += val;
                if (val < 0 || val > 1) {
                    allNormalized = false;
                }
            }

            float[] skorlar;
            // Eğer zaten normalize edilmişse (softmax çıkışıysa)
            if (allNormalized && sum > 0.9f && sum < 1.1f) {
                skorlar = sonuclar;
                android.util.Log.d("DomatesTeşhis", "Model zaten softmax çıktısı veriyor, sum=" + sum);
            } else {
                skorlar = softmax(sonuclar);
                android.util.Log.d("DomatesTeşhis", "Softmax uygulandı, raw sum=" + sum);
            }

            // En yüksek skorlu sınıfı bul
            int maxIndex = 0;
            float maxScore = skorlar[0];
            for (int i = 1; i < skorlar.length; i++) {
                if (skorlar[i] > maxScore) {
                    maxScore = skorlar[i];
                    maxIndex = i;
                }
            }

            lastConfidence = maxScore * 100;

            // Debug: Tüm sınıf skorlarını logla
            StringBuilder debugLog = new StringBuilder(
                    "Model sonuçları (maxIndex=" + maxIndex + ", maxScore=" + maxScore + "):\n");
            for (int i = 0; i < skorlar.length && i < SINIF_ISIMLERI.length; i++) {
                debugLog.append(String.format(Locale.getDefault(), "%s: %.2f%%\n",
                        SINIF_ISIMLERI[i], skorlar[i] * 100));
            }
            android.util.Log.d("DomatesTeşhis", debugLog.toString());

            // Güven eşiği kontrolü - düşükse "tanımlanamadı"
            if (maxScore < MIN_CONFIDENCE_THRESHOLD) {
                isHealthy = false;
                detectedDiseaseName = "Tanımlanamadı";
                lastSuggestion = "Görsel net değil veya bu bir domates yaprağı olmayabilir. Lütfen net bir domates yaprağı fotoğrafı çekin veya seçin.";
                lastConfidence = maxScore * 100; // Düşük güven göster
            } else {
                // Sağlıklı sınıfı genellikle son indekste (9)
                int healthyIndex = SINIF_ISIMLERI.length - 1; // "Sağlıklı" = son sınıf

                if (maxIndex == healthyIndex) {
                    isHealthy = true;
                    detectedDiseaseName = "Sağlıklı";
                    lastSuggestion = "Harika! Domates bitkisi gayet sağlıklı görünüyor. Düzenli sulama ve gübrelemeye devam edin.";
                } else {
                    isHealthy = false;
                    if (maxIndex < SINIF_ISIMLERI.length) {
                        detectedDiseaseName = SINIF_ISIMLERI[maxIndex];
                        lastSuggestion = getDiseaseSuggestion(maxIndex);
                    } else {
                        detectedDiseaseName = "Bilinmeyen Hastalık";
                        lastSuggestion = "Hastalık tespit edildi. Bir tarım uzmanına danışmanızı öneriyoruz.";
                    }
                }
            }

            // UI güncelleme
            updateResultCard();

            // Geçmişe kaydet
            saveToHistory();

            interpreter.close();
        } catch (Exception e) {
            Toast.makeText(this, "Teşhis hatası: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            android.util.Log.e("DomatesTeşhis", "Hata", e);
        }
    }

    // Softmax fonksiyonu - raw skorları olasılıklara çevirir
    private float[] softmax(float[] input) {
        float[] output = new float[input.length];
        float maxVal = Float.NEGATIVE_INFINITY;

        // Numerik stabilite için max değeri bul
        for (float val : input) {
            if (val > maxVal)
                maxVal = val;
        }

        // Exp hesapla ve topla
        float sum = 0;
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) Math.exp(input[i] - maxVal);
            sum += output[i];
        }

        // Normalize et
        for (int i = 0; i < output.length; i++) {
            output[i] /= sum;
        }

        return output;
    }

    // Hastalık önerileri
    private String getDiseaseSuggestion(int diseaseIndex) {
        switch (diseaseIndex) {
            case 0: // Bakteriyel Leke
                return "Bakteriyel leke tespit edildi. Enfekte yaprakları uzaklaştırın, bakır bazlı fungisit uygulayın ve sulamayı yaprak üzerine değil kök bölgesine yapın.";
            case 1: // Erken Yanıklık
                return "Erken yanıklık tespit edildi. Alt yaprakları temizleyin, malçlama yapın ve fungisit uygulayın. Bitkileri iyi havalandırın.";
            case 2: // Geç Yanıklık
                return "Geç yanıklık tespit edildi! Bu ciddi bir hastalıktır. Hemen enfekte bitkileri izole edin, fungisit uygulayın ve diğer bitkileri kontrol edin.";
            case 3: // Yaprak Küfü
                return "Yaprak küfü tespit edildi. Nem seviyesini düşürün, havalandırmayı artırın ve fungisit uygulayın.";
            case 4: // Septoria Yaprak Lekesi
                return "Septoria yaprak lekesi tespit edildi. Enfekte yaprakları uzaklaştırın, fungisit uygulayın ve bitkiler arası mesafeyi koruyun.";
            case 5: // Örümcek Akarı
                return "Örümcek akarı tespit edildi. Yaprakları su ile yıkayın, neem yağı veya akarisit uygulayın. Düşman böcekler kullanabilirsiniz.";
            case 6: // Hedef Leke
                return "Hedef leke tespit edildi. Enfekte yaprakları çıkarın, fungisit uygulayın ve yaprakların ıslanmasını önleyin.";
            case 7: // Sarı Yaprak Kıvırcıklık Virüsü
                return "Sarı yaprak kıvırcıklık virüsü tespit edildi. Bu viral bir hastalıktır. Enfekte bitkileri uzaklaştırın, beyaz sinekleri kontrol edin.";
            case 8: // Domates Mozaik Virüsü
                return "Domates mozaik virüsü tespit edildi. Enfekte bitkileri izole edin, ellerinizi yıkayın ve aletlerinizi dezenfekte edin.";
            default:
                return "Hastalık tespit edildi. Bir tarım uzmanına danışmanızı öneriyoruz.";
        }
    }

    private void updateResultCard() {
        resultCard.setVisibility(View.VISIBLE);

        // Animasyonlu göster
        resultCard.setAlpha(0f);
        resultCard.setTranslationY(50f);
        resultCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        if (detectedDiseaseName.equals("Tanımlanamadı")) {
            // Tanımlanamayan/düşük güvenli sonuç
            resultContainer.setBackgroundResource(R.drawable.result_card_unknown);
            resultIcon.setText("❓");
            resultStatus.setText("Tanımlanamadı");
            // lastSuggestion zaten performDiagnosis'te atandı
        } else if (isHealthy) {
            resultContainer.setBackgroundResource(R.drawable.result_card_healthy);
            resultIcon.setText("✅");
            resultStatus.setText("Sağlıklı");
            // lastSuggestion zaten performDiagnosis'te atandı
        } else {
            resultContainer.setBackgroundResource(R.drawable.result_card_disease);
            resultIcon.setText("⚠️");
            resultStatus.setText(detectedDiseaseName);
            // lastSuggestion zaten performDiagnosis'te atandı (hastalık-özel öneri)
        }

        resultSuggestion.setText(lastSuggestion);

        confidencePercent.setText(String.format(Locale.getDefault(), "%%%.0f", lastConfidence));

        // Güven çubuğu animasyonu
        ObjectAnimator animator = ObjectAnimator.ofInt(confidenceBar, "progress", 0, (int) lastConfidence);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    private void saveToHistory() {
        SharedPreferences prefs = getSharedPreferences("diagnosis_history", MODE_PRIVATE);
        String history = prefs.getString("history", "");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String date = sdf.format(new Date());

        // Fotoğrafı kaydet
        String imagePath = saveImageToInternalStorage();

        // Hastalık ismini ve fotoğraf yolunu kaydet
        String status = detectedDiseaseName;
        String confidence = String.format(Locale.getDefault(), "%.0f", lastConfidence);
        String newEntry = date + "|" + status + "|" + confidence + "|" + imagePath + ";";

        prefs.edit().putString("history", newEntry + history).apply();
    }

    private String saveImageToInternalStorage() {
        if (bitmap == null)
            return "";

        try {
            // Klasör oluştur
            File dir = new File(getFilesDir(), "diagnosis_images");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Benzersiz dosya adı
            String fileName = "diagnosis_" + System.currentTimeMillis() + ".jpg";
            File file = new File(dir, fileName);

            // Fotoğrafı kaydet
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.close();

            return file.getAbsolutePath();
        } catch (IOException e) {
            android.util.Log.e("DomatesTeşhis", "Fotoğraf kaydedilemedi", e);
            return "";
        }
    }
}