package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import javax.security.auth.callback.Callback;

public class StoreWebView extends AppCompatActivity {

    private WebView wvStore;
    private String webUrl;
    private ProgressBar pbStorePageLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_web_view);

        wvStore = findViewById(R.id.wvStore);
        pbStorePageLoading = findViewById(R.id.pbStorePageLoading);

        String storePageUrl = getIntent().getStringExtra("EXTRA_STORE_IMAGE_URL");
        wvStore.setWebViewClient(new WebViewClient());
        wvStore.loadUrl(storePageUrl);
    }

    public class WebViewClient extends android.webkit.WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view,url,favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pbStorePageLoading.setVisibility(View.GONE);
        }
    }
}