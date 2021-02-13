package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ro.tav.pavgame.data.GameEntity;

public class GameMediator {
    private final GameLocalRepository mRepository;
    private final Application mApplication;

    public GameMediator( Application application ) {
        this.mApplication = application;
        mRepository = new GameLocalRepository( application );

        Data.Builder builder = new Data.Builder();
        builder.putString( "mode", "get" );
        Data data = builder.build();

        //Vrem ca primul get sa se faca instant cand se deschide aplicatia
        OneTimeWorkRequest downloadWorkRequest =
                new OneTimeWorkRequest.Builder( GameWorker.class )
                        .setInputData( data )
                        .build();
        WorkManager.getInstance( mApplication ).enqueue( downloadWorkRequest );

        //De asemenea, vrem sa facem get pe parcurs
        PeriodicWorkRequest downloadWorkRequestLoop =
                new PeriodicWorkRequest.Builder( GameWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS )
                        .setInputData( data )
                        .build();
        WorkManager.getInstance( mApplication )
                .enqueueUniquePeriodicWork( "getFirebaseGames", ExistingPeriodicWorkPolicy.KEEP, downloadWorkRequestLoop );
    }

    protected LiveData < List < GameEntity > > getAllGames() {
        return mRepository.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mRepository.getSpecificGamesbyUserName( user );
    }

    protected void insertGame( GameEntity game ) {
        if ( game.getGameId().equals( "" ) ) {
            game.setGameId( new Timestamp( System.currentTimeMillis() ).toString() );
            Data.Builder builder = new Data.Builder();
            builder.putString( "mode", "post" );
            builder.putString( "numeJucator", game.getNumeJucator() );
            builder.putString( "gameType", game.getGameType() );
            builder.putString( "result", game.getResult() );
            builder.putString( "gameId", game.getGameId() );
            Data data = builder.build();
            OneTimeWorkRequest uploadWorkRequest =
                    new OneTimeWorkRequest.Builder( GameWorker.class )
                            .setInputData( data )
                            .build();
            WorkManager.getInstance( mApplication.getApplicationContext() ).enqueue( uploadWorkRequest );
        }
        mRepository.insertGame( game );
    }
}
