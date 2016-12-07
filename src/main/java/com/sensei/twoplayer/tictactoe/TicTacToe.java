package com.sensei.twoplayer.tictactoe ;

import java.applet.Applet ;
import java.awt.BorderLayout ;
import java.awt.Color ;
import java.awt.Frame ;
import java.util.ArrayList ;
import java.util.List ;

import com.sensei.twoplayer.core.ComputerPlayer ;
import com.sensei.twoplayer.core.EvaluationPolicy ;
import com.sensei.twoplayer.core.GameBoard ;
import com.sensei.twoplayer.core.GameEngine ;
import com.sensei.twoplayer.core.HumanPlayer ;
import com.sensei.twoplayer.core.Player ;
import com.sensei.twoplayer.core.Position ;
import com.sensei.twoplayer.core.ui.GameBoardUI ;

class TicTacHumanPlayer extends HumanPlayer {
    public TicTacHumanPlayer( GameBoard gameBoard, 
                              int perspective,
                              GameBoardUI ui ) {
        super( gameBoard, perspective, ui ) ;
    }

    public void cellSelected( Position pos ) {
        TicTacPosition position = (TicTacPosition) pos ;
        if( position.perspective == TicTacToeBoard.EMPTY ) {
            super.setNextMove( new TicTacMove( pos.row, pos.col, perspective ) ) ;
        }
    }
}

class TicTacComputerPlayer extends ComputerPlayer {
    public TicTacComputerPlayer( GameBoard gameBoard, int perspective,
                                 int maxDepth, List<EvaluationPolicy> policies ) {
        super( gameBoard, perspective, maxDepth, policies ) ;
    }
}

@SuppressWarnings( "serial" )
public class TicTacToe extends Applet {
    
    TicTacToeBoard          board            = null ;
    TicTacToeBoardUI        ui               = null ;

    List<EvaluationPolicy>  policies         = null ;

    Player                  computerPlayer   = null ;
    Player                  computerPlayer1  = null ;
    Player                  humanPlayer      = null ;

    public static final int MAX_DEPTH        = 5 ;

    private boolean         runAsApplication = false ;

    public TicTacToe() {
    }

    public void init() {
        board = new TicTacToeBoard() ;
        ui = new TicTacToeBoardUI( board ) ;

        policies = new ArrayList<EvaluationPolicy>() ;
        policies.add( new TicTacStrategy() ) ;

        computerPlayer = new TicTacComputerPlayer( board,
                                                   TicTacToeBoard.CIRCLE, 
                                                   MAX_DEPTH, policies ) ;
        humanPlayer = new TicTacHumanPlayer( board, TicTacToeBoard.CROSS, ui ) ;

        if( !runAsApplication ) {
            setLayout( new BorderLayout() ) ;
            add( ui ) ;
        }

        setBackground( Color.gray ) ;
    }

    public void start() {
        GameEngine gameEngine = new GameEngine( board, humanPlayer, computerPlayer ) ;
        gameEngine.start() ;
    }

    public void startGameApplication() {
        runAsApplication = true ;
        init() ;
        Frame frame = new Frame() ;
        frame.setLayout( new BorderLayout() ) ;
        frame.add( ui ) ;
        frame.setSize( 400, 400 ) ;
        frame.addWindowListener( new java.awt.event.WindowAdapter() {
            public void windowClosing( java.awt.event.WindowEvent e ) {
                System.exit( -1 ) ;
            }
        } ) ;
        frame.setVisible( true ) ;
        start() ;
    }

    public static void main( String[] args ) {
        new TicTacToe().startGameApplication() ;
    }
}
