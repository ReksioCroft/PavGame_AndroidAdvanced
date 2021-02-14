package ro.tav.pavgame.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;
import ro.tav.pavgame.presentation.PavGameFragmentStack;
import ro.tav.pavgame.presentation.fragments.GameFragment;
import ro.tav.pavgame.presentation.fragments.HomeFragment;
import ro.tav.pavgame.presentation.fragments.SlideshowFragment;
import ro.tav.pavgame.presentation.notification.PavGameNotificationFactory;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        openFragment( new HomeFragment( R.layout.fragment_home ) );
        setTitle( getString( R.string.menu_home ).toUpperCase() );


        //afisam o notificare de bun venit
        PavGameApplication.getNotificationManager().notify( PavGameNotificationFactory.getHelloNotificationId(),
                PavGameNotificationFactory.createHelloNotification( this ) );

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
            openFragment( new HomeFragment( R.layout.fragment_home ) );
            setTitle( getString( R.string.menu_home ).toUpperCase() );
        } else if ( id == R.id.nav_game ) {
            openFragment( new GameFragment( R.layout.fragment_game ) );
            setTitle( getString( R.string.menu_game ).toUpperCase() );
        } else if ( id == R.id.nav_slideshow ) {
            openFragment( new SlideshowFragment( R.layout.fragment_slideshow ) );
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


    public void openCreditActivity( MenuItem item ) {
        startActivity( new Intent( MainActivity.this, CreditsActivity.class ) );
    }

    public void makeLogout( View view ) {
        while ( LoginActivity.getFireBaseCurrentInstance().getCurrentUser() != null )
            LoginActivity.getFireBaseCurrentInstance().signOut();
        finish();
    }
}
