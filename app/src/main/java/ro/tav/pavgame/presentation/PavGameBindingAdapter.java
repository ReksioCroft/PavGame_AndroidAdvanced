package ro.tav.pavgame.presentation;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.data.GameHistory;
import ro.tav.pavgame.domain.GamesAdapter;

public class PavGameBindingAdapter {
    @BindingAdapter( "pavGameInsert" )
    public static void addGameResult( RecyclerView recyclerView, GameHistory mGame ) {
        PavGameViewModel.getGame().insert( mGame );
    }

    @BindingAdapter( "pavGames" )
    public static void recycleViewGamesBinding( RecyclerView mRecyclerViewGames, GamesAdapter gamesAdapter ) {
        if ( mRecyclerViewGames.getAdapter() == null ) {
            // set the adapter to the recycler view
            mRecyclerViewGames.setAdapter( gamesAdapter );
            // define and set layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( mRecyclerViewGames.getContext() );
            mRecyclerViewGames.setLayoutManager( layoutManager );
        }
        //pornim binduirea permanenta intre recyleview si baza de date
        PavGameViewModel.getGame().getAllGames().observeForever( games -> gamesAdapter.setGames( games ) );
    }
}
