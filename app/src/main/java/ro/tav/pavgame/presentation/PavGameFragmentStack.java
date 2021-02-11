package ro.tav.pavgame.presentation;

import androidx.fragment.app.Fragment;

import java.util.Stack;

import ro.tav.pavgame.presentation.fragments.GameFragment;

public class PavGameFragmentStack extends Stack < Fragment > {
    private Boolean containsGame = Boolean.FALSE;
    private int co = 0;

    public Boolean isGameInStack() {
        return containsGame;
    }

    @Override
    public Fragment push( Fragment item ) {
        if ( item.getClass() == GameFragment.class )
            containsGame = Boolean.TRUE;
        co++;
        return super.push( item );
    }

    @Override
    public synchronized Fragment pop() {
        if ( !this.empty() ) {
            if ( this.peek().getClass() == GameFragment.class )
                containsGame = Boolean.FALSE;
            co--;
        }
        return super.pop();
    }

    public int getNrOfItems() {
        return co;
    }
}
