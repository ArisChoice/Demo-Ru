package com.app.rum_a.utils.swipeutils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rum_a.DashActivity;
import com.app.rum_a.R;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.appinterface.OnSwipePerform;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.Utils;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;


/**
 * Created by Harish on 19/08/18.
 */
@Layout(R.layout.swipe_item_layout)
public class SwipeTCard {

    @View(R.id.imgpropertyImage)
    private ImageView imgProperty;

    @View(R.id.propertyTypeText)
    private TextView propertyTypeText;

    @View(R.id.txtPropertyName)
    private TextView txtPropertyName;

    @View(R.id.txtDistance)
    private TextView txtDistance;

    @SwipeView
    private android.view.View cardView;

    private PropertyListResponseModel.ResultBean mData;
    private DashActivity mContext;
    private SwipePlaceHolderView mSwipeView;
    OnSwipePerform onSwipePerform;

    public SwipeTCard(DashActivity context, PropertyListResponseModel.ResultBean mData, SwipePlaceHolderView swipeView, OnSwipePerform onSwipePerform) {
        mContext = context;
        this.mData = mData;
        mSwipeView = swipeView;
        this.onSwipePerform = onSwipePerform;
    }

    @Resolve
    private void onResolved() {
//        MultiTransformation multi = new MultiTransformation(
//                new BlurTransformation(mContext, 30),
//                new RoundedCornersTransformation(
//                        mContext, Utils.dpToPx(7), 0,
//                        RoundedCornersTransformation.CornerType.TOP));

     /*   Glide.with(mContext).load(mProfile.getImageUrl())
                .bitmapTransform(multi)
                .into(profileImageView);*/
       /* nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getLocation());*/

        String currencySymbol = CommonUtils.getCurrencySymbol(mData.getCurrency());
        txtPropertyName.setText(mData.getName() + " " + currencySymbol + " " + mData.getPrice());
        if (mData.getDistanceType().equals(mContext.getString(R.string.str_miles)))
            txtDistance.setText(String.format("%.2f", mData.getDistance()) + " " + mContext.getString(R.string.str_miles) + " away");
        else
            txtDistance.setText(String.format("%.2f", mData.getDistance()) + " " + mContext.getString(R.string.str_km) + " away");
        try {
            new ImageUtility(mContext).LoadImage(CommonUtils.getValidUrl(mData.getPropertyImageList().get(0).getImageURL()), imgProperty);
        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(" onSwipeHeadCard ", " " + mData.getPropertyImageList().get(0).getImageURL());
        }
        if (mData.getOwnerDetails().getOwnerId() != mContext.getUserDeatil().getUserId()) {
            propertyTypeText.setVisibility(android.view.View.VISIBLE);
            if (mData.getForRentOrBuy() + "" != null) {
                if (mData.getForRentOrBuy() == AppConstants.ForRentOrBuy.Rent)
                    propertyTypeText.setText(R.string.str_rent);
                else propertyTypeText.setText(R.string.str_buy);
            } else propertyTypeText.setText(R.string.str_rent);
        } else propertyTypeText.setVisibility(android.view.View.GONE);
    }

    @SwipeHead
    private void onSwipeHeadCard() {
//        try {
//            new ImageUtility(mContext).LoadImage(CommonUtils.getValidUrl(mData.getPropertyImageList().get(0).getImageURL()), imgProperty);
//            Log.e(" onResolved ", " " + mData.getPropertyImageList().get(0).getImageURL());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        cardView.invalidate();
    }

    @Click(R.id.imgpropertyImage)
    private void onClick() {
        Log.d("EVENT", "profileImageView click");
//        mSwipeView.addView(this);
        onSwipePerform.onSWipeData(mData, AppConstants.SwipeType.CLICK);
    }

    @SwipeOut
    private void onSwipedOut() {
        Log.d("EVENT", "onSwipedOut");
//        mSwipeView.addView(this);
        onSwipePerform.onSWipeData(mData, AppConstants.SwipeType.CROSS);
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
//        onSwipePerform.onSWipeData(mData);
    }

    @SwipeIn
    private void onSwipeIn() {
        Log.d("EVENT", "onSwipedIn");
        onSwipePerform.onSWipeData(mData, AppConstants.SwipeType.LIKE);
    }

    @SwipeInState
    private void onSwipeInState() {
        Log.d("EVENT", "onSwipeInState");
//        onSwipePerform.onSWipeData(mData);
    }

    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");
//        onSwipePerform.onSWipeData(mData);
    }
}
