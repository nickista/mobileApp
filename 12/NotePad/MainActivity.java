package com.example.notepad;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText noteEditText;
    private Button saveButton;
    private Button loadButton;
    private static final String FILE_NAME = "note.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI 요소 초기화
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);
        loadButton = findViewById(R.id.loadButton);

        // EditText 텍스트 색상 설정
        noteEditText.setTextColor(Color.BLACK);
        noteEditText.setHintTextColor(Color.GRAY);

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(v -> saveNote());

        // 불러오기 버튼 클릭 리스너
        loadButton.setOnClickListener(v -> loadNote());

        // 앱 시작시 자동으로 메모 불러오기
        loadNote();

        // EditText가 사용자에게 보이기 전에 측정이 완료된 후 실행되는 리스너
        noteEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 레이아웃이 그려진 후 EditText에 포커스 주기
                noteEditText.requestFocus();

                // 커서 위치를 텍스트 시작 부분으로 설정 (있다면)
                if (noteEditText.getText().length() > 0) {
                    noteEditText.setSelection(0);
                }

                // 한 번만 실행하도록 리스너 제거
                noteEditText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 화면이 활성화될 때마다 EditText에 포커스 설정
        noteEditText.post(() -> {
            noteEditText.requestFocus();
            if (noteEditText.getText().length() > 0) {
                noteEditText.setSelection(0);
            }
        });
    }

    // 메모 저장 메소드
    private void saveNote() {
        String text = noteEditText.getText().toString();

        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(text.getBytes());
            Toast.makeText(this, "메모가 저장되었습니다", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "저장 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
        }
    }

    // 메모 불러오기 메소드
    private void loadNote() {
        try (FileInputStream fis = openFileInput(FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            noteEditText.setText(sb.toString());

            // 텍스트를 로드한 후 커서를 텍스트의 맨 앞으로 이동
            noteEditText.setSelection(0);

        } catch (IOException e) {
            // 파일이 없는 경우 등의 예외 처리
            e.printStackTrace();
        }
    }
}