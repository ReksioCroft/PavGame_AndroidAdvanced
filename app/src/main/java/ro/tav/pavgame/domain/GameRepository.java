package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.AppDatabase;
import ro.tav.pavgame.data.GameDataSource;
import ro.tav.pavgame.data.GameEntity;

public class GameRepository extends GameDataSource {
    public GameRepository( Application application ) {
        super( AppDatabase.getAppDatabase( application ).gameDao() );
    }

    protected LiveData < List < GameEntity > > getAllGames() {
        return mGameDao.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGames( String user ) {
        return mGameDao.getSpecificGames( user );
    }

    protected void insertGame( GameEntity game ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            mGameDao.insertGame( game );
        } );
    }
}
