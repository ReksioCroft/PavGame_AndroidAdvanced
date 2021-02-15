package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public class GameUseCase extends ViewModel {
    private final GameMediator mGameMediator;

    protected GameUseCase( Application application ) {
        super();
        mGameMediator = new GameMediator( application );
    }

    public LiveData < List < GameEntity > > getAllGames() {
        return mGameMediator.getAllGames();
    }

    public LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mGameMediator.getSpecificGamesbyUserName( user );
    }

    public void insertGame( GameEntity game ) {
        mGameMediator.insertGame( game );
    }
}
