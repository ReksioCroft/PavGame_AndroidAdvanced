package ro.tav.pavgame.presentation.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;
import ro.tav.pavgame.presentation.PavGameService;
import ro.tav.pavgame.presentation.PavGameViewModel;
import ro.tav.pavgame.presentation.notification.PavGameNotificationFactory;
import timber.log.Timber;

public class GameFragment extends Fragment {
    private SharedPreferences sharedPreferences;      ///folosim sharedPreferences pt a stoca pt fiecare utilizator timpul total de joc (de cand a instalat aplicatia)
    private final static String pavGameSharedPreference = "pavGameSharedPreference";
    private Chronometer chronometer;
    private int lat;
    private int nrDala;
    private static final int nrGreseliMax = 5;
    private int nrGreseli;
    private final int[][] matrix = new int[ 256 ][ 256 ];
    private EditText input;
    private boolean started;
    private boolean finished;
    private Intent gameInProgressServiceIntent;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        return super.onCreateView( inflater, container, savedInstanceState );
    }

    public GameFragment( int contentLayoutId ) {
        super( contentLayoutId );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );

        //obtin din sharedPreferances cat timp am avut in app pana acum, pt a seta cronometrul
        long timeSpent;   //timpul petrecut(cu precizie de minute) in joc
        sharedPreferences = super.requireContext().getSharedPreferences( pavGameSharedPreference, Context.MODE_PRIVATE );
        try {
            String s = sharedPreferences.getString( PavGameViewModel.getUserName(), "0" );//preluam timpul utilizatorului
            timeSpent = Long.parseLong( s );
        } catch ( Exception e ) {
            Timber.d( e );
            timeSpent = 0;
        }

        //setez cronometrul
        chronometer = requireView().findViewById( R.id.chronometer );
        chronometer.setBase( SystemClock.elapsedRealtime() - timeSpent * 60000 );//asa se initializeaza cronometrul
        chronometer.start();

        //intent pt serviciul din foreground ce se creeaza cand e un joc in desfasurare
        gameInProgressServiceIntent = new Intent( requireContext(), PavGameService.class );

        //adaugam listener pt butonul de joc, pt a putea incepe jocul
        Button button = ( Button ) requireView().findViewById( R.id.startGameButton );
        button.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick( View v ) {
                startGame( v );
            }
        } );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //luam noul timpul total petrecut in aplicatie (cat era initial + cat s-a jucat utilizatorul acum)
        long newTime = SystemClock.elapsedRealtime() - chronometer.getBase();//milisecunde petrecut in total, dupa acest joc
        newTime /= 1000; //secunde
        newTime /= 60; //minute

        //convertim acest numar (valoare rotunjita la minute) tinut pe Long la json
        //si ii fac update in shared preferances
        Gson gson = new Gson();
        String json = gson.toJson( newTime );       ///convertim acest timp la un json string
        sharedPreferences.edit().putString( PavGameViewModel.getUserName() + "", json ).apply(); ///updatam valoarea in shared preferances

        stopGameService();
    }

    public void startGame( View view ) {    //crearea si initializarea butoanelor jocului
        String s = "", messege;
        int nrInt = 0;
        started = finished = false;
        input = requireView().findViewById( R.id.pavGameInputText );

        try {
            //obtinem nr introdus si calculam latura
            s = input.getText().toString();
            nrInt = Integer.parseInt( s );
            if ( nrInt < 0 || nrInt > 4 )
                throw new Exception( getString( R.string.invalidInput ) );
            lat = 1 << nrInt;
            input.setHint( R.string.pozDala );
            nrDala = nrGreseli = 0;
            ConstraintLayout pavGameBoard = requireView().findViewById( R.id.pavGameBoard );
            pavGameBoard.removeAllViews();

            for ( int i = 0; i < lat; i++ ) {
                for ( int j = 0; j < lat; j++ ) {
                    //adaugam cate un buton pt fiecare casuta
                    matrix[ i ][ j ] = 0;
                    Button button = new Button( getContext() );
                    button.setLayoutParams( new ConstraintLayout.LayoutParams( ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_SPREAD, ConstraintLayout.LayoutParams.WRAP_CONTENT ) );
                    button.setId( lat * i + j );
                    button.setOnClickListener( new handleClick() );//listener pt a putea selecta optiunea
                    pavGameBoard.addView( button );


                    //setam constraint-urile
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone( pavGameBoard );

                    //legatura in sus
                    if ( i == 0 ) { //top->topOfParrent
                        constraintSet.connect( button.getId(), ConstraintSet.TOP, R.id.pavGameBoard, ConstraintSet.TOP );
                    } else {//top->bottomOfButton
                        constraintSet.connect( button.getId(), ConstraintSet.TOP, button.getId() - lat, ConstraintSet.BOTTOM );
                        //bottomOfButton->top
                        constraintSet.connect( button.getId() - lat, ConstraintSet.BOTTOM, button.getId(), ConstraintSet.TOP );
                    }

                    //legatura la stanga
                    if ( j == 0 ) {//left->leftOfParrent
                        constraintSet.connect( button.getId(), ConstraintSet.START, R.id.pavGameBoard, ConstraintSet.START );
                    } else {//left->rightOfButton
                        constraintSet.connect( button.getId(), ConstraintSet.START, button.getId() - ( 1 ), ConstraintSet.END );
                        //rightOfButton->left
                        constraintSet.connect( button.getId() - ( 1 ), ConstraintSet.END, button.getId(), ConstraintSet.START );
                    }
                    //legatura la dreapta
                    //right->endOfParrent
                    constraintSet.connect( button.getId(), ConstraintSet.END, R.id.pavGameBoard, ConstraintSet.END );

                    //legatura in jos
                    //bottom->bottomOfParrent
                    constraintSet.connect( button.getId(), ConstraintSet.BOTTOM, R.id.pavGameBoard, ConstraintSet.BOTTOM );

                    //aplicam modificarile
                    constraintSet.applyTo( pavGameBoard );
                }
            }
            //modificam mesajul de pe buton si il dezactivam
            messege = getString( R.string.copac );
            Button gameButton = ( Button ) view;
            gameButton.setEnabled( false );
            gameButton.setText( getString( R.string.start_game ) );
            requireActivity().startService( gameInProgressServiceIntent );

        } catch ( Exception e ) {
            if ( s.length() == 0 )  //daca s-a introdus stringul vid
                messege = getString( R.string.errLipsaDimLatura );
            else if ( nrInt < 0 || nrInt > 4 )
                messege = getString( R.string.invalidInput );
            else
                messege = getString( R.string.unknownError );
        }
        Toast.makeText( getContext(), messege, Toast.LENGTH_LONG ).show();
    }

    private class handleClick implements View.OnClickListener { //controller-ele in joc
        //un butonel din tabla mare
        private Button button;

        //colorare buton
        private void setButon( int nr ) {
            button = requireView().findViewById( nr );
            button.setText( String.valueOf( nrDala ) );
            button.setBackgroundColor( getResources().getColor( R.color.colorPrimary, requireContext().getTheme() ) );
            button.setTextColor( getResources().getColor( R.color.colorAccent, requireContext().getTheme() ) );
        }

        public void onClick( View view ) {
            String messege, s1 = "", s2;
            boolean result;
            button = ( Button ) view;
            //luam pozitia dalei de inserare
            input = requireView().findViewById( R.id.pavGameInputText );
            int l = button.getId() / lat;
            int c = button.getId() % lat;
            if ( !started ) {//initializarea primul buton pt gaura
                started = true;
                button.setBackgroundColor( getResources().getColor( R.color.colorAccent, requireContext().getTheme() ) );
                button.setText( "0" );
                button.setTextColor( getResources().getColor( R.color.colorPrimary, requireContext().getTheme() ) );
                matrix[ l ][ c ] = -1;
                button = requireView().findViewById( R.id.startGameButton );
                button.setText( R.string.alegeDala );
            } else if ( nrGreseli > nrGreseliMax ) {  //pierdere
                messege = getString( R.string.esec ) + " :(";
                Toast.makeText( getContext(), messege, Toast.LENGTH_LONG ).show();
            } else if ( nrDala * 3 + 1 == lat * lat ) { //castig
                messege = getString( R.string.victorie ) + " :)";
                Toast.makeText( getContext(), messege, Toast.LENGTH_LONG ).show();
            } else {//jocul continua
                try {
                    s1 = input.getText().toString();
                    int tipDala = Integer.parseInt( s1 );   //obtinem val de int din input
                    messege = getString( R.string.mutareIncorecta );
                    if ( tipDala < 1 || tipDala > 4 )   ///verificam sa fie un tip de pozitie correct
                        throw new Exception( getString( R.string.dalaIncorecta ) );
                    else if ( tipDala == 1 ) {//mutarile
                        if ( matrix[ l ][ c ] == 0 && matrix[ l + 1 ][ c ] == 0 && matrix[ l ][ c + 1 ] == 0 && ( l + 1 ) < lat && ( c + 1 ) < lat ) {
                            nrDala++;
                            matrix[ l ][ c ] = matrix[ l + 1 ][ c ] = matrix[ l ][ c + 1 ] = nrDala;
                            setButon( lat * l + c );
                            setButon( lat * ( l + 1 ) + c );
                            setButon( lat * l + ( c + 1 ) );
                        } else {
                            Toast.makeText( getContext(), messege, Toast.LENGTH_SHORT ).show();
                            nrGreseli++;
                        }
                    } else if ( tipDala == 2 ) {
                        if ( matrix[ l ][ c ] == 0 && matrix[ l + 1 ][ c ] == 0 && matrix[ l ][ c - 1 ] == 0 && ( l + 1 ) < lat ) {
                            nrDala++;
                            matrix[ l ][ c ] = matrix[ l + 1 ][ c ] = matrix[ l ][ c - 1 ] = nrDala;
                            setButon( lat * l + c );
                            setButon( lat * ( l + 1 ) + c );
                            setButon( lat * l + ( c - 1 ) );
                        } else {
                            Toast.makeText( getContext(), messege, Toast.LENGTH_SHORT ).show();
                            nrGreseli++;
                        }
                    } else if ( tipDala == 3 ) {
                        if ( matrix[ l ][ c ] == 0 && matrix[ l - 1 ][ c ] == 0 && matrix[ l ][ c + 1 ] == 0 && ( c + 1 ) < lat ) {
                            nrDala++;
                            matrix[ l ][ c ] = matrix[ l - 1 ][ c ] = matrix[ l ][ c + 1 ] = nrDala;
                            setButon( lat * l + c );
                            setButon( lat * ( l - 1 ) + c );
                            setButon( lat * l + ( c + 1 ) );
                        } else {
                            Toast.makeText( getContext(), messege, Toast.LENGTH_SHORT ).show();
                            nrGreseli++;
                        }
                    } else {
                        if ( matrix[ l ][ c ] == 0 && matrix[ l - 1 ][ c ] == 0 && matrix[ l ][ c - 1 ] == 0 ) {
                            nrDala++;
                            matrix[ l ][ c ] = matrix[ l - 1 ][ c ] = matrix[ l ][ c - 1 ] = nrDala;
                            setButon( lat * l + c );
                            setButon( lat * ( l - 1 ) + c );
                            setButon( lat * l + ( c - 1 ) );
                        } else {
                            nrGreseli++;
                            Toast.makeText( getContext(), messege, Toast.LENGTH_SHORT ).show();
                        }
                    }
                } catch ( Exception e ) {
                    if ( s1.length() == 0 )
                        messege = getString( R.string.errLipsaDimLatura );
                    else
                        messege = getString( R.string.dalaIncorecta );
                    Toast.makeText( getContext(), messege, Toast.LENGTH_SHORT ).show();
                }
            }
            if ( !finished && ( nrDala * 3 + 1 == lat * lat || nrGreseli > nrGreseliMax ) ) {
                finished = true;//daca se ajunge la finish
                requireView().findViewById( R.id.startGameButton ).setEnabled( true );
                button = requireView().findViewById( R.id.startGameButton );
                if ( nrDala * 3 + 1 == lat * lat ) {
                    messege = getString( R.string.victorie ) + " :)";
                    button.setText( messege );
                    result = true;
                    s1 = getString( R.string.Win );
                    s2 = getString( R.string.victorie ) + ", " + PavGameViewModel.getUserName() + "!";
                } else {
                    messege = getString( R.string.esec ) + " :(";
                    button.setText( messege );
                    result = false;
                    s1 = getString( R.string.Lose );
                    s2 = getString( R.string.esec ) + ", " + PavGameViewModel.getUserName() + "!";
                }

                //adaugam jocul folosindu-ne de viewModel
                PavGameViewModel.addResult( PavGameViewModel.getUserName(), result, lat );
                PavGameApplication.getNotificationManager().notify( PavGameNotificationFactory.getHelloNotificationId(),
                        PavGameNotificationFactory.createCustomHelloNotification( getContext(),
                                s1, s2 ) );
                Toast.makeText( getContext(), messege, Toast.LENGTH_LONG ).show();
                stopGameService(); //optimi si serviciul cand se castiga/pierde jocul
            }
        }

    }

    private void stopGameService() {
        requireActivity().stopService( gameInProgressServiceIntent );//oprirea serviciului
    }
}
