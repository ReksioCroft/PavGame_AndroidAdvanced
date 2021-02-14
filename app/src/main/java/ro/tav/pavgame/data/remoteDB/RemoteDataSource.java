package ro.tav.pavgame.data.remoteDB;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public abstract class RemoteDataSource {
    protected static final String TAG = "remote-source";
    protected final RetrofitApi api;

    protected RemoteDataSource( RetrofitApi api ) {
        this.api = api;
    }

    protected abstract List < GameEntity > getAllGames();

    protected abstract void insertGame( GameEntity gameEntity );
}
