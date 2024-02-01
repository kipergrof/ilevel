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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.adapter.Painting_Adapter_Ride_Accuracy;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;
import java.util.ArrayList;
import java.util.List;

public class Tank_Pressure_Mode extends Activity implements AdapterView.OnItemClickListener {
    private Painting_Adapter_Ride_Accuracy adapter;
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    private BroadcastReceiver errorDialogReceiver;
    private List<View> img_tick_views;
    private ListView lvpaintingview;
    @SuppressLint({"NewApi"})
    ActionBar mActionBar;
    private Context myContext;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    private TextView txtMsg;
    private TextView txtTitle;
    private TextView txtWarning;
    private Typeface typeface;
    private Typeface typefaceBold;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.tank_pressure_mode);
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
        this.lvpaintingview = (ListView) findViewById(R.id.list_view_tank_pressure);
        try {
            this.typeface = Typeface.createFromAsset(getAssets(), "fonts/Helvetica_55.ttf");
            this.typefaceBold = Typeface.createFromAsset(getAssets(), "Helvetica_55_Bold.ttf");
        } catch (Exception e) {
            Log.e("FONT", "Could not generate typeface");
        }
        this.txtTitle = (TextView) findViewById(R.id.txtTitle_TankPressure);
        this.txtMsg = (TextView) findViewById(R.id.txtMSG_TankPressure);
        this.txtWarning = (TextView) findViewById(R.id.txtWarning_TankPressure);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMsg.setText(Html.fromHtml("Your system was shipped with the <b><I>Tank Pressure Mode</I></b> set at <b>150 PSI</b>.You can change this setting below:"));
        this.txtMsg.setTypeface(this.typeface);
        this.txtWarning.setTypeface(this.typefaceBold);
        List<String> list = new ArrayList<>();
        list.add("150 Mode (110psi ON/150psi OFF)");
        list.add("175 Mode(135psi ON/175psi OFF)");
        list.add("200 Mode (160psi ON/200psi OFF)");
        this.adapter = new Painting_Adapter_Ride_Accuracy(this, list, this.application, true);
        this.lvpaintingview.setAdapter(this.adapter);
        this.lvpaintingview.setOnItemClickListener(this);
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
        Log.i("status", "Tank_Pressure_Mode onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Tank_Pressure_Mode.this.alertDialog == null) {
                        boolean unused = Tank_Pressure_Mode.this.showError = true;
                        Tank_Pressure_Mode.this.showErrorMessage(error);
                    } else if (Tank_Pressure_Mode.this.alertDialog != null && !Tank_Pressure_Mode.this.alertDialog.isShowing()) {
                        boolean unused2 = Tank_Pressure_Mode.this.showError = true;
                        Tank_Pressure_Mode.this.showErrorMessage(error);
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
        Log.i("System", "Tank_Pressure_Mode - onPause - isCancelled=true");
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

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
        String command;
        this.img_tick_views = this.adapter.getListOfView();
        if (this.img_tick_views != null) {
            for (int pos = 0; pos < this.img_tick_views.size(); pos++) {
                ((ImageView) this.img_tick_views.get(pos)).setVisibility(8);
            }
        }
        ((ImageView) view.findViewById(R.id.imageView1)).setVisibility(0);
        if (position == 0) {
            this.application.setTank_pressure_mode(getResources().getString(R.string.onefivezero));
            command = "a55a0a5001240d0d21210000abcd00000076000012340000000000000000000000007b7d7b7dce6ee1e3";
        } else if (position == 1) {
            this.application.setTank_pressure_mode(getResources().getString(R.string.onesevenfive));
            command = "a55a0a5001240d0d21210000abcd00000077000012340000000000000000000000007b7d7b7dd9bdd0be";
        } else {
            this.application.setTank_pressure_mode(getResources().getString(R.string.twohundred));
            command = "a55a0a5001240d0d21210000abcd00000078000012340000000000000000000000007b7d7b7d001dccc5";
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
                Tank_Pressure_Mode.this.alertDialog.dismiss();
                ILevelApplication access$400 = Tank_Pressure_Mode.this.application;
                access$400.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Tank_Pressure_Mode.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Tank_Pressure_Mode.this.alertDialog.dismiss();
                boolean unused = Tank_Pressure_Mode.this.showError = false;
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
                Tank_Pressure_Mode.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Tank_Pressure_Mode.this.application.checkWifiStatus();
            }
        }
    }
}
