package com.app.rum_a;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.resp.DefaultResponse;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.activity.AddPropertyActivity;
import com.app.rum_a.ui.postauth.activity.BrowserActivity;
import com.app.rum_a.ui.postauth.activity.EditProfileActivity;
import com.app.rum_a.ui.postauth.activity.LikedPropertiesActivity;
import com.app.rum_a.ui.postauth.activity.ProfileActivity;
import com.app.rum_a.ui.postauth.activity.SettingActivity;
import com.app.rum_a.ui.postauth.adapter.ScreenSlidePagerAdapter;
import com.app.rum_a.ui.postauth.adapter.SwipeViewAdapter;
import com.app.rum_a.ui.postauth.fragments.MyProperties;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.quickbloxutils.XmppUtils;
import com.app.rum_a.ui.postauth.service.LocationService;
import com.app.rum_a.ui.pre.LandingActivity;
import com.app.rum_a.ui.pre.SplashActivity;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.Log4Android;
import com.app.rum_a.utils.LogoutUtils;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.SpringAnimation;
import com.app.rum_a.utils.appinterface.OnSwipePerform;
import com.app.rum_a.utils.appinterface.SwipeOperation;
import com.app.rum_a.utils.swipecardlib.SwipeCardView;
import com.app.rum_a.utils.swipeutils.SwipeTCard;
import com.app.rum_a.utils.views.RumTextView;
import com.google.gson.Gson;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.quickblox.users.model.QBUser;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class DashActivity extends BaseActivity implements /*SwipeCardView.OnCardFlingListener,*/ RestCallback,
        SwipeCardView.OnItemClickListener, SwipeOperation {
    @Named(DaggerValues.AUTH)
    @Inject
    RestService apiInterface;
    @Inject
    PreferenceManager mPref;
    @Inject
    CommonUtils cUtils;
    /*@BindView(R.id.txt_sampl)
    TextView txtSampl;*/
    ArrayList<Fragment> activeFragments;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.main_home_view)
    LinearLayout mainHomeView;
    @BindView(R.id.toolbar_icon_chat)
    ImageView toolbarIconChat;
    @BindView(R.id.toolbar_icon_add_property)
    ImageView toolbarIconAddProperty;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.toolbar_logo)
    ImageView toolbarLogo;
    @BindView(R.id.toolbar_icon_menu)
    ImageView toolbarIconMenu;
    @BindView(R.id.lay_sampl)
    LinearLayout laySampl;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.side_btn_home)
    RumTextView sideBtnHome;
    @BindView(R.id.side_btn_profile)
    RumTextView sideBtnProfile;
    @BindView(R.id.side_btn_properties)
    RumTextView sideBtnProperties;
    @BindView(R.id.side_btn_settings)
    RumTextView sideBtnSettings;
    @BindView(R.id.side_btn_about)
    RumTextView sideBtnAbout;
    @BindView(R.id.side_btn_faq)
    RumTextView sideBtnFaq;
    @BindView(R.id.side_btn_logout)
    RumTextView sideBtnLogout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.user_profilr_name)
    RumTextView userProfilrName;
    @BindView(R.id.lay_edit_profile)
    LinearLayout layEditProfile;
    @BindView(R.id.swipeView)
    SwipePlaceHolderView swipeView;
    private ScreenSlidePagerAdapter page;
    @BindView(R.id.imgPropertyImage)
    CircleImageView imgPropertyImage;
    @BindView(R.id.txtFindingNearBy)
    RumTextView txtFindingNearBy;
    @BindView(R.id.ripple_back)
    RippleBackground rippleBack;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;
    //    @BindView(R.id.swipeContainer)
