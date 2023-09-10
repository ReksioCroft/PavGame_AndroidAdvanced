package ro.tav.pavgame.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ro.tav.pavgame.data.model.GameEntity;
import ro.tav.pavgame.data.model.PavGamePojo;
import ro.tav.pavgame.domain.GameRemoteRepository;
import timber.log.Timber;

public class RemoteDataSource extends GameRemoteRepository {
    private final RetrofitApi api;
    private final Map< Class< ? extends PavGamePojo >, ApiHandlerGet > apiHandlersGet;
    private final LocalGameDataSource localDataSource;
    private final InMemoryDataSource inMemoryDataSource;

    private interface ApiHandlerGet {
        Call< JsonObject > call();
    }

    public RemoteDataSource( LocalGameDataSource localDataSource, InMemoryDataSource rescuePetsInMemoryRepository ) {
        super();
        api = RetrofitApi.createApi();
        this.localDataSource = localDataSource;
        this.inMemoryDataSource = rescuePetsInMemoryRepository;

        apiHandlersGet = new HashMap<>();
        apiHandlersGet.put( GameEntity.class, api::getAllGames );
    }

    @Override
    protected @Nullable List< PavGamePojo > getObjects( Class< ? extends PavGamePojo > clazz ) {
        ApiHandlerGet apiHandler = apiHandlersGet.get( clazz );

        if ( apiHandler == null ) {
            Timber.wtf( "no api handler found" );
            return null;
        }

        List< PavGamePojo > entities = null;
        Gson gson = new Gson();
        try {
            JsonObject response = apiHandler.call().execute().body();
            if ( response != null ) {
                entities = new ArrayList<>();
                for ( String jsonObjectKeys : response.keySet() ) {
                    JsonObject jsonObject = response.getAsJsonObject( jsonObjectKeys );
                    PavGamePojo pojoEntity = gson.fromJson( jsonObject, clazz );
                    if ( pojoEntity != null ) {
                        pojoEntity.setGameId( jsonObjectKeys );
                        entities.add( pojoEntity );
                    }
                }
            }
        } catch ( Exception e ) {
            Timber.d( e, "Something happened" );
        }
        return entities;
    }

    @Override
    protected void insertPojo( PavGamePojo pojo ) {
        Callback< JsonObject > callback = new Callback< JsonObject >() {
            @Override
            public void onResponse( @NonNull Call< JsonObject > call, @NonNull Response< JsonObject > response ) {
                if ( response.isSuccessful() ) {
                    Timber.d( "Success inserting pojo in firebase db" );
                    try {
                        JsonObject jsonObject = response.body();
                        if ( jsonObject != null ) {
                            JsonElement jsonElement = jsonObject.get( "name" );
                            if ( jsonElement != null ) {
                                String uid = jsonElement.getAsString();
                                if ( uid != null ) {
                                    pojo.setGameId( uid );
                                    if ( pojo instanceof GameEntity )
                                        localDataSource.insertGame( ( GameEntity ) pojo );
                                    else
                                        Timber.tag( "remoteDataSource" ).wtf( "unexpected class" );
                                }
                            }
                        }
                    } catch ( Exception e ) {
                        Timber.e( e );
                    }

                } else {
                    Timber.d( "Recieved firebase response is failed" );
                    inMemoryDataSource.addInMemory( pojo );
                }
            }

            @Override
            public void onFailure( @NonNull Call< JsonObject > call, @NonNull Throwable t ) {
                Timber.d( "fail inserting pojo in firebase db" );
                Timber.tag( "remoteDataSource" ).e( t );
                inMemoryDataSource.addInMemory( pojo );
            }
        };
        if ( pojo instanceof GameEntity )
            api.insertGame( ( GameEntity ) pojo ).enqueue( callback );
        else
            Timber.tag( "remoteDataSource" ).wtf( "unexpected class" );
    }

    @Override
    protected void updatePojo( PavGamePojo pojo ) {
        Callback< JsonObject > callback = new Callback< JsonObject >() {
            @Override
            public void onResponse( @NonNull Call< JsonObject > call, @NonNull Response< JsonObject > response ) {
                if ( response.isSuccessful() ) {
                    Timber.d( "Success updating pojo in firebase db" );
                    if ( pojo instanceof GameEntity )
                        localDataSource.insertGame( ( GameEntity ) pojo );
                    else
                        Timber.tag( "remoteDataSource" ).wtf( "unexpected class" );
                } else {
                    Timber.d( "Recieved firebase response is failed" );
                    inMemoryDataSource.addInMemory( pojo );
                }
            }

            @Override
            public void onFailure( @NonNull Call< JsonObject > call, @NonNull Throwable t ) {
                Timber.d( "fail inserting pojo in firebase db" );
                Timber.tag( "remoteDataSource" ).e( t );
                inMemoryDataSource.addInMemory( pojo );
            }
        };
        if ( pojo instanceof GameEntity )
            api.updateGame( pojo.getGameId(), ( GameEntity ) pojo ).enqueue( callback );
        else
            Timber.wtf( "unexpected class" );
    }

    private interface RetrofitApi {
        @GET( "games.json" )
        Call< JsonObject > getAllGames();

        @POST( "games.json" )
        Call< JsonObject > insertGame( @Body GameEntity gameEntity );

        @PUT( "games/{gameId}.json" )
        Call< JsonObject > updateGame( @Path( "gameId" ) String gameId, @Body GameEntity gameEntity );

        static RetrofitApi createApi() {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor( new StethoInterceptor() )
                    .build();

            return new Retrofit.Builder()
                    .baseUrl( firebaseRealtimeDatabaseUrl )
                    .client( okHttpClient )
                    .addConverterFactory( GsonConverterFactory.create() )
                    .build()
                    .create( RetrofitApi.class );
        }
    }
}
