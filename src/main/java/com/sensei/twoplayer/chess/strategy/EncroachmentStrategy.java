package com.sensei.twoplayer.chess.strategy ;

import com.sensei.twoplayer.chess.* ;
import com.sensei.twoplayer.core.* ;

public class EncroachmentStrategy extends BaseStrategy {
    
    public EncroachmentStrategy( double weight ) {
        super( weight ) ;
    }

    public double getEvaluation( GameBoard gameBoard, int perspective ) {
        
        ChessBoard chessBoard = (ChessBoard) gameBoard ;
        int enePers = chessBoard.getEnemyPerspective( perspective ) ;
        
        double ownEnc = 0 ;
        double eneEnc = 0 ;

        int ownBaseLine = ( perspective == chessBoard.getTopColor() ) ? 1 : 6 ;
        int eneBaseLine = ( perspective == chessBoard.getTopColor() ) ? 6 : 7 ;
        
        int[][] ownPcLoc = chessBoard.getPositionIndex( perspective ) ;
        int[][] enePcLoc = chessBoard.getPositionIndex( enePers ) ;

        for( int row = 0 ; row < 8 ; row++ ) {
            for( int col = 0 ; col < 8 ; col++ ) {
                if( ownPcLoc[row][col] == 1 ) {
                    ownEnc += Math.abs( row - ownBaseLine ) ;
                }
                else if( enePcLoc[row][col] == 1 ) {
                    eneEnc += Math.abs( row - eneBaseLine ) ;
                }
            }
        }

        return ownEnc - eneEnc ;
    }
}
