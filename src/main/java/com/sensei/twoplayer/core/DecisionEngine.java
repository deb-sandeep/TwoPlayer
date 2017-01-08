package com.sensei.twoplayer.core ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;

/**
 * This class is used to help the computer find the best possible move in a
 * given board situation. This DecisionEngine is build in a very generic way not
 * taking into assumption any game in particular. However this decision engine
 * does make some assumptions on the game board, so all the game specific boards
 * should extend from the GameBoard class provided in the code package.
 * <p>
 * This Engine treats every game as a GameTree and tries to apply the MINI_MAX
 * algorithm with Alpha-Beta pruning as a performance enhancer. By changing the
 * currentPerspective of this decision engine, it can be effectively used to
 * suggest the human player for better moves.
 * <p>
 * This engine operates on a variable number of evaluation policies, which are
 * registered by the computer player in a game specific manner. A evaluation
 * policy is essentially a piece of logic which evaluates a specified game board
 * in a given state and returns the goodness of the state, in simpler terms.. it
 * tries to evaluate how good is this state for me. Goodness increses in the
 * positive scale.
 * <p>
 * 
 * <pre>
 *      function AlphaBetaMM( N, A, B) 
 *      # N = A Node in the game tree
 *      # A = value of alpha.
 *      # B = value of bets.
 *      begin
 *       if N is a leaf then
 *          return evaluateBoard ;
 *       if N is a Min node then
 *         For all successor Ni of N loop
 *           B = Min{ B, AlphaBetaMM(Ni, A, B)};
 *           if A >= B then
 *             Return A 
 *           fi
 *         end For
 *         Return Beta;
 *       else
 *         For each successor Ni of N loop
 *           A = Max{ A, AlphaBetaMM(Ni, A, B)};
 *           if A >= B then
 *             Return B
 *           fi 
 *         end For
 *         Return A;
 *      end AlphaBetaMM
 * </pre>
 * 
 */
class DecisionEngine {
    
    private GameBoard gameBoard = null ;
    private List<EvaluationPolicy> evaluationPolicies = null ;
    private int currentPerspective = Integer.MIN_VALUE ;
    private int maxDepthOfSearch   = Integer.MAX_VALUE ;

    public DecisionEngine( GameBoard gameBoard ) {
        evaluationPolicies = new ArrayList<EvaluationPolicy>() ;
        setGameBoard( gameBoard ) ;
    }

    public Move getBestMove() {
        
        Move bestMove = null ;
        List<Move> allMoves = null ;

        double alpha = Double.MAX_VALUE ;
        double beta = Double.MIN_VALUE ;
        double subTreeEval = 0 ;

        GameBoard scratchBoard = gameBoard.getScratchBoard() ;
        allMoves = scratchBoard.getAllPossibleMoves( currentPerspective ) ;
        Collections.sort( allMoves ) ;

        if( allMoves == null || allMoves.size() == 0 ) {
            System.out.println( "Stale mate.... no more moves for the computer" ) ;
        }
        else {
            
            for( Move tempMove : allMoves ) {
                scratchBoard.makeMove( tempMove ) ;
                subTreeEval = getSubtreeEvaluation( 1, alpha, beta, scratchBoard ) ;
                
                if( subTreeEval > alpha ) {
                    alpha = subTreeEval ;
                    bestMove = tempMove ;
                }
                scratchBoard.undoLastMove() ;
            }

            if( bestMove == null ) {
                System.out.println( "Amazing... still null" ) ;
            }
        }

        return bestMove ;
    }

