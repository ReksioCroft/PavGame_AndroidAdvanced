package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.localDB.AppDatabase;
import ro.tav.pavgame.data.localDB.GameDao;

//@RunWith( RobolectricTestRunner.class )

@RunWith( AndroidJUnit4.class )
public class jUnitTests {
    private GameDao gameDao;
    private AppDatabase db;
    Context context;
    GameEntity mGame;

    @Before
    public void createDb() {
        mGame = new GameEntity();
        mGame.setNumeJucator( "myTest" );
        mGame.setResult( "Win" );
        mGame.setGameType( "Game Type: 1024x1024" );
        mGame.setGameId( "" );
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void test1() {
        GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
        gameRemoteRepository.insertGame( mGame );
        List < GameEntity > l = gameRemoteRepository.getAllGames();
        Boolean ok = false;
        for ( GameEntity gameEntity : l ) {
            if ( gameEntity.equals( mGame ) ) {
                ok = true;
            }
        }
        assert ok;
    }

    @Test
    public void test2() {
        db = Room.inMemoryDatabaseBuilder( context, AppDatabase.class ).allowMainThreadQueries().build();
        gameDao = db.gameDao();
        gameDao.insertGame( mGame );

//        AppDatabase.databaseWriteExecutor.execute( new Runnable() {
//            @Override
//            public void run() {
//                gameDao.insertGame( mGame );
//            }
//        } );
//        PavGameApplication pavGameApplication = mock(PavGameApplication.class);
//        GameLocalRepository gameLocalRepository = new GameLocalRepository(context);

//        gameLocalRepository.insertGame( mGame );
//        List < GameEntity > l = gameLocalRepository.getAllGames().getValue();
//        try {
//            Boolean ok = false;
//            for ( GameEntity gameEntity : l ) {
//                if ( gameEntity.equals( mGame ) ) {
//                    ok = true;
//                }
//            }
//            db.close();
//            assert ok;
//        } catch ( Exception e ) {
//            assert false;
//        }
        assert gameDao.getSpecificGamesbyUserName( "myTest" ).getValue().isEmpty();
    }
}
