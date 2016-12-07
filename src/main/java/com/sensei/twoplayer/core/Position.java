package com.sensei.twoplayer.core ;

/**
 * This class represents a position in the game board. This is an empty class
 * and should be subclasses to provide appropriate implementation of game board
 * positions.
 */
public class Position {
    
    public int row ;
    public int col ;

    public Object clone() {
        return new Position() ;
    }
}
