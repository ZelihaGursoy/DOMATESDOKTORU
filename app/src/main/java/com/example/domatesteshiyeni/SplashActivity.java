package com.example.domatesteshiyeni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Karanlık mod ayarını uygula
        SharedPreferences settingsPrefs = getSharedPreferences("app_settings", MODE_PRIVATE);
        boolean isDarkMode = settingsPrefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_splash);

        // Animasyonları başlat
        startAnimations();

        // Sonraki ekrana geçiş
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("app_settings", MODE_PRIVATE);
            boolean onboardingCompleted = prefs.getBoolean("onboarding_completed", false);

            Intent intent;
            if (onboardingCompleted) {
                // Onboarding tamamlandı, ana menüye git
                intent = new Intent(SplashActivity.this, HomeActivity.class);
            } else {
                // İlk açılış, onboarding'e git
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            }

            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DURATION);
    }

    private void startAnimations() {
        ImageView logo = findViewById(R.id.splashLogo);
        TextView title = findViewById(R.id.splashTitle);
        TextView subtitle = findViewById(R.id.splashSubtitle);

        // Logo animasyonu - Scale ve Fade
        logo.setScaleX(0.5f);
        logo.setScaleY(0.5f);
        logo.setAlpha(0f);
        logo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(800)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        // Başlık animasyonu
        title.setTranslationY(50f);
        title.setAlpha(0f);
        title.animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(400)
                .setDuration(600)
                .start();

        // Alt başlık animasyonu
        subtitle.setTranslationY(30f);
        subtitle.setAlpha(0f);
        subtitle.animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(600)
                .setDuration(600)
                .start();
    }
}
