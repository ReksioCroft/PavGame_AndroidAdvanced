package ro.tav.pavgame.presentation;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.sql.Timestamp;
import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameUseCase;
import ro.tav.pavgame.domain.PavGameDependencyProvider;

public class PavGameViewModel extends AndroidViewModel {
    private static final String defaultUserName = "Current User";
    private static String userName = defaultUserName;
    private final GameUseCase gameUseCase;

    public PavGameViewModel( Application application ) {//AndroidViewModel extinde ViewModel si ne permite sa avem acest prototip in constructor
        super( application );
        PavGameDependencyProvider pavGameDependencyProvider = new PavGameDependencyProvider( application );
        this.gameUseCase = pavGameDependencyProvider.provideUseCase();      //comunicarea cu domain si data
    }

    public static void setUserName( String name ) { //apelat din LoginActivity
        if ( name == null )
            name = defaultUserName;
        userName = name;
    }

    public static String getUserName() {
        return userName;
    }

    public void addResult( String userName, boolean result, int gametype ) {
        //construim jocul si apoi il trimitem in bindingAdapter pt a fi adaugat
        String timeStamp = new Timestamp( System.currentTimeMillis() ).toString();  //id-ul va fi timpul la care s-a facut adaugarea
        GameEntity mGame = new GameEntity( userName, gametype, result, timeStamp );
        gameUseCase.insertGame( mGame );
    }

    //Functii pentru Binding
    public LiveData < List < GameEntity > > getAllGames() {
        return gameUseCase.getAllGames();
    }

    public LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return gameUseCase.getSpecificGamesbyUserName( user );
    }
}
