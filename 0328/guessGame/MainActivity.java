package com.example.guessgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText et;
    TextView tv1;
    int rn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.editTextText);
        tv1 = findViewById(R.id.textView1);

        generateRandomNumber();  // 랜덤 숫자 생성
    }

    private void generateRandomNumber() {
        rn = new Random().nextInt(100) + 1;
    }

    public void playrandomnumber(View v) {
        String str = et.getText().toString();

        if (str.isEmpty()) {
            tv1.setText("숫자를 입력하세요.");
            return;
        }

        int user;
        try {
            user = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            tv1.setText("유효한 숫자를 입력하세요.");
            return;
        }

        if (user < 1 || user > 100) {
            tv1.setText("1부터 100까지의 숫자를 입력하세요.");
            return;
        }

        if (user > rn) {
            tv1.setText("숫자가 큽니다.");
        } else if (user < rn) {
            tv1.setText("숫자가 작습니다.");
        } else {
            tv1.setText("🎉 축하합니다. 숫자를 맞췄습니다!");
            generateRandomNumber(); // 다음 게임을 위해 새로운 숫자 생성
        }

        et.setText("");
    }
}
