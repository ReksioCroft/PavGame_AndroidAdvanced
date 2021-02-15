package ro.tav.pavgame.data.inMemoryDB;

import ro.tav.pavgame.data.GameEntity;

public interface InMemoryDataSource {
    InMemoryDatabase inMemoryDatabase = new InMemoryDatabase();

    void addInMemory( GameEntity gameEntity );

    GameEntity removeInMemory();

    int getNrOfElements();
}
