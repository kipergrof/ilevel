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
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;

public class Right_Monitor_Mode extends Activity implements CompoundButton.OnCheckedChangeListener {
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private CompoundButton cb_rmm;
    private BroadcastReceiver errorDialogReceiver;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private Context myContext;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txtMsg;
    private TextView txtTitle;
    private Typeface typeface;
    private Typeface typefaceBold;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.ride_monitor_mode);
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
        this.application = (ILevelApplication) getApplicationContext();
        try {
            this.typeface = Typeface.createFromAsset(getAssets(), "fonts/Helvetica_55.ttf");
            this.typefaceBold = Typeface.createFromAsset(getAssets(), "Helvetica_55_Bold.ttf");
        } catch (Exception e) {
            Log.e("FONT", "Could not generate typeface");
        }
        this.txtTitle = (TextView) findViewById(R.id.txtTitleRMM);
        this.txtMsg = (TextView) findViewById(R.id.txtMSG_RMM);
        this.cb_rmm = (CompoundButton) findViewById(R.id.switch_rmm);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMsg.setTypeface(this.typeface);
        this.txtMsg.setText(Html.fromHtml("Your system was shipped with <b><I>RideMonitor™ Mode</I></b> enabled (ON). You may choose to disable (OFF) or re-enable (ON) this feature. When <b><I>RideMonitor™ Mode</I></b> is ON, the system will Monitor the vehicle™s height whenever the ignition is ON and make adjustments for changes in load when deemed necessary. When this feature is OFF, the system will only adjust when prompted by pressing the height selection switch ™ It will not Monitor the vehicle™s height and will not make adjustments for changes in load automatically."));
        this.cb_rmm.setChecked(false);
        if (this.application.getRide_monitor_mode().equals(getResources().getString(R.string.on))) {
            this.cb_rmm.setChecked(true);
        } else {
            this.cb_rmm.setChecked(false);
        }
        this.cb_rmm.setOnCheckedChangeListener(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.showError) {
            startActivity(new Intent(this, Settings.class));
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
        Log.i("status", "Right_Monitor_Mode onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Right_Monitor_Mode.this.alertDialog == null) {
                        boolean unused = Right_Monitor_Mode.this.showError = true;
                        Right_Monitor_Mode.this.showErrorMessage(error);
                    } else if (Right_Monitor_Mode.this.alertDialog != null && !Right_Monitor_Mode.this.alertDialog.isShowing()) {
                        boolean unused2 = Right_Monitor_Mode.this.showError = true;
                        Right_Monitor_Mode.this.showErrorMessage(error);
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
        Log.i("System", "Right_Monitor_Mode - onPause - isCancelled=true");
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

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String command;
        if (isChecked) {
            this.application.setRide_monitor_mode(getResources().getString(R.string.on));
            command = "a55a0a5001240d0d21210000abcd0000004e000012340000000000000000000000007b7d7b7dfae173a2";
        } else {
            this.application.setRide_monitor_mode(getResources().getString(R.string.off));
            command = "a55a0a5001240d0d21210000abcd0000004f000012340000000000000000000000007b7d7b7ded3242ff";
        }
        ILevelApplication iLevelApplication = this.application;
        iLevelApplication.getClass();
        new ILevelApplication.SendCommandToSocket(command).run();
    }

    /* access modifiers changed from: private */
    public void showErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Right_Monitor_Mode.this.alertDialog.dismiss();
                ILevelApplication access$400 = Right_Monitor_Mode.this.application;
                access$400.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Right_Monitor_Mode.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Right_Monitor_Mode.this.alertDialog.dismiss();
                boolean unused = Right_Monitor_Mode.this.showError = false;
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
                Right_Monitor_Mode.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Right_Monitor_Mode.this.application.checkWifiStatus();
            }
        }
    }
}
