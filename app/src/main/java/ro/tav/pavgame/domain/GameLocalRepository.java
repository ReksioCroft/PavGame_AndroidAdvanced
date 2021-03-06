package ro.tav.pavgame.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public abstract class GameLocalRepository {
    protected GameLocalRepository() {
        //empty constructor for modifying access
    }

    protected abstract LiveData < List < GameEntity > > getAllGames();

    protected abstract LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user );

    protected abstract void insertGame( GameEntity game );
}
