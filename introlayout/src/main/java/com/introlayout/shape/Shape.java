package com.introlayout.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.introlayout.target.Target;
import com.introlayout.utils.Constants;

/**
 * Created by kisha_000 on 2/28/2017.
 */

public abstract class Shape {

    protected Target target;


    protected int padding;

    public Shape(Target target) {
        this(target, Constants.DEFAULT_TARGET_PADDING);
    }


    public Shape(Target target, int padding) {
        this.target = target;
        this.padding = padding;
    }

    public abstract void draw(Canvas canvas, Paint eraser, int padding);

    protected Point getFocusPoint() {
        return target.getPoint();
    }

    public abstract void reCalculateAll();

    public abstract Point getPoint();

    public abstract int getHeight();

    /**
     * Determines if a click is on the shape
     *
     * @param x x-axis location of click
     * @param y y-axis location of click
     * @return true if click is inside shape
     */
    public abstract boolean isTouchOnFocus(double x, double t);

}
