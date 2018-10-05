package com.app.rum_a.di;

import com.app.rum_a.DashActivity;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.net.firebase.MyFirebaseInstanceIDService;
import com.app.rum_a.net.firebase.MyFirebaseMessagingService;
import com.app.rum_a.ui.postauth.activity.AddPropertyActivity;
import com.app.rum_a.ui.postauth.activity.EditProfileActivity;
import com.app.rum_a.ui.postauth.activity.FeedbackActivity;
import com.app.rum_a.ui.postauth.activity.LikedPropertiesActivity;
import com.app.rum_a.ui.postauth.activity.ProfileActivity;
import com.app.rum_a.ui.postauth.activity.PropertyDetailActivity;
import com.app.rum_a.ui.postauth.activity.SettingActivity;
import com.app.rum_a.ui.postauth.fragments.MyProperties;
import com.app.rum_a.ui.postauth.qbloxui.activities.ChatScreenActivity;
import com.app.rum_a.ui.postauth.service.LocationService;
import com.app.rum_a.ui.pre.LoginActivity;
import com.app.rum_a.ui.pre.SignupActivity;
import com.app.rum_a.ui.pre.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Harish on 9/11/16.
 */


@Singleton
@Component(modules = {DiModule.class})

public interface DiComponent {
    void inject(LoginActivity signupActivity);

    void inject(SignupActivity signupActivity);

    void inject(SplashActivity splashActivity);

    void inject(DashActivity dashActivity);

    void inject(ProfileActivity profileActivity);

    void inject(AddPropertyActivity addPropertyActivity);

    void inject(BaseActivity baseActivity);

    void inject(MyProperties myProperties);

    void inject(LocationService locationService);

    void inject(EditProfileActivity editProfile);

    void inject(SettingActivity settingActivity);

    void inject(PropertyDetailActivity detailActivity);

    void inject(ChatScreenActivity chatActivity);

    void inject(LikedPropertiesActivity propertiesActivity);

    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);

    void inject(MyFirebaseMessagingService myFirebaseMessagingService);

    void inject(FeedbackActivity feedbackActivity);

    // to update the fields in your activities


}