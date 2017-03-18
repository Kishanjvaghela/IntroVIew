package com.introlayout.target;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by kisha_000 on 2/28/2017.
 */

public class ViewTarget implements Target {
    private final View view;
    private final String text;

    public ViewTarget(View view, String text) {
        this.view = view;
        this.text = text;
    }

    @Override
    public Point getPoint() {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0] + (view.getWidth() / 2), location[1] + (view.getHeight() / 2));
    }

    @Override
    public Rect getRect() {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Rect(
                location[0],
                location[1],
                location[0] + view.getWidth(),
                location[1] + view.getHeight()
        );
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public String getText() {
        return text;
    }
}
