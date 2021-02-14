package ro.tav.pavgame.data.inMemoryDB;

import java.util.LinkedList;

import ro.tav.pavgame.data.GameEntity;

public class InMemoryDatabase {
    private static final LinkedList < GameEntity > q = new LinkedList <>();
    private static int nrOfElements = 0;

    public void addInMemery( GameEntity gameEntity ) {
        if ( q.isEmpty() || !gameEntity.equals( q.element() ) ) {
            q.add( gameEntity );
            nrOfElements++;
        }
    }

    public GameEntity removeInMemory() {
        GameEntity gameEntity = q.remove();
        nrOfElements--;
        return gameEntity;
    }

    public int getNrOfElements() {
        return nrOfElements;
    }
}
