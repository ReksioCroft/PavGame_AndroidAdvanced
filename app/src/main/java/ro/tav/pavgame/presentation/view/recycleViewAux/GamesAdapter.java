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

    public GamesAdapter( Context context ) {
        mInflater = LayoutInflater.from( context );
        this.context = context;
    }

    @NonNull
    @Override
    public GamesViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        itemView = mInflater.inflate( R.layout.contact_item, parent, false );
        return new GamesViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder( @NonNull GamesViewHolder contactsViewHolder, int i ) {
        if ( mGames != null ) {
            GameEntity currentGame = mGames.get( i );
            contactsViewHolder.mTextViewName.setText( currentGame.getNumeJucator() );
            String result = itemView.getResources().getString( currentGame.getResult() ? R.string.Win : R.string.Lose );
            contactsViewHolder.mTextViewResult.setText( result );
            String gameType = currentGame.getGameType() + "x" + currentGame.getGameType();
            contactsViewHolder.mTextViewType.setText( gameType );

            String totalPoints = itemView.getResources().getString( R.string.total_points ) + currentGame.getGameId();
            contactsViewHolder.mTextViewTotalPoints.setText( totalPoints );

            if ( contactsViewHolder.mTextViewResult.getText().equals( itemView.getResources().getString( R.string.Lose ) ) ) {
                contactsViewHolder.mTextViewName.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                contactsViewHolder.mTextViewType.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                contactsViewHolder.mTextViewResult.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                contactsViewHolder.mTextViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                contactsViewHolder.mCard.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
            } else {
                contactsViewHolder.mTextViewName.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                contactsViewHolder.mTextViewType.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                contactsViewHolder.mTextViewResult.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                contactsViewHolder.mTextViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                contactsViewHolder.mCard.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
            }

            contactsViewHolder.mCard.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    Intent intent = new Intent( context, RecyclerViewActivity.class );
                    Bundle b = new Bundle();
                    b.putString( "user", currentGame.getNumeJucator() ); //user_email
                    intent.putExtras( b ); //Put your id to your next Intent
                    context.startActivity( intent );//cream o noua activitate pt utilizatorul specific
                }
            } );
        } else {
            contactsViewHolder.mTextViewName.setText( R.string.noText );
            contactsViewHolder.mTextViewResult.setText( R.string.noText );
            contactsViewHolder.mTextViewType.setText( R.string.noText );
            contactsViewHolder.mTextViewTotalPoints.setText( R.string.noText );
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
