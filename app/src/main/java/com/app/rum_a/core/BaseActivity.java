package com.app.rum_a.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.app.rum_a.R;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.callbacks.QBResRequestExecutor;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.Dialogs;
import com.app.rum_a.utils.PreferenceManager;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by harish on 21/8/18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    // since its going to be common for all the activities
    private ProgressDialog mProgressDialog;
    private Dialogs dialogs;
    @Inject
    PreferenceManager mPref;
    public QBResRequestExecutor requestExecutor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestExecutor = MyApplication.getQbResRequestExecutor();
        ((RumApplication) getApplication()).getMyComponent().inject(this);

        performDataBinding();
        dialogs = Dialogs.getInstance(this);
    }

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    public void showLoading() {
        hideLoading();
        mProgressDialog = dialogs.showLoadingDialog(this);
        mProgressDialog.setCancelable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.setCancelable(true);
            }
        }, AppConstants.TimeLimits.VERY_LONG);

    }

    public void hideLoading() {
        dialogs.DismissDialog(mProgressDialog);
    }

    private void performDataBinding() {
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    /**
     * @return user data
     */
    public UserResponseModel.ResultBean.UserBean getUserDeatil() {
        return new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public static void activitySwitcher(Activity from, Class<?> to, Bundle bundle) {

        Intent intent = new Intent(from, to);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//        from.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}