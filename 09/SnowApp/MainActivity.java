package com.example.snowapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    private SnowfallView snowfallView;
    private SeekBar snowCountSeekBar;
    private SeekBar snowSizeSeekBar;
    private TextView snowCountText;
    private TextView snowSizeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snowfallView = findViewById(R.id.snowfall_view);
        snowCountSeekBar = findViewById(R.id.snow_count_seekbar);
        snowSizeSeekBar = findViewById(R.id.snow_size_seekbar);
        snowCountText = findViewById(R.id.snow_count_text);
        snowSizeText = findViewById(R.id.snow_size_text);

        // 초기값 설정
        snowCountSeekBar.setMax(200);
        snowCountSeekBar.setProgress(50);
        snowSizeSeekBar.setMax(20);
        snowSizeSeekBar.setProgress(5);

        updateTexts();

        // 눈 개수 조절
        snowCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int snowCount = Math.max(10, progress);
                snowfallView.setSnowCount(snowCount);
                updateTexts();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 눈 크기 조절
        snowSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float snowSize = Math.max(2, progress);
                snowfallView.setSnowSize(snowSize);
                updateTexts();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateTexts() {
        int count = Math.max(10, snowCountSeekBar.getProgress());
        int size = Math.max(2, snowSizeSeekBar.getProgress());
        snowCountText.setText("눈 개수: " + count);
        snowSizeText.setText("눈 크기: " + size);
    }

    @Override
    protected void onResume() {
        super.onResume();
        snowfallView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snowfallView.pause();
    }
}