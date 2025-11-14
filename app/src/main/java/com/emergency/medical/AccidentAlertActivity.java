package com.emergency.medical;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AccidentAlertActivity extends AppCompatActivity {

    private static final int COUNTDOWN_SECONDS = 10;

    private TextView tvCountdown;
    private ProgressBar progressRing;
    private MaterialButton btnCancelAlert;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_alert);

        initializeViews();
        setupListeners();
        startCountdown();
    }

    private void initializeViews() {
        tvCountdown = findViewById(R.id.tvCountdown);
        progressRing = findViewById(R.id.progressRing);
        btnCancelAlert = findViewById(R.id.btnCancelAlert);

        progressRing.setMax(COUNTDOWN_SECONDS);
        progressRing.setProgress(COUNTDOWN_SECONDS);
        tvCountdown.setText(String.valueOf(COUNTDOWN_SECONDS));
    }

    private void setupListeners() {
        btnCancelAlert.setOnClickListener(v -> {
            cancelCountdown();
            Toast.makeText(this,
                    R.string.alerts_cancelled_message,
                    Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void startCountdown() {
        cancelCountdown();

        countDownTimer = new CountDownTimer(COUNTDOWN_SECONDS * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) Math.ceil(millisUntilFinished / 1000.0);
                tvCountdown.setText(String.valueOf(secondsRemaining));
                progressRing.setProgress(secondsRemaining);
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("0");
                progressRing.setProgress(0);
                Toast.makeText(AccidentAlertActivity.this,
                        R.string.alerts_timeout_message,
                        Toast.LENGTH_LONG).show();
                finish();
            }
        };
        countDownTimer.start();
    }

    private void cancelCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelCountdown();
    }
}
