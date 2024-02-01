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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;

public class PasswordAlt extends Activity {
    private String TAG = "Password";
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private Button btnSave;
    private BroadcastReceiver errorDialogReceiver;
    private EditText etConfermPassword;
    /* access modifiers changed from: private */
    public EditText etNewPassword;
    private EditText etOldPassword;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    /* access modifiers changed from: private */
    public Context myContext;
    /* access modifiers changed from: private */
    public String password;
    private ProgressBar progressBar;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txtMSG;
    private TextView txtMSG2;
    private TextView txtTitle;
    private Typeface typeface;
    private Typeface typefaceBold;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.password_alt);
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
            this.typeface = Typeface.createFromAsset(getAssets(), "Helvetica_55.ttf");
            this.typefaceBold = Typeface.createFromAsset(getAssets(), "Helvetica_55_Bold.ttf");
        } catch (Exception e) {
            Log.d("FONT", "Could not generate typeface");
        }
        this.etNewPassword = (EditText) findViewById(R.id.etNewPassword_alt);
        this.btnSave = (Button) findViewById(R.id.btnSavePassword_alternate);
        this.txtTitle = (TextView) findViewById(R.id.txtPassword_alt);
        this.txtMSG = (TextView) findViewById(R.id.txtOldPasswordMSG_alt);
        this.txtMSG2 = (TextView) findViewById(R.id.txtOldPasswordMSG2_alt);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMSG.setTypeface(this.typeface);
        this.txtMSG2.setTypeface(this.typeface);
        this.etNewPassword.setTypeface(this.typeface);
        this.btnSave.setTypeface(this.typeface);
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"InlinedApi"})
            public void onClick(View v) {
                String unused = PasswordAlt.this.password = PasswordAlt.this.etNewPassword.getText().toString();
                if (PasswordAlt.this.password.trim().length() == 0) {
                    String unused2 = PasswordAlt.this.password = "0";
                }
                PasswordAlt.this.application.savePassword(PasswordAlt.this.password);
                Toast.makeText(PasswordAlt.this.myContext, "Password saved", 1).show();
                PasswordAlt.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.application.networkThread.getSocket() == null) {
            this.application.checkWifiStatus();
        }
        Log.i("status", "PasswordAlt onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (PasswordAlt.this.alertDialog == null) {
                        boolean unused = PasswordAlt.this.showError = true;
                        PasswordAlt.this.showErrorMessage(error);
                    } else if (PasswordAlt.this.alertDialog != null && !PasswordAlt.this.alertDialog.isShowing()) {
                        boolean unused2 = PasswordAlt.this.showError = true;
                        PasswordAlt.this.showErrorMessage(error);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.showError) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
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
        Log.i("System", "PasswordAlt - onPause - isCancelled=true");
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

    /* access modifiers changed from: private */
    public void showErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PasswordAlt.this.alertDialog.dismiss();
                ILevelApplication access$200 = PasswordAlt.this.application;
                access$200.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = PasswordAlt.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PasswordAlt.this.alertDialog.dismiss();
                boolean unused = PasswordAlt.this.showError = false;
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.show();
    }

    private String stringToHex(String string) {
        StringBuilder buf = new StringBuilder(200);
        char[] charArray = string.toCharArray();
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            buf.append(String.format("%02x", new Object[]{Integer.valueOf(charArray[i])}));
        }
        return buf.toString();
    }

    private class ScreenStateReceiver extends BroadcastReceiver {
        private ScreenStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                PasswordAlt.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                PasswordAlt.this.application.checkWifiStatus();
            }
        }
    }
}
