package com.app.rum_a.ui.postauth.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.resp.DefaultResponse;
import com.app.rum_a.model.resp.SaveChatInstanceModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.views.RumEditText;
import com.app.rum_a.utils.views.RumTextView;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 21/9/18.
 */

public class FeedbackActivity extends BaseActivity implements RestCallback {
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
    @BindView(R.id.feedback_text)
    RumEditText feedbackText;
    @BindView(R.id.submitt_feedback_btn)
    RumTextView submittFeedbackBtn;

    @Override
    public int getLayoutId() {
        return R.layout.layout_feedback_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        txtTitleToolbar.setText(R.string.str_feedback);
    }

    @OnClick({R.id.back_toolbar, R.id.submitt_feedback_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.submitt_feedback_btn:
                if (validation()) {
                    sendFeedback();
                }
                break;
        }
    }

    private void sendFeedback() {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<DefaultResponse> call = apiInterface.sendFeedback(userId, feedbackText.getText().toString());
        call.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.SAVE_FEEDBACK, this, FeedbackActivity.this, true));

    }

    private boolean validation() {
        if (CommonUtils.isEmpty(feedbackText)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_enter_text));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.SAVE_FEEDBACK:
                DefaultResponse defaultResponse = new DefaultResponse();
                feedbackText.setText("");
                cUtils.ShowToast(defaultResponse.getMessage());
                break;
        }
    }

    @Override
    public void onLogout() {

    }
}
