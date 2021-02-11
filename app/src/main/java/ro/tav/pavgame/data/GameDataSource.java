package ro.tav.pavgame.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.domain.GameRepository;

public class GameDataSource implements GameRepository {
    private final GameDao mGameDao;

    public GameDataSource( Application application ) {
        AppDatabase db = AppDatabase.getAppDatabase( application );
        mGameDao = db.gameDao();
    }

    public LiveData < List < GameHistory > > getAllGames() {
        return mGameDao.getAllGames();
    }

    public LiveData < List < GameHistory > > getSpecificGames( String user ) {
        return mGameDao.getSpecificGames( user );
    }

    public void insertGame( GameHistory gameHistory ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            mGameDao.insertGame( gameHistory );
        } );
    }
}
