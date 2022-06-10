package ro.tav.pavgame.data.source;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameRemoteRepository;
import timber.log.Timber;

public class RemoteDataSource extends GameRemoteRepository {
    private final RetrofitApi api;

    public RemoteDataSource() {
        super();
        api = RetrofitApi.createApi();
    }

    @Override
    protected List< GameEntity > getAllGames() {
        List< GameEntity > gameEntities = null;
        Gson gson = new Gson();
        try {
            JsonObject response = api.getAllGames().execute().body();
            if ( response != null ) {
                gameEntities = new ArrayList<>();
                for ( String jsonObjectKeys : response.keySet() ) {
                    JsonObject jsonObject = response.getAsJsonObject( jsonObjectKeys );
                    GameEntity gameEntity = gson.fromJson( jsonObject, GameEntity.class );
                    if ( gameEntity != null )
                        gameEntities.add( gameEntity );
                }
            }
        } catch ( Exception e ) {
            Timber.d( e, "Something happened" );
        }
        return gameEntities;
    }

    @Override
    protected void insertGame( GameEntity gameEntity ) {
        Call< GameEntity > call = api.insertGame( gameEntity );
        call.enqueue( new Callback< GameEntity >() {
            @Override
            public void onResponse( @NotNull Call< GameEntity > call, @NotNull Response< GameEntity > response ) {
                Timber.d( "Success inserting game in firebase db" );
            }

            @Override
            public void onFailure( @NotNull Call< GameEntity > call, @NotNull Throwable t ) {
                Timber.d( "fail inserting game in firebase db" );
                InMemoryDataSource inMemoryDataSource = new InMemoryDataSource();
                inMemoryDataSource.addInMemory( gameEntity );
            }
        } );
    }

    private interface RetrofitApi {
        String BASE_URL = "https://pav-game-tav.firebaseio.com/";

        @GET( "games.json" )
        Call< JsonObject > getAllGames();

        @POST( "games.json" )
        Call< GameEntity > insertGame( @Body GameEntity gameEntity );

        static RetrofitApi createApi() {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor( new StethoInterceptor() )
                    .build();

            return new Retrofit.Builder()
                    .baseUrl( BASE_URL )
                    .client( okHttpClient )
                    .addConverterFactory( GsonConverterFactory.create() )
                    .build()
                    .create( RetrofitApi.class );
        }
    }
}
