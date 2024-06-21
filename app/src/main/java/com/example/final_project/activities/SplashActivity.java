package com.example.final_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.final_project.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 3000; // Время отображения SplashActivity в миллисекундах

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        // Инициализация LottieAnimationView
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie_animation_view);
        lottieAnimationView.setAnimation(R.raw.splash); // Загрузка анимации из raw
        lottieAnimationView.playAnimation(); // Запуск анимации

        // Переход к MainActivity после задержки
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}
