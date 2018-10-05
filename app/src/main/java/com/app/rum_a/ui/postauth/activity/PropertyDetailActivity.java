package com.app.rum_a.ui.postauth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.resp.ChatUsersModelResponse;
import com.app.rum_a.model.resp.PropertyDetailResponseModel;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.model.resp.SaveChatInstanceModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.adapter.HorizontalAdapterChats;
import com.app.rum_a.ui.postauth.adapter.ImagesPagerAdapter;
import com.app.rum_a.ui.postauth.qbloxui.activities.ChatScreenActivity;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatHelper;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.Consts;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.DialogsDAO;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.DialogsManager;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.appinterface.OnItemClickListener;
import com.app.rum_a.utils.views.RumTextView;
import com.google.gson.Gson;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

import static com.quickblox.chat.model.QBDialogType.GROUP;

/**
 * Created by harish on 12/9/18.
 */

public class PropertyDetailActivity extends BaseActivity implements RestCallback {
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
    @BindView(R.id.property_pager_images)
    ViewPager propertyPagerImages;
    @BindView(R.id.property_name)
    RumTextView propertyName;
    @BindView(R.id.property_price)
    RumTextView propertyPrice;
    @BindView(R.id.property_address)
    RumTextView propertyAddress;
    @BindView(R.id.property_type)
    RumTextView propertyType;
    @BindView(R.id.property_desc)
    RumTextView propertyDesc;
    @BindView(R.id.owner_image)
    CircleImageView ownerImage;
    @BindView(R.id.owner_name)
    RumTextView ownerName;
    @BindView(R.id.owner_address)
    RumTextView ownerAddress;
    @BindView(R.id.imgchat)
    ImageView chatIcon;
    @BindView(R.id.recyclar_View_lst)
    RecyclerView recyclarViewLst;
    @BindView(R.id.chat_users_holder)
    LinearLayout chatUsersHolder;
    @BindView(R.id.txt_error)
    RumTextView txtError;
    @BindView(R.id.property_type_text)
    RumTextView propertyTypeText;
    @BindView(R.id.notification_status_img)
    ImageView notificationStatusImg;
    private PropertyListResponseModel.ResultBean propertDetail;
    private String chatDialog;
    private LinearLayoutManager layoutManager;
    private HorizontalAdapterChats adapter;
    private boolean isPropertyOwner = false;
    private int propertyId;

