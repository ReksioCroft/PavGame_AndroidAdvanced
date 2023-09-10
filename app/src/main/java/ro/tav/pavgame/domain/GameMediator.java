package ro.tav.pavgame.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ro.tav.pavgame.data.model.GameEntity;
import timber.log.Timber;

final class GameMediator implements GameUseCase {
    private final GameLocalRepository localRepository;
    private final GameInMemoryRepository inMemoryRepository;

    private final WorkManager workManager;
    private final Data getDataforBuilder = ( new Data.Builder().putString( "mode", "get" ) ).build();
    private final Data postDataforBuilder = ( new Data.Builder().putString( "mode", "post" ) ).build();

    GameMediator( GameLocalRepository localRepository, GameInMemoryRepository inMemoryRepository, WorkManager workManager ) {
        this.localRepository = localRepository;
        this.inMemoryRepository = inMemoryRepository;
        this.workManager = workManager;

        //Vrem ca primul get sa se faca instant cand se deschide aplicatia
        getFromRemoteRepository();

        //De asemenea, vrem sa facem get pe parcurs
        setPeriodicRequests();
    }

    @Override
    @Nullable
    public LiveData< List< GameEntity > > getAllGames() {
        return localRepository.getAllGames();
    }

    @Override
    @Nullable
    public LiveData< List< GameEntity > > getSpecificGamesbyUserName( String user ) {
        return localRepository.getSpecificGamesbyUserName( user );
    }

    @Override
    public void insertGame( @NonNull GameEntity game ) {
        //adaugam jocul in firebase db
        postToRemoteRepository( game );
    }

    private void setPeriodicRequests() {
        //In caz ca vreun joc nu ajunge in Firebase, vrem sa il sincronizam
        //fara a fi necesar sa se deschida RecucleViewActivity sau sa se castige alt joc
        try {
            PeriodicWorkRequest postWorkRequest =
                    new PeriodicWorkRequest.Builder( GameWorker.class,
                            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS )
                            .setInputData( postDataforBuilder )
                            .build();
            workManager.enqueueUniquePeriodicWork( "postInMemoryGames",
                    ExistingPeriodicWorkPolicy.KEEP, postWorkRequest );
            // keep - va fi creat o singura data si va rula la fiecare 15min, alte instantieri fiind ignorate
            // ex: cand se acceseaza RecylceViewActivity sau se insereaza un joc
        } catch ( Exception e ) {
            Timber.d( e );
        }
    }

    private void getFromRemoteRepository() {
        //este apelat de cate ori se acceseaza RecycleView activity
        try {
            OneTimeWorkRequest downloadWorkRequest =
                    new OneTimeWorkRequest.Builder( GameWorker.class )
                            .setInputData( getDataforBuilder )
                            .build();
            workManager.enqueue( downloadWorkRequest );
        } catch ( Exception e ) {
            Timber.d( e );
        }
    }

    private void postToRemoteRepository( GameEntity game ) {
        //inseram jocul in coada repo-ului local
        inMemoryRepository.addInMemory( game );

        //dupa ce am inserat acest joc, il vom trimite (pe el si ce mai e in coada) in repo-ul firebase, prin worker
        try {
            OneTimeWorkRequest postWorkRequest =
                    new OneTimeWorkRequest.Builder( GameWorker.class )
                            .setInputData( postDataforBuilder )
                            .build();
            workManager.enqueue( postWorkRequest );
        } catch ( Exception e ) {
            Timber.d( e );
        }
    }
}
