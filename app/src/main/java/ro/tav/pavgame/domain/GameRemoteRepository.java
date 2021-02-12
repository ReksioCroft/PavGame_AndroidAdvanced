package ro.tav.pavgame.domain;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
            List < JsonObject > response = api.getAllGames().execute().body();
            List < GameEntity > gameEntities = new ArrayList <>();
            for ( JsonObject jsonObjectofObjects : response ) {
                for ( String jsonObjectKeys : jsonObjectofObjects.keySet() ) {
                    GameEntity gameEntity = new GameEntity();
                    JsonObject jsonObject = jsonObjectofObjects.getAsJsonObject( jsonObjectKeys );
                    gameEntity.setGameId( jsonObject.get( "gameId" ).getAsString() );
                    gameEntity.setGameType( jsonObject.get( "gameType" ).getAsString() );
                    gameEntity.setResult( jsonObject.get( "result" ).getAsString() );
                    gameEntity.setNumeJucator( jsonObject.get( "numeJucator" ).getAsString() );
                    gameEntities.add( gameEntity );
                }
            }
            return gameEntities;
        } catch ( Exception e ) {
            Timber.tag( TAG ).w( e, "Something went wrong" );
            return new ArrayList <>();
        }
    }


    public void insertGame( GameEntity gameEntity ) {
        try {
            int id;
            try {
                id = api.getAllGames().execute().body().size();
            } catch ( Exception e ) {
                id = 0;
            }
            Call < GameEntity > call = api.insertGame( String.valueOf( id ), gameEntity );
            call.enqueue( new Callback < GameEntity >() {
                @Override
                public void onResponse( @NotNull Call < GameEntity > call, @NotNull Response < GameEntity > response ) {
                    Timber.d( "Success inserting game in firebase db" );
                }

                @Override
                public void onFailure( @NotNull Call < GameEntity > call, @NotNull Throwable t ) {
                    Timber.d( "fail inserting game in firebase db" );
                }
            } );
        } catch ( Exception e ) {
            Timber.d( e );
        }
    }
}