    @Override
    public int getLayoutId() {
        return R.layout.propery_detail_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        txtTitleToolbar.setText(R.string.str_property_detail);
        getIntentData(getIntent());
        initQbuser();
        propertyPagerImages.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cUtils.FullImageScreem(PropertyDetailActivity.this, propertDetail.getPropertyImageList().get(position).getImageURL());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initQbuser() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    try {
                        QBChatService.getInstance().login(PreferenceManager.getInstance(PropertyDetailActivity.this).getQbUser());
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SmackException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void getIntentData(Intent intent) {
        propertyId = intent.getIntExtra(AppConstants.ParmsType.PROPERTY_ID, 0);
        Serializable propertyDetail = intent.getSerializableExtra(AppConstants.ParmsType.PROPERTY_DETAIL);
        if (propertyDetail != null) {
            propertDetail = (PropertyListResponseModel.ResultBean) propertyDetail;
            setdetailedData(propertDetail);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getPropertyDetail();
    }

    private void initRecyclar() {
        layoutManager = new LinearLayoutManager(PropertyDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclarViewLst.setLayoutManager(layoutManager);
        adapter = new HorizontalAdapterChats(this, new ArrayList<ChatUsersModelResponse.ResultBean>(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type, Object t) {
                switch (type) {
                    case AppConstants.Clickerations.IMAGE_VIEW_CLICK:
                        chatDialog = ((ChatUsersModelResponse.ResultBean) t).getChatDialog();
                        Intent intent = new Intent(PropertyDetailActivity.this, ChatScreenActivity.class);
                        intent.putExtra(Consts.REQUEST_CODE.EXTRA_DIALOG_ID, chatDialog);
                        intent.putExtra(AppConstants.ParmsType.isAlreadyChatted, true);
                        intent.putExtra(AppConstants.ParmsType.USER_ID, "" + ((ChatUsersModelResponse.ResultBean) t).getSenderId());
                        intent.putExtra(AppConstants.ParmsType.PROPERTY_DETAIL, propertDetail);
                        intent.putExtra(AppConstants.ParmsType.USER_NAME, ((ChatUsersModelResponse.ResultBean) t).getSenderName());
                        intent.putExtra(AppConstants.ParmsType.USER_IMAGE, ((ChatUsersModelResponse.ResultBean) t).getSenderImage().get(0).getImageURL());
                        startActivity(intent);
                        break;
                }
            }
        });
        recyclarViewLst.setAdapter(adapter);

    }

    private void getPropertyDetail() {
        int userID = new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId();
        Call<PropertyDetailResponseModel> call = apiInterface.getPropertyDetail(propertyId, userID);
        call.enqueue(new RestProcess<PropertyDetailResponseModel>(AppConstants.ServiceModes.GET_PROPERTY_DETAIL, this, PropertyDetailActivity.this, true));


    }

    private void getChatDialogs(Integer ownerId) {
//        String senderId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<ChatUsersModelResponse> call = apiInterface.getPropertyChatDialog(propertDetail.getPropertyID(), ownerId);
        call.enqueue(new RestProcess<ChatUsersModelResponse>(AppConstants.ServiceModes.GET_CHAT_DIALOGS, this, PropertyDetailActivity.this, true));


    }

    private void setdetailedData(PropertyListResponseModel.ResultBean propertDetail) {
        propertyAddress.setText(propertDetail.getAddress());
        propertyDesc.setText(propertDetail.getDescription());
        propertyName.setText(propertDetail.getName());
        propertyPrice.setText(CommonUtils.getCurrencySymbol(propertDetail.getCurrency()) + propertDetail.getPrice());
        ImagesPagerAdapter adapter = new ImagesPagerAdapter((RumApplication) RumApplication.getInstance(), PropertyDetailActivity.this,
                propertDetail.getPropertyImageList(), new OnItemClickListener<PropertyListResponseModel.ResultBean.PropertyImageListBean>() {
            @Override
            public void onItemClick(View view, int position, int type, Object t) {

            }
        });
        propertyPagerImages.setAdapter(adapter);

        setPropertyType(propertDetail.getPropertyType());

        ownerName.setText(propertDetail.getOwnerDetails().getOwnerFirstName() + " " + propertDetail.getOwnerDetails().getOwnerLastName());
        ownerAddress.setText(propertDetail.getOwnerDetails().getAddress());
        List<PropertyListResponseModel.OwnerProfilePic> ownerImageLst = propertDetail.getOwnerDetails().getOwnerProfilePics();
        new ImageUtility(PropertyDetailActivity.this).LoadImage(CommonUtils.getValidUrl(ownerImageLst.get(0).getImageURL()), ownerImage);
        if (propertDetail.getOwnerDetails().getOwnerId() == getUserDeatil().getUserId()) {
            ownerName.setText(R.string.str_your_property);
            chatUsersHolder.setVisibility(View.VISIBLE);
            initRecyclar();
            getChatDialogs(propertDetail.getOwnerDetails().getOwnerId());
            isPropertyOwner = true;
            propertyTypeText.setText(cUtils.getlookingType(propertDetail.getSellingType()));
            propertyTypeText.setVisibility(View.VISIBLE);
            propertyTypeText.bringToFront();
        } else {
            propertyTypeText.bringToFront();
            propertyTypeText.setVisibility(View.VISIBLE);
            if (propertDetail.getForRentOrBuy() + "" != null) {
                if (propertDetail.getForRentOrBuy() == AppConstants.ForRentOrBuy.Rent)
                    propertyTypeText.setText(R.string.str_rent);
                else propertyTypeText.setText(R.string.str_buy);
            } else propertyTypeText.setText(R.string.str_rent);
            checkChatStatus();
        }

    }

    private void checkChatStatus() {
        try {
            if (propertDetail != null && propertDetail.getChatDialog() != null && !propertDetail.getChatDialog().equals("")) {
                Set<String> dialogsIds = new HashSet<String>() {{
                    add(propertDetail.getChatDialog());
                }};
                QBRestChatService.getTotalUnreadMessagesCount(dialogsIds, null).performAsync(new QBEntityCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer, Bundle bundle) {
                        Log.e("TAG", " Unread :  " + bundle.getInt(propertDetail.getChatDialog()));
                        if (bundle.getInt(propertDetail.getChatDialog()) > 0) {
                            notificationStatusImg.setVisibility(View.VISIBLE);
                        } else notificationStatusImg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            } else notificationStatusImg.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPropertyType(int propertType) {
        switch (propertType) {
            case AppConstants.Seekingtype.House:
                propertyType.setText(R.string.str_house);
                break;
            case AppConstants.Seekingtype.Apartment:
                propertyType.setText(R.string.str_apartment);
                break;
            case AppConstants.Seekingtype.Commercial_Space:
                propertyType.setText(R.string.str_commercial);
                break;
            case AppConstants.Seekingtype.Land:
                propertyType.setText(R.string.str_Land);
                break;

        }
    }

    @OnClick({R.id.back_toolbar, R.id.property_desc, R.id.owner_image, R.id.imgchat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.imgchat:
                if (!isPropertyOwner) {
                    if (chatDialog != null) {
                        continueChat();
                    } else
                        initChat();
                } else {
                    if (chatUsersHolder.getVisibility() == View.VISIBLE)
                        chatUsersHolder.setVisibility(View.GONE);
                    else chatUsersHolder.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.owner_image:
                startActivity(new Intent(PropertyDetailActivity.this, ProfileActivity.class)
                        .putExtra(AppConstants.ParmsType.USER_ID, propertDetail.getOwnerDetails().getOwnerId()));
                finish();
                break;

        }
    }

    private void continueChat() {
        Intent intent = new Intent(PropertyDetailActivity.this, ChatScreenActivity.class);
        intent.putExtra(Consts.REQUEST_CODE.EXTRA_DIALOG_ID, chatDialog);
        intent.putExtra(AppConstants.ParmsType.isAlreadyChatted, true);
        intent.putExtra(AppConstants.ParmsType.USER_ID, "" + propertDetail.getOwnerDetails().getOwnerId());
        intent.putExtra(AppConstants.ParmsType.PROPERTY_DETAIL, propertDetail);
        intent.putExtra(AppConstants.ParmsType.USER_NAME, propertDetail.getOwnerDetails().getOwnerFirstName());
        intent.putExtra(AppConstants.ParmsType.USER_IMAGE, propertDetail.getOwnerDetails().getOwnerProfilePics().get(0).getImageURL());
        startActivity(intent);
    }

    private List<Integer> makeUsersList(ArrayList<QBUser> selectedUsers) {
        List<Integer> usersList = new ArrayList<>();
        for (int i = 0; i < selectedUsers.size(); i++) {
            usersList.add(selectedUsers.get(i).getId());
        }
        return usersList;
    }

    private void initChat() {
        try {
            showLoading();
            List<QBUser> selecteduser = new ArrayList<>();
            selecteduser.add(PreferenceManager.getInstance(PropertyDetailActivity.this).getQbUser());
            QBUser user = new QBUser();
            user.setPhone(String.valueOf(propertDetail.getOwnerDetails().getOwnerId()));
            user.setId(Integer.parseInt(propertDetail.getOwnerDetails().getQbId()));
//        user.setEmail(propertDetail.getOwnerDetails().g);
            user.setLogin(String.valueOf(propertDetail.getOwnerDetails().getOwnerId()));
            selecteduser.add(user);
            final DialogsManager dialogsManager = new DialogsManager();
            final QBSystemMessagesManager systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
//        ProgressDialogFragment.show(getActivity().getSupportFragmentManager(), R.string.please_wait);
            ChatHelper.getInstance().createGroupChatDialogWithSelectedUsers(makeUsersList((ArrayList<QBUser>) selecteduser), propertDetail.getName(),
                    new QBEntityCallback<QBChatDialog>() {
                        @Override
                        public void onSuccess(QBChatDialog dialog, Bundle args) {
                            try {
                                hideLoading();
                                dialog.setType(GROUP);

//                            dialog.setOccupantsIds(occupntList);
                                DialogsDAO.getInstance().insertDailogs(PropertyDetailActivity.this, dialog);
//                            ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());
                                dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                                chatDialog = dialog.getDialogId();
                                saveChatDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            try {
                                CommonUtils.getInstance(PropertyDetailActivity.this).ShowToast(e.getMessage());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
            );
        } catch (Exception e) {
//            cUtils.ShowToast(getString(R.string.msg_error));
        }
    }

    private void saveChatDialog() {
        String senderId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<SaveChatInstanceModel> call = apiInterface.seveChatDialog(String.valueOf(propertDetail.getPropertyID()),
                String.valueOf(propertDetail.getOwnerDetails().getOwnerId()),
                chatDialog, senderId, propertDetail.getOwnerDetails().getOwnerId().toString(), cUtils.getTimestampOther(), "First Message");
        call.enqueue(new RestProcess<SaveChatInstanceModel>(AppConstants.ServiceModes.SAVE_CHAT_DIALOG, this, PropertyDetailActivity.this, true));

    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.GET_CHAT_DIALOGS:
                ChatUsersModelResponse responseModel = (ChatUsersModelResponse) model.body();
                if (responseModel.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    if (responseModel.getResult() != null && responseModel.getResult().size() > 0) {
                        adapter.update(responseModel.getResult());
                        recyclarViewLst.setVisibility(View.VISIBLE);
                        txtError.setVisibility(View.GONE);
                    } else {
                        recyclarViewLst.setVisibility(View.GONE);
                        txtError.setVisibility(View.VISIBLE);
                        txtError.setText(R.string.str_no_one_message_you_error);
                    }
                }
                break;
            case AppConstants.ServiceModes.GET_PROPERTY_DETAIL:
                PropertyDetailResponseModel responseData = (PropertyDetailResponseModel) model.body();
                if (responseData.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    Log.e("GET_PROPERTY_DETAIL ", "  " + new Gson().toJson(responseData));
                    checkIsChatDialog(responseData.getResult());
                    propertDetail = responseData.getResult();
                    setdetailedData(responseData.getResult());
                }
                break;
            case AppConstants.ServiceModes.SAVE_CHAT_DIALOG:
                SaveChatInstanceModel defaultResponse = (SaveChatInstanceModel) model.body();
                if (defaultResponse.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    Intent intent = new Intent(PropertyDetailActivity.this, ChatScreenActivity.class);
                    intent.putExtra(Consts.REQUEST_CODE.EXTRA_DIALOG_ID, chatDialog);
                    intent.putExtra(AppConstants.ParmsType.isAlreadyChatted, true);
                    intent.putExtra(AppConstants.ParmsType.USER_ID, "" + propertDetail.getOwnerDetails().getOwnerId());
                    intent.putExtra(AppConstants.ParmsType.PROPERTY_DETAIL, propertDetail);
                    intent.putExtra(AppConstants.ParmsType.USER_NAME, propertDetail.getOwnerDetails().getOwnerFirstName());
                    intent.putExtra(AppConstants.ParmsType.USER_IMAGE, propertDetail.getOwnerDetails().getOwnerProfilePics().get(0).getImageURL());
                    startActivityForResult(intent, AppConstants.RequestCode.CHAT_DIALOG_NOTIFY);
                } else
                    cUtils.ShowToast(getString(R.string.msg_error));
                break;
        }

    }

    private void checkIsChatDialog(PropertyListResponseModel.ResultBean result) {
        if (result != null) {
            if (result.getChatDialog() != null) {
                chatDialog = result.getChatDialog();
            } else {
                chatDialog = null;
            }
        }
    }

    @Override
    public void onLogout() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstants.RequestCode.CHAT_DIALOG_NOTIFY:
//                if (requestCode == RESULT_OK)
//                    chatDialog = data.getStringExtra(Consts.REQUEST_CODE.EXTRA_DIALOG_ID);
//                Log.e("onActivityResult", " " + chatDialog);
                break;
        }
    }
}
