package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameDataSource;
import ro.tav.pavgame.data.GameHistory;

public class GameMediator extends AndroidViewModel {
    private final GameDataSource mRepository;

    public GameMediator( Application application ) {
        super( application );
        mRepository = new GameDataSource( application );
    }

    public LiveData < List < GameHistory > > getAllGames() {
        return mRepository.getAllGames();
    }

    public LiveData < List < GameHistory > > getSpecificGames( String user ) {
        return mRepository.getSpecificGames( user );
    }

    public void insertGame( GameHistory gamehistory ) {
        mRepository.insertGame( gamehistory );
    }
}
