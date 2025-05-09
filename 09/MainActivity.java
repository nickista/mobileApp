package com.example.paint;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.paint.DrawingView;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = findViewById(R.id.drawingView);

        // 색상 변경
        findViewById(R.id.btnBlack).setOnClickListener(v -> drawingView.setColor(Color.BLACK));
        findViewById(R.id.btnRed).setOnClickListener(v -> drawingView.setColor(Color.RED));
        findViewById(R.id.btnBlue).setOnClickListener(v -> drawingView.setColor(Color.BLUE));
        findViewById(R.id.btnGreen).setOnClickListener(v -> drawingView.setColor(Color.GREEN));
        findViewById(R.id.btnYellow).setOnClickListener(v -> drawingView.setColor(Color.YELLOW));

        // 굵기 조절
        findViewById(R.id.btnThin).setOnClickListener(v -> drawingView.setStrokeWidth(5f));
        findViewById(R.id.btnMedium).setOnClickListener(v -> drawingView.setStrokeWidth(10f));
        findViewById(R.id.btnThick).setOnClickListener(v -> drawingView.setStrokeWidth(20f));

        // 지우개
        findViewById(R.id.btnEraser).setOnClickListener(v -> drawingView.enableEraser());
    }
}
