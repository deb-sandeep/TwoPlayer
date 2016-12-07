package com.sensei.twoplayer.core ;

/**
 * This is the base class for players in the game. The players in the game are
 * queried by the game engine for their next moves till the game is over. Now, a
 * player can be either Human or Computer. A human player generally interacts
 * with a user interface to input his moves and the computer relies on a
 * decision engine to generate its next best move. To implement this concept
 * generically, this class implements a publisher-subscriber model to return the
 * next move. In case a move is not set the call to getMove waits till a move is
 * set.
 */
public abstract class Player {
    
    private Move        nextMove    = null ;
    private boolean     available   = false ;
    protected GameBoard gameBoard   = null ;
    protected int       perspective = 0 ;

    protected Player( GameBoard gameBoard, int perspective ) {
        this.gameBoard = gameBoard ;
        this.perspective = perspective ;
    }

    /**
     * Gets the next move for this player.
     */
    public synchronized Move getMove() {
        // Let the sub-class decide whether to generate the move in a
        // synchronous manner or an asynchronous manner.
        generateNextMove() ;
        while( available == false ) {
            try {
                wait() ;
            }
            catch( InterruptedException e ) {
            }
        }

        available = false ;
        notifyAll() ;
        return nextMove ;
    }

    /**
     * Set the next best move for this player. This function is generally called
     * either by the ui (In case of a human player) and by the decision engine
     * in case of a computer player.
     */
    public synchronized void setNextMove( Move move ) {
        
        if( move == null ) {
            System.err.println( "Can't set null move " ) ;
            return ;
        }
        while( available == true ) {
            try {
                wait() ;
            }
            catch( InterruptedException e ) {
            }
        }

        available = true ;
        nextMove = move ;
        notifyAll() ;
    }

    /**
     * This function should be overridden by sub classes who want to do the
     * processing in the same thread.. like the computer player. This method
     * should call the setNextMove to set the processed move.
     */
    protected void generateNextMove() {
    }
}
