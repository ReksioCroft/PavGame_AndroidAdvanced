package ro.tav.pavgame.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ro.tav.pavgame.presentation.view.LoginActivity;
import ro.tav.pavgame.R;

public class HomeFragment extends Fragment {
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View root = inflater.inflate( R.layout.fragment_home, container, false );
        TextView textView = root.findViewById( R.id.text_home );
        textView.setText( String.format( getString( R.string.welcome ), Objects.requireNonNull( LoginActivity.getFireBaseCurrentInstance().getCurrentUser() ).getEmail() ) );
        return root;
    }

    public HomeFragment() {
        // Required empty public constructor
    }
}
