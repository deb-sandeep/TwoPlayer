package com.sensei.twoplayer.core ;

import com.sensei.twoplayer.core.ui.* ;

public abstract class HumanPlayer extends Player 
    implements GameBoardInteractionListener {
    
    protected GameBoardUI ui = null ;

    public HumanPlayer( GameBoard gameBoard, int perspective, GameBoardUI ui ) {
        
        super( gameBoard, perspective ) ;
        this.ui = ui ;
        ui.addInteractionListener( this ) ;
    }
}
