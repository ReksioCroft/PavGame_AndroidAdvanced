package ro.tav.pavgame.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ro.tav.pavgame.R;
import ro.tav.pavgame.data.model.GameEntity;
import ro.tav.pavgame.databinding.GameItemBinding;
import ro.tav.pavgame.presentation.activity.RecyclerViewActivity;

public class GamesAdapter extends RecyclerView.Adapter< GamesAdapter.GamesViewHolder > implements Filterable {

    private final List< GameEntity > mGames;
    private final List< GameEntity > mFilteredGames;
    private final LayoutInflater mInflater;
    private View itemView;
    private final Context context;
    private final boolean setOnClickListenerOnViewCards;

    public GamesAdapter( Context context, boolean setOnClickListenerOnViewCards ) {
        mInflater = LayoutInflater.from( context );
        this.context = context;
        this.setOnClickListenerOnViewCards = setOnClickListenerOnViewCards;
        this.mGames = new ArrayList<>();
        this.mFilteredGames = new ArrayList<>();
    }

    public class GamesViewHolder extends RecyclerView.ViewHolder {
        public GameItemBinding binding;

        public GamesViewHolder( @NonNull View itemView ) {
            super( itemView );
            binding = GameItemBinding.bind( itemView );
        }
    }

    @NonNull
    @Override
    public GamesViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        itemView = mInflater.inflate( R.layout.game_item, parent, false );
        return new GamesViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder( @NonNull GamesViewHolder gamesViewHolder, int i ) {
        if ( mFilteredGames != null ) {
            GameEntity currentGame = mFilteredGames.get( i );
            gamesViewHolder.binding.textviewName.setText( currentGame.getNumeJucator() );
            String result = itemView.getResources().getString( currentGame.getResult() ? R.string.Win : R.string.Lose );
            gamesViewHolder.binding.textviewResult.setText( result );
            String gameType = currentGame.getGameType() + "x" + currentGame.getGameType();
            gamesViewHolder.binding.textviewGameType.setText( gameType );
            String date = currentGame.getGameDateTime();
            if ( date != null ) {
                if ( currentGame.getGameId().equals( currentGame.getNumeJucator() ) ) {//total points
                    String totalPoints = itemView.getResources().getString( R.string.total_points ) + date;
                    gamesViewHolder.binding.textViewDate.setVisibility( View.GONE );
                    gamesViewHolder.binding.textViewTotalPoints.setText( totalPoints );
                } else { // one user
                    gamesViewHolder.binding.textViewTotalPoints.setVisibility( View.GONE );
                    gamesViewHolder.binding.textViewDate.setText( date );
                }
            } else {
                gamesViewHolder.binding.textViewTotalPoints.setVisibility( View.GONE );
                gamesViewHolder.binding.textViewDate.setVisibility( View.GONE );
            }

            if ( gamesViewHolder.binding.textviewResult.getText().equals( itemView.getResources().getString( R.string.Lose ) ) ) {
                gamesViewHolder.binding.textviewName.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                gamesViewHolder.binding.textviewGameType.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                gamesViewHolder.binding.textviewResult.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                gamesViewHolder.binding.textViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                gamesViewHolder.binding.textViewDate.setTextColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
                gamesViewHolder.binding.cardViewResults.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
            } else {
                gamesViewHolder.binding.textviewName.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                gamesViewHolder.binding.textviewGameType.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                gamesViewHolder.binding.textviewResult.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                gamesViewHolder.binding.textViewTotalPoints.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                gamesViewHolder.binding.textViewDate.setTextColor( itemView.getResources().getColor( R.color.colorPrimary, context.getTheme() ) );
                gamesViewHolder.binding.cardViewResults.setCardBackgroundColor( itemView.getResources().getColor( R.color.colorAccent, context.getTheme() ) );
            }

            if ( setOnClickListenerOnViewCards ) {
                gamesViewHolder.binding.cardViewResults.setOnClickListener( new View.OnClickListener() {
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
            gamesViewHolder.binding.cardViewResults.setVisibility( View.GONE );
        }
    }

    @Override
    public int getItemCount() {
        if ( mFilteredGames != null )
            return mFilteredGames.size();
        else
            return 0;
    }

    public void setGames( @Nullable List< GameEntity > games ) {
        synchronized ( mGames ) {
            mGames.clear();
            if ( games != null ) {
                mGames.addAll( games );
            }
            synchronized ( mFilteredGames ) {
                mFilteredGames.clear();
                mFilteredGames.addAll( mGames );
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            private List< GameEntity > getFilteredResults( String constraint ) {
                List< GameEntity > filteredGames = new ArrayList<>();
                synchronized ( mGames ) {
                    for ( GameEntity game : mGames ) {
                        if ( game.getNumeJucator().toLowerCase().contains( constraint ) ) {
                            filteredGames.add( game );
                        }
                    }
                }
                return filteredGames;
            }

            @Override
            protected void publishResults( CharSequence constraint, FilterResults results ) {
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering( CharSequence constraint ) {
                List< GameEntity > filteredGames = ( constraint.length() == 0 ) ?
                        mGames : getFilteredResults( constraint.toString().toLowerCase() );

                synchronized ( mFilteredGames ) {
                    mFilteredGames.clear();
                    mFilteredGames.addAll( filteredGames );
                }

                return new FilterResults();
            }
        };
    }
}
