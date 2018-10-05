package com.app.rum_a.ui.postauth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.rum_a.R;
import com.app.rum_a.core.BaseFragment;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.ui.postauth.adapter.SwipeViewAdapter;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.SpringAnimation;
import com.app.rum_a.utils.appinterface.SwipeOperation;
import com.app.rum_a.utils.swipecardlib.SwipeCardView;
import com.app.rum_a.utils.views.RumTextView;
import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 28/8/18.
 */

public class HomeFragment extends BaseFragment implements SwipeCardView.OnCardFlingListener, RestCallback,
        SwipeCardView.OnItemClickListener, SwipeOperation {
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgPropertyImage)
    CircleImageView imgPropertyImage;
    @BindView(R.id.txtFindingNearBy)
    RumTextView txtFindingNearBy;
    @BindView(R.id.ripple_back)
    RippleBackground rippleBack;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;
    @BindView(R.id.swipeContainer)
    SwipeCardView swipeContainer;
    @BindView(R.id.imgCross)
    ImageView imgCross;
    @BindView(R.id.imgLike)
    ImageView imgLike;
    @BindView(R.id.linearBottom)
    LinearLayout linearBottom;
    @BindView(R.id.lay_sampl)
    LinearLayout laySampl;
    Unbinder unbinder;
    private SpringAnimation springAnimation;
    private SwipeViewAdapter adapter;

    public static HomeFragment newInstance(Context context) {
        HomeFragment f = new HomeFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, root);

//        unbinder = ButterKnife.bind(this, root);

//        swipeContainerSetup();
//        springAnim();
//        startSearching();
//        setAdapter();
        return root;
    }

    private void setAdapter() {

        Log.e("test", "1");
        try {
            swipeContainer.removeAllViewsInLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        adapter = new SwipeViewAdapter(getActivity(), new ArrayList<String>(), this);
//        swipeContainer.init(getActivity(), adapter);
//        adapter.updateAdapter();
    }

    private void startSearching() {
        if (adapter != null) {
            swipeContainer.removeAllViewsInLayout();
            //swipeContainer.init(this, adapter);
//            adapter.updateAdapter();
        } else {
//            users.clear();
            startRippleAnimation();
        }
    }

    private void startRippleAnimation() {
        rippleBack.startRippleAnimation();
        CommonUtils.showView(searchLayout);
        CommonUtils.showView(rippleBack);
        CommonUtils.hideView(linearBottom);
//        getUsers();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRippleAnimation();
                setAdapter();
            }
        }, 2000);

    }

    private void stopRippleAnimation() {
        CommonUtils.hideView(searchLayout);
        CommonUtils.hideView(rippleBack);
        CommonUtils.showView(linearBottom);
        rippleBack.stopRippleAnimation();
    }

    private void springAnim() {
        springAnimation = new SpringAnimation();
        springAnimation.setAnimation(imgPropertyImage);
    }

    private void swipeContainerSetup() {
        swipeContainer.setOnItemClickListener(this);
        swipeContainer.setFlingListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void performSwipe(int size) {

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

    @Override
    public void onItemClicked(int itemPosition, Object dataObject) {

    }

    @Override
    public void onCardExitLeft(Object dataObject) {

    }

    @Override
    public void onCardExitRight(Object dataObject) {

    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {

    }

    @Override
    public void onScroll(float scrollProgressPercent) {

    }

    @Override
    public void onCardExitTop(Object dataObject) {

    }

    @Override
    public void onCardExitBottom(Object dataObject) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @OnClick({R.id.imgPropertyImage, R.id.txtFindingNearBy, R.id.ripple_back, R.id.imgCross, R.id.imgLike})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgPropertyImage:
                break;
            case R.id.txtFindingNearBy:
                break;
            case R.id.ripple_back:
                break;
            case R.id.imgCross:
                break;
            case R.id.imgLike:
                break;
        }
    }
}
