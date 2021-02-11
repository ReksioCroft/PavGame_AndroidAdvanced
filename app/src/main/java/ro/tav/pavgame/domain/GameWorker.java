package ro.tav.pavgame.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class GameWorker extends Worker {
    private Context context;

    public GameWorker( @NonNull Context context,
                       @NonNull WorkerParameters workerParams ) {
        super( context, workerParams );
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
//        Data data = getInputData();
//        String value = data.getString( "type" );
//
//        if ( "get".equals( value ) ) {
//            List < GameHistory > gameHistories = RemoteDataSource.getItems();
//            for ( GameHistory gameHistory : gameHistories ) {
//                PavGameBindingAdapter.addGameResult( new RecyclerView( context ), gameHistory );
//            }
//
//        } else if ( "post".equals( value ) ) {
//            // POST operation
//            PavGameViewModel pavGameViewModel = PavGameViewModel.getInstance();
//            LiveData < List < GameHistory > > liveData = PavGameViewModel.getInstance().getGame().getAllGames();
//            for( GameHistory gameHistory: Objects.requireNonNull( liveData.getValue() ) ){
//
//            }
//        }

        return Result.success();
    }
}

