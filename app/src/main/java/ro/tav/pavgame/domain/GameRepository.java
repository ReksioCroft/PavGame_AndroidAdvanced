package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.AppDatabase;
import ro.tav.pavgame.data.GameDao;
import ro.tav.pavgame.data.GameHistory;

public class GameRepository {
    private final GameDao mGameDao;
    private final LiveData < List < GameHistory > > mAllGames;

    GameRepository( Application application ) {
        AppDatabase db = AppDatabase.getAppDatabase( application );
        mGameDao = db.gameDao();
        mAllGames = mGameDao.getAllGames();
    }

    LiveData < List < GameHistory > > getAllGames() {
        return mAllGames;
    }

    void insert( GameHistory gameHistory ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            mGameDao.insertGame( gameHistory );
        } );
    }

}
