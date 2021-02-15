package ro.tav.pavgame.presentation;

import android.app.Application;

import java.sql.Timestamp;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameUseCase;

public class PavGameViewModel extends GameUseCase {
    private static final String defaultUserName = "Current User";
    private static String userName = defaultUserName;

    public PavGameViewModel( Application application ) {
        super( application );
    }

    public static void setUserName( String name ) {
        if ( name == null )
            name = defaultUserName;
        userName = name;
    }

    public static String getUserName() {
        return userName;
    }

    public void addResult( String userName, boolean result, int gametype ) {
        //construim jocul si apoi il trimitem in bindingAdapter pt a fi adaugat
        String timeStamp = new Timestamp( System.currentTimeMillis() ).toString();
        GameEntity mGame = new GameEntity( userName, gametype, result, timeStamp );
        insertGame( mGame );
    }
}
