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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;

public class Calibration_Next extends Activity implements View.OnClickListener {
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private BroadcastReceiver errorDialogReceiver;
    private LinearLayout lLayout_CaliberationNext;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private Context myContext;
    private int position = 0;
    /* access modifiers changed from: private */
    public RelativeLayout rLayout_CaliberationNext;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txtMSG_CaliberationNext;
    private TextView txtProced;
    private TextView txtTitle_CaliberationNext;
    private TextView txtWarning_CaliberationNext;
    private TextView txt_Header;
    private Typeface typeface;
    private Typeface typefaceBold;
    /* access modifiers changed from: private */
    public boolean wasScreenOn = true;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.calibration_next);
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
        this.txtProced = (TextView) findViewById(R.id.txt_proceed_CaliberationNext);
        this.txtTitle_CaliberationNext = (TextView) findViewById(R.id.txtTitle_CaliberationNext);
        this.txtMSG_CaliberationNext = (TextView) findViewById(R.id.txtMSG_CaliberationNext);
        this.txtWarning_CaliberationNext = (TextView) findViewById(R.id.txtWarning_CaliberationNext);
        this.rLayout_CaliberationNext = (RelativeLayout) findViewById(R.id.rLayout_CaliberationNext);
        this.txtProced.setTypeface(this.typeface);
        this.txtTitle_CaliberationNext.setTypeface(this.typefaceBold);
        this.txtMSG_CaliberationNext.setTypeface(this.typeface);
        this.txtWarning_CaliberationNext.setTypeface(this.typefaceBold);
        Intent intent = getIntent();
        if (intent != null) {
            this.position = intent.getIntExtra("POSITION", 0);
        }
        switch (this.position) {
            case 0:
                this.txtTitle_CaliberationNext.setText(R.string.automatic_calibration);
                this.txtMSG_CaliberationNext.setText(R.string.automatic_calibration_next_msg);
                this.txtWarning_CaliberationNext.setText(R.string.automatic_calibration_warning_next);
                break;
            case 1:
                this.txtTitle_CaliberationNext.setText(R.string.manual_calibration);
                this.txtMSG_CaliberationNext.setText(R.string.automatic_calibration_next_msg);
                this.txtWarning_CaliberationNext.setText(R.string.automatic_calibration_warning_next);
                break;
            case 2:
                this.txtTitle_CaliberationNext.setText(R.string.auto_cal_sensor_title);
                this.txtMSG_CaliberationNext.setText(R.string.automatic_calibration_next_msg);
                this.txtWarning_CaliberationNext.setText(R.string.automatic_calibration_warning_next);
                break;
            case 3:
                this.txtTitle_CaliberationNext.setText(R.string.man_cal_sensor_title);
                this.txtMSG_CaliberationNext.setText(R.string.automatic_calibration_next_msg);
                this.txtWarning_CaliberationNext.setText(R.string.automatic_calibration_warning_next);
                break;
        }
        this.rLayout_CaliberationNext.setOnClickListener(this);
        this.rLayout_CaliberationNext.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({"InlinedApi"})
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    Calibration_Next.this.rLayout_CaliberationNext.setBackgroundColor(Calibration_Next.this.getResources().getColor(17170456));
                }
                if (event.getAction() != 1) {
                    return false;
                }
                Calibration_Next.this.rLayout_CaliberationNext.setBackgroundColor(Calibration_Next.this.getResources().getColor(17170445));
                return false;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.application.networkThread.getSocket() == null) {
            this.application.checkWifiStatus();
        }
        Log.i("status", "Calibration_Next onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Calibration_Next.this.alertDialog == null) {
                        boolean unused = Calibration_Next.this.showError = true;
                        Calibration_Next.this.showErrorMessage(error);
                    } else if (Calibration_Next.this.alertDialog != null && !Calibration_Next.this.alertDialog.isShowing()) {
                        boolean unused2 = Calibration_Next.this.showError = true;
                        Calibration_Next.this.showErrorMessage(error);
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
            if (this.alertDialog != null) {
                this.alertDialog.dismiss();
            }
            if (this.errorDialogReceiver != null) {
                unregisterReceiver(this.errorDialogReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("System", "Calibration_Next - onPause - isCancelled=true");
        this.application.isCancelled = true;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        try {
            if (this.alertDialog != null) {
                this.alertDialog.dismiss();
            }
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
            Intent intent = new Intent(this, Calibration.class);
            intent.setFlags(67108864);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (this.position) {
            case 0:
                intent = new Intent(this, Automatic_Calibration.class);
                intent.putExtra("AUTO_CALIBRATION", 0);
                break;
            case 1:
                intent = new Intent(this, Manul7in2.class);
                intent.putExtra("MANUAL_CALIBRATION", 1);
                break;
            case 2:
                intent = new Intent(this, Automatic_Calibration.class);
                intent.putExtra("AUTO_CALIBRATION", 2);
                break;
            case 3:
                intent = new Intent(this, Manul7in2.class);
                intent.putExtra("MANUAL_CALIBRATION", 3);
                break;
        }
        if (!this.showError) {
            startActivity(intent);
            finish();
        }
    }

    /* access modifiers changed from: private */
    public void showErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Calibration_Next.this.alertDialog.dismiss();
                ILevelApplication access$500 = Calibration_Next.this.application;
                access$500.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Calibration_Next.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Calibration_Next.this.alertDialog.dismiss();
                boolean unused = Calibration_Next.this.showError = false;
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
                boolean unused = Calibration_Next.this.wasScreenOn = false;
                Calibration_Next.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                boolean unused2 = Calibration_Next.this.wasScreenOn = true;
                Calibration_Next.this.application.checkWifiStatus();
            }
        }
    }
}
