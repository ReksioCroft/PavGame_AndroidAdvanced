package ro.tav.pavgame.data.remote;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.tav.pavgame.data.GameEntity;
import timber.log.Timber;

public class RemoteDataSource {
    private static final String TAG = "remote-source";
    private final FirebaseApi api;

    public RemoteDataSource( FirebaseApi api ) {
        this.api = api;
    }

    public List < GameEntity > getAllGames() {
        try {
            return api.getAllGames().execute().body();
        } catch ( IOException e ) {
            Timber.tag( TAG ).w( e, "Something went wrong" );
            return null;
        }
    }

    public void insertGame( GameEntity game ) {
        Call < GameEntity > call = api.insertGame( game );
        call.enqueue( new Callback < GameEntity >() {
            @Override
            public void onResponse( @NotNull Call < GameEntity > call, @NotNull Response < GameEntity > response ) {
                Timber.d( "Success" );
            }

            @Override
            public void onFailure( @NotNull Call < GameEntity > call, @NotNull Throwable t ) {
                Timber.d( "fail" );
            }
        } );
    }
}
