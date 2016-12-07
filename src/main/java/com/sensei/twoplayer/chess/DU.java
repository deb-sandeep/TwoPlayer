package com.sensei.twoplayer.chess;


// This is a debug utilities class... can be removed.
public class DU
{
    public static String getPieceStr( int piece )
    {
        switch( piece )
        {
            case ChessBoard.PAWN:
                return "Pawn" ;
            case ChessBoard.ROOK:
                return "Rook" ;
            case ChessBoard.KNIGHT:
                return "Knight" ;
            case ChessBoard.BISHOP:
                return "Bishop" ;
            case ChessBoard.QUEEN:
                return "Queen" ;
            case ChessBoard.KING:
                return "King" ;
        } ;
        return "Empty" ;                
    }
    
    
    public static char getPieceCh( int piece )
    {
        switch( piece )
        {
            case ChessBoard.PAWN:
                return 'p' ;
            case ChessBoard.ROOK:
                return 'r' ;
            case ChessBoard.KNIGHT:
                return 'k' ;
            case ChessBoard.BISHOP:
                return 'b' ;
            case ChessBoard.QUEEN:
                return 'Q' ;
            case ChessBoard.KING:
                return 'K' ;
        } ;
        return ' ' ;                
    }
    
    
    public static String getColor( int color )
    {
        if( color == ChessBoard.BLACK )
        {
            return "Black" ;
        }
        else
        {
            return "White" ;
        }
    }
}
