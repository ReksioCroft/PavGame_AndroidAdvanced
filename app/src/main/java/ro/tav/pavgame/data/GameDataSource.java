package ro.tav.pavgame.data;

import androidx.lifecycle.LiveData;

import java.util.List;

public abstract class GameDataSource {
    protected final GameDao mGameDao;

    public GameDataSource( GameDao gameDao ) {
        mGameDao = gameDao;
    }

    protected abstract LiveData < List < GameEntity > > getAllGames();

    protected abstract LiveData < List < GameEntity > > getSpecificGames( String user );

    protected abstract void insertGame( GameEntity game );

}
