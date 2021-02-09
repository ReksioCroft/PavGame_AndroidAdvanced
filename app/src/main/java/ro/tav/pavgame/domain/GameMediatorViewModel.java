package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameHistory;

public class GameMediatorViewModel extends AndroidViewModel {
    private final GameRepository mRepository;
    private final LiveData < List < GameHistory > > mAllGames;

    public GameMediatorViewModel( Application application ) {
        super( application );
        mRepository = new GameRepository( application );
        mAllGames = mRepository.getAllGames();
    }

    public LiveData < List < GameHistory > > getAllGames() {
        return mAllGames;
    }

    public void insert( GameHistory gamehistory ) {
        mRepository.insert( gamehistory );
    }
}
