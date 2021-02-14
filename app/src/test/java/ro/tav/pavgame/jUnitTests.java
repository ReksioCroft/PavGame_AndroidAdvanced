package ro.tav.pavgame;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.sql.Timestamp;
import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.inMemoryDB.InMemoryDatabase;
import ro.tav.pavgame.data.localDB.AppDatabase;
import ro.tav.pavgame.data.localDB.GameDao;
import ro.tav.pavgame.domain.GameRemoteRepository;
import ro.tav.pavgame.domain.GameUseCase;
import timber.log.Timber;

@RunWith( RobolectricTestRunner.class )
public class jUnitTests {
    private Context context;
    private GameEntity mGame;
    private final String numeJucator = "myJUnitTest";


    @Before
    public void createGameEntity() {
        String timeStamp = new Timestamp( System.currentTimeMillis() ).toString();
        mGame = new GameEntity( numeJucator, 4096, true, timeStamp );
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void test1() {
        GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
        gameRemoteRepository.insertGame( mGame );
        try {
            Thread.sleep( 1000 );
        } catch ( Exception e ) {
            Timber.d( e );
        }
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

        List < GameEntity > l = gameDao.getSpecificGamesbyUserNameStatic( numeJucator );

        try {
            assert !l.isEmpty();
        } catch ( Exception e ) {
            assert false;
        }
        db.close();
    }

    @Test
    public void test3() {
        GameUseCase gameUseCase = new GameUseCase( ApplicationProvider.getApplicationContext() );
        gameUseCase.insertGame( mGame );
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase();
        GameEntity gameFromRemoteRepository = inMemoryDatabase.removeInMemory();
        assert gameFromRemoteRepository == mGame;
    }
}
