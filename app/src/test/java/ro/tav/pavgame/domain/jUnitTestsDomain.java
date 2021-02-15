package ro.tav.pavgame.domain;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.sql.Timestamp;
import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.inMemoryDB.InMemoryDataSource;
import ro.tav.pavgame.data.remoteDB.RemoteDataSource;
import timber.log.Timber;

@RunWith( RobolectricTestRunner.class )
public class jUnitTestsDomain {
    private GameEntity mGame;

    @Before
    public void createGameEntity() {
        String timeStamp = new Timestamp( System.currentTimeMillis() ).toString();
        String numeJucator = "myJUnitTest";
        mGame = new GameEntity( numeJucator, 4096, true, timeStamp );
    }

    @Test
    public void test1() {
        GameRemoteRepository gameRemoteRepository = new RemoteDataSource();
        gameRemoteRepository.insertGame( mGame );
        try {
            Thread.sleep( 2000 );
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
        GameUseCase gameUseCase = new GameUseCase( ApplicationProvider.getApplicationContext() );
        gameUseCase.insertGame( mGame );
        GameInMemoryRepository gameInMemoryRepository = new InMemoryDataSource();
        GameEntity gameFromRemoteRepository = gameInMemoryRepository.removeInMemory();
        assert gameFromRemoteRepository == mGame;
    }
}
