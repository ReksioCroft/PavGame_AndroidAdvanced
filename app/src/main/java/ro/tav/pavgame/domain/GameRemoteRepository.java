package ro.tav.pavgame.domain;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.inMemoryDB.InMemoryDataSource;


public abstract class GameRemoteRepository {
    protected final GameInMemoryRepository gameInMemoryRepository;

    protected GameRemoteRepository() {
        gameInMemoryRepository = new InMemoryDataSource();
    }

    protected abstract List < GameEntity > getAllGames();

    protected abstract void insertGame( GameEntity gameEntity );

    protected void addInMemoryRepository( GameEntity gameEntity ) {
        gameInMemoryRepository.addInMemory( gameEntity );
    }
}
