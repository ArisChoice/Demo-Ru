package com.app.rum_a.ui.postauth.qbloxui.countdown;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.app.rum_a.utils.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Timer_Service extends Service {

    public static String str_receiver = "com.social.cangetit.receiver";

    private Handler mHandler = new Handler();
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String strDate;
    Date date_current, date_diff;
    SharedPreferences.Editor mEditor;

    private Timer mTimer = null;
    public static final long NOTIFY_INTERVAL = 1000;
    Intent intent;
    private PreferenceManager mpref;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mpref = PreferenceManager.getInstance(Timer_Service.this);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);
        intent = new Intent(str_receiver);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    strDate = simpleDateFormat.format(calendar.getTime());
                    //Log.e("strDate", strDate);
                    twoDatesBetweenTime();

                }

            });
        }

    }

    public String twoDatesBetweenTime() {


        try {
            date_current = simpleDateFormat.parse(strDate);
        } catch (Exception e) {

        }

        try {
            date_diff = simpleDateFormat.parse(mpref.getPrefrencesString("data"));
        } catch (Exception e) {

        }

        try {

            long diff = date_current.getTime() - date_diff.getTime();
            int int_hours = Integer.valueOf(mpref.getPrefrencesString("hours"));
            long int_timer = TimeUnit.HOURS.toMillis(int_hours);
            long long_hours = int_timer - diff;
            long diffSeconds2 = long_hours / 1000 % 60;
            long diffMinutes2 = long_hours / (60 * 1000) % 60;
            long diffHours2 = long_hours / (60 * 60 * 1000) % 24;


            if (long_hours > 0) {
                String str_testing = diffHours2 + ":" + diffMinutes2 + ":" + diffSeconds2;

                //Log.e("TIME", str_testing);

                fn_update(str_testing);
            } else {
                mpref.setPrefrencesBoolean("finish", true);
                mTimer.cancel();
                mpref.setKissCount(0);
                mpref.setWinkCount(0);
                //Log.e(" My Preferences ", "       reseted    ");
            }
        } catch (Exception e) {
            mTimer.cancel();
            mTimer.purge();
        }

        return "";

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service finish", "Finish");
    }

    private void fn_update(String str_time) {

        intent.putExtra("time", str_time);
        sendBroadcast(intent);
    }
}