package com.app.rum_a.ui.postauth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.adapter.ProfilePropertyAdapter;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.appinterface.OnItemClickListener;
import com.app.rum_a.utils.views.RumTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 31/8/18.
 */

public class ProfileActivity extends BaseActivity implements RestCallback {
    @Named(DaggerValues.AUTH)
    @Inject
    RestService apiInterface;
    @Inject
    PreferenceManager mPref;
    @Inject
    CommonUtils cUtils;
    @BindView(R.id.back_toolbar)
    ImageView backToolbar;
    @BindView(R.id.txt_title_toolbar)
    RumTextView txtTitleToolbar;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.location_icon)
    ImageView locationIcon;
    @BindView(R.id.recyclar_View_lst)
    RecyclerView recyclarViewLst;
    @BindView(R.id.profile_image_one)
    CircleImageView profileImageOne;
    @BindView(R.id.profile_image_two)
    CircleImageView profileImageTwo;
    @BindView(R.id.profile_image_three)
    CircleImageView profileImageThree;
    @BindView(R.id.txt_error)
    RumTextView txtError;
    @BindView(R.id.user_Name)
    RumTextView userName;
    @BindView(R.id.user_Address_top)
    RumTextView userAddressTop;
    @BindView(R.id.user_liked_properties)
    RumTextView userLikedProperties;
    @BindView(R.id.user_fav_properties)
    RumTextView userFavProperties;
    @BindView(R.id.user_address_distance)
    RumTextView userAddress;
    @BindView(R.id.user_address2)
    RumTextView userAddress2;
    @BindView(R.id.user_about_text)
    RumTextView userAboutText;
    @BindView(R.id.user_looking_type_txt)
    RumTextView userLookingTypeTxt;
    @BindView(R.id.user_budget_txt)
    RumTextView userBudgetTxt;
    @BindView(R.id.user_seeking_type_txt)
    RumTextView userSeekingTypeTxt;
    private GridLayoutManager gridLayoutManager;
    private ProfilePropertyAdapter propertyAdapter;
    private UserResponseModel userData;
    private int userId;

    @Override
    public int getLayoutId() {
        return R.layout.profile_screen;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        txtTitleToolbar.setText(R.string.str_profile);
        initRecyclar();
        getIntentData(getIntent());

    }

    private void getIntentData(Intent intent) {
        try {
            userId = intent.getIntExtra(AppConstants.ParmsType.USER_ID, 0);
            if (userId == 0) {
                userId = new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId();
            }
        } catch (Exception e) {
            userId = new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserProfile();
    }

    private void getUserProfile() {

        Call<UserResponseModel> call = apiInterface.getProfile(String.valueOf(userId));
        call.enqueue(new RestProcess<UserResponseModel>(AppConstants.ServiceModes.GET_PROFILE, this, ProfileActivity.this, true));
    }

    private void initRecyclar() {
        propertyAdapter = new ProfilePropertyAdapter(this, new ArrayList<PropertyListResponseModel.ResultBean>(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type, Object t) {
                switch (type) {
                    case AppConstants.Clickerations.IMAGE_VIEW_CLICK:
                        PropertyListResponseModel.ResultBean dataitem = (PropertyListResponseModel.ResultBean) t;
                        startActivity(new Intent(ProfileActivity.this, PropertyDetailActivity.class)
                                .putExtra(AppConstants.ParmsType.PROPERTY_DETAIL, dataitem)
                                .putExtra(AppConstants.ParmsType.PROPERTY_ID, dataitem.getPropertyID()));
                        break;
                }
            }
        });
        gridLayoutManager = new GridLayoutManager(ProfileActivity.this, 3, GridLayoutManager.VERTICAL, false);
        recyclarViewLst.setLayoutManager(gridLayoutManager);
        recyclarViewLst.setAdapter(propertyAdapter);
    }

    @OnClick({R.id.back_toolbar, R.id.location_icon, R.id.user_liked_properties})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.location_icon:
                break;
            case R.id.user_liked_properties:
                startActivity(new Intent(ProfileActivity.this, LikedPropertiesActivity.class)
                        .putExtra(AppConstants.ParmsType.USER_ID, userData.getResult().getUser().getUserId()));
                break;
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.GET_PROFILE:
                UserResponseModel userResponse = (UserResponseModel) model.body();
                if (userResponse.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    setUserData(userResponse);
                }
                break;
        }

    }

    private void setUserData(UserResponseModel userResponse) {
        userData = userResponse;
        userName.setText(userResponse.getResult().getUser().getFirstName() + " " + userResponse.getResult().getUser().getLastName());
        if (userResponse.getResult().getUser().getAboutMe() != null && !userResponse.getResult().getUser().getAboutMe().equals(""))
            userAboutText.setText(userResponse.getResult().getUser().getAboutMe());
        try {
            userAddressTop.setText(userResponse.getResult().getUser().getAddress());
            userAddress2.setText(userResponse.getResult().getUser().getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userBudgetTxt.setText(cUtils.getCurrencySymbol(userResponse.getResult().getUser().getCurrency()) + "" + userResponse.getResult().getUser().getBudget());
        } catch (Exception e) {
            userBudgetTxt.setText("$" + "0");

        }
        userLookingTypeTxt.setText(cUtils.getlookingType(userResponse.getResult().getUser().getLookingType()));
        userSeekingTypeTxt.setText(cUtils.getSeekingType(userResponse.getResult().getUser().getSeekingType()));
        userLikedProperties.setText("" + userResponse.getResult().getUser().getTotalLikeProperties());


        List<UserResponseModel.ResultBean.ProfilePic> userImages = userResponse.getResult().getUser().getProfilePics();
        new ImageUtility(ProfileActivity.this).LoadImage(CommonUtils.getValidUrl(userImages.get(0).getImageURL()), profileImageOne);
        new ImageUtility(ProfileActivity.this).LoadImage(CommonUtils.getValidUrl(userImages.get(1).getImageURL()), profileImageTwo);
        new ImageUtility(ProfileActivity.this).LoadImage(CommonUtils.getValidUrl(userImages.get(2).getImageURL()), profileImageThree);

        setPropertyAdapter(userResponse.getResult().getUser().getResult());
    }

    private void setPropertyAdapter(List<PropertyListResponseModel.ResultBean> result) {
        if (result != null && result.size() > 0)
            propertyAdapter.updateAll(result);
    }

    @Override
    public void onLogout() {

    }
}
