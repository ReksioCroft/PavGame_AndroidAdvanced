package ro.tav.pavgame.presentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ro.tav.pavgame.R;
import ro.tav.pavgame.data.GameHistory;
import ro.tav.pavgame.domain.ContactsAdapter;
import ro.tav.pavgame.domain.gameViewModel;

public class RecyclerViewActivity extends AppCompatActivity {

    static private gameViewModel game;
    static private Boolean dbEngineInitialized;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_recycler_view );

        // get recycler view from xml layout
        RecyclerView mRecyclerViewContacts = findViewById( R.id.recycler_view_contacts_1 );

        // get the adapter instance
        final ContactsAdapter contactsAdapter = new ContactsAdapter( this );

        // set the adapter to the recycler view
        mRecyclerViewContacts.setAdapter( contactsAdapter );
        // define and set layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        mRecyclerViewContacts.setLayoutManager( layoutManager );

        game = new ViewModelProvider( this ).get( gameViewModel.class );

        game.getAllGames().observe( this, new Observer < List < GameHistory > >() {
            @Override
            public void onChanged( @Nullable final List < GameHistory > gameHistories ) {
                contactsAdapter.setContacts( gameHistories );
            }
        } );
        if ( !dbEngineInitialized ) {
            dbEngineInitialized = true;
            finish();
        }
    }

    public static void addResult( String nume, String rezultat, String tip ) {
        GameHistory mGame = new GameHistory();
        mGame.setGameType( tip );
        mGame.setNume( nume );
        mGame.setResult( rezultat );
        game.insert( mGame );
    }

    public static void setDbEngineInitialized( boolean val ) {
        dbEngineInitialized = val;
    }
}
