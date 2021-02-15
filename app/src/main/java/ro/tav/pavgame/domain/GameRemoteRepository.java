package ro.tav.pavgame.domain;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.remoteDB.RemoteDataSource;
import timber.log.Timber;

public class GameRemoteRepository implements RemoteDataSource {
    @Override
    public List < GameEntity > getAllGames() {
        List < GameEntity > gameEntities = new ArrayList <>();
        Gson gson = new Gson();
        try {
            JsonObject response = api.getAllGames().execute().body();
            if ( response != null ) {
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
    public void insertGame( GameEntity gameEntity ) {
        Call < GameEntity > call = api.insertGame( gameEntity );
        call.enqueue( new Callback < GameEntity >() {
            @Override
            public void onResponse( @NotNull Call < GameEntity > call, @NotNull Response < GameEntity > response ) {
                Timber.d( "Success inserting game in firebase db" );
            }

            @Override
            public void onFailure( @NotNull Call < GameEntity > call, @NotNull Throwable t ) {
                Timber.d( "fail inserting game in firebase db" );
                GameInMemoryRepository gameInMemoryRepository = new GameInMemoryRepository();
                gameInMemoryRepository.addInMemory( gameEntity );
            }
        } );
    }
}
