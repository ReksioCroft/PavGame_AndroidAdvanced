package ro.tav.pavgame.presentation.view.recycleViewAux;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.presentation.PavGameViewModel;


public class PavGameBindingAdapter {
    @BindingAdapter( "gamesAdapter" )
    public static void recycleViewSetAdapter( RecyclerView mRecyclerViewGames, GamesAdapter gamesAdapter ) {
        if ( mRecyclerViewGames.getAdapter() == null ) {
            // set the adapter to the recycler view
            mRecyclerViewGames.setAdapter( gamesAdapter );
            // define and set layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( mRecyclerViewGames.getContext() );
            mRecyclerViewGames.setLayoutManager( layoutManager );
        }
    }

    @BindingAdapter( { "pavGameViewModel", "user" } )
    public static void RecycleViewGamesBindingGames( RecyclerView mRecyclerViewGames, PavGameViewModel pavGameViewModel, @Nullable String user ) {
        //daca am primit un string null, facem bind cu toate jocurile din repo
        if ( user == null ) {
            pavGameViewModel.getAllGames().observeForever( new Observer < List < GameEntity > >() {
                @Override
                public void onChanged( @Nullable final List < GameEntity > games ) {
                    //suntem siguri ca adaptorul nostru este de tipul GameAdapter
                    GamesAdapter gamesAdapter = ( GamesAdapter ) mRecyclerViewGames.getAdapter();
                    gamesAdapter.setGames( games );
                }
            } );
        }  //altfel, afisam jocurile unui anumite utilizator
        else {
            pavGameViewModel.getSpecificGamesbyUserName( user ).observeForever( new Observer < List < GameEntity > >() {
                @Override
                public void onChanged( @Nullable final List < GameEntity > games ) {
                    GamesAdapter gamesAdapter = ( GamesAdapter ) mRecyclerViewGames.getAdapter();
                    gamesAdapter.setGames( games );
                }
            } );
        }
    }
}
