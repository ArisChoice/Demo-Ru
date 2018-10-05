package com.app.rum_a.ui.postauth.qbloxui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.modelutils.TempUserModel;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.model.resp.SaveChatInstanceModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.activity.ProfileActivity;
import com.app.rum_a.ui.postauth.activity.PropertyDetailActivity;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatHelper;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatModel;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatModelActaul;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.Consts;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.CustomQBChatMessage;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.DeleteChatDAO;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.DeleteChatDTO;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.GeneralUtils;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.UserDetailsHasMap;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.VerboseQbChatConnectionListener;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.WebRtcSessionManager;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.activities.CallActivity;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.adapter.ChatRvAdapter;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database.CallHistoryDAO;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database.CallHistoryDTO;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database.DatabaseHelper;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.views.RumEditText;
import com.app.rum_a.utils.views.RumTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBMessageStatusesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBMessageStatusListener;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.Consts.ChatValues.PROPERTY_SAVE_TO_HISTORY;

/**
 * Created by harish on 17/9/18.
 */

public class ChatScreenActivity extends BaseActivity implements RestCallback {
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
    @BindView(R.id.imgVideoCall)
    ImageView imgVideoCall;
    @BindView(R.id.imgAudioCall)
    ImageView imgAudioCall;
    @BindView(R.id.recyclar_View_lst)
    RecyclerView recyclarViewLst;
    @BindView(R.id.txt_error)
    RumTextView txtError;
    @BindView(R.id.messageField)
    RumEditText messageField;
    @BindView(R.id.send_btn)
    ImageView sendBtn;
    @BindView(R.id.chat_property_txt)
    RumTextView chatPropertyTxt;
    private boolean isRunForCall;
    private String otherUserId;
    private String otherUserName;
    private WebRtcSessionManager webRtcSessionManager;
    private QBChatDialog qbChatDialog;
    private int skipPagination = 0;
    private ChatMessageListener chatMessageListener;
    private QBMessageStatusesManager messageStatusesManager;
    private QBMessageStatusListener messageStatusListener;
    private ChatRvAdapter chatAdapter;
    private ArrayList<CustomQBChatMessage> unShownMessages;

    /**
     * hold messages id for set status of message seen
     */
    HashMap<String, String> mapMessagePosition = new LinkedHashMap<>();
    /**
     * holder position of sent messages
     */
    List<Integer> sendMessagesPostion = new LinkedList<>();
    private String TAG = ChatScreenActivity.class.getSimpleName();
    private VerboseQbChatConnectionListener chatConnectionListener;
    private QBAttachment attachment;
    private PropertyListResponseModel.ResultBean propertDetail;
    private boolean isAlreadyChat = false;
    private String qbChatDialogId;
    private String otherUserImage;

    @Override
    public int getLayoutId() {
        return R.layout.chat_screen_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        initRecyclar();
        gettingExtras();
        setupVideoSeesions();
        if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
            if (!GeneralUtils.hasPermission(ChatScreenActivity.this, Manifest.permission.RECORD_AUDIO)) {
                CommonUtils.getInstance(ChatScreenActivity.this).ShowToast("Some one calling from kuku.But Need Permission To Pick Call.Please unable from app settings");
                finish();
                return;
            }
            CallActivity.start(this, true);
            finish();
        }
        if (qbChatDialog == null) {
            return;
        }

