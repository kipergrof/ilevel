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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.adapter.Painting_Adapter_Menuscreen;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;
import java.util.ArrayList;
import java.util.List;

public class Calibration extends Activity implements AdapterView.OnItemClickListener {
    private Painting_Adapter_Menuscreen adapter;
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private BroadcastReceiver errorDialogReceiver;
    private ListView listView;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private Context myContext;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txt_Header;
    private TextView txt_calibration_details;
    private TextView txt_title;
    private Typeface typeface;
    private Typeface typefaceBold;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.calibration);
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
        this.txt_title = (TextView) findViewById(R.id.txt_title);
        this.txt_calibration_details = (TextView) findViewById(R.id.txt_calibration_details);
        this.txt_title.setTypeface(this.typefaceBold);
        this.txt_calibration_details.setTypeface(this.typeface);
        List<String> list = new ArrayList<>();
        list.add("Automatic Calibration (Suggested)");
        list.add("Manual Calibration");
        list.add("Auto Cal w/Less Than 4 Sensors");
        list.add("Man Cal w/Less Than 4 Sensors");
        this.adapter = new Painting_Adapter_Menuscreen(this, list);
        this.listView = (ListView) findViewById(R.id.new3listView1);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.application.networkThread.getSocket() == null) {
            this.application.checkWifiStatus();
        }
        Log.i("status", "Calibration onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Calibration.this.alertDialog == null) {
                        boolean unused = Calibration.this.showError = true;
                        Calibration.this.showErrorMessage(error);
                    } else if (Calibration.this.alertDialog != null && !Calibration.this.alertDialog.isShowing()) {
                        boolean unused2 = Calibration.this.showError = true;
                        Calibration.this.showErrorMessage(error);
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
        Log.i("System", "Calibration - onPause - isCancelled=true");
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.showError) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        Intent nextActivity = new Intent(this, Calibration_Next.class);
        if (position == 0) {
            nextActivity.putExtra("POSITION", position);
        } else if (position == 1) {
            nextActivity.putExtra("POSITION", position);
        } else if (position == 2) {
            nextActivity.putExtra("POSITION", position);
        } else if (position == 3) {
            nextActivity.putExtra("POSITION", position);
        }
        if (!this.showError) {
            startActivity(nextActivity);
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
                Calibration.this.alertDialog.dismiss();
                ILevelApplication access$400 = Calibration.this.application;
                access$400.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Calibration.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Calibration.this.alertDialog.dismiss();
                boolean unused = Calibration.this.showError = false;
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
                Calibration.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Calibration.this.application.checkWifiStatus();
            }
        }
    }
}
