package com.webintoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();

        // রিয়েল অ্যাপ ভাইব এবং পারফরম্যান্স বুস্ট
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); // আধুনিক ওয়েবসাইটের জন্য মাস্ট
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        
        // ক্যাশ সিস্টেম পুরোপুরি অফ করে দেওয়া হলো যাতে স্টোরেজ না খায়
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // অ্যাপের ভেতরেই লিংক ওপেন হবে, বাইরের ব্রাউজারে যাবে না
        mWebView.setWebViewClient(new WebViewClient());

        // ==========================================
        // নিচে আপনার পছন্দমতো যেকোনো একটি চালু রাখুন:
        // ==========================================

        // Option 1: REMOTE RESOURCE (যেকোনো ওয়েবসাইটের লিংক)
        mWebView.loadUrl("https://google.com");

        // Option 2: LOCAL RESOURCE (আপনার assets ফোল্ডারের index.html)
        // mWebView.loadUrl("file:///android_asset/index.html");
    }

    // ব্যাক বাটন চাপলে অ্যাপ ক্লোজ না হয়ে আগের পেজে যাবে
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
