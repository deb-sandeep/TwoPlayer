package com.sensei.twoplayer.chess.strategy ;

import java.util.List ;

import com.sensei.twoplayer.chess.ChessBoard ;
import com.sensei.twoplayer.core.GameBoard ;
import com.sensei.twoplayer.core.Move ;

public class MobilityStrategy extends BaseStrategy {
    
    public MobilityStrategy( double weight ) {
        super( weight ) ;
    }

    public double getEvaluation( GameBoard gameBoard, int perspective ) {
        double evalVal = 0 ;
        double ownMobilityAdvantage = 0 ;
        double enemyMobilityAdvantage = 0 ;

        ChessBoard board = (ChessBoard) gameBoard ;

        ownMobilityAdvantage = getMobilityAdvantage( perspective, board ) ;
        enemyMobilityAdvantage = getMobilityAdvantage(
                board.getEnemyPerspective( perspective ), board ) ;

        evalVal = ( ownMobilityAdvantage - enemyMobilityAdvantage ) ;

        return evalVal ;
    }

    private double getMobilityAdvantage( int perspective, ChessBoard board ) {
        List<Move> allMoves = board.getAllPossibleMoves( perspective ) ;
        return allMoves.size() ;
    }
}
