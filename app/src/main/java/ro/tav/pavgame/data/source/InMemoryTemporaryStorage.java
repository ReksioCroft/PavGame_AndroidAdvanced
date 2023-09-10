package ro.tav.pavgame.data.source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import ro.tav.pavgame.data.model.PavGamePojo;

public class InMemoryTemporaryStorage {
    private static final ConcurrentHashMap< Class< ? extends PavGamePojo >, InMemoryTemporaryStorage > instances = new ConcurrentHashMap<>();

    private final ConcurrentLinkedQueue< PavGamePojo > q = new ConcurrentLinkedQueue<>();
    private static int nrOfElements = 0;


    public static InMemoryTemporaryStorage getInstance( Class< ? extends PavGamePojo > type ) {
        synchronized ( instances ) {
            InMemoryTemporaryStorage instance = instances.get( type );
            if ( instance == null ) {
                instance = new InMemoryTemporaryStorage();
                instances.put( type, instance );
            }
            return instance;
        }
    }

    private InMemoryTemporaryStorage() {
        super();
    }

    void addInMemory( PavGamePojo obj ) {
        synchronized ( q ) {
            q.add( obj );
            nrOfElements++;
        }
    }

    PavGamePojo removeInMemory() {
        synchronized ( q ) {
            if ( nrOfElements > 0 ) {
                PavGamePojo obj = q.remove();
                nrOfElements--;          //daca mu s-a aruncat exceptie, inseamna ca putem scadea nr de elemente
                return obj;
            } else {
                return null;
            }
        }
    }

    int getNrOfElements() {
        synchronized ( q ) {
            return nrOfElements;
        }
    }
}
