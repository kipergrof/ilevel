package com.android.ilevel;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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

public class Ride_Heigh_On_Start extends Activity implements CompoundButton.OnCheckedChangeListener {
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private CompoundButton cb_rhos;
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
        setContentView(R.layout.ride_height_on_start);
        setupUI();
        if (Build.VERSION.SDK_INT >= 11) {
            this.mActionBar = getActionBar();
            this.mActionBar.setDisplayShowHomeEnabled(false);
        }
        if (!this.application.isTablet()) {
            setRequestedOrientation(1);
        }
    }

    @TargetApi(14)
    private void setupUI() {
        this.myContext = this;
        this.application = (ILevelApplication) getApplicationContext();
        try {
            this.typeface = Typeface.createFromAsset(getAssets(), "fonts/Helvetica_55.ttf");
            this.typefaceBold = Typeface.createFromAsset(getAssets(), "Helvetica_55_Bold.ttf");
        } catch (Exception e) {
            Log.e("FONT", "Could not generate typeface");
        }
        this.txtTitle = (TextView) findViewById(R.id.txtTitleRHOS);
        this.txtMsg = (TextView) findViewById(R.id.txtRHOS_Msg);
        this.cb_rhos = (CompoundButton) findViewById(R.id.switch_rhos);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMsg.setTypeface(this.typeface);
        this.txtMsg.setText(Html.fromHtml("Your system was shipped with <B><I>Ride-Height-On-Start enabled</I></B> (ON). You may choose to disable (OFF) or re-enable (ON) this feature. When this feature is ON, the system will automatically re-level the vehicle to <B><I>Position #2 (Ride Height)</I></B> every time that the ignition is switched ON. When this feature is OFF, the system will remain at the last height the vehicle was at when the ignition is switched ON."));
        if (this.application.getRide_height_on_start().equals(getResources().getString(R.string.on))) {
            this.cb_rhos.setChecked(true);
        } else {
            this.cb_rhos.setChecked(false);
        }
        this.cb_rhos.setOnCheckedChangeListener(this);
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
        Log.i("status", "RHOS onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Ride_Heigh_On_Start.this.alertDialog == null) {
                        boolean unused = Ride_Heigh_On_Start.this.showError = true;
                        Ride_Heigh_On_Start.this.showErrorMessage(error);
                    } else if (Ride_Heigh_On_Start.this.alertDialog != null && !Ride_Heigh_On_Start.this.alertDialog.isShowing()) {
                        boolean unused2 = Ride_Heigh_On_Start.this.showError = true;
                        Ride_Heigh_On_Start.this.showErrorMessage(error);
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
        Log.i("System", "RHOS - onPause - isCancelled=true");
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

    public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
        String command;
        if (isChecked) {
            this.application.setRide_height_on_start(getResources().getString(R.string.on));
            command = "a55a0a5001240d0d21210000abcd0000004c000012340000000000000000000000007b7d7b7dd5471118";
        } else {
            this.application.setRide_height_on_start(getResources().getString(R.string.off));
            command = "a55a0a5001240d0d21210000abcd0000004d000012340000000000000000000000007b7d7b7dc2942045";
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
                Ride_Heigh_On_Start.this.alertDialog.dismiss();
                ILevelApplication access$400 = Ride_Heigh_On_Start.this.application;
                access$400.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Ride_Heigh_On_Start.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Ride_Heigh_On_Start.this.alertDialog.dismiss();
                boolean unused = Ride_Heigh_On_Start.this.showError = false;
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
                Ride_Heigh_On_Start.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Ride_Heigh_On_Start.this.application.checkWifiStatus();
            }
        }
    }
}
