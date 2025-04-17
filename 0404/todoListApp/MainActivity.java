package com.example.todolist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonAdd;
    private LinearLayout taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.Button1);
        taskList = findViewById(R.id.taskList);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editTextTask.getText().toString();
                if (!task.isEmpty()) {
                    addTask(task);
                    editTextTask.setText(""); // 입력 필드 비우기
                }
            }
        });
    }

    private void addTask(String task) {
        // 체크박스 생성
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(task);

        // 체크박스 클릭 리스너 설정
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 완료 상태일 때
                checkBox.setText("완료: " + task);
            } else {
                // 미완료 상태일 때
                checkBox.setText(task);
            }
        });

        // 할 일을 리스트에 추가
        taskList.addView(checkBox);
    }
}