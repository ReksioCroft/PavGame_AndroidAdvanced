package ro.tav.pavgame.data.source;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.sql.Timestamp;
import java.util.List;

import ro.tav.pavgame.data.model.GameEntity;

@RunWith( RobolectricTestRunner.class )
public class JUnitTestsData {
    private Context context;
    private GameEntity mGame;
    private final String numeJucator = "myJUnitTest";
    AppDatabase db = null;


    @Before
    public void createGameEntity() {
        String timeStamp = new Timestamp( System.currentTimeMillis() ).toString();
        mGame = new GameEntity( numeJucator, numeJucator, 4096, true, timeStamp );
        context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder( context, AppDatabase.class ).allowMainThreadQueries().build();
    }

    @Test
    public void test1() {
        LocalGameDataSource.GameDao gameDao = db.gameDao();
        gameDao.insertGame( mGame );

        LiveData< List< GameEntity > > listLiveData = gameDao.getSpecificGamesbyUserName( numeJucator );
        assert listLiveData != null;
        listLiveData.observeForever( new Observer< List< GameEntity > >() {
            @Override
            public void onChanged( List< GameEntity > gameEntities ) {
                if ( gameEntities == null )
                    return;
                assert !gameEntities.isEmpty();
            }
        } );

    }

    @After
    public void cleanup() {
        db.close();
    }
}
