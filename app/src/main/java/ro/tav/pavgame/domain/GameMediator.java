package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public class GameMediator {
    private final GameLocalRepository mRepository;

    public GameMediator( Application application ) {
        mRepository = new GameLocalRepository( application );
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder( GameWorker.class )
                        .build();
        WorkManager.getInstance( application.getApplicationContext() ).enqueue( uploadWorkRequest );
    }

    protected LiveData < List < GameEntity > > getAllGames() {
        return mRepository.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mRepository.getSpecificGamesbyUserName( user );
    }

    protected void insertGame( GameEntity game ) {
        mRepository.insertGame( game );
    }
}
