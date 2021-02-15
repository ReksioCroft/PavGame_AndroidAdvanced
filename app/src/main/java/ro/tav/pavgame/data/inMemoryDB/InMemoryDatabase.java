package ro.tav.pavgame.data.inMemoryDB;

import java.util.LinkedList;

import ro.tav.pavgame.data.GameEntity;

public class InMemoryDatabase {
    private static final LinkedList < GameEntity > q = new LinkedList <>();
    private static int nrOfElements = 0;

    protected InMemoryDatabase() {
        //empty constructor for making it protected
    }

    protected void addInMemory( GameEntity gameEntity ) {
        q.add( gameEntity );
        nrOfElements++;
    }

    protected GameEntity removeInMemory() {
        GameEntity gameEntity = q.remove();
        nrOfElements--;          //daca mu s-a aruncat exceptie, inseamna ca putem scadea nr de elemente
        return gameEntity;
    }

    protected int getNrOfElements() {
        return nrOfElements;
    }
}