//    SwipeCardView swipeContainer;
    @BindView(R.id.imgCross)
    ImageView imgCross;
    @BindView(R.id.imgLike)
    ImageView imgLike;
    @BindView(R.id.linearBottom)
    LinearLayout linearBottom;
    private SwipeViewAdapter adapter;
    private SpringAnimation springAnimation;
    Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private Handler handler;
    private Handler mHandler;
    private int getpropertyCheck = 1;
    private int propertyCount;
    private boolean isSwipeOpened = false;//Used to handle on one swipe at a time.
    private int iSwipeCount;
    private ArrayList<PropertyListResponseModel.ResultBean> propertyList;
    private PropertyListResponseModel.ResultBean currentSwipedPropertyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        activeFragments = getFragments();
        mHandler = new Handler();
        toolbar = findViewById(R.id.toolbar);
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.menu_icon); //set your own
        toggle.syncState();
        setupPager();
        swipeContainerSetup();
        springAnim();
        startSearching();
        getUserProfile();
    }

    private void getUserProfile() {
        int userId = new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId();
        Call<UserResponseModel> call = apiInterface.getProfile(String.valueOf(userId));
        call.enqueue(new RestProcess<UserResponseModel>(AppConstants.ServiceModes.GET_PROFILE, this, DashActivity.this, false));
    }

    private void getNearByProperties() {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
//        String lo = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<PropertyListResponseModel> call = apiInterface.getHomeProperty();
        call.enqueue(new RestProcess<PropertyListResponseModel>(AppConstants.ServiceModes.GET_HOME_PROPERTIES, this, DashActivity.this, false));

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dash;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUserLocatin();
            }
        }, AppConstants.TimeLimits.SMALL);
        setUserDetail();
    }

    private void setUserDetail() {
        userProfilrName.setText(getUserDeatil().getFirstName() + " " + getUserDeatil().getLastName());
        getUserSessionQuickBlox();
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

    private void updateUserLocatin() {
//        double[] userLocation = mPref.getUserLocation();
//        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
//        Call<DefaultResponse> call = apiInterface.updateUserLocation(userId, String.valueOf(userLocation[0]),
//                String.valueOf(String.valueOf(userLocation[1])), mPref.getPrefrencesString(AppConstants.ParmsType.USER_ADDRESS));
//        call.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.UPDATE_LOCATION, this, DashActivity.this, false));
        stopService(new Intent(DashActivity.this, LocationService.class));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(new Intent(DashActivity.this, LocationService.class));
            }
        }, AppConstants.TimeLimits.MEDIUM);

    }


    private void startSearching() {
        startRippleAnimation();
    }

    private void startRippleAnimation() {
        rippleBack.startRippleAnimation();
        txtFindingNearBy.setText(R.string.finding_property);
        CommonUtils.showView(searchLayout);
        CommonUtils.showView(rippleBack);
        CommonUtils.hideView(linearBottom);
//        getUsers();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                stopRippleAnimation();
//                setAdapter();
                getNearByProperties();
            }
        }, 2000);

    }

    private void stopRippleAnimation() {
        CommonUtils.hideView(searchLayout);
        CommonUtils.hideView(rippleBack);
        CommonUtils.showView(linearBottom);
        rippleBack.stopRippleAnimation();
    }

    private void springAnim() {
        springAnimation = new SpringAnimation();
        springAnimation.setAnimation(imgPropertyImage);
    }

    private void swipeContainerSetup() {
        int bottomMargin = cUtils.dpToPx(80);
        Point windowSize = cUtils.getDisplaySize(getWindowManager());
        swipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(10)
                        .setRelativeScale(0.01f)
                        .setSwipeRotationAngle(20)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));
    }


    private void setupPager() {
        // Instantiate a ViewPager and a PagerAdapter.
        page = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(page);

    }

    private ArrayList<Fragment> getFragments() {
        activeFragments = new ArrayList<>();
//        activeFragments.add(HomeFragment.newInstance(DashActivity.this));
        activeFragments.add(MyProperties.newInstance(DashActivity.this));
        return activeFragments;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.imgPropertyImage, R.id.side_btn_home, R.id.side_btn_profile,
            R.id.side_btn_properties, R.id.side_btn_settings,
            R.id.side_btn_about, R.id.side_btn_faq, R.id.imgCross, R.id.imgLike,
            R.id.side_btn_logout, R.id.toolbar_icon_menu, R.id.toolbar_icon_chat, R.id.toolbar_icon_add_property, R.id.lay_edit_profile})
    public void onClick(View view) {
        // Handle navigation view item clicks here.
//        View ew = swipeContainer.getSelectedView();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        handler = new Handler();
        switch (view.getId()) {

            case R.id.side_btn_home:
                toolbar.setBackground(getResources().getDrawable(R.drawable.toolbar_shadow_bg));
                toggle.setHomeAsUpIndicator(R.drawable.menu_icon); //set your own
                CommonUtils.showView(toolbarLogo);
                CommonUtils.hideView(titleTxt);
                CommonUtils.showView(mainHomeView);
                CommonUtils.hideView(viewPager);
                CommonUtils.showView(toolbarIconChat);
                CommonUtils.hideView(toolbarIconAddProperty);
                startRippleAnimation();
                break;
            case R.id.toolbar_icon_chat:
                activitySwitcher(DashActivity.this, LikedPropertiesActivity.class, null);
                break;
            case R.id.imgPropertyImage:
                startRippleAnimation();
                break;
            case R.id.side_btn_profile:
                activitySwitcher(DashActivity.this, ProfileActivity.class, null);
                break;
            case R.id.side_btn_properties:
                toolbar.setBackground(getResources().getDrawable(R.drawable.toolbar_shadow_app_color));
                titleTxt.setText(R.string.str_properties_);
                toggle.setHomeAsUpIndicator(R.drawable.menu_white); //set your own
                titleTxt.setTextColor(getResources().getColor(R.color.color_white));
                CommonUtils.showView(titleTxt);
                CommonUtils.hideView(toolbarLogo);
                CommonUtils.showView(viewPager);
                CommonUtils.hideView(mainHomeView);
                CommonUtils.showView(toolbarIconAddProperty);
                CommonUtils.hideView(toolbarIconChat);
                setupPager();
                break;
            case R.id.side_btn_settings:
                activitySwitcher(DashActivity.this, SettingActivity.class, null);
                break;
            case R.id.side_btn_about:
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.ParmsType.WEB_URL, NetworkConstatnts.API.ABOUT_US);
                activitySwitcher(DashActivity.this, BrowserActivity.class, bundle);
                break;
            case R.id.side_btn_faq:
                bundle = new Bundle();
                bundle.putString(AppConstants.ParmsType.WEB_URL, NetworkConstatnts.API.FAQ);
                activitySwitcher(DashActivity.this, BrowserActivity.class, bundle);
                break;
            case R.id.side_btn_logout:
                onLogout();
                break;
            case R.id.toolbar_icon_add_property:

                activitySwitcher(DashActivity.this, AddPropertyActivity.class, null);
                break;
            case R.id.imgCross:
                springAnimation = new SpringAnimation();
                springAnimation.setAnimation(imgCross);
                crossClicked();
                break;
            case R.id.imgLike:
                springAnimation = new SpringAnimation();
                springAnimation.setAnimation(imgLike);
                likeClicked();
                break;
            case R.id.lay_edit_profile:
                activitySwitcher(DashActivity.this, EditProfileActivity.class, null);
                break;
        }
    }

    private int swipeType;

    private void crossClicked() {

        swipeType = AppConstants.SwipeType.CROSS;
        swipeView.doSwipe(false);
//        swiptRequesApiCall(swipeType);
    }

    private void likeClicked() {
        swipeType = AppConstants.SwipeType.LIKE;
        swipeView.doSwipe(true);
//        swiptRequesApiCall(swipeType);

    }


    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.GET_HOME_PROPERTIES:
                PropertyListResponseModel responseData = (PropertyListResponseModel) model.body();
                if (responseData.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    if (responseData.getResult() != null && responseData.getResult().size() > 0) {
                        propertyList = new ArrayList<>(responseData.getResult());
                        Log.e(" onSuccess ", " " + " " + propertyList.size());
                        setSwipeData(responseData.getResult());
                    } else {
                        txtFindingNearBy.setText(R.string.str_no_property_found);
                        CommonUtils.showView(searchLayout);
                        CommonUtils.showView(rippleBack);
                        CommonUtils.hideView(linearBottom);
                        rippleBack.stopRippleAnimation();
                    }
                } else if (responseData.getStatus() == NetworkConstatnts.ResponseCode.sessionExpred) {
                    onLogout();
                    cUtils.ShowToast(responseData.getMessage());
                } else txtFindingNearBy.setText(R.string.str_no_property_found);
                break;
            case AppConstants.ServiceModes.DISLIKE_PROPERTY:
            case AppConstants.ServiceModes.LIKE_PROPERTY:
                DefaultResponse defaultResponse = (DefaultResponse) model.body();
                if (defaultResponse.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    Log.e(" onSuccess ", " " + propertyList.size());
                    if (propertyList.size() == propertyCount + 1) {
                        getpropertyCheck = 1;
                        adapter = null;
                        startRippleAnimation();
                    }
                }

                break;
            case AppConstants.ServiceModes.GET_PROFILE:
                UserResponseModel userResponse = (UserResponseModel) model.body();
                if (userResponse.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    try {
                        UserResponseModel.ResultBean.UserBean userData = getUserDeatil();
                        userData.setQuickBloxDetails(userResponse.getResult().getUser().getQuickBloxDetails());
                        userData.setProfilePics(userResponse.getResult().getUser().getProfilePics());
                        mPref.setUserData(new Gson().toJson(userData));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (userResponse.getStatus() == NetworkConstatnts.ResponseCode.sessionExpred) {
                    onLogout();
                    cUtils.ShowToast(userResponse.getMessage());
                }
                break;
        }

    }

    private void setSwipeData(final List<PropertyListResponseModel.ResultBean> propertyLit) {
        stopRippleAnimation();
        for (PropertyListResponseModel.ResultBean mData : propertyLit) {
            swipeView.addView(new SwipeTCard(DashActivity.this, mData, swipeView, new OnSwipePerform() {
                @Override
                public void onSWipeData(Object o, int sType) {
                    iSwipeCount++;
                    PropertyListResponseModel.ResultBean data = (PropertyListResponseModel.ResultBean) o;
                    currentSwipedPropertyData = data;
                    propertyList.remove(data);
                    if (data.getOwnerDetails().getOwnerId() != getUserDeatil().getUserId())
                        swiptRequesApiCall(sType);
                    Log.d("onSwipeData ", "   " + data.getName() + " " + propertyList.size());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (propertyList.size() == 0) {
                                startSearching();
                            }
                        }
                    }, AppConstants.TimeLimits.MEDIUM);
                }
            }));
        }
    }

    private void getDefaultProperties() {
        String userId = "28";
//        String lo = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<PropertyListResponseModel> call = apiInterface.getProperty(userId);
        call.enqueue(new RestProcess<PropertyListResponseModel>(AppConstants.ServiceModes.GET_PROPERTIES, this, DashActivity.this, false));

    }

    @Override
    public void onLogout() {
        mPref.setPrefrencesBoolean(AppConstants.ParmsType.isLogedIn, false);
        LogoutUtils.logOutApp(DashActivity.this);
//        activitySwitcher(DashActivity.this, LandingActivity.class, null);
        finish();
    }

    //double tap to close app
    boolean doubleBackToExitPressedOnce;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            cUtils.getInstance(DashActivity.this).ShowToast(getResources().getString(R.string.str_click_back_to_exit));
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3500);
        }
    }

    @Override
    public void performSwipe(int size) {

    }

    @Override
    public void onItemClicked(int itemPosition, Object dataObject) {

    }

    private void swiptRequesApiCall(int swipeType) {
        Log.e(" swiptRequesApiCall ", "  " + currentSwipedPropertyData.getName());
        ///   call API on Swipe
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        switch (swipeType) {
            case AppConstants.SwipeType.LIKE:
                isSwipeOpened = false;
//                Call<PropertyListResponseModel> call = apiInterface.likeProperty(swipeRequest);
                Call<DefaultResponse> call = apiInterface.likeProperty(userId, String.valueOf(currentSwipedPropertyData.getPropertyID()));
                call.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.LIKE_PROPERTY, this, DashActivity.this, false));
                break;
            case AppConstants.SwipeType.CROSS:
                isSwipeOpened = false;
                Call<DefaultResponse> call2 = apiInterface.dislikeProperty(userId, String.valueOf(currentSwipedPropertyData.getPropertyID()));
                call2.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.DISLIKE_PROPERTY, this, DashActivity.this, false));
                break;
        }

    }

}
