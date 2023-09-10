package ro.tav.pavgame.data.source;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ro.tav.pavgame.data.model.GameEntity;

@Database( entities = { GameEntity.class }, version = 1 )
abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    protected static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool( NUMBER_OF_THREADS );

    protected AppDatabase() {
        super();
    }

    protected abstract LocalGameDataSource.GameDao gameDao();

    protected static AppDatabase getAppDatabase( final Context context ) {
        synchronized ( AppDatabase.class ) {
            if ( INSTANCE == null ) {
                INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                                AppDatabase.class,
                                "game-db" )
                        .build();
            }
            return INSTANCE;
        }
    }
}
