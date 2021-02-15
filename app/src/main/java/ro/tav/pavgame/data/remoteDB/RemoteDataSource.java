package ro.tav.pavgame.data.remoteDB;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public interface RemoteDataSource {
    RetrofitApi api = RetrofitApi.createApi();

    List < GameEntity > getAllGames();

    void insertGame( GameEntity gameEntity );
}
