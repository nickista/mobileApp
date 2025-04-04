package com.example.calculatorai;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText num1, num2;
    Button btnAdd, btnSubtract, btnMultiply, btnDivide;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        btnAdd = findViewById(R.id.btn_add);
        btnSubtract = findViewById(R.id.btn_subtract);
        btnMultiply = findViewById(R.id.btn_multiply);
        btnDivide = findViewById(R.id.btn_divide);
        result = findViewById(R.id.result);

        btnAdd.setOnClickListener(v -> calculate('+'));
        btnSubtract.setOnClickListener(v -> calculate('-'));
        btnMultiply.setOnClickListener(v -> calculate('*'));
        btnDivide.setOnClickListener(v -> calculate('/'));
    }

    private void calculate(char operator) {
        String strNum1 = num1.getText().toString();
        String strNum2 = num2.getText().toString();

        if (strNum1.isEmpty() || strNum2.isEmpty()) {
            result.setText("숫자를 입력하세요!");
            return;
        }

        double number1 = Double.parseDouble(strNum1);
        double number2 = Double.parseDouble(strNum2);
        double output = 0;

        switch (operator) {
            case '+': output = number1 + number2; break;
            case '-': output = number1 - number2; break;
            case '*': output = number1 * number2; break;
            case '/':
                if (number2 == 0) {
                    result.setText("0으로 나눌 수 없습니다!");
                    return;
                }
                output = number1 / number2;
                break;
        }

        result.setText("결과: " + output);
    }
}
