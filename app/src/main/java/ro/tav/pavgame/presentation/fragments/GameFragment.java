package ro.tav.pavgame.presentation.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;
import ro.tav.pavgame.presentation.view.MainActivity;
import timber.log.Timber;

public class GameFragment extends Fragment {
    private final Gson gson;
    private final SharedPreferences sharedPreferences;      ///folosim sharedPreferences pt a stoca pt fiecare utilizator timpul total de joc (de cand a instalat aplicatia)
    private final long timeSpent;
    private final static String pavGameSharedPreference = "pavGameSharedPreference";
    private Chronometer chronometer;

    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_game, container, false );
    }

    public GameFragment() {
        gson = new Gson();
        sharedPreferences = PavGameApplication.getContext().getSharedPreferences( pavGameSharedPreference, Context.MODE_PRIVATE );
        long tAux;
        try {
            String s = sharedPreferences.getString( MainActivity.getUserName(), "0" );//preluam timpul utilizatorului
            tAux = Long.parseLong( s );
        } catch ( Exception e ) {
            Timber.d( e );
            tAux = 0;
        }
        timeSpent = tAux;       ///timpul petrecut(cu precizie de minute) in joc
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        chronometer = requireView().findViewById( R.id.chronometer );
        chronometer.setBase( SystemClock.elapsedRealtime() - timeSpent * 60000 );//asa se initializeaza cronometrul
        chronometer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        long newTime = SystemClock.elapsedRealtime() - chronometer.getBase();//milisecunde petrecut in total, dupa acest joc
        newTime /= 1000; //secunde
        newTime /= 60; //minute

        String json = gson.toJson( newTime );       ///convertim acest timp la un json string
        sharedPreferences.edit().putString( MainActivity.getUserName() + "", json ).apply(); ///updatam valoarea in shared preferances
    }
}
