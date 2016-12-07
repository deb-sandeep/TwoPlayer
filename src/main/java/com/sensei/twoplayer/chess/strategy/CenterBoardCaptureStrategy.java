package com.sensei.twoplayer.chess.strategy ;

import com.sensei.twoplayer.chess.* ;
import com.sensei.twoplayer.core.* ;

public class CenterBoardCaptureStrategy extends BaseStrategy {
    
    private static final double CENTER_RW   = 1.5 ;
    private static final double OUTER_RW    = 0.5 ;

    private double RING_WT[][] = {
            { OUTER_RW, OUTER_RW, OUTER_RW, OUTER_RW },
            { OUTER_RW, CENTER_RW, CENTER_RW, OUTER_RW },
            { OUTER_RW, CENTER_RW, CENTER_RW, OUTER_RW },
            { OUTER_RW, OUTER_RW, OUTER_RW, OUTER_RW } } ;

    public CenterBoardCaptureStrategy( double wt ) {
        super( wt ) ;
    }

    public double getEvaluation( GameBoard gameBoard, int perspective ) {
        
        ChessBoard chessBoard = (ChessBoard) gameBoard ;
        
        double ownEnc = 0 ;
        double eneEnc = 0 ;
        int    enePers = chessBoard.getEnemyPerspective( perspective ) ;

        int[][] ownPcLoc = chessBoard.getPositionIndex( perspective ) ;
        int[][] enePcLoc = chessBoard.getPositionIndex( enePers ) ;

        for( int row = 2 ; row < 6 ; row++ ) {
            for( int col = 2 ; col < 6 ; col++ ) {
                if( ownPcLoc[row][col] == 1 ) {
                    ownEnc += RING_WT[row - 2][col - 2] ;
                }
                else if( enePcLoc[row][col] == 1 ) {
                    eneEnc += RING_WT[row - 2][col - 2] ;
                }
            }
        }

        return ownEnc - eneEnc ;
    }
}
