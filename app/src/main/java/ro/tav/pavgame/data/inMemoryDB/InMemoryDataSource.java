package ro.tav.pavgame.data.inMemoryDB;

import ro.tav.pavgame.data.GameEntity;

public abstract class InMemoryDataSource {
    protected final InMemoryDatabase inMemoryDatabase;

    protected InMemoryDataSource() {
        inMemoryDatabase = new InMemoryDatabase();
    }

    protected abstract void addInMemory( GameEntity gameEntity );

    protected abstract GameEntity removeInMemory();

    protected abstract int getNrOfElements();
}
