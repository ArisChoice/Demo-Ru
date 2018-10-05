package com.app.rum_a.ui.postauth.qbloxui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.app.rum_a.core.RumApplication;
import com.app.rum_a.utils.CommonUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBSubscription;

import java.util.ArrayList;

/**
 * Created by Sunny on 3/26/2017.
 */

public class SubscribeToNotification {


    static SubscribeToNotification subscribeToNotification;

    public static SubscribeToNotification getInstance() {
        if (subscribeToNotification == null) {
            subscribeToNotification = new SubscribeToNotification();
        }
        return subscribeToNotification;
    }

    public SubscribeToNotification() {
    }

    public void setSubscribeToNotification(Context context) {
        QBSubscription subscription = new QBSubscription(QBNotificationChannel.GCM);
        subscription.setEnvironment(QBEnvironment.DEVELOPMENT);
        //
        String deviceId;
        final TelephonyManager mTelephony;
        try {
            mTelephony = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(RumApplication.getInstance(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            deviceId = mTelephony.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        subscription.setDeviceUdid(deviceId);
        //
        String registrationID = CommonUtils.getInstance(RumApplication.getInstance()).getDevideGCMid();//gcm push token
        subscription.setRegistrationID(registrationID);

        QBPushNotifications.createSubscription(subscription).performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {

            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {
                Log.i("subscrib", ">>> Subscription: " + subscriptions.toString());
            }

            @Override
            public void onError(QBResponseException errors) {
                handleErrors(errors);
            }
        });
    }

    public void handleErrors(QBResponseException exc) {
        String message = String.format("[ERROR] Request has been completed with errors: %s", exc.getErrors()
                + ", code: " + exc.getHttpStatusCode());

        // print
        Log.i("error", message);
    }

    public void unSubcribToNotfication() {

        QBPushNotifications.getSubscriptions().performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {

                Log.e("sucesssss", "done unsubcreb");
                for (QBSubscription subscription : subscriptions) {
                    QBPushNotifications.deleteSubscription(subscription.getId()).performAsync(new QBEntityCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid, Bundle bundle) {
                            Log.e("sucess", "done unsubcreb");
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.e("error", e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onError(QBResponseException errors) {
                Log.e("error", errors.getMessage());

            }
        });
    }

//    public void unSubcribToNotfication(){
//
//        QBPushNotifications.deleteSubscription()
//    }
}

