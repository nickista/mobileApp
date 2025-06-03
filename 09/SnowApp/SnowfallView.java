package com.example.snowapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnowfallView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread animationThread;
    private boolean isAnimating;
    private SurfaceHolder surfaceHolder;
    private Paint snowPaint;
    private List<Snowflake> snowflakes;
    private Random random;
    private int snowCount = 50;
    private float snowSize = 5f;
    private Bitmap backgroundImage;

    public SnowfallView(Context context) {
        super(context);
        init();
    }

    public SnowfallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        snowPaint = new Paint();
        snowPaint.setColor(Color.WHITE);
        snowPaint.setAntiAlias(true);

        snowflakes = new ArrayList<>();
        random = new Random();

        // 배경 이미지 로드 (예시로 안드로이드 로봇 이미지 사용)
        // 실제로는 drawable 폴더에 있는 이미지를 사용하세요
        try {
            backgroundImage = BitmapFactory.decodeResource(getResources(), android.R.drawable.sym_def_app_icon);
        } catch (Exception e) {
            // 기본 이미지가 없으면 null로 설정
            backgroundImage = null;
        }
    }

    public void setSnowCount(int count) {
        this.snowCount = count;
        initializeSnowflakes();
    }

    public void setSnowSize(float size) {
        this.snowSize = size;
        for (Snowflake flake : snowflakes) {
            flake.radius = random.nextFloat() * size + 2;
        }
    }

    private void initializeSnowflakes() {
        snowflakes.clear();
        for (int i = 0; i < snowCount; i++) {
            snowflakes.add(createSnowflake());
        }
    }

    private Snowflake createSnowflake() {
        Snowflake flake = new Snowflake();
        flake.x = random.nextFloat() * getWidth();
        flake.y = random.nextFloat() * getHeight() - getHeight();
        flake.radius = random.nextFloat() * snowSize + 2;
        flake.speed = random.nextFloat() * 3 + 1;
        flake.drift = (random.nextFloat() - 0.5f) * 2;
        return flake;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initializeSnowflakes();
        resume();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 화면 크기가 변경되면 눈송이들을 다시 초기화
        initializeSnowflakes();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pause();
    }

    public void resume() {
        if (!isAnimating) {
            isAnimating = true;
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    public void pause() {
        isAnimating = false;
        if (animationThread != null) {
            try {
                animationThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (isAnimating) {
            if (surfaceHolder.getSurface().isValid()) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    // 배경 그리기
                    canvas.drawColor(Color.BLACK);

                    // 배경 이미지 그리기 (있는 경우)
                    if (backgroundImage != null) {
                        // 이미지를 화면 중앙에 맞춰 그리기
                        float scale = Math.min(
                                (float) getWidth() / backgroundImage.getWidth(),
                                (float) getHeight() / backgroundImage.getHeight()
                        ) * 0.8f; // 80% 크기로 조정

                        int scaledWidth = (int) (backgroundImage.getWidth() * scale);
                        int scaledHeight = (int) (backgroundImage.getHeight() * scale);
                        int x = (getWidth() - scaledWidth) / 2;
                        int y = (getHeight() - scaledHeight) / 2;

                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(backgroundImage, scaledWidth, scaledHeight, true);
                        canvas.drawBitmap(scaledBitmap, x, y, null);
                    }

                    // 눈송이 업데이트 및 그리기
                    updateAndDrawSnowflakes(canvas);

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            try {
                Thread.sleep(16); // 약 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateAndDrawSnowflakes(Canvas canvas) {
        for (Snowflake flake : snowflakes) {
            // 눈송이 위치 업데이트
            flake.y += flake.speed;
            flake.x += flake.drift;

            // 화면을 벗어나면 다시 위로
            if (flake.y > getHeight()) {
                flake.y = -flake.radius;
                flake.x = random.nextFloat() * getWidth();
            }

            // 좌우로 벗어나면 반대편으로
            if (flake.x < -flake.radius) {
                flake.x = getWidth() + flake.radius;
            } else if (flake.x > getWidth() + flake.radius) {
                flake.x = -flake.radius;
            }

            // 눈송이 그리기
            canvas.drawCircle(flake.x, flake.y, flake.radius, snowPaint);
        }
    }

    private static class Snowflake {
        float x, y;
        float radius;
        float speed;
        float drift;
    }
}
