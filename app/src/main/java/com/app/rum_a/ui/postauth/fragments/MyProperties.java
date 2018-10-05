package com.app.rum_a.ui.postauth.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.rum_a.R;
import com.app.rum_a.core.BaseFragment;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.activity.AddPropertyActivity;
import com.app.rum_a.ui.postauth.activity.PropertyDetailActivity;
import com.app.rum_a.ui.postauth.adapter.PropertyListAdapter;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.appinterface.CustomeDialogClickListner;
import com.app.rum_a.utils.appinterface.OnItemClickListener;
import com.app.rum_a.utils.views.RumTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by harish on 28/8/18.
 */

public class MyProperties extends BaseFragment implements RestCallback {
    @Named(DaggerValues.AUTH)
    @Inject
    RestService apiInterface;
    @Inject
    PreferenceManager mPref;
    @Inject
    CommonUtils cUtils;


    @BindView(R.id.recyclar_View_lst)
    RecyclerView recyclarViewLst;
    Unbinder unbinder;
    @BindView(R.id.swipe_view)
    SwipeRefreshLayout swipeView;
    @BindView(R.id.txt_error)
    RumTextView txtError;
    private LinearLayoutManager propertyLayoutManager;
    private PropertyListAdapter propertyAdapter;
    private PopupWindow popupWindow;

    public static MyProperties newInstance(Context context) {
        MyProperties f = new MyProperties();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((RumApplication) getActivity().getApplication()).getMyComponent().inject(this);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_myproperties, null);
        unbinder = ButterKnife.bind(this, root);
        initRecyclar();
        getPropertyList();
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                getPropertyList();
            }
        });
        return root;
    }

    private void getPropertyList() {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<PropertyListResponseModel> call = apiInterface.getProperty(userId);
        call.enqueue(new RestProcess<PropertyListResponseModel>(AppConstants.ServiceModes.GET_PROPERTIES, this, getActivity(), true));
    }

    private void initRecyclar() {
        propertyAdapter = new PropertyListAdapter(getActivity(), true, new ArrayList<PropertyListResponseModel.ResultBean>(),
                new OnItemClickListener<PropertyListResponseModel.ResultBean>() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object t) {
                        switch (type) {
                            case AppConstants.Clickerations.IMAGE_VIEW_CLICK:
                                PropertyListResponseModel.ResultBean dataitem = (PropertyListResponseModel.ResultBean) t;
                                startActivity(new Intent(getActivity(), PropertyDetailActivity.class).
                                        putExtra(AppConstants.ParmsType.PROPERTY_DETAIL, dataitem)
                                        .putExtra(AppConstants.ParmsType.PROPERTY_ID, dataitem.getPropertyID()));
                                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                break;
                            case AppConstants.Clickerations.OPTION_VIEW_CLICK:
                                ImageView imgOption = view.findViewById(R.id.optionBtn);
                                if (popupWindow != null) {
                                    if (popupWindow.isShowing()) {
                                        popupWindow.dismiss();
                                    } else {
                                        openPopup(imgOption, (PropertyListResponseModel.ResultBean) t, position);
                                    }
                                } else {
                                    openPopup(imgOption, (PropertyListResponseModel.ResultBean) t, position);
                                }
                                break;
                        }
                    }
                });
        propertyLayoutManager = new LinearLayoutManager(getActivity());
        recyclarViewLst.setLayoutManager(propertyLayoutManager);
        recyclarViewLst.setAdapter(propertyAdapter);
    }

    private void openPopup(ImageView view, final PropertyListResponseModel.ResultBean t, final int position) {
        try {
            LayoutInflater layoutInflater
                    = (LayoutInflater) getActivity().getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.layout_popup_options_property, null);
            popupWindow = new PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            TextView editBtn = popupView.findViewById(R.id.edit_btn);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    editPropertyProperty(t);
                }
            });
            TextView deleteBtn = popupView.findViewById(R.id.delete_btn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    cUtils.DialogSingleButton(getActivity(), getString(R.string.str_delete_property), getString(R.string.str_delete_message),
                            new CustomeDialogClickListner() {
                        @Override
                        public void onOkClick() {
                            deleteProperty(t.getPropertyID());
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });

                    propertyAdapter.removeProperty(position);
                }
            });

            popupWindow.showAsDropDown(view, 0, 20);
            popupWindow.setOutsideTouchable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editPropertyProperty(PropertyListResponseModel.ResultBean t) {
        Intent intent = new Intent(getActivity(), AddPropertyActivity.class);
        intent.putExtra(AppConstants.ParmsType.IS_EDIT, true);
        intent.putExtra(AppConstants.ParmsType.PROPERTY_DETAIL, t);
        startActivityForResult(intent, AppConstants.RequestCode.EDIT_PROPERTY_REQUEST);
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void deleteProperty(int propertyID) {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<PropertyListResponseModel> call = apiInterface.deleteProperty(userId, propertyID);
        call.enqueue(new RestProcess<PropertyListResponseModel>(AppConstants.ServiceModes.DELETE_PROPERTY, this, getActivity(), true));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {
        swipeView.setRefreshing(false);
    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.GET_PROPERTIES:
                swipeView.setRefreshing(false);
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
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(" onActivityResult ", " " + requestCode);
        switch (requestCode) {
            case AppConstants.RequestCode.EDIT_PROPERTY_REQUEST:
                getPropertyList();
                break;
        }
    }

    @Override
    public void onLogout() {
        CommonUtils.getInstance(getActivity()).LogoutUser();
    }
}
