package ro.tav.pavgame.domain;

import androidx.annotation.Nullable;

import java.util.List;

import ro.tav.pavgame.data.model.PavGamePojo;

public abstract class GameRemoteRepository {
    public static final String firebaseRealtimeDatabaseUrl = "https://pav-game-tav.firebaseio.com/";

    protected GameRemoteRepository() {
        super();
    }

    protected abstract @Nullable List< PavGamePojo > getObjects( Class< ? extends PavGamePojo > clazz );

    protected abstract void insertPojo( PavGamePojo pojo );

    protected abstract void updatePojo( PavGamePojo pojo );
}
