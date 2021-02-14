package ro.tav.pavgame.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ro.tav.pavgame.R;

public class SlideshowFragment extends Fragment {
    private static final String PAV_GAME_INSPIRATION = "https://infoarena.ro/problema/pav";

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View root = super.onCreateView( inflater, container, savedInstanceState );
        WebView mWebViewExample = root.findViewById( R.id.webview_example );
        mWebViewExample.loadUrl( PAV_GAME_INSPIRATION );
        return root;
    }

    public SlideshowFragment( int contentLayoutId ) {
        super( contentLayoutId );
    }
}
