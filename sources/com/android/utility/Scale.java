package com.android.utility;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class Scale {
    public static void scaleContents(View rootView, View container) {
        scaleContents(rootView, container, rootView.getWidth(), rootView.getHeight());
    }

    public static void scaleContents(View rootView, View container, int width, int height) {
        Log.d("notcloud.scale", "Scale::scaleContents: container: " + container.getWidth() + "x" + container.getHeight() + ".");
        float scale = Math.min(((float) container.getWidth()) / ((float) width), ((float) container.getHeight()) / ((float) height));
        Log.d("notcloud.scale", "Scale::scaleContents: scale=" + scale + ", width=" + width + ", height=" + height + ".");
        scaleViewAndChildren(rootView, scale, 0);
    }

    public static void scaleViewAndChildren(View root, float scale, int canary) {
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        if (!(layoutParams.width == -1 || layoutParams.width == -2)) {
            layoutParams.width = (int) (((float) layoutParams.width) * scale);
        }
        if (!(layoutParams.height == -1 || layoutParams.height == -2)) {
            layoutParams.height = (int) (((float) layoutParams.height) * scale);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginParams.leftMargin = (int) (((float) marginParams.leftMargin) * scale);
            marginParams.topMargin = (int) (((float) marginParams.topMargin) * scale);
            marginParams.rightMargin = (int) (((float) marginParams.rightMargin) * scale);
            marginParams.bottomMargin = (int) (((float) marginParams.bottomMargin) * scale);
        }
        root.setLayoutParams(layoutParams);
        root.setPadding((int) (((float) root.getPaddingLeft()) * scale), (int) (((float) root.getPaddingTop()) * scale), (int) (((float) root.getPaddingRight()) * scale), (int) (((float) root.getPaddingBottom()) * scale));
        if (root instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) root;
            for (int i = 0; i < vg.getChildCount(); i++) {
                scaleViewAndChildren(vg.getChildAt(i), scale, canary + 1);
            }
        }
    }
}
