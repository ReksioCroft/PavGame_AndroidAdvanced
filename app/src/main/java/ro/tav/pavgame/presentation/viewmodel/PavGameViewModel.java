package ro.tav.pavgame.presentation.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

import ro.tav.pavgame.data.model.GameEntity;
import ro.tav.pavgame.domain.GameUseCase;
import ro.tav.pavgame.domain.PavGameDependencyProviderModule;
import ro.tav.pavgame.presentation.activity.LoginActivity;
import timber.log.Timber;

public class PavGameViewModel extends AndroidViewModel implements GameUseCase {
    private static FirebaseUser firebaseUser = null;
    private static GoogleSignInAccount googleSignInAccount = null;

    @Inject
    GameUseCase gameUseCase = null;


    public PavGameViewModel( Application application ) {//AndroidViewModel extinde ViewModel si ne permite sa avem acest prototip in constructor
        super( application );
        Timber.tag( "PavGameViewModel" ).d( "PavGameViewModel constructor called" );
    }

    public static void setFirebaseUser( FirebaseUser newFirebaseUser ) {
        firebaseUser = newFirebaseUser;
    }

    public static void setGoogleSignInAccount( GoogleSignInAccount newGoogleSignInAccount ) {
        googleSignInAccount = newGoogleSignInAccount;
    }

    public static @Nullable
    String getUserName( @Nullable Activity activity ) throws NullPointerException {
        if ( googleSignInAccount != null )
            return googleSignInAccount.getEmail();
        else if ( firebaseUser != null )
            return firebaseUser.getEmail();
        else if ( activity != null ) {
            // open login activity
            Intent intent = new Intent( activity, LoginActivity.class );
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            activity.startActivity( intent );
        }
        return null;
    }

    @Override
    public void insertGame( @NonNull GameEntity game ) {
        safeGameUseCase().insertGame( game );
    }

    @Override
    @Nullable
    public LiveData< List< GameEntity > > getAllGames() {
        return safeGameUseCase().getAllGames();
    }

    @Override
    @Nullable
    public LiveData< List< GameEntity > > getSpecificGamesbyUserName( @NonNull String user ) {
        return safeGameUseCase().getSpecificGamesbyUserName( user );
    }

    private GameUseCase safeGameUseCase() {
        if ( gameUseCase == null ) {
            Timber.e( "dagger injection failed, fallback to trivial dependency provider" );
            PavGameDependencyProviderModule pavGameDependencyProviderModule = new PavGameDependencyProviderModule( PavGameViewModel.this.getApplication() );
            gameUseCase = pavGameDependencyProviderModule.provideUseCase();
        } else {
            Timber.d( "dagger injection succeeded" );
        }
        return gameUseCase;
    }
}
