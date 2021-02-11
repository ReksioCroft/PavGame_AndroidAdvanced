package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.remote.FirebaseApi;
import ro.tav.pavgame.data.remote.RemoteDataSource;
import ro.tav.pavgame.presentation.PavGameViewModel;


public class GameWorker extends Worker {
    private Context context;
    private GameEntity game;

    public GameWorker( @NonNull Context context,
                       @NonNull WorkerParameters workerParams, GameEntity game ) {
        super( context, workerParams );
        this.context = context;
        this.game = game;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String value = data.getString( "type" );

        if ( "get".equals( value ) ) {
            RemoteDataSource remoteDataSource = new RemoteDataSource( FirebaseApi.createApi() );
            List < GameEntity > games = remoteDataSource.getAllGames();
            for ( GameEntity game : games ) {
                PavGameViewModel.getGameUseCase().insertGame( game );
            }

        } else if ( "post".equals( value ) ) {
            // POST operation
            RemoteDataSource remoteDataSource = new RemoteDataSource( FirebaseApi.createApi() );
            remoteDataSource.insertGame( game );
        }

        return Result.success();
    }
}

