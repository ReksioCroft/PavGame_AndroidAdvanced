package ro.tav.pavgame.domain;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.remote.RemoteDataSource;
import ro.tav.pavgame.data.remote.RetrofitApi;
import timber.log.Timber;

public class GameRemoteRepository extends RemoteDataSource {
    public GameRemoteRepository() {
        super( RetrofitApi.createApi() );
    }

    public List < GameEntity > getAllGames() {
        try {
            return api.getAllGames().execute().body();
        } catch ( IOException e ) {
            Timber.tag( TAG ).w( e, "Something went wrong" );
            return null;
        }
    }

//    public GameEntity  getGame() {
//        try {
//            return api.getGame().execute().body();
//        } catch ( IOException e ) {
//            Timber.tag( TAG ).w( e, "Something went wrong" );
//            return null;
//        }
//    }

    //    public void insertGame( @Field( "gameId" ) String gameId,
//                            @Field( "gameType" ) String gameType,
//                            @Field( "numeJucator" ) String numeJucator,
//                            @Field( "result" ) String result ) {
//        Call < List < GameEntity > > call = api.insertGame( gameId, gameType, numeJucator, result );
    public void insertGame( GameEntity gameEntity ) {
        List<GameEntity> l = Arrays.asList( gameEntity );
        Call <  List < GameEntity  > > call = api.insertGame( gameEntity );
        call.enqueue( new Callback <  List<GameEntity > >() {
            @Override
            public void onResponse( @NotNull Call <List < GameEntity >> call, @NotNull Response <List < GameEntity  > > response ) {
                Timber.d( "Success inserting game in firebase db" );
            }

            @Override
            public void onFailure( @NotNull Call < List < GameEntity > > call, @NotNull Throwable t ) {
                Timber.d( "fail inserting game in firebase db" );
            }
        } );
    }
}
