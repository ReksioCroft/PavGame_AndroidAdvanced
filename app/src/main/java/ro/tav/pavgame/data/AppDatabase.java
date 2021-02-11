package ro.tav.pavgame.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database( entities = { GameEntity.class }, version = 1 )
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract GameDao gameDao();

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool( NUMBER_OF_THREADS );

    public static AppDatabase getAppDatabase( final Context context ) {
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
