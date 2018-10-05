package com.app.rum_a.ui.pre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rum_a.DashActivity;
import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.req.RegisterRequestModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.activity.BrowserActivity;
import com.app.rum_a.ui.postauth.qbloxui.SubscribeToNotification;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.views.RumTextView;
import com.google.gson.Gson;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 27/8/18.
 */

public class SignupActivity extends BaseActivity implements RestCallback {

    @Named(DaggerValues.NON_AUTH)
    @Inject
    RestService apiInterface;
    @Named(DaggerValues.AUTH)
    @Inject
    RestService apiInterfaceA;
    @Inject
    PreferenceManager mPref;
    @Inject
    CommonUtils cUtils;
    @BindView(R.id.back_toolbar)
    ImageView backToolbar;
    @BindView(R.id.txt_title_toolbar)
    TextView txtTitleToolbar;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.edTxt_first_name)
    EditText edTxtFirstName;
    @BindView(R.id.edTxt_last_name)
    EditText edTxtLastName;
    @BindView(R.id.edTxt_username)
    EditText edTxtUsername;
    @BindView(R.id.edTxt_email)
    EditText edTxtEmail;
    @BindView(R.id.edTxt_password)
    EditText edTxtPassword;
    @BindView(R.id.edTxt_re_password)
    EditText edTxtRePassword;
    @BindView(R.id.txt_btn_complte)
    TextView txtBtnComplte;
    @BindView(R.id.terms_check)
    CheckBox termsCheck;
    @BindView(R.id.termsText)
    RumTextView termsText;
    private int lookingFor, seekingFor;

    @Override
    public int getLayoutId() {
        return R.layout.signup_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        txtTitleToolbar.setText(R.string.str_sign_up);
        getIntentData(getIntent());
//        String text = "I have " + getString(R.string.str_term_conditions) + "<font color='blue'></font>.";
        String text = "<font color='black'>I have </font> <font color='blue'>agree to terms & conditions</font>.";
        termsText.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
    }

    private void getIntentData(Intent intent) {
        lookingFor = intent.getIntExtra(AppConstants.ParmsType.lookingType, 0);
        seekingFor = intent.getIntExtra(AppConstants.ParmsType.seekingType, 0);
    }

    @OnClick({R.id.back_toolbar, R.id.txt_btn_complte, R.id.termsText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.txt_btn_complte:
                if (isvalidatedFields()) goNext(view);
                break;
            case R.id.termsText:
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.ParmsType.WEB_URL, NetworkConstatnts.API.TERMS);
                activitySwitcher(SignupActivity.this, BrowserActivity.class, bundle);
                break;

        }
    }

    private boolean isvalidatedFields() {

        if (CommonUtils.isEmpty(edTxtFirstName)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_enter_first_name));
            return false;
        } else if (CommonUtils.isEmpty(edTxtLastName)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_enter_last_name));
            return false;
        } else if (!CommonUtils.getInstance(this).isValidEmail(edTxtEmail)) {
            return false;
        } else if (edTxtPassword.getText().toString().length() < 6) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_min_pas_len));
            return false;
        } else if (CommonUtils.isEmpty(edTxtRePassword)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_re_enter_password));
            return false;
        } else if (!CommonUtils.getText(edTxtPassword).contentEquals(CommonUtils.getText(edTxtRePassword))) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_password_not_match));
            return false;
        } else if (!termsCheck.isChecked()) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_accept_terms));
            return false;
        } else {
            return true;
        }
    }

    private void goNext(View view) {
//        Call<DefaultResponse> call = apiInterface.signInRequest(getRequestData());
//        call.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.SIGN_UP_REQUEST, this, SignupActivity.this, false));
        Call<UserResponseModel> call = apiInterface.signUpRequest(getRequestData().getFirstName(),
                getRequestData().getLastName(),
                getRequestData().getNickName(),
                getRequestData().getEmail(),
                getRequestData().getPassword(),
                getRequestData().getLookingType(),
                getRequestData().getSeekingType());
        call.enqueue(new RestProcess<UserResponseModel>(AppConstants.ServiceModes.SIGN_UP_REQUEST, this, SignupActivity.this, true));
    }

    private RegisterRequestModel getRequestData() {
        RegisterRequestModel request = new RegisterRequestModel();
        request.setFirstName(CommonUtils.getText(edTxtFirstName));
        request.setLastName(CommonUtils.getText(edTxtLastName));
        request.setEmail(CommonUtils.getText(edTxtEmail));
        request.setPassword(CommonUtils.getText(edTxtPassword));
        request.setLookingType(lookingFor);
        request.setSeekingType(seekingFor);
        return request;
    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {
        hideLoading();
    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.SIGN_UP_REQUEST:
                hideLoading();
                UserResponseModel userResponse = (UserResponseModel) model.body();
                if (userResponse.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    mPref.setUserData(new Gson().toJson(userResponse.getResult().getUser()));
                    mPref.setPrefrencesBoolean(AppConstants.ParmsType.isLogedIn, true);
                    getQbUser(cUtils.registerToQb(userResponse.getResult().getUser()), userResponse.getResult().getUser());
                    activitySwitcher(SignupActivity.this, DashActivity.class, null);
                } else
                    CommonUtils.getInstance(SignupActivity.this).ShowToast(userResponse.getMessage());
                break;
            case AppConstants.ServiceModes.SAVE_QBDETAIL_REQUEST:
                break;
        }

    }

    private void getQbUser(QBUser qbUser, final UserResponseModel.ResultBean.UserBean userData) {
        registerQb(qbUser);
    }

    private void registerQb(QBUser qbUser) {
        QBUsers.signUpSignInTask(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.e(" registerQuickBlox ", " success  " + qbUser.getId());
                saveDetail(qbUser, getUserDeatil());
                quickBloxSignin(qbUser);
            }

            @Override
            public void onError(QBResponseException error) {
                Log.e("registerQuickBlox", "error");
            }
        });
    }

    private void quickBloxSignin(final QBUser qbUser) {
        final QBUser user = new QBUser();
        user.setLogin(qbUser.getLogin() + "");
        user.setPassword(AppConstants.QuickBloxConstants.USER_GLOBAL_PASSWORD);
        QBUsers.signIn(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
//                callback.onSuccess(qbUser, bundle);
                Log.d(" quickBloxSignin ", "  onSuccess  " + qbUser.getId());
                SubscribeToNotification.getInstance().setSubscribeToNotification(SignupActivity.this);
                qbUser.setLogin(getUserDeatil().getUserId() + "");
                qbUser.setPassword(AppConstants.QuickBloxConstants.USER_GLOBAL_PASSWORD);
                PreferenceManager.getInstance(SignupActivity.this).saveQbUser(qbUser);

            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(" quickBloxSignin ", "  onError " + e.getLocalizedMessage());
                if (e.getLocalizedMessage().contains("Unauthorized"))
                    registerQb(qbUser);
//                callback.onError(e);
            }
        });
    }

    private void saveDetail(QBUser qbUser, UserResponseModel.ResultBean.UserBean userData) {
        Call<UserResponseModel> call = apiInterfaceA.saveUserRequest(String.valueOf(userData.getUserId()),
                userData.getFirstName(),
                userData.getLastName(),
                String.valueOf(qbUser.getId()),
                qbUser.getPassword());
        call.enqueue(new RestProcess<UserResponseModel>(AppConstants.ServiceModes.SAVE_QBDETAIL_REQUEST,
                this, SignupActivity.this, false));

    }

    @Override
    public void onLogout() {

    }
}
