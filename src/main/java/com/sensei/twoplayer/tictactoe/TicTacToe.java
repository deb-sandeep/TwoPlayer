package com.sensei.twoplayer.tictactoe;


import java.util.* ;
import java.awt.* ;
import java.applet.* ;

import com.sensei.twoplayer.core.* ;
import com.sensei.twoplayer.core.ui.* ;


class TicTacHumanPlayer extends HumanPlayer
{
    public TicTacHumanPlayer( GameBoard gameBoard, int perspective, GameBoardUI ui )
    {
        super( gameBoard, perspective, ui ) ;
    }    
    
    public void cellSelected( Position pos )
    {
        TicTacPosition position = ( TicTacPosition )pos ;
        if( position.perspective == TicTacToeBoard.EMPTY )
        {
            super.setNextMove( new TicTacMove( pos.row, pos.col, perspective ) ) ;
        }
    }
}


class TicTacComputerPlayer extends ComputerPlayer
{
    public TicTacComputerPlayer( GameBoard gameBoard, int perspective, 
                                 int maxDepth, Vector policies )
    {
        super( gameBoard, perspective, maxDepth, policies ) ;
    }
}


public class TicTacToe extends Applet
{
    TicTacToeBoard board = null ;
    TicTacToeBoardUI ui = null ;
    
    Vector policies = null ;
    
    Player computerPlayer = null ;
    Player computerPlayer1 = null ;
    Player humanPlayer = null ;
    
    public static final int MAX_DEPTH = 5 ;
    
    private boolean runAsApplication = false ;
        
    public TicTacToe()
    {
    }
    
    public void init() 
    {
        board = new TicTacToeBoard() ;
        ui = new TicTacToeBoardUI( board ) ;
        
        policies = new Vector() ;
        policies.addElement( new TicTacStrategy() ) ;
        
        computerPlayer = new TicTacComputerPlayer( board, TicTacToeBoard.CIRCLE, MAX_DEPTH, policies ) ;
        humanPlayer = new TicTacHumanPlayer( board, TicTacToeBoard.CROSS, ui ) ;
        
        if( !runAsApplication )
        {
            setLayout( new BorderLayout() ) ;
            add( ui ) ;
        }
        
        setBackground( Color.gray ) ;
    }
    
    
    public void start()
    {
        GameEngine gameEngine = new GameEngine( board, humanPlayer, computerPlayer ) ;
        gameEngine.start() ;
    }
    
    public void startGameApplication()
    {
        runAsApplication = true ;
        init() ;
        Frame frame = new Frame() ;
        frame.setLayout( new BorderLayout() ) ;
        frame.add( ui ) ;
        frame.setSize( 400, 400 ) ;
        frame.addWindowListener( new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent e)
            {
                System.exit( -1 ) ;
            }
        } ) ;
        frame.setVisible( true ) ;        
        start() ;
    }
    
    public static void main(String[] args)
    {
        new TicTacToe().startGameApplication() ;
    }
}
