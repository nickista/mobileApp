package com.example.paint;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    private Path path = new Path();
    private Paint paint = new Paint();
    private Bitmap bitmap;
    private Canvas canvas;
    private float lastX, lastY;
    private int currentColor = Color.BLACK;
    private float strokeWidth = 10f;
    private boolean isEraser = false;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(currentColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas c) {
        c.drawBitmap(bitmap, 0, 0, null);
        c.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(lastX, lastY, (x + lastX)/2, (y + lastY)/2);
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(x, y);
                canvas.drawPath(path, paint);
                path.reset();
                break;
        }
        invalidate();
        return true;
    }

    public void setColor(int color) {
        isEraser = false;
        currentColor = color;
        paint.setColor(color);
        paint.setXfermode(null); // erase 모드 해제
    }

    public void setStrokeWidth(float width) {
        strokeWidth = width;
        paint.setStrokeWidth(width);
    }

    public void enableEraser() {
        isEraser = true;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setStrokeWidth(50f); // 지우개 굵기
    }

    public void clear() {
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}
