package com.android.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.android.application.ILevelApplication;

public class ShowNetworkDialog {
    /* access modifiers changed from: private */
    public static AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public static ILevelApplication application;
    private static Context myContext;
    private static ShowNetworkDialog networkDialog;

    public static ShowNetworkDialog getInstance(Context context, ILevelApplication application2) {
        if (networkDialog == null) {
            networkDialog = new ShowNetworkDialog();
            myContext = context;
            application = application2;
        }
        return networkDialog;
    }

    public boolean isDialogVisible() {
        if (alertDialog == null || alertDialog == null || alertDialog.isShowing()) {
            return false;
        }
        return false;
    }

    public void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void showNetworkError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setTitle("Error");
        builder.setMessage("Please check your network connectivity.");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ShowNetworkDialog.alertDialog.dismiss();
                ShowNetworkDialog.application.checkWifiStatus();
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ShowNetworkDialog.alertDialog.dismiss();
            }
        });
    }
}
