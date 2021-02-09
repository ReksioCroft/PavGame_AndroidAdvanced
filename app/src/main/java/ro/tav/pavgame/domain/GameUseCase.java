package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameHistory;

public class GameUseCase extends AndroidViewModel {
    private final GameMediator mRepository;
    private final LiveData < List < GameHistory > > mAllGames;

    public GameUseCase( Application application ) {
        super( application );
        mRepository = new GameMediator( application );
        mAllGames = mRepository.getAllGames();
    }

    public LiveData < List < GameHistory > > getAllGames() {
        return mAllGames;
    }

    public void insert( GameHistory gamehistory ) {
        mRepository.insert( gamehistory );
    }
}
