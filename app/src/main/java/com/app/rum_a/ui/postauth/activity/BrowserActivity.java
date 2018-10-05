package com.app.rum_a.ui.postauth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.views.RumTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by harish on 29/8/18.
 */

public class BrowserActivity extends BaseActivity {
    @BindView(R.id.back_toolbar)
    ImageView backToolbar;
    @BindView(R.id.txt_title_toolbar)
    RumTextView txtTitleToolbar;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.browser_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData(getIntent());
        txtTitleToolbar.setText(R.string.app_name);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                pbLoading.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                pbLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getIntentData(Intent intent) {
        try {
            url = intent.getStringExtra(AppConstants.ParmsType.WEB_URL);
        } catch (Exception e) {
            url = NetworkConstatnts.API.ABOUT_US;
        }
//        Log.e(" getIntentData ", " " + intent.getStringExtra(AppConstants.ParmsType.WEB_URL));
    }

    @OnClick({R.id.back_toolbar, R.id.web_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.web_view:
                break;
        }
    }

}
