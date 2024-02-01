package com.android.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class ScalingLinearLayout extends LinearLayout {
    boolean alreadyScaled = false;
    int baseHeight;
    int baseWidth;
    int expectedHeight;
    int expectedWidth;
    float scale;
    int scaledHeight;
    int scaledWidth;

    public ScalingLinearLayout(Context context) {
        super(context);
    }

    public ScalingLinearLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public void onFinishInflate() {
        Log.d("notcloud.view", "ScalingLinearLayout::onFinishInflate: 1 width=" + getWidth() + ", height=" + getHeight());
        measure(1000, 1000);
        this.baseWidth = getMeasuredWidth();
        this.baseHeight = getMeasuredHeight();
        if (this.alreadyScaled) {
            Scale.scaleViewAndChildren(this, this.scale, 0);
        }
    }

    public void draw(Canvas canvas) {
        float xScale;
        float yScale;
        int width = getWidth();
        int height = getHeight();
        if (this.alreadyScaled && width == this.expectedWidth && height == this.expectedHeight) {
            super.draw(canvas);
            return;
        }
        if (!this.alreadyScaled || width == this.expectedWidth) {
            xScale = ((float) width) / ((float) this.baseWidth);
            this.scaledWidth = this.baseWidth;
        } else {
            xScale = ((float) width) / ((float) this.scaledWidth);
        }
        if (!this.alreadyScaled || height == this.expectedHeight) {
            yScale = ((float) height) / ((float) this.baseHeight);
            this.scaledHeight = this.baseHeight;
        } else {
            yScale = ((float) height) / ((float) this.scaledHeight);
        }
        this.scale = Math.min(xScale, yScale);
        Log.d("notcloud.view", "xScale=" + xScale + ", width=" + width + " baseWidth=" + this.baseWidth + " scaledWidth=" + this.scaledWidth);
        Log.d("notcloud.view", "yScale=" + yScale + ", height=" + height + " baseHeight=" + this.baseHeight + " scaledHeight=" + this.scaledHeight);
        Log.d("notcloud.view", "ScalingLinearLayout::onLayout: Scaling! Scale=" + this.scale + " AlreadyScaled=" + this.alreadyScaled);
        Scale.scaleViewAndChildren(this, this.scale, 0);
        this.alreadyScaled = true;
        this.expectedWidth = width;
        this.expectedHeight = height;
        this.scaledHeight = (int) (((float) this.scaledHeight) * this.scale);
        this.scaledWidth = (int) (((float) this.scaledWidth) * this.scale);
    }
}
