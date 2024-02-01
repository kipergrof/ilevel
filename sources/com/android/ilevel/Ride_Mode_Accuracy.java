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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.adapter.Painting_Adapter_Ride_Accuracy;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;
import java.util.ArrayList;
import java.util.List;

public class Ride_Mode_Accuracy extends Activity implements AdapterView.OnItemClickListener {
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
    private Typeface typeface;
    private Typeface typefaceBold;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.ride_monitor_accuracy);
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
        this.lvpaintingview = (ListView) findViewById(R.id.list_view_ride_mode_accuracy);
        try {
            this.typeface = Typeface.createFromAsset(getAssets(), "fonts/Helvetica_55.ttf");
            this.typefaceBold = Typeface.createFromAsset(getAssets(), "Helvetica_55_Bold.ttf");
        } catch (Exception e) {
            Log.e("FONT", "Could not generate typeface");
        }
        this.txtTitle = (TextView) findViewById(R.id.txtTitle_RMMA);
        this.txtMsg = (TextView) findViewById(R.id.txtMSG_RMMA);
        this.txtTitle.setTypeface(this.typefaceBold);
        this.txtMsg.setTypeface(this.typeface);
        List<String> list = new ArrayList<>();
        list.add("Level 1 (15% Lower Accuracy)");
        list.add("Level 2 (Factory Setting)");
        list.add("Level 3 (8% Higher Accuracy)");
        this.adapter = new Painting_Adapter_Ride_Accuracy(this, list, this.application, false);
        this.lvpaintingview.setAdapter(this.adapter);
        this.lvpaintingview.setOnItemClickListener(this);
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
        Log.i("status", "Ride_Mode_Accuracy onResume isCancelled = false");
        this.application.isCancelled = false;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (Ride_Mode_Accuracy.this.alertDialog == null) {
                        boolean unused = Ride_Mode_Accuracy.this.showError = true;
                        Ride_Mode_Accuracy.this.showErrorMessage(error);
                    } else if (Ride_Mode_Accuracy.this.alertDialog != null && !Ride_Mode_Accuracy.this.alertDialog.isShowing()) {
                        boolean unused2 = Ride_Mode_Accuracy.this.showError = true;
                        Ride_Mode_Accuracy.this.showErrorMessage(error);
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
        Log.i("System", "Ride_Mode_Accuracy - onPause - isCancelled=true");
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
            this.application.setRide_mode_accracy(getResources().getString(R.string.one));
            command = "a55a0a5001240d0d21210000abcd00000054000012340000000000000000000000007b7d7b7d122c9397";
        } else if (position == 1) {
            this.application.setRide_mode_accracy(getResources().getString(R.string.two));
            command = "a55a0a5001240d0d21210000abcd00000055000012340000000000000000000000007b7d7b7d05ffa2ca";
        } else {
            this.application.setRide_mode_accracy(getResources().getString(R.string.three));
            command = "a55a0a5001240d0d21210000abcd00000056000012340000000000000000000000007b7d7b7d3d8af12d";
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
                Ride_Mode_Accuracy.this.alertDialog.dismiss();
                ILevelApplication access$400 = Ride_Mode_Accuracy.this.application;
                access$400.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = Ride_Mode_Accuracy.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Ride_Mode_Accuracy.this.alertDialog.dismiss();
                boolean unused = Ride_Mode_Accuracy.this.showError = false;
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
                Ride_Mode_Accuracy.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Ride_Mode_Accuracy.this.application.checkWifiStatus();
            }
        }
    }
}
