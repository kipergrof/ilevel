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
import android.widget.ImageView;
import android.widget.TextView;
import com.android.application.ILevelApplication;
import com.android.ilevel.button.IlevelButton;
import com.ideasthatfloat.ilevel.R;

public class Save_Heights_Step_Two extends Activity implements View.OnClickListener {
    private String TAG = "Save_Heights_Step_Two";
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private Button btnBack;
    private Button btnSave;
    private IlevelButton btn_01;
    private IlevelButton btn_02;
    private IlevelButton btn_03;
    private BroadcastReceiver errorDialogReceiver;
    private ImageView led1;
    private ImageView led2;
    private ImageView led3;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private Context myContext;
    private int position = 0;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txtStep;
    private TextView txtStepMSG;
    private Typeface typeface;
    private Typeface typefaceBold;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.save_height_step_two);
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
            this.typeface = Typeface.createFromAsset(getAssets(), "fonts/Helvetica_55.ttf");
            this.typefaceBold = Typeface.createFromAsset(getAssets(), "Helvetica_55_Bold.ttf");
        } catch (Exception e) {
            Log.e("FONT", "Could not generate typeface");
        }
        this.btnSave = (Button) findViewById(R.id.btnSave_SaveHeight2);
        this.btn_01 = (IlevelButton) findViewById(R.id.btn_01_SaveHeight2);
        this.btn_02 = (IlevelButton) findViewById(R.id.btn_02_SaveHeight2);
        this.btn_03 = (IlevelButton) findViewById(R.id.btn_03_SaveHeight2);
        this.txtStep = (TextView) findViewById(R.id.txtStep_SaveHeight2);
        this.txtStepMSG = (TextView) findViewById(R.id.txtStepMSG_SaveHeight2);
        this.led1 = (ImageView) findViewById(R.id.btn_01_highlight_SaveHeight2);
        this.led2 = (ImageView) findViewById(R.id.btn_02_highlight_SaveHeight2);
        this.led3 = (ImageView) findViewById(R.id.btn_03_highlight_SaveHeight2);
        this.btnSave.setTypeface(this.typeface);
        this.txtStep.setTypeface(this.typefaceBold);
        this.txtStepMSG.setTypeface(this.typeface);
        this.btnSave.setOnClickListener(this);
        this.btn_01.setOnClickListener(this);
        this.btn_02.setOnClickListener(this);
        this.btn_03.setOnClickListener(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.showError) {
            startActivity(new Intent(this, Save_Heights.class));
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
        Log.i("status", "Save_Heights_Step_Two onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Save_Heights_Step_Two.this.alertDialog == null) {
                        Save_Heights_Step_Two.this.showErrorMessage(error);
                        boolean unused = Save_Heights_Step_Two.this.showError = true;
                    } else if (Save_Heights_Step_Two.this.alertDialog != null && !Save_Heights_Step_Two.this.alertDialog.isShowing()) {
                        Save_Heights_Step_Two.this.showErrorMessage(error);
                        boolean unused2 = Save_Heights_Step_Two.this.showError = true;
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
        Log.i("System", "Save_Heights_Step_Two - onPause - isCancelled=true");
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

    public void onClick(View view) {
        if (view == this.btnBack) {
            if (!this.showError) {
                Intent intent = new Intent(this, Save_Heights.class);
                intent.setFlags(67108864);
                startActivity(intent);
                finish();
            }
        } else if (view == this.btnSave) {
            if (!this.showError) {
                String command = "";
                switch (this.position) {
                    case 1:
                        command = "a55a0a5001240d0d21210000abcd0000002d000012340000000000000000000000007b7d7b7dd2790ca0";
                        break;
                    case 2:
                        command = "a55a0a5001240d0d21210000abcd0000002e000012340000000000000000000000007b7d7b7dea0c5f47";
                        break;
                    case 3:
                        command = "a55a0a5001240d0d21210000abcd0000002f000012340000000000000000000000007b7d7b7dfddf6e1a";
                        break;
                }
                if (this.position > 0) {
                    ILevelApplication iLevelApplication = this.application;
                    iLevelApplication.getClass();
                    new ILevelApplication.SendCommandToSocket(command).run();
                    this.application.sendButtonReleaseCommand();
                    Intent intent2 = new Intent(this, HomeScreen.class);
                    intent2.putExtra("POSITION_SAVED", this.position);
                    intent2.setFlags(67108864);
                    startActivity(intent2);
                    finish();
                }
            }
        } else if (view == this.btn_01) {
            this.position = 1;
            this.led1.setVisibility(0);
            this.led2.setVisibility(4);
            this.led3.setVisibility(4);
        } else if (view == this.btn_02) {
            this.position = 2;
            this.led1.setVisibility(4);
            this.led2.setVisibility(0);
            this.led3.setVisibility(4);
        } else if (view == this.btn_03) {
            this.position = 3;
            this.led1.setVisibility(4);
            this.led2.setVisibility(4);
            this.led3.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public void showErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Save_Heights_Step_Two.this.alertDialog.dismiss();
                ILevelApplication access$400 = Save_Heights_Step_Two.this.application;
                access$400.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Save_Heights_Step_Two.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Save_Heights_Step_Two.this.alertDialog.dismiss();
                boolean unused = Save_Heights_Step_Two.this.showError = false;
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
                Save_Heights_Step_Two.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Save_Heights_Step_Two.this.application.checkWifiStatus();
            }
        }
    }
}
