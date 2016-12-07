package com.sensei.twoplayer.core ;


import java.util.List ;


/**
 * This class represents a ComputerPlayer which uses a DecisionEngine for getting 
 * the next best move. A computer player generates moves in the same thread and
 * hence implements the generateNextMove method. This is unlike the HumanPlayer
 * who never implements the above method but instead relies on the UI to set the 
 * users next move.
 */
public class ComputerPlayer extends Player {
    
    private DecisionEngine decisionEngine = null ;
    
    /**
     * The constructor.
     * 
     * @param gameBoard       The gameBoard which is being played on.
     * @param perspective     The perspective of the computer player.
     * @param maxSearchDepth  The maximum search depth.
     * @param policies        A list of game specific evaluation policies
     *                        which will be registered with the decision engine.
     */
    public ComputerPlayer( GameBoard gameBoard, int perspective,
	                       int maxSearchDepth, List<EvaluationPolicy> policies ) {
        super( gameBoard, perspective ) ;
        decisionEngine = new DecisionEngine( gameBoard ) ;
        decisionEngine.setMaxDepthOfSearch( maxSearchDepth ) ;
        decisionEngine.setCurrentPerspective( perspective ) ;
		
		if ( policies != null ) {
		    for( EvaluationPolicy policy : policies ) {
		        decisionEngine.registerEvaluationPolicy( policy ) ;
		    }
		}
    }
    
    /**
     * This function generates the next best move for the computer in the same
     * thread. This is done by delegating the decision making to the DecisionEngine
     */
    protected void generateNextMove() {
        Move bestMove = decisionEngine.getBestMove() ;
        super.setNextMove( bestMove ) ;
    }
}
