package ro.tav.pavgame.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameHistory;

public interface GameRepository {
    LiveData < List < GameHistory > > getAllGames();
    void insertGame( GameHistory gameHistory );
    public LiveData < List < GameHistory > > getSpecificGames( String user );
}
