package ro.tav.pavgame.presentation;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.data.GameHistory;
import ro.tav.pavgame.domain.GameUseCase;
import ro.tav.pavgame.presentation.view.recycleViewAux.PavGameBindingAdapter;

public class PavGameViewModel extends ViewModel {
    static private GameUseCase game;      ///game face legatura cu repo-ul din domain
    static private PavGameViewModel instance = null;

    private PavGameViewModel( ViewModelStoreOwner c ) { //constructor privat pt clasa singleotn
        game = new ViewModelProvider( c ).get( GameUseCase.class );
    }

    public static PavGameViewModel getInstance( ViewModelStoreOwner c ) {
        if ( instance == null )
            instance = new PavGameViewModel( c );
        return instance;
    }

    public static PavGameViewModel getInstance() {//poate intoarce NULL
        return instance;
    }

    public static GameUseCase getGame(){  ///pentru a putea accesa repoul din domain
        return game;
    }

    public static void addResult( RecyclerView recyclerView, String userName, String result, String gametype){
        //construim jocul si apoi il trimitem in bindingAdapter pt a fi adaugat
        GameHistory mGame = new GameHistory();
        mGame.setNume( userName );
        mGame.setResult( result );
        mGame.setGameType( gametype );
        PavGameBindingAdapter.addGameResult( recyclerView, mGame );
    }
}
