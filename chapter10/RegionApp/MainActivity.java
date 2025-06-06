package com.example.regionapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleText = findViewById(R.id.titleText);
        TextView descriptionText = findViewById(R.id.descriptionText);
        ImageView foodImage = findViewById(R.id.foodImage);

        // 시스템 언어 확인
        String currentLanguage = Locale.getDefault().getLanguage();

        if (currentLanguage.equals("ko")) {
            // 한국어 설정
            titleText.setText("비빔밥");
            descriptionText.setText("비빔밥은 밥 위에 여러 가지 나물과 고추장을 올려 비벼 먹는 전통 한국 요리입니다. " +
                    "건강하고 균형 잡힌 영양소를 한 그릇에 담아낸 대표적인 한식으로, " +
                    "색깔이 다양하고 맛도 훌륭합니다.");
            foodImage.setImageResource(R.drawable.bibimbap);
        } else {
            // 영어 또는 기본 설정 (햄버거)
            titleText.setText("Hamburger");
            descriptionText.setText("A hamburger is a popular sandwich consisting of a cooked ground beef patty " +
                    "placed between two halves of a sliced bun. It's often served with lettuce, tomato, " +
                    "cheese, and various condiments. This classic American dish has become a global favorite.");
            foodImage.setImageResource(R.drawable.hamburger);
        }
    }
}