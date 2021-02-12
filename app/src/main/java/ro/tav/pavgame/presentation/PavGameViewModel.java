package ro.tav.pavgame.presentation;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameUseCase;
import ro.tav.pavgame.presentation.view.recycleViewAux.PavGameBindingAdapter;

public class PavGameViewModel extends ViewModel {
    private static final GameUseCase gameUseCase = new GameUseCase( PavGameApplication.getApplication() );///gameUseCase face legatura cu repo-ul din domain

    public static GameUseCase getGameUseCase() {  ///pentru a putea accesa repoul din domain
        return gameUseCase;
    }

    public static void addResult( RecyclerView recyclerView, String userName, String result, String gametype ) {
        //construim jocul si apoi il trimitem in bindingAdapter pt a fi adaugat
        GameEntity mGame = new GameEntity();
        mGame.setNumeJucator( userName );
        mGame.setResult( result );
        mGame.setGameType( gametype );
        mGame.setGameId( "" );//va fi setat in mediator pt a evita ca ce se descarca de pe firebase sa fie uploadat inca o data
        PavGameBindingAdapter.addGameResult( recyclerView, mGame );
    }


}
