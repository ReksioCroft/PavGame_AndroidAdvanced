package ro.tav.pavgame.room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class gameViewModel extends AndroidViewModel {
    private final GameRepository mRepository;
    private final LiveData < List < GameHistory > > mAllGames;

    public gameViewModel( Application application ) {
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
