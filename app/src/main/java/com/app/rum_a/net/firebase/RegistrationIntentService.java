/*
package com.app.rum_a.net.firebase;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.rum_a.R;
import com.app.rum_a.core.RumApplication;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            String token = instanceID.getToken(getString(R.string.sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token);
            Log.e(TAG, "GCM Registration Token: " + token);
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);

        }
    }

    private void sendRegistrationToServer(String token) {
//        LocalBroadcastManager.getInstance(RumApplication.getInstance()).
//                sendBroadcast(new Intent(GlobalsVariables.RECEIVER.TOKEN_REFRESH).putExtra(GlobalsVariables.FINAL_VARIABLES.TOKEN_REFRESH, token));
    }
}*/
