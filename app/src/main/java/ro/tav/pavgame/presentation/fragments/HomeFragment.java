package ro.tav.pavgame.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ro.tav.pavgame.R;
import ro.tav.pavgame.presentation.PavGameViewModel;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View root = super.onCreateView( inflater, container, savedInstanceState );
        if ( root != null ) {
            TextView textView = root.findViewById( R.id.text_home );
            textView.setText( String.format( getString( R.string.welcome ), PavGameViewModel.getUserName() ) );
        }
        return root;
    }

    public HomeFragment( int contentLayoutId ) {
        super( contentLayoutId );
    }
}
