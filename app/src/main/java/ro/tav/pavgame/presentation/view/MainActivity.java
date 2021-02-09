package ro.tav.pavgame.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;
import ro.tav.pavgame.data.GameHistory;
import ro.tav.pavgame.domain.gameViewModel;
import ro.tav.pavgame.presentation.PavGameBindingAdapter;
import ro.tav.pavgame.presentation.fragments.GameFragment;
import ro.tav.pavgame.presentation.fragments.HomeFragment;
import ro.tav.pavgame.presentation.fragments.SlideshowFragment;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText input;
    private String s;
    private String messege;
    private Boolean started;
    private Boolean finished;
    private int lat;
    private int nrDala;
    private static final int nrGreseliMax = 5;
    private int nrGreseli;
    private final int[][] matrix = new int[ 256 ][ 256 ];
    private static String userName = "Current User";
    public static final int NOTIFICATION_LAUNCH_CODE = 485;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        PavGameApplication.instance.activities.add( this );
        Timber.i( "MainActivity created" );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                startActivity( new Intent( MainActivity.this, RecyclerViewActivity.class ) );
            }
        } );
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        PavGameBindingAdapter.setGame( new ViewModelProvider( this ).get( gameViewModel.class ) );

        NavigationView navigationView = findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
        View headerView = navigationView.getHeaderView( 0 );
        TextView textView = headerView.findViewById( R.id.nav_header_subtitle );
        textView.setText( Objects.requireNonNull( LoginActivity.getFireBaseCurrentInstance().getCurrentUser() ).getEmail() );

        openFragment( new HomeFragment() );
        setTitle( "HOME" );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            while ( LoginActivity.getFireBaseCurrentInstance().getCurrentUser() != null )
                LoginActivity.getFireBaseCurrentInstance().signOut();
            super.onBackPressed();
        }
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
            setTitle( "HOME" );
        } else if ( id == R.id.nav_game ) {
            openFragment( new GameFragment() );
            setTitle( "PAV GAME" );
        } else if ( id == R.id.nav_slideshow ) {
            openFragment( new SlideshowFragment() );
            setTitle( "INFOARENA" );
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
        fragmentTransaction.replace( R.id.nav_host_fragment, fragment );
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
        LoginActivity.getFireBaseCurrentInstance().signOut();
        finish();
    }

    private void initializeMatrix() {
        nrDala = nrGreseli = 0;
        for ( int i = 0; i < lat; i++ ) {
            for ( int j = 0; j < lat; j++ ) {
                matrix[ i ][ j ] = 0;
            }
        }
    }

    public void startGame( View view ) {
        started = finished = Boolean.FALSE;
        input = findViewById( R.id.pavGameInputText );
        try {
            s = input.getText().toString();
            lat = 1 << Integer.parseInt( s );
            input.setHint( R.string.pozDala );
            initializeMatrix();
            LinearLayout pavGameBoard = findViewById( R.id.pavGameBoard );
            int maxWidth = getResources().getDisplayMetrics().widthPixels / lat;
            for ( int i = 0; i < lat; i++ ) {
                LinearLayout pavGameSubLayout = new LinearLayout( this );
                pavGameSubLayout.setOrientation( LinearLayout.HORIZONTAL );
                pavGameSubLayout.setLayoutParams( new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT ) );
                for ( int j = 0; j < lat; j++ ) {
                    Button button = new Button( this );
                    button.setLayoutParams( new LinearLayout.LayoutParams( maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT ) );
                    button.setId( lat * i + j );
                    button.setOnClickListener( new handleClick() );
                    pavGameSubLayout.addView( button );
                }

                pavGameBoard.addView( pavGameSubLayout );
                messege = getString( R.string.copac );
            }
            findViewById( R.id.startGameButton ).setEnabled( false );
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

    public void makeLogout( MenuItem logOutButton ) {
        while ( LoginActivity.getFireBaseCurrentInstance().getCurrentUser() != null )
            LoginActivity.getFireBaseCurrentInstance().signOut();
        finish();
    }

    private class handleClick implements View.OnClickListener {
        private Button button;

        private void setButon( int nr ) {
            button = findViewById( nr );
            button.setText( String.valueOf( nrDala ) );
            button.setBackgroundColor( getResources().getColor( R.color.colorPrimary ) );
            button.setTextColor( getResources().getColor( R.color.colorAccent ) );
        }

        public void onClick( View view ) {
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
                messege = getString( R.string.esec );
                Toast.makeText( MainActivity.this, messege, Toast.LENGTH_LONG ).show();
            } else if ( nrDala * 3 + 1 == lat * lat ) {
                messege = getString( R.string.victorie );
                Toast.makeText( MainActivity.this, messege, Toast.LENGTH_LONG ).show();
            } else {
                try {
                    s = input.getText().toString();
                    int tipDala = Integer.parseInt( s );
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
                    if ( s.length() == 0 )
                        messege = getString( R.string.errLipsaDimLatura );
                    else
                        messege = getString( R.string.dalaIncorecta );
                    Toast.makeText( MainActivity.this, messege, Toast.LENGTH_SHORT ).show();
                }
            }
            if ( nrDala * 3 + 1 == lat * lat && finished == Boolean.FALSE ) {
                finished = Boolean.TRUE;
                messege = getString( R.string.victorie );
                Toast.makeText( MainActivity.this, messege, Toast.LENGTH_LONG ).show();
                button = findViewById( R.id.startGameButton );
                button.setText( R.string.victorie );
                GameHistory mGame = new GameHistory();
                mGame.setNume( userName );
                mGame.setResult( "Win" );
                mGame.setGameType( "Game Type: " + lat + "x" + lat );
                //  RecyclerViewActivity.addResult( userName, "Win", "Game Type: " + lat + "x" + lat );
                PavGameBindingAdapter.addResul( findViewById( R.id.recycler_view_contacts_1 ), mGame );
            } else if ( nrGreseli > nrGreseliMax && finished == Boolean.FALSE ) {
                finished = Boolean.TRUE;
                messege = getString( R.string.esec );
                Toast.makeText( MainActivity.this, messege, Toast.LENGTH_LONG ).show();
                button = findViewById( R.id.startGameButton );
                button.setText( R.string.esec );
                GameHistory mGame = new GameHistory();
                mGame.setNume( userName );
                mGame.setResult( "Lose" );
                mGame.setGameType( "Game Type: " + lat + "x" + lat );
                // RecyclerViewActivity.addResult( userName, "Lose", "Game Type: " + lat + "x" + lat );
                PavGameBindingAdapter.addResul( findViewById( R.id.recycler_view_contacts_1 ), mGame );
            }
        }
    }

    protected static void setUserName( String name ) {
        if ( name == null )
            name = "Current User";
        userName = name;
    }
}
