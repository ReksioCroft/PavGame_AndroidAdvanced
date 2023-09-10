package ro.tav.pavgame.domain;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.sql.Timestamp;
import java.util.List;

import ro.tav.pavgame.data.model.GameEntity;
import ro.tav.pavgame.data.model.PavGamePojo;
import timber.log.Timber;

@RunWith( RobolectricTestRunner.class )
public class JUnitTestsDomain {
    private GameEntity mGame;
    private final PavGameDependencyProviderModule pavGameDependencyProviderModule =
            new PavGameDependencyProviderModule( ApplicationProvider.getApplicationContext() );


    @Before
    public void createGameEntity() {
        String timeStamp = new Timestamp( System.currentTimeMillis() ).toString();
        mGame = new GameEntity( "myJUnitTest", "myJUnitTest", 4096, true, timeStamp );
    }

    @Test
    public void test1() {
        GameRemoteRepository gameRemoteRepository = pavGameDependencyProviderModule.provideRemoteRepository();
        gameRemoteRepository.insertPojo( mGame );
        try {
            Thread.sleep( 2000 );
        } catch ( Exception e ) {
            Timber.d( e );
        }
        List< PavGamePojo > l = gameRemoteRepository.getObjects( GameEntity.class );
        boolean ok = false;
        assert l != null;
        for ( PavGamePojo gameEntity : l ) {
            if ( gameEntity.equals( mGame ) ) {
                ok = true;
                break;
            }
        }
        assert ok;
    }

    @Test
    public void test2() {
        GameUseCase gameUseCase = pavGameDependencyProviderModule.provideUseCase();
        gameUseCase.insertGame( mGame );
        GameInMemoryRepository gameInMemoryRepository = pavGameDependencyProviderModule.provideInMemoryRepository();
        PavGamePojo gameFromRemoteRepository = gameInMemoryRepository.removeInMemory( GameEntity.class );
        assert gameFromRemoteRepository == mGame;
    }
}
