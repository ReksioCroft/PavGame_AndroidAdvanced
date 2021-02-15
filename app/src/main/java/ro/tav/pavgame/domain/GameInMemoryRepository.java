package ro.tav.pavgame.domain;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.inMemoryDB.InMemoryDataSource;

public class GameInMemoryRepository extends InMemoryDataSource {

    public GameInMemoryRepository() {
        super();
    }

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
