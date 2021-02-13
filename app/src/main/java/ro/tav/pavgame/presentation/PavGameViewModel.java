package ro.tav.pavgame.presentation;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameUseCase;

public interface PavGameViewModel {
    GameUseCase gameUseCase = new GameUseCase( PavGameApplication.getApplication() );///gameUseCase face legatura cu repo-ul din domain

    static GameUseCase getGameUseCase() {  ///pentru a putea accesa repoul din domain
        return gameUseCase;
    }

    static void addResult( String userName, String result, String gametype ) {
        //construim jocul si apoi il trimitem in bindingAdapter pt a fi adaugat
        GameEntity mGame = new GameEntity();
        mGame.setNumeJucator( userName );
        mGame.setResult( result );
        mGame.setGameType( gametype );
        mGame.setGameId( "" );//va fi setat in mediator pt a evita ca ce se descarca de pe firebase sa fie uploadat inca o data
        gameUseCase.insertGame( mGame );
    }
}
