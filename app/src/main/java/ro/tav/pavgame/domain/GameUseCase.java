package ro.tav.pavgame.domain;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.GameHistory;

public class GameUseCase extends AndroidViewModel {
    private final GameMediator mRepository;

    public GameUseCase( Application application ) {
        super( application );
        mRepository = new GameMediator( application );
    }

    public LiveData < List < GameHistory > > getAllGames() {
        return mRepository.getAllGames();
    }

    public LiveData < List < GameHistory > > getSpecificGames( String user ) {
        return mRepository.getSpecificGames( user );
    }

    public void insert( GameHistory gamehistory ) {
        mRepository.insert( gamehistory );
    }
}
