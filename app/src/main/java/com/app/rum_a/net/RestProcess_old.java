/*
package com.app.rum_a.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.app.rum_a.R;
import com.app.rum_a.ui.pre.SignupActivity;
import com.app.rum_a.utils.CommonUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


*/
/**
 * Created by Harish on 3/12/14.
 *//*

public class RestProcess<T> implements Callback<T> {

    private final RestCallback restCallback;
    private final CommonUtils commonUtils;
    private final int serviceMode;
    private boolean showProgress, isCancel;
    private View view;
    private ProgressDialog progressDialog;
    //    private Dialogs dialogs;
    private Context context;
    private boolean isInstagram;


    public RestProcess(Context context, RestCallback restCallback, int serviceMode, View view, boolean showProgress, boolean isInstagram) {
        this.restCallback = restCallback;
        this.serviceMode = serviceMode;
        this.showProgress = showProgress;
        this.view = view;
        this.context = context;
        commonUtils = CommonUtils.getInstance(context);
//        dialogs = Dialogs.getInstance(context);
        setEnabled(false);
        isCancel = false;
        this.isInstagram = isInstagram;
        if (showProgress) {
//            progressDialog = dialogs.getProgressDialog("");
        }
    }

    */
/**
     * Description : Used to handle success and failure of RESTApi
     *
     * @param context
     * @param restCallback
     * @param serviceMode  : Integer to get along with result after response
     *//*

    public RestProcess(Context context, RestCallback restCallback, int serviceMode) {
        this(context, restCallback, serviceMode, null, false, false);
    }

    public RestProcess(Context context, RestCallback restCallback, View view, int serviceMode) {
        this(context, restCallback, serviceMode, view, true, false);
    }

    public RestProcess(int signUp, SignupActivity restCallback, Class<?> activity, boolean b) {
    }


    public void setCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }


    private void setEnabled(boolean isEnabled) {
        if (view != null) {
            view.setEnabled(isEnabled);
        }
        if (isEnabled) {
//            dialogs.DismissDialog(progressDialog);
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        setEnabled(true);
        if (restCallback != null && !isCancel) {

            if (!isInstagram) {
                String json_str = new Gson().toJson(response);

                JSONObject obj;
                try {
                    obj = new JSONObject(json_str);
                    int statusCode = obj.getInt("Status");
                    //Log out of app
                    if (statusCode == 203) {
                        String message = obj.getString("Message");
                        restCallback.onLogout();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } else {
                        restCallback.onSuccess(call, response, serviceMode);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                restCallback.onSuccess(call, response, serviceMode);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        setEnabled(true);
//        if (t.getKind() == RetrofitError.Kind.NETWORK) {
//            CommonUtils.ShowToast(R.string.toast_network_not_available);
//        }
        if (restCallback != null)
            restCallback.onFailure(t.getCause(), serviceMode);
    }
}
*/
