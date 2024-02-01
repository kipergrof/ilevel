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

public class Automatic_Calibration extends Activity implements View.OnClickListener {
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private BroadcastReceiver errorDialogReceiver;
    private LinearLayout lLayoutProcedure_AutomaticCalibration;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private Context myContext;
    private int position;
    /* access modifiers changed from: private */
    public RelativeLayout rLayoutProcedure_AutomaticCalibration;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txtHeader;
    private TextView txtMSG;
    private TextView txtProceed;
    private TextView txtTitle;
    private TextView txtWarning;
    private Typeface typeface;
    private Typeface typefaceBold;
    /* access modifiers changed from: private */
    public boolean wasScreenOn = true;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.automatic_calibration);
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
        this.rLayoutProcedure_AutomaticCalibration = (RelativeLayout) findViewById(R.id.rLayoutProcedure_AutomaticCalibration);
        this.txtProceed = (TextView) findViewById(R.id.txtProceed_AutomaticCalibration);
        this.txtTitle = (TextView) findViewById(R.id.txtTitle_AutomaticCalibration);
        this.txtMSG = (TextView) findViewById(R.id.txtMSG_AutomaticCalibration);
        this.txtWarning = (TextView) findViewById(R.id.txtWarning_AutomaticCalibration);
        this.txtProceed.setTypeface(this.typeface);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMSG.setTypeface(this.typeface);
        this.txtWarning.setTypeface(this.typeface);
        Intent intent = getIntent();
        if (intent != null) {
            this.position = intent.getIntExtra("AUTO_CALIBRATION", 0);
        }
        switch (this.position) {
            case 0:
                this.txtTitle.setText(R.string.automatic_calibration);
                this.txtMSG.setText(R.string.automatic_calibration_msg);
                this.txtWarning.setText(R.string.automatic_calibration_warning);
                break;
            case 2:
                this.txtTitle.setText(R.string.auto_cal_sensor_title);
                this.txtMSG.setText(R.string.automatic_calibration_msg);
                this.txtWarning.setText(R.string.automatic_calibration_warning);
                break;
        }
        this.rLayoutProcedure_AutomaticCalibration.setOnClickListener(this);
        this.rLayoutProcedure_AutomaticCalibration.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    Automatic_Calibration.this.rLayoutProcedure_AutomaticCalibration.setBackgroundColor(Automatic_Calibration.this.getResources().getColor(17170456));
                }
                if (event.getAction() != 1) {
                    return false;
                }
                Automatic_Calibration.this.rLayoutProcedure_AutomaticCalibration.setBackgroundColor(Automatic_Calibration.this.getResources().getColor(17170445));
                return false;
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.showError) {
            Intent intent = new Intent(this, Calibration_Next.class);
            intent.putExtra("POSITION", this.position);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.application.networkThread.getSocket() == null) {
            this.application.checkWifiStatus();
        }
        Log.i("status", "Automatic_Calibration onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Automatic_Calibration.this.alertDialog == null) {
                        boolean unused = Automatic_Calibration.this.showError = true;
                        Automatic_Calibration.this.showErrorMessage(error);
                    } else if (Automatic_Calibration.this.alertDialog != null && !Automatic_Calibration.this.alertDialog.isShowing()) {
                        boolean unused2 = Automatic_Calibration.this.showError = true;
                        Automatic_Calibration.this.showErrorMessage(error);
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
        Log.i("System", "Automatic_Calibration - onPause - isCancelled=true");
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

    public void onClick(View view) {
        if (view == this.rLayoutProcedure_AutomaticCalibration && !this.showError) {
            Intent intent = new Intent(this, Start_Calibration.class);
            intent.putExtra("START_CALIBRATION", this.position);
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
                Automatic_Calibration.this.alertDialog.dismiss();
                ILevelApplication access$500 = Automatic_Calibration.this.application;
                access$500.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Automatic_Calibration.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Automatic_Calibration.this.alertDialog.dismiss();
                boolean unused = Automatic_Calibration.this.showError = false;
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
                boolean unused = Automatic_Calibration.this.wasScreenOn = false;
                Automatic_Calibration.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                boolean unused2 = Automatic_Calibration.this.wasScreenOn = true;
                Automatic_Calibration.this.application.checkWifiStatus();
            }
        }
    }
}
