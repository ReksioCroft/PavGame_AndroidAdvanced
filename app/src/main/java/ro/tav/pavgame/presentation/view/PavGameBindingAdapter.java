package ro.tav.pavgame.presentation.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ro.tav.pavgame.data.model.GameEntity;
import ro.tav.pavgame.presentation.viewmodel.PavGameViewModel;


public class PavGameBindingAdapter {

    @BindingAdapter( "searchView" )
    public static void setSearchViewFilter( @NonNull RecyclerView mRecyclerViewGames, @Nullable SearchView searchView ) {
        if ( searchView != null ) {
            GamesAdapter gamesAdapter = ( GamesAdapter ) mRecyclerViewGames.getAdapter();
            if ( gamesAdapter != null ) {
                searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit( String query ) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange( String newText ) {
                        gamesAdapter.getFilter().filter( newText );
                        return false;
                    }
                } );

            }
        }
    }

    @BindingAdapter( { "gamesAdapter" } )
    public static void recycleViewInit( @NonNull RecyclerView mRecyclerViewGames, boolean setOnClickListenerOnViewCards ) {
        if ( mRecyclerViewGames.getAdapter() == null ) {
            // setam adaptorul pentru fiecare joc
            GamesAdapter gamesAdapter = new GamesAdapter( mRecyclerViewGames.getContext(), setOnClickListenerOnViewCards );
            mRecyclerViewGames.setAdapter( gamesAdapter );
        }
        if ( mRecyclerViewGames.getLayoutManager() == null ) {
            // setam layout managerul
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( mRecyclerViewGames.getContext() );
            mRecyclerViewGames.setLayoutManager( layoutManager );
        }
    }

    @BindingAdapter( { "pavGameViewModel", "activity", "user" } )
    public static void recycleViewGamesBind( @NonNull RecyclerView mRecyclerViewGames,
                                             @NonNull PavGameViewModel pavGameViewModel,
                                             @NonNull AppCompatActivity activity,
                                             @Nullable String user ) {
        //daca am primit un string null, facem bind cu toate jocurile din repo
        if ( user == null ) {
            LiveData< List< GameEntity > > listLiveData = pavGameViewModel.getAllGames();
            if ( listLiveData == null )
                return;
            listLiveData.observe( activity, new Observer< List< GameEntity > >() {
                @Override
                public void onChanged( @Nullable final List< GameEntity > games ) {
                    if ( games == null )
                        return;
                    //suntem siguri ca adaptorul nostru este de tipul GameAdapter
                    GamesAdapter gamesAdapter = ( GamesAdapter ) mRecyclerViewGames.getAdapter();
                    Objects.requireNonNull( gamesAdapter ).setGames( games );
                }
            } );
        }  //altfel, afisam jocurile unui anumite utilizator
        else {
            LiveData< List< GameEntity > > listLiveData = pavGameViewModel.getSpecificGamesbyUserName( user );
            if ( listLiveData == null )
                return;
            listLiveData.observe( activity, new Observer< List< GameEntity > >() {
                @Override
                public void onChanged( @Nullable final List< GameEntity > games ) {
                    if ( games == null )
                        return;
                    GamesAdapter gamesAdapter = ( GamesAdapter ) mRecyclerViewGames.getAdapter();
                    Objects.requireNonNull( gamesAdapter ).setGames( games );
                }
            } );
        }
    }
}
