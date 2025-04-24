// MainActivity.java
package com.example.notification10timer;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;

    private static final String CHANNEL_ID = "timer_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);

        // 알림 채널 생성 (안드로이드 8.0 이상 필수)
        createNotificationChannel();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerRunning) {
                    startTimer();
                } else {
                    cancelTimer();
                }
            }
        });
    }

    private void createNotificationChannel() {
        // 안드로이드 8.0(Oreo) 이상에서는 알림 채널을 만들어야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "타이머 알림";
            String description = "타이머 종료 알림을 위한 채널";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // 시스템에 채널 등록
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 남은 시간을 초 단위로 표시
                int seconds = (int) (millisUntilFinished / 1000);
                timerTextView.setText(seconds + "초");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("시간 종료!");
                timerRunning = false;
                startButton.setText("시작");

                // 알림 표시
                showNotification();

                // 다이얼로그 표시
                showAlertDialog();
            }
        };

        countDownTimer.start();
        timerRunning = true;
        startButton.setText("취소");
        Toast.makeText(this, "10초 타이머가 시작되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timerRunning = false;
            timerTextView.setText("10초");
            startButton.setText("시작");
            Toast.makeText(this, "타이머가 취소되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNotification() {
        // 알림 터치 시 MainActivity로 이동하는 인텐트 생성
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("타이머 종료")
                .setContentText("10초 타이머가 종료되었습니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("타이머 종료")
                .setMessage("10초가 지났습니다. 다시 10초 타이머를 시작하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 타이머 다시 시작
                        startTimer();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 아무 작업 없음
                    }
                })
                .setCancelable(false) // 백 버튼으로 다이얼로그 취소 불가능
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 리소스 해제
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}