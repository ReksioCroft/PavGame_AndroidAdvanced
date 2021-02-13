package ro.tav.pavgame.presentation.view.recycleViewAux;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ro.tav.pavgame.R;
import ro.tav.pavgame.data.GameEntity;

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
            contactsViewHolder.mTextViewResult.setText( currentGame.getResult() );
            contactsViewHolder.mTextViewType.setText( currentGame.getGameType() );

            String totalPoints = itemView.getResources().getString( R.string.total_points ) + currentGame.getGameId();
            contactsViewHolder.mTextViewTotalPoints.setText( totalPoints );

            if ( contactsViewHolder.mTextViewResult.getText().equals( "Lose" ) ) {
                contactsViewHolder.mTextViewName.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                contactsViewHolder.mTextViewResult.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                contactsViewHolder.mTextViewType.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                contactsViewHolder.mTextViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                contactsViewHolder.mCard.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
            } else {
                contactsViewHolder.mTextViewName.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                contactsViewHolder.mTextViewResult.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                contactsViewHolder.mTextViewType.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                contactsViewHolder.mTextViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                contactsViewHolder.mCard.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
            }
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
