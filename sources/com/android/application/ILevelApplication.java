package com.android.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import com.android.application.NetworkHandler;
import com.android.ilevel.FirmwareUpdate;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ILevelApplication extends Application {
    public static String SERVERIP = "1.2.3.4";
    public static final int SERVERPORT = 2000;
    /* access modifiers changed from: private */
    public static String TAG = "ILevelApplication";
    private static final int TIMEOUT = 500;
    public static boolean VERSION_INFO = false;
    public static ILevelApplication application;
    public static Socket clientSocket;
    private static Timer timerForClient;
    /* access modifiers changed from: private */
    public static Timer timerForVersionRequest;
    private static int ver1;
    private static int ver2;
    private static int version1 = 0;
    private static int version2 = 0;
    /* access modifiers changed from: private */
    public boolean AOK_PASS = false;
    private boolean AOK_SET_OPT_PASSWORD = false;
    public boolean IS_REGULAR_UPDATE = false;
    private int Old_Comp_Range_Bit;
    public boolean STOP_VERSION_REQUEST = false;
    public String appVersion;
    private ConnectivityManager connectivityManager;
    private int counter = 0;
    public String firmwareVersion;
    /* access modifiers changed from: private */
    public iLevelIgnitionCounter iLevelIgnitionCounter;
    private iLevelVersionCounter iLevelVersionCounter;
    private BufferedInputStream inputFromServer;
    public boolean isCancelled = false;
    public boolean isIgnitionOn;
    public View mainView;
    public NetworkHandler networkHandler;
    public NetworkHandler.NetworkInterface networkInterface = new NetworkHandler.NetworkInterface() {
        public void TcpNetworkConnected(int arg2) {
        }

        public void TcpNetworkCallback(Bundle bundle) {
            ILevelApplication.this.processData(bundle.getByteArray(NetworkHandler.NETWORK_KEY));
        }

        public void TcpNetworkClosed(int id) {
        }
    };
    public NetworkThread networkThread;
    private String ride_height_on_start;
    private String ride_mode_accracy;
    private String ride_monitor_mode;
    private String tank_pressure_mode;
    private String trim_mode;
    private BroadcastReceiver versionRequestReceiver;
    private WifiManager wifiManager;

    public void onCreate() {
        super.onCreate();
        application = this;
        this.networkHandler = new NetworkHandler(this.networkInterface);
        this.networkThread = new NetworkThread(this.networkHandler, (Socket) null);
        this.wifiManager = (WifiManager) getSystemService("wifi");
        this.connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        timerForClient = new Timer();
        this.versionRequestReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    ILevelApplication.this.STOP_VERSION_REQUEST = intent.getBooleanExtra("stopVersionRequest", false);
                    ILevelApplication.this.stopVersionRequest();
                    ILevelApplication iLevelApplication = ILevelApplication.application;
                    iLevelApplication.getClass();
                    new SendCommandToSocket("2424240D0A").run();
                    ILevelApplication.this.stopVersionRequest();
                    Log.i(ILevelApplication.TAG, "Enter in Command mode $$$  :- 2424240D0A");
                }
            }
        };
        registerReceiver(this.versionRequestReceiver, new IntentFilter("com.iLevel.stopVersionRequest"));
        this.iLevelIgnitionCounter = new iLevelIgnitionCounter(5000, 2500);
        this.iLevelIgnitionCounter.start();
        this.iLevelVersionCounter = new iLevelVersionCounter(30000, 1000);
        this.iLevelVersionCounter.start();
        try {
            this.appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.v(TAG, e.getMessage());
        }
        if (this.appVersion == null) {
            this.appVersion = "N/A";
        }
        if (this.firmwareVersion == null) {
            this.firmwareVersion = "N/A";
        }
    }

    public void onTerminate() {
        super.onTerminate();
        stopTimerTask();
    }

    public void updateFirmware(View view) {
        new FirmwareUpdate();
        FirmwareUpdate.updateFirmware(view);
    }

    @SuppressLint({"InlinedApi"})
    public void checkWifiStatus() {
        boolean isConnected;
        if (this.wifiManager.isWifiEnabled()) {
            isConnected = isWiFiConnected();
        } else {
            startWiFi();
            isConnected = isWiFiConnected();
        }
        if (!isConnected) {
            Intent intent = new Intent();
            intent.setAction("com.iLevel.NETWORK_ERROR");
            intent.setFlags(32);
            sendBroadcast(intent);
        }
    }

    private boolean isWiFiConnected() {
        if (!this.connectivityManager.getNetworkInfo(1).isConnected()) {
            return false;
        }
        new OpenSocketConnection().execute(new String[0]);
        return true;
    }

    private void startWiFi() {
        this.wifiManager.setWifiEnabled(true);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRide_height_on_start() {
        return this.ride_height_on_start;
    }

    public void setRide_height_on_start(String ride_height_on_start2) {
        this.ride_height_on_start = ride_height_on_start2;
    }

    public String getRide_monitor_mode() {
        return this.ride_monitor_mode;
    }

    public void setRide_monitor_mode(String ride_monitor_mode2) {
        this.ride_monitor_mode = ride_monitor_mode2;
    }

    public String getRide_mode_accracy() {
        return this.ride_mode_accracy;
    }

    public void setRide_mode_accracy(String ride_mode_accracy2) {
        this.ride_mode_accracy = ride_mode_accracy2;
    }

    public String getTrim_mode() {
        return this.trim_mode;
    }

    public void setTrim_mode(String trim_mode2) {
        this.trim_mode = trim_mode2;
    }

    public String getTank_pressure_mode() {
        return this.tank_pressure_mode;
    }

    public void setTank_pressure_mode(String tank_pressure_mode2) {
        this.tank_pressure_mode = tank_pressure_mode2;
    }

    public class OpenSocketConnection extends AsyncTask<String, String, String> {
        boolean IS_EXCEPTION = false;
        boolean connected = false;

        public OpenSocketConnection() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... params) {
            Log.i("bg", "OpenSocketConnection - do in background  - isCancelled=" + ILevelApplication.this.isCancelled);
            if (ILevelApplication.this.isCancelled) {
                ILevelApplication.this.stopVersionRequest();
                ILevelApplication.this.stopTimerTask();
            } else {
                ILevelApplication.this.networkThread.startConnection(ILevelApplication.SERVERIP, (int) ILevelApplication.SERVERPORT);
            }
            return "";
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            Log.i("System", "ILevelApplication - onPostExecute");
            if (this.IS_EXCEPTION) {
                ILevelApplication.this.showException(result);
            } else if (ILevelApplication.this.isCancelled) {
                ILevelApplication.this.stopVersionRequest();
                ILevelApplication.this.stopTimerTask();
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!ILevelApplication.this.AOK_PASS && ILevelApplication.timerForVersionRequest == null) {
                    ILevelApplication.this.requestVersion();
                }
            }
        }
    }

    public void stopTimerTask() {
        if (timerForClient != null) {
            timerForClient.cancel();
            timerForClient = null;
        }
        if (timerForVersionRequest != null) {
            timerForVersionRequest.cancel();
            timerForVersionRequest = null;
        }
        if (this.iLevelIgnitionCounter != null) {
            this.iLevelIgnitionCounter.cancel();
        }
        if (this.iLevelVersionCounter != null) {
            this.iLevelVersionCounter.cancel();
        }
    }

    public void requestVersion() {
        Log.i("Version", "Application.requestVersion - timerForVersionRequest = " + timerForVersionRequest);
        if (timerForVersionRequest == null) {
            timerForVersionRequest = new Timer();
            Log.i("Version", "timerForVersionRequest new Timer");
        }
        timerForVersionRequest.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Log.i("Version", "timer Task - Stop_Version_Request = " + ILevelApplication.this.STOP_VERSION_REQUEST);
                if (!ILevelApplication.this.STOP_VERSION_REQUEST) {
                    new IsDeviceConnected().run();
                }
            }
        }, 0, 1000);
    }

    public void stopVersionRequest() {
        if (timerForVersionRequest != null) {
            timerForVersionRequest.cancel();
            timerForVersionRequest.purge();
            timerForVersionRequest = null;
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint({"InlinedApi"})
    public void changeHomeScreen() {
        if (!this.IS_REGULAR_UPDATE) {
            Intent intent = new Intent();
            intent.setFlags(32);
            intent.setAction("com.iLevel.ScreenToShow");
            intent.putExtra("IGNITION", false);
            sendBroadcast(intent);
        } else if (this.IS_REGULAR_UPDATE) {
            Intent intent2 = new Intent();
            intent2.setFlags(32);
            intent2.setAction("com.iLevel.ScreenToShow");
            intent2.putExtra("IGNITION", true);
            sendBroadcast(intent2);
        }
    }

    public class IsDeviceConnected implements Runnable {
        public IsDeviceConnected() {
        }

        public void run() {
            try {
                if (!ILevelApplication.this.STOP_VERSION_REQUEST && ILevelApplication.this.networkThread.getSocket() != null && ILevelApplication.this.networkThread.isConnected()) {
                    ILevelApplication.this.networkThread.sendData(new BigInteger("A55A0A50080180", 16).toByteArray());
                }
            } catch (Exception e) {
                e.printStackTrace();
                ILevelApplication.this.showException(e.getMessage());
            }
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r5v14, types: [int, byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r5v16, types: [int, byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r5v23, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r5v4, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r5v9, types: [byte] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void processData(byte[] r21) {
        /*
            r20 = this;
            r17 = 0
            byte r17 = r21[r17]
            r0 = r17
            r8 = r0 & 255(0xff, float:3.57E-43)
            java.lang.String r17 = TAG
            java.lang.String r18 = java.util.Arrays.toString(r21)
            android.util.Log.d(r17, r18)
            r0 = r21
            int r0 = r0.length
            r17 = r0
            r18 = 5
            r0 = r17
            r1 = r18
            if (r0 <= r1) goto L_0x004f
            r17 = 165(0xa5, float:2.31E-43)
            r0 = r17
            if (r8 != r0) goto L_0x0186
            r17 = 1
            byte r17 = r21[r17]
            r18 = 90
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x004f
            r17 = 2
            byte r17 = r21[r17]
            r18 = 10
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x004f
            r17 = 3
            byte r17 = r21[r17]
            r18 = 80
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x004f
            r17 = 4
            byte r17 = r21[r17]     // Catch:{ Exception -> 0x0058 }
            switch(r17) {
                case 1: goto L_0x0050;
                case 2: goto L_0x0063;
                case 5: goto L_0x00e2;
                case 6: goto L_0x00eb;
                case 8: goto L_0x00f4;
                case 100: goto L_0x017c;
                case 101: goto L_0x0181;
                default: goto L_0x004f;
            }     // Catch:{ Exception -> 0x0058 }
        L_0x004f:
            return
        L_0x0050:
            java.lang.String r17 = TAG     // Catch:{ Exception -> 0x0058 }
            java.lang.String r18 = "COMMAND_KP_DATA"
            android.util.Log.i(r17, r18)     // Catch:{ Exception -> 0x0058 }
            goto L_0x004f
        L_0x0058:
            r6 = move-exception
            java.lang.String r17 = TAG
            java.lang.String r18 = r6.toString()
            android.util.Log.d(r17, r18)
            goto L_0x004f
        L_0x0063:
            java.lang.String r17 = TAG     // Catch:{ Exception -> 0x0058 }
            java.lang.String r18 = "COMMAND_ECU_DATA"
            android.util.Log.i(r17, r18)     // Catch:{ Exception -> 0x0058 }
            boolean r17 = com.android.ilevel.HomeScreen.IGNITION_OFF_COMMAND     // Catch:{ Exception -> 0x0058 }
            if (r17 != 0) goto L_0x00c7
            r0 = r20
            com.android.application.ILevelApplication$iLevelIgnitionCounter r0 = r0.iLevelIgnitionCounter     // Catch:{ Exception -> 0x0058 }
            r17 = r0
            r17.cancel()     // Catch:{ Exception -> 0x0058 }
            r0 = r20
            com.android.application.ILevelApplication$iLevelIgnitionCounter r0 = r0.iLevelIgnitionCounter     // Catch:{ Exception -> 0x0058 }
            r17 = r0
            r17.start()     // Catch:{ Exception -> 0x0058 }
            r17 = 1
            r0 = r17
            r1 = r20
            r1.IS_REGULAR_UPDATE = r0     // Catch:{ Exception -> 0x0058 }
            r20.changeHomeScreen()     // Catch:{ Exception -> 0x0058 }
        L_0x008b:
            r5 = 0
            r17 = 5
            byte r12 = r21[r17]     // Catch:{ Exception -> 0x0058 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0058 }
            r13.<init>()     // Catch:{ Exception -> 0x0058 }
            r11 = 6
            r9 = 0
        L_0x0097:
            if (r9 >= r12) goto L_0x00db
            byte r5 = r21[r11]     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00d0 }
        L_0x009b:
            int r11 = r11 + 1
            r17 = 16
            r0 = r17
            if (r5 >= r0) goto L_0x00aa
            java.lang.String r17 = "0"
            r0 = r17
            r13.append(r0)     // Catch:{ Exception -> 0x0058 }
        L_0x00aa:
            java.lang.String r17 = java.lang.Integer.toHexString(r5)     // Catch:{ Exception -> 0x0058 }
            r0 = r17
            r13.append(r0)     // Catch:{ Exception -> 0x0058 }
            r17 = r9 & 3
            r18 = 3
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x00c4
            java.lang.String r17 = " "
            r0 = r17
            r13.append(r0)     // Catch:{ Exception -> 0x0058 }
        L_0x00c4:
            int r9 = r9 + 1
            goto L_0x0097
        L_0x00c7:
            r17 = 0
            r0 = r17
            r1 = r20
            r1.IS_REGULAR_UPDATE = r0     // Catch:{ Exception -> 0x0058 }
            goto L_0x008b
        L_0x00d0:
            r6 = move-exception
            java.lang.String r17 = TAG     // Catch:{ Exception -> 0x0058 }
            java.lang.String r18 = r6.toString()     // Catch:{ Exception -> 0x0058 }
            android.util.Log.d(r17, r18)     // Catch:{ Exception -> 0x0058 }
            goto L_0x009b
        L_0x00db:
            r0 = r20
            r0.setUIForHomeScreen(r13)     // Catch:{ Exception -> 0x0058 }
            goto L_0x004f
        L_0x00e2:
            java.lang.String r17 = TAG     // Catch:{ Exception -> 0x0058 }
            java.lang.String r18 = "COMMAND_PROG_DATA"
            android.util.Log.i(r17, r18)     // Catch:{ Exception -> 0x0058 }
            goto L_0x004f
        L_0x00eb:
            java.lang.String r17 = TAG     // Catch:{ Exception -> 0x0058 }
            java.lang.String r18 = "COMMAND_PROG_CRC"
            android.util.Log.i(r17, r18)     // Catch:{ Exception -> 0x0058 }
            goto L_0x004f
        L_0x00f4:
            r17 = 1
            VERSION_INFO = r17     // Catch:{ Exception -> 0x0058 }
            r17 = 6
            byte r17 = r21[r17]     // Catch:{ ArrayIndexOutOfBoundsException -> 0x016d }
            version1 = r17     // Catch:{ ArrayIndexOutOfBoundsException -> 0x016d }
        L_0x00fe:
            int r17 = version1     // Catch:{ Exception -> 0x0058 }
            java.lang.String r17 = java.lang.Integer.toString(r17)     // Catch:{ Exception -> 0x0058 }
            r0 = r17
            r1 = r20
            r1.firmwareVersion = r0     // Catch:{ Exception -> 0x0058 }
            java.lang.String r17 = TAG     // Catch:{ Exception -> 0x0058 }
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0058 }
            r18.<init>()     // Catch:{ Exception -> 0x0058 }
            java.lang.String r19 = "ClientThread Version: "
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x0058 }
            r0 = r20
            java.lang.String r0 = r0.firmwareVersion     // Catch:{ Exception -> 0x0058 }
            r19 = r0
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x0058 }
            java.lang.String r18 = r18.toString()     // Catch:{ Exception -> 0x0058 }
            android.util.Log.i(r17, r18)     // Catch:{ Exception -> 0x0058 }
            r0 = r20
            android.view.View r0 = r0.mainView     // Catch:{ Exception -> 0x0058 }
            r17 = r0
            if (r17 == 0) goto L_0x0156
            int r17 = version1     // Catch:{ Exception -> 0x0058 }
            if (r17 == 0) goto L_0x0156
            int r17 = version1     // Catch:{ Exception -> 0x0058 }
            r18 = 11
            r0 = r17
            r1 = r18
            if (r0 >= r1) goto L_0x0178
            r0 = r20
            android.view.View r0 = r0.mainView     // Catch:{ Exception -> 0x0058 }
            r17 = r0
            android.content.Context r17 = r17.getContext()     // Catch:{ Exception -> 0x0058 }
            android.app.Activity r17 = (android.app.Activity) r17     // Catch:{ Exception -> 0x0058 }
            com.android.application.ILevelApplication$4 r18 = new com.android.application.ILevelApplication$4     // Catch:{ Exception -> 0x0058 }
            r0 = r18
            r1 = r20
            r0.<init>()     // Catch:{ Exception -> 0x0058 }
            r17.runOnUiThread(r18)     // Catch:{ Exception -> 0x0058 }
        L_0x0156:
            r0 = r20
            com.android.application.ILevelApplication$iLevelVersionCounter r0 = r0.iLevelVersionCounter     // Catch:{ Exception -> 0x0058 }
            r17 = r0
            r17.cancel()     // Catch:{ Exception -> 0x0058 }
            r0 = r20
            com.android.application.ILevelApplication$iLevelVersionCounter r0 = r0.iLevelVersionCounter     // Catch:{ Exception -> 0x0058 }
            r17 = r0
            r17.start()     // Catch:{ Exception -> 0x0058 }
            r20.changeHomeScreen()     // Catch:{ Exception -> 0x0058 }
            goto L_0x004f
        L_0x016d:
            r6 = move-exception
            java.lang.String r17 = TAG     // Catch:{ Exception -> 0x0058 }
            java.lang.String r18 = r6.toString()     // Catch:{ Exception -> 0x0058 }
            android.util.Log.d(r17, r18)     // Catch:{ Exception -> 0x0058 }
            goto L_0x00fe
        L_0x0178:
            com.android.ilevel.FirmwareUpdate.disableFirmwareCheck()     // Catch:{ Exception -> 0x0058 }
            goto L_0x0156
        L_0x017c:
            com.android.ilevel.FirmwareUpdate.ackReceived()     // Catch:{ Exception -> 0x0058 }
            goto L_0x004f
        L_0x0181:
            com.android.ilevel.FirmwareUpdate.nakReceived()     // Catch:{ Exception -> 0x0058 }
            goto L_0x004f
        L_0x0186:
            r17 = 67
            r0 = r17
            if (r8 != r0) goto L_0x023c
            r5 = 0
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r17 = java.lang.Integer.toHexString(r8)
            r0 = r17
            r13.append(r0)
            r11 = 1
            r9 = 0
        L_0x019d:
            r17 = 4
            r0 = r17
            if (r9 >= r0) goto L_0x01d7
            byte r5 = r21[r11]
            int r11 = r11 + 1
            r17 = -1
            r0 = r17
            if (r5 == r0) goto L_0x01d7
            r17 = 16
            r0 = r17
            if (r5 >= r0) goto L_0x01ba
            java.lang.String r17 = "0"
            r0 = r17
            r13.append(r0)
        L_0x01ba:
            java.lang.String r17 = java.lang.Integer.toHexString(r5)
            r0 = r17
            r13.append(r0)
            r17 = r9 & 3
            r18 = 3
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x01d4
            java.lang.String r17 = " "
            r0 = r17
            r13.append(r0)
        L_0x01d4:
            int r9 = r9 + 1
            goto L_0x019d
        L_0x01d7:
            java.lang.String r3 = r13.toString()
            java.lang.String r17 = " "
            java.lang.String r18 = ""
            r0 = r17
            r1 = r18
            java.lang.String r3 = r3.replace(r0, r1)
            android.content.Intent r10 = new android.content.Intent
            r10.<init>()
            r17 = 32
            r0 = r17
            r10.setFlags(r0)
            java.lang.String r17 = "com.iLevel.Password.StepOne"
            r0 = r17
            r10.setAction(r0)
            java.lang.String r17 = "434d440d0a"
            r0 = r17
            boolean r17 = r3.contentEquals(r0)
            if (r17 == 0) goto L_0x0230
            java.lang.String r17 = "STEP_ONE"
            r18 = 1
            r0 = r17
            r1 = r18
            r10.putExtra(r0, r1)
        L_0x020f:
            java.lang.String r17 = TAG
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "RX data response for command mode :-"
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r13)
            java.lang.String r18 = r18.toString()
            android.util.Log.i(r17, r18)
            r0 = r20
            r0.sendBroadcast(r10)
            goto L_0x004f
        L_0x0230:
            java.lang.String r17 = "STEP_ONE"
            r18 = 0
            r0 = r17
            r1 = r18
            r10.putExtra(r0, r1)
            goto L_0x020f
        L_0x023c:
            r17 = 115(0x73, float:1.61E-43)
            r0 = r17
            if (r8 != r0) goto L_0x03b4
            r17 = 4
            byte r17 = r21[r17]
            r18 = 111(0x6f, float:1.56E-43)
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x03b4
            r17 = 1
            r0 = r17
            r1 = r20
            r1.AOK_SET_OPT_PASSWORD = r0
            r5 = 0
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r17 = java.lang.Integer.toHexString(r8)
            r0 = r17
            r13.append(r0)
            r0 = r20
            int r0 = r0.counter
            r17 = r0
            if (r17 != 0) goto L_0x0311
            r0 = r20
            int r0 = r0.counter
            r17 = r0
            int r17 = r17 + 1
            r0 = r17
            r1 = r20
            r1.counter = r0
            r11 = 1
        L_0x027c:
            r17 = -1
            r0 = r17
            if (r5 == r0) goto L_0x02c8
            byte r5 = r21[r11]
            int r11 = r11 + 1
            r17 = -1
            r0 = r17
            if (r5 == r0) goto L_0x027c
            r17 = 16
            r0 = r17
            if (r5 >= r0) goto L_0x0299
            java.lang.String r17 = "0"
            r0 = r17
            r13.append(r0)
        L_0x0299:
            java.lang.String r17 = java.lang.Integer.toHexString(r5)
            r0 = r17
            r13.append(r0)
            java.lang.String r17 = TAG
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "Reply :-"
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r13)
            java.lang.String r18 = r18.toString()
            android.util.Log.i(r17, r18)
            java.lang.String r17 = r13.toString()
            java.lang.String r18 = "414f4b"
            boolean r17 = r17.contains(r18)
            if (r17 == 0) goto L_0x027c
        L_0x02c8:
            java.lang.String r17 = TAG
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "RX data response for set opt password or Save :-"
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r13)
            java.lang.String r18 = r18.toString()
            android.util.Log.i(r17, r18)
            java.lang.String r17 = r13.toString()
            java.lang.String r18 = "0d0a"
            java.lang.String[] r16 = r17.split(r18)
            r14 = 0
            r2 = 0
            r9 = 0
        L_0x02ef:
            r0 = r16
            int r0 = r0.length
            r17 = r0
            r0 = r17
            if (r9 >= r0) goto L_0x0368
            r17 = r16[r9]
            java.lang.String r18 = "73617665"
            boolean r17 = r17.equalsIgnoreCase(r18)
            if (r17 == 0) goto L_0x0303
            r14 = 1
        L_0x0303:
            r17 = r16[r9]
            java.lang.String r18 = "414f4b"
            boolean r17 = r17.equalsIgnoreCase(r18)
            if (r17 == 0) goto L_0x030e
            r2 = 1
        L_0x030e:
            int r9 = r9 + 1
            goto L_0x02ef
        L_0x0311:
            r11 = 1
        L_0x0312:
            r17 = -1
            r0 = r17
            if (r5 == r0) goto L_0x035e
            byte r5 = r21[r11]
            int r11 = r11 + 1
            java.lang.String r17 = TAG
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "Rx response data :-----"
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r5)
            java.lang.String r18 = r18.toString()
            android.util.Log.i(r17, r18)
            r17 = -1
            r0 = r17
            if (r5 == r0) goto L_0x0352
            r17 = 16
            r0 = r17
            if (r5 >= r0) goto L_0x0349
            java.lang.String r17 = "0"
            r0 = r17
            r13.append(r0)
        L_0x0349:
            java.lang.String r17 = java.lang.Integer.toHexString(r5)
            r0 = r17
            r13.append(r0)
        L_0x0352:
            java.lang.String r17 = r13.toString()
            java.lang.String r18 = "636f6e666967"
            boolean r17 = r17.contains(r18)
            if (r17 == 0) goto L_0x0312
        L_0x035e:
            r17 = 0
            r0 = r17
            r1 = r20
            r1.counter = r0
            goto L_0x02c8
        L_0x0368:
            if (r2 == 0) goto L_0x038d
            android.content.Intent r10 = new android.content.Intent
            r10.<init>()
            r17 = 32
            r0 = r17
            r10.setFlags(r0)
            java.lang.String r17 = "com.iLevel.Password.StepTwo"
            r0 = r17
            r10.setAction(r0)
            java.lang.String r17 = "STEP_TWO"
            r18 = 1
            r0 = r17
            r1 = r18
            r10.putExtra(r0, r1)
            r0 = r20
            r0.sendBroadcast(r10)
        L_0x038d:
            if (r14 == 0) goto L_0x004f
            android.content.Intent r10 = new android.content.Intent
            r10.<init>()
            r17 = 32
            r0 = r17
            r10.setFlags(r0)
            java.lang.String r17 = "com.iLevel.Password.StepThree"
            r0 = r17
            r10.setAction(r0)
            java.lang.String r17 = "STEP_THREE"
            r18 = 1
            r0 = r17
            r1 = r18
            r10.putExtra(r0, r1)
            r0 = r20
            r0.sendBroadcast(r10)
            goto L_0x004f
        L_0x03b4:
            r17 = 101(0x65, float:1.42E-43)
            r0 = r17
            if (r8 != r0) goto L_0x045d
            r5 = r8
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r17 = java.lang.Integer.toHexString(r8)
            r0 = r17
            r13.append(r0)
            r11 = 1
            r9 = 0
        L_0x03cb:
            r17 = 15
            r0 = r17
            if (r9 >= r0) goto L_0x0402
            byte r5 = r21[r11]
            r0 = r21
            int r0 = r0.length
            r17 = r0
            int r17 = r17 + -1
            r0 = r17
            if (r11 >= r0) goto L_0x03e0
            int r11 = r11 + 1
        L_0x03e0:
            r17 = 16
            r0 = r17
            if (r5 >= r0) goto L_0x03ed
            java.lang.String r17 = "0"
            r0 = r17
            r13.append(r0)
        L_0x03ed:
            java.lang.String r17 = java.lang.Integer.toHexString(r5)
            r0 = r17
            r13.append(r0)
            java.lang.String r17 = r13.toString()
            java.lang.String r18 = "45584954"
            boolean r17 = r17.contains(r18)
            if (r17 == 0) goto L_0x044d
        L_0x0402:
            android.content.Intent r10 = new android.content.Intent
            r10.<init>()
            r17 = 32
            r0 = r17
            r10.setFlags(r0)
            java.lang.String r17 = "com.iLevel.Password.StepFour"
            r0 = r17
            r10.setAction(r0)
            java.lang.String r17 = r13.toString()
            java.lang.String r18 = "45584954"
            boolean r17 = r17.contains(r18)
            if (r17 == 0) goto L_0x0451
            java.lang.String r17 = "STEP_FOUR"
            r18 = 1
            r0 = r17
            r1 = r18
            r10.putExtra(r0, r1)
        L_0x042c:
            java.lang.String r17 = TAG
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "RX data for exit :-"
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r13)
            java.lang.String r18 = r18.toString()
            android.util.Log.i(r17, r18)
            r0 = r20
            r0.sendBroadcast(r10)
            goto L_0x004f
        L_0x044d:
            int r9 = r9 + 1
            goto L_0x03cb
        L_0x0451:
            java.lang.String r17 = "STEP_FOUR"
            r18 = 0
            r0 = r17
            r1 = r18
            r10.putExtra(r0, r1)
            goto L_0x042c
        L_0x045d:
            r17 = 80
            r0 = r17
            if (r8 != r0) goto L_0x0524
            r17 = 1
            r0 = r17
            r1 = r20
            r1.AOK_PASS = r0
            r5 = r8
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r17 = java.lang.Integer.toHexString(r8)
            r0 = r17
            r13.append(r0)
            r11 = 1
            r9 = 0
        L_0x047c:
            r17 = 4
            r0 = r17
            if (r9 >= r0) goto L_0x04aa
            byte r5 = r21[r11]     // Catch:{ ArrayIndexOutOfBoundsException -> 0x049f }
        L_0x0484:
            int r11 = r11 + 1
            r17 = 16
            r0 = r17
            if (r5 >= r0) goto L_0x0493
            java.lang.String r17 = "0"
            r0 = r17
            r13.append(r0)
        L_0x0493:
            java.lang.String r17 = java.lang.Integer.toHexString(r5)
            r0 = r17
            r13.append(r0)
            int r9 = r9 + 1
            goto L_0x047c
        L_0x049f:
            r6 = move-exception
            java.lang.String r17 = TAG
            java.lang.String r18 = r6.toString()
            android.util.Log.d(r17, r18)
            goto L_0x0484
        L_0x04aa:
            java.lang.String r17 = TAG
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "RX data for password :-"
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r13)
            java.lang.String r18 = r18.toString()
            android.util.Log.i(r17, r18)
            java.lang.String r17 = r13.toString()
            java.lang.String r18 = "504153533f"
            boolean r17 = r17.equalsIgnoreCase(r18)
            if (r17 == 0) goto L_0x004f
            java.lang.String r4 = r20.readPassword()
            java.lang.String r17 = r4.trim()
            int r17 = r17.length()
            if (r17 <= 0) goto L_0x0507
            r0 = r20
            java.lang.String r7 = r0.stringToHex(r4)
            com.android.application.ILevelApplication$SendCommandToSocket r15 = new com.android.application.ILevelApplication$SendCommandToSocket
            java.lang.StringBuilder r17 = new java.lang.StringBuilder
            r17.<init>()
            r0 = r17
            java.lang.StringBuilder r17 = r0.append(r7)
            java.lang.String r18 = "0D0A"
            java.lang.StringBuilder r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r20
            r1 = r17
            r15.<init>(r1)
            r15.run()
            goto L_0x004f
        L_0x0507:
            r20.stopTimerTask()
            android.content.Intent r10 = new android.content.Intent
            r10.<init>()
            r17 = 32
            r0 = r17
            r10.setFlags(r0)
            java.lang.String r17 = "com.iLevel.Password.Update"
            r0 = r17
            r10.setAction(r0)
            r0 = r20
            r0.sendBroadcast(r10)
            goto L_0x004f
        L_0x0524:
            r17 = 65
            r0 = r17
            if (r8 != r0) goto L_0x05ef
            r5 = r8
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r17 = java.lang.Integer.toHexString(r5)
            r0 = r17
            r13.append(r0)
            r11 = 1
            r9 = 0
        L_0x053b:
            r17 = 2
            r0 = r17
            if (r9 >= r0) goto L_0x056f
            byte r5 = r21[r11]     // Catch:{ ArrayIndexOutOfBoundsException -> 0x0564 }
        L_0x0543:
            int r11 = r11 + 1
            r17 = -1
            r0 = r17
            if (r5 == r0) goto L_0x056f
            r17 = 16
            r0 = r17
            if (r5 >= r0) goto L_0x0558
            java.lang.String r17 = "0"
            r0 = r17
            r13.append(r0)
        L_0x0558:
            java.lang.String r17 = java.lang.Integer.toHexString(r5)
            r0 = r17
            r13.append(r0)
            int r9 = r9 + 1
            goto L_0x053b
        L_0x0564:
            r6 = move-exception
            java.lang.String r17 = TAG
            java.lang.String r18 = r6.toString()
            android.util.Log.d(r17, r18)
            goto L_0x0543
        L_0x056f:
            java.lang.String r17 = TAG
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "Rx data for AOK :-"
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r13)
            java.lang.String r18 = r18.toString()
            android.util.Log.i(r17, r18)
            r0 = r20
            boolean r0 = r0.AOK_PASS
            r17 = r0
            r18 = 1
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x05ae
            java.lang.String r17 = r13.toString()
            java.lang.String r18 = "414f4b"
            boolean r17 = r17.equalsIgnoreCase(r18)
            if (r17 == 0) goto L_0x05ae
            r17 = 0
            r0 = r17
            r1 = r20
            r1.AOK_PASS = r0
            r20.requestVersion()
        L_0x05ae:
            r0 = r20
            boolean r0 = r0.AOK_SET_OPT_PASSWORD
            r17 = r0
            if (r17 == 0) goto L_0x004f
            java.lang.String r17 = r13.toString()
            java.lang.String r18 = "414f4b"
            boolean r17 = r17.equalsIgnoreCase(r18)
            if (r17 == 0) goto L_0x004f
            r17 = 0
            r0 = r17
            r1 = r20
            r1.AOK_SET_OPT_PASSWORD = r0
            android.content.Intent r10 = new android.content.Intent
            r10.<init>()
            r17 = 32
            r0 = r17
            r10.setFlags(r0)
            java.lang.String r17 = "com.iLevel.Password.StepTwo"
            r0 = r17
            r10.setAction(r0)
            java.lang.String r17 = "STEP_TWO"
            r18 = 1
            r0 = r17
            r1 = r18
            r10.putExtra(r0, r1)
            r0 = r20
            r0.sendBroadcast(r10)
            goto L_0x004f
        L_0x05ef:
            java.lang.String r17 = TAG
            java.lang.String r18 = "Ignition Off :----------------------"
            android.util.Log.i(r17, r18)
            r0 = r20
            boolean r0 = r0.AOK_PASS
            r17 = r0
            if (r17 == 0) goto L_0x004f
            r17 = 0
            r0 = r17
            r1 = r20
            r1.AOK_PASS = r0
            android.content.Intent r10 = new android.content.Intent
            r10.<init>()
            r17 = 32
            r0 = r17
            r10.setFlags(r0)
            java.lang.String r17 = "com.iLevel.Password.Incorrect"
            r0 = r17
            r10.setAction(r0)
            r0 = r20
            r0.sendBroadcast(r10)
            goto L_0x004f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.application.ILevelApplication.processData(byte[]):void");
    }

    @SuppressLint({"InlinedApi"})
    private void setUIForHomeScreen(StringBuilder stringBuilder) {
        Log.i(TAG, "setUIForHomeScreen RX data = " + stringBuilder);
        String[] dataOutStream = stringBuilder.toString().split(" ");
        if (dataOutStream.length > 4) {
            String errorIndicator = dataOutStream[4];
            String errorCode = new StringBuilder().append(errorIndicator.charAt(6)).append(errorIndicator.charAt(7)).toString();
            if (errorCode.equalsIgnoreCase("00")) {
                String dataOutStream_2 = dataOutStream[2];
                String ecuSetting = new StringBuilder().append(dataOutStream_2.charAt(0)).append(dataOutStream_2.charAt(1)).toString();
                String heightIndicator = new StringBuilder().append(dataOutStream_2.charAt(6)).append(dataOutStream_2.charAt(7)).toString();
                int ecuSettingDecimal = Integer.parseInt(ecuSetting, 16);
                int heightIndicatorDecimal = Integer.parseInt(heightIndicator, 16);
                Log.i(TAG, "setUIForHomeScreen Height Indicator and ECU Setting in decimal :- " + heightIndicatorDecimal + "--" + ecuSettingDecimal);
                setSettingInfo(ecuSettingDecimal);
                Intent intent = new Intent();
                intent.setFlags(32);
                intent.setAction("com.iLevel.Button.Highlited");
                switch (heightIndicatorDecimal) {
                    case 0:
                        intent.putExtra("BUTTON", 0);
                        break;
                    case 1:
                        intent.putExtra("BUTTON", 1);
                        break;
                    case 2:
                        intent.putExtra("BUTTON", 2);
                        break;
                    case 3:
                        intent.putExtra("BUTTON", 3);
                        break;
                    case 101:
                        intent.putExtra("BUTTON", 101);
                        break;
                    case 102:
                        intent.putExtra("BUTTON", 102);
                        break;
                    case 103:
                        intent.putExtra("BUTTON", 103);
                        break;
                    case 201:
                        intent.putExtra("BUTTON", 201);
                        break;
                    case 202:
                        intent.putExtra("BUTTON", 202);
                        break;
                    case 203:
                        intent.putExtra("BUTTON", 203);
                        break;
                }
                sendBroadcast(intent);
                Intent intent1 = new Intent();
                intent1.setFlags(32);
                intent1.setAction("com.iLevel.NoError");
                intent1.putExtra("ERROR_CODE", errorCode);
                sendBroadcast(intent1);
                return;
            }
            int errorCodeDecimal = Integer.parseInt(errorCode, 16);
            Log.i(TAG, "Error code decimal :-" + errorCodeDecimal);
            Intent intent2 = new Intent();
            intent2.setFlags(32);
            intent2.setAction("com.iLevel.ErrorIndicator");
            intent2.putExtra("ERROR", errorCodeDecimal);
            long tempInt = Long.parseLong(dataOutStream[6], 16);
            switch (errorCodeDecimal) {
                case 2:
                case 7:
                    Log.i(TAG, "setUIForHomeScreen Fault 2 or 7 - corner indicator=" + tempInt);
                    intent2.putExtra("GLOBAL_CORNER_INDICATOR", tempInt);
                    break;
            }
            sendBroadcast(intent2);
        }
    }

    private void setSettingInfo(int setting) {
        String settingInBinary = Integer.toBinaryString(setting);
        if (settingInBinary.trim().length() == 4) {
            settingInBinary = "0000" + settingInBinary;
        } else if (settingInBinary.trim().length() == 5) {
            settingInBinary = "000" + settingInBinary;
        } else if (settingInBinary.trim().length() == 6) {
            settingInBinary = "00" + settingInBinary;
        } else if (settingInBinary.trim().length() == 7) {
            settingInBinary = "0" + settingInBinary;
        }
        if (settingInBinary.length() == 8) {
            int RHOS = Integer.parseInt(Character.toString(settingInBinary.charAt(7)));
            int ACTIVE_OFF = Integer.parseInt(Character.toString(settingInBinary.charAt(6)));
            int ACTIVE_START_OFF = Integer.parseInt(Character.toString(settingInBinary.charAt(5)));
            int COMPRASSR_RANGE = Integer.parseInt(Character.toString(settingInBinary.charAt(4)));
            int parseInt = Integer.parseInt(Character.toString(settingInBinary.charAt(3)));
            int parseInt2 = Integer.parseInt(Character.toString(settingInBinary.charAt(2)));
            int parseInt3 = Integer.parseInt(Character.toString(settingInBinary.charAt(1)));
            int parseInt4 = Integer.parseInt(Character.toString(settingInBinary.charAt(0)));
            switch (RHOS) {
                case 0:
                    setRide_height_on_start("OFF");
                    break;
                case 1:
                    setRide_height_on_start("ON");
                    break;
            }
            switch (ACTIVE_OFF) {
                case 0:
                    setRide_monitor_mode("ON");
                    break;
                case 1:
                    setRide_monitor_mode("OFF");
                    break;
            }
            switch (ACTIVE_START_OFF) {
                case 0:
                    setTrim_mode("ON");
                    break;
                case 1:
                    setTrim_mode("OFF");
                    break;
            }
            switch (Integer.parseInt(Character.toString(settingInBinary.charAt(2)).concat(Character.toString(settingInBinary.charAt(3))))) {
                case 0:
                    setRide_mode_accracy("1");
                    break;
                case 1:
                    setRide_mode_accracy("1");
                    break;
                case 10:
                    setRide_mode_accracy("2");
                    break;
                case 11:
                    setRide_mode_accracy("3");
                    break;
            }
            setCompressorRange(COMPRASSR_RANGE);
        }
    }

    private void setCompressorRange(int COMPRASSR_RANGE) {
        if (COMPRASSR_RANGE == 0) {
            if (this.Old_Comp_Range_Bit != COMPRASSR_RANGE) {
                setTank_pressure_mode("175");
            } else {
                setTank_pressure_mode("150");
            }
            this.Old_Comp_Range_Bit = COMPRASSR_RANGE;
        } else if (COMPRASSR_RANGE == 1) {
            if (this.Old_Comp_Range_Bit != COMPRASSR_RANGE) {
                setTank_pressure_mode("175");
            } else {
                setTank_pressure_mode("200");
            }
            this.Old_Comp_Range_Bit = COMPRASSR_RANGE;
        }
    }

    public class SendCommandToSocket implements Runnable {
        private String command = "";
        private Timer txTimeout;

        public SendCommandToSocket(String command2) {
            this.command = command2;
        }

        public void run() {
            if (ILevelApplication.this.networkThread.getSocket() != null) {
                Log.i("TxData", "Start of TX Routine. txTimeout created.");
                this.txTimeout = new Timer();
                this.txTimeout.schedule(new TimerTask() {
                    public void run() {
                        Log.i("TxData", "TX Data 500 mS timeout!");
                        ILevelApplication.this.stopTimerTask();
                    }
                }, 500);
                ILevelApplication.this.networkThread.sendData(new BigInteger(this.command, 16).toByteArray());
                if (this.txTimeout != null) {
                    this.txTimeout.cancel();
                    this.txTimeout.purge();
                    this.txTimeout = null;
                }
                Log.i("TxData", "Data Tx = " + this.command);
            }
        }
    }

    public void sendButtonReleaseCommand() {
        ILevelApplication iLevelApplication = application;
        iLevelApplication.getClass();
        this.networkHandler.postDelayed(new SendCommandToSocket("a55a0a5001240d0d21210000abcd00000000000012340000000000000000000000007b7d7b7dd79b62af"), 100);
    }

    /* access modifiers changed from: private */
    @SuppressLint({"InlinedApi"})
    public void showException(String msg) {
        stopTimerTask();
        VERSION_INFO = false;
        Intent intent = new Intent();
        intent.setFlags(32);
        intent.setAction("com.iLevel.error");
        intent.putExtra("ERROR_DIALOG", msg);
        sendBroadcast(intent);
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

    public void savePassword(String password) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/.iLevel");
            if (!file.exists()) {
                file.mkdirs();
            } else {
                file.delete();
            }
            FileWriter fileWriter = new FileWriter(new File(file, "iLevel.txt"));
            fileWriter.append(password);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readPassword() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(Environment.getExternalStorageDirectory() + "/.iLevel/iLevel.txt")));
            StringBuilder text = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    return text.toString().replace("\n", "");
                }
                text.append(line);
                text.append(10);
            }
        } catch (FileNotFoundException e) {
            Log.e("AMin", "File not found: " + e.toString());
            return "";
        } catch (IOException e2) {
            Log.e("fdsd", "Can not read file: " + e2.toString());
            return "";
        }
    }

    private class iLevelIgnitionCounter extends CountDownTimer {
        public iLevelIgnitionCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            ILevelApplication.this.IS_REGULAR_UPDATE = false;
            Log.i(ILevelApplication.TAG, "Finished called for timer :)))))))))");
            ILevelApplication.this.changeHomeScreen();
            start();
            ILevelApplication.this.iLevelIgnitionCounter.start();
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    private class iLevelVersionCounter extends CountDownTimer {
        public iLevelVersionCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            ILevelApplication.VERSION_INFO = false;
            start();
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    public boolean isTablet() {
        return (getResources().getConfiguration().screenLayout & 15) >= 3;
    }
}
