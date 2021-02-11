package ro.tav.pavgame.domain;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.List;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.data.GameEntity;

public class GameMediator {
    private final Context context = PavGameApplication.getContext();
    private final GameLocalRepository mRepository;

    public GameMediator( Application application ) {
        mRepository = new GameLocalRepository( application );
    }

    protected LiveData < List < GameEntity > > getAllGames() {
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(GameWorker.class)
                        .build();
        WorkManager.getInstance(context).enqueue( uploadWorkRequest );
        return mRepository.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGames( String user ) {
        return mRepository.getSpecificGames( user );
    }

    protected void insertGame( GameEntity game ) {
        mRepository.insertGame( game );
    }
}
