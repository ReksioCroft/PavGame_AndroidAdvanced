package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import ro.tav.pavgame.data.GameHistory;
import ro.tav.pavgame.data.remote.FirebaseApi;
import ro.tav.pavgame.data.remote.RemoteDataSource;
import ro.tav.pavgame.presentation.PavGameViewModel;


public class GameWorker extends Worker {
    private Context context;
    private GameHistory gameHistory;

    public GameWorker( @NonNull Context context,
                       @NonNull WorkerParameters workerParams, GameHistory gameHistory ) {
        super( context, workerParams );
        this.context = context;
        this.gameHistory = gameHistory;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String value = data.getString( "type" );

        if ( "get".equals( value ) ) {
            RemoteDataSource remoteDataSource = new RemoteDataSource( FirebaseApi.createApi() );
            List < GameHistory > gameHistories = remoteDataSource.getAllGames();
            for ( GameHistory gameHistory : gameHistories ) {
                PavGameViewModel.getGameUseCase().insertGame( gameHistory );
            }

        } else if ( "post".equals( value ) ) {
            // POST operation
            RemoteDataSource remoteDataSource = new RemoteDataSource( FirebaseApi.createApi() );
            remoteDataSource.insertGame( gameHistory );
        }

        return Result.success();
    }
}

