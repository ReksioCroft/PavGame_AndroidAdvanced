package ro.tav.pavgame.presentation;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.data.GameHistory;

public class PavGameBindingAdapter {
    @BindingAdapter( "pavGames" )
    public static void addGameResult( RecyclerView recyclerView, GameHistory mGame ) {
        PavGameViewModel.getGame().insert( mGame );
    }
}
