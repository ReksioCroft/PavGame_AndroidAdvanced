package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;

import dagger.Module;
import dagger.Provides;
import ro.tav.pavgame.data.source.InMemoryDataSource;
import ro.tav.pavgame.data.source.LocalGameDataSource;
import ro.tav.pavgame.data.source.RemoteDataSource;

//se afla in .domain, deci ne poate da obiecte din tipul acelor clase
@Module
public final class PavGameDependencyProviderModule {
    private final @NonNull GameInMemoryRepository inMemoryRepository;
    private final @NonNull GameRemoteRepository remoteRepository;
    private final @NonNull GameUseCase useCase;

    public PavGameDependencyProviderModule( Context context ) {
        GameLocalRepository localRepository = new LocalGameDataSource( context );
        inMemoryRepository = new InMemoryDataSource();
        remoteRepository = new RemoteDataSource( ( LocalGameDataSource ) localRepository, ( InMemoryDataSource ) inMemoryRepository );

        WorkManager workManager1;           //necesar pt jUnitTests
        try {
            workManager1 = WorkManager.getInstance( context );
        } catch ( Exception e ) {
            workManager1 = null;
        }
        WorkManager workManager = workManager1;

        useCase = new GameMediator( localRepository, inMemoryRepository, workManager );
    }

    @Provides
    GameInMemoryRepository provideInMemoryRepository() {
        return inMemoryRepository;
    }

    @Provides
    GameRemoteRepository provideRemoteRepository() {
        return remoteRepository;
    }

    @Provides
    public @NonNull GameUseCase provideUseCase() {
        return useCase;
    }
}
