package com.example.domatesteshiyeni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout indicatorLayout;
    private Button nextBtn;
    private TextView skipBtn;

    private static final OnboardingItem[] PAGES = {
            new OnboardingItem("🍅", "Hoş Geldiniz!",
                    "Domates Teşhis uygulamasına hoş geldiniz! Yapay zeka destekli hastalık teşhisi ile bitkilerinizi koruyun."),
            new OnboardingItem("📸", "Fotoğraf Çekin",
                    "Domates yaprağının fotoğrafını çekin veya galeriden seçin. Net ve aydınlık fotoğraflar en iyi sonuçları verir."),
            new OnboardingItem("🔬", "Teşhis Alın",
                    "Yapay zeka modeli yaprağı analiz eder ve olası hastalıkları tespit eder. Tedavi önerileri de alabilirsiniz."),
            new OnboardingItem("🌱", "Başlayalım!",
                    "Bitkilerinizi sağlıklı tutmak için hemen teşhis yapmaya başlayın. Düzenli kontrol hastalıkları önler!")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        initViews();
        setupViewPager();
        setupClickListeners();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        indicatorLayout = findViewById(R.id.indicatorLayout);
        nextBtn = findViewById(R.id.nextBtn);
        skipBtn = findViewById(R.id.skipBtn);
    }

    private void setupViewPager() {
        OnboardingAdapter adapter = new OnboardingAdapter(PAGES);
        viewPager.setAdapter(adapter);

        // İndikatörleri oluştur
        for (int i = 0; i < PAGES.length; i++) {
            View indicator = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 8);
            params.setMargins(4, 0, 4, 0);
            indicator.setLayoutParams(params);
            indicator.setBackgroundResource(R.drawable.indicator_inactive);
            indicatorLayout.addView(indicator);
        }
        updateIndicators(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicators(position);

                // Son sayfada buton metnini değiştir
                if (position == PAGES.length - 1) {
                    nextBtn.setText("Başla");
                    skipBtn.setVisibility(View.GONE);
                } else {
                    nextBtn.setText("Devam");
                    skipBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicatorLayout.getChildCount(); i++) {
            View indicator = indicatorLayout.getChildAt(i);
            if (i == position) {
                indicator.setBackgroundResource(R.drawable.indicator_active);
                indicator.getLayoutParams().width = 32;
            } else {
                indicator.setBackgroundResource(R.drawable.indicator_inactive);
                indicator.getLayoutParams().width = 24;
            }
            indicator.requestLayout();
        }
    }

    private void setupClickListeners() {
        nextBtn.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < PAGES.length - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                finishOnboarding();
            }
        });

        skipBtn.setOnClickListener(v -> finishOnboarding());
    }

    private void finishOnboarding() {
        // Onboarding tamamlandı olarak işaretle
        SharedPreferences prefs = getSharedPreferences("app_settings", MODE_PRIVATE);
        prefs.edit().putBoolean("onboarding_completed", true).apply();

        // Ana menüye git
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    // Onboarding item veri sınıfı
    private static class OnboardingItem {
        String icon, title, description;

        OnboardingItem(String icon, String title, String description) {
            this.icon = icon;
            this.title = title;
            this.description = description;
        }
    }

    // Adapter
    private static class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {
        private OnboardingItem[] items;

        OnboardingAdapter(OnboardingItem[] items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_onboarding, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            OnboardingItem item = items[position];
            holder.icon.setText(item.icon);
            holder.title.setText(item.title);
            holder.description.setText(item.description);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView icon, title, description;

            ViewHolder(View view) {
                super(view);
                icon = view.findViewById(R.id.onboardingIcon);
                title = view.findViewById(R.id.onboardingTitle);
                description = view.findViewById(R.id.onboardingDesc);
            }
        }
    }
}
