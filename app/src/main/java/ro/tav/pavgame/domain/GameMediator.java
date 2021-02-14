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
import ro.tav.pavgame.data.inMemoryDB.InMemoryDatabase;

public class GameMediator {
    private final GameLocalRepository mRepository;
    private final Application mApplication;
    private final Data dataforBuilder = ( new Data.Builder().putString( "mode", "get" ) ).build();
    private final Data syncDataforBuilder = ( new Data.Builder().putString( "mode", "sync" ) ).build();

    public GameMediator( Application application ) {
        this.mApplication = application;
        mRepository = new GameLocalRepository( application );

        //Vrem ca primul get sa se faca instant cand se deschide aplicatia
        getFromRemoteRepository();

        //De asemenea, vrem sa facem get pe parcurs
        PeriodicWorkRequest downloadWorkRequestLoop =
                new PeriodicWorkRequest.Builder( GameWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS )
                        .setInputData( dataforBuilder )
                        .build();
        WorkManager.getInstance( mApplication )
                .enqueueUniquePeriodicWork( "getFirebaseGames", ExistingPeriodicWorkPolicy.KEEP, downloadWorkRequestLoop );

        //In caz ca vreun joc nu ajunge in Firebase, vrem sa il sincronizam
        PeriodicWorkRequest syncWorkRequest =
                new PeriodicWorkRequest.Builder( GameWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS )
                        .setInputData( syncDataforBuilder )
                        .build();
        WorkManager.getInstance( mApplication ).enqueueUniquePeriodicWork( "syncInMemoryGames", ExistingPeriodicWorkPolicy.KEEP, syncWorkRequest );
    }

    protected LiveData < List < GameEntity > > getAllGames() {
        return mRepository.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mRepository.getSpecificGamesbyUserName( user );
    }

    protected List < GameEntity > getSpecificGamesbyUserNameStatic( String user ) {
        return mRepository.getSpecificGamesbyUserNameStatic( user );
    }

    protected void insertGame( GameEntity game ) {
        if ( game.getGameId().equals( "" ) ) {
            //adaugam timpul la care s-a terminat jocul
            game.setGameId( new Timestamp( System.currentTimeMillis() ).toString() );

            //inseram jocul in coada repo-ului local
            InMemoryDatabase inMemoryDatabase = new InMemoryDatabase();
            inMemoryDatabase.addInMemery( game );

            //dupa ce am inserat acest joc, il vom trimite (pe el si ce mai e in coada) in repo-ul firebase
            OneTimeWorkRequest syncWorkRequest =
                    new OneTimeWorkRequest.Builder( GameWorker.class )
                            .setInputData( syncDataforBuilder )
                            .build();
            WorkManager.getInstance( mApplication.getApplicationContext() ).enqueue( syncWorkRequest );
        }
        mRepository.insertGame( game );
    }

    private void getFromRemoteRepository() {
        OneTimeWorkRequest downloadWorkRequest =
                new OneTimeWorkRequest.Builder( GameWorker.class )
                        .setInputData( dataforBuilder )
                        .build();
        WorkManager.getInstance( mApplication.getApplicationContext() ).enqueue( downloadWorkRequest );
    }
}
