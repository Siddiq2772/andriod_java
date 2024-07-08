package com.example.movies7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
    private static final String WEBVIEW_STATE = "webview_state";

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
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setSupportMultipleWindows(true);

            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new MyWebChromeClient());

            if (savedInstanceState != null) {
                webView.restoreState(savedInstanceState.getBundle(WEBVIEW_STATE));
            } else {
                webView.loadUrl("https://movies7.to/");
            }
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null && uri.getHost() != null && uri.getHost().endsWith("movies7.to")) {
            if (webView != null) {
                webView.loadUrl(uri.toString());
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
            webView.goBack();
        } else {
            super.onBackPressed();
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
            WebSettings webSettings = popupWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
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
        // Handle orientation change if necessary
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (webView != null) {
            Bundle webViewBundle = new Bundle();
            webView.saveState(webViewBundle);
            outState.putBundle(WEBVIEW_STATE, webViewBundle);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (webView != null) {
            webView.restoreState(savedInstanceState.getBundle(WEBVIEW_STATE));
        }
    }
}
