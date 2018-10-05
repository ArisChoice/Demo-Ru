package com.app.rum_a.core;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.callbacks.QBResRequestExecutor;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.messages.services.QBPushManager;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Harish on 2/3/15.
 */
public class MyApplication extends RumApplication {

    String timeStamp, clientHashText, secretKey, sessionId, deviceId, uniqueDeviceId;
    private boolean isUserLogin;
    private String ChatId = "0";
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    SharedPreferences sharedpreferences;
    String mypreferenceNew = "mypref_Token";
    private String TAG = " :: MyApplication ::  ";
    private static QBResRequestExecutor qbResRequestExecutor;
    private static MyApplication instance;


    private Activity activity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ImageLoaderInint();
        FirebaseApp.initializeApp(this);
        // now subscribe to `global` topic to receive app wide notifications
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
        setQuickBlox();
    }

    private void setQuickBlox() {
        initPushManager();
        QBSessionManager.getInstance().addListener(new QBSessionManager.QBSessionListener() {
            @Override
            public void onSessionCreated(QBSession qbSession) {
                Log.d(TAG, "Session Created");
            }

            @Override
            public void onSessionUpdated(QBSessionParameters qbSessionParameters) {
                Log.d(TAG, "Session Updated");
            }

            @Override
            public void onSessionDeleted() {
                Log.d(TAG, "Session Deleted");
            }

            @Override
            public void onSessionRestored(QBSession qbSession) {
                Log.d(TAG, "Session Restored");
            }

            @Override
            public void onSessionExpired() {
                Log.d(TAG, "Session Expired");
            }
        });
    }

    public static synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null ? qbResRequestExecutor = new QBResRequestExecutor() : qbResRequestExecutor;
    }


    private void initPushManager() {
        QBPushManager.getInstance().addListener(new QBPushManager.QBSubscribeListener() {
            @Override
            public void onSubscriptionCreated() {
                Log.d(TAG, "SubscriptionCreated");
            }

            @Override
            public void onSubscriptionError(Exception e, int resultCode) {
                Log.d(TAG, "SubscriptionError" + e.getLocalizedMessage());
                if (resultCode >= 0) {
                    String error = GoogleApiAvailability.getInstance().getErrorString(resultCode);
                    Log.d(TAG, "SubscriptionError playServicesAbility: " + error);
                }
            }
        });
    }


    public String getDeviceId() {
        //GET THIS TOKEN FROM PREF
        sharedpreferences = getSharedPreferences(mypreferenceNew, Context.MODE_PRIVATE);
        deviceId = sharedpreferences.getString("pushToken", "");
        return deviceId;
    }

    private long getTimeStamp() {
        return System.currentTimeMillis() / 10000;
    }


    public void setChatId(String chatId) {
        ChatId = chatId;
    }

    public String getChatId() {
        return ChatId;
    }

    public void setActivty(Activity activty) {
        this.activity = activty;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public static Context getInstance() {
        return instance;
    }
}
