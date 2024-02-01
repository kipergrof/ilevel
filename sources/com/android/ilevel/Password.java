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

public class Password extends Activity {
    private String TAG = "Password";
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private Button btnSave;
    private BroadcastReceiver errorDialogReceiver;
    /* access modifiers changed from: private */
    public EditText etConfermPassword;
    /* access modifiers changed from: private */
    public EditText etNewPassword;
    /* access modifiers changed from: private */
    public EditText etOldPassword;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    /* access modifiers changed from: private */
    public Context myContext;
    /* access modifiers changed from: private */
    public String password;
    private BroadcastReceiver passwordReceiverStepFour;
    private BroadcastReceiver passwordReceiverStepOne;
    private BroadcastReceiver passwordReceiverStepThree;
    private BroadcastReceiver passwordReceiverStepTwo;
    /* access modifiers changed from: private */
    public ProgressBar progressBar;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txtNewPassword;
    private TextView txtOldPassword;
    private Typeface typeface;
    private Typeface typefaceBold;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.password);
        setupUI();
        if (Build.VERSION.SDK_INT >= 11) {
            this.mActionBar = getActionBar();
        }
        this.mActionBar.setDisplayShowHomeEnabled(false);
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
        this.etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        this.etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        this.etConfermPassword = (EditText) findViewById(R.id.etNewPasswordAgain);
        this.btnSave = (Button) findViewById(R.id.btnSavePassword);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBarPassword);
        this.txtOldPassword = (TextView) findViewById(R.id.txtOldPassword);
        this.txtNewPassword = (TextView) findViewById(R.id.txtNewPassword);
        this.txtOldPassword.setTypeface(this.typefaceBold);
        this.txtNewPassword.setTypeface(this.typefaceBold);
        this.btnSave.setTypeface(this.typeface);
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"InlinedApi"})
            public void onClick(View v) {
                String old = Password.this.etOldPassword.getText().toString();
                String existingPassword = Password.this.application.readPassword();
                String newPassword = Password.this.etNewPassword.getText().toString();
                String confermPassword = Password.this.etConfermPassword.getText().toString();
                if (old.trim().length() == 0) {
                    old = "0";
                }
                if (existingPassword.trim().length() > 0) {
                    if (!existingPassword.equalsIgnoreCase(old)) {
                        Toast.makeText(Password.this.myContext, "Existing password do not match with old password.", 1).show();
                    } else if (newPassword.equals(confermPassword)) {
                        Password.this.application.stopVersionRequest();
                        Intent intent = new Intent();
                        intent.setFlags(32);
                        intent.setAction("com.iLevel.stopVersionRequest");
                        intent.putExtra("stopVersionRequest", true);
                        Password.this.sendBroadcast(intent);
                        String unused = Password.this.password = newPassword;
                        Password.this.progressBar.setVisibility(0);
                    } else {
                        Toast.makeText(Password.this.myContext, "Password are not same.", 1).show();
                    }
                } else if (newPassword.equals(confermPassword)) {
                    Password.this.application.stopVersionRequest();
                    Intent intent2 = new Intent();
                    intent2.setFlags(32);
                    intent2.setAction("com.iLevel.stopVersionRequest");
                    intent2.putExtra("stopVersionRequest", true);
                    Password.this.sendBroadcast(intent2);
                    String unused2 = Password.this.password = newPassword;
                    Password.this.progressBar.setVisibility(0);
                } else {
                    Toast.makeText(Password.this.myContext, "Password are not same.", 1).show();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.application.networkThread.getSocket() == null) {
            this.application.checkWifiStatus();
        }
        Log.i("status", "Password onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Password.this.progressBar.isEnabled()) {
                        Password.this.progressBar.setVisibility(8);
                    }
                    if (Password.this.alertDialog == null) {
                        boolean unused = Password.this.showError = true;
                        Password.this.showErrorMessage(error);
                    } else if (Password.this.alertDialog != null && !Password.this.alertDialog.isShowing()) {
                        boolean unused2 = Password.this.showError = true;
                        Password.this.showErrorMessage(error);
                    }
                }
            }
        };
        this.passwordReceiverStepOne = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                if (intent.getBooleanExtra("STEP_ONE", false)) {
                    if (Password.this.password.trim().length() == 0) {
                        String unused = Password.this.password = "0";
                    }
                    Log.i("Password", "Password is :-" + Password.this.password);
                    String command = Password.this.stringToHex("set opt password " + Password.this.password);
                    Log.i("Password", "set opt password request :-" + command + "0D0A");
                    ILevelApplication access$100 = Password.this.application;
                    access$100.getClass();
                    new ILevelApplication.SendCommandToSocket(command + "0D0A").run();
                    return;
                }
                Password.this.progressBar.setVisibility(8);
                Toast.makeText(Password.this.myContext, "Password is not set.", 1).show();
            }
        };
        this.passwordReceiverStepTwo = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    boolean step_TWO = intent.getBooleanExtra("STEP_TWO", false);
                    if (step_TWO) {
                        ILevelApplication access$100 = Password.this.application;
                        access$100.getClass();
                        new ILevelApplication.SendCommandToSocket("736176650D0A").run();
                        Log.i("Password", "Request to save password :- 736176650D0A");
                    } else if (!step_TWO) {
                        Password.this.progressBar.setVisibility(8);
                        Toast.makeText(Password.this.myContext, "Password is not set.", 1).show();
                    }
                }
            }
        };
        this.passwordReceiverStepThree = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                if (intent.getBooleanExtra("STEP_THREE", false)) {
                    String command = Password.this.stringToHex("exit");
                    Log.i("Password", "exit command :-" + command + "0D0A");
                    ILevelApplication access$100 = Password.this.application;
                    access$100.getClass();
                    new ILevelApplication.SendCommandToSocket(command + "0D0A").run();
                    return;
                }
                Password.this.progressBar.setVisibility(8);
                Toast.makeText(Password.this.myContext, "Password is not set.", 1).show();
            }
        };
        this.passwordReceiverStepFour = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                if (intent.getBooleanExtra("STEP_FOUR", false)) {
                    Password.this.application.savePassword(Password.this.password);
                    Password.this.application.requestVersion();
                    Password.this.progressBar.setVisibility(8);
                    Toast.makeText(Password.this.myContext, "Password is set successfully", 1).show();
                    Log.i("Password", "Password Change Succesful");
                    Password.this.finish();
                    return;
                }
                Password.this.progressBar.setVisibility(8);
                Toast.makeText(Password.this.myContext, "Password is not set.", 1).show();
            }
        };
        registerReceiver(this.errorDialogReceiver, new IntentFilter("com.iLevel.error"));
        registerReceiver(this.passwordReceiverStepOne, new IntentFilter("com.iLevel.Password.StepOne"));
        registerReceiver(this.passwordReceiverStepTwo, new IntentFilter("com.iLevel.Password.StepTwo"));
        registerReceiver(this.passwordReceiverStepThree, new IntentFilter("com.iLevel.Password.StepThree"));
        registerReceiver(this.passwordReceiverStepFour, new IntentFilter("com.iLevel.Password.StepFour"));
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
        try {
            if (this.passwordReceiverStepOne != null) {
                unregisterReceiver(this.passwordReceiverStepOne);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            if (this.passwordReceiverStepTwo != null) {
                unregisterReceiver(this.passwordReceiverStepTwo);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            if (this.passwordReceiverStepThree != null) {
                unregisterReceiver(this.passwordReceiverStepThree);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        try {
            if (this.passwordReceiverStepFour != null) {
                unregisterReceiver(this.passwordReceiverStepFour);
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        this.application.requestVersion();
        Log.i("System", "Password - onPause - isCancelled=true");
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
            if (this.passwordReceiverStepOne != null) {
                unregisterReceiver(this.passwordReceiverStepOne);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            if (this.passwordReceiverStepTwo != null) {
                unregisterReceiver(this.passwordReceiverStepTwo);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            if (this.passwordReceiverStepThree != null) {
                unregisterReceiver(this.passwordReceiverStepThree);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        try {
            if (this.passwordReceiverStepFour != null) {
                unregisterReceiver(this.passwordReceiverStepFour);
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        try {
            if (this.screenStateReceiver != null) {
                unregisterReceiver(this.screenStateReceiver);
            }
        } catch (Exception e6) {
            e6.printStackTrace();
        }
        this.application.requestVersion();
    }

    /* access modifiers changed from: private */
    public void showErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Password.this.alertDialog.dismiss();
                ILevelApplication access$100 = Password.this.application;
                access$100.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Password.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Password.this.alertDialog.dismiss();
                boolean unused = Password.this.showError = false;
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.show();
    }

    /* access modifiers changed from: private */
    public String stringToHex(String string) {
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
                Password.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Password.this.application.checkWifiStatus();
            }
        }
    }
}
