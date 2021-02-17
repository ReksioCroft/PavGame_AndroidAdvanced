package ro.tav.pavgame.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public class GameUseCase {
    private final GameMediator mGameMediator;

    protected GameUseCase( GameMediator mediator ) {
        this.mGameMediator = mediator;
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
