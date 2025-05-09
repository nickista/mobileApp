package com.example.paint;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingView = findViewById(R.id.drawingView);
        Button btnColor = findViewById(R.id.btnColor);
        Button btnThickness = findViewById(R.id.btnThickness);
        Button btnEraser = findViewById(R.id.btnEraser);

        // 색상 선택 팝업
        btnColor.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add("검정");
            popup.getMenu().add("빨강");
            popup.getMenu().add("파랑");
            popup.getMenu().add("초록");
            popup.getMenu().add("노랑");

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "검정":
                        drawingView.setColor(Color.BLACK);
                        break;
                    case "빨강":
                        drawingView.setColor(Color.RED);
                        break;
                    case "파랑":
                        drawingView.setColor(Color.BLUE);
                        break;
                    case "초록":
                        drawingView.setColor(Color.GREEN);
                        break;
                    case "노랑":
                        drawingView.setColor(Color.YELLOW);
                        break;
                }
                return true;
            });
            popup.show();
        });

        // 굵기 선택 팝업
        btnThickness.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add("얇게");
            popup.getMenu().add("중간");
            popup.getMenu().add("굵게");

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "얇게":
                        drawingView.setStrokeWidth(5f);
                        break;
                    case "중간":
                        drawingView.setStrokeWidth(10f);
                        break;
                    case "굵게":
                        drawingView.setStrokeWidth(20f);
                        break;
                }
                return true;
            });
            popup.show();
        });

        // 지우개 (흰색으로 색상 설정)
        btnEraser.setOnClickListener(v -> drawingView.setColor(Color.WHITE));
    }
}
