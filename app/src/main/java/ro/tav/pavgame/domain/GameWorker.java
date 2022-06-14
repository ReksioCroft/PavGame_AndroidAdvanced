package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.source.InMemoryDataSource;
import ro.tav.pavgame.data.source.LocalGameDataSource;
import ro.tav.pavgame.data.source.RemoteDataSource;
import timber.log.Timber;


public class GameWorker extends Worker {
    private final Context context;

    public GameWorker( @NonNull Context context, @NonNull WorkerParameters workerParams ) {
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
            GameRemoteRepository gameRemoteRepository = new RemoteDataSource();
            List< GameEntity > games = gameRemoteRepository.getAllGames();

            if ( games != null ) {
                GameLocalRepository gameLocalRepository = new LocalGameDataSource( context );
                gameLocalRepository.deleteAllGames();
                for ( GameEntity game : games ) {
                    gameLocalRepository.insertGame( game );
                }
                Timber.d( "worker finished downloading games" );
            } else {
                Timber.d( "null response received from remote datasource" );
                return Result.retry();
            }
        } else if ( "post".equals( value ) ) {
            Timber.d( "POST Operation" );
            GameInMemoryRepository gameInMemoryRepository = new InMemoryDataSource();
            GameRemoteRepository gameRemoteRepository = new RemoteDataSource();

            final int nrOfSyncs = gameInMemoryRepository.getNrOfElements();
            for ( int i = 0; i < nrOfSyncs; i++ ) {         ///pt a nu face ciclu infinit; ex:nu merge netul=> worst case n inserts failed
                gameRemoteRepository.insertGame( gameInMemoryRepository.removeInMemory() );
            }
            if ( gameInMemoryRepository.getNrOfElements() == nrOfSyncs ) {
                Timber.d( "worker failed posting games" );
                return Result.retry();
            } else {
                Timber.d( "worker finished posting games" );
            }
        }

        return Result.success();
    }
}