        try {
            qbChatDialog.initForChat(QBChatService.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

    }

    private void initRecyclar() {
        recyclarViewLst.setHasFixedSize(true);
        recyclarViewLst.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initChat() {
        switch (qbChatDialog.getType()) {
            case GROUP:
            case PUBLIC_GROUP:
                joinGroupChat();
                break;

            case PRIVATE:
//                showProgress();
                loadDialogUsers();
                break;

            default:
                CommonUtils.getInstance(ChatScreenActivity.this).ShowToast("Chat not supported");
                finish();
                break;
        }

    }

    private void initChatConnectionListener() {
        chatConnectionListener = new VerboseQbChatConnectionListener(recyclarViewLst) {
            @Override
            public void reconnectionSuccessful() {
                super.reconnectionSuccessful();
                skipPagination = 0;
                switch (qbChatDialog.getType()) {
                    case GROUP:
                        chatAdapter = null;
                        // Join active room if we're in Group Chat
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joinGroupChat();
                            }
                        });
                        break;
                }
            }
        };
    }

    private void setUpListeners() {
        chatMessageListener = new ChatMessageListener();
        qbChatDialog.addMessageListener(chatMessageListener);
        initMessageStatusManagerAndListener();
    }

    private void initMessageStatusManagerAndListener() {
        messageStatusesManager = QBChatService.getInstance().getMessageStatusesManager();
        messageStatusListener = new QBMessageStatusListener() {
            @Override
            public void processMessageDelivered(String messageId, String dialogId, Integer userId) {

                try {
                    mapMessagePosition.put(messageId, sendMessagesPostion.get(0) + "");
                    sendMessagesPostion.remove(0);
                    Collection<Integer> ids = chatAdapter.getList().get(Integer.parseInt(mapMessagePosition.get(messageId))).getDeliveredIds();
                    ids.remove(userId);
                    ids.add(userId);
                    chatAdapter.getList().get(Integer.parseInt(mapMessagePosition.get(messageId))).setDeliveredIds(ids);
                    chatAdapter.notifyDataSetChanged();

//                    Collection<Integer> ids = chatAdapter.getList().get(chatAdapter.getList().size() - 1).getDeliveredIds();
//                    ids.remove(userId);
//                    ids.add(userId);
//                    chatAdapter.getList().get(chatAdapter.getList().size() - 1).setDeliveredIds(ids);
//                    chatAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
                Log.d("message delivered: ", "" + messageId + " to user " + userId
                        + ". DialogId: " + dialogId);
            }

            @Override
            public void processMessageRead(String messageId, String dialogId, Integer userId) {
                Log.d("message read: ", "" + messageId + " to user " + userId
                        + ". DialogId: " + dialogId);
                try {
                    if (chatAdapter != null) {
                        Collection<Integer> ids = chatAdapter.getList().get(Integer.parseInt(mapMessagePosition.get(messageId))).getReadIds();
                        ids.add(userId);
                        chatAdapter.getList().get(Integer.parseInt(mapMessagePosition.get(messageId))).setReadIds(ids);
                        chatAdapter.notifyDataSetChanged();

//                        Collection<Integer> ids = chatAdapter.getList().get(chatAdapter.getList().size() - 1).getReadIds();
//                        ids.add(userId);
//                        chatAdapter.getList().get(chatAdapter.getList().size() - 1).setReadIds(ids);
//                        chatAdapter.notifyDataSetChanged();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        };
        messageStatusesManager.addMessageStatusListener(messageStatusListener);

    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {

    }

    @Override
    public void onLogout() {

    }

    public class ChatMessageListener implements QBChatDialogMessageListener {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {

            if (!TextUtils.isEmpty(qbChatMessage.getBody()) && qbChatMessage.getBody().equalsIgnoreCase(Consts.ChatValues.MESSAGE_SEEN)) {
                setAllMessageSeen();
                return;
            }

            mapMessagePosition.put(qbChatMessage.getId(), mapMessagePosition.size() + "");
            if (qbChatMessage.isMarkable()) {
                try {
                    qbChatDialog.readMessage(qbChatMessage);
                } catch (XMPPException | SmackException.NotConnectedException e) {
                }
            }

            String newMessage = new Gson().toJson(qbChatMessage);
            CustomQBChatMessage customQBChatMessage = new Gson().fromJson(newMessage, CustomQBChatMessage.class);
            showMessage(customQBChatMessage);
        }

        @Override
        public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
            Log.d("processError: ", "   " + e.getLocalizedMessage());
        }
    }

    private void joinGroupChat() {
        Log.d("joinGroupChat: ", " --------------------------------------  " + qbChatDialog.getDialogId());
//        showLoading();
        try {
            ChatHelper.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void result, Bundle b) {
                    loadDialogUsers();
                }

                @Override
                public void onError(QBResponseException e) {
                    try {
                        CommonUtils.getInstance(ChatScreenActivity.this).ShowToast(e.getMessage());
                        hideLoading();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private void setupVideoSeesions() {
        webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
    }

    public void loadDialogUsers() {
        try {
            Log.d("loadDialogUsers: ", " --------------------------------------  ");
            ChatHelper.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                    loadChatHistory();
                }

                @Override
                public void onError(QBResponseException e) {
                    hideLoading();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadChatHistory() {

        Log.d("loadChatHistory: ", " --------------------------------------  " + qbChatDialog.getDialogId());
        ChatHelper.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messagesRec, Bundle args) {
                // The newest messages should be in the end of list,
                // so we need to reverse list to show messages in the right order
                hideLoading();
                sendChatSeenMessage();
                markedmessageasRead();
                String messagesString = new Gson().toJson(messagesRec).toString();
                Type typeOfObjectsList = new TypeToken<ArrayList<CustomQBChatMessage>>() {
                }.getType();

                List<CustomQBChatMessage> messages = new Gson().fromJson(messagesString, typeOfObjectsList);

                Collections.reverse(messages);
                setDataInMap(messages);

                //  if (chatAdapter == null) {
                chatAdapter = new ChatRvAdapter(ChatScreenActivity.this, ChatScreenActivity.this, qbChatDialog, messages, getUserDeatil(), getOtherUser());
                if (unShownMessages != null && !unShownMessages.isEmpty()) {
                    List<CustomQBChatMessage> chatList = chatAdapter.getList();
                    for (CustomQBChatMessage message : unShownMessages) {
                        try {
                            if (!chatList.contains(message)) {
                                chatAdapter.add(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                recyclarViewLst.setAdapter(chatAdapter);
//                setUpDecorator();
                recyclarViewLst.scrollToPosition(messages.size() - 1);
//                    mMessageListView.setAreHeadersSticky(false);
//                    mMessageListView.setDivider(null);
               /* } else {
                    chatAdapter.addList(messages);
                    recyclerView.scrollToPosition(messages.size() - 1);
                }*/
//                hideProgress();
            }

            @Override
            public void onError(QBResponseException e) {
                try {
                    hideLoading();
                    CommonUtils.getInstance(ChatScreenActivity.this).ShowToast(e.getMessage());
//                    ProgressDialogFragment.hide(getSupportFragmentManager());
                    skipPagination -= ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        skipPagination += ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
    }

    private TempUserModel getOtherUser() {
        TempUserModel otherUser = new TempUserModel();
        otherUser.setUserId(otherUserId);
        otherUser.setUserImage(otherUserImage);
        otherUser.setUserName(otherUserName);
        return otherUser;
    }

    private void gettingExtras() {

        try {
            isRunForCall = getIntent().getBooleanExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            otherUserId = getIntent().getStringExtra(AppConstants.ParmsType.USER_ID);
            otherUserName = getIntent().getStringExtra(AppConstants.ParmsType.USER_NAME);
            otherUserImage = getIntent().getStringExtra(AppConstants.ParmsType.USER_IMAGE);
            txtTitleToolbar.setText(otherUserName);
            isAlreadyChat = getIntent().getBooleanExtra(AppConstants.ParmsType.isAlreadyChatted, false);
            showLoading();
            if (!isAlreadyChat) {
                qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(Consts.REQUEST_CODE.EXTRA_DIALOG_ID);
                Serializable propertyDetail = getIntent().getSerializableExtra(AppConstants.ParmsType.PROPERTY_DETAIL);
                propertDetail = (PropertyListResponseModel.ResultBean) propertyDetail;
                qbChatDialogId = qbChatDialog.getDialogId();
                propertDetail.setChatDialog(qbChatDialogId);
                saveChatDialog();
                initChat();
                initChatConnectionListener();
                setUpListeners();

            } else {
                qbChatDialogId = getIntent().getStringExtra(Consts.REQUEST_CODE.EXTRA_DIALOG_ID);
                joinChatDialog();
                Serializable propertyDetail = getIntent().getSerializableExtra(AppConstants.ParmsType.PROPERTY_DETAIL);
                propertDetail = (PropertyListResponseModel.ResultBean) propertyDetail;
            }
            chatPropertyTxt.setText(propertDetail.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey(Constants.IntentVariables.USER_ID)) {
            userId = extras.getString(Constants.IntentVariables.USER_ID);
            position = extras.getInt(Constants.IntentVariables.POSITION);
        }
        if (extras != null && extras.containsKey(Constants.IntentVariables.HIT_MATCH)) {
            isMatch = true;
        }*/

    }

    private void joinChatDialog() {
        QBRestChatService.getChatDialogById(qbChatDialogId).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog dialog, Bundle bundle) {
                Log.d(TAG, "getChatDialogById " + dialog.getName() + " QBChatDialog TYPE= " + dialog.getType());
                qbChatDialog = dialog;
//                deleteActionManage(false);
                if (qbChatDialog == null) {
                    return;
                }

                try {
                    qbChatDialog.initForChat(QBChatService.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
                //setUpAdpter();
                initChat();
                initChatConnectionListener();
                setUpListeners();
            }

            @Override
            public void onError(QBResponseException e) {

                Log.d(TAG, "getChatDialogById QBResponseException onError " + e.getMessage());
//                hideCustomDialog();
            }
        });
    }

    private void saveChatDialog() {
        String senderId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<SaveChatInstanceModel> call = apiInterface.seveChatDialog(String.valueOf(propertDetail.getPropertyID()),
                String.valueOf(propertDetail.getOwnerDetails().getOwnerId()),
                qbChatDialog.getDialogId(), senderId, otherUserId, cUtils.getTimestampOther(), "First Message");
        call.enqueue(new RestProcess<SaveChatInstanceModel>(AppConstants.ServiceModes.SAVE_CHAT_DIALOG, this, ChatScreenActivity.this, true));

    }

    public static void start(Context context, boolean isRunForCall) {
        Intent intent = new Intent(context, ChatScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        releaseChat();

        if (chatAdapter.checkSelectedExist()) {
            chatAdapter.unSelectDelete();
            return;

        }
        sendDialogId();

        super.onBackPressed();
    }

    private void sendDialogId() {
        Intent result = new Intent();
        result.putExtra(Consts.REQUEST_CODE.EXTRA_DIALOG_ID, qbChatDialogId);
        setResult(RESULT_OK, result);
    }

    @OnClick({R.id.back_toolbar, R.id.imgVideoCall, R.id.imgAudioCall, R.id.send_btn, R.id.txt_title_toolbar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.imgVideoCall:
                videoCall();
                break;
            case R.id.imgAudioCall:
                if (!GeneralUtils.hasPermission(this, Manifest.permission.RECORD_AUDIO)) {
                    cUtils.getInstance(ChatScreenActivity.this).ShowToast("Permission required");
                    return;
                }
                startCall(false);
                break;
            case R.id.send_btn:
                sendChatMessage();
                break;
            case R.id.txt_title_toolbar:
                startActivity(new Intent(ChatScreenActivity.this, ProfileActivity.class)
                        .putExtra(AppConstants.ParmsType.USER_ID, Integer.parseInt(otherUserId)));
                break;
        }
    }

    private void sendChatMessage() {
        attachment = null;
        if (TextUtils.isEmpty(messageField.getText().toString())) {
            return;
        }
        QBChatMessage chatMessage = new QBChatMessage();
        if (attachment != null) {
            if (attachment.getType().contains("video")) {
                sendMessageNow("Attachment video", "");
                chatMessage.setBody("Attachment video");
            } else if (attachment.getType().equalsIgnoreCase(Consts.ChatValues.SHARE_STICKER)) {
                chatMessage.setBody("Sticker");
//                sendMessageNow("Sticker", "");
            } else {
                chatMessage.setBody("Attached image");
//                sendMessageNow("Attactment", "");
            }
            chatMessage.addAttachment(attachment);
        } else {
            sendMessageNow(messageField.getText().toString(), "");
            chatMessage.setBody(messageField.getText().toString());
        }

        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setDateSent(System.currentTimeMillis() / 1000);
        chatMessage.setMarkable(true);
        Collection<Integer> id = new ArrayList<>();
        id.add(PreferenceManager.getInstance(ChatScreenActivity.this).getQbUser().getId());
        chatMessage.setDeliveredIds(id);
        chatMessage.setReadIds(id);
        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType()) && !qbChatDialog.isJoined()) {
            cUtils.getInstance(ChatScreenActivity.this).ShowToast("You're still joining a group chat, please wait a bit");
            return;
        }

        try {
            qbChatDialog.sendMessage(chatMessage);
            if (QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                String newMessage = new Gson().toJson(chatMessage);

                CustomQBChatMessage customQBChatMessage = new Gson().fromJson(newMessage, CustomQBChatMessage.class);
                showMessage(customQBChatMessage);
                if (chatAdapter.getList() != null)
                    sendMessagesPostion.add(chatAdapter.getList().size() - 1);
//                mapMessagePosition.put(customMessasgeId + "", mapMessagePosition.size() + "");
//                customMessasgeId++;
            }

            if (attachment != null) {
//                attachmentPreviewAdapter.remove(attachment);
            } else {
                messageField.setText("");
            }
        } catch (SmackException.NotConnectedException e) {
//            Log.w(TAG, e);
//            Toaster.shortToast("Can't send a message, You are not connected to chat");
        }
    }

    private void sendMessageNow(String message, String isCalling) {
        // Send Push: create QuickBlox Push Notification Event
        QBEvent qbEvent = new QBEvent();
        qbEvent.setNotificationType(QBNotificationType.PUSH);
        qbEvent.setEnvironment(QBEnvironment.DEVELOPMENT);
        ChatModelActaul apn = new ChatModelActaul();
//        ChatModel chatModel = new ChatModel();
//        JSONObject json = new JSONObject();

        try {

            apn.setMessage(PreferenceManager.getInstance(ChatScreenActivity.this).getQbUser().getFullName() + " : " + message);
            apn.setIos_badge("");
            apn.setIos_sound(Consts.NOTIFICATION_CODE.APN_SOUND_BLANK);
            ChatModel.Gcm gcm = new ChatModel.Gcm();
            gcm.setNOTI_TITLE(getUserDeatil().getFirstName() + " " + getUserDeatil().getLastName());
            gcm.setNOTI_ID(PreferenceManager.getInstance(ChatScreenActivity.this).getQbUser().getId() + "");
            gcm.setNOTI_MESSAGE(message);
            gcm.setPropertyBody(new Gson().toJson(propertDetail));
            gcm.setSenderAppUserId(otherUserId);
            gcm.setSenderAppUserImage(otherUserImage);
            apn.setGcm(gcm);
            if (TextUtils.isEmpty(isCalling)) {
                apn.setType(Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_MESSAGE);
            } else if (isCalling.equalsIgnoreCase(Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_VIDEO)) {
                apn.setType(Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_VIDEO);
            } else if (isCalling.equalsIgnoreCase(Consts.NOTIFICATION_CODE.NOTFIFICATION_TYPE_AUDIO)) {
                apn.setType(Consts.NOTIFICATION_CODE.NOTFIFICATION_TYPE_AUDIO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        qbEvent.setMessage(new Gson().toJson(apn).toString());

        List<Integer> opponentsList = qbChatDialog.getOccupants();
        opponentsList.remove(PreferenceManager.getInstance(ChatScreenActivity.this).getQbUser().getId());
        StringifyArrayList<Integer> userIds = new StringifyArrayList<>();
        userIds.addAll(opponentsList);
        qbEvent.setUserIds(userIds);

        QBPushNotifications.createEvent(qbEvent).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {
                Log.d("notficationstatus", "send");
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("notficationstatus", "error");
            }
        });


    }

    private void videoCall() {
        if (!GeneralUtils.hasPermission(this, Manifest.permission.RECORD_AUDIO)) {
            cUtils.getInstance(ChatScreenActivity.this).ShowToast("Permission required");
            return;
        }
        startCall(true);
    }

    private void startCall(boolean isVideoCall) {
        if (qbChatDialog.getOccupants().size() > Consts.MAX_OPPONENTS_COUNT) {
            cUtils.getInstance(ChatScreenActivity.this).ShowToast(String.format(getString(com.quickblox.sample.groupchatwebrtc.R.string.error_max_opponents_count),
                    Consts.MAX_OPPONENTS_COUNT));
            return;
        }

        List<Integer> opponentsList = qbChatDialog.getOccupants();
        opponentsList.remove(PreferenceManager.getInstance(ChatScreenActivity.this).getQbUser().getId());

        if (opponentsList.size() == 0) {
            cUtils.getInstance(ChatScreenActivity.this).ShowToast("Not able to pick users.Please restart app");
            return;
        }

        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);
//        PushNotificationSender.sendPushMessage(opponentsList, currentUser.getFullName());

        SQLiteDatabase database = DatabaseHelper.getDatabaseInstanse(getApplicationContext()).getWritableDatabase();
        CallHistoryDAO dao = new CallHistoryDAO();
        dao.oppoName = qbChatDialog.getName();
        if (opponentsList.size() == 1) {
            dao.oppoImage = UserDetailsHasMap.getInstance().userImage.get(opponentsList.get(0)) + "";
        } else {
            dao.oppoImage = "";
        }
        dao.callTime = System.currentTimeMillis() + "";
        dao.callMode = Consts.ChatValues.CALL_DEAL;
        dao.oppoID = TextUtils.join(",", opponentsList);
        if (isVideoCall) {
            dao.callType = Consts.ChatValues.CALL_TYPE_VIDEO;
            sendPushMessage(Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_VIDEO, Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_VIDEO);
        } else {
            dao.callType = Consts.ChatValues.CALL_TYPE_AUDIO;
            sendPushMessage(Consts.NOTIFICATION_CODE.NOTFIFICATION_TYPE_AUDIO, Consts.NOTIFICATION_CODE.NOTFIFICATION_TYPE_AUDIO);
        }
        if (opponentsList.size() == 1) {
            CallHistoryDTO.getInstance().insertCallRecord(database, dao);
        }
        CallActivity.start(this, false);
    }

    private void sendPushMessage(String message, String isCalling) {


        // Send Push: create QuickBlox Push Notification Event
        QBEvent qbEvent = new QBEvent();
        qbEvent.setNotificationType(QBNotificationType.PUSH);
        qbEvent.setEnvironment(QBEnvironment.DEVELOPMENT);
        ChatModelActaul apn = new ChatModelActaul();
//        ChatModel chatModel = new ChatModel();
//        JSONObject json = new JSONObject();

        try {
            // standart parameters
            // read more about parameters formation http://quickblox.com/developers/Messages#Use_custom_parameters

//            ChatModel.Apn apn = new ChatModel.Apn();
//            apn.setAlert(message);
//            apn.setBadge("");
//            apn.setSound(CONSTANTS.NOTIFICATION_CODE.APN_SOUND_BLANK);
//            chatModel.setAps(apn);
//            ChatModel.Gcm gcm = new ChatModel.Gcm();
//            gcm.setNOTI_TITLE(SharedPrefsHelper.getInstance().getQbUser().getFullName());
//            gcm.setNOTI_ID(SharedPrefsHelper.getInstance().getQbUser().getId() + "");
//            gcm.setNOTI_MESSAGE(message);
//            chatModel.setGcm(gcm);
//            if (TextUtils.isEmpty(isCalling)) {
//                chatModel.setType(CONSTANTS.NOTIFICATION_CODE.NOTIFICATION_TYPE_MESSAGE);
//            } else if (isCalling.equalsIgnoreCase(CONSTANTS.NOTIFICATION_CODE.NOTIFICATION_TYPE_VIDEO)) {
//                chatModel.setType(CONSTANTS.NOTIFICATION_CODE.NOTIFICATION_TYPE_VIDEO);
//            } else if (isCalling.equalsIgnoreCase(CONSTANTS.NOTIFICATION_CODE.NOTFIFICATION_TYPE_AUDIO)) {
//                chatModel.setType(CONSTANTS.NOTIFICATION_CODE.NOTFIFICATION_TYPE_AUDIO);
//            }


            apn.setMessage(PreferenceManager.getInstance(ChatScreenActivity.this).getQbUser().getFullName() + " : " + message);
            apn.setIos_badge("");
            apn.setIos_sound(Consts.NOTIFICATION_CODE.APN_SOUND_BLANK);
            ChatModel.Gcm gcm = new ChatModel.Gcm();
            gcm.setNOTI_TITLE(PreferenceManager.getInstance(ChatScreenActivity.this).getQbUser().getFullName());
            gcm.setNOTI_ID(PreferenceManager.getInstance(ChatScreenActivity.this).getQbUser().getId() + "");
            gcm.setNOTI_MESSAGE(message);
//            gcm.setPropertyBody(new Gson().toJson(propertDetail));
            apn.setGcm(gcm);
            if (TextUtils.isEmpty(isCalling)) {
                apn.setType(Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_MESSAGE);
            } else if (isCalling.equalsIgnoreCase(Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_VIDEO)) {
                apn.setType(Consts.NOTIFICATION_CODE.NOTIFICATION_TYPE_VIDEO);
            } else if (isCalling.equalsIgnoreCase(Consts.NOTIFICATION_CODE.NOTFIFICATION_TYPE_AUDIO)) {
                apn.setType(Consts.NOTIFICATION_CODE.NOTFIFICATION_TYPE_AUDIO);
            }

//            json.put(CONSTANTS.NOTIFICATION_CODE.CUSTOM_MESSAGE, new Gson().toJson(chatModel).toString());
          /*  json.put(CONSTANTS.NOTIFICATION_CODE.NOTIFICATION_TYPE.TYPE, CONSTANTS.NOTIFICATION_CODE.NOTIFICATION_TYPE.CHAT);
            json.put(CONSTANTS.NOTIFICATION_CODE.SENDER_NAME, *//*SharedPrefsHelper.getInstance().getQbUser().getFullName()*//*"sddfd");
            json.put(CONSTANTS.NOTIFICATION_CODE.SENDER_ID, SharedPrefsHelper.getInstance().getQbUser().getNOTI_ID());
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        qbEvent.setMessage(new Gson().toJson(apn).toString());

        List<Integer> opponentsList = qbChatDialog.getOccupants();
        opponentsList.remove(PreferenceManager.getInstance(ChatScreenActivity.this).getQbUser().getId());
        StringifyArrayList<Integer> userIds = new StringifyArrayList<>();
        userIds.addAll(opponentsList);
        qbEvent.setUserIds(userIds);

        QBPushNotifications.createEvent(qbEvent).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {
                Log.d("notficationstatus", "send");
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("notficationstatus", "error");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatHelper.getInstance().addConnectionListener(chatConnectionListener);
        cUtils.getInstance(this).setIsChatActive(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChatHelper.getInstance().removeConnectionListener(chatConnectionListener);
        cUtils.getInstance(this).setIsChatActive(false);
    }

    private void sendChatSeenMessage() {

        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(Consts.ChatValues.MESSAGE_SEEN);
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "0");
        chatMessage.setDateSent(System.currentTimeMillis() / 1000);
        chatMessage.setMarkable(false);
        try {
            qbChatDialog.sendMessage(chatMessage);
        } catch (Exception e) {

        }
    }

    /**
     * helpfull for seen unseen messages
     *
     * @param messages
     */
    private void setDataInMap(List<CustomQBChatMessage> messages) {
        for (int i = 0; i < messages.size(); i++) {
            mapMessagePosition.put(messages.get(i).getId(), i + "");
        }
    }

    public void markedmessageasRead() {

        QBRestChatService.markMessagesAsRead(qbChatDialog.getDialogId(), null).performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Log.i(TAG, "read OK");
            }

            @Override
            public void onError(QBResponseException errors) {
                Log.i(TAG, "read error");
            }
        });

    }

    public void setAllMessageSeen() {
        if (chatAdapter == null) {
            return;
        }

        try {
            for (int i = 0; i < chatAdapter.getList().size(); i++) {
                Collection<Integer> ids = chatAdapter.getList().get(i).getReadIds();
                ids.remove(GeneralUtils.getUserId(qbChatDialog));
                ids.add(Integer.parseInt(GeneralUtils.getUserId(qbChatDialog)));
                chatAdapter.getList().get(i).setReadIds(ids);
            }
            chatAdapter.notifyDataSetChanged();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }


    }

    public void showMessage(CustomQBChatMessage message) {
        if (chatAdapter != null) {
            chatAdapter.add(message);
            scrollMessageListDown();
        } else {
            if (unShownMessages == null) {
                unShownMessages = new ArrayList<>();
            }
            unShownMessages.add(message);
        }
    }

    private void scrollMessageListDown() {
        recyclarViewLst.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    protected void onDestroy() {

        if (chatAdapter != null) {
            List<CustomQBChatMessage> messages = chatAdapter.getList();
            if (messages != null && messages.size() > 0) {
                new InsertMessageIdsInDbAsyc(messages).execute();
            }
        }
        mapMessagePosition = null;
        initChatConnectionListener();
        releaseChat();
        if (messageStatusesManager != null) {
            messageStatusesManager.removeMessageStatusListener(messageStatusListener);
        }
        chatAdapter = null;
//        messageStatusesManager.addMessageStatusListener(null);
        super.onDestroy();
    }

    private void releaseChat() {
        try {
            if (qbChatDialog == null) {
                return;
            }
            qbChatDialog.removeMessageListrener(chatMessageListener);
            if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                leaveGroupDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void leaveGroupDialog() {
        try {
            ChatHelper.getInstance().leaveChatDialog(qbChatDialog);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

    }

    public class InsertMessageIdsInDbAsyc extends AsyncTask<Void, Void, Void> {

        List<CustomQBChatMessage> messages;

        public InsertMessageIdsInDbAsyc(List<CustomQBChatMessage> messages) {
            this.messages = messages;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < messages.size(); i++) {

                SQLiteDatabase database = DatabaseHelper.getDatabaseInstanse(getApplicationContext()).getWritableDatabase();
                DeleteChatDAO deleteChatDAO = new DeleteChatDAO();
                deleteChatDAO.message_id = messages.get(i).getId();
                DeleteChatDTO.getInstance().insertChatID(database, deleteChatDAO);
                deleteChatDAO = null;
            }
            return null;
        }
    }

}
