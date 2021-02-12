package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;
import java.util.Objects;

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
        String value = data.getString( "mode" );

        if ( "get".equals( value ) ) {
            GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
            List < GameEntity > games = gameRemoteRepository.getAllGames();
            if ( games != null ) {
                for ( GameEntity game : games ) {
                    PavGameViewModel.getGameUseCase().insertGame( game );
                }
                Timber.d( "worker finished downloading games" );
            }
//            GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
//            GameEntity game = gameRemoteRepository.getGame();
//            Timber.v( game.getNumeJucator() );
//            PavGameViewModel.getGameUseCase().insertGame( game );
        } else if ( "post".equals( value ) ) {
//         POST operation
            GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
            GameEntity game = new GameEntity();
            game.setResult( Objects.requireNonNull( data.getString( "result" ) ) );
            game.setGameType( Objects.requireNonNull( data.getString( "gameType" ) ) );
            game.setGameId( Objects.requireNonNull( data.getString( "gameId" ) ) );//nu stim cu ce id se va introduce in bd locala si nici nu avem nevoie
            game.setNumeJucator( Objects.requireNonNull( data.getString( "numeJucator" ) ) );
            gameRemoteRepository.insertGame( game );
//            gameRemoteRepository.insertGame( data.getString( "gameId" ), data.getString( "gameType" ) ,
//                    data.getString( "numeJucator" ), data.getString( "result" ) );

            Timber.d( "Worker terminat cu succes pt upload firebase db" );
        }

        return Result.success();
    }
}

