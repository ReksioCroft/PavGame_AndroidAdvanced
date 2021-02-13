package ro.tav.pavgame.presentation;

import androidx.lifecycle.ViewModel;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameUseCase;

public class PavGameViewModel extends ViewModel {
    private static final String defaultUserName = "Current User";
    private static final GameUseCase gameUseCase = new GameUseCase( PavGameApplication.getApplication() );///gameUseCase face legatura cu repo-ul din domain
    private static String userName = defaultUserName;

    public static void setUserName( String name ) {
        if ( name == null )
            name = defaultUserName;
        userName = name;
    }

    public static String getUserName() {
        return userName;
    }

    public static GameUseCase getGameUseCase() {
        return gameUseCase;
    }

    public static void addResult( String userName, String result, String gametype ) {
        //construim jocul si apoi il trimitem in bindingAdapter pt a fi adaugat
        GameEntity mGame = new GameEntity();
        mGame.setNumeJucator( userName );
        mGame.setResult( result );
        mGame.setGameType( gametype );
        mGame.setGameId( "" );//va fi setat in mediator pt a evita ca ce se descarca de pe firebase sa fie uploadat inca o data
        gameUseCase.insertGame( mGame );
    }
}
