package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
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
            Timber.d( "GET Operation" );
            GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();
            List < GameEntity > games = gameRemoteRepository.getAllGames();

            GameLocalRepository gameLocalRepository = new GameLocalRepository( context );
            for ( GameEntity game : games ) {
                gameLocalRepository.insertGame( game );
            }
            Timber.d( "worker finished downloading games" );


        } else if ( "sync".equals( value ) ) {
            Timber.d( "SYNC Operation" );
            GameInMemoryRepository gameInMemoryRepository = new GameInMemoryRepository();
            GameRemoteRepository gameRemoteRepository = new GameRemoteRepository();

            final int nrOfSyncs = gameInMemoryRepository.getNrOfElements();
            for ( int i = 0; i < nrOfSyncs; i++ ) {         ///pt a nu face ciclu infinit; ex:nu merge netul=> worst case n insert failed
                gameRemoteRepository.insertGame( gameInMemoryRepository.removeInMemory() );
            }
        }

        return Result.success();
    }
}
