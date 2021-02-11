package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.presentation.PavGameViewModel;
import timber.log.Timber;


public class GameWorker extends Worker {
    private Context context;

    public GameWorker( @NonNull Context context,
                       @NonNull WorkerParameters workerParams ) {
        super( context, workerParams );
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String value = data.getString( "type" );

//        if ( "get".equals( value ) ) {
//            GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
//            List < GameEntity > games = gameRemoteRepository.getAllGames();
//            for ( GameEntity game : games ) {
//                PavGameViewModel.getGameUseCase().insertGame( game );
//            }

//        } else if ( "post".equals( value ) ) {
//            // POST operation
//            GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
//            gameRemoteRepository.insertGame( game );
//        }
        GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
        GameEntity game = gameRemoteRepository.getGame();
        Timber.v( game.getNumeJucator() );
        PavGameViewModel.getGameUseCase().insertGame( game );
        return Result.success();
    }
}

