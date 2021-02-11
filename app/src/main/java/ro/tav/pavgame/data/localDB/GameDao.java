package ro.tav.pavgame.data.localDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

@Dao
public interface GameDao {
    @Query( "SELECT DISTINCT numeJucator, gameType, result, count(*) as gameId from gameEntity  group by numeJucator, gameType, result order by gameId desc, numeJucator, gameType desc, result desc" )
    LiveData < List < GameEntity > > getAllGames();

    @Query( "SELECT DISTINCT numeJucator, gameType, result, count(*) as gameId from gameEntity  group by numeJucator, gameType, result having numeJucator=:user order by gameType desc, result desc" )
    LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user );

    @Query( "SELECT * from gameEntity where gameId=:gameId" )
    GameEntity getGameById( int gameId );

    @Insert( onConflict = OnConflictStrategy.ABORT )
    void insertGame( GameEntity game );
}
