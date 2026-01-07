package com.example.domatesteshiyeni;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DiseaseLibraryActivity extends AppCompatActivity {

    private RecyclerView diseaseRecycler;
    private ImageButton backBtn;
    private List<DiseaseInfo> diseaseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_library);

        initViews();
        loadDiseases();
        setupClickListeners();
    }

    private void initViews() {
        diseaseRecycler = findViewById(R.id.diseaseRecycler);
        backBtn = findViewById(R.id.backBtn);

        diseaseRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadDiseases() {
        // Hastalık listesini oluştur
        diseaseList.add(new DiseaseInfo("Sağlıklı", "✅",
                "Sağlıklı domates yaprağı",
                "Canlı yeşil renk, leke veya deformasyon yok."));

        diseaseList.add(new DiseaseInfo("Bakteriyel Leke", "🦠",
                "Xanthomonas campestris bakterisi kaynaklı",
                "Yapraklarda küçük, koyu kahverengi lekeler ve sarı haleler görülür."));

        diseaseList.add(new DiseaseInfo("Erken Yanıklık", "🔥",
                "Alternaria solani mantarı kaynaklı",
                "Yapraklarda hedef tahtası görünümünde iç içe halkalar oluşur."));

        diseaseList.add(new DiseaseInfo("Geç Yanıklık", "⚡",
                "Phytophthora infestans kaynaklı - çok tehlikeli!",
                "Yapraklarda ıslak koyu lekeler, beyaz küf tabakası. Hızla yayılır."));

        diseaseList.add(new DiseaseInfo("Yaprak Küfü", "🍄",
                "Passalora fulva mantarı kaynaklı",
                "Yaprak üstünde sarı lekeler, altında gri-mor küf oluşumu."));

        diseaseList.add(new DiseaseInfo("Septoria Yaprak Lekesi", "⚫",
                "Septoria lycopersici mantarı kaynaklı",
                "Çok sayıda küçük, yuvarlak lekeler ve ortasında siyah noktalar."));

        diseaseList.add(new DiseaseInfo("Örümcek Akarı", "🕷️",
                "Tetranychus urticae zararlısı",
                "Yapraklarda bronz renk, ince ağ yapıları ve küçük hareketli zararlılar."));

        diseaseList.add(new DiseaseInfo("Hedef Leke", "🎯",
                "Corynespora cassiicola mantarı kaynaklı",
                "İç içe geçmiş kahverengi halkalar ve nekrotik alanlar."));

        diseaseList.add(new DiseaseInfo("Sarı Yaprak Kıvırcıklık Virüsü", "💛",
                "TYLCV - Beyaz sinekle bulaşır",
                "Yapraklarda sarı renk, kıvrılma ve bitki gelişiminde duraksama."));

        diseaseList.add(new DiseaseInfo("Domates Mozaik Virüsü", "🌈",
                "TMV - Mekanik temasla bulaşır",
                "Yapraklarda mozaik desenli renk değişimi ve deformasyon."));

        DiseaseAdapter adapter = new DiseaseAdapter(diseaseList, disease -> {
            // Hastalık detayına git
            Intent intent = new Intent(this, DiseaseDetailActivity.class);
            intent.putExtra("isHealthy", disease.name.equals("Sağlıklı"));
            intent.putExtra("diseaseName", disease.name);
            intent.putExtra("suggestion", disease.description);
            intent.putExtra("confidence", 100f);
            intent.putExtra("fromLibrary", true);
            startActivity(intent);
        });

        diseaseRecycler.setAdapter(adapter);
    }

    private void setupClickListeners() {
        backBtn.setOnClickListener(v -> finish());
    }

    // Hastalık bilgi sınıfı
    public static class DiseaseInfo {
        public String name;
        public String icon;
        public String shortDesc;
        public String description;

        public DiseaseInfo(String name, String icon, String shortDesc, String description) {
            this.name = name;
            this.icon = icon;
            this.shortDesc = shortDesc;
            this.description = description;
        }
    }

    // Adapter
    private static class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.ViewHolder> {
        private List<DiseaseInfo> items;
        private OnDiseaseClickListener listener;

        interface OnDiseaseClickListener {
            void onDiseaseClick(DiseaseInfo disease);
        }

        DiseaseAdapter(List<DiseaseInfo> items, OnDiseaseClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_disease, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DiseaseInfo item = items.get(position);
            holder.diseaseName.setText(item.name);
            holder.diseaseDesc.setText(item.shortDesc);
            holder.diseaseIcon.setText(item.icon);

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDiseaseClick(item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView diseaseName, diseaseDesc, diseaseIcon;

            ViewHolder(View view) {
                super(view);
                diseaseName = view.findViewById(R.id.diseaseName);
                diseaseDesc = view.findViewById(R.id.diseaseDesc);
                diseaseIcon = view.findViewById(R.id.diseaseIcon);
            }
        }
    }
}
