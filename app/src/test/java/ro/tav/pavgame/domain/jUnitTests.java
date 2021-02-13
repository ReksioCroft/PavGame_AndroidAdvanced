package ro.tav.pavgame.domain;

import org.junit.Test;

import java.util.Objects;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.data.GameEntity;

public class jUnitTests {
    @Test
    public void test1() {
        GameEntity mGame = new GameEntity();
        mGame.setNumeJucator( "myTest" );
        mGame.setResult( "Win" );
        mGame.setGameType( "Game Type: 2048x2048" );
        mGame.setGameId( "" );
        PavGameApplication pavGameApplication = new PavGameApplication();
        GameUseCase gameUseCase = new GameUseCase( PavGameApplication.getApplication() );
        gameUseCase.insertGame( mGame );
        assert ( Objects.requireNonNull( gameUseCase.getAllGames().getValue() ).contains( mGame ) );
    }
}
