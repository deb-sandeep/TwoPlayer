package com.sensei.twoplayer.core ;

/**
 * This class drives the execution of the game. This class runs in a separate
 * thread and co-ordinates the players who play the game.
 */
public class GameEngine extends Thread {
    
    private GameBoard gameBoard = null ;
    private Player    player1   = null ;
    private Player    player2   = null ;

    public GameEngine( GameBoard gameBoard, Player player1, Player player2 ) {
        this.gameBoard = gameBoard ;
        this.player1 = player1 ;
        this.player2 = player2 ;
    }

    public void run() {
        Move movePlayer1 = null ;
        Move movePlayer2 = null ;

        do {
            try {
                movePlayer1 = player1.getMove() ;
                gameBoard.makeMove( movePlayer1 ) ;

                Thread.sleep( 100 ) ;

                if( gameBoard.isGameActive() ) {
                    movePlayer2 = player2.getMove() ;
                    gameBoard.makeMove( movePlayer2 ) ;
                }

                Thread.sleep( 100 ) ;
            }
            catch( InterruptedException e ) {
            }
        }
        while( gameBoard.isGameActive() ) ;
    }
}
