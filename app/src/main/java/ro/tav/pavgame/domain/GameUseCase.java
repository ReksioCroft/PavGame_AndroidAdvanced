package ro.tav.pavgame.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import ro.tav.pavgame.data.model.GameEntity;

// Mediator may implement more than one UseCase in mvvm with clean architecture
// so we cannot make the use-cases abstract classes, only interfaces.
// Even though these are public methods, they can be accessed only through
// the use-cases, as the mediator is not a public class and cannot be imported
// outside this package
public interface GameUseCase {
    @Nullable
    LiveData< List< GameEntity > > getAllGames();

    @Nullable
    LiveData< List< GameEntity > > getSpecificGamesbyUserName( @NonNull String user );

    void insertGame( @NonNull GameEntity game );
}
