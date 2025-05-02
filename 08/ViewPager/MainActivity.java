package com.example.viewproj; // ← 패키지 이름은 프로젝트에 맞게 바꿔주세요

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    int[] images = {
            R.drawable.image1, // 나중에 drawable 폴더에 추가하세요
            R.drawable.image2,
            R.drawable.image3
    };

    String[] texts = {
            "첫 번째 페이지",
            "두 번째 페이지",
            "세 번째 페이지"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewPager);
        com.example.viewproj.MyPagerAdapter adapter = new com.example.viewproj.MyPagerAdapter(this, images, texts);
        viewPager.setAdapter(adapter);
    }
}
