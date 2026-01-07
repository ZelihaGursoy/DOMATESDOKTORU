package com.example.domatesteshiyeni;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Locale;

public class DiseaseDetailActivity extends AppCompatActivity {

    private LinearLayout statusContainer;
    private TextView statusIcon, statusText, confidenceText;
    private TextView descriptionText, symptomsText, treatmentText, preventionText;
    private CardView symptomsCard;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);

        initViews();
        loadData();
        setupClickListeners();
    }

    private void initViews() {
        statusContainer = findViewById(R.id.statusContainer);
        statusIcon = findViewById(R.id.statusIcon);
        statusText = findViewById(R.id.statusText);
        confidenceText = findViewById(R.id.confidenceText);
        descriptionText = findViewById(R.id.descriptionText);
        symptomsText = findViewById(R.id.symptomsText);
        treatmentText = findViewById(R.id.treatmentText);
        preventionText = findViewById(R.id.preventionText);
        symptomsCard = findViewById(R.id.symptomsCard);
        backBtn = findViewById(R.id.backBtn);
    }

    private void loadData() {
        boolean isHealthy = getIntent().getBooleanExtra("isHealthy", true);
        float confidence = getIntent().getFloatExtra("confidence", 0f);
        String diseaseName = getIntent().getStringExtra("diseaseName");
        String suggestion = getIntent().getStringExtra("suggestion");

        confidenceText.setText(String.format(Locale.getDefault(), "Güven: %%%.0f", confidence));

        if (diseaseName != null && diseaseName.equals("Tanımlanamadı")) {
            showUnknownInfo(suggestion);
        } else if (isHealthy) {
            showHealthyInfo();
        } else {
            showDiseaseInfo(diseaseName, suggestion);
        }
    }

    private void showUnknownInfo(String suggestion) {
        statusContainer.setBackgroundResource(R.drawable.result_card_unknown);
        statusIcon.setText("❓");
        statusText.setText("Tanımlanamadı");

        descriptionText.setText(
                suggestion != null ? suggestion
                        : "Görsel net değil veya bu bir domates yaprağı olmayabilir. " +
                                "Model bu görüntüyü güvenilir bir şekilde sınıflandıramadı.");

        symptomsCard.setVisibility(View.GONE);

        treatmentText.setText(
                "• Net bir domates yaprağı fotoğrafı çekin\n" +
                        "• Yaprağı düz bir zemine koyun\n" +
                        "• İyi aydınlatılmış ortamda fotoğraf çekin\n" +
                        "• Yaprağın tamamını kadrajlayın");

        preventionText.setText(
                "• Sadece domates yaprağı görsellerini kullanın\n" +
                        "• Bulanık veya karanlık fotoğraflardan kaçının\n" +
                        "• Yaprak dışındaki nesneleri uzaklaştırın\n" +
                        "• Yakın çekim yapın");
    }

    private void showHealthyInfo() {
        statusContainer.setBackgroundResource(R.drawable.result_card_healthy);
        statusIcon.setText("✅");
        statusText.setText("Sağlıklı Bitki");

        descriptionText.setText(
                "Tebrikler! Domates bitkiniz gayet sağlıklı görünmektedir. " +
                        "Yapraklar canlı yeşil renkte, leke veya deformasyon bulunmamaktadır. " +
                        "Bitki normal gelişimini sürdürüyor.");

        symptomsCard.setVisibility(View.GONE);

        treatmentText.setText(
                "• Düzenli sulama rutinine devam edin\n" +
                        "• 2-3 haftada bir dengeli gübre kullanın\n" +
                        "• Yaprakları düzenli olarak kontrol edin\n" +
                        "• Yeterli güneş ışığı aldığından emin olun");

        preventionText.setText(
                "• Yaprakları ıslatmadan dipten sulama yapın\n" +
                        "• İyi havalandırma sağlayın\n" +
                        "• Alt yaprakları temizleyin\n" +
                        "• Hastalıklı bitkilerden uzak tutun\n" +
                        "• Toprak pH'ını 6.0-6.8 arasında tutun");
    }

    private void showDiseaseInfo(String diseaseName, String suggestion) {
        statusContainer.setBackgroundResource(R.drawable.result_card_disease);
        statusIcon.setText("⚠️");
        statusText.setText(diseaseName != null ? diseaseName : "Hastalık Tespit Edildi");

        descriptionText.setText(
                suggestion != null ? suggestion
                        : "Domates bitkisinde hastalık belirtileri tespit edilmiştir. " +
                                "Erken müdahale ile hastalığın yayılması önlenebilir.");

        symptomsCard.setVisibility(View.VISIBLE);
        symptomsText.setText(getSymptomsByDisease(diseaseName));

        treatmentText.setText(getTreatmentByDisease(diseaseName));

        preventionText.setText(
                "• Yaprakları ıslatmadan sulama yapın\n" +
                        "• Bitkiler arası mesafeyi artırın\n" +
                        "• Hastalıklı bitki artıklarını imha edin\n" +
                        "• Alet ve ekipmanları dezenfekte edin\n" +
                        "• Dayanıklı çeşitler tercih edin\n" +
                        "• Ekim rotasyonu uygulayın");
    }

    private String getSymptomsByDisease(String diseaseName) {
        if (diseaseName == null)
            return getDefaultSymptoms();

        switch (diseaseName) {
            case "Bakteriyel Leke":
                return "• Yapraklarda küçük, koyu kahverengi lekeler\n" +
                        "• Lekelerin etrafında sarı hale\n" +
                        "• Meyvelerde kabuksu lekeler\n" +
                        "• Islak havalarda belirtiler artar";
            case "Erken Yanıklık":
                return "• Yapraklarda iç içe halkalar (hedef tahtası görünümü)\n" +
                        "• Alt yapraklardan başlayan sararma\n" +
                        "• Gövdede koyu lekeler\n" +
                        "• Meyve sapında çürüme";
            case "Geç Yanıklık":
                return "• Yapraklarda ıslak görünümlü koyu lekeler\n" +
                        "• Yaprak altında beyaz küf tabakası\n" +
                        "• Hızla yayılan kahverengi alanlar\n" +
                        "• Gövde ve meyvelerde çürüme";
            case "Yaprak Küfü":
                return "• Yaprak üst yüzünde sarı lekeler\n" +
                        "• Yaprak altında gri-mor küf\n" +
                        "• Yaprakların kıvrılması\n" +
                        "• Nemli ortamlarda hızlı yayılma";
            case "Septoria Yaprak Lekesi":
                return "• Çok sayıda küçük, yuvarlak lekeler\n" +
                        "• Lekelerin ortasında siyah noktalar\n" +
                        "• Alt yapraklardan başlar\n" +
                        "• Şiddetli enfeksiyonda yaprak dökümü";
            case "Örümcek Akarı":
                return "• Yapraklarda bronz/bakır renk\n" +
                        "• İnce ağ yapıları\n" +
                        "• Yaprak altında küçük hareketli noktalar\n" +
                        "• Yaprak kuruması ve dökülmesi";
            case "Hedef Leke":
                return "• İç içe geçmiş kahverengi halkalar\n" +
                        "• Yapraklarda nekrotik alanlar\n" +
                        "• Alt yapraklardan başlar\n" +
                        "• Meyvelerde küçük çöküntüler";
            case "Sarı Yaprak Kıvırcıklık Virüsü":
                return "• Yapraklarda sarı renk ve kıvrılma\n" +
                        "• Bitki gelişiminde duraksama\n" +
                        "• Çiçek ve meyve azalması\n" +
                        "• Yaprakların küçülmesi";
            case "Domates Mozaik Virüsü":
                return "• Yapraklarda mozaik desenli renk değişimi\n" +
                        "• Açık ve koyu yeşil alanlar\n" +
                        "• Yaprak deformasyonu\n" +
                        "• Meyvelerde düzensiz olgunlaşma";
            default:
                return getDefaultSymptoms();
        }
    }

    private String getDefaultSymptoms() {
        return "• Yapraklarda kahverengi/sarı lekeler\n" +
                "• Yaprak kenarlarında kuruma\n" +
                "• Gövdede koyu lezyon izleri\n" +
                "• Solgunluk ve pörsüme\n" +
                "• Meyvelerde çürüme belirtileri";
    }

    private String getTreatmentByDisease(String diseaseName) {
        if (diseaseName == null)
            return getDefaultTreatment();

        switch (diseaseName) {
            case "Bakteriyel Leke":
                return "• Enfekte yaprakları uzaklaştırın\n" +
                        "• Bakır bazlı bakterisit uygulayın\n" +
                        "• Sulamayı yaprak üzerine yapmayın\n" +
                        "• Enfekte bitkileri izole edin";
            case "Erken Yanıklık":
                return "• Alt yaprakları temizleyin\n" +
                        "• Fungisit (Chlorothalonil) uygulayın\n" +
                        "• Malçlama yapın\n" +
                        "• Havalandırmayı artırın";
            case "Geç Yanıklık":
                return "• ACİL: Enfekte bitkileri hemen izole edin\n" +
                        "• Sistemik fungisit uygulayın\n" +
                        "• Çevredeki bitkileri kontrol edin\n" +
                        "• Şiddetli ise bitkileri imha edin";
            case "Yaprak Küfü":
                return "• Nemi %85 altında tutun\n" +
                        "• Havalandırmayı artırın\n" +
                        "• Fungisit uygulayın\n" +
                        "• Enfekte yaprakları çıkarın";
            case "Septoria Yaprak Lekesi":
                return "• Enfekte yaprakları uzaklaştırın\n" +
                        "• Bakır veya maneb fungisit uygulayın\n" +
                        "• Malçlama yapın\n" +
                        "• 7-10 günde tedaviyi tekrarlayın";
            case "Örümcek Akarı":
                return "• Yaprakları güçlü su jeti ile yıkayın\n" +
                        "• Neem yağı veya sabunlu su uygulayın\n" +
                        "• Akarisit kullanın\n" +
                        "• Yaprak altını kontrol edin";
            case "Hedef Leke":
                return "• Enfekte yaprakları çıkarın\n" +
                        "• Fungisit uygulayın\n" +
                        "• Yaprakların ıslanmasını önleyin\n" +
                        "• Bitkiler arası mesafeyi artırın";
            case "Sarı Yaprak Kıvırcıklık Virüsü":
                return "• Tedavisi yok - viral hastalık\n" +
                        "• Enfekte bitkileri uzaklaştırın\n" +
                        "• Beyaz sinekleri kontrol edin\n" +
                        "• Dayanıklı çeşitler kullanın";
            case "Domates Mozaik Virüsü":
                return "• Tedavisi yok - viral hastalık\n" +
                        "• Enfekte bitkileri izole edin\n" +
                        "• Ellerinizi ve aletleri dezenfekte edin\n" +
                        "• Tütün ürünlerinden uzak tutun";
            default:
                return getDefaultTreatment();
        }
    }

    private String getDefaultTreatment() {
        return "• Hasta yaprakları hemen uzaklaştırın\n" +
                "• Bakır bazlı fungisit uygulayın\n" +
                "• Sulama miktarını azaltın\n" +
                "• Bitkiyi karantinaya alın\n" +
                "• Gerekirse organik neem yağı kullanın\n" +
                "• 7-10 gün sonra tedaviyi tekrarlayın";
    }

    private void setupClickListeners() {
        backBtn.setOnClickListener(v -> finish());
    }
}
