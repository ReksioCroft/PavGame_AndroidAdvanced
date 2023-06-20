package ro.tav.pavgame.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ro.tav.pavgame.R;

public class WebViewFragment extends Fragment {
    private static final String PAV_GAME_INSPIRATION = "https://www.youtube.com/embed/aCZ4wXPm3A8";

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View root = super.onCreateView( inflater, container, savedInstanceState );
        if ( root != null ) {
            WebView mWebViewExample = root.findViewById( R.id.webview_example );
            mWebViewExample.setWebViewClient( new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading( WebView view, WebResourceRequest request ) {
                    return false;
                }
            } );
            WebSettings webSettings = mWebViewExample.getSettings();
            webSettings.setJavaScriptEnabled( true );
            webSettings.setLoadWithOverviewMode( true );
            webSettings.setUseWideViewPort( true );
            mWebViewExample.loadUrl( PAV_GAME_INSPIRATION );
        }
        return root;
    }

    public WebViewFragment( int contentLayoutId ) {
        super( contentLayoutId );
    }

    public WebViewFragment() {
        super();
    }
}
