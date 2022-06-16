package ro.tav.pavgame.presentation.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;
import ro.tav.pavgame.presentation.PavGameViewModel;
import ro.tav.pavgame.presentation.PavGameViewModelFactory;
import ro.tav.pavgame.presentation.view.PavGameBindingAdapter;


public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_recycler_view );

        //butonul de sus pentru a inchide activitatea
        ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null )
            actionBar.setDisplayHomeAsUpEnabled( true );

        // obtinem recycleView-ul si searchView-ul
        RecyclerView mRecyclerViewGames = findViewById( R.id.recycler_view_contacts_1 );
        SearchView searchView = findViewById( R.id.search_bar );

        //daca suntem pe jocurile unui singur utilizator, va fi setat fals la verificare
        //pentru a nu permite sa se creeze activitati la infinit pt a afisa jocurile unui utilizator
        boolean setOnClickListenerOnViewCards = true;

        //verificam sa vedem daca vrem sa afisam doar pt un anumit utilizator
        Bundle b = getIntent().getExtras();
        String specificUser = null; // specificUser==null=>afisam toti utilizatorii
        if ( b != null ) {
            specificUser = b.getString( "specificUser" );
            setOnClickListenerOnViewCards = false;
            searchView.setVisibility( View.GONE ); //atunci cand afisam un singur utilizator
            searchView = null;                     //nu vrem sa afisam searchView-ul
        }

        //binding pentru a seta gameAdaptorul si layoutManagerul la RecyclerView-ul nostru
        PavGameBindingAdapter.recycleViewInit( mRecyclerViewGames, setOnClickListenerOnViewCards );

        //obtinem ViewModel
        PavGameViewModel pavGameViewModel =
                new ViewModelProvider( this, new PavGameViewModelFactory( PavGameApplication.getApplication() ) )
                        .get( PavGameViewModel.class );

        //binding pentru a prelua datele din repository
        PavGameBindingAdapter.recycleViewGamesBind( mRecyclerViewGames, pavGameViewModel, specificUser );

        //activare searchView
        PavGameBindingAdapter.setSearchViewFilter( mRecyclerViewGames, searchView );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        PavGameApplication.getApplication().removeActivity( this );
    }


    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if ( item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
