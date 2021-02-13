package ro.tav.pavgame.domain;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.localDB.AppDatabase;
import ro.tav.pavgame.data.localDB.GameDataSource;
import timber.log.Timber;

public class GameLocalRepository extends GameDataSource {
    public GameLocalRepository( Application application ) {
        super( AppDatabase.getAppDatabase( application ).gameDao() );
    }

    public GameLocalRepository( Context context ) {
        super( AppDatabase.getAppDatabase( context ).gameDao() );
    }

    protected LiveData < List < GameEntity > > getAllGames() {
        return mGameDao.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mGameDao.getSpecificGamesbyUserName( user );
    }

    protected List < GameEntity > getSpecificGamesbyUserNameStatic( String user ) {
        return mGameDao.getSpecificGamesbyUserNameStatic( user );
    }

    protected void insertGame( GameEntity game ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                mGameDao.insertGame( game );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }
}
