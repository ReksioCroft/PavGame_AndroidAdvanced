package ro.tav.pavgame.presentation.view.recycleViewAux;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.presentation.PavGameViewModel;

public class PavGameBindingAdapter {
    @BindingAdapter( { "game_adapter", "user" } )
    public static void recycleViewGamesBinding( RecyclerView mRecyclerViewGames, GamesAdapter gamesAdapter, String user ) {
        if ( mRecyclerViewGames.getAdapter() == null ) {
            // set the adapter to the recycler view
            mRecyclerViewGames.setAdapter( gamesAdapter );
            // define and set layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( mRecyclerViewGames.getContext() );
            mRecyclerViewGames.setLayoutManager( layoutManager );
        }
        //pornim binduirea permanenta intre recyleview si baza de date
        PavGameViewModel pavGameViewModel = new PavGameViewModel( PavGameApplication.getApplication() );
        if ( user == null ) {
            pavGameViewModel.getAllGames().observeForever( new Observer < List < GameEntity > >() {
                @Override
                public void onChanged( @Nullable final List < GameEntity > games ) {
                    gamesAdapter.setGames( games );
                }
            } );
        } else {
            pavGameViewModel.getSpecificGamesbyUserName( user ).observeForever( new Observer < List < GameEntity > >() {
                @Override
                public void onChanged( @Nullable final List < GameEntity > games ) {
                    gamesAdapter.setGames( games );
                }
            } );
        }
    }
}
