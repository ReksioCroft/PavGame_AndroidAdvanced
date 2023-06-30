package ro.tav.pavgame.domain;

import android.content.Context;

import dagger.BindsInstance;
import dagger.Component;
import ro.tav.pavgame.presentation.PavGameViewModel;

@Component(modules = PavGameDependencyProviderModule.class)
public interface PavGameDependencyProviderComponent {
    void inject( PavGameViewModel pavGameViewModel );

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder context( Context context);

        Builder pavGameDependencyProviderModule(PavGameDependencyProviderModule module);

        PavGameDependencyProviderComponent build();
    }
}
