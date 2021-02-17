package ro.tav.pavgame.presentation.view.recycleViewAux;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ro.tav.pavgame.R;
import ro.tav.pavgame.data.GameEntity;
import ro.tav.pavgame.presentation.view.RecyclerViewActivity;

public class GamesAdapter extends RecyclerView.Adapter < GamesViewHolder > {

    private List < GameEntity > mGames;
    private final LayoutInflater mInflater;
    private View itemView;
    private final Context context;
    private final boolean setOnClickListenerOnViewCards;

    public GamesAdapter( Context context, boolean setOnClickListenerOnViewCards ) {
        mInflater = LayoutInflater.from( context );
        this.context = context;
        this.setOnClickListenerOnViewCards = setOnClickListenerOnViewCards;
    }

    @NonNull
    @Override
    public GamesViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        itemView = mInflater.inflate( R.layout.contact_item, parent, false );
        return new GamesViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder( @NonNull GamesViewHolder gamesViewHolder, int i ) {
        if ( mGames != null ) {
            GameEntity currentGame = mGames.get( i );
            gamesViewHolder.mTextViewName.setText( currentGame.getNumeJucator() );
            String result = itemView.getResources().getString( currentGame.getResult() ? R.string.Win : R.string.Lose );
            gamesViewHolder.mTextViewResult.setText( result );
            String gameType = currentGame.getGameType() + "x" + currentGame.getGameType();
            gamesViewHolder.mTextViewType.setText( gameType );

            String totalPoints = itemView.getResources().getString( R.string.total_points ) + currentGame.getGameId();
            gamesViewHolder.mTextViewTotalPoints.setText( totalPoints );

            if ( gamesViewHolder.mTextViewResult.getText().equals( itemView.getResources().getString( R.string.Lose ) ) ) {
                gamesViewHolder.mTextViewName.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                gamesViewHolder.mTextViewType.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                gamesViewHolder.mTextViewResult.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                gamesViewHolder.mTextViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                gamesViewHolder.mCard.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
            } else {
                gamesViewHolder.mTextViewName.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                gamesViewHolder.mTextViewType.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                gamesViewHolder.mTextViewResult.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                gamesViewHolder.mTextViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                gamesViewHolder.mCard.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
            }

            if ( setOnClickListenerOnViewCards ) {
                gamesViewHolder.mCard.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick( View view ) {
                        Intent intent = new Intent( context, RecyclerViewActivity.class );
                        Bundle bundle = new Bundle();
                        bundle.putString( "specificUser", currentGame.getNumeJucator() ); //user_email
                        intent.putExtras( bundle ); //Put your id to your next Intent
                        context.startActivity( intent );//cream o noua activitate pt utilizatorul specific
                    }
                } );
            }
        } else {
            gamesViewHolder.mTextViewName.setText( R.string.noText );
            gamesViewHolder.mTextViewResult.setText( R.string.noText );
            gamesViewHolder.mTextViewType.setText( R.string.noText );
            gamesViewHolder.mTextViewTotalPoints.setText( R.string.noText );
        }
    }

    @Override
    public int getItemCount() {
        if ( mGames != null )
            return mGames.size();
        else
            return 0;
    }

    public void setGames( List < GameEntity > games ) {
        mGames = games;
        notifyDataSetChanged();
    }
}
