package com.sensei.twoplayer.chess ;

import java.awt.Graphics ;
import java.awt.Point ;
import java.awt.image.BufferedImage ;
import java.io.InputStream ;
import java.util.HashMap ;
import java.util.Map ;

import javax.imageio.ImageIO ;

import com.sensei.twoplayer.core.Position ;
import com.sensei.twoplayer.core.ui.GameBoardUI ;

@SuppressWarnings( "serial" )
public class ChessBoardUI extends GameBoardUI {
    
    private int numRows = 0 ;
    private int numCols = 0 ;

    private Map<String, BufferedImage> imageCache = null ;
    ChessPosition selectedPosition = null ;

    public ChessBoardUI( ChessBoard chessBoard ) {
        super( chessBoard ) ;
        numRows = chessBoard.getNumRows() ;
        numCols = chessBoard.getNumCols() ;
        imageCache = new HashMap<String, BufferedImage>() ;
    }

    private BufferedImage getPositionImage( ChessPosition cell ) {
        
        BufferedImage retVal = null ;
        StringBuffer buffer = new StringBuffer( "/com/sensei/twoplayer/chess/images/" ) ;

        if( cell.pieceColor == ChessBoard.BLACK ) {
            buffer.append( "Black" ) ;
        }
        else if( cell.pieceColor == ChessBoard.WHITE ) {
            buffer.append( "White" ) ;
        }

        switch( cell.piece ){
            case ChessBoard.PAWN:
                buffer.append( "Pawn" ) ;
                break ;

            case ChessBoard.ROOK:
                buffer.append( "Rook" ) ;
                break ;

            case ChessBoard.KNIGHT:
                buffer.append( "Knight" ) ;
                break ;

            case ChessBoard.BISHOP:
                buffer.append( "Bishop" ) ;
                break ;

            case ChessBoard.QUEEN:
                buffer.append( "Queen" ) ;
                break ;

            case ChessBoard.KING:
                buffer.append( "King" ) ;
                break ;

            case ChessBoard.EMPTY:
                buffer.append( "Empty" ) ;
                break ;
        }

        if( cell.cellColor == ChessBoard.BLACK ) {
            buffer.append( "_B" ) ;
        }
        else {
            buffer.append( "_W" ) ;
        }

        buffer.append( ".jpg" ) ;

        retVal = ( BufferedImage )imageCache.get( buffer.toString() ) ;
        if( retVal == null ) {
            try {
                InputStream is = getClass().getResourceAsStream( buffer.toString() ) ;
                retVal = ImageIO.read( is ) ;

                imageCache.put( buffer.toString(), retVal ) ;
            }
            catch( Exception e ) {
                e.printStackTrace() ;
            }
        }

        return retVal ;
    }

    public int getColAtPoint( Point p ) {
        int cellWidth = getWidth() / numCols + 1 ;
        int x = p.x ;
        return x / cellWidth ;
    }

    public int getRowAtPoint( Point p ) {
        int cellHeight = getHeight() / numRows + 1 ;
        int y = p.y ;
        return y / cellHeight ;
    }

    public void paint( Graphics g ) {
        
        int height = getHeight() ;
        int width = getWidth() ;

        int cellWidth = width / numCols ;
        int cellHeight = height / numRows ;

        for( int row = 0 ; row < numRows ; row++ ) {
            for( int col = 0 ; col < numCols ; col++ ) {
                
                ChessPosition tempPosition = null ;
                tempPosition = (ChessPosition) gameBoard .getPosition( row, col ) ;
                BufferedImage image = getPositionImage( tempPosition ) ;

                g.drawImage( image, 
                             col * cellWidth + 1, 
                             row * cellHeight + 1,
                             cellWidth, 
                             cellHeight, 
                             null ) ;

                if( selectedPosition != null ) {
                    if( selectedPosition.row == row && 
                        selectedPosition.col == col ) {
                        g.draw3DRect( col * cellWidth + 5,
                                      row * cellHeight + 5, 
                                      cellWidth - 10,
                                      cellHeight - 10, true ) ;
                    }
                }
            }
        }
    }

    public void moveMade( com.sensei.twoplayer.core.Move move ) {
        
        int height = getHeight() ;
        int width = getWidth() ;

        int cellWidth = width / numCols ;
        int cellHeight = height / numRows ;

        ChessMove mv = (ChessMove) move ;

        repaint( mv.srcCol * cellWidth + 1, 
                 mv.srcRow * cellHeight + 1,
                 cellWidth, cellHeight ) ;
        
        repaint( mv.destCol * cellWidth + 1, 
                 mv.destRow * cellHeight + 1,
                 cellWidth, cellHeight ) ;
    }

    public void repaintPosition( Position p ) {
        
        int height = getHeight() ;
        int width = getWidth() ;

        int cellWidth = width / numCols ;
        int cellHeight = height / numRows ;

        repaint( p.col * cellWidth + 1, 
                 p.row * cellHeight + 1, 
                 cellWidth, cellHeight ) ;
    }
}
