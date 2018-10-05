package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.app.rum_a.DashActivity;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatPingAlarmManager;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.Consts;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.WebRtcSessionManager;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.quickbloxutils.XmppUtils;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;

import org.jivesoftware.smackx.ping.PingFailedListener;

/**
 * QuickBlox team
 */
public class CallService extends Service {
    private static final String TAG = CallService.class.getSimpleName();
    private QBChatService chatService;
    private QBRTCClient rtcClient;
    private PendingIntent pendingIntent;
    private Intent intent;
    private int currentCommand;
    private QBUser currentUser;
//    AppCompatActivity appCompatActivity;


    public static void start(AppCompatActivity context, QBUser qbUser, PendingIntent pendingIntent) {
        Intent intent = new Intent(context, CallService.class);

        intent.putExtra(Consts.EXTRA_COMMAND_TO_SERVICE, Consts.COMMAND_LOGIN);
        intent.putExtra(Consts.EXTRA_QB_USER, qbUser);
        intent.putExtra(Consts.EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra("context", (Parcelable) context);
        context.startService(intent);
    }

    public static void start(Context context, QBUser qbUser) {
        Intent serviceintent = new Intent(context, CallService.class);
        serviceintent.putExtra(Consts.EXTRA_COMMAND_TO_SERVICE, Consts.COMMAND_LOGIN);
        serviceintent.putExtra(Consts.EXTRA_QB_USER, qbUser);

//        start(context, qbUser, intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceintent);
        } else {
            context.startService(serviceintent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());
        createChatService();

        Log.d(TAG, "Service onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Service started");

        parseIntentExtras(intent);

        startSuitableActions();

        return START_REDELIVER_INTENT;
    }

    private void parseIntentExtras(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            currentCommand = intent.getIntExtra(Consts.EXTRA_COMMAND_TO_SERVICE, Consts.COMMAND_NOT_FOUND);
            pendingIntent = intent.getParcelableExtra(Consts.EXTRA_PENDING_INTENT);
            currentUser = (QBUser) intent.getSerializableExtra(Consts.EXTRA_QB_USER);
//            appCompatActivity = intent.getParcelableExtra("Context");
        }
    }

    private void startSuitableActions() {
        if (currentCommand == Consts.COMMAND_LOGIN) {
            startLoginToChat();
        } else if (currentCommand == Consts.COMMAND_LOGOUT) {
            logout();
        }
    }

    private void createChatService() {
        if (chatService == null) {
            QBChatService.setDebugEnabled(true);
            chatService = QBChatService.getInstance();
        }
    }

    private void startLoginToChat() {
        if (!chatService.isLoggedIn()) {
            Log.e(TAG, "loging in  if");
            loginToChat(currentUser);
        } else {
            Log.e(TAG, "login in  else");
            sendResultToActivity(true, null);
        }
    }

    private void loginToChat(QBUser qbUser) {
//        ProgressDialogFragment.show(appCompatActivity.getSupportFragmentManager(), appCompatActivity.getString(R.string.please_wait));
//        QBSettings.getInstance().setEnablePushNotification(true);
        chatService.login(qbUser, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.d(TAG, "login onSuccess");
                startActionsOnSuccessLogin();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "login onError " + e.getMessage());
                sendResultToActivity(false, e.getMessage() != null
                        ? e.getMessage()
                        : "Login error");
            }
        });
    }

    private void startActionsOnSuccessLogin() {
        initPingListener();
        initQBRTCClient();
        if (pendingIntent == null) {
            sendResultToActivity(true, null);
        } else {
            startActivity(new Intent(this, DashActivity.class));
            intent = null;
        }
    }

    private void initPingListener() {
        ChatPingAlarmManager.onCreate(this);
        ChatPingAlarmManager.getInstanceFor().addPingListener(new PingFailedListener() {
            @Override
            public void pingFailed() {
                Log.d(TAG, "Ping chat server failed");
            }
        });
    }

    private void initQBRTCClient() {
        try {
            Log.e(TAG, "call logining");
            rtcClient = QBRTCClient.getInstance(getApplicationContext());
            // Add signalling manager
            chatService.getVideoChatWebRTCSignalingManager().addSignalingManagerListener(new QBVideoChatSignalingManagerListener() {
                @Override
                public void signalingCreated(QBSignaling qbSignaling, boolean createdLocally) {
                    //                ProgressDialogFragment.hide(appCompatActivity.getSupportFragmentManager());
                    Log.e(TAG, "call logined");
                    if (!createdLocally) {
                        rtcClient.addSignaling((QBWebRTCSignaling) qbSignaling);
                    }
                }
            });

            // Configure
            QBRTCConfig.setDebugEnabled(true);
            XmppUtils.configRTCTimers(CallService.this);

            // Add service as callback to RTCClient
            rtcClient.addSessionCallbacksListener(WebRtcSessionManager.getInstance(this));
            rtcClient.prepareToProcessCalls();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendResultToActivity(boolean isSuccess, String errorMessage) {
        if (pendingIntent != null) {
            Log.d(TAG, "sendResultToActivity()");
            try {
                Intent intent = new Intent();
                intent.putExtra(Consts.EXTRA_LOGIN_RESULT, isSuccess);
                intent.putExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE, errorMessage);

                pendingIntent.send(CallService.this, Consts.EXTRA_LOGIN_RESULT_CODE, intent);
            } catch (PendingIntent.CanceledException e) {
                String errorMessageSendingResult = e.getMessage();
                Log.d(TAG, errorMessageSendingResult != null
                        ? errorMessageSendingResult
                        : "Error sending result to activity");
            }
        }
    }

    public static void logout(Context context) {
        Intent intent = new Intent(context, CallService.class);
        intent.putExtra(Consts.EXTRA_COMMAND_TO_SERVICE, Consts.COMMAND_LOGOUT);
        context.startService(intent);
    }

    private void logout() {
        destroyRtcClientAndChat();
    }

    private void destroyRtcClientAndChat() {
        if (rtcClient != null) {
            rtcClient.destroy();
        }
        ChatPingAlarmManager.onDestroy();
        if (chatService != null) {
            chatService.logout(new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    chatService.destroy();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d(TAG, "logout onError " + e.getMessage());
                    chatService.destroy();
                }
            });
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service onDestroy()");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service onBind)");
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "Service onTaskRemoved()");
        super.onTaskRemoved(rootIntent);
        destroyRtcClientAndChat();
    }
}
