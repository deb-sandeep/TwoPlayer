package com.sensei.twoplayer.tictactoe ;

import java.util.* ;

import com.sensei.twoplayer.core.* ;

class TicTacPosition extends Position {
    public int perspective = 0 ;

    public TicTacPosition( int row, int col, int pers ) {
        this.row = row ;
        this.col = col ;
        this.perspective = pers ;
    }

    public String toString() {
        String retVal = "" ;
        if( perspective == TicTacToeBoard.EMPTY ) {
            retVal = "-" ;
        }
        else if( perspective == TicTacToeBoard.CIRCLE ) {
            retVal = "O" ;
        }
        else if( perspective == TicTacToeBoard.CROSS ) {
            retVal = "X" ;
        }
        return retVal ;
    }

    public Object clone() {
        TicTacPosition position = new TicTacPosition( row, col, perspective ) ;
        return position ;
    }
}

public class TicTacToeBoard extends GameBoard {
    private String           endingMessage = null ;

    public static final int  EMPTY         = 0 ;
    public static final int  CIRCLE        = 1 ;
    public static final int  CROSS         = 2 ;

    private static final int NUM_ROWS      = 3 ;
    private static final int NUM_COLS      = 3 ;

    public TicTacToeBoard() {
        super( NUM_ROWS, NUM_COLS ) ;
    }

    public void initializeBoard() {
        for( int row = 0 ; row < NUM_ROWS ; row++ ) {
            for( int col = 0 ; col < NUM_COLS ; col++ ) {
                positions[row][col] = new TicTacPosition( row, col, EMPTY ) ;
            }
        }
    }

    public void initializeBoard( GameBoard gameBoard ) {
        for( int row = 0 ; row < NUM_ROWS ; row++ ) {
            for( int col = 0 ; col < NUM_COLS ; col++ ) {
                TicTacPosition p = (TicTacPosition) gameBoard.getPosition( row, col ) ;
                ( (TicTacPosition) positions[row][col] ).perspective = p.perspective ;
            }
        }
    }

    public List<Move> getAllPossibleMoves( int perspective ) {
        
        List<Move> possibleMoves = new ArrayList<Move>() ;
        if( isGameActive() ) {
            for( int row = 0 ; row < NUM_ROWS ; row++ ) {
                for( int col = 0 ; col < NUM_COLS ; col++ ) {
                    TicTacPosition pos = (TicTacPosition) positions[row][col] ;
                    if( pos.perspective == EMPTY ) {
                        possibleMoves.add( new TicTacMove( row, col, perspective ) ) ;
                    }
                }
            }
        }

        return possibleMoves ;
    }

    public void updateGameBoard( Move move ) {
        TicTacMove mv = (TicTacMove) move ;
        TicTacPosition pos = (TicTacPosition) positions[mv.row][mv.col] ;

        pos.perspective = mv.perspective ;
        moves.push( mv ) ;
    }

    public void undoLastMove() {
        if( !moves.empty() ) {
            TicTacMove mv = (TicTacMove) moves.pop() ;
            TicTacPosition pos = (TicTacPosition) positions[mv.row][mv.col] ;
            pos.perspective = EMPTY ;
        }
    }

    public boolean isValidMove( Move move ) {
        boolean retVal = false ;

        TicTacMove mv = (TicTacMove) move ;
        TicTacPosition pos = (TicTacPosition) positions[mv.row][mv.col] ;
        if( pos.perspective == EMPTY ) {
            retVal = true ;
        }

        return retVal ;
    }

    public int getEnemyPerspective( int currentPerspective ) {
        return ( currentPerspective == CROSS ) ? CIRCLE : CROSS ;
    }

    public boolean isGameActive() {
        boolean boardFull = true ;
        boolean lineCircle = isLineMade( CIRCLE ) ;
        boolean lineCross = isLineMade( CROSS ) ;

        if( lineCircle ) {
            endingMessage = "CIRCLE wins the game" ;
            return false ;
        }

        if( lineCross ) {
            endingMessage = "CROSS wins the game" ;
            return false ;
        }

        for( int row = 0 ; row < NUM_ROWS ; row++ ) {
            for( int col = 0 ; col < NUM_COLS ; col++ ) {
                TicTacPosition p = (TicTacPosition) getPosition( row, col ) ;
                if( p.perspective == TicTacToeBoard.EMPTY ) {
                    boardFull = false ;
                    break ;
                }
            }

            if( boardFull == false ) {
                break ;
            }
        }
        if( boardFull == true ) {
            endingMessage = "NO more moves possible" ;
            return false ;
        }

        return true ;
    }

    boolean isLineMade( int perspective ) {
        int persArr[][] = new int[NUM_ROWS][NUM_COLS] ;
        boolean lineMade = false ;

        for( int row = 0 ; row < NUM_ROWS ; row++ ) {
            for( int col = 0 ; col < NUM_COLS ; col++ ) {
                TicTacPosition p = (TicTacPosition) getPosition( row, col ) ;
                persArr[row][col] = p.perspective ;
            }
        }

        for( int row = 0 ; row < NUM_ROWS ; row++ ) {
            lineMade = true ;
            for( int col = 0 ; col < NUM_COLS ; col++ ) {
                if( persArr[row][col] != perspective ) {
                    lineMade = false ;
                    break ;
                }
            }

            if( lineMade ) { return true ; }
        }

        for( int col = 0 ; col < NUM_COLS ; col++ ) {
            lineMade = true ;
            for( int row = 0 ; row < NUM_ROWS ; row++ ) {
                if( persArr[row][col] != perspective ) {
                    lineMade = false ;
                    break ;
                }
            }

            if( lineMade ) { return true ; }
        }

        lineMade = true ;
        for( int row = 0 ; row < NUM_ROWS ; row++ ) {
            if( persArr[row][row] != perspective ) {
                lineMade = false ;
                break ;
            }
        }
        if( lineMade ) { return true ; }

        lineMade = true ;
        for( int row = 0 ; row < NUM_ROWS ; row++ ) {
            if( persArr[row][NUM_ROWS - 1 - row] != perspective ) {
                lineMade = false ;
                break ;
            }
        }
        if( lineMade ) { return true ; }

        return lineMade ;
    }

    public GameBoard getScratchBoard() {
        TicTacToeBoard gameBoard = new TicTacToeBoard() ;

        gameBoard.initializeBoard( this ) ;
        return gameBoard ;
    }

    public void printBoard() {
        for( int row = 0 ; row < NUM_ROWS ; row++ ) {
            for( int col = 0 ; col < NUM_COLS ; col++ ) {
                Position p = (Position) getPosition( 2 - row, col ) ;
                System.out.print( p ) ;
            }
            System.out.println() ;
        }
    }

    public String getEndingMessage() {
        return endingMessage ;
    }
}
