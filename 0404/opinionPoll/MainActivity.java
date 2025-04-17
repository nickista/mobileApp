package com.example.opinionpoll;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private RadioButton radio1, radio2, radio3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        Button button = findViewById(R.id.Button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio1.isChecked()) {
                    imageView.setImageResource(R.drawable.image0);
                } else if (radio2.isChecked()) {
                    imageView.setImageResource(R.drawable.image1);
                } else if (radio3.isChecked()) {
                    imageView.setImageResource(R.drawable.image2);
                }
            }
        });
    }
}