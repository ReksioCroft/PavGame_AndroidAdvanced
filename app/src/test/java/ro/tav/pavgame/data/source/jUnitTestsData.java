package ro.tav.pavgame.data.source;

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

@RunWith( RobolectricTestRunner.class )
public class jUnitTestsData {
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
        AppDatabase db = Room.inMemoryDatabaseBuilder( context, AppDatabase.class ).allowMainThreadQueries().build();
        LocalGameDataSource.GameDao gameDao = db.gameDao();
        gameDao.insertGame( mGame );

        List < GameEntity > l = gameDao.getSpecificGamesbyUserNameStatic( numeJucator );

        db.close();

        try {
            assert !l.isEmpty();
        } catch ( Exception e ) {
            assert false;
        }
    }
}
