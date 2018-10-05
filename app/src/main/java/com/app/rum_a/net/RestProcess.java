package com.app.rum_a.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.rum_a.core.RumApplication;
import com.app.rum_a.model.resp.DefaultResponse;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.Dialogs;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Harish on 28/12/16.
 */

public class RestProcess<T> implements Callback<T>/*, OnAlertDialogItemListener*/ {

    private final Dialogs dialogs;
    ProgressDialog mProgressDialog;
    //    AlertDialogs dialogs;
    RestCallback callback;
    int serviceCode;
    Activity activity;
    boolean isDialogShow;
//    Util util;

    public RestProcess(int serviceCode, RestCallback restCallback, Activity activity, boolean isDialogShow) {
        this.callback = restCallback;
        this.activity = activity;
        this.serviceCode = serviceCode;
        this.isDialogShow = isDialogShow;
        dialogs = Dialogs.getInstance(activity);
        if (isDialogShow) {
            hideLoading();
            mProgressDialog = dialogs.showLoadingDialog(activity);
        }
    }
    public RestProcess(int serviceCode, RestCallback restCallback, Context activity, boolean isDialogShow) {
        this.callback = restCallback;
        this.serviceCode = serviceCode;
        this.isDialogShow = isDialogShow;
        dialogs = Dialogs.getInstance(activity);
        if (isDialogShow) {
            hideLoading();
            mProgressDialog = dialogs.showLoadingDialog(activity);
        }
    }

    public void hideLoading() {
        dialogs.DismissDialog(mProgressDialog);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        hideLoading();
        if (call != null) {
            if (response.code() == 500) {
//                if (dialogs != null && isDialogShow) {
//                    dialogs.dismiss();
//                }
//                new Util().setToast(MyApplication.getInstance().getString(R.string.servererror));
            } else if (response.code() == 203) {
//                if (dialogs != null && isDialogShow) {
//                    dialogs.dismiss();
//                }
//                new Util().setToast(MyApplication.getInstance().getString(R.string.servererror));
//                CommonUtils.getInstance(activity).ShowToast(activity.getString(R.string.servererror));
                new RumApplication().logoutUser();
            } else {
//                if (dialogs != null && isDialogShow) {
//                    dialogs.dismiss();
//                }
                callback.onSuccess(call, response, serviceCode);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("", t.getMessage());
//        if (dialogs != null && isDialogShow) {
//            dialogs.dismiss();
//        }
        hideLoading();
        if (t instanceof SocketTimeoutException) {
            Log.e("SocketTimeoutException ", t.getMessage());
        } else if (t instanceof UnknownHostException) {
            Log.e("UnknownHostException ", t.getMessage());
//            util.showOneButtonDialog(activity, activity.getString(R.string.networl_alert), activity.getString(R.string.internet_connection_message), activity.getString(R.string.tryagain), this, GlobalsVariables.TYPE.APAPTER_DIALOG_NETWORK_CLICK);
        } else if (callback != null) {
            callback.onFailure(call, t, serviceCode);
        }


    }

    /* @Override
     public void onClickDone(int type, Object o) {
         switch (type) {
             case GlobalsVariables.TYPE.APAPTER_DIALOG_NETWORK_CLICK:
                 exit();
                 break;
         }
     }

     @Override
     public void onClickCancel(int type, Object o) {

     }
 */
    private void exit() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(startMain);
        activity.finish();
    }
}