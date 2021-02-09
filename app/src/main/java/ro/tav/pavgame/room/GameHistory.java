package ro.tav.pavgame.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "gameplay_history" )
public class GameHistory {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )
    private int gameId;

    @ColumnInfo( name = "nume_jucator" )
    private String nume;

    @ColumnInfo( name = "result" )
    private String result;

    @ColumnInfo( name = "game_type" )
    private String gameType;

    public int getGameId() {
        return gameId;
    }

    public String getNume() {
        return nume;
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

    public void setNume( String nume ) {
        this.nume = nume;
    }

    public void setResult( String result ) {
        this.result = result;
    }

    public void setGameType( String gameType ) {
        this.gameType = gameType;
    }
}
