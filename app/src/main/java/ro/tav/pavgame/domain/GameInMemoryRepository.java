package ro.tav.pavgame.domain;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.inMemoryDB.InMemoryDatabase;

public abstract class GameInMemoryRepository {
    protected final InMemoryDatabase inMemoryDatabase;

    protected GameInMemoryRepository( InMemoryDatabase inMemoryDatabase ) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    protected abstract void addInMemory( GameEntity gameEntity );

    protected abstract GameEntity removeInMemory();

    protected abstract int getNrOfElements();
}
