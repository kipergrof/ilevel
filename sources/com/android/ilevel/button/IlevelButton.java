package com.android.ilevel.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Button;
import com.android.ilevel.HomeScreen;
import com.android.ilevel.Manul7in2;
import com.android.ilevel.Save_Heights;

public class IlevelButton extends Button {
    private HomeScreen homeScreen;
    private Manul7in2 manul7in2;
    private Save_Heights save_Heights;

    public IlevelButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public IlevelButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IlevelButton(Context context) {
        super(context);
    }

    public void setSampleLongpress(HomeScreen homeScreen2) {
        this.homeScreen = homeScreen2;
    }

    public void setSampleLongpress(Manul7in2 manul7in22) {
        this.manul7in2 = manul7in22;
    }

    public void setSampleLongpress(Save_Heights save_Heights2) {
        this.save_Heights = save_Heights2;
    }

    public boolean onTouchEvent(MotionEvent event) {
        cancelLongpressIfRequired(event);
        return super.onTouchEvent(event);
    }

    public boolean onTrackballEvent(MotionEvent event) {
        cancelLongpressIfRequired(event);
        return super.onTrackballEvent(event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 23 || keyCode == 66) {
            cancelLongpress();
        }
        return super.onKeyUp(keyCode, event);
    }

    private void cancelLongpressIfRequired(MotionEvent event) {
        if (event.getAction() == 3 || event.getAction() == 1) {
            cancelLongpress();
        }
    }

    private void cancelLongpress() {
        if (this.homeScreen != null) {
            this.homeScreen.cancelLongPress();
        } else if (this.manul7in2 != null) {
            this.manul7in2.cancelLongPress();
        } else if (this.save_Heights != null) {
            this.save_Heights.cancelLongPress();
        }
    }
}
