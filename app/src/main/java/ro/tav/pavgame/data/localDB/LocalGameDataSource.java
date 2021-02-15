package ro.tav.pavgame.data.localDB;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameLocalRepository;
import timber.log.Timber;


public class LocalGameDataSource extends GameLocalRepository {
    protected final LocalGameDataSource.GameDao mGameDao;

    public LocalGameDataSource( Context context ) {
        super();
        mGameDao = AppDatabase.getAppDatabase( context ).gameDao();
    }

    @Override
    protected LiveData < List < GameEntity > > getAllGames() {
        return mGameDao.getAllGames();
    }

    @Override
    protected LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mGameDao.getSpecificGamesbyUserName( user );
    }

    @Override
    protected void insertGame( GameEntity game ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                mGameDao.insertGame( game );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }

    @Dao
    protected interface GameDao {
        @Query( "SELECT DISTINCT numeJucator, gameType, result, cast (count(*) as text) as gameId from gameEntity  group by numeJucator, gameType, result order by count(*) desc, numeJucator, gameType desc, result desc" )
        LiveData < List < GameEntity > > getAllGames();

        @Query( "SELECT DISTINCT numeJucator, gameType, result, cast ( count(*) as text) as gameId from gameEntity  group by numeJucator, gameType, result having numeJucator=:user order by gameType desc, result desc" )
        LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user );

        @Query( "SELECT DISTINCT numeJucator, gameType, result, cast ( count(*) as text) as gameId from gameEntity  group by numeJucator, gameType, result having numeJucator=:user order by gameType desc, result desc" )
        List < GameEntity > getSpecificGamesbyUserNameStatic( String user );

        @Insert( onConflict = OnConflictStrategy.IGNORE )
        void insertGame( GameEntity game );
    }
}
