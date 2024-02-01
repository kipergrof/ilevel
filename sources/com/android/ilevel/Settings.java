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
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.adapter.PaintingAdapterSetting;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;
import java.util.ArrayList;
import java.util.List;

public class Settings extends Activity implements AdapterView.OnItemClickListener {
    /* access modifiers changed from: private */
    public PaintingAdapterSetting adapter;
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private BroadcastReceiver errorDialogReceiver;
    private TextView firmware_text;
    private Intent intent;
    private List<String> list;
    private ListView lvpentingview;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private Context myContext;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private Typeface typeface;
    private Typeface typefaceBold;
    private TextView version_text;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.settings);
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
        this.list = new ArrayList();
        this.list.add("Ride-Height-On-Start");
        this.list.add("RideMonitor™ Mode");
        this.list.add("RideMonitor™ Mode Accuracy");
        this.list.add("TrimMode™");
        this.list.add("Tank Pressure Mode");
        this.adapter = new PaintingAdapterSetting(this, this.list, this.application);
        this.lvpentingview = (ListView) findViewById(R.id.settinglistView1);
        this.lvpentingview.setAdapter(this.adapter);
        this.lvpentingview.setOnItemClickListener(this);
        this.version_text = (TextView) findViewById(R.id.footer_version);
        this.firmware_text = (TextView) findViewById(R.id.footer_firmware);
        this.version_text.setTypeface(this.typeface);
        this.firmware_text.setTypeface(this.typeface);
        this.version_text.setText("Version: " + this.application.appVersion);
        this.firmware_text.setText("Firmware: " + this.application.firmwareVersion);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.showError) {
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
        Log.i("status", "Settings onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.adapter.notifyDataSetChanged();
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Settings.this.alertDialog == null) {
                        boolean unused = Settings.this.showError = true;
                        Settings.this.showErrorMessage(error);
                    } else if (Settings.this.alertDialog != null && !Settings.this.alertDialog.isShowing()) {
                        boolean unused2 = Settings.this.showError = true;
                        Settings.this.showErrorMessage(error);
                    }
                }
            }
        };
        registerReceiver(this.errorDialogReceiver, new IntentFilter("com.iLevel.error"));
        CountDownTimer start = new CountDownTimer(3000, 500) {
            public void onTick(long millisUntilFinished) {
                Settings.this.adapter.notifyDataSetChanged();
            }

            public void onFinish() {
                Settings.this.adapter.notifyDataSetChanged();
            }
        }.start();
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
        Log.i("System", "Settings - onPause - isCancelled=true");
        this.application.isCancelled = true;
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        if (position == 0) {
            this.intent = new Intent(this, Ride_Heigh_On_Start.class);
        }
        if (position == 1) {
            this.intent = new Intent(this, Right_Monitor_Mode.class);
        }
        if (position == 2) {
            this.intent = new Intent(this, Ride_Mode_Accuracy.class);
        }
        if (position == 3) {
            this.intent = new Intent(this, Trim_Mode.class);
        }
        if (position == 4) {
            this.intent = new Intent(this, Tank_Pressure_Mode.class);
        }
        if (!this.showError) {
            startActivity(this.intent);
            finish();
        }
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

    /* access modifiers changed from: private */
    public void showErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Settings.this.alertDialog.dismiss();
                ILevelApplication access$500 = Settings.this.application;
                access$500.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Settings.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Settings.this.alertDialog.dismiss();
                boolean unused = Settings.this.showError = false;
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
                Settings.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Settings.this.application.checkWifiStatus();
            }
        }
    }
}
