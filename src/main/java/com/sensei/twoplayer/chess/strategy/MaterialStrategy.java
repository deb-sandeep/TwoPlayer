package com.sensei.twoplayer.chess.strategy ;

import com.sensei.twoplayer.chess.ChessBoard ;
import com.sensei.twoplayer.chess.ChessPosition ;
import com.sensei.twoplayer.core.GameBoard ;

public class MaterialStrategy extends BaseStrategy {
    public MaterialStrategy( double weight ) {
        super( weight ) ;
    }

    public double getEvaluation( GameBoard gameBoard, int perspective ) {
        
        ChessBoard chessBoard = (ChessBoard) gameBoard ;
        int enePers = chessBoard.getEnemyPerspective( perspective ) ;

        double ownMaterialVal = 0 ;
        double eneMaterialVal = 0 ;

        int[][] ownPcLoc = chessBoard.getPositionIndex( perspective ) ;
        int[][] enePcLoc = chessBoard.getPositionIndex( enePers ) ;

        ChessPosition tempCell = null ;

        for( int row = 0 ; row < 8 ; row++ ) {
            for( int col = 0 ; col < 8 ; col++ ) {
                
                tempCell = ( ChessPosition )chessBoard.getPosition( row, col ) ;

                if( ownPcLoc[row][col] == 1 ) {
                    ownMaterialVal += ChessBoard.getMaterialWeight( tempCell.piece ) ;
                }
                else if( enePcLoc[row][col] == 1 ) {
                    eneMaterialVal += ChessBoard.getMaterialWeight( tempCell.piece ) ;
                }
            }
        }

        return ( ownMaterialVal - eneMaterialVal ) ;
    }
}
