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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;

public class Start_Calibration extends Activity implements View.OnClickListener {
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private Button btnStartCalibration;
    private BroadcastReceiver errorDialogReceiver;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private Context myContext;
    private int position;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txtMSG;
    private TextView txtTitle;
    private TextView txtWarning;
    private Typeface typeface;
    private Typeface typefaceBold;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.start_calibration);
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
        this.txtTitle = (TextView) findViewById(R.id.txtTitle_StartCalibration);
        this.txtMSG = (TextView) findViewById(R.id.txtMSG_StartCalibration);
        this.txtWarning = (TextView) findViewById(R.id.txtWarning_StartCalibration);
        this.btnStartCalibration = (Button) findViewById(R.id.btnStartCalibration);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMSG.setTypeface(this.typeface);
        this.txtWarning.setTypeface(this.typefaceBold);
        this.btnStartCalibration.setTypeface(this.typeface);
        this.txtMSG.setText(R.string.start_calibration_detail);
        this.txtWarning.setText(R.string.start_calibration_warning);
        this.btnStartCalibration.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            this.position = intent.getIntExtra("START_CALIBRATION", 0);
        }
        switch (this.position) {
            case 0:
                this.txtTitle.setText(R.string.automatic_calibration);
                return;
            case 1:
                this.txtTitle.setText(R.string.manual_calibration);
                return;
            case 2:
                this.txtTitle.setText(R.string.auto_cal_sensor_title);
                return;
            case 3:
                this.txtTitle.setText(R.string.man_cal_sensor_title);
                return;
            default:
                return;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            Intent intent = null;
            switch (this.position) {
                case 0:
                    intent = new Intent(this, Automatic_Calibration.class);
                    intent.putExtra("AUTO_CALIBRATION", this.position);
                    break;
                case 1:
                    intent = new Intent(this, Manul7in2.class);
                    intent.putExtra("MANUAL_CALIBRATION", this.position);
                    break;
                case 2:
                    intent = new Intent(this, Automatic_Calibration.class);
                    intent.putExtra("AUTO_CALIBRATION", this.position);
                    break;
                case 3:
                    intent = new Intent(this, Manul7in2.class);
                    intent.putExtra("MANUAL_CALIBRATION", this.position);
                    break;
            }
            if (!this.showError) {
                startActivity(intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.application.networkThread.getSocket() == null) {
            this.application.checkWifiStatus();
        }
        Log.i("status", "Start_Calibration onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Start_Calibration.this.alertDialog == null) {
                        boolean unused = Start_Calibration.this.showError = true;
                        Start_Calibration.this.showErrorMessage(error);
                    } else if (Start_Calibration.this.alertDialog != null && !Start_Calibration.this.alertDialog.isShowing()) {
                        boolean unused2 = Start_Calibration.this.showError = true;
                        Start_Calibration.this.showErrorMessage(error);
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
        Log.i("System", "Start_Calibration - onPause - isCancelled=true");
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
        if (view == this.btnStartCalibration && !this.showError) {
            Intent intent = new Intent(this, Abort_Calibration.class);
            intent.putExtra("ABORT_CALIBRATION", this.position);
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
                Start_Calibration.this.alertDialog.dismiss();
                ILevelApplication access$400 = Start_Calibration.this.application;
                access$400.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Start_Calibration.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Start_Calibration.this.alertDialog.dismiss();
                boolean unused = Start_Calibration.this.showError = false;
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
                Start_Calibration.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Start_Calibration.this.application.checkWifiStatus();
            }
        }
    }
}
