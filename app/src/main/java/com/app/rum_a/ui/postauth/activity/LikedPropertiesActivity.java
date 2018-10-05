package com.app.rum_a.ui.postauth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.resp.DefaultResponse;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.adapter.PropertyListAdapter;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
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
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 17/9/18.
 */

public class LikedPropertiesActivity extends BaseActivity implements RestCallback {
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
    @BindView(R.id.recyclar_View_lst)
    RecyclerView recyclarViewLst;
    @BindView(R.id.txt_error)
    RumTextView txtError;
    private PropertyListAdapter propertyAdapter;
    private LinearLayoutManager propertyLayoutManager;

    @Override
    public int getLayoutId() {
        return R.layout.property_list_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        txtTitleToolbar.setText(R.string.str_liked_properties_);
        initRecyclar();
        getPropertyList();
    }

    private void getPropertyList() {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<PropertyListResponseModel> call = apiInterface.getLikedProperty(userId);
        call.enqueue(new RestProcess<PropertyListResponseModel>(AppConstants.ServiceModes.GET_LIKED_PROPERTIES, this, LikedPropertiesActivity.this, true));
    }

    private void initRecyclar() {
        propertyAdapter = new PropertyListAdapter(LikedPropertiesActivity.this, false, new ArrayList<PropertyListResponseModel.ResultBean>(),
                new OnItemClickListener<PropertyListResponseModel.ResultBean>() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object t) {
                        switch (type) {
                            case AppConstants.Clickerations.IMAGE_VIEW_CLICK:
                                PropertyListResponseModel.ResultBean dataitem = (PropertyListResponseModel.ResultBean) t;
                                startActivity(new Intent(LikedPropertiesActivity.this, PropertyDetailActivity.class)
                                        .putExtra(AppConstants.ParmsType.PROPERTY_DETAIL, dataitem)
                                        .putExtra(AppConstants.ParmsType.PROPERTY_ID, dataitem.getPropertyID()));
                                break;
                            case AppConstants.Clickerations.REMOVE_VIEW_CLICK:
                                removeProperty((PropertyListResponseModel.ResultBean) t);
                                propertyAdapter.removeProperty(position);
                                break;
                        }
                    }
                });
        propertyLayoutManager = new LinearLayoutManager(LikedPropertiesActivity.this);
        recyclarViewLst.setLayoutManager(propertyLayoutManager);
        recyclarViewLst.setAdapter(propertyAdapter);
    }

    private void removeProperty(PropertyListResponseModel.ResultBean t) {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<DefaultResponse> call = apiInterface.removeLikedProperty(String.valueOf(t.getPropertyID()), userId);
        call.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.REMOVE_LIKED_PROPERTIES, this, LikedPropertiesActivity.this, true));

    }

    @OnClick({R.id.back_toolbar, R.id.txt_title_toolbar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.txt_title_toolbar:
                break;
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.GET_LIKED_PROPERTIES:
//                swipeView.setRefreshing(false);
                PropertyListResponseModel responseBody = (PropertyListResponseModel) model.body();
                if (responseBody.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    List<PropertyListResponseModel.ResultBean> propertyList = responseBody.getResult();
                    if (propertyAdapter != null && propertyList != null && propertyList.size() > 0) {
                        propertyAdapter.updateAdapter(propertyList);
                        txtError.setVisibility(View.GONE);
                        recyclarViewLst.setVisibility(View.VISIBLE);
                    } else {
                        txtError.setVisibility(View.VISIBLE);
                        recyclarViewLst.setVisibility(View.GONE);
                    }
                } else if (responseBody.getStatus() == NetworkConstatnts.ResponseCode.sessionExpred) {
                    onLogout();
                }
                break;
            case AppConstants.ServiceModes.REMOVE_LIKED_PROPERTIES:
                DefaultResponse responseData = (DefaultResponse) model.body();
                if (responseData.getStatus() == NetworkConstatnts.ResponseCode.success) {

                }
                break;
        }
    }

    @Override
    public void onLogout() {

    }
}
