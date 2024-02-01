package com.android.ilevel;

import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

public class ActivitySwitcher {
    private static final float DEPTH = 400.0f;
    private static final int DURATION = 300;

    public interface AnimationFinishedListener {
        void onAnimationFinished();
    }

    public static void animationIn(View container, WindowManager windowManager) {
        animationIn(container, windowManager, (AnimationFinishedListener) null);
    }

    public static void animationIn(View container, WindowManager windowManager, AnimationFinishedListener listener) {
        apply3DRotation(90.0f, 0.0f, false, container, windowManager, listener);
    }

    public static void animationOut(View container, WindowManager windowManager) {
        animationOut(container, windowManager, (AnimationFinishedListener) null);
    }

    public static void animationOut(View container, WindowManager windowManager, AnimationFinishedListener listener) {
        apply3DRotation(0.0f, -90.0f, true, container, windowManager, listener);
    }

    private static void apply3DRotation(float fromDegree, float toDegree, boolean reverse, View container, WindowManager windowManager, final AnimationFinishedListener listener) {
        Display display = windowManager.getDefaultDisplay();
        Rotate3dAnimation a = new Rotate3dAnimation(fromDegree, toDegree, ((float) display.getWidth()) / 2.0f, ((float) display.getHeight()) / 2.0f, DEPTH, reverse);
        a.reset();
        a.setDuration(300);
        a.setFillAfter(true);
        a.setInterpolator(new AccelerateInterpolator());
        if (listener != null) {
            a.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    listener.onAnimationFinished();
                }
            });
        }
        container.clearAnimation();
        container.startAnimation(a);
    }
}
