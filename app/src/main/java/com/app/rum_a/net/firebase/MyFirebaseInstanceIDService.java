package com.app.rum_a.net.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.app.rum_a.core.RumApplication;
import com.app.rum_a.utils.PreferenceManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

/**
 * Created by harish on 5/27/2016.
 */


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Inject
    PreferenceManager mPref;
    private static final String TAG = "MyFirebaseIDService";
    public static final String mypreferenceNew = "mypref_Token";//used only for push token
    SharedPreferences sharedpreferences;

    @Override
    public void onTokenRefresh() {
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.e(TAG, " Refreshed token  :::  =====> " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

        mPref.setDeviceId(token);
        final SharedPreferences prefs = getGCMPreferences(getApplicationContext());
        prefs.edit().putInt(mypreferenceNew, getAppVersion()).apply();
//
//        //THIS IS Stored in other sharedPrefetence.
        sharedpreferences = getSharedPreferences(mypreferenceNew, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("pushToken", token);
        editor.commit();


        //GET THIS TOKEN FROM PREF
        sharedpreferences = getSharedPreferences(mypreferenceNew, Context.MODE_PRIVATE);
        sharedpreferences.getString("pushToken", "");
        Log.e(TAG, " Refreshed token  ::: sendRegistrationToServer  =====> " + sharedpreferences.getString("pushToken", ""));


//        Log.e(TAG, "token  ::: sendRegistrationToServer  =====> " + token + '\n' +
//                ((BaseApplication) getApplicationContext().getApplicationContext())
//                        .getPreferences().getDeviceId());
        //You can implement this method to store the token on your server
        //Not required for current project
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getApplicationContext().getSharedPreferences("XAL", Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private int getAppVersion() {
        try {
            PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