    /**
     * This function gets the subtree evaluation by using MINIMAX and AlphaBeta
     * pruning algorithms. When treeDepth % 2 == 0 its the computers turn to
     * play and when its 1 its the opponents turn to play.
     * 
     * <pre>
     *      function AlphaBetaMM( N, A, B) 
     *      # N = A Node in the game tree
     *      # A = value of alpha.
     *      # B = value of bets.
     *      begin
     *       if N is a leaf then
     *          return evaluateBoard ;
     *       if N is a Min node then
     *         For all successor Ni of N loop
     *           B = Min{ B, AlphaBetaMM(Ni, A, B)};
     *           if A >= B then
     *             Return A 
     *           fi
     *         end For
     *         Return Beta;
     *       else
     *         For each successor Ni of N loop
     *           A = Max{ A, AlphaBetaMM(Ni, A, B)};
     *           if A >= B then
     *             Return B
     *           fi 
     *         end For
     *         Return A;
     *      end AlphaBetaMM
     * </pre>
     * 
     * @param treeDepth The depth of tree to evaluate.
     * @param alpha The alpha value.
     * @param beta The beta value.
     */
    private double getSubtreeEvaluation( int treeDepth, double alpha,
                                         double beta, GameBoard scratchBoard ) {
        double eval = 0 ;
        boolean isMaxNode = false ;
        int perspective = Integer.MIN_VALUE ;
        List<Move> allMoves = null ;

        isMaxNode = ( ( treeDepth % 2 ) == 0 ) ? true : false ;
        perspective = ( isMaxNode ) ? currentPerspective : 
                                      scratchBoard.getEnemyPerspective( currentPerspective ) ;

        if( treeDepth > maxDepthOfSearch || !scratchBoard.isGameActive() ) {
            eval = evaluateGameBoard( currentPerspective, scratchBoard ) ;
        }
        else {
            
            allMoves = scratchBoard.getAllPossibleMoves( perspective ) ;
            Collections.sort( allMoves ) ;
            
            if( allMoves == null || allMoves.size() == 0 ) {
                eval = evaluateGameBoard( currentPerspective, scratchBoard ) ;
            }
            else {
                boolean evalSet = false ;

                if( isMaxNode ) {
                    
                    for( Move tempMove : allMoves ) {
                        scratchBoard.makeMove( tempMove ) ;
                        
                        alpha = Math.max(
                                alpha,
                                getSubtreeEvaluation( treeDepth + 1, 
                                                      alpha, beta, 
                                                      scratchBoard ) ) ;
                        scratchBoard.undoLastMove() ;

                        if( alpha >= beta ) {
                            eval = beta ;
                            evalSet = true ;
                            break ;
                        }
                    }

                    if( !evalSet ) {
                        eval = alpha ;
                    }
                }
                else {
                    for( Move tempMove : allMoves ) {
                        scratchBoard.makeMove( tempMove ) ;
                        
                        beta = Math.min(
                                beta,
                                getSubtreeEvaluation( treeDepth + 1, 
                                                      alpha, beta, 
                                                      scratchBoard ) ) ;
                        scratchBoard.undoLastMove() ;

                        if( alpha >= beta ) {
                            eval = alpha ;
                            evalSet = true ;
                            break ;
                        }
                    }

                    if( !evalSet ) {
                        eval = beta ;
                    }
                }
            }
        }

        return eval ;
    }

    private double evaluateGameBoard( int perspective, GameBoard scratchBoard ) {
        
        double eval = 0 ;
        
        for( EvaluationPolicy tempPolicy : evaluationPolicies ) {
            
            double evalVal = tempPolicy.getEvaluation( scratchBoard, perspective ) ;
            double evalWeight = tempPolicy.getEvaluationWeight() ;
            eval += evalVal * evalWeight ;
        }
        
        return eval ;
    }

    public void setMaxDepthOfSearch( int depth ) {
        this.maxDepthOfSearch = depth ;
    }

    public GameBoard getGameBoard() {
        return this.gameBoard ;
    }

    public void setGameBoard( GameBoard gameBoard ) {
        this.gameBoard = gameBoard ;
    }

    public void registerEvaluationPolicy( EvaluationPolicy policy ) {
        evaluationPolicies.add( policy ) ;
    }

    public void unRegisterEvaluationPolicy( EvaluationPolicy policy ) {
        evaluationPolicies.remove( policy ) ;
    }

    public int getCurrentPerspective() {
        return currentPerspective ;
    }

    public void setCurrentPerspective( int perspective ) {
        this.currentPerspective = perspective ;
    }
}
