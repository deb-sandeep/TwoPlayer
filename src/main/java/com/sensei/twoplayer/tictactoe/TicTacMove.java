package com.sensei.twoplayer.tictactoe;


import com.sensei.twoplayer.core.Move ;


public class TicTacMove extends Move
{
    public int row = -1 ;
    public int col = -1 ;
    public int perspective = -1 ;
    
    public TicTacMove( int row, int col, int pers )
    {
        this.row = row ;
        this.col = col ;
        this.perspective = pers ;
    }
    
    public String toString()
    {
        return "[" +  row + "," + col + "] -> " + getPers() ;
    }
    
    // DEBUG Function
    public String getPers()
    {
        String retVal = "" ;
        if ( perspective == TicTacToeBoard.EMPTY )
        {
            retVal = "" ;
        }
        else if ( perspective == TicTacToeBoard.CIRCLE )
        {
            retVal = "O" ;
        }
        else if ( perspective == TicTacToeBoard.CROSS )
        {
            retVal = "X" ;
        }
        return retVal ;
    }
}
