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

public class Trim_Mode extends Activity implements CompoundButton.OnCheckedChangeListener {
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private CompoundButton cb_trim;
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
        setContentView(R.layout.trim_mode);
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
        this.txtTitle = (TextView) findViewById(R.id.txtTitle_TRIM);
        this.txtMsg = (TextView) findViewById(R.id.txtMsg_TRIM);
        this.cb_trim = (CompoundButton) findViewById(R.id.switch_trim);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMsg.setTypeface(this.typeface);
        this.txtMsg.setText(Html.fromHtml("Once a general height adjustment has been made, the e-Level™ will begin to monitor this position and make corrections if necessary (when <B><I>RideMonitor™ Mode </I></B> is ON). The first 5 seconds of <I><B>RideMonitor™ Mode</I></B> is called <I><B>TrimMode™.</I></B> For the <I><B>TrimMode™</I></B>, the system holds a tighter tolerance than regular parked mode and makes precise adjustments if necessary. For the most accurate leveling, this feature should be left ON. For applications where this feature could be a nuisance due to frequent starts and stops and where super accurate leveling is not such a concern, this feature can be turned OFF."));
        this.cb_trim.setChecked(false);
        if (this.application.getTrim_mode().equals(getResources().getString(R.string.on))) {
            this.cb_trim.setChecked(true);
        } else {
            this.cb_trim.setChecked(false);
        }
        this.cb_trim.setOnCheckedChangeListener(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.showError) {
            Intent intent = new Intent(this, Settings.class);
            intent.setFlags(67108864);
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
        Log.i("status", "Trim_Mode onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Trim_Mode.this.alertDialog == null) {
                        boolean unused = Trim_Mode.this.showError = true;
                        Trim_Mode.this.showErrorMessage(error);
                    } else if (Trim_Mode.this.alertDialog != null && !Trim_Mode.this.alertDialog.isShowing()) {
                        boolean unused2 = Trim_Mode.this.showError = true;
                        Trim_Mode.this.showErrorMessage(error);
                    }
                }
            }
        };
        Log.i("status", "HomeScreen onResume isCancelled = false");
        this.application.isCancelled = false;
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
        Log.i("System", "Trim_Mode - onPause - isCancelled=true");
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
            this.application.setTrim_mode("ON");
            command = "a55a0a5001240d0d21210000abcd00000050000012340000000000000000000000007b7d7b7d4d6056e3";
        } else {
            this.application.setTrim_mode("OFF");
            command = "a55a0a5001240d0d21210000abcd00000051000012340000000000000000000000007b7d7b7d5ab367be";
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
                Trim_Mode.this.alertDialog.dismiss();
                ILevelApplication access$400 = Trim_Mode.this.application;
                access$400.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Trim_Mode.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Trim_Mode.this.alertDialog.dismiss();
                boolean unused = Trim_Mode.this.showError = false;
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
                Trim_Mode.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Trim_Mode.this.application.checkWifiStatus();
            }
        }
    }
}
