package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.sql.Timestamp;
import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public class GameMediator {
    private final GameLocalRepository mRepository;
    private final Application mApplication;

    public GameMediator( Application application ) {
        this.mApplication = application;
        mRepository = new GameLocalRepository( application );

        Data.Builder builder = new Data.Builder();
        builder.putString( "mode", "get" );
        Data data = builder.build();
        OneTimeWorkRequest downloadWorkRequest =
                new OneTimeWorkRequest.Builder( GameWorker.class )
                        .setInputData( data )
                        .build();
        WorkManager.getInstance( mApplication.getApplicationContext() ).enqueue( downloadWorkRequest );
    }

    protected LiveData < List < GameEntity > > getAllGames() {
        return mRepository.getAllGames();
    }

    protected LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user ) {
        return mRepository.getSpecificGamesbyUserName( user );
    }

    protected void insertGame( GameEntity game ) {
        if ( game.getGameId().equals( "" ) ) {
            game.setGameId( new Timestamp( System.currentTimeMillis() ).toString() );
            Data.Builder builder = new Data.Builder();
            builder.putString( "mode", "post" );
            builder.putString( "numeJucator", game.getNumeJucator() );
            builder.putString( "gameType", game.getGameType() );
            builder.putString( "result", game.getResult() );
            builder.putString( "gameId", game.getGameId() );
            Data data = builder.build();
            OneTimeWorkRequest uploadWorkRequest =
                    new OneTimeWorkRequest.Builder( GameWorker.class )
                            .setInputData( data )
                            .build();
            WorkManager.getInstance( mApplication.getApplicationContext() ).enqueue( uploadWorkRequest );
        }
        mRepository.insertGame( game );
    }
}
