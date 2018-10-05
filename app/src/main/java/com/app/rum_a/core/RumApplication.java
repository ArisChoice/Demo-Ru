package com.app.rum_a.core;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.app.rum_a.R;
import com.app.rum_a.di.DaggerDiComponent;
import com.app.rum_a.di.DiComponent;
import com.app.rum_a.di.DiModule;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.Consts;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.CoreConfigUtils;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.QbConfigs;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.SampleConfigs;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.PreferenceManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;

import java.io.IOException;

/**
 * Created by harish on 23/8/18.
 */

public class RumApplication extends MultiDexApplication {
    private static final String TAG = RumApplication.class.getSimpleName();
    private static RumApplication instance;
    private DiComponent myComponent;
    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config.json";
    private QbConfigs qbConfigs;
    private static SampleConfigs sampleConfigs;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
        myComponent = DaggerDiComponent.builder().diModule(new DiModule()).build();
        initQbConfigs();
        initCredentials();
        ImageLoaderInint();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private void initQbConfigs() {
        Log.e(TAG, "QB CONFIG FILE NAME: " + getQbConfigFileName());
        qbConfigs = CoreConfigUtils.getCoreConfigsOrNull(getQbConfigFileName());
    }
    public void initCredentials() {
        if (qbConfigs != null) {
            QBSettings.getInstance().init(getApplicationContext(), qbConfigs.getAppId(), qbConfigs.getAuthKey(), qbConfigs.getAuthSecret());
            QBSettings.getInstance().setAccountKey(qbConfigs.getAccountKey());

            if (!TextUtils.isEmpty(qbConfigs.getApiDomain()) && !TextUtils.isEmpty(qbConfigs.getChatDomain())) {
                QBSettings.getInstance().setEndpoints(qbConfigs.getApiDomain(), qbConfigs.getChatDomain(), ServiceZone.PRODUCTION);
                QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
            }
        }
    }
    private void initSampleConfigs() {
        try {
            sampleConfigs = CoreConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SampleConfigs getSampleConfigs() {
        return sampleConfigs;
    }

    protected String getQbConfigFileName() {
        return QB_CONFIG_DEFAULT_FILE_NAME;
    }

    public void ImageLoaderInint() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(5 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();
        ImageLoader.getInstance().init(config.build());
    }

    public DiComponent getMyComponent() {
        return myComponent;
    }

    DisplayImageOptions defaultOptions;

    public DisplayImageOptions getDisplayImageOptions(boolean isRounded) {
        DisplayImageOptions.Builder defaultOptionsbuilder =
                new DisplayImageOptions.Builder();
        defaultOptionsbuilder.cacheInMemory(true);
        defaultOptionsbuilder.cacheOnDisk(true);
        defaultOptionsbuilder.considerExifParams(true);
        defaultOptionsbuilder.showImageOnLoading(R.drawable.img2);
        defaultOptionsbuilder.showImageOnFail(R.drawable.img2);
        if (isRounded) {
            defaultOptionsbuilder.displayer(new RoundedBitmapDisplayer(20));
        }
        defaultOptions = defaultOptionsbuilder.build();
        return defaultOptions;
    }

    /* public RestService getRestService() {
         isUserLogin = isUserLogin(false);
         Log4Android.e(this, isUserLogin + ":isUserLogin" + "   " + getPreferences().getDeviceId());
         deviceId = getDeviceId();
         if (isUserLogin) {
             sessionId = getUserData().getSessionId();
 //            deviceId = getPreferences().getDeviceId();  //Static id For temp use
         }
         return getRestAdapter().create(RestService.class);
     }*/
    public static Context getInstance() {
        return instance;
    }

    public void logoutUser() {
//        CommonUtils.getInstance(this).ShowToast("Session Expired");
        new PreferenceManager().setPrefrencesBoolean(AppConstants.ParmsType.isLogedIn, false);

    }
}
