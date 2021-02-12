package ro.tav.pavgame.domain;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;

import java.sql.Timestamp;
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
                    if ( game != gameInDatabase ) {                                     //daca a aparut conflict de unique id
                        GameEntity newGame = new GameEntity();                          //cand se preia un joc din firebase cu un id deja folosit local
                        newGame.setNumeJucator( game.getNumeJucator() );                //luam din bd local jocul cu care apare conflictul
                        newGame.setGameType( game.getGameType() );                      //verificam sa nu fie acelasi joc
                        newGame.setResult( game.getResult() );                          //iar daca nu e, ne creeam o alta variabila,
                        newGame.setGameId( new Timestamp( System.currentTimeMillis() ).toString() );
                        mGameDao.insertGame( newGame );                                 //careia i se va atribui alt id
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
