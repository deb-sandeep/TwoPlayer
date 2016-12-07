package com.sensei.twoplayer.core.ui ;

import java.awt.Canvas ;
import java.awt.Point ;
import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;
import java.util.ArrayList ;
import java.util.List ;

import com.sensei.twoplayer.core.GameBoard ;
import com.sensei.twoplayer.core.GameBoardMoveListener ;
import com.sensei.twoplayer.core.Position ;

@SuppressWarnings( "serial" )
public abstract class GameBoardUI extends Canvas 
    implements GameBoardMoveListener {
    
    private List<GameBoardInteractionListener> interactionListeners = null ;
    protected GameBoard gameBoard = null ;

    protected GameBoardUI( GameBoard gameBoard ) {
        
        interactionListeners = new ArrayList<GameBoardInteractionListener>() ;
        this.gameBoard = gameBoard ;
        this.gameBoard.addMoveListener( this ) ;

        addMouseListener( new MouseAdapter() {
            public void mouseClicked( MouseEvent e ) {
                Point p = e.getPoint() ;
                userClick( p ) ;
            }
        } ) ;
    }

    private void userClick( Point p ) {
        int row = getRowAtPoint( p ) ;
        int col = getColAtPoint( p ) ;

        Position pos = gameBoard.getPosition( row, col ) ;
        positionSelected( pos ) ;
    }

    public void moveMade( com.sensei.twoplayer.core.Move move ) {
        repaint( 0, 0, getWidth(), getHeight() ) ;
    }

    public void gameOver( String message ) {
        MessageDialog.showMessageDialog( message ) ;
    }

    public void addInteractionListener( GameBoardInteractionListener listener ) {
        interactionListeners.add( listener ) ;
    }

    public void removeInteractionListener( GameBoardInteractionListener listener ) {
        interactionListeners.remove( listener ) ;
    }

    public void positionSelected( Position p ) {
        for( GameBoardInteractionListener listener : interactionListeners ) {
            listener.cellSelected( p ) ;
        }
    }

    public void repaintPosition( Position p ) {
        repaint() ;
    }

    public abstract int getRowAtPoint( Point p ) ;

    public abstract int getColAtPoint( Point p ) ;
}
