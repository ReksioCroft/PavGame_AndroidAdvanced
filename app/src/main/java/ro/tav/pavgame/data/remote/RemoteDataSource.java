package ro.tav.pavgame.data.remote;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.tav.pavgame.data.GameHistory;
import timber.log.Timber;

public class RemoteDataSource  {
    private static final String TAG = "remote-source";
    private final FirebaseApi api;

    public RemoteDataSource( FirebaseApi api ) {
        this.api = api;
    }

    public  List < GameHistory >  getAllGames() {
        try {
            return api.getAllGames().execute().body();
        } catch ( IOException e ) {
            Timber.tag( TAG ).w( e, "Something went wrong" );
            List < GameHistory > ceva;
            return null;
        }
    }

    public void insertGame( GameHistory game ) {
        Call < GameHistory > call = api.insertGame( game );
        call.enqueue( new Callback < GameHistory >() {
            @Override
            public void onResponse( @NotNull Call < GameHistory > call, @NotNull Response < GameHistory > response ) {
                Timber.d( "Success" );
            }

            @Override
            public void onFailure( @NotNull Call < GameHistory > call, @NotNull Throwable t ) {
                Timber.d( "fail" );
            }
        } );
    }
}
