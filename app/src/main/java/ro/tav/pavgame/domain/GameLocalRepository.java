package ro.tav.pavgame.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.model.GameEntity;

public abstract class GameLocalRepository {
    protected GameLocalRepository() {
        super();
    }

    protected abstract LiveData< List< GameEntity > > getAllGames();

    protected abstract LiveData< List< GameEntity > > getSpecificGamesbyUserName( String user );

    protected abstract void insertGame( GameEntity game );

    protected abstract void deleteAllGames();
}
