package com.sensei.twoplayer.core ;

import java.util.ArrayList ;
import java.util.List ;
import java.util.Stack ;

/**
 * This class represents an abstract GameBoard for any multi-player, tabular,
 * strategy games likes Chess, Tic-Tac-Toe, Draught, Othello, Chinese-Checkers
 * etc. This class provides the following functionality to its sub-classes:
 * <ol>
 * <li>This game board assumes all the sub-classes ( specific game boards ) will
 * be rectangular (specific rows, columns). Since maximum of the board games are
 * rectangular in nature, this seems a fair assumptions. However, there are
 * games like Chinese-Checkers where the board is not rectangular but star
 * shaped. In these cases the sub-classes need to mask certain positions on the
 * board to get the game started.
 * 
 * <li>This game board has a two dimentional protected array
 * <code>positions</code> for keeping track of the positions on the board.
 * <code>Position</code> is again a generic concept which specific games should
 * implement.
 * 
 * <li>This game board keeps track of all the moves made on this board, in a
 * LIFO manner.
 * 
 * <li>This game board provides funtionality for registering move listeners.
 * Move Listeners are parties which are interested in a notification, whenever a
 * move is made on the game board.
 * </ol>
 */
public abstract class GameBoard {
    
    protected int          numRows       = 0 ;
    protected int          numCols       = 0 ;

    protected Position[][] positions     = null ;
    protected Stack<Move>  moves         = new Stack<Move>() ;

    protected List<GameBoardMoveListener> moveListeners = null ;

    protected GameBoard() {
    }

    /**
     * The constructor which takes the number of rows and columns in the game
     * board. This method calls back an abstract method
     * <code>initializeBoard</code>, which should be implemented by the
     * sub-classes to provide logic for initializing the gameboard to an initial
     * or a predefined state.
     */
    protected GameBoard( int numRows, int numCols ) {
        this.numRows = numRows ;
        this.numCols = numCols ;
        positions = new Position[numRows][numCols] ;
        moveListeners = new ArrayList<GameBoardMoveListener>() ;

        // Give a chance to the sub-classes for initializing the gameboard to an
        // initial or predefined state.
        initializeBoard() ;
    }

    /**
     * This is an abstract call back method, which is invoked during the board
     * construction time (constructor). This method should be implemented by the
     * subclasses to initialize the game board to a predefined or an initial
     * state.
     */
    public abstract void initializeBoard() ;

    /**
     * This abstract method should be implemented by the sub-classes to provide
     * logic to return all the possible moves for a given perspective at the
     * current state of the board.
     * 
     * @param perspective
     *            The perspective in the board. Perspective can be thought of as
     *            a side in the game. This is a game dependent concept, for
     *            example in Tic-Tac-Toe perspective can mean either CROSS or
     *            CIRCLE, in Chess it may mean either BLACK or WHITE.. etc.
     */
    public abstract List<Move> getAllPossibleMoves( int perspective ) ;

    /**
     * Make a move on the game board. This method, calls back a method on the
     * sub-classes <code>updateGameBoard</code>, which should be implemented the
     * specific logic of making a move, also this method informs all the move
     * listeners of the following events.
     * <ul>
     * <li>A move is made.
     * <li>If by making this move, the game is complete.. the listeners are
     * informed of the gameOver event.
     * </ul>
     * 
     * @param move The game specific move.
     */
    public void makeMove( Move move ) {
        updateGameBoard( move ) ;
        
        if( !moveListeners.isEmpty() ) {
            
            boolean gameOver = !isGameActive() ;
            
            for( GameBoardMoveListener element : moveListeners ) {
                element.moveMade( move ) ;
                if( gameOver ) {
                    String message = getEndingMessage() ;
                    element.gameOver( message ) ;
                }
            }
        }
    }

    /**
     * This abstract method is called when a move is made on the board.
     * Sub-classes should provide game specific implementations for updating the
     * game board. For example in case of Tic-Tac-Toe, a move would mean
     * updating the position with the perspective, whereas in case of Chess it
     * would be more complex as we have to look out for capture moves.
     */
    public abstract void updateGameBoard( Move move ) ;

    /**
     * This function should undo the last move made. The past moves are
     * available in a Stack named <code>moves</code>.
     */
    public abstract void undoLastMove() ;

    /**
     * This function should analyze the given move and return whether the move
     * is a valid move or not.
     */
    public abstract boolean isValidMove( Move move ) ;

    /**
     * This function should return the opponent perspective for the given
     * currentPerspective.
     */
    public abstract int getEnemyPerspective( int currentPerspective ) ;

    /**
     * This function should return whether the game is active.
     */
    public abstract boolean isGameActive() ;

    /**
     * This function should be implemented by the subclasses to return a <b>DEEP
     * COPY</code> of the gameboard in the current state.
     */
    public abstract GameBoard getScratchBoard() ;

    public abstract void printBoard() ;

    public abstract String getEndingMessage() ;

    public void addMoveListener( GameBoardMoveListener moveListener ) {
        moveListeners.add( moveListener ) ;
    }

    public void removeMoveListener( GameBoardMoveListener moveListener ) {
        moveListeners.remove( moveListener ) ;
    }

    public int getNumRows() {
        return this.numRows ;
    }

    public int getNumCols() {
        return this.numCols ;
    }

    public Position getPosition( int row, int col ) {
        return positions[row][col] ;
    }
}
