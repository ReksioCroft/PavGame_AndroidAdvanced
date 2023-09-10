package ro.tav.pavgame.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import ro.tav.pavgame.domain.PavGameDependencyProviderComponent;
import timber.log.Timber;

//Because our ViewModel has a parameter in the constructor, we have to use this class with ViewModelProvider
public class PavGameViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;

    public PavGameViewModelFactory( Application application ) {
        this.mApplication = application;
        Timber.tag( "PavGameViewModelFactory" ).d( "PavGameViewModelFactory constructor called" );
    }

    @NonNull
    @Override
    public < T extends ViewModel > T create( @NonNull Class< T > modelClass ) {
        PavGameViewModel pavGameViewModelInstance = new PavGameViewModel( this.mApplication );
        try {
            PavGameApplication pavGameApplication = ( PavGameApplication ) this.mApplication; // may throw class cast exception
            PavGameDependencyProviderComponent dependencyProviderComponent = pavGameApplication.getPavGameDependencyProviderComponent();
            if ( dependencyProviderComponent != null ) {
                dependencyProviderComponent.inject( pavGameViewModelInstance );
            } else {
                Timber.tag( "PavGameViewModelFactory" ).wtf( "dependencyProviderComponent is null" );
            }
        } catch ( Exception e ) {
            Timber.wtf( e );
        }
        return Objects.requireNonNull( modelClass.cast( pavGameViewModelInstance ) );
    }
}
