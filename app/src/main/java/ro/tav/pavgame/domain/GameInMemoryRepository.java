package ro.tav.pavgame.domain;

import ro.tav.pavgame.data.GameEntity;

public abstract class GameInMemoryRepository {
    protected GameInMemoryRepository() {
        //empty constructor for modifying access
    }

    protected abstract void addInMemory( GameEntity gameEntity );

    protected abstract GameEntity removeInMemory();

    protected abstract int getNrOfElements();
}
