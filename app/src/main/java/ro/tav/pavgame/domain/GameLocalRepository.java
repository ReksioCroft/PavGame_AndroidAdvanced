package ro.tav.pavgame.domain;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;

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

    protected LiveData < List < GameEntity > > getAllGames() {
        return mGameDao.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mGameDao.getSpecificGamesbyUserName( user );
    }

    protected void insertGame( GameEntity game ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                mGameDao.insertGame( game );
            } catch ( SQLiteConstraintException e ) {
                try {
                    GameEntity gameInDatabase = mGameDao.getGameById( game.getGameId() );
                    if ( game != gameInDatabase ) {
                        GameEntity newGame = new GameEntity();
                        newGame.setNumeJucator( game.getNumeJucator() );
                        newGame.setGameType( game.getGameType() );
                        newGame.setResult( game.getResult() );
                        mGameDao.insertGame( newGame );
                    }
                } catch ( Exception e1 ) {
                    Timber.e( e1 );
                }
            } catch ( Exception e2 ) {
                Timber.e( e2 );
            }
        } );
    }
}
