package com.android.ilevel;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.application.ILevelApplication;
import com.android.ilevel.button.IlevelButton;
import com.ideasthatfloat.ilevel.R;

public class Manul7in2 extends Activity implements View.OnClickListener, View.OnTouchListener {
    final long[] CrcTable = {0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013};
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private IlevelButton btn_BackDown;
    private IlevelButton btn_BackUp;
    private IlevelButton btn_FrontDown;
    private IlevelButton btn_FrontUp;
    private IlevelButton btn_LeftBackDown;
    private IlevelButton btn_LeftBackUp;
    private IlevelButton btn_LeftFrontDown;
    private IlevelButton btn_LeftFrontUp;
    private IlevelButton btn_RightBackDown;
    private IlevelButton btn_RightBackUp;
    private IlevelButton btn_RightFrontDown;
    private IlevelButton btn_RightFrontUp;
    /* access modifiers changed from: private */
    public int buttonPress = 0;
    private BroadcastReceiver errorDialogReceiver;
    /* access modifiers changed from: private */
    public int lastButtonPressed = 0;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private int mCounter = 0;
    /* access modifiers changed from: private */
    public boolean mDown;
    /* access modifiers changed from: private */
    public Handler mHandlerLongPressed = new Handler();
    private final Runnable mRunnable_valveControl = new Runnable() {
        public void run() {
            if (Manul7in2.this.buttonPress == 0) {
                Log.i("valve_sh", "No Button pressed " + Manul7in2.this.buttonPress);
                boolean unused = Manul7in2.this.mDown = false;
                Manul7in2.this.cancelLongPress();
                Manul7in2.this.application.sendButtonReleaseCommand();
                int unused2 = Manul7in2.this.lastButtonPressed = 0;
                return;
            }
            Manul7in2.access$808(Manul7in2.this);
            String command = "a55a0a5001240d0d21210000abcd0000000000001234000000" + String.format("%02x", new Object[]{Integer.valueOf(Manul7in2.this.buttonPress)}) + "00000000000000007b7d7b7d";
            String command2 = command + String.format("%08x", new Object[]{Long.valueOf(Manul7in2.this.getCRC(command).longValue() & -1)});
            Log.i("valve_sh", "Button pressed " + Manul7in2.this.buttonPress + " - " + command2);
            ILevelApplication access$600 = Manul7in2.this.application;
            access$600.getClass();
            new ILevelApplication.SendCommandToSocket(command2).run();
            Manul7in2.this.mHandlerLongPressed.postDelayed(this, 100);
        }
    };
    private Context myContext;
    private int position;
    private RelativeLayout rLayoutBack;
    private RelativeLayout rLayoutNext;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txtMSG;
    private TextView txtNext;
    private TextView txtTitle;
    private Typeface typeface;
    private Typeface typefaceBold;

