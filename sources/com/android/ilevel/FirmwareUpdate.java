package com.android.ilevel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import com.android.application.ILevelApplication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FirmwareUpdate extends Activity {
    private static final int NO_OF_GLOBAL_RETRIES = 10;
    private static final int NO_OF_RETRIES = 10;
    private static String address = null;
    public static ILevelApplication application = null;
    private static String command = null;
    /* access modifiers changed from: private */
    public static Context context = null;
    private static String count = null;
    private static boolean firmwareBypass = false;
    public static final String firmwareCRC = "A8F7";
    /* access modifiers changed from: private */
    public static String[] firmwareLines = null;
    /* access modifiers changed from: private */
    public static int globalRetryCount = 0;
    public static final int latestFirmwareVersion = 11;
    /* access modifiers changed from: private */
    public static int lineNumber;
    /* access modifiers changed from: private */
    public static int numberOfLines;
    /* access modifiers changed from: private */
    public static boolean oldStopVersionRequest;
    private static ProgressDialog progressBar;
    private static Runnable retry = new Runnable() {
        public void run() {
            Log.i("firmwareUpdate", "Retry Timer");
            FirmwareUpdate.nakReceived();
        }
    };
    /* access modifiers changed from: private */
    public static int retryCount;
    private static Handler retryHandler;
    private static int rotation;
    /* access modifiers changed from: private */
    public static View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rotation = 255;
    }

    public static void forceFirmwareUpdate(View v) {
        firmwareBypass = false;
        updateFirmware(v);
    }

    public static void disableFirmwareCheck() {
        firmwareBypass = true;
    }

    public static void updateFirmware(View v) {
        view = v;
        context = view.getContext();
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        if (!firmwareBypass) {
            new AlertDialog.Builder(context).setTitle("Firmware Update").setMessage(Html.fromHtml("New firmware is available for your iLevel module.  The update process will take several minutes during which your iLevel will not be accessible.<br><br><b>Ignition must be OFF during this process to be successful.</b>")).setCancelable(true).setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).setPositiveButton("Update", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    FirmwareUpdate.loadFirmwareFile();
                    int unused = FirmwareUpdate.numberOfLines = FirmwareUpdate.firmwareLines.length;
                    int unused2 = FirmwareUpdate.lineNumber = 0;
                    int unused3 = FirmwareUpdate.retryCount = 0;
                    int unused4 = FirmwareUpdate.globalRetryCount = 0;
                    FirmwareUpdate.application = ILevelApplication.application;
                    FirmwareUpdate.startProgressDialog(FirmwareUpdate.view);
                    if (FirmwareUpdate.application.STOP_VERSION_REQUEST) {
                        boolean unused5 = FirmwareUpdate.oldStopVersionRequest = true;
                    } else {
                        boolean unused6 = FirmwareUpdate.oldStopVersionRequest = false;
                    }
                    FirmwareUpdate.application.STOP_VERSION_REQUEST = true;
                    Log.i("firmwareUpdate", "Checksum 0000");
                    ILevelApplication iLevelApplication = FirmwareUpdate.application;
                    iLevelApplication.getClass();
                    new ILevelApplication.SendCommandToSocket("a55a0a5006020000").run();
                    FirmwareUpdate.startRetryTimer(5000);
                }
            }).create().show();
        }
        firmwareBypass = true;
    }

    public static void ackReceived() {
        Log.i("firmwareUpdate", "ACK received");
        if (numberOfLines > 0) {
            stopRetryTimer();
            retryCount = 0;
            if (lineNumber >= numberOfLines) {
                firmwareSuccess();
                return;
            }
            lineNumber++;
            sendLine();
        }
    }

    public static void nakReceived() {
        Log.i("firmwareUpdate", "NAK received");
        if (numberOfLines > 0) {
            stopRetryTimer();
            if (lineNumber >= numberOfLines) {
                lineNumber = 0;
                retryCount = 0;
                globalRetryCount++;
            } else if (retryCount <= 10) {
                retryCount++;
            } else {
                globalRetryCount++;
                retryCount = 0;
                lineNumber = 0;
            }
            if (globalRetryCount <= 10) {
                sendLine();
            } else {
                firmwareFailed();
            }
        }
    }

    @SuppressLint({"InlinedApi"})
    private static void lockOrientation() {
        rotation = ((Activity) context).getRequestedOrientation();
        if (Build.VERSION.SDK_INT >= 18) {
            ((Activity) context).setRequestedOrientation(14);
        } else {
            ((Activity) context).setRequestedOrientation(5);
        }
    }

    /* access modifiers changed from: private */
    public static void unlockOrientation() {
        if (rotation == 255) {
            ((Activity) context).setRequestedOrientation(-1);
        } else {
            ((Activity) context).setRequestedOrientation(rotation);
        }
    }

    /* access modifiers changed from: private */
    public static void startProgressDialog(View V) {
        Log.i("firmwareUpdate", "Launching progress dialog");
        progressBar = new ProgressDialog(V.getContext());
        progressBar.setCancelable(false);
        progressBar.setTitle("Updating Firmware...");
        progressBar.setMessage("Your iLevel is being updated to the latest firmware.  Please do not interrupt this process.");
        progressBar.setProgressStyle(1);
        progressBar.setProgress(lineNumber);
        progressBar.setMax(numberOfLines);
        lockOrientation();
        progressBar.show();
    }

    private static void updateProgressDialog() {
        progressBar.setProgress(lineNumber);
    }

    private static void firmwareSuccess() {
        Log.i("firmwareUpdate", "Firmware Update Complete");
        progressBar.dismiss();
        application.stopTimerTask();
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(FirmwareUpdate.context).setTitle("Firmware Updated").setMessage("Your iLevel firmware has been updated.  The app will close to complete this process.  Please allow a moment while your iLevel restarts.").setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        FirmwareUpdate.unlockOrientation();
                        if (Build.VERSION.SDK_INT >= 16) {
                            ((Activity) FirmwareUpdate.context).finishAffinity();
                        } else {
                            ((Activity) FirmwareUpdate.context).finish();
                        }
                        System.exit(0);
                    }
                }).create().show();
            }
        });
        numberOfLines = 0;
        application.STOP_VERSION_REQUEST = oldStopVersionRequest;
    }

    private static void firmwareFailed() {
        Log.i("firmwareUpdate", "!!!!!!FIRMWARE UPDATE FAILED!!!!!!");
        progressBar.dismiss();
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(FirmwareUpdate.context).setTitle("Update Failed").setMessage("Firmware update is unable to be completed at this time.  Please try again later.").setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        FirmwareUpdate.unlockOrientation();
                    }
                }).create().show();
            }
        });
        numberOfLines = 0;
        application.STOP_VERSION_REQUEST = oldStopVersionRequest;
    }

    private static void sendLine() {
        startTxTimer();
    }

    /* access modifiers changed from: private */
    public static void loadFirmwareFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("firmware.s19")));
            ArrayList<String> lines = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else if (line.startsWith("S1")) {
                    lines.add(line);
                }
            }
            firmwareLines = (String[]) lines.toArray(new String[lines.size()]);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            firmwareFailed();
        }
        Log.i("firmwareUpdate", "Loading firmware file complete");
    }

    /* access modifiers changed from: private */
    public static void startRetryTimer(long time) {
        Log.i("firmwareUpdate", "Starting Retry Timer for " + time + " milliseconds");
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        if (retryHandler == null) {
            retryHandler = new Handler();
        }
        if (retryHandler != null) {
            retryHandler.postDelayed(retry, time);
        } else {
            Log.i("firmwareUpdate", "retryHandler = null");
        }
    }

    private static void stopRetryTimer() {
        Log.i("firmwareUpdate", "Stopping Retry Timer");
        if (retryHandler != null) {
            retryHandler.removeCallbacks(retry);
        }
    }

    private static void startTxTimer() {
        Log.i("firmwareUpdate", "Starting Tx Timer");
        updateProgressDialog();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("firmwareUpdate", "sleep failed, canceling update");
            firmwareFailed();
        }
        Log.i("firmwareUpdate", "Tx Timer");
        if (lineNumber < numberOfLines) {
            count = firmwareLines[lineNumber].substring(2, 4);
            address = firmwareLines[lineNumber].substring(4, 8);
            command = firmwareLines[lineNumber].substring(8, firmwareLines[lineNumber].length() - 2);
            Log.i("firmwareUpdate", "FIRMWARE:(line " + lineNumber + " of " + numberOfLines + ")-" + firmwareLines[lineNumber]);
            ILevelApplication iLevelApplication = application;
            iLevelApplication.getClass();
            new ILevelApplication.SendCommandToSocket("a55A0A5005" + count + address + String.format("%02X", new Object[]{Integer.valueOf(Integer.valueOf(count, 16).intValue() - 3)}) + command).run();
            startRetryTimer(1000);
            return;
        }
        Log.i("firmwareUpdate", "FIRMWARE: $CA8F7");
        ILevelApplication iLevelApplication2 = application;
        iLevelApplication2.getClass();
        new ILevelApplication.SendCommandToSocket("a55a0a500602A8F7").run();
        startRetryTimer(5000);
    }
}
