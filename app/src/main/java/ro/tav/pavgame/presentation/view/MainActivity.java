package ro.tav.pavgame.presentation.view;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;
import ro.tav.pavgame.presentation.PavGameFragmentStack;
import ro.tav.pavgame.presentation.PavGameService;
import ro.tav.pavgame.presentation.PavGameViewModel;
import ro.tav.pavgame.presentation.fragments.GameFragment;
import ro.tav.pavgame.presentation.fragments.HomeFragment;
import ro.tav.pavgame.presentation.fragments.SlideshowFragment;
import ro.tav.pavgame.presentation.notification.PavGameNotificationFactory;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText input;
    private Boolean started;
    private Boolean finished;
    private int lat;
    private int nrDala;
    private static final int nrGreseliMax = 5;
    private int nrGreseli;
    private final int[][] matrix = new int[ 256 ][ 256 ];
    private static String userName = "Current User";
    public static final int NOTIFICATION_LAUNCH_CODE = 485;
    private NotificationManager notificationManager;
    private Intent gameInProgressServiceIntent;
    private final PavGameFragmentStack fragmentStack = new PavGameFragmentStack();

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        ///adaugam instanta
        PavGameApplication.addActivity( this );

        //ne afisam un mesaj cu timber
        Timber.i( "MainActivity created" );

        //setam ce layout deschidem
        setContentView( R.layout.activity_main );

        //toolbarul de sus
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        //butonul cu care vom accesa panoul cu scoruri
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                startActivity( new Intent( MainActivity.this, RecyclerViewActivity.class ) );
            }
        } );

        //navigation drawer
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        //aduagam textul in navigation drwwer si adaugam listener pt a il putea folosi
        NavigationView navigationView = findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
        View headerView = navigationView.getHeaderView( 0 );
        TextView textView = headerView.findViewById( R.id.nav_header_subtitle );
        textView.setText( Objects.requireNonNull( LoginActivity.getFireBaseCurrentInstance().getCurrentUser() ).getEmail() );

        //deschidem fragmentul acasa
        openFragment( new HomeFragment() );
        setTitle( getString( R.string.menu_home ).toUpperCase() );

        //ne initializam sistemul de notificari
        notificationManager = ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
        //afisam o notificare de bun venit
        notificationManager.notify( PavGameNotificationFactory.HELLO_NOTIFICATION_ID,
                PavGameNotificationFactory.createHelloNotification( this ) );

        //intent for the service of gameInProgress
        gameInProgressServiceIntent = new Intent( this, PavGameService.class );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        } else {

            if ( fragmentStack.getNrOfItems() > 1 && fragmentStack.peek().getClass() != GameFragment.class ) {
                popFragment();
            } else {
                Toast.makeText( MainActivity.this, getString( R.string.longPressFinishGame ), Toast.LENGTH_LONG ).show();
            }

        }
    }

    @Override
    public boolean onKeyLongPress( int keyCode, KeyEvent event ) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            popFragment();
            if ( fragmentStack.empty() ) {
                while ( LoginActivity.getFireBaseCurrentInstance().getCurrentUser() != null )
                    LoginActivity.getFireBaseCurrentInstance().signOut();
                finish();
            }
        }
        return super.onKeyLongPress( keyCode, event );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings ) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if ( id == R.id.nav_home ) {
            openFragment( new HomeFragment() );
            setTitle( getString( R.string.menu_home ).toUpperCase() );
        } else if ( id == R.id.nav_game ) {
            openFragment( new GameFragment() );
            setTitle( getString( R.string.menu_game ).toUpperCase() );
        } else if ( id == R.id.nav_slideshow ) {
            openFragment( new SlideshowFragment() );
            setTitle( getString( R.string.menu_infoarena ).toUpperCase() );
        }

        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    private void openFragment( Fragment fragment ) {
        // 4 steps to add dynamically a fragment inside of an activity
        // step 1: create an instance of FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // step 2: create an instance of FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // step 3: replace container content with the fragment content

        while ( fragmentStack.getNrOfItems() > 1 && fragmentStack.peek().getClass() != GameFragment.class ) {
            fragmentTransaction.remove( fragmentStack.pop() );
        }

        //Pentru a nu adauga inca un joc peste jocul curent
        if ( !fragmentStack.isGameInStack() || fragment.getClass() != GameFragment.class ) {
            if ( !fragmentStack.empty() )
                fragmentTransaction.hide( fragmentStack.peek() );
            fragmentTransaction.add( R.id.nav_host_fragment, fragmentStack.push( fragment ) );
        } else {
            fragmentTransaction.show( fragmentStack.peek() );
        }
        // step 4: commit transaction
        fragmentTransaction.commit();
    }

    private void popFragment() {
        // 4 steps to add dynamically a fragment inside of an activity
        // step 1: create an instance of FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // step 2: create an instance of FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // step 3: replace container content with the fragment content
        if ( !fragmentStack.empty() ) {
            if ( fragmentStack.peek().getClass() == GameFragment.class )
                stopGameService();
            fragmentTransaction.remove( fragmentStack.pop() );
        }
        if ( !fragmentStack.empty() ) {
            fragmentTransaction.show( fragmentStack.peek() );
        }
        // step 4: commit transaction
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        PavGameApplication.removeActivity( this );
    }

    public void startGame( View view ) {    //crearea si initializarea butoanelor jocului
        String s = "", messege = "";
        started = finished = Boolean.FALSE;
        input = findViewById( R.id.pavGameInputText );

        try {
            s = input.getText().toString();
            lat = 1 << Integer.parseInt( s );
            input.setHint( R.string.pozDala );
            nrDala = nrGreseli = 0;
            ConstraintLayout pavGameBoard = findViewById( R.id.pavGameBoard );
            pavGameBoard.removeAllViews();

            for ( int i = 0; i < lat; i++ ) {
                for ( int j = 0; j < lat; j++ ) {
                    matrix[ i ][ j ] = 0;
                    Button button = new Button( this );
                    button.setLayoutParams( new ConstraintLayout.LayoutParams( ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_SPREAD, ConstraintLayout.LayoutParams.WRAP_CONTENT ) );
                    button.setId( lat * i + j );
                    button.setOnClickListener( new handleClick() );
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
                        constraintSet.connect( button.getId(), ConstraintSet.START, button.getId() - 1, ConstraintSet.END );
                        //rightOfButton->left
                        constraintSet.connect( button.getId() - 1, ConstraintSet.END, button.getId(), ConstraintSet.START );
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
            messege = getString( R.string.copac );
            Button gameButton = findViewById( R.id.startGameButton );
            gameButton.setEnabled( false );
            gameButton.setText( getString( R.string.start_game ) );
            startService( gameInProgressServiceIntent );

        } catch ( Exception e ) {
            if ( s.length() == 0 )
                messege = getString( R.string.errLipsaDimLatura );
            else
                messege = getString( R.string.unknownError );
        }
        Toast.makeText( MainActivity.this, messege, Toast.LENGTH_LONG ).show();
    }

    public void openCreditActivity( MenuItem item ) {
        startActivity( new Intent( MainActivity.this, CreditsActivity.class ) );
    }

    public void makeLogout( View view ) {
        while ( LoginActivity.getFireBaseCurrentInstance().getCurrentUser() != null )
            LoginActivity.getFireBaseCurrentInstance().signOut();
        finish();
    }

    private class handleClick implements View.OnClickListener { //controller-ele in joc
        private Button button;

        private void setButon( int nr ) {
            button = findViewById( nr );
            button.setText( String.valueOf( nrDala ) );
            button.setBackgroundColor( getResources().getColor( R.color.colorPrimary ) );
            button.setTextColor( getResources().getColor( R.color.colorAccent ) );
        }

        public void onClick( View view ) {
            String messege = "", s1 = "", s2 = "", result = "";
            button = ( Button ) view;
            input = findViewById( R.id.pavGameInputText );
            int l = button.getId() / lat;
            int c = button.getId() % lat;
            if ( !started ) {
                started = true;
                button.setBackgroundColor( getResources().getColor( R.color.colorAccent ) );
                button.setText( "0" );
                button.setTextColor( getResources().getColor( R.color.colorPrimary ) );
                matrix[ l ][ c ] = -1;
                button = findViewById( R.id.startGameButton );
                button.setText( R.string.alegeDala );
            } else if ( nrGreseli > nrGreseliMax ) {
                messege = getString( R.string.esec ) + " :(";
                Toast.makeText( MainActivity.this, messege, Toast.LENGTH_LONG ).show();
            } else if ( nrDala * 3 + 1 == lat * lat ) {
                messege = getString( R.string.victorie ) + " :)";
                Toast.makeText( MainActivity.this, messege, Toast.LENGTH_LONG ).show();
            } else {
                try {
                    s1 = input.getText().toString();
                    int tipDala = Integer.parseInt( s1 );
                    messege = getString( R.string.mutareIncorecta );
                    if ( tipDala < 1 || tipDala > 4 )
                        throw new Exception( getString( R.string.dalaIncorecta ) );
                    else if ( tipDala == 1 ) {
                        if ( matrix[ l ][ c ] == 0 && matrix[ l + 1 ][ c ] == 0 && matrix[ l ][ c + 1 ] == 0 && ( l + 1 ) < lat && ( c + 1 ) < lat ) {
                            nrDala++;
                            matrix[ l ][ c ] = matrix[ l + 1 ][ c ] = matrix[ l ][ c + 1 ] = nrDala;
                            setButon( lat * l + c );
                            setButon( lat * ( l + 1 ) + c );
                            setButon( lat * l + ( c + 1 ) );
                        } else {
                            Toast.makeText( MainActivity.this, messege, Toast.LENGTH_SHORT ).show();
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
                            Toast.makeText( MainActivity.this, messege, Toast.LENGTH_SHORT ).show();
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
                            Toast.makeText( MainActivity.this, messege, Toast.LENGTH_SHORT ).show();
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
                            Toast.makeText( MainActivity.this, messege, Toast.LENGTH_SHORT ).show();
                        }
                    }
                } catch ( Exception e ) {
                    if ( s1.length() == 0 )
                        messege = getString( R.string.errLipsaDimLatura );
                    else
                        messege = getString( R.string.dalaIncorecta );
                    Toast.makeText( MainActivity.this, messege, Toast.LENGTH_SHORT ).show();
                }
            }
            if ( finished == Boolean.FALSE && ( nrDala * 3 + 1 == lat * lat || nrGreseli > nrGreseliMax ) ) {
                finished = Boolean.TRUE;
                findViewById( R.id.startGameButton ).setEnabled( true );
                button = findViewById( R.id.startGameButton );
                if ( nrDala * 3 + 1 == lat * lat ) {
                    messege = getString( R.string.victorie ) + " :)";
                    button.setText( messege );
                    result = "Win";
                    s1 = getString( R.string.Win );
                    s2 = getString( R.string.victorie ) + ", " + userName + "!";
                } else {
                    messege = getString( R.string.esec ) + " :(";
                    button.setText( messege );
                    result = "Lose";
                    s1 = getString( R.string.Lose );
                    s2 = getString( R.string.esec ) + ", " + userName + "!";
                }

                //adaugam jocul folosindu-ne de viewModel
                PavGameViewModel.addResult( findViewById( R.id.recycler_view_contacts_1 ),
                        userName, result, "Game Type: " + lat + "x" + lat );
                notificationManager.notify( PavGameNotificationFactory.HELLO_NOTIFICATION_ID,
                        PavGameNotificationFactory.createCustomHelloNotification( PavGameApplication.getContext(),
                                s1, s2 ) );
                Toast.makeText( MainActivity.this, messege, Toast.LENGTH_LONG ).show();
                stopGameService();
            }
        }

    }

    public void stopGameService() {
        stopService( gameInProgressServiceIntent );
    }

    protected static void setUserName( String name ) {
        if ( name == null )
            name = "Current User";
        userName = name;
    }

}
