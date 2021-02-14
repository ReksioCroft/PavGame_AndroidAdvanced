package ro.tav.pavgame.domain;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.data.inMemoryDB.InMemoryDataSource;

public class GameInMemoryRepository extends InMemoryDataSource {

    public GameInMemoryRepository() {
        super();
    }

    @Override
    protected void addInMemory( GameEntity gameEntity ) {
        inMemoryDatabase.addInMemery( gameEntity );
    }

    @Override
    protected GameEntity removeInMemory() {
        return inMemoryDatabase.removeInMemory();
    }

    @Override
    protected int getNrOfElements() {
        return inMemoryDatabase.getNrOfElements();
    }
}
