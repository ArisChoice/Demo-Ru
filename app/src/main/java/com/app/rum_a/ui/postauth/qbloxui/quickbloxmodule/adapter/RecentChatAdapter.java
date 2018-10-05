package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.rum_a.R;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.GeneralUtils;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.QbDialogUtils;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.TimeUtils;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.UserDetailsHasMap;
import com.app.rum_a.utils.ImageUtility;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vivek on 20-02-2017.
 */

public class RecentChatAdapter extends BaseAdatperRecycler<QBChatDialog, RecentChatAdapter.UserListingViewHolder>
        implements Filterable {

    List<QBChatDialog> locList = new ArrayList<>();
    List<QBChatDialog> templist = new ArrayList<>();
    private Context c;


    public RecentChatAdapter(Context context, List<QBChatDialog> dialogs) {
        super(context, dialogs);

        locList = dialogs;
        Log.e("Size XXX", locList.size() + "-----------------" + dialogs.size());
        templist = dialogs;
        Log.e("Size ZZZZ", templist.size() + "?????????????");


    }

    @Override
    public UserListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_chat, parent, false);
        return new UserListingViewHolder(v);
    }


    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserListingViewHolder vholder = (UserListingViewHolder) holder;
        QBChatDialog dialog = getItem(position);
        String friendId = GeneralUtils.getUserId(dialog);
        if (dialog.getType().equals(QBDialogType.PRIVATE) && !TextUtils.isEmpty(friendId) && UserDetailsHasMap.getInstance().userImage.get(Integer.parseInt(friendId)) != null) {
            //   vholder.mProgressBar.setVisibility(View.VISIBLE);
//            Glide.with(context).load(GeneralUtils.generateProfileImagelink
//                    (UserDetailsHasMap.getInstance().userImage.get(Integer.parseInt(friendId)) + "")).into(vholder.mUserImage);
            new ImageUtility(context).LoadImage(GeneralUtils.generateProfileImagelink
                    (UserDetailsHasMap.getInstance().userImage.get(Integer.parseInt(friendId)) + ""), vholder.mUserImage);
        } else if (dialog.getType().equals(QBDialogType.GROUP) /*&& !TextUtils.isEmpty(dialog.getPhoto()) && !dialog.getPhoto().equalsIgnoreCase("null")*/) {
            //   vholder.mProgressBar.setVisibility(View.VISIBLE);
//            Glide.with(context).load(R.drawable.user_group).into(vholder.mUserImage);
            new ImageUtility(context).LoadImage("", vholder.mUserImage);
        } else {
//            Glide.with(context).load(R.drawable.ic_circle_user).into(vholder.mUserImage);
            new ImageUtility(context).LoadImage("", vholder.mUserImage);
            vholder.mUserImage.setImageResource(R.drawable.avtar);
            vholder.mProgressBar.setVisibility(View.GONE);
        }


        vholder.mOnlineImage.setVisibility(View.GONE);
        vholder.mUnreadMessages.setVisibility(View.GONE);

        vholder.mFriendNametxt.setText(dialog.getName());
        vholder.mFriendLastMsgtxt.setText(prepareTextLastMessage(dialog));
        vholder.mDateTxt.setText(TimeUtils.getLastMessageDate(dialog.getLastMessageDateSent() * 1000));

        int ureadmessage = getUnreadMsgCount(dialog);
        if (ureadmessage != 0) {
            vholder.mUnreadMessages.setVisibility(View.VISIBLE);
            vholder.mUnreadMessages.setText(ureadmessage + "");
        }

    }

    @Override
    public void upDateList(List<QBChatDialog> updatedList) {
        locList = getList();
        templist = getList();
    }


    private int getUnreadMsgCount(QBChatDialog chatDialog) {
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        if (unreadMessageCount == null) {
            return 0;
        } else {
            return unreadMessageCount;
        }
    }

    private boolean isLastMessageAttachment(QBChatDialog dialog) {
        String lastMessage = dialog.getLastMessage();
        Integer lastMessageSenderId = dialog.getLastMessageUserId();
        return TextUtils.isEmpty(lastMessage) && lastMessageSenderId != null;
    }

    private String prepareTextLastMessage(QBChatDialog chatDialog) {
        if (isLastMessageAttachment(chatDialog)) {
            return context.getString(R.string.chat_attachment);
        } else {
            return chatDialog.getLastMessage();
        }
    }

    //ek min
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<QBChatDialog> results = new ArrayList<>();

                List<QBChatDialog> list = new ArrayList<>();

                locList = templist;

                list = locList;

                Log.e("list size", list.size() + "//");
                if (charSequence != null) {

                    Log.e("In", "if");
                    if (list != null && list.size() > 0) {
                        for (final QBChatDialog g1 : list) {
                            Log.e("In", "for");

                            boolean itemContain = GeneralUtils.containsIgnoreCase(QbDialogUtils.getDialogName(g1),
                                    charSequence.toString().toLowerCase());

                            if (itemContain) {
                                Log.e("In", "matched");
                                results.add(g1);
                            }
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }

                return oReturn;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {

                locList = (List<QBChatDialog>) results.values;
                updateList(locList, false);
            }
        };

    }

    public class UserListingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameTV)
        TextView mFriendNametxt;

        @BindView(R.id.lastMsgTV)
        TextView mFriendLastMsgtxt;

        @BindView(R.id.dateTV)
        TextView mDateTxt;

        @BindView(R.id.onlineImgae)
        ImageView mOnlineImage;

        @BindView(R.id.unreadmessagecount)
        TextView mUnreadMessages;


        @BindView(R.id.userimage)
        CircleImageView mUserImage;

        @BindView(R.id.progressbar)
        ProgressBar mProgressBar;

        public UserListingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }


}
