package ro.tav.pavgame.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity( tableName = "gameEntity", primaryKeys = { "gameId", "numeJucator" } )
public final class GameEntity extends PavGamePojo {
    @ColumnInfo( name = "numeJucator" )
    @SerializedName( "numeJucator" )
    @Expose
    @NonNull
    private final String numeJucator;

    @ColumnInfo( name = "gameType" )
    @SerializedName( "gameType" )
    @Expose
    private final int gameType;

    @ColumnInfo( name = "result" )
    @SerializedName( "result" )
    @Expose
    private final boolean result;

    @ColumnInfo( name = "gameDateTime" )
    @SerializedName( "gameDateTime" )
    @Expose
    @Nullable
    private final String gameDateTime;

    public GameEntity( @NotNull String gameId, @NotNull String numeJucator, int gameType, boolean result, @Nullable String gameDateTime ) {
        super( gameId );
        this.numeJucator = numeJucator;
        this.gameType = gameType;
        this.result = result;
        this.gameDateTime = gameDateTime;
    }

    public @NotNull String getNumeJucator() {
        return numeJucator;
    }

    public boolean getResult() {
        return result;
    }

    public int getGameType() {
        return gameType;
    }

    @Nullable
    public String getGameDateTime() {
        return gameDateTime;
    }

    @Override
    public boolean equals( @Nullable Object obj ) {
        if ( obj != null && obj.getClass() == GameEntity.class ) {
            GameEntity gameToCompare = ( GameEntity ) obj;
            return gameId.equals( gameToCompare.gameId ) &&
                    gameType == gameToCompare.gameType &&
                    result == gameToCompare.result &&
                    numeJucator.equals( gameToCompare.numeJucator );
        } else
            return super.equals( obj );
    }
}
