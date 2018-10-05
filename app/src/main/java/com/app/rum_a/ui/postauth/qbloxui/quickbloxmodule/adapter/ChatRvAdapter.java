package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.rum_a.R;
import com.app.rum_a.model.modelutils.TempUserModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.ui.postauth.qbloxui.activities.ChatScreenActivity;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatHelper;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.CustomQBChatMessage;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.GeneralUtils;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.TimeUtils;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.UserDetailsHasMap;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Sunny on 3/4/2017.
 */

public class ChatRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> /*implements
        StickyHeaderAdapter<ChatRvAdapter.HeaderViewHolder> */ {

    private static final String TAG = ChatRvAdapter.class.getSimpleName();
    final int VIEW_TYPE_MY = 0;
    final int VIEW_TYPE_FRIEND = 1;
    private final QBChatDialog chatDialog;
    ArrayList<String> deletedList = new ArrayList<String>();
    Activity context;
    List<CustomQBChatMessage> chatMessages;
    Activity chatWindowsActivity;
    private LayoutInflater mInflater;
    private boolean isDeleteSelected;
    UserResponseModel.ResultBean.UserBean logedUserData;
    TempUserModel otherUser;

    public ChatRvAdapter(Activity chatWindowsActivity, ChatScreenActivity context, QBChatDialog chatDialog, List<CustomQBChatMessage> chatMessages,
                         UserResponseModel.ResultBean.UserBean logedUserData, TempUserModel otherUser) {
        this.chatDialog = chatDialog;
        this.chatWindowsActivity = chatWindowsActivity;
        this.chatMessages = chatMessages;
        this.context = context;
        this.logedUserData = logedUserData;
        this.otherUser = otherUser;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemViewType(int position) {
        if (isIncoming(chatMessages.get(position))) {
            return VIEW_TYPE_FRIEND;
        } else {
            return VIEW_TYPE_MY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FRIEND) {
            View view = mInflater.inflate(R.layout.chatting_item_layout_recivier, parent, false);
            return new FriendViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.chatting_item_layout_sender, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int post) {
        int pos = holder.getAdapterPosition();
        CustomQBChatMessage message = chatMessages.get(pos);
        Log.e(" onBindViewHolder ", "---------------------------onBindViewHolder onBindViewHolder ");
        /*if (message.getDateSent() > Long.parseLong(SharedPrefsHelper.getInstance().get(SharedPrefsHelper.DELETION_TIME_ACTIVATE, ""))) {

        }*/

        if (isIncoming(chatMessages.get(pos))) {
            ChatRvAdapter.FriendViewHolder friendViewHolder = (ChatRvAdapter.FriendViewHolder) holder;
            friendViewHolder.txtMessage.setVisibility(View.GONE);
//            friendViewHolder.mSentIv.setVisibility(View.GONE);
//            friendViewHolder.addContact.setVisibility(View.GONE);
//            friendViewHolder.mDocumentLL.setVisibility(View.GONE);
//            friendViewHolder.mAudioLayout.setVisibility(View.GONE);
//            friendViewHolder.videoFL.setVisibility(View.GONE);
//            friendViewHolder.allItemRL.setBackgroundColor(context.getResources().getColor(R.color.white));
            setMessageBodyToFriend(friendViewHolder, message);
            friendViewHolder.imgUserImage.setVisibility(View.VISIBLE);
            String friendId = GeneralUtils.getUserId(chatDialog);
            new ImageUtility(context).LoadImage(CommonUtils.getValidUrl(otherUser.getUserImage()),
                    friendViewHolder.imgUserImage);

            setclickListenerToFriends(friendViewHolder, message);
        } else {
            ChatRvAdapter.MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.txtMessage.setVisibility(View.GONE);
//            myViewHolder.mSentIv.setVisibility(View.GONE);
//            myViewHolder.mDocumentLL.setVisibility(View.GONE);
//            myViewHolder.addContact.setVisibility(View.GONE);
//            myViewHolder.mAudioLayout.setVisibility(View.GONE);
//            myViewHolder.videoFL.setVisibility(View.GONE);
//            myViewHolder.allItemRL.setBackgroundColor(context.getResources().getColor(R.color.white));
            if (logedUserData.getProfilePics() != null && !logedUserData.getProfilePics().get(0).getImageURL().equals(""))
                new ImageUtility(chatWindowsActivity).LoadImage(CommonUtils.getValidUrl(logedUserData.getProfilePics().get(0).getImageURL().toString()),
                        myViewHolder.imgUserImage);
            setMessageBodyToMe(myViewHolder, message);
//            setMessageDeliveryStatus(message, myViewHolder.imageMessageDeliveryStatus);

            //    myViewHolder.mUserImage.setVisibility(View.VISIBLE);
            setclickListenerToMy(myViewHolder, message);
        }

    }

    private void openVideoPlayer(String fileurl) {
      /*  Intent intent = new Intent(context, NativeVideoPlayerActivity.class);
        intent.putExtra(CONSTANTS.REQUEST_CODE.VIDEO_URL, fileurl);
        context.startActivity(intent);*/
    }

    public boolean checkSelectedExist() {
        for (int i = 0; i < chatMessages.size(); i++) {
            if (chatMessages.get(i).isDeletionActivate()) {
                isDeleteSelected = true;
                return true;
            } else {
                isDeleteSelected = false;
            }

        }
        return false;
    }

    private HashSet<String> getSelectedList() {
        deletedList.clear();
        HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < chatMessages.size(); i++) {
            if (chatMessages.get(i).isDeletionActivate()) {
                set.add(chatMessages.get(i).getId().trim());
                Log.e("Matched", "Matched now----------------------------------->");
                deletedList.add(chatMessages.get(i).getId().trim());
            }
        }
        return set;
    }

    public void unSelectDelete() {
//        chatWindowsActivity.deleteActionManage(false);
//        for (int p = 0; p < chatMessages.size(); p++) {
//            chatMessages.get(p).setDeletionActivate(false);
//        }
//        notifyDataSetChanged();
    }

    //profile+image
    public void deleteNow() {
        final HashSet<String> messageId = getSelectedList();

        if (messageId == null || messageId.size() == 0) {
            CommonUtils.getInstance(chatWindowsActivity).ShowToast("Select Messages to delete");
            return;
        }

        if (messageId != null && messageId.size() >= 0) {
//            this.chatWindowsActivity.showProgressDialog("Deleting..");
            QBRestChatService.deleteMessages(messageId, true).performAsync(new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    try {
                        Log.e("messge", "deleted");
//                        chatWindowsActivity.dissmissProgress();
                        for (int i = 0; i < deletedList.size(); i++) {
                            for (int j = 0; j < chatMessages.size(); j++) {
                                if (deletedList.get(i).toString().trim().equalsIgnoreCase(chatMessages.get(j).getId().toString().trim())) {
                                    chatMessages.remove(j);
                                    j--;
                                }
                            }
                        }

//                        chatWindowsActivity.deleteActionManage(false);
                        for (int p = 0; p < chatMessages.size(); p++) {
                            chatMessages.get(p).setDeletionActivate(false);
                        }

                        notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e("message", "unabletodlete");
                }
            });
        }
    }


    private void setclickListenerToFriends(final FriendViewHolder holder, final CustomQBChatMessage chatMessage) {

    }


    private void setclickListenerToMy(final MyViewHolder holder, final CustomQBChatMessage chatMessage) {

    }


    private void setMessageBodyToFriend(final FriendViewHolder holder, CustomQBChatMessage chatMessage) {
        holder.mReceiverLayout.setVisibility(View.VISIBLE);
//        holder.imageSticker.setVisibility(View.GONE);
        if (hasAttachments(chatMessage)) {
            Collection<QBAttachment> attachments = chatMessage.getAttachments();
            QBAttachment attachment = attachments.iterator().next();
            holder.txtMessage.setVisibility(View.GONE);

        } else {

//            holder.mSentIv.setVisibility(View.GONE);
            holder.txtMessage.setVisibility(View.VISIBLE);
            holder.txtMessage.setText(chatMessage.getBody());
            try {
                Map<String, String> hashMap = chatMessages.get(holder.getAdapterPosition()).getProperties();
                if (hashMap.containsKey("phones")) {
                    holder.txtMessage.setText(hashMap.get("firstName"));
//                    holder.addContact.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

//        if (!chatMessage.isDeletionActivate()) {
//            holder.allItemRL.setBackgroundColor(context.getResources().getColor(R.color.white));
//        } else {
//            holder.allItemRL.setBackgroundColor(context.getResources().getColor(R.color.selected_grey));
//        }
        holder.txtMessageDate.setText(TimeUtils.getTime(chatMessage.getDateSent() * 1000));
        Log.e("MessageToFriend", chatMessage.getDateSent() + " ");
    }

    private void setMessageBodyToMe(final MyViewHolder holder, CustomQBChatMessage chatMessage) {
        holder.mSenderLayout.setVisibility(View.VISIBLE);

//        holder.imageSticker.setVisibility(View.GONE);
        if (hasAttachments(chatMessage)) {

        } else {

//            holder.mSentIv.setVisibility(View.GONE);
            holder.txtMessage.setVisibility(View.VISIBLE);
            holder.txtMessage.setText(chatMessage.getBody());
            try {
                Map<String, String> hashMap = chatMessages.get(holder.getAdapterPosition()).getProperties();
                if (hashMap.containsKey("phones")) {
                    holder.txtMessage.setText(hashMap.get("firstName"));
                    //  holder.addContact.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        if (!chatMessage.isDeletionActivate()) {
//            holder.allItemRL.setBackgroundColor(context.getResources().getColor(R.color.white));
//        } else {
//            holder.allItemRL.setBackgroundColor(context.getResources().getColor(R.color.selected_grey));
//        }
        holder.txtMessageDate.setText(TimeUtils.getTime(chatMessage.getDateSent() * 1000));
        Log.e("MessageToMe", chatMessage.getDateSent() + " ");
    }


    private boolean hasAttachments(CustomQBChatMessage chatMessage) {
        Collection<QBAttachment> attachments = chatMessage.getAttachments();
        return attachments != null && !attachments.isEmpty();
    }


    @Override
    public int getItemCount() {
        return chatMessages == null || chatMessages.size() == 0 ? 0 : chatMessages.size();
    }


    private boolean isIncoming(CustomQBChatMessage chatMessage) {
        QBUser currentUser = ChatHelper.getCurrentUser();
        return chatMessage.getSenderId() != null && !chatMessage.getSenderId().equals(currentUser.getId());
    }

    public void updateList(List<CustomQBChatMessage> newData) {
        chatMessages = newData;
        notifyDataSetChanged();
    }

    public void add(CustomQBChatMessage item) {
        chatMessages.add(item);
        notifyDataSetChanged();
    }

    public void addList(List<CustomQBChatMessage> items) {
        chatMessages = items;
        notifyDataSetChanged();
    }

    public List<CustomQBChatMessage> getList() {
        return chatMessages;
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.imgUserImage)
        CircleImageView imgUserImage;
        @BindView(R.id.txtMessage)
        TextView txtMessage;
        @BindView(R.id.txtMessageDate)
        TextView txtMessageDate;
        @BindView(R.id.linearChat)
        LinearLayout linearChat;
        @Nullable
        @BindView(R.id.senderLayout)
        RelativeLayout mSenderLayout;
        @BindView(R.id.receiverLayout)
        @Nullable
        RelativeLayout mReceiverLayout;
        @Nullable
        @BindView(R.id.imageView_msgReadStatus)
        ImageView readStatus;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.imgUserImage)
        CircleImageView imgUserImage;
        @BindView(R.id.txtMessage)
        TextView txtMessage;
        @BindView(R.id.txtMessageDate)
        TextView txtMessageDate;
        @BindView(R.id.linearChat)
        LinearLayout linearChat;
        @Nullable
        @BindView(R.id.senderLayout)
        RelativeLayout mSenderLayout;
        @Nullable
        @BindView(R.id.receiverLayout)
        RelativeLayout mReceiverLayout;
        @Nullable
        @BindView(R.id.imageView_msgReadStatus)
        ImageView readStatus;

        public FriendViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

}
