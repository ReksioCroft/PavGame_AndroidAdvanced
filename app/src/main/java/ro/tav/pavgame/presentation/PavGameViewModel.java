package ro.tav.pavgame.presentation;

import androidx.lifecycle.ViewModel;

import java.sql.Timestamp;

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

    public static void addResult( String userName, boolean result, int gametype ) {
        //construim jocul si apoi il trimitem in bindingAdapter pt a fi adaugat
        String timeStamp = new Timestamp( System.currentTimeMillis() ).toString();
        GameEntity mGame = new GameEntity( userName, gametype, result, timeStamp );
        gameUseCase.insertGame( mGame );
    }
}
