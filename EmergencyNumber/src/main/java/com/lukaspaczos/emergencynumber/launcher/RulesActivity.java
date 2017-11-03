package com.lukaspaczos.emergencynumber.launcher;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.util.NetworkUtils;

public class RulesActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private View webViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.first_aid_web_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        webViewError = findViewById(R.id.first_aid_web_view_error);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setSupportZoom(true);
        webView.setClickable(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                webViewError.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);

                NetworkUtils.hasActiveInternetConnection(new NetworkUtils.NetworkStateListener() {
                    @Override
                    public void hasNetworkConnection(final boolean result) {
                        RulesActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result)
                                    Toast.makeText(RulesActivity.this, R.string.error_unknown, Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(RulesActivity.this, R.string.error_internet, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

        webView.loadUrl(getString(R.string.rules_url));
    }
}
