package ro.tav.pavgame;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.localDB.AppDatabase;
import ro.tav.pavgame.data.localDB.GameDao;
import ro.tav.pavgame.domain.GameRemoteRepository;

@RunWith( RobolectricTestRunner.class )
public class jUnitTests {

    Context context;
    GameEntity mGame;

    @Before
    public void createDb() {
        mGame = new GameEntity();
        mGame.setNumeJucator( "myTest" );
        mGame.setResult( "Win" );
        mGame.setGameType( "Game Type: 4096x4096" );
        mGame.setGameId( "" );
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void test1() {
        GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
        gameRemoteRepository.insertGame( mGame );
        List < GameEntity > l = gameRemoteRepository.getAllGames();
        boolean ok = false;
        for ( GameEntity gameEntity : l ) {
            if ( gameEntity.equals( mGame ) ) {
                ok = true;
                break;
            }
        }
        assert ok;
    }

    @Test
    public void test2() {
        AppDatabase db = Room.inMemoryDatabaseBuilder( context, AppDatabase.class ).allowMainThreadQueries().build();
        GameDao gameDao = db.gameDao();
        gameDao.insertGame( mGame );

        List < GameEntity > l = gameDao.getSpecificGamesbyUserNameStatic( "myTest" );

        try {
            assert !l.isEmpty();
        } catch ( Exception e ) {
            assert false;
        }
        db.close();
    }
}
