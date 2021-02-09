package ro.tav.pavgame.domain;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ro.tav.pavgame.R;

public class GamesUseCaseViewHolder extends RecyclerView.ViewHolder {

    protected final TextView mTextViewName;
    protected final TextView mTextViewResult;
    protected final TextView mTextViewType;
    protected final TextView mTextViewTotalPoints;
    protected final CardView mCard;

    public GamesUseCaseViewHolder( @NonNull View itemView ) {
        super( itemView );

        mTextViewName = itemView.findViewById( R.id.textview_name );
        mTextViewResult = itemView.findViewById( R.id.textview_result );
        mTextViewType = itemView.findViewById( R.id.textview_gameType );
        mTextViewTotalPoints = itemView.findViewById( R.id.textView_totalPoints );
        mCard = itemView.findViewById( R.id.cardViewResults );
    }
}
