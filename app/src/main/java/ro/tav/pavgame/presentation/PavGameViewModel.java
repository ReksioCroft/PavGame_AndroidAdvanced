package ro.tav.pavgame.presentation;

import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Timestamp;
import java.util.List;

import androidx.annotation.Nullable;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameUseCase;
import ro.tav.pavgame.domain.PavGameDependencyProvider;
import ro.tav.pavgame.presentation.activity.LoginActivity;

public class PavGameViewModel extends AndroidViewModel {
    private static FirebaseUser firebaseUser = null;
    private static GoogleSignInAccount googleSignInAccount = null;
    private final GameUseCase gameUseCase;

    public PavGameViewModel( Application application ) {//AndroidViewModel extinde ViewModel si ne permite sa avem acest prototip in constructor
        super( application );
        PavGameDependencyProvider pavGameDependencyProvider = new PavGameDependencyProvider( application );
        this.gameUseCase = pavGameDependencyProvider.provideUseCase();      //comunicarea cu domain si data
    }

    public static void setFirebaseUser( FirebaseUser newFirebaseUser ) {
        firebaseUser = newFirebaseUser;
    }

    public static void setGoogleSignInAccount( GoogleSignInAccount newGoogleSignInAccount ) {
        googleSignInAccount = newGoogleSignInAccount;
    }

    public static @Nullable
    String getUserName() throws NullPointerException {
        if ( googleSignInAccount != null )
            return googleSignInAccount.getEmail();
        else if ( firebaseUser != null )
            return firebaseUser.getEmail();
        else {
            // open login activity
            Intent intent = new Intent( PavGameApplication.getApplication(), LoginActivity.class );
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            PavGameApplication.getApplication().startActivity( intent );
            return null;
        }
    }

    public void addResult( String userName, boolean result, int gametype ) {
        //construim jocul si apoi il trimitem in bindingAdapter pt a fi adaugat
        String timeStamp = new Timestamp( System.currentTimeMillis() ).toString();  //id-ul va fi timpul la care s-a facut adaugarea
        GameEntity mGame = new GameEntity( userName, gametype, result, timeStamp );
        gameUseCase.insertGame( mGame );
    }

    //Functii pentru Binding
    public LiveData< List< GameEntity > > getAllGames() {
        return gameUseCase.getAllGames();
    }

    public LiveData< List< GameEntity > > getSpecificGamesbyUserName( String user ) {
        return gameUseCase.getSpecificGamesbyUserName( user );
    }
}
