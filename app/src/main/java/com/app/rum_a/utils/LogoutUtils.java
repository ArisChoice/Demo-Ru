package com.app.rum_a.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.app.rum_a.core.MyApplication;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatHelper;
import com.app.rum_a.ui.postauth.service.LocationService;
import com.app.rum_a.ui.pre.LandingActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

/**
 * Created by Harish on 27/11/15.
 */
public class LogoutUtils {

    /**
     * Log out from app
     */
    public static void logOutApp(Context context) {

        try {
            context.stopService(new Intent(context, LocationService.class));
//            context.stopService(new Intent(context, XmppConnectionService.class));

            Intent intent = new Intent(context, LandingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            //UtilitySingleton.activitySwitcher((Activity)context, LandingActivity.class, null);

//            new PreferenceManager().clearUserData();
            new PreferenceManager().setPrefrencesBoolean(AppConstants.ParmsType.isLogedIn, false);
            //((Activity)context).finish();

//            BusProvider.getInstance().post(new FinishActivity());
            /*InstagramLogin instagramLogin = new InstagramLogin((Activity)context);

            if (instagramLogin.getSession().isActive()) {
                InstagramSession session = new InstagramSession(context);
                session.reset();
            }*/
            //logout from fb
            logoutFromFb(context);
            //logoutFrom quick Blox.
            logoutFromQb();

            //clear all pending notifications
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
            nMgr.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logoutFromQb() {
        boolean isLoggedIn = ChatHelper.getInstance().isLogged();
        if (!isLoggedIn) {
            return;
        }
        QBChatService.getInstance().logout(new QBEntityCallback() {

            @Override
            public void onSuccess(Object o, Bundle bundle) {
                QBChatService.getInstance().destroy();
            }

            @Override
            public void onError(QBResponseException errors) {

            }
        });
    }

    /**
     * Log out from facebook
     */
    public static void logoutFromFb(Context context) {
        try {
            FacebookSdk.sdkInitialize(context);
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