    static /* synthetic */ int access$808(Manul7in2 x0) {
        int i = x0.mCounter;
        x0.mCounter = i + 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.manual7in2);
        setupUI();
        if (Build.VERSION.SDK_INT >= 11) {
            this.mActionBar = getActionBar();
            this.mActionBar.setDisplayShowHomeEnabled(false);
        }
        if (!this.application.isTablet()) {
            setRequestedOrientation(1);
        }
    }

    private void setupUI() {
        this.myContext = this;
        this.application = ILevelApplication.application;
        try {
            this.typeface = Typeface.createFromAsset(getAssets(), "Helvetica_Neue.ttf");
            this.typefaceBold = Typeface.createFromAsset(getAssets(), "Helvetica_55_Bold.ttf");
        } catch (Exception e) {
            Log.e("FONT", "Could not generate typeface");
        }
        this.txtTitle = (TextView) findViewById(R.id.txtStep_manual7in2);
        this.txtMSG = (TextView) findViewById(R.id.txtStepMSG_manual7in2);
        this.txtNext = (TextView) findViewById(R.id.txtNext_manual7in2);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMSG.setTypeface(this.typeface);
        this.txtNext.setTypeface(this.typeface);
        Intent intent = getIntent();
        if (intent != null) {
            this.position = intent.getIntExtra("MANUAL_CALIBRATION", 0);
        }
        this.rLayoutNext = (RelativeLayout) findViewById(R.id.rLayoutbtnNext_manual7in2);
        this.rLayoutNext.setOnClickListener(this);
        this.btn_FrontUp = (IlevelButton) findViewById(R.id.btn_FrontUp_manual7in2);
        this.btn_FrontDown = (IlevelButton) findViewById(R.id.btn_FrontDown_manual7in2);
        this.btn_RightFrontUp = (IlevelButton) findViewById(R.id.btn_RightFrontUp_manual7in2);
        this.btn_RightFrontDown = (IlevelButton) findViewById(R.id.btn_RightFrontDown_manual7in2);
        this.btn_LeftFrontUp = (IlevelButton) findViewById(R.id.btn_LeftFrontUp_manual7in2);
        this.btn_LeftFrontDown = (IlevelButton) findViewById(R.id.btn_LeftFrontDown_manual7in2);
        this.btn_BackUp = (IlevelButton) findViewById(R.id.btn_BackUp_manual7in2);
        this.btn_BackDown = (IlevelButton) findViewById(R.id.btn_BackDown_manual7in2);
        this.btn_RightBackUp = (IlevelButton) findViewById(R.id.btn_RightBackUp_manual7in2);
        this.btn_RightBackDown = (IlevelButton) findViewById(R.id.btn_RightBackDown_manual7in2);
        this.btn_LeftBackUp = (IlevelButton) findViewById(R.id.btn_LeftBackUp_manual7in2);
        this.btn_LeftBackDown = (IlevelButton) findViewById(R.id.btn_LeftBackDown_manual7in2);
        this.btn_FrontUp.setOnTouchListener(this);
        this.btn_FrontUp.setSampleLongpress(this);
        this.btn_FrontDown.setOnTouchListener(this);
        this.btn_FrontDown.setSampleLongpress(this);
        this.btn_LeftFrontUp.setOnTouchListener(this);
        this.btn_LeftFrontUp.setSampleLongpress(this);
        this.btn_LeftFrontDown.setOnTouchListener(this);
        this.btn_LeftFrontDown.setSampleLongpress(this);
        this.btn_RightFrontUp.setOnTouchListener(this);
        this.btn_RightFrontUp.setSampleLongpress(this);
        this.btn_RightFrontDown.setOnTouchListener(this);
        this.btn_RightFrontDown.setSampleLongpress(this);
        this.btn_BackUp.setOnTouchListener(this);
        this.btn_BackUp.setSampleLongpress(this);
        this.btn_BackDown.setOnTouchListener(this);
        this.btn_BackDown.setSampleLongpress(this);
        this.btn_LeftBackUp.setOnTouchListener(this);
        this.btn_LeftBackUp.setSampleLongpress(this);
        this.btn_LeftBackDown.setOnTouchListener(this);
        this.btn_LeftBackDown.setSampleLongpress(this);
        this.btn_RightBackUp.setOnTouchListener(this);
        this.btn_RightBackUp.setSampleLongpress(this);
        this.btn_RightBackDown.setOnTouchListener(this);
        this.btn_RightBackDown.setSampleLongpress(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.application.networkThread.getSocket() == null) {
            this.application.checkWifiStatus();
        }
        Log.i("status", "Manual7in2 onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Manul7in2.this.alertDialog == null) {
                        boolean unused = Manul7in2.this.showError = true;
                        Manul7in2.this.showErrorMessage(error);
                    } else if (Manul7in2.this.alertDialog != null && !Manul7in2.this.alertDialog.isShowing()) {
                        boolean unused2 = Manul7in2.this.showError = true;
                        Manul7in2.this.showErrorMessage(error);
                    }
                }
            }
        };
        registerReceiver(this.errorDialogReceiver, new IntentFilter("com.iLevel.error"));
    }

    @SuppressLint({"NewApi"})
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == 20) {
            this.application.stopTimerTask();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        try {
            if (this.errorDialogReceiver != null) {
                unregisterReceiver(this.errorDialogReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("System", "Manual7in2 - onPause - isCancelled=true");
        this.application.isCancelled = true;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        try {
            if (this.errorDialogReceiver != null) {
                unregisterReceiver(this.errorDialogReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.screenStateReceiver != null) {
                unregisterReceiver(this.screenStateReceiver);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.showError) {
            Intent intent = new Intent(this, Calibration_Next.class);
            intent.putExtra("POSITION", this.position);
            intent.setFlags(67108864);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View view) {
        if (!this.showError) {
            switch (view.getId()) {
                case R.id.rLayoutbtnNext_manual7in2 /*2131165392*/:
                    Intent intent1 = new Intent(this, Start_Calibration.class);
                    intent1.putExtra("START_CALIBRATION", this.position);
                    startActivity(intent1);
                    finish();
                    return;
                default:
                    return;
            }
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == 0) {
            switch (view.getId()) {
                case R.id.btn_BackDown_manual7in2 /*2131165245*/:
                    this.btn_BackDown.setBackgroundResource(R.drawable.selected_button_down);
                    this.buttonPress |= 160;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_BackUp_manual7in2 /*2131165248*/:
                    this.btn_BackUp.setBackgroundResource(R.drawable.selected_button_up);
                    this.buttonPress |= 80;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_FrontDown_manual7in2 /*2131165252*/:
                    this.btn_FrontDown.setBackgroundResource(R.drawable.selected_button_down);
                    this.buttonPress |= 10;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_FrontUp_manual7in2 /*2131165255*/:
                    this.btn_FrontUp.setBackgroundResource(R.drawable.selected_button_up);
                    this.buttonPress |= 5;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_LeftBackDown_manual7in2 /*2131165259*/:
                    this.btn_LeftBackDown.setBackgroundResource(R.drawable.button_selected_down);
                    this.buttonPress |= 32;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_LeftBackUp_manual7in2 /*2131165263*/:
                    this.btn_LeftBackUp.setBackgroundResource(R.drawable.button_selected_up);
                    this.buttonPress |= 16;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_LeftFrontDown_manual7in2 /*2131165267*/:
                    this.btn_LeftFrontDown.setBackgroundResource(R.drawable.button_selected_down);
                    this.buttonPress |= 2;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_LeftFrontUp_manual7in2 /*2131165271*/:
                    this.btn_LeftFrontUp.setBackgroundResource(R.drawable.button_selected_up);
                    this.buttonPress |= 1;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_RightBackDown_manual7in2 /*2131165278*/:
                    this.btn_RightBackDown.setBackgroundResource(R.drawable.button_selected_down);
                    this.buttonPress |= 128;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_RightBackUp_manual7in2 /*2131165282*/:
                    this.btn_RightBackUp.setBackgroundResource(R.drawable.button_selected_up);
                    this.buttonPress |= 64;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_RightFrontDown_manual7in2 /*2131165286*/:
                    this.btn_RightFrontDown.setBackgroundResource(R.drawable.button_selected_down);
                    this.buttonPress |= 8;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_RightFrontUp_manual7in2 /*2131165290*/:
                    this.btn_RightFrontUp.setBackgroundResource(R.drawable.button_selected_up);
                    this.buttonPress |= 4;
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
            }
        }
        if (event.getAction() != 1 && event.getAction() != 3) {
            return false;
        }
        Log.i("valve", "Action UP or CANCEL - Manual Calibration");
        switch (view.getId()) {
            case R.id.btn_BackDown_manual7in2 /*2131165245*/:
                this.btn_BackDown.setBackgroundResource(R.drawable.button_down);
                this.buttonPress = (int) (((long) this.buttonPress) & -161);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_BackUp_manual7in2 /*2131165248*/:
                this.btn_BackUp.setBackgroundResource(R.drawable.button_up);
                this.buttonPress = (int) (((long) this.buttonPress) & -81);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_FrontDown_manual7in2 /*2131165252*/:
                this.btn_FrontDown.setBackgroundResource(R.drawable.button_down);
                this.buttonPress = (int) (((long) this.buttonPress) & -11);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_FrontUp_manual7in2 /*2131165255*/:
                this.btn_FrontUp.setBackgroundResource(R.drawable.button_up);
                this.buttonPress = (int) (((long) this.buttonPress) & -6);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_LeftBackDown_manual7in2 /*2131165259*/:
                this.btn_LeftBackDown.setBackgroundResource(R.drawable.button_middle_dn);
                this.buttonPress = (int) (((long) this.buttonPress) & -33);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_LeftBackUp_manual7in2 /*2131165263*/:
                this.btn_LeftBackUp.setBackgroundResource(R.drawable.button_middle_up);
                this.buttonPress = (int) (((long) this.buttonPress) & -17);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_LeftFrontDown_manual7in2 /*2131165267*/:
                this.btn_LeftFrontDown.setBackgroundResource(R.drawable.button_middle_dn);
                this.buttonPress = (int) (((long) this.buttonPress) & -3);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_LeftFrontUp_manual7in2 /*2131165271*/:
                this.btn_LeftFrontUp.setBackgroundResource(R.drawable.button_middle_up);
                this.buttonPress = (int) (((long) this.buttonPress) & -2);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_RightBackDown_manual7in2 /*2131165278*/:
                this.btn_RightBackDown.setBackgroundResource(R.drawable.button_middle_dn);
                this.buttonPress = (int) (((long) this.buttonPress) & -129);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_RightBackUp_manual7in2 /*2131165282*/:
                this.btn_RightBackUp.setBackgroundResource(R.drawable.button_middle_up);
                this.buttonPress = (int) (((long) this.buttonPress) & -65);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_RightFrontDown_manual7in2 /*2131165286*/:
                this.btn_RightFrontDown.setBackgroundResource(R.drawable.button_middle_dn);
                this.buttonPress = (int) (((long) this.buttonPress) & -9);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            case R.id.btn_RightFrontUp_manual7in2 /*2131165290*/:
                this.btn_RightFrontUp.setBackgroundResource(R.drawable.button_middle_up);
                this.buttonPress = (int) (((long) this.buttonPress) & -5);
                if (this.lastButtonPressed != 0) {
                    return false;
                }
                this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                return false;
            default:
                return false;
        }
    }

    private long Crc32Fast(long Crc, long Data) {
        long Crc2 = Crc ^ Data;
        long Crc3 = ((Crc2 << 4) ^ this.CrcTable[(int) ((Crc2 >>> 28) & 15)]) & 4294967295L;
        long Crc4 = ((Crc3 << 4) ^ this.CrcTable[(int) ((Crc3 >>> 28) & 15)]) & 4294967295L;
        long Crc5 = ((Crc4 << 4) ^ this.CrcTable[(int) ((Crc4 >>> 28) & 15)]) & 4294967295L;
        long Crc6 = ((Crc5 << 4) ^ this.CrcTable[(int) ((Crc5 >>> 28) & 15)]) & 4294967295L;
        long Crc7 = ((Crc6 << 4) ^ this.CrcTable[(int) ((Crc6 >>> 28) & 15)]) & 4294967295L;
        long Crc8 = ((Crc7 << 4) ^ this.CrcTable[(int) ((Crc7 >>> 28) & 15)]) & 4294967295L;
        long Crc9 = ((Crc8 << 4) ^ this.CrcTable[(int) ((Crc8 >>> 28) & 15)]) & 4294967295L;
        return ((Crc9 << 4) ^ this.CrcTable[(int) ((Crc9 >>> 28) & 15)]) & 4294967295L;
    }

    /* access modifiers changed from: private */
    public Long getCRC(String dataIn) {
        return Long.valueOf(Crc32Fast(Crc32Fast(Crc32Fast(Crc32Fast(Crc32Fast(Crc32Fast(4294967295L, Long.parseLong(dataIn.substring(21, 28), 16)), Long.parseLong(dataIn.substring(29, 36), 16)), Long.parseLong(dataIn.substring(37, 44), 16)), Long.parseLong(dataIn.substring(45, 52), 16)), Long.parseLong(dataIn.substring(53, 60), 16)), Long.parseLong(dataIn.substring(61, 68), 16)));
    }

    public void cancelLongPress() {
        this.mDown = false;
        this.mCounter = 0;
    }

    /* access modifiers changed from: private */
    public void showErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Manul7in2.this.alertDialog.dismiss();
                ILevelApplication access$600 = Manul7in2.this.application;
                access$600.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Manul7in2.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Manul7in2.this.alertDialog.dismiss();
                boolean unused = Manul7in2.this.showError = false;
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.show();
    }

    private class ScreenStateReceiver extends BroadcastReceiver {
        private ScreenStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                Manul7in2.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Manul7in2.this.application.checkWifiStatus();
            }
        }
    }
}
