package com.app.rum_a.ui.pre;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.app.rum_a.DashActivity;
import com.app.rum_a.Manifest;
import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.quickbloxutils.XmppUtils;
import com.app.rum_a.ui.postauth.service.LocationService;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.PreferenceManager;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by harish on 23/8/18.
 */

public class SplashActivity extends BaseActivity {
    @Inject
    PreferenceManager mPref;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 190;

    @Override
    public int getLayoutId() {
        return R.layout.splash_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
            moveNext();
        } else {
            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
                moveNext();
            }
        }

    }

    private boolean checkAndRequestPermissions() {
        int permissionState = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
//        int storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int locationPermission2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
//        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
        if (permissionState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (locationPermission2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_CAMERA);
            return false;
        }

        return true;
    }

    private void moveNext() {

        CommonUtils.getInstance(SplashActivity.this).getKeyHash();
        startService(new Intent(SplashActivity.this, LocationService.class));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPref.getPrefrencesBoolean(AppConstants.ParmsType.isLogedIn)) {
                    startActivity(new Intent(SplashActivity.this, DashActivity.class));
                    getUserSessionQuickBlox();
                } else startActivity(new Intent(SplashActivity.this, LandingActivity.class));
                finish();

            }
        }, AppConstants.TimeLimits.LONG);

    }

    private void getUserSessionQuickBlox() {
        try {
            QBUser user = new QBUser(getUserDeatil().getUserId() + "", XmppUtils.QuickBloxConstants.USER_GLOBAL_PASSWORD);
            user.setId(getUserDeatil().getQuickBloxDetails().getQbId());
            user.setFullName(getUserDeatil().getFirstName() + " " + getUserDeatil().getLastName());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        user.setTags(tags);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    moveNext();
                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                    CommonUtils.getInstance(this).ShowToast(getString(R.string.permission_denied));
                }
                break;
        }
    }
}
