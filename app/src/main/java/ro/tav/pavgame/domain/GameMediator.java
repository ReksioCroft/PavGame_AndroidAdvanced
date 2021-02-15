package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.inMemoryDB.InMemoryDatabase;
import timber.log.Timber;

public class GameMediator {
    private final GameLocalRepository mRepository;
    private final Application mApplication;
    private final Data dataforBuilder = ( new Data.Builder().putString( "mode", "get" ) ).build();
    private final Data postDataforBuilder = ( new Data.Builder().putString( "mode", "post" ) ).build();

    protected GameMediator( Application application ) {
        this.mApplication = application;
        mRepository = new GameLocalRepository( application );

        //Vrem ca primul get sa se faca instant cand se deschide aplicatia
        getFromRemoteRepository();

        //De asemenea, vrem sa facem get pe parcurs
        setPeriodicRequests();
    }

    private void setPeriodicRequests() {
        try {
            PeriodicWorkRequest downloadWorkRequestLoop =
                    new PeriodicWorkRequest.Builder( GameWorker.class,
                            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS )
                            .setInputData( dataforBuilder )
                            .build();
            WorkManager.getInstance( mApplication )
                    .enqueueUniquePeriodicWork( "getFirebaseGames",
                            ExistingPeriodicWorkPolicy.KEEP, downloadWorkRequestLoop );
        } catch ( Exception e ) {
            Timber.d( e );
        }
        //In caz ca vreun joc nu ajunge in Firebase, vrem sa il sincronizam
        try {
            PeriodicWorkRequest postWorkRequest =
                    new PeriodicWorkRequest.Builder( GameWorker.class,
                            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS )
                            .setInputData( postDataforBuilder )
                            .build();
            WorkManager.getInstance( mApplication ).enqueueUniquePeriodicWork( "postInMemoryGames",
                    ExistingPeriodicWorkPolicy.KEEP, postWorkRequest );
        } catch ( Exception e ) {
            Timber.d( e );
        }
    }

    protected LiveData < List < GameEntity > > getAllGames() {
        return mRepository.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mRepository.getSpecificGamesbyUserName( user );
    }

    protected void insertGame( GameEntity game ) {
        //inseram jocul in coada repo-ului local
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase();
        inMemoryDatabase.addInMemery( game );

        //dupa ce am inserat acest joc, il vom trimite (pe el si ce mai e in coada) in repo-ul firebase
        try {
            OneTimeWorkRequest postWorkRequest =
                    new OneTimeWorkRequest.Builder( GameWorker.class )
                            .setInputData( postDataforBuilder )
                            .build();
            WorkManager.getInstance( mApplication ).enqueue( postWorkRequest );
        } catch ( Exception e ) {
            Timber.d( e );
        }

        mRepository.insertGame( game );
    }

    private void getFromRemoteRepository() {
        try {
            OneTimeWorkRequest downloadWorkRequest =
                    new OneTimeWorkRequest.Builder( GameWorker.class )
                            .setInputData( dataforBuilder )
                            .build();
            WorkManager.getInstance( mApplication ).enqueue( downloadWorkRequest );
        } catch ( Exception e ) {
            Timber.d( e );
        }
    }
}
