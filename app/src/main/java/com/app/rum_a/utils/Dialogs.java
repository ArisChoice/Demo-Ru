package com.app.rum_a.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.app.rum_a.R;
import com.app.rum_a.utils.views.HeartBeatView;

public class Dialogs {
    private static Context context;
    private static Dialogs instance;
    private static HeartBeatView loaderHrtBts;

    public static Dialogs getInstance(Context context1) {
        context = context1;
        if (instance == null) {
            instance = new Dialogs();
        }
        return instance;
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.custome_progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        loaderHrtBts = progressDialog.findViewById(R.id.lodar_heart_bt);
        loaderHrtBts.start();
        loaderHrtBts.setScaleFactor(1);
        loaderHrtBts.setDurationBasedOnBPM(50);

        return progressDialog;
    }

    public void DismissDialog(ProgressDialog dialog) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}