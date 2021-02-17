package ro.tav.pavgame.data.source;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ro.tav.pavgame.data.GameEntity;

@Database( entities = { GameEntity.class }, version = 1 )
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    protected static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool( NUMBER_OF_THREADS );

    protected AppDatabase() {
        //empty constructor for making class protected
    }

    protected abstract LocalGameDataSource.GameDao gameDao();

    protected static AppDatabase getAppDatabase( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized ( AppDatabase.class ) {
                INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                        AppDatabase.class,
                        "game-db" )
                        .build();
            }
        }
        return INSTANCE;
    }
}
