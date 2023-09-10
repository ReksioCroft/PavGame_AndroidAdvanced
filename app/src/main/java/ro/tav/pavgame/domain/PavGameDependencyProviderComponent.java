package ro.tav.pavgame.domain;

import android.app.Application;

import dagger.BindsInstance;
import dagger.Component;
import ro.tav.pavgame.presentation.viewmodel.PavGameViewModel;

@Component( modules = PavGameDependencyProviderModule.class )
public interface PavGameDependencyProviderComponent {
    void inject( PavGameViewModel pavGameViewModel );

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application( Application application );

        Builder pavGameDependencyProviderModule( PavGameDependencyProviderModule module );

        PavGameDependencyProviderComponent build();
    }
}
