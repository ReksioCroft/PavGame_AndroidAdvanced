package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.AppDatabase;
import ro.tav.pavgame.data.GameDao;
import ro.tav.pavgame.data.GameHistory;

public class GameMediator {
    private final GameDao mGameDao;

    GameMediator( Application application ) {
        AppDatabase db = AppDatabase.getAppDatabase( application );
        mGameDao = db.gameDao();
    }

    LiveData < List < GameHistory > > getAllGames() {
        return mGameDao.getAllGames();
    }

    LiveData < List < GameHistory > > getSpecificGames( String user ) {
        return mGameDao.getSpecificGames( user );
    }

    void insert( GameHistory gameHistory ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            mGameDao.insertGame( gameHistory );
        } );
    }
}
