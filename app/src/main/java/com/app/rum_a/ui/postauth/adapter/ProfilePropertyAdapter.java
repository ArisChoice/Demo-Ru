package com.app.rum_a.ui.postauth.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.rum_a.R;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.ui.postauth.activity.ProfileActivity;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.appinterface.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ProfilePropertyAdapter extends RecyclerView.Adapter<ProfilePropertyAdapter.ViewHolder> {

    private ArrayList<PropertyListResponseModel.ResultBean> listModels;
    private Context context;
    String TAG = "ServiceProviderListAdapter";
    boolean[] selectedService;
    OnItemClickListener onItemClickListener;

    public ProfilePropertyAdapter(Context context, ArrayList<PropertyListResponseModel.ResultBean> listModels, OnItemClickListener onItemClickListener) {
        this.listModels = listModels;
        this.context = context;
        this.listModels = listModels;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void updateAll(List<PropertyListResponseModel.ResultBean> resultLst) {
        listModels.clear();
        listModels.addAll(resultLst);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView service_list_name, servicePriceTxt;
        ImageView imageViewProperty, selectImg;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewProperty = (ImageView) itemView.findViewById(R.id.image_view_property);
            imageViewProperty.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_view_property:
                    onItemClickListener.onItemClick(v, getAdapterPosition(), AppConstants.Clickerations.IMAGE_VIEW_CLICK, listModels.get(getAdapterPosition()));
                    break;
            }

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PropertyListResponseModel.ResultBean positionData = listModels.get(position);
        try {
            new ImageUtility(context).LoadImage(CommonUtils.getValidUrl(positionData.getPropertyImageList().get(0).getImageURL()), holder.imageViewProperty);
        } catch (Exception e)
        {

        }
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }


}
