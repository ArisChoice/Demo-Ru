package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.quickbloxutils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.rum_a.R;
import com.app.rum_a.model.resp.ChatUsersModelResponse;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatModel;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.Consts;
import com.app.rum_a.utils.AppConstants;
import com.google.gson.Gson;

import static com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ResourceUtils.getString;

public class NotificationUtils {

    public static void showNotification(Context context, Class<? extends Activity> activityClass,
                                        String title, String message, ChatModel.Gcm chatModel, @DrawableRes int icon,
                                        int notificationId) {


        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        Log.e("TEST----->", "" + isScreenOn);
        if (isScreenOn == false) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");

            wl_cpu.acquire(10000);
        }
        Intent intent = new Intent(context, activityClass);
        intent.putExtra(Consts.NOTIFICATION_CODE.EXTRA_GCM_MESSAGE, message);
        try {
            String propertyData = chatModel.getPropertyBody();
            PropertyListResponseModel.ResultBean prooertyData = new Gson().fromJson(propertyData, PropertyListResponseModel.ResultBean.class);
            intent.putExtra(Consts.REQUEST_CODE.EXTRA_DIALOG_ID, prooertyData.getChatDialog());
            intent.putExtra(AppConstants.ParmsType.isAlreadyChatted, true);
            intent.putExtra(AppConstants.ParmsType.USER_ID, "" + chatModel.getSenderAppUserId());
            intent.putExtra(AppConstants.ParmsType.PROPERTY_DETAIL, prooertyData);
            intent.putExtra(AppConstants.ParmsType.USER_NAME, title);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        intent.putExtra(Consts.NOTIFICATION_CODE.EXTRA_GCM_BODY, new Gson().toJson(chatModel));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder;

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
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        } else {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(icon).setTicker("New Message")
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentTitle(title).setVibrate(new long[]{10, 10})
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        }


    }

}
