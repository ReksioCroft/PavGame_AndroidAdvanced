package ro.tav.pavgame.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;
import ro.tav.pavgame.presentation.view.recycleViewAux.GamesAdapter;
import ro.tav.pavgame.presentation.view.recycleViewAux.PavGameBindingAdapter;
import timber.log.Timber;


public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_recycler_view );

        //butonul de sus pentru a inchide activitatea
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );

        //adaugam activitatea in lista
        PavGameApplication.addActivity( this );

        // get recycler view from xml layout
        RecyclerView mRecyclerViewGames = findViewById( R.id.recycler_view_contacts_1 );

        // get the adapter instance
        final GamesAdapter gamesAdapter = new GamesAdapter( mRecyclerViewGames.getContext() );

        //binding

        //verificam sa vedem daca vrem sa afisam doar pt un anumit utilizator
        Bundle b = getIntent().getExtras();
        String specificUser = null; // specificUser==null=>afisam toti utilizatorii
        if ( b != null )
            specificUser = b.getString( "user" );

        PavGameBindingAdapter.recycleViewGamesBinding( mRecyclerViewGames, gamesAdapter, specificUser );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PavGameApplication.removeActivity( this );
    }

    public void showSpecificUserGames( View view ) {//clickListener pt user din cardView
        Timber.v( "Specific game activity creating" );
        Intent intent = new Intent( RecyclerViewActivity.this, RecyclerViewActivity.class );
        Bundle b = new Bundle();
        TextView textView = ( TextView ) view;  //suntem siguri ca primim textView
        b.putString( "user", textView.getText().toString() ); //user_email
        intent.putExtras( b ); //Put your id to your next Intent
        startActivity( intent );//cream o noua activitate pt utilizatorul specific
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if ( item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
