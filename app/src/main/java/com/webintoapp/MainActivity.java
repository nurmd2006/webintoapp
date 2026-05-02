package com.webintoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private RelativeLayout mSplashScreen;
    private FrameLayout mCustomViewContainer;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AppConfig.SHOW_STATUS_BAR) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                               WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webView);
        mSplashScreen = findViewById(R.id.splashScreen);
        mCustomViewContainer = findViewById(R.id.customViewContainer);

        setupWebView();
        handleSplashScreen();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(AppConfig.ENABLE_JAVASCRIPT);
        webSettings.setDomStorageEnabled(AppConfig.ENABLE_DOM_STORAGE);
        webSettings.setBuiltInZoomControls(AppConfig.ENABLE_ZOOM_CONTROLS);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        
        // Essential for HTML5 Video
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new WebViewClient());

        // Handling HTML5 Fullscreen Video
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (mCustomView != null) {
                    onHideCustomView();
                    return;
                }
                mCustomView = view;
                mCustomViewCallback = callback;
                mCustomViewContainer.addView(mCustomView);
                mCustomViewContainer.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
            }

            @Override
            public void onHideCustomView() {
                if (mCustomView == null) return;
                mCustomViewContainer.setVisibility(View.GONE);
                mCustomViewContainer.removeView(mCustomView);
                mCustomView = null;
                mCustomViewCallback.onCustomViewHidden();
                mWebView.setVisibility(View.VISIBLE);
            }
        });

        mWebView.loadUrl(AppConfig.URL_TO_LOAD);
    }

    private void handleSplashScreen() {
        if (AppConfig.SHOW_SPLASH_SCREEN) {
            mSplashScreen.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mSplashScreen.setVisibility(View.GONE);
                if (mCustomView == null) mWebView.setVisibility(View.VISIBLE);
            }, AppConfig.SPLASH_SCREEN_DURATION);
        } else {
            mSplashScreen.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mCustomView != null) {
            // Exit fullscreen if active
            mWebView.getWebChromeClient().onHideCustomView();
        } else if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
