package ro.tav.pavgame.data.remote;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public abstract class RemoteDataSource {
    protected static final String TAG = "remote-source";
    protected final RetrofitApi api;

    public RemoteDataSource( RetrofitApi api ) {
        this.api = api;
    }

    protected abstract List < GameEntity > getAllGames();

    protected abstract GameEntity getGame();

    protected abstract void insertGame( GameEntity game );
}
