package ro.tav.pavgame.data.inMemoryDB;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameInMemoryRepository;

public class InMemoryDataSource extends GameInMemoryRepository {
    public InMemoryDataSource() {
        super( new InMemoryDatabase() );
    }

    @Override
    protected void addInMemory( GameEntity gameEntity ) {
        inMemoryDatabase.addInMemory( gameEntity );
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
