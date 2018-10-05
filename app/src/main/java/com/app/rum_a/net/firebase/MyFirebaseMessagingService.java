/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.rum_a.net.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.rum_a.DashActivity;
import com.app.rum_a.R;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.ui.postauth.activity.PropertyDetailActivity;
import com.app.rum_a.ui.postauth.qbloxui.activities.ChatScreenActivity;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatModel;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.Consts;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.GeneralUtils;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.activities.CallService;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.quickbloxutils.NotificationUtils;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.quickbloxutils.XmppUtils;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.PreferenceManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import javax.inject.Inject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Inject
    PreferenceManager mPref;
    private static final String TAG = "MyFirebaseMsgService";
    private static int NOTIFICATION_ID;
    private String notificationMsgType, messageText;
    private PushSenderModel senderData;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ((RumApplication) getApplication()).getMyComponent().inject(this);
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        if (mPref.getPrefrencesBoolean(AppConstants.ParmsType.isLogedIn)) {
            try {
                Log.d(TAG, " Rum-A  , From ::::::: P U S H     M E S S A G E:: 1 " + remoteMessage.getFrom());
                String gcmdata = remoteMessage.getData().get("gcm");
                String messagetype = remoteMessage.getData().get("type");
                Log.d(TAG, " Rum-A  , From ::::::: P U S H     M E S S A G E::  2 " + gcmdata + "    " + messagetype);
//                if (!XmppUtils.checkBlockStatus(gcmdata))
                if (gcmdata != null && !gcmdata.equals("")) {
                    generateIntentData(this, gcmdata, messagetype);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                // Check if message contains a data payload.
                if (remoteMessage.getNotification() != null && remoteMessage.getNotification().getBody() != null
                        && !remoteMessage.getNotification().getBody().equals("")) {
                    Log.d(TAG, " Rum-A ,  Message data payload: " + remoteMessage.getData());
                    Bundle bundle = new Bundle();
                    // Check if message contains a notification payload.
                    NotificationStructureModel newModel = null;
                    Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                    String notificationStruct = remoteMessage.getNotification().getBody().toString();
                    newModel = new Gson().fromJson(notificationStruct, NotificationStructureModel.class);
                    Log.d(TAG, "Message Notification Body::::: " + newModel.getPropertyID());
                    bundle.putInt(AppConstants.ParmsType.PROPERTY_ID, newModel.getPropertyID());
                    sendNotification(newModel, bundle);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param messageText
     * @param data
     * @param bundle
     */
    private void sendNotification(NotificationStructureModel data, Bundle bundle) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        String message = data.getMsg();
        String title = "Property Liked";
        Intent notificationIntent;
        PendingIntent contentIntent;
        NOTIFICATION_ID = (int) System.currentTimeMillis();
        notificationIntent = new Intent(this, PropertyDetailActivity.class);
        notificationIntent.putExtras(bundle);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        generateNotification(message, title,contentIntent);
    }


    private void generateNotification(String message, String title, PendingIntent contentIntent) {
        NotificationCompat.Builder mBuilder;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "RumA_channel_01";
            CharSequence name = "RumA_channel";
            String Description = "This is RumA channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            mNotificationManager.createNotificationChannel(mChannel);
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_ruma)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(contentIntent);
        } else {
            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_ruma)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentText(message);
            mBuilder.setContentIntent(contentIntent);
        }
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    private void generateIntentData(Context context, String data, String message) {
        try {
            ChatModel.Gcm chatModel = new Gson().fromJson(data, ChatModel.Gcm.class);
            if (message.equalsIgnoreCase(Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_MESSAGE)) {
                if (!CommonUtils.getIsChatActive()) {
                    NotificationUtils.showNotification(this,
                            ChatScreenActivity.class,
                            chatModel.getNOTI_TITLE(),
                            chatModel.getNOTI_MESSAGE(), chatModel,
                            R.mipmap.ic_launcher,
                            Integer.parseInt(chatModel.getNOTI_ID()));
                }
            } else if (message.equalsIgnoreCase(Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_VIDEO) ||
                    message.equalsIgnoreCase(Consts.NOTIFICATION_CODE.NOTFIFICATION_TYPE_AUDIO)) {
                CallService.start(context, GeneralUtils.getUserFromSession());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
