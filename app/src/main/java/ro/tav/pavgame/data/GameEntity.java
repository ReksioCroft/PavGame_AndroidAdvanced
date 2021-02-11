package ro.tav.pavgame.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "gameEntity" )
public class GameEntity {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "gameId" )
    private int gameId;

    @ColumnInfo( name = "numeJucator" )
    private String numeJucator;

    @ColumnInfo( name = "result" )
    private String result;

    @ColumnInfo( name = "gameType" )
    private String gameType;

    public int getGameId() {
        return gameId;
    }

    public String getNumeJucator() {
        return numeJucator;
    }

    public String getResult() {
        return result;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameId( int gameId ) {
        this.gameId = gameId;
    }

    public void setNumeJucator( String nume ) {
        this.numeJucator = nume;
    }

    public void setResult( String result ) {
        this.result = result;
    }

    public void setGameType( String gameType ) {
        this.gameType = gameType;
    }
}
