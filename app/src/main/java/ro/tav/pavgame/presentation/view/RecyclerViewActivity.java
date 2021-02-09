package ro.tav.pavgame.presentation.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;
import ro.tav.pavgame.domain.GamesAdapter;
import ro.tav.pavgame.presentation.PavGameBindingAdapter;


public class RecyclerViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_recycler_view );
        PavGameApplication.addActivity( this );
        // get recycler view from xml layout
        RecyclerView mRecyclerViewGames = findViewById( R.id.recycler_view_contacts_1 );

        // get the adapter instance
        final GamesAdapter gamesAdapter = new GamesAdapter( mRecyclerViewGames.getContext() );

        //binding
        PavGameBindingAdapter.recycleViewGamesBinding( mRecyclerViewGames, gamesAdapter );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PavGameApplication.removeActivity( this );
    }
}
