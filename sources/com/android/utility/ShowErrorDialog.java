package com.android.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.android.application.ILevelApplication;

public class ShowErrorDialog {
    /* access modifiers changed from: private */
    public static AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public static ILevelApplication application;
    private static Context myContext;
    private static ShowErrorDialog showErrorDialog;

    public static ShowErrorDialog getInstance(Context context, ILevelApplication application2) {
        if (showErrorDialog == null) {
            showErrorDialog = new ShowErrorDialog();
            myContext = context;
            application = application2;
        }
        return showErrorDialog;
    }

    public boolean isDiaogVisible() {
        if (alertDialog == null) {
            return false;
        }
        return alertDialog.isShowing();
    }

    public void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void showErrorMSG(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ShowErrorDialog.alertDialog.dismiss();
                ILevelApplication access$100 = ShowErrorDialog.application;
                access$100.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ShowErrorDialog.alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
