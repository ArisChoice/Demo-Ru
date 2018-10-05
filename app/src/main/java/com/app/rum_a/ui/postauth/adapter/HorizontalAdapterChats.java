package com.app.rum_a.ui.postauth.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rum_a.R;
import com.app.rum_a.model.resp.ChatUsersModelResponse;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.ui.postauth.activity.PropertyDetailActivity;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.appinterface.OnItemClickListener;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalAdapterChats extends RecyclerView.Adapter<HorizontalAdapterChats.HorizontalViewHolder> {

    PropertyDetailActivity addPropertyActivity;
    ArrayList<ChatUsersModelResponse.ResultBean> userList;
    OnItemClickListener onItemClickListener;

    public HorizontalAdapterChats(PropertyDetailActivity addPropertyActivity, ArrayList<ChatUsersModelResponse.ResultBean> imageList,
                                  OnItemClickListener onItemClickListener) {
        this.addPropertyActivity = addPropertyActivity;
        this.userList = imageList;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_chat_user_view, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder holder, int position) {
        ChatUsersModelResponse.ResultBean positionData = userList.get(position);
        holder.userName.setText(positionData.getSenderName());
        setNotificationStatus(holder, positionData);
        try {
            List<ChatUsersModelResponse.ResultBean.SenderImageBean> userImages = positionData.getSenderImage();
            new ImageUtility(addPropertyActivity).LoadImage(CommonUtils.getValidUrl(userImages.get(0).getImageURL()), holder.userImage);
        } catch (Exception e) {

        }
    }

    private void setNotificationStatus(final HorizontalViewHolder holder, final ChatUsersModelResponse.ResultBean positionData) {
        Set<String> dialogsIds = new HashSet<String>() {{
            add(positionData.getChatDialog());
        }};
        QBRestChatService.getTotalUnreadMessagesCount(dialogsIds, null).performAsync(new QBEntityCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer, Bundle bundle) {
                Log.e("TAG", "total unread messages:  " + integer);
                Log.e("TAG", "dialog Unread1:  " + bundle.getInt(positionData.getChatDialog()));
                if (bundle.getInt(positionData.getChatDialog()) > 0) {
                    holder.chatNotificatin.setVisibility(View.VISIBLE);
                } else holder.chatNotificatin.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void update(List<ChatUsersModelResponse.ResultBean> item) {
        userList.clear();
        userList.addAll(item);
        notifyDataSetChanged();
    }

    public ArrayList<ChatUsersModelResponse.ResultBean> getImageList() {
        return userList;
    }

    public void removeItems() {
        userList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        userList.remove(position);
        notifyDataSetChanged();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chat_user_image)
        CircleImageView userImage;
        @BindView(R.id.notification_status_img)
        ImageView chatNotificatin;
        @BindView(R.id.chat_user_name)
        TextView userName;

        public HorizontalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, getAdapterPosition(), AppConstants.Clickerations.IMAGE_VIEW_CLICK, userList.get(getAdapterPosition()));

                }
            });

        }
    }
}