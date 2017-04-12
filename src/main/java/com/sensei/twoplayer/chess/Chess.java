package com.sensei.twoplayer.chess ;

import java.applet.Applet ;
import java.awt.BorderLayout ;
import java.awt.Button ;
import java.awt.Color ;
import java.awt.Frame ;
import java.awt.Panel ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.util.ArrayList ;
import java.util.List ;

import com.sensei.twoplayer.chess.strategy.CenterBoardCaptureStrategy ;
import com.sensei.twoplayer.chess.strategy.EncroachmentStrategy ;
import com.sensei.twoplayer.chess.strategy.MaterialStrategy ;
import com.sensei.twoplayer.chess.strategy.MobilityStrategy ;
import com.sensei.twoplayer.core.ComputerPlayer ;
import com.sensei.twoplayer.core.EvaluationPolicy ;
import com.sensei.twoplayer.core.GameBoard ;
import com.sensei.twoplayer.core.GameEngine ;
import com.sensei.twoplayer.core.Player ;
import com.sensei.twoplayer.core.Position ;
import com.sensei.twoplayer.core.ui.GameBoardUI ;
import com.sensei.twoplayer.core.ui.MessageDialog ;

class ChessHumanPlayer extends com.sensei.twoplayer.core.HumanPlayer {
    
    boolean       firstMoveSelected = false ;
    ChessPosition firstPosition     = null ;

    public ChessHumanPlayer( GameBoard gameBoard, 
                             int perspective, 
                             GameBoardUI ui ) {
        super( gameBoard, perspective, ui ) ;
    }

    public void cellSelected( Position pos ) {
        
        if( !gameBoard.isGameActive() ) {
            MessageDialog.showMessageDialog( "Game is not active" ) ;
            return ;
        }

        ChessPosition position = (ChessPosition) pos ;

        if( !firstMoveSelected ) {
            if( position.pieceColor != perspective ) {
                MessageDialog.showMessageDialog( "Can't move ememy piece" ) ;
                return ;
            }
            firstPosition = position ;
            ((ChessBoardUI)ui).selectedPosition = firstPosition ;
            firstMoveSelected = true ;
        }
        else {
            ChessMove move = new ChessMove( firstPosition, position ) ;
            if( gameBoard.isValidMove( move ) ) {
                super.setNextMove( move ) ;
            }
            else {
                MessageDialog.showMessageDialog( "Please enter a valid move" ) ;
            }
            firstMoveSelected = false ;
            ( (ChessBoardUI) ui ).selectedPosition = null ;
        }
        ui.repaintPosition( pos ) ;
    }
}

class ChessComputerPlayer extends ComputerPlayer {
    
    public ChessComputerPlayer( GameBoard gameBoard, int perspective,
                                int maxDepth, 
                                List<EvaluationPolicy> policies ) {
        super( gameBoard, perspective, maxDepth, policies ) ;
    }
}

@SuppressWarnings( "serial" )
public class Chess extends Applet implements ActionListener {
    
    ChessBoard                  board            = null ;
    ChessBoardUI                ui               = null ;
    Panel                       btnPanel         = null ;
    Frame                       applicationFrame = null ;

    List<EvaluationPolicy>      policies         = null ;

    Player                      computerPlayer   = null ;
    Player                      humanPlayer      = null ;

    public static final int     MAX_DEPTH        = 3 ;

    private boolean             runAsApplication = false ;

    private static final String GAME_OVER        = "GameOver" ;
    private static final String NEW_GAME         = "NewGame" ;

    public void actionPerformed( ActionEvent e ) {
        
        String actionCommand = e.getActionCommand() ;
        
        if( actionCommand.equals( NEW_GAME ) ) {
            
            NewGameDialog dialog = new NewGameDialog( ui ) ;
            dialog.setModal( true ) ;
            dialog.setVisible( true ) ;

            if( dialog.getUserIntention() != NewGameDialog.CANCEL ) {
                int userColor = dialog.getUserColor() ;
                int difficulty = dialog.getDifficultyLeve() ;

                startGame( userColor, difficulty ) ;
            }
        }
        else if( actionCommand.equals( GAME_OVER ) ) {
            board.setGameActive( false, "Game terminated by user" ) ;
        }
    }

    private void startGame( int userColor, int difficulty ) {
        
        int computerColor = ( userColor == ChessBoard.WHITE ) ? 
                            ChessBoard.BLACK : ChessBoard.WHITE ;

        if( runAsApplication ) {
            applicationFrame.remove( ui ) ;
        }
        else {
            remove( ui ) ;
        }

        board.setGameActive( false, "Closing this game for a new game" ) ;
        board = new ChessBoard( computerColor ) ;
        ui = new ChessBoardUI( board ) ;

        computerPlayer = new ChessComputerPlayer( board, computerColor,
                                                  difficulty, policies ) ;
        humanPlayer = new ChessHumanPlayer( board, userColor, ui ) ;

        if( !runAsApplication ) {
            add( ui, "Center" ) ;
            validate() ;
        }
        else {
            applicationFrame.add( ui, "Center" ) ;
            applicationFrame.validate() ;
        }

        GameEngine gameEngine = null ;

        if( userColor == ChessBoard.WHITE ) {
            gameEngine = new GameEngine( board, humanPlayer, computerPlayer ) ;
        }
        else {
            gameEngine = new GameEngine( board, computerPlayer, humanPlayer ) ;
        }
        board.setGameActive( true, "Game active" ) ;
        gameEngine.start() ;
    }

    public void init() {
        
        board = new ChessBoard( ChessBoard.BLACK ) ;
        ui = new ChessBoardUI( board ) ;

        btnPanel = new Panel() ;
        btnPanel.setBackground( Color.gray.brighter() ) ;
        
        Button gameOverBtn = new Button( "End game" ) ;
        gameOverBtn.addActionListener( this ) ;
        gameOverBtn.setActionCommand( GAME_OVER ) ;
        
        Button newGameBtn = new Button( "New game" ) ;
        newGameBtn.addActionListener( this ) ;
        newGameBtn.setActionCommand( NEW_GAME ) ;

        btnPanel.add( gameOverBtn ) ;
        btnPanel.add( newGameBtn ) ;

        policies = new ArrayList<EvaluationPolicy>() ;
        policies.add( new MaterialStrategy( 100 ) ) ;
        policies.add( new MobilityStrategy( 2 ) ) ;
        policies.add( new EncroachmentStrategy( 6 ) ) ;
        policies.add( new CenterBoardCaptureStrategy( 10 ) ) ;

        computerPlayer = new ChessComputerPlayer( board, ChessBoard.BLACK,
                                                  MAX_DEPTH, policies ) ;
        humanPlayer = new ChessHumanPlayer( board, ChessBoard.WHITE, ui ) ;

        if( !runAsApplication ) {
            
            setLayout( new BorderLayout() ) ;
            add( ui, "Center" ) ;
            add( btnPanel, "South" ) ;
        }
    }

    public void start() {
        GameEngine gameEngine = new GameEngine( board, humanPlayer,
                computerPlayer ) ;
        gameEngine.start() ;
    }

    public void startGameApplication() {
        
        runAsApplication = true ;
        init() ;
        applicationFrame = new Frame() ;
        applicationFrame.setLayout( new BorderLayout() ) ;
        applicationFrame.setLocationRelativeTo( null );
        applicationFrame.add( ui, "Center" ) ;
        applicationFrame.add( btnPanel, "South" ) ;
        applicationFrame.setSize( 750, 800 ) ;
        applicationFrame.addWindowListener( new java.awt.event.WindowAdapter() {
            public void windowClosing( java.awt.event.WindowEvent e ) {
                System.exit( -1 ) ;
            }
        } ) ;
        applicationFrame.setVisible( true ) ;
    }

    public static void main( String[] args ) {
        new Chess().startGameApplication() ;
    }
}
