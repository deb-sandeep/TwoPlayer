package com.sensei.twoplayer.chess ;

import com.sensei.twoplayer.core.* ;

public class ChessPosition extends Position
{
	public int pieceColor ;
	public int cellColor ;
	public int piece ;
	
	public ChessPosition( int piece, int pieceColor, int cellColor, int row, int col )
	{
		this.row = row ;
		this.col = col ;
		this.piece = piece ;
		this.pieceColor = pieceColor ;
		this.cellColor = cellColor ;
	}
}
