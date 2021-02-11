package ro.tav.pavgame.presentation;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.data.GameHistory;
import ro.tav.pavgame.domain.GameUseCase;
import ro.tav.pavgame.presentation.view.recycleViewAux.PavGameBindingAdapter;

public class PavGameViewModel extends ViewModel {
    private static final GameUseCase gameUseCase = new GameUseCase( PavGameApplication.getApplication() );///gameUseCase face legatura cu repo-ul din domain

    public static GameUseCase getGameUseCase() {  ///pentru a putea accesa repoul din domain
        return gameUseCase;
    }

    public static void addResult( RecyclerView recyclerView, String userName, String result, String gametype ) {
        //construim jocul si apoi il trimitem in bindingAdapter pt a fi adaugat
        GameHistory mGame = new GameHistory();
        mGame.setNume( userName );
        mGame.setResult( result );
        mGame.setGameType( gametype );
        PavGameBindingAdapter.addGameResult( recyclerView, mGame );
    }


}
