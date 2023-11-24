package ro.tav.pavgame.presentation.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Pair;
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
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.Objects;

import ro.tav.pavgame.R;
import ro.tav.pavgame.data.model.GameEntity;
import ro.tav.pavgame.presentation.notification.PavGameNotificationFactory;
import ro.tav.pavgame.presentation.notification.PavGameService;
import ro.tav.pavgame.presentation.viewmodel.PavGameApplication;
import ro.tav.pavgame.presentation.viewmodel.PavGameViewModel;
import ro.tav.pavgame.presentation.viewmodel.PavGameViewModelFactory;
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
    private Intent gameInProgressServiceIntent;
    private Pair< Integer, Integer > pozGaura = null;

    private enum GameState {ToStart, Init, InProgress, Finished}

    private GameState gameState = GameState.ToStart;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        return super.onCreateView( inflater, container, savedInstanceState );
    }

    public GameFragment( int contentLayoutId ) {
        super( contentLayoutId );
    }

    public GameFragment() {
        super();
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );

        //obtin din sharedPreferances cat timp am avut in app pana acum, pt a seta cronometrul
        long timeSpent;   //timpul petrecut(cu precizie de minute) in joc
        sharedPreferences = super.requireContext().getSharedPreferences( pavGameSharedPreference, Context.MODE_PRIVATE );
        try {
            String s = sharedPreferences.getString( PavGameViewModel.getUserName( getActivity() ), "0" );//preluam timpul utilizatorului
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
        Button button = requireView().findViewById( R.id.startGameButton );
        button.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if ( gameState == GameState.ToStart ) {
                    startGame( v );
                } else if ( gameState == GameState.Finished && pozGaura != null ) {
                    nrDala = 0;
                    solve( 0, 0, lat, pozGaura.first, pozGaura.second );
                    pozGaura = null;
                    gameState = GameState.ToStart;
                    ( ( Button ) v ).setText( getString( R.string.start_game ) );
                } else {
                    Timber.e( "invalid game state" );
                }
            }
        } );
    }


    private void solve( int lstart, int cstart, int latura, int lgaura, int cgaura ) {
        int[] dlin = { 0, 0, 1, 1 };
        int[] dcol = { 0, 1, 0, 1 };
        int i0 = 0;

        if ( latura > 1 ) {                            ///daca matricea se mai poate impartii in submatrici
            latura >>= 1;                              ///injumatatim latura
            for ( int i = 0; i < 4; i++ ) {
                if ( lgaura >= lstart + latura * dlin[ i ] &&
                        lgaura < lstart + latura * ( dlin[ i ] + 1 ) &&
                        cgaura >= cstart + latura * dcol[ i ] &&
                        cgaura < cstart + latura * ( dcol[ i ] + 1 ) )
                    i0 = i;                              ///luam fiecare din cele 4 submatrici, si retinem in care se afla gaura
            }
            nrDala++;                                    ///incrementam nr dalei cu care pavam
            for ( int i = 0; i < 4; i++ )
                if ( i != i0 ) {                         ///daca nu suntem in submatricea cu gaura
                    int lin = lstart + latura + dlin[ i ] - 1;
                    int col = cstart + latura + dcol[ i ] - 1;
                    matrix[ lin ][ col ] = nrDala;       ///pavam cele 3 colturi din jurul submatricii cu gaura cu o dala
                    setButon( lin * lat + col );         ///afisam in ui noua dala
                }
            for ( int i = 0; i < 4; i++ ) {                                ///impartim matricea in cele 4 submatrici
                if ( i != i0 )                                             ///daca suntem in submatricea fara gaura, setam gaura artificiala creata de noi
                    solve( lstart + latura * dlin[ i ],
                            cstart + latura * dcol[ i ],
                            latura,
                            lstart + latura + dlin[ i ] - 1,
                            cstart + latura + dcol[ i ] - 1 );
                else                                                       ///daca suntem in submatricea ce are deja gaura, o transmitem mai departe
                    solve( lstart + latura * dlin[ i ],
                            cstart + latura * dcol[ i ],
                            latura,
                            lgaura,
                            cgaura );
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //luam noul timpul total petrecut in aplicatie (cat era initial + cat s-a jucat utilizatorul acum)
        long newTime = SystemClock.elapsedRealtime() - chronometer.getBase();//milisecunde petrecut in total, dupa acest joc
        newTime /= 1000; //secunde
        newTime /= 60; //minute

        //convertim acest numar (valoare rotunjita la minute) tinut pe Long la json
        //si ii fac update in shared preferances
        Gson gson = new Gson();
        String json = gson.toJson( newTime );       ///convertim acest timp la un json string
        sharedPreferences.edit().putString( PavGameViewModel.getUserName( getActivity() ) + "", json ).apply(); ///updatam valoarea in shared preferances

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        stopGameService();
    }

    public void startGame( View view ) {    //crearea si initializarea butoanelor jocului
        if ( gameState == GameState.ToStart ) {
            String s = "", message;
            int nrInt = 0;
            input = requireView().findViewById( R.id.pavGameInputText );
            try {
                //obtinem nr introdus si calculam latura
                s = input.getText().toString();
                nrInt = Integer.parseInt( s );
                if ( nrInt < 0 || nrInt > 3 )
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
                            constraintSet.connect( button.getId(), ConstraintSet.START, -1 + button.getId(), ConstraintSet.END );
                            //rightOfButton->left
                            constraintSet.connect( -1 + button.getId(), ConstraintSet.END, button.getId(), ConstraintSet.START );
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
                message = getString( R.string.copac );
                Button gameButton = ( Button ) view;
                gameButton.setEnabled( false );
                gameButton.setText( getString( R.string.start_game ) );
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                    requireActivity().startForegroundService( gameInProgressServiceIntent );
                } else {
                    requireActivity().startService( gameInProgressServiceIntent );
                }
                gameState = GameState.Init;
            } catch ( Exception e ) {
                if ( s.length() == 0 )  //daca s-a introdus stringul vid
                    message = getString( R.string.errLipsaDimLatura );
                else if ( nrInt < 0 || nrInt > 3 )
                    message = getString( R.string.invalidInput );
                else
                    message = getString( R.string.unknownError );
            }
            Toast.makeText( getContext(), message, Toast.LENGTH_LONG ).show();
        } else {
            Timber.e( "Invalid game state" );
        }
    }

    //colorare buton
    private void setButon( int nr ) {
        Button button = requireView().findViewById( nr );
        button.setText( String.valueOf( nrDala ) );
        button.setBackgroundColor( getResources().getColor( R.color.colorPrimary, requireContext().getTheme() ) );
        button.setTextColor( getResources().getColor( R.color.colorAccent, requireContext().getTheme() ) );
    }

    private class handleClick implements View.OnClickListener { //controller-ele in joc

        @Override
        public void onClick( View view ) {
            String messege, s1 = "", s2;
            boolean result;
            Button button = ( Button ) view;
            //luam pozitia dalei de inserare
            input = requireView().findViewById( R.id.pavGameInputText );
            int l = button.getId() / lat;
            int c = button.getId() % lat;
            if ( gameState == GameState.Init ) {//initializarea primul buton pt gaura
                button.setBackgroundColor( getResources().getColor( R.color.colorAccent, requireContext().getTheme() ) );
                button.setText( "0" );
                button.setTextColor( getResources().getColor( R.color.colorPrimary, requireContext().getTheme() ) );
                matrix[ l ][ c ] = -1;
                pozGaura = new Pair<>( l, c );
                button = requireView().findViewById( R.id.startGameButton );
                button.setText( R.string.alegeDala );
                gameState = GameState.InProgress;
            } else if ( gameState == GameState.InProgress ) {//jocul continua
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
            } else if ( gameState == GameState.Finished ) {
                if ( nrGreseli > nrGreseliMax ) {  //pierdere
                    messege = getString( R.string.esec ) + " :(";
                    Toast.makeText( getContext(), messege, Toast.LENGTH_LONG ).show();
                } else if ( nrDala * 3 + 1 == lat * lat ) { //castig
                    messege = getString( R.string.victorie ) + " :)";
                    Toast.makeText( getContext(), messege, Toast.LENGTH_LONG ).show();
                }
            } else {
                Timber.e( "Invalid game state" );
            }

            // finish the game
            if ( gameState == GameState.InProgress && ( nrDala * 3 + 1 == lat * lat || nrGreseli > nrGreseliMax ) ) {
                gameState = GameState.Finished;
                requireView().findViewById( R.id.startGameButton ).setEnabled( true );
                button = requireView().findViewById( R.id.startGameButton );
                if ( nrDala * 3 + 1 == lat * lat ) {
                    messege = getString( R.string.victorie ) + "! " + getString( R.string.solution );
                    button.setText( messege );
                    result = true;
                    s1 = getString( R.string.Win );
                    s2 = getString( R.string.victorie ) + ", " + PavGameViewModel.getUserName( getActivity() ) + "!";
                } else {
                    messege = getString( R.string.esec ) + "! " + getString( R.string.solution );
                    button.setText( messege );
                    result = false;
                    s1 = getString( R.string.Lose );
                    s2 = getString( R.string.esec ) + ", " + PavGameViewModel.getUserName( getActivity() ) + "!";
                }

                //adaugam jocul in baza de date folosindu-ne de viewModel
                PavGameViewModel pavGameViewModel = new ViewModelProvider( requireActivity(), new PavGameViewModelFactory( requireActivity().getApplication() ) ).get( PavGameViewModel.class );
                String timeStamp = new Timestamp( System.currentTimeMillis() ).toString();  //id-ul va fi timpul la care s-a facut adaugarea
                GameEntity game = new GameEntity( "", Objects.requireNonNull( PavGameViewModel.getUserName( getActivity() ) ), lat, result, timeStamp );
                pavGameViewModel.insertGame( game );
                PavGameApplication.getNotificationManager().notify( PavGameNotificationFactory.getHelloNotificationId(),
                        PavGameNotificationFactory.createCustomHelloNotification( getContext(),
                                s1, s2 ) );
                Toast.makeText( getContext(), messege, Toast.LENGTH_LONG ).show();
                stopGameService(); //oprim si serviciul cand se castiga/pierde jocul
            }
        }
    }

    private void stopGameService() {
        requireActivity().stopService( gameInProgressServiceIntent );//oprirea serviciului
    }
}
