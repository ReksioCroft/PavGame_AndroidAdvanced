package ro.tav.pavgame.data.source;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ro.tav.pavgame.data.model.GameEntity;
import ro.tav.pavgame.domain.GameLocalRepository;
import timber.log.Timber;


public class LocalGameDataSource extends GameLocalRepository {
    protected final GameDao mGameDao;

    public LocalGameDataSource( Context context ) {
        super();
        mGameDao = AppDatabase.getAppDatabase( context ).gameDao();
    }

    @Override
    protected LiveData< List< GameEntity > > getAllGames() {
        return mGameDao.getAllGames();
    }

    @Override
    protected void deleteAllGames() {
        mGameDao.deleteAllGames();
    }

    @Override
    protected LiveData< List< GameEntity > > getSpecificGamesbyUserName( String user ) {
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
        @Query( "SELECT numeJucator as gameId, numeJucator, gameType, result, cast (count(*) as text) as gameDateTime" +
                " from gameEntity " +
                " group by numeJucator, gameType, result" +
                " order by count(*) desc, gameType desc, result desc, numeJucator" )
        LiveData< List< GameEntity > > getAllGames();

        @Query( "SELECT * " +
                "from gameEntity " +
                "WHERE numeJucator=:user " +
                "order by gameDateTime desc" )
        LiveData< List< GameEntity > > getSpecificGamesbyUserName( String user );

        @Insert( onConflict = OnConflictStrategy.IGNORE )
        void insertGame( GameEntity game );

        @Query( "DELETE FROM gameEntity" )
        void deleteAllGames();
    }
}
