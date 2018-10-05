package com.app.rum_a.ui.pre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.rum_a.DashActivity;
import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.modelutils.FacebookUser;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.qbloxui.SubscribeToNotification;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.Log4Android;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 27/8/18.
 */

public class LoginActivity extends BaseActivity implements RestCallback, GoogleApiClient.OnConnectionFailedListener {
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
    @BindView(R.id.logo_land)
    ImageView logoLand;
    @BindView(R.id.facebook_btn)
    RelativeLayout facebookBtn;
    @BindView(R.id.google_btn)
    RelativeLayout googleBtn;
    @BindView(R.id.edTxt_email)
    EditText edTxtEmail;
    @BindView(R.id.edTxt_password)
    EditText edTxtPassword;
    @BindView(R.id.txt_btn_signin)
    TextView txtBtnSignin;
    @BindView(R.id.txt_btn_signup)
    TextView txtBtnSignup;
    private int lookingFor, seekingFor;
    private Object mGoogleSignInClient;
    private String TAG = LoginActivity.class.getSimpleName();
    private int RC_SIGN_IN = 127;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;

    @Override
    public int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);

        getIntentData(getIntent());
        initGoogle();
        initFacebook();


    }

    private void initFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d(" onSuccess ", "  " + loginResult.getAccessToken());
                        if (loginResult.getAccessToken() != null) {
                            getuserdatafacebook();
                        }
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(" onCancel ", "  -------- ");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(" onError ", "  -------- " + exception.toString());
                    }
                });
       /* AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();*/
    }

    private void getuserdatafacebook() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        // Application code
                        FacebookUser facebookUser;
                        if (AccessToken.getCurrentAccessToken() != null) {
                            if (user != null) {
                                Log4Android.e(LoginActivity.this, user.toString());
                                Log.d("onCompleted ", "  1  " + new Gson().toJson(user));
                                facebookUser = new Gson().fromJson(user.toString(), FacebookUser.class);
                                try {
                                    Log.d("onCompleted ", "  2  " + new Gson().toJson(facebookUser));
                                    facebookUser = getUsableData(response);
                                    requestSocialRegisterOrLogin(null, facebookUser, AppConstants.RequestCode.FACEBOOK_SOCIAL);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    facebookUser = getUsableData(response);
                                    requestSocialRegisterOrLogin(null, facebookUser, AppConstants.RequestCode.FACEBOOK_SOCIAL);
                                }
                            }
                        }
                    }
                });

        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,email,first_name,last_name");
        parameters.putString("fields", "id,name,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private FacebookUser getUsableData(GraphResponse response) {
        JSONObject resObj = response.getJSONObject();
        String fbUserID = null;
        try {
            fbUserID = resObj.getString("id");
            Log.d("getUsableData ", "==============>" + new Gson().toJson(resObj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //checkSocialLogin(fbUserID);
        String firstName = "";
        String lastName = "";
        String email = "";
        String image_value = "";
        String userBday = "";
        try {
            firstName = resObj.getString("first_name");

        } catch (Exception e) {

        }
        try {
            lastName = resObj.getString("last_name");

        } catch (Exception e) {

        }

        try {
            email = resObj.getString("email");
        } catch (Exception e) {
            email = fbUserID + AppConstants.STATIC_FB_EMAIL;
        }


        try {
            fbUserID = resObj.getString("id");
            image_value = "http://graph.facebook.com/" + fbUserID + "/picture?type=large";
        } catch (Exception e) {

        }
        FacebookUser facebookUser = new FacebookUser();
        facebookUser.setEmail(email);
        facebookUser.setFirstName(firstName);
        facebookUser.setLastName(lastName);
        facebookUser.setId(fbUserID);
        facebookUser.setName(firstName + " " + lastName);
        facebookUser.setBirthDay(userBday);
        return facebookUser;
    }

    private void initGoogle() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void getIntentData(Intent intent) {
        lookingFor = intent.getIntExtra(AppConstants.ParmsType.lookingType, 0);
        seekingFor = intent.getIntExtra(AppConstants.ParmsType.seekingType, 0);
    }

    @OnClick({R.id.facebook_btn, R.id.google_btn, R.id.txt_btn_signin, R.id.txt_btn_signup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebook_btn:
                goNext(view);
                break;
            case R.id.google_btn:
                goNext(view);
                break;
            case R.id.txt_btn_signin:
                if (isvalidatedFields()) requestLogin();
                break;
            case R.id.txt_btn_signup:
                goNextSignUp();
                break;
        }
    }

    private boolean isvalidatedFields() {

        if (!CommonUtils.getInstance(this).isValidEmail(edTxtEmail)) {
            return false;
        } else if (edTxtPassword.getText().toString().length() < 6) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_min_pas_len));
            return false;
        } else if (CommonUtils.isEmpty(edTxtPassword)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_re_enter_password));
            return false;
        } else {
            return true;
        }
    }

    private void goNextSignUp() {
        final Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.ParmsType.lookingType, lookingFor);
        bundle.putInt(AppConstants.ParmsType.seekingType, seekingFor);
        activitySwitcher(LoginActivity.this, SignupActivity.class, bundle);
    }

    private void goNext(View view) {
        switch (view.getId()) {
            case R.id.google_btn:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, AppConstants.RequestCode.RC_SIGN_IN);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.facebook_btn:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                break;
        }

        
       /* showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                requestLogin();
                activitySwitcher(LoginActivity.this, DashActivity.class, null);
                finish();
            }
        }, AppConstants.TimeLimits.MEDIUM);*/
    }

    private void requestLogin() {
        Call<UserResponseModel> call = apiInterface.signInRequest(
                edTxtEmail.getText().toString(),
                edTxtPassword.getText().toString()
        );
        call.enqueue(new RestProcess<UserResponseModel>(AppConstants.ServiceModes.SIGN_IN_REQUEST, this, LoginActivity.this, true));
    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        UserResponseModel responseModel;
        switch (serviceMode) {
            case AppConstants.ServiceModes.SIGN_IN_REQUEST:
            case AppConstants.ServiceModes.SIGN_UP_REQUEST:
                responseModel = (UserResponseModel) model.body();
                if (responseModel.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    mPref.setUserData(new Gson().toJson(responseModel.getResult().getUser()));
                    mPref.setPrefrencesBoolean(AppConstants.ParmsType.isLogedIn, true);
                    getQbUser(cUtils.registerToQb(responseModel.getResult().getUser()), responseModel.getResult().getUser());
                    activitySwitcher(LoginActivity.this, DashActivity.class, null);
                    finish();
                    Log4Android.e("onSuccess ", "  " + mPref.getUserData());

                } else cUtils.displayMessage(LoginActivity.this, responseModel.getMessage());
                break;
        }
    }

    private void getQbUser(QBUser qbUser, UserResponseModel.ResultBean.UserBean user) {
        quickBloxSignin(qbUser);
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
                if (getUserDeatil().getQuickBloxDetails() == null)
                    saveDetail(qbUser, getUserDeatil());
                SubscribeToNotification.getInstance().setSubscribeToNotification(LoginActivity.this);
                qbUser.setLogin(getUserDeatil().getUserId() + "");
                qbUser.setPassword(AppConstants.QuickBloxConstants.USER_GLOBAL_PASSWORD);
                PreferenceManager.getInstance(LoginActivity.this).saveQbUser(qbUser);

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

    private void registerQb(QBUser qbUser) {
        QBUsers.signUpSignInTask(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.d(" registerQuickBlox ", " success  " + qbUser.getId());
                saveDetail(qbUser, getUserDeatil());
                quickBloxSignin(qbUser);
            }

            @Override
            public void onError(QBResponseException error) {
                Log.d("registerQuickBlox", "error");
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
                this, LoginActivity.this, false));
    }

    @Override
    public void onLogout() {

    }

    GoogleSignInAccount acct;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                acct = result.getSignInAccount();
                // Get account information
                Log.d(" data :::::  ", new Gson().toJson(acct));
                requestSocialRegisterOrLogin(acct, null, AppConstants.RequestCode.GOOGLE_SOCIAL);
                disconnectFromGoogle();
            }
        }
    }

    private HashMap<String, String> getRequestData(GoogleSignInAccount acct, int typeSocial) {
        Log.d(" getRequestData :::::  ", acct.getDisplayName() + '\n' + acct.getFamilyName() + '\n' + acct.getGivenName() + '\n' + acct.getId());
        HashMap<String, String> map = new HashMap<>();
        map.put(NetworkConstatnts.Params.firstName, acct.getGivenName());
        map.put(NetworkConstatnts.Params.lastName, acct.getFamilyName());
        map.put(NetworkConstatnts.Params.nickname, acct.getDisplayName());
        map.put(NetworkConstatnts.Params.email, acct.getEmail());
        map.put(NetworkConstatnts.Params.password, "123456");
        map.put(NetworkConstatnts.Params.lookingType, String.valueOf(lookingFor));
        map.put(NetworkConstatnts.Params.seekingType, String.valueOf(seekingFor));
        map.put(NetworkConstatnts.Params.googleId, acct.getId());
        return map;
    }

    private HashMap<String, String> getRequestDataFacebook(FacebookUser acct, int typeSocial) {
        Log.d(" getRequestData :::::  ", acct.getId() + '\n' + acct.getFirstName() + '\n' + acct.getLastName() + '\n' + acct.getId());
        HashMap<String, String> map = new HashMap<>();
        map.put(NetworkConstatnts.Params.firstName, acct.getFirstName());
        map.put(NetworkConstatnts.Params.lastName, acct.getLastName());
        map.put(NetworkConstatnts.Params.nickname, acct.getFirstName() + " " + acct.getLastName());
        map.put(NetworkConstatnts.Params.email, acct.getEmail());
        map.put(NetworkConstatnts.Params.password, "123456");
        map.put(NetworkConstatnts.Params.lookingType, String.valueOf(lookingFor));
        map.put(NetworkConstatnts.Params.seekingType, String.valueOf(seekingFor));
        map.put(NetworkConstatnts.Params.facebookId, acct.getId());
        return map;
    }

    private void requestSocialRegisterOrLogin(GoogleSignInAccount acct, FacebookUser oData, int typeSocial) {
        switch (typeSocial) {
            case AppConstants.RequestCode.GOOGLE_SOCIAL:
                HashMap requestData = getRequestData(acct, typeSocial);
                Call<UserResponseModel> call = apiInterface.signUpRequest(requestData);
                call.enqueue(new RestProcess<UserResponseModel>(AppConstants.ServiceModes.SIGN_UP_REQUEST, this,
                        LoginActivity.this, true));
                break;
            case AppConstants.RequestCode.FACEBOOK_SOCIAL:
                HashMap requestData1 = getRequestDataFacebook(oData, typeSocial);
                Call<UserResponseModel> call1 = apiInterface.signUpRequest(requestData1);
                call1.enqueue(new RestProcess<UserResponseModel>(AppConstants.ServiceModes.SIGN_UP_REQUEST, this,
                        LoginActivity.this, true));
                break;

        }

    }

    //log out of google account
    private void disconnectFromGoogle() {
        try {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        CommonUtils.getInstance(LoginActivity.this).ShowToast(connectionResult.getErrorMessage());
    }
}
