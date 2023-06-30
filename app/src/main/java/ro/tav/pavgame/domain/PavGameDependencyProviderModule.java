package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.work.WorkManager;

import dagger.Module;
import dagger.Provides;
import ro.tav.pavgame.data.source.InMemoryDataSource;
import ro.tav.pavgame.data.source.LocalGameDataSource;
import ro.tav.pavgame.data.source.RemoteDataSource;

//se afla in .domain, deci ne poate da obiecte din tipul acelor clase
@Module
public final class PavGameDependencyProviderModule {
    private final GameInMemoryRepository inMemoryRepository;
    private final GameRemoteRepository remoteRepository;
    private final GameUseCase useCase;

    public PavGameDependencyProviderModule( Context context ) {
        GameLocalRepository localRepository = new LocalGameDataSource( context );
        inMemoryRepository = new InMemoryDataSource();
        remoteRepository = new RemoteDataSource();

        WorkManager workManager1;           //necesar pt jUnitTests
        try {
            workManager1 = WorkManager.getInstance( context );
        } catch ( Exception e ) {
            workManager1 = null;
        }
        WorkManager workManager = workManager1;

        GameMediator mediator = new GameMediator( localRepository, workManager );
        useCase = new GameUseCase( mediator );
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
    public GameUseCase provideUseCase() {
        return useCase;
    }
}
