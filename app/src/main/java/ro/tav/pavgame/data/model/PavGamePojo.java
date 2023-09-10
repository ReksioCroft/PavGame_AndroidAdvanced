package ro.tav.pavgame.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PavGamePojo implements Serializable {
    @ColumnInfo( name = "gameId" )
    @SerializedName( "gameId" )
    @Expose
    @NonNull
    protected String gameId;

    PavGamePojo( @NonNull String uid ) {
        this.gameId = uid;
    }

    @NonNull
    public String getGameId() {
        return gameId;
    }

    public void setGameId( @NonNull String gameId ) {
        this.gameId = gameId; // maybe you set it as the firbase hash
    }
}
