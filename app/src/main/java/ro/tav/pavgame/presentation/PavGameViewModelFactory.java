package ro.tav.pavgame.presentation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.domain.PavGameDependencyProviderComponent;

//Because our ViewModel has a parameter in the constructor, we have to use this class with ViewModelProvider
public class PavGameViewModelFactory implements ViewModelProvider.Factory {
    private final PavGameApplication application;

    public PavGameViewModelFactory( PavGameApplication application ) {
        this.application = application;
    }

    @NonNull
    @Override
    public < T extends ViewModel > T create( @NonNull Class< T > modelClass ) {
        PavGameDependencyProviderComponent myComponent = application.getPavGameDependencyProviderComponent();
        PavGameViewModel pavGameViewModelInstance = new PavGameViewModel( application );
        myComponent.inject(pavGameViewModelInstance);

        return Objects.requireNonNull( modelClass.cast(pavGameViewModelInstance ) );
    }
}
