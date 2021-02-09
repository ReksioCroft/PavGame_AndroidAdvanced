package ro.tav.pavgame.domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ro.tav.pavgame.R;
import ro.tav.pavgame.data.GameHistory;

public class GamesAdapter extends RecyclerView.Adapter < GamesViewHolder > {

    private List < GameHistory > mGames;
    private final LayoutInflater mInflater;
    private View itemView;

    public GamesAdapter( Context context ) {
        mInflater = LayoutInflater.from( context );
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
            GameHistory currentGame = mGames.get( i );
            contactsViewHolder.mTextViewName.setText( currentGame.getNume() );
            contactsViewHolder.mTextViewResult.setText( currentGame.getResult() );
            contactsViewHolder.mTextViewType.setText( currentGame.getGameType() );

            String totalPoints = itemView.getResources().getString( R.string.total_points ) + currentGame.getGameId();
            contactsViewHolder.mTextViewTotalPoints.setText( totalPoints );

            if ( contactsViewHolder.mTextViewResult.getText().equals( "Lose" ) ) {
                contactsViewHolder.mTextViewName.setTextColor( itemView.getResources().getColor( R.color.colorAccent ) );
                contactsViewHolder.mTextViewResult.setTextColor( itemView.getResources().getColor( R.color.colorAccent ) );
                contactsViewHolder.mTextViewType.setTextColor( itemView.getResources().getColor( R.color.colorAccent ) );
                contactsViewHolder.mTextViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorAccent ) );
                contactsViewHolder.mCard.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorPrimary ) );
            } else {
                contactsViewHolder.mTextViewName.setTextColor( itemView.getResources().getColor( R.color.colorPrimary ) );
                contactsViewHolder.mTextViewResult.setTextColor( itemView.getResources().getColor( R.color.colorPrimary ) );
                contactsViewHolder.mTextViewType.setTextColor( itemView.getResources().getColor( R.color.colorPrimary ) );
                contactsViewHolder.mTextViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorPrimary ) );
                contactsViewHolder.mCard.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorAccent ) );
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

    public void setGames( List < GameHistory > games ) {
        mGames = games;
        notifyDataSetChanged();
    }
}
