package com.sensei.twoplayer.core ;

/**
 * This interface should be implemented by all parties interested in listenting
 * to the moves being made on the game board. The listeners will also be
 * notified when the game got over due to a move.
 */
public interface GameBoardMoveListener {
    
    public void moveMade( Move move ) ;
    public void gameOver( String message ) ;
}
