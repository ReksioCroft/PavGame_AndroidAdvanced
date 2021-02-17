package ro.tav.pavgame.domain;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

public abstract class GameRemoteRepository {
    protected GameRemoteRepository() {
        //empty constructor for modifying access
    }

    protected abstract List < GameEntity > getAllGames();

    protected abstract void insertGame( GameEntity gameEntity );
}
