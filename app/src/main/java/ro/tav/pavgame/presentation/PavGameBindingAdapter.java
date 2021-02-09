package ro.tav.pavgame.presentation;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.data.GameHistory;
import ro.tav.pavgame.domain.gameViewModel;

public class PavGameBindingAdapter {
    static private gameViewModel game;

    @BindingAdapter( "pavGames" )
    public static void addResul( RecyclerView recyclerView, GameHistory mGame ) {
        game.insert( mGame );
    }

    static public void setGame( gameViewModel newGame ) {
        game = newGame;
    }

    static public gameViewModel getGame() {
        return game;
    }
}
