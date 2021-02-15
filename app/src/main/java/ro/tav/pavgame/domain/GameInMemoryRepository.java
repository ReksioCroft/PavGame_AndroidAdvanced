package ro.tav.pavgame.domain;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.inMemoryDB.InMemoryDataSource;

public class GameInMemoryRepository implements InMemoryDataSource {
    @Override
    public void addInMemory( GameEntity gameEntity ) {
        inMemoryDatabase.addInMemery( gameEntity );
    }

    @Override
    public GameEntity removeInMemory() {
        return inMemoryDatabase.removeInMemory();
    }

    @Override
    public int getNrOfElements() {
        return inMemoryDatabase.getNrOfElements();
    }
}
