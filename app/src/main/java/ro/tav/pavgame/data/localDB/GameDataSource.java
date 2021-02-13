package ro.tav.pavgame.data.localDB;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public abstract class GameDataSource {
    protected final GameDao mGameDao;

    public GameDataSource( GameDao gameDao ) {
        mGameDao = gameDao;
    }

    protected abstract LiveData < List < GameEntity > > getAllGames();

    protected abstract LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user );

    protected abstract List < GameEntity > getSpecificGamesbyUserNameStatic( String user );

    protected abstract void insertGame( GameEntity game );
}
