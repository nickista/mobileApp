// MainActivity.java
package com.example.contextmenuclaude;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main_layout);

        // 레이아웃을 컨텍스트 메뉴에 등록
        registerForContextMenu(mainLayout);
    }

    // 컨텍스트 메뉴 생성
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("배경색 변경");
        menu.add(0, 1, 0, "빨간색");
        menu.add(0, 2, 0, "녹색");
        menu.add(0, 3, 0, "파란색");
        menu.add(0, 4, 0, "노란색");
        menu.add(0, 5, 0, "흰색");
    }

    // 컨텍스트 메뉴 아이템 선택 처리
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                mainLayout.setBackgroundColor(Color.RED);
                return true;
            case 2:
                mainLayout.setBackgroundColor(Color.GREEN);
                return true;
            case 3:
                mainLayout.setBackgroundColor(Color.BLUE);
                return true;
            case 4:
                mainLayout.setBackgroundColor(Color.YELLOW);
                return true;
            case 5:
                mainLayout.setBackgroundColor(Color.WHITE);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}