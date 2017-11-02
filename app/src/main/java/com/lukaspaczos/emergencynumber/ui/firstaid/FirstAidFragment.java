package com.lukaspaczos.emergencynumber.ui.firstaid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.util.NetworkUtils;
import com.lukaspaczos.emergencynumber.ui.AbstractTabFragment;

public class FirstAidFragment extends AbstractTabFragment {
    public static final String TAG = "FIRST_AID_FRAGMENT";

    private WebView webView;
    private ProgressBar progressBar;
    private View webViewError;

    public FirstAidFragment() {
        // Required empty public constructor
    }

    public static FirstAidFragment newInstance() {
        FirstAidFragment fragment = new FirstAidFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_aid, container, false);

        webView = (WebView) view.findViewById(R.id.first_aid_web_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        webViewError = view.findViewById(R.id.first_aid_web_view_error);

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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result)
                                    Toast.makeText(getActivity(), R.string.error_unknown, Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getActivity(), R.string.error_internet, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

        NetworkUtils.hasActiveInternetConnection(new NetworkUtils.NetworkStateListener() {
            @Override
            public void hasNetworkConnection(final boolean result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result)
                            webView.loadUrl(getString(R.string.first_aid_url));
                        else
                            webView.loadUrl(getString(R.string.first_aid_cache_file_path));
                    }
                });
            }
        });

        return view;
    }
}
