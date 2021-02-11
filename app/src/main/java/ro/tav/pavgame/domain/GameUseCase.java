package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameHistory;

public class GameUseCase extends AndroidViewModel {
    private final GameMediator mGameMediator;

    public GameUseCase( Application application ) {
        super( application );
        mGameMediator = new GameMediator( application );
    }

    public LiveData < List < GameHistory > > getAllGames() {
        return mGameMediator.getAllGames();
    }

    public LiveData < List < GameHistory > > getSpecificGames( String user ) {
        return mGameMediator.getSpecificGames( user );
    }

    public void insertGame( GameHistory gamehistory ) {
        mGameMediator.insertGame( gamehistory );
    }
}
