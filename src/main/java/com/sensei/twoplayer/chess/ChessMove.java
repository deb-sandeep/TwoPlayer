package com.sensei.twoplayer.chess ;

import com.sensei.twoplayer.core.* ;

public class ChessMove extends Move {
    
    public int srcPiece       = -1 ;
    public int destPiece      = -1 ;
    public int srcCellColor   = -1 ;
    public int destCellColor  = -1 ;
    public int srcPieceColor  = -1 ;
    public int destPieceColor = -1 ;
    public int srcRow, srcCol = -1 ;
    public int destRow, destCol = -1 ;

    public ChessMove( ChessPosition srcCell, ChessPosition destCell ) {
        
        srcPiece = srcCell.piece ;
        destPiece = destCell.piece ;

        srcCellColor = srcCell.cellColor ;
        destCellColor = destCell.cellColor ;

        srcPieceColor = srcCell.pieceColor ;
        destPieceColor = destCell.pieceColor ;

        srcRow = srcCell.row ;
        srcCol = srcCell.col ;
        destRow = destCell.row ;
        destCol = destCell.col ;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer() ;

        buffer.append( "Move "
                + ( ( destPiece != ChessBoard.EMPTY ) ? "X" : " " ) + " Src["
                + srcRow + "," + srcCol + "] Piece = "
                + DU.getPieceStr( srcPiece ) + " | Dst[" + destRow + ","
                + destCol + "] Piece = " + DU.getPieceStr( destPiece ) ) ;

        return buffer.toString() ;
    }

    // This function decides, how this move is better than the move supplied to
    // it as an argument. This will help improve the games performance, as all the
    // better moves will be analyzed first.
    public int compareTo( Move aMove ) {
        
        ChessMove mv = (ChessMove) aMove ;

        if( ChessBoard.getMaterialWeight( destPiece ) > 
            ChessBoard.getMaterialWeight( mv.destPiece ) ) {
            return -1 ;
        }
        else if( ChessBoard.getMaterialWeight( destPiece ) < 
                 ChessBoard.getMaterialWeight( mv.destPiece ) ) { 
            return 1 ; 
        }

        return 0 ;
    }
}
