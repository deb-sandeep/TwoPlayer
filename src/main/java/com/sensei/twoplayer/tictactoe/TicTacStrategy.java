package com.sensei.twoplayer.tictactoe;


import com.sensei.twoplayer.core.* ;


public class TicTacStrategy implements EvaluationPolicy
{
    public double getEvaluation( GameBoard gameBoard, int perspective )
    {
        double retVal = 0 ;
        TicTacToeBoard board = ( TicTacToeBoard )gameBoard ;
		
		int enemyPerspective = gameBoard.getEnemyPerspective( perspective ) ;


        if( board.isLineMade( perspective ) )
            return 100 ;
        
        if( board.isLineMade( enemyPerspective  ) )
            return -100 ;

        return retVal ;		
    }
    
    public double getEvaluationWeight()
    {
        return 1 ;
    }
}
