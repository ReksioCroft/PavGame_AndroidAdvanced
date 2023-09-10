package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;

import ro.tav.pavgame.data.model.GameEntity;
import ro.tav.pavgame.data.model.PavGamePojo;
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
        if ( value == null ) return Result.failure();

        GameLocalRepository localRepository = new LocalGameDataSource( context );
        GameInMemoryRepository inMemoryRepository = new InMemoryDataSource();
        GameRemoteRepository remoteRepository = new RemoteDataSource( ( LocalGameDataSource ) localRepository, ( InMemoryDataSource ) inMemoryRepository );

        if ( "get".equals( value ) ) {
            Timber.d( "GET Operation" );

            boolean ok = false;

            do {
                List< PavGamePojo > games = remoteRepository.getObjects( GameEntity.class );
                if ( games == null )
                    break;
                localRepository.deleteAllGames();
                for ( PavGamePojo game : games ) {
                    localRepository.insertGame( ( GameEntity ) game );
                }
                ok = true;
            } while ( false );

            if ( !ok ) {
                Timber.d( "null response received from remote datasource" );
                return Result.retry();
            }
        } else if ( "post".equals( value ) || "put".equals( value ) ) {
            Timber.d( "POST Operation" );

            List< Class< ? extends PavGamePojo > > classTypes = new ArrayList<>();
            classTypes.add( GameEntity.class );
            for ( Class< ? extends PavGamePojo > classType : classTypes ) {
                int nrOfElements = inMemoryRepository.getNrOfElements( classType );
                for ( int i = 0; i < nrOfElements; i++ ) {
                    PavGamePojo obj = inMemoryRepository.removeInMemory( classType );
                    if ( obj != null ) {
                        if ( obj.getGameId().isEmpty() )
                            remoteRepository.insertPojo( obj );
                        else
                            remoteRepository.updatePojo( obj );
                    } else {
                        break;
                    }
                }
            }
            if ( inMemoryRepository.getNrOfElements( GameEntity.class ) != 0 )
                return Result.retry();
        }

        return Result.success();
    }
}
