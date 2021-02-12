package ro.tav.pavgame.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity( tableName = "gameEntity", primaryKeys = { "gameId", "numeJucator" } )
public class GameEntity {
    @ColumnInfo( name = "gameId" )
    @NonNull
    @Expose
    @SerializedName( "gameId" )
    private String gameId;

    @ColumnInfo( name = "numeJucator" )
    @NonNull
    @Expose
    @SerializedName( "numeJucator" )
    private String numeJucator;

    @ColumnInfo( name = "result" )
    @NonNull
    @Expose
    @SerializedName( "result" )
    private String result;

    @ColumnInfo( name = "gameType" )
    @NonNull
    @Expose
    @SerializedName( "gameType" )
    private String gameType;

    public @NotNull String getGameId() {
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

    public void setGameId( @NotNull String gameId ) {
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
