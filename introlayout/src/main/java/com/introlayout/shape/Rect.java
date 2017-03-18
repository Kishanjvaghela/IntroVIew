package com.introlayout.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.introlayout.target.Target;

/**
 * Created by kisha_000 on 2/28/2017.
 */

public class Rect extends Shape {
    RectF adjustedRect;

    public Rect(Target target) {
        super(target);
        calculateAdjustedRect();
    }


    public Rect(Target target, int padding) {
        super(target, padding);
        calculateAdjustedRect();
    }

    @Override
    public void draw(Canvas canvas, Paint eraser, int padding) {
        canvas.drawRoundRect(adjustedRect, padding, padding, eraser);
    }

    private void calculateAdjustedRect() {
        RectF rect = new RectF();
        rect.set(target.getRect());

        rect.left -= padding;
        rect.top -= padding;
        rect.right += padding;
        rect.bottom += padding;

        adjustedRect = rect;
    }

    @Override
    public void reCalculateAll() {
        calculateAdjustedRect();
    }

    @Override
    public Point getPoint() {
        return target.getPoint();
    }

    @Override
    public int getHeight() {
        return (int) adjustedRect.height();
    }

    @Override
    public int getWidth() {
        return (int) adjustedRect.width();
    }

    @Override
    public boolean isTouchOnFocus(double x, double y) {
        return adjustedRect.contains((float) x, (float) y);
    }

}
