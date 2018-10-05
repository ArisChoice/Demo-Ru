package com.app.rum_a.ui.postauth.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.rum_a.R;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.ui.postauth.activity.PropertyDetailActivity;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.appinterface.OnItemClickListener;

import java.util.List;


public class ImagesPagerAdapter extends PagerAdapter {
    RumApplication app;
    LayoutInflater mLayoutInflater;
    List<PropertyListResponseModel.ResultBean.PropertyImageListBean> propertyImageList;
    Activity activity;
    //    List<String> list;
//    Util util;
    int type;
    OnItemClickListener<PropertyListResponseModel.ResultBean.PropertyImageListBean> onItemClickListener;
    /*public ImagesPagerAdapter(Context context, List<String> list, Util util, OnItemClickListener listener, int type) {
        this.mContext = context;
        this.list = list;
        this.util = util;
        this.listener = listener;
        this.type = type;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/


    public ImagesPagerAdapter(RumApplication app, Activity activity, List<PropertyListResponseModel.ResultBean.PropertyImageListBean> propertyImageList,
                              OnItemClickListener<PropertyListResponseModel.ResultBean.PropertyImageListBean> onItemClickListener) {
        this.app = app;
        this.onItemClickListener = onItemClickListener;
        this.propertyImageList = propertyImageList;
        this.activity = activity;
        mLayoutInflater = (LayoutInflater) app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (propertyImageList != null && propertyImageList.size() > 0)
            return propertyImageList.size();
        else return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View itemView = mLayoutInflater.inflate(R.layout.view_pager_item, container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//        final RelativeLayout btnDelete = (RelativeLayout) itemView.findViewById(R.id.btnDelete);
        /*if (type == 0) {
            btnDelete.setVisibility(View.VISIBLE);
            Bitmap bitmap = util.getBitmap(list.get(position));
            imageView.setImageBitmap(bitmap);
        } else {*/
//        btnDelete.setVisibility(View.GONE);
        try {
            new ImageUtility(app).LoadImage(CommonUtils.getValidUrl(propertyImageList.get(position).getImageURL()), imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CommonUtils().FullImageScreem(activity, CommonUtils.getValidUrl(propertyImageList.get(position).getImageURL()));
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
        }
        container.addView(itemView);
       /* imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                listener.onItemClick(view, position, GlobalsVariables.TYPE.APAPTER_PAGER_CLICK, null);
                listener.onItemClick(imageView, position, APAPTER_SELECTED_IMAGES, postImage);
            }
        });
*/
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public void update(String path) {
        notifyDataSetChanged();
    }


    public List<PropertyListResponseModel.ResultBean.PropertyImageListBean> getImageList() {
        return propertyImageList;
    }

}