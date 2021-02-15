package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.localDB.AppDatabase;
import ro.tav.pavgame.data.localDB.LocalGameDataSource;
import timber.log.Timber;

public class GameLocalRepository extends LocalGameDataSource {
    public GameLocalRepository( Context context ) {
        super( AppDatabase.getAppDatabase( context ).gameDao() );
    }

    @Override
    public LiveData < List < GameEntity > > getAllGames() {
        return mGameDao.getAllGames();
    }

    @Override
    public LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mGameDao.getSpecificGamesbyUserName( user );
    }

    @Override
    public void insertGame( GameEntity game ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                mGameDao.insertGame( game );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }
}
