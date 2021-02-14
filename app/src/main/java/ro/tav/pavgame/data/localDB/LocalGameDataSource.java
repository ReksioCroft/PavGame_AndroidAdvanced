package ro.tav.pavgame.data.localDB;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public abstract class LocalGameDataSource {
    protected final GameDao mGameDao;

    protected LocalGameDataSource( GameDao gameDao ) {
        mGameDao = gameDao;
    }

    protected abstract LiveData < List < GameEntity > > getAllGames();

    protected abstract LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user );

    protected abstract void insertGame( GameEntity game );
}
