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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;
import java.util.Timer;
import java.util.TimerTask;

public class Abort_Calibration extends Activity implements View.OnClickListener {
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private Button btnAbortCaliberation;
    private BroadcastReceiver calibrationCompleteReceiver;
    private BroadcastReceiver errorDialogReceiver;
    private BroadcastReceiver errorIndicatorReceiver;
    private Handler handler;
    private boolean isCalibrationDone = false;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private Context myContext;
    private int position;
    private ProgressBar progressBar;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private Timer statusTimer;
    private TextView txtMSG;
    private TextView txtTitle;
    private Typeface typeface;
    private Typeface typefaceBold;
    /* access modifiers changed from: private */
    public int updateUICounter = 0;
    /* access modifiers changed from: private */
    public boolean wasScreenOn = true;

    static /* synthetic */ int access$208(Abort_Calibration x0) {
        int i = x0.updateUICounter;
        x0.updateUICounter = i + 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.abort_calibration);
        setupUI();
        if (Build.VERSION.SDK_INT >= 11) {
            this.mActionBar = getActionBar();
            this.mActionBar.setDisplayShowHomeEnabled(false);
        }
        if (!this.application.isTablet()) {
            setRequestedOrientation(1);
        }
        String command = "";
        switch (this.position) {
            case 0:
                command = "a55a0a5001240d0d21210000abcd00000031000012340000000000000000000000007b7d7b7d4a5e4b5b";
                break;
            case 1:
                command = "a55a0a5001240d0d21210000abcd00000030000012340000000000000000000000007b7d7b7d5d8d7a06";
                break;
            case 2:
                command = "a55a0a5001240d0d21210000abcd00000033000012340000000000000000000000007b7d7b7d65f829e1";
                break;
            case 3:
                command = "a55a0a5001240d0d21210000abcd00000032000012340000000000000000000000007b7d7b7d722b18bc";
                break;
        }
        ILevelApplication iLevelApplication = this.application;
        iLevelApplication.getClass();
        new ILevelApplication.SendCommandToSocket(command).run();
        this.statusTimer = new Timer();
        this.statusTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Log.i("statusTimer", "Send status update");
                Abort_Calibration.this.application.sendButtonReleaseCommand();
            }
        }, 1000, 1000);
        Log.i("statusTimer", "Status Timer Created - onCreate");
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
        this.btnAbortCaliberation = (Button) findViewById(R.id.btnAbortCalibration);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar_AbortCalibration);
        this.txtTitle = (TextView) findViewById(R.id.txtTitle_AbortCaliberation);
        this.txtMSG = (TextView) findViewById(R.id.txtMSG_AbortCaliberation);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMSG.setTypeface(this.typeface);
        this.btnAbortCaliberation.setTypeface(this.typeface);
        this.btnAbortCaliberation.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            this.position = intent.getIntExtra("ABORT_CALIBRATION", 0);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.showError) {
            if (!this.isCalibrationDone) {
                for (int i = 0; i < 5; i++) {
                    this.handler = new Handler();
                    this.handler.postDelayed(new Runnable() {
                        public void run() {
                            ILevelApplication access$000 = Abort_Calibration.this.application;
                            access$000.getClass();
                            new ILevelApplication.SendCommandToSocket("a55a0a5001240d0d21210000abcd00000000000012340000000100000000000000007b7d7b7d259bc8c9").run();
                        }
                    }, 100);
                }
                try {
                    Thread.sleep(1400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (this.statusTimer != null) {
                this.statusTimer.cancel();
                this.statusTimer.purge();
                this.statusTimer = null;
            }
            Log.i("statusTimer", "Status Timer Cancelled - Back Button");
            Log.i("Abort_Calibration", "Back Button has been pressed");
            Intent intent = new Intent(this, Start_Calibration.class);
            intent.putExtra("START_CALIBRATION", this.position);
            intent.setFlags(67108864);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                Log.i("Abort_Calibration", "Action Bar Back has been pressed");
                if (!this.showError) {
                    if (!this.isCalibrationDone) {
                        for (int i = 0; i < 5; i++) {
                            this.handler = new Handler();
                            this.handler.postDelayed(new Runnable() {
                                public void run() {
                                    ILevelApplication access$000 = Abort_Calibration.this.application;
                                    access$000.getClass();
                                    new ILevelApplication.SendCommandToSocket("a55a0a5001240d0d21210000abcd00000000000012340000000100000000000000007b7d7b7d259bc8c9").run();
                                }
                            }, 100);
                        }
                        try {
                            Thread.sleep(1400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (this.statusTimer != null) {
                        this.statusTimer.cancel();
                        this.statusTimer.purge();
                        this.statusTimer = null;
                    }
                    Log.i("statusTimer", "Status Timer Cancelled - Action Bar Back");
                    Intent intent = new Intent(this, HomeScreen.class);
                    intent.setFlags(67108864);
                    startActivity(intent);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.application.networkThread.getSocket() == null) {
            this.application.checkWifiStatus();
        }
        Log.i("status", "Abort_Calibration onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.calibrationCompleteReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String error = intent.getStringExtra("ERROR_CODE");
                if (error.trim().length() > 0 && error.equalsIgnoreCase("00")) {
                    Abort_Calibration.access$208(Abort_Calibration.this);
                    if (Abort_Calibration.this.updateUICounter > 10) {
                        Abort_Calibration.this.updateUI();
                    }
                }
            }
        };
        if (this.statusTimer == null) {
            this.statusTimer = new Timer();
            this.statusTimer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    Log.i("statusTimer", "Send status update - onResume");
                    Abort_Calibration.this.application.sendButtonReleaseCommand();
                }
            }, 1000, 1000);
            Log.i("statusTimer", "Status Timer Created - onResume");
        }
        this.errorIndicatorReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    boolean homeScreen = false;
                    switch (intent.getIntExtra("ERROR", 1000)) {
                        case 1:
                            homeScreen = true;
                            break;
                        case 2:
                            homeScreen = true;
                            break;
                        case 7:
                            homeScreen = true;
                            break;
                        case 8:
                            homeScreen = true;
                            break;
                        case 11:
                            homeScreen = true;
                            break;
                        case 67:
                            homeScreen = true;
                            break;
                        case 68:
                            homeScreen = true;
                            break;
                        case 89:
                            homeScreen = true;
                            break;
                    }
                    if (homeScreen) {
                        Intent intent2 = new Intent(Abort_Calibration.this, HomeScreen.class);
                        intent2.putExtra("ABORT_CALIBRATION_ERROR", true);
                        intent2.setFlags(67108864);
                        Abort_Calibration.this.startActivity(intent2);
                        Abort_Calibration.this.finish();
                    }
                }
            }
        };
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Abort_Calibration.this.alertDialog == null) {
                        boolean unused = Abort_Calibration.this.showError = true;
                        Abort_Calibration.this.showErrorMessage(error);
                    } else if (Abort_Calibration.this.alertDialog != null && !Abort_Calibration.this.alertDialog.isShowing()) {
                        boolean unused2 = Abort_Calibration.this.showError = true;
                        Abort_Calibration.this.showErrorMessage(error);
                    }
                }
            }
        };
        registerReceiver(this.calibrationCompleteReceiver, new IntentFilter("com.iLevel.NoError"));
        registerReceiver(this.errorIndicatorReceiver, new IntentFilter("com.iLevel.ErrorIndicator"));
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
        if (this.statusTimer != null) {
            this.statusTimer.cancel();
            this.statusTimer.purge();
            this.statusTimer = null;
        }
        Log.i("statusTimer", "Status Timer Cancelled - onPause");
        try {
            if (this.errorDialogReceiver != null) {
                unregisterReceiver(this.errorDialogReceiver);
            }
            if (this.alertDialog != null) {
                this.alertDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.errorIndicatorReceiver != null) {
                unregisterReceiver(this.errorIndicatorReceiver);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            if (this.calibrationCompleteReceiver != null) {
                unregisterReceiver(this.calibrationCompleteReceiver);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        Log.i("System", "Abort_Calibration - onPause - isCancelled=true");
        this.application.isCancelled = true;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.statusTimer != null) {
            this.statusTimer.cancel();
            this.statusTimer.purge();
            this.statusTimer = null;
        }
        Log.i("statusTimer", "Status Timer Cancelled - onDestoy");
        try {
            if (this.errorDialogReceiver != null) {
                unregisterReceiver(this.errorDialogReceiver);
            }
            if (this.alertDialog != null) {
                this.alertDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.errorIndicatorReceiver != null) {
                unregisterReceiver(this.errorIndicatorReceiver);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            if (this.calibrationCompleteReceiver != null) {
                unregisterReceiver(this.calibrationCompleteReceiver);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            if (this.screenStateReceiver != null) {
                unregisterReceiver(this.screenStateReceiver);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    public void onClick(View view) {
        if (view == this.btnAbortCaliberation && !this.showError) {
            if (!this.isCalibrationDone) {
                for (int i = 0; i < 5; i++) {
                    this.handler = new Handler();
                    this.handler.postDelayed(new Runnable() {
                        public void run() {
                            ILevelApplication access$000 = Abort_Calibration.this.application;
                            access$000.getClass();
                            new ILevelApplication.SendCommandToSocket("a55a0a5001240d0d21210000abcd00000000000012340000000100000000000000007b7d7b7d259bc8c9").run();
                        }
                    }, 100);
                }
                try {
                    Thread.sleep(1400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (this.statusTimer != null) {
                this.statusTimer.cancel();
                this.statusTimer.purge();
                this.statusTimer = null;
            }
            Log.i("statusTimer", "Status Timer Cancelled - Abort Button Pressed");
            Intent intent = new Intent(this, HomeScreen.class);
            intent.setFlags(67108864);
            startActivity(intent);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void updateUI() {
        this.isCalibrationDone = true;
        this.txtTitle.setText(R.string.calibration_complete);
        this.txtMSG.setText(R.string.calibration_complete_msg);
        this.progressBar.setVisibility(8);
        this.btnAbortCaliberation.setText("Ok");
        this.btnAbortCaliberation.setBackgroundResource(R.drawable.red_button);
    }

    /* access modifiers changed from: private */
    public void showErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Abort_Calibration.this.alertDialog.dismiss();
                ILevelApplication access$000 = Abort_Calibration.this.application;
                access$000.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Abort_Calibration.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Abort_Calibration.this.alertDialog.dismiss();
                boolean unused = Abort_Calibration.this.showError = false;
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
                boolean unused = Abort_Calibration.this.wasScreenOn = false;
                Abort_Calibration.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                boolean unused2 = Abort_Calibration.this.wasScreenOn = true;
                Abort_Calibration.this.application.checkWifiStatus();
            }
        }
    }
}
