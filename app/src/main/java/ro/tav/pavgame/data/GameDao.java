package ro.tav.pavgame.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameDao {
    @Query( "SELECT DISTINCT numeJucator, gameType, result, count(*) as gameId from gameEntity  group by numeJucator, gameType, result order by gameId desc, numeJucator, gameType desc, result desc" )
    LiveData < List < GameEntity > > getAllGames();

    @Query( "SELECT DISTINCT numeJucator, gameType, result, count(*) as gameId from gameEntity  group by numeJucator, gameType, result having numeJucator=:user order by gameType desc, result desc" )
    LiveData < List < GameEntity > > getSpecificGames( String user );

    @Insert
    void insertGame( GameEntity game );
}
