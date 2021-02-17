package ro.tav.pavgame.data.source;

import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.domain.GameInMemoryRepository;

public class InMemoryDataSource extends GameInMemoryRepository {
    private final InMemoryTemporaryStorage inMemoryTemporaryStorage;

    public InMemoryDataSource() {
        super();
        inMemoryTemporaryStorage = new InMemoryTemporaryStorage();
    }

    @Override
    protected void addInMemory( GameEntity gameEntity ) {
        inMemoryTemporaryStorage.addInMemory( gameEntity );
    }

    @Override
    protected GameEntity removeInMemory() {
        return inMemoryTemporaryStorage.removeInMemory();
    }

    @Override
    protected int getNrOfElements() {
        return inMemoryTemporaryStorage.getNrOfElements();
    }
}
