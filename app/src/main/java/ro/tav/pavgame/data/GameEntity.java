package ro.tav.pavgame.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity( tableName = "gameEntity" )
public class GameEntity {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "gameId" )
    private int gameId;

    @ColumnInfo( name = "numeJucator" )
    @NonNull
    private String numeJucator;

    @ColumnInfo( name = "result" )
    @NonNull
    private String result;

    @ColumnInfo( name = "gameType" )
    @NonNull
    private String gameType;

    public int getGameId() {
        return gameId;
    }

    public @NotNull String getNumeJucator() {
        return numeJucator;
    }

    public @NotNull String getResult() {
        return result;
    }

    public @NotNull String getGameType() {
        return gameType;
    }

    public void setGameId( int gameId ) {
        this.gameId = gameId;
    }

    public void setNumeJucator( @NotNull String nume ) {
        this.numeJucator = nume;
    }

    public void setResult( @NotNull String result ) {
        this.result = result;
    }

    public void setGameType( @NotNull String gameType ) {
        this.gameType = gameType;
    }
}
