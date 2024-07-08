package com.example.aniwave;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private WebView popupWebView;
    private FrameLayout fullScreenContainer;
    private FrameLayout popupContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fullScreenContainer = findViewById(R.id.full_screen_container);
        popupContainer = findViewById(R.id.popup_container);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        webView = findViewById(R.id.webView1);
        if (webView != null) {
            String url = "https://lite.aniwave.to/";
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new MyWebChromeClient());
            webView.loadUrl(url);
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData(); // Get the URL from the intent
        if (uri != null && "lite.aniwave.to".equals(uri.getHost())) {
            if (webView != null) {
                webView.loadUrl(uri.toString()); // Load the URL in the WebView
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (customView != null) {
            exitFullScreen();
        } else if (popupWebView != null) {
            popupContainer.removeView(popupWebView);
            popupWebView = null;
        } else if (webView != null && webView.canGoBack()) {
            webView.goBack(); // Go to the previous page in the WebView
        } else {
            super.onBackPressed(); // Otherwise, perform the default back button action
        }
    }

    private void enterFullScreen(View view, WebChromeClient.CustomViewCallback callback) {
        fullScreenContainer.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        customView = view;
        customViewCallback = callback;
        fullScreenContainer.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
    }

    private void exitFullScreen() {
        fullScreenContainer.removeView(customView);
        customView = null;
        customViewCallback.onCustomViewHidden();
        fullScreenContainer.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            enterFullScreen(view, callback);
        }

        @Override
        public void onHideCustomView() {
            exitFullScreen();
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, android.os.Message resultMsg) {
            popupWebView = new WebView(MainActivity.this);
            popupWebView.getSettings().setJavaScriptEnabled(true);
            popupWebView.setWebViewClient(new WebViewClient());
            popupWebView.setWebChromeClient(this);

            popupContainer.addView(popupWebView, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(popupWebView);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            if (popupWebView != null) {
                popupContainer.removeView(popupWebView);
                popupWebView = null;
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Handle orientation change
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Handle landscape orientation
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Handle portrait orientation
        }
    }
}
