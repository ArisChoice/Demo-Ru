package com.app.rum_a.ui.postauth.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rum_a.R;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.Log4Android;
import com.app.rum_a.utils.appinterface.SwipeOperation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by tarun on 15/6/15.
 */
public class SwipeViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    //    private ArrayList<UserData> users;
    private ImageUtility imageUtility;
    SwipeOperation swipeOperation;
    Resources res;
    ArrayList<PropertyListResponseModel.ResultBean> propertyLst;
//    int imageLst[] = {R.drawable.sample_property, R.drawable.sample_property2, R.drawable.sample_property3,
//            R.drawable.sample_property, R.drawable.sample_property2, R.drawable.sample_property_4, R.drawable.sample_property3};//For tem use

    public SwipeViewAdapter(Context context, ArrayList<PropertyListResponseModel.ResultBean> propertyLst, SwipeOperation swipeOperation) {
        this.context = context;
//        this.users = users;
        inflater = LayoutInflater.from(context);
        imageUtility = new ImageUtility(context);
        res = context.getResources();
        this.swipeOperation = swipeOperation;
        this.propertyLst = propertyLst;
//        Log.e("setAdapter,", users.size() + "");
//        Log4Android.e(this, "setAdapter" + users.size() + "");
    }

    public void updateAdapter(ArrayList<PropertyListResponseModel.ResultBean> propertyList) {
        propertyList.addAll(propertyList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return propertyLst.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        swipeOperation.performSwipe(position);
        Log.e("position", "position " + position);
        Log.e("view", "in");
        Log4Android.e(this, "getViewCalled");
        ViewHolder viewHolder;
        if (convertView == null) {
            Log.e("convert", "in");

            convertView = inflater.inflate(R.layout.swipe_item_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            Log.e("convert", "out");
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PropertyListResponseModel.ResultBean positionData = propertyLst.get(position);
//        viewHolder.imgPropertyImage.setImageResource(imageLst[position]);//For tem use demo list
        String currencySymbol = CommonUtils.getCurrencySymbol(positionData.getCurrency());
        viewHolder.txtPropertyName.setText(positionData.getName() + " " + currencySymbol + " " + positionData.getPrice());
        if (positionData.getDistanceType().equals(context.getString(R.string.str_miles)))
            viewHolder.txtDistance.setText(String.format("%.2f", positionData.getDistance()) + " " + context.getString(R.string.str_miles));
        else
            viewHolder.txtDistance.setText(String.format("%.2f", positionData.getDistance()) + " " + context.getString(R.string.str_km));
        try {
            new ImageUtility(context).LoadImage(CommonUtils.getValidUrl(positionData.getPropertyImageList().get(0).getImageURL()), viewHolder.imgPropertyImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'swipe_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @BindView(R.id.imgpropertyImage)
        ImageView imgPropertyImage;
        @BindView(R.id.txtPropertyName)
        TextView txtPropertyName;
        @BindView(R.id.txtDistance)
        TextView txtDistance;

        /*@BindView(R.id.txtNoOfPics)
        TextView txtNoOfPics;
*/
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
