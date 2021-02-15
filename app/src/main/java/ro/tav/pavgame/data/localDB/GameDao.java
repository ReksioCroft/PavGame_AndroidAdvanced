package ro.tav.pavgame.data.localDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ro.tav.pavgame.data.GameEntity;

//LocalDB Data Source
//Dao = Data Acces Object
@Dao
public interface GameDao {
    @Query( "SELECT DISTINCT numeJucator, gameType, result, cast (count(*) as text) as gameId from gameEntity  group by numeJucator, gameType, result order by count(*) desc, numeJucator, gameType desc, result desc" )
    LiveData < List < GameEntity > > getAllGames();

    @Query( "SELECT DISTINCT numeJucator, gameType, result, cast ( count(*) as text) as gameId from gameEntity  group by numeJucator, gameType, result having numeJucator=:user order by gameType desc, result desc" )
    LiveData < List < GameEntity > > getSpecificGamesbyUserName( String user );

    @Query( "SELECT DISTINCT numeJucator, gameType, result, cast ( count(*) as text) as gameId from gameEntity  group by numeJucator, gameType, result having numeJucator=:user order by gameType desc, result desc" )
    List < GameEntity > getSpecificGamesbyUserNameStatic( String user );

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertGame( GameEntity game );
}
