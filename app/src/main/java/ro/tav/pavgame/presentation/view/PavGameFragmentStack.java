package ro.tav.pavgame.presentation.view;

import androidx.fragment.app.Fragment;

import java.util.Stack;

import ro.tav.pavgame.presentation.fragments.GameFragment;

public class PavGameFragmentStack extends Stack< Fragment > {
    private boolean containsGame = false;
    private int co = 0;

    public boolean isGameInStack() {
        return containsGame;
    }

    @Override
    public Fragment push( Fragment item ) {
        if ( item.getClass() == GameFragment.class )
            containsGame = true;
        co++;
        return super.push( item );
    }

    @Override
    public synchronized Fragment pop() {
        if ( !this.empty() ) {
            if ( this.peek().getClass() == GameFragment.class )
                containsGame = false;
            co--;
        }
        return super.pop();
    }

    public int getNrOfItems() {
        return co;
    }
}
