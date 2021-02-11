package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public class GameMediator {
    private final GameLocalRepository mRepository;

    public GameMediator( Application application ) {
        mRepository = new GameLocalRepository( application );
    }

    protected LiveData < List < GameEntity > > getAllGames() {
        return mRepository.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGames( String user ) {
        return mRepository.getSpecificGames( user );
    }

    protected void insertGame( GameEntity game ) {
        mRepository.insertGame( game );
    }
}
