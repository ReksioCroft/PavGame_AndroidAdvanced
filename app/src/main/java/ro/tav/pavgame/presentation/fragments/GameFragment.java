package ro.tav.pavgame.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ro.tav.pavgame.R;

public class GameFragment extends Fragment {
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_game, container, false );
    }

    public GameFragment() {
        // Required empty public constructor
    }
}
