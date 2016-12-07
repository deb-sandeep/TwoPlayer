package com.sensei.twoplayer.tictactoe ;

import java.awt.Color ;
import java.awt.Graphics ;
import java.awt.Point ;

import com.sensei.twoplayer.core.ui.GameBoardUI ;

@SuppressWarnings( "serial" )
public class TicTacToeBoardUI extends GameBoardUI {
    
    private int numRows = 0 ;
    private int numCols = 0 ;

    public TicTacToeBoardUI( TicTacToeBoard board ) {
        super( board ) ;
        numRows = board.getNumRows() ;
        numCols = board.getNumCols() ;
    }

    public int getRowAtPoint( Point p ) {
        int cellHeight = getHeight() / numRows + 1 ;
        int y = p.y ;
        return y / cellHeight ;
    }

    public int getColAtPoint( Point p ) {
        int cellWidth = getWidth() / numCols + 1 ;
        int x = p.x ;
        return x / cellWidth ;
    }

    public void paint( java.awt.Graphics g ) {
        super.paint( g ) ;
        g.setColor( Color.black ) ;
        for( int i = 1 ; i < numRows ; i++ ) {
            g.drawLine( 0, 
                        i * getHeight() / numRows, 
                        getWidth(), 
                        i * getHeight() / numRows ) ;
        }
        for( int i = 1 ; i < numCols ; i++ ) {
            g.drawLine( i * getWidth() / numCols, 
                        0, 
                        i * getWidth() / numCols,
                        getWidth() ) ;
        }

        for( int row = 0 ; row < numRows ; row++ ) {
            for( int col = 0 ; col < numCols ; col++ ) {
                TicTacPosition tempPosition = (TicTacPosition)gameBoard.getPosition( row, col ) ;
                drawCell( tempPosition.row, tempPosition.col,
                        tempPosition.perspective, g ) ;
            }
        }
    }

    private void drawCell( int row, int col, int perspective, Graphics g ) {
        
        int cellWidth = getWidth() / numCols ;
        int cellHeight = getHeight() / numRows ;

        int x = col * cellWidth + cellWidth / 5 ;
        int y = row * cellHeight + cellHeight / 5 ;

        if( perspective == TicTacToeBoard.CIRCLE ) {
            g.drawOval( x, y, cellWidth / 2, cellHeight / 2 ) ;
        }
        else if( perspective == TicTacToeBoard.CROSS ) {
            g.drawLine( x, y, x + cellWidth / 2, y + cellHeight / 2 ) ;
            g.drawLine( x, y + cellHeight / 2, x + cellWidth / 2, y ) ;
        }
    }
}
