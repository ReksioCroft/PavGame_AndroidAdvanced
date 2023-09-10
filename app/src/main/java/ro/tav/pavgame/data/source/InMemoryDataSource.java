package ro.tav.pavgame.data.source;

import ro.tav.pavgame.data.model.GameEntity;
import ro.tav.pavgame.data.model.PavGamePojo;
import ro.tav.pavgame.domain.GameInMemoryRepository;

public final class InMemoryDataSource extends GameInMemoryRepository {
    private final InMemoryTemporaryStorage gameInMemoryTemporaryStorage;


    public InMemoryDataSource() {
        super();
        gameInMemoryTemporaryStorage = InMemoryTemporaryStorage.getInstance( GameEntity.class );
    }

    @Override
    protected void addInMemory( PavGamePojo pojo ) {
        if ( pojo instanceof GameEntity )
            gameInMemoryTemporaryStorage.addInMemory( pojo );
        else
            throw new RuntimeException( "obj instance not known" );
    }

    @Override
    protected PavGamePojo removeInMemory( Class< ? extends PavGamePojo > pojoClass ) {
        if ( pojoClass.equals( GameEntity.class ) )
            return gameInMemoryTemporaryStorage.removeInMemory();
        else
            throw new RuntimeException( "obj instance not known" );
    }

    @Override
    protected int getNrOfElements( Class< ? extends PavGamePojo > pojoClass ) {
        if ( pojoClass.equals( GameEntity.class ) )
            return gameInMemoryTemporaryStorage.getNrOfElements();
        else
            throw new RuntimeException( "obj instance not known" );
    }
}
