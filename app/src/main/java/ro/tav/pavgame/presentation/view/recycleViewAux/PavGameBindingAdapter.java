package ro.tav.pavgame.presentation.view.recycleViewAux;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.data.GameHistory;
import ro.tav.pavgame.domain.GameUseCase;
import ro.tav.pavgame.presentation.PavGameViewModel;

public class PavGameBindingAdapter {
    @BindingAdapter( { "game_useCase", "gameToInsert" } )
    public static void addGameResult( RecyclerView recyclerView, GameUseCase gameUseCase, GameHistory mGame ) {
        gameUseCase.insert( mGame );
    }

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
        if ( user == null )
            PavGameViewModel.getGameUseCase().getAllGames().observeForever( games -> gamesAdapter.setGames( games ) );
        else
            PavGameViewModel.getGameUseCase().getSpecificGames( user ).observeForever( games -> gamesAdapter.setGames( games ) );
    }


}
