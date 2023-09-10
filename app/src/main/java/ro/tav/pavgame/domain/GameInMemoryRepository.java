package ro.tav.pavgame.domain;

import ro.tav.pavgame.data.model.PavGamePojo;

public abstract class GameInMemoryRepository {
    protected GameInMemoryRepository() {
        super();
    }

    protected abstract void addInMemory( PavGamePojo pojo );

    protected abstract PavGamePojo removeInMemory( Class< ? extends PavGamePojo > pojoClass );

    protected abstract int getNrOfElements( Class< ? extends PavGamePojo > pojoClass );
}
