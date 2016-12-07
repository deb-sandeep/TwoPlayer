package com.sensei.twoplayer.chess ;

import java.io.BufferedReader ;
import java.io.FileReader ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.StringTokenizer ;

import com.sensei.twoplayer.core.GameBoard ;
import com.sensei.twoplayer.core.GameBoardMoveListener ;
import com.sensei.twoplayer.core.Move ;

public class ChessBoard extends GameBoard {
    
    public static final int    BLACK          = 1 ;
    public static final int    WHITE          = 0 ;

    public static final int    PAWN           = 2 ;
    public static final int    ROOK           = 3 ;
    public static final int    BISHOP         = 4 ;
    public static final int    KNIGHT         = 5 ;
    public static final int    QUEEN          = 6 ;
    public static final int    KING           = 7 ;
    public static final int    EMPTY          = 8 ;

    private int                topColor       = EMPTY ;

    private static final int   NUM_ROWS       = 8 ;
    private static final int   NUM_COLS       = 8 ;

    public static final double PAWN_WT        = 10 ;
    public static final double BISHOP_WT      = 50 ;
    public static final double KNIGHT_WT      = 100 ;
    public static final double ROOK_WT        = 200 ;
    public static final double QUEEN_WT       = 900 ;
    public static final double KING_WT        = 10000 ;

    private int[][][]          pieceLocations = new int[2][NUM_ROWS][NUM_COLS] ;

    private boolean            gameActive     = false ;
    private String             endingMessage  = "Test" ;

    private ChessBoard( ChessBoard chessBoard ) {
        
        this.numRows = NUM_ROWS ;
        this.numCols = NUM_COLS ;
        positions = new ChessPosition[numRows][numCols] ;
        moveListeners = new ArrayList<GameBoardMoveListener>() ;
        for( int row = 0 ; row < NUM_ROWS ; row++ ) {
            for( int col = 0 ; col < NUM_COLS ; col++ ) {
                ChessPosition pos = null ;
                pos = (ChessPosition) chessBoard.getPosition( row, col ) ;
                positions[row][col] = new ChessPosition( pos.piece,
                                                         pos.pieceColor, 
                                                         pos.cellColor, 
                                                         row, col ) ;
            }
        }

        this.topColor = chessBoard.topColor ;
        for( int i = 0 ; i < 2 ; i++ ) {
            for( int row = 0 ; row < NUM_ROWS ; row++ ) {
                for( int col = 0 ; col < NUM_COLS ; col++ ) {
                    ChessPosition pos = null ;
                    pos = (ChessPosition) chessBoard.getPosition( row, col ) ;
                    if( pos.piece != EMPTY ) {
                        if( pos.pieceColor == i ) {
                            pieceLocations[i][row][col] = 1 ;
                        }
                    }
                }
            }
        }
        gameActive = chessBoard.gameActive ;
    }

    public ChessBoard( int topColor ) {
        super( NUM_ROWS, NUM_COLS ) ;
        initializeBoard( topColor ) ;
        this.topColor = topColor ;
    }

    public ChessBoard( int topColor, String initFile ) {
        super( NUM_ROWS, NUM_COLS ) ;
        this.topColor = topColor ;
        initializeBoard( initFile ) ;
    }

    public int getEnemyPerspective( int currPers ) {
        return ( currPers == BLACK ) ? WHITE : BLACK ;
    }

    public boolean isGameActive() {
        return gameActive ;
    }

    public void updateGameBoard( Move move ) {
        
        ChessMove mv = (ChessMove) move ;
        ChessPosition sourceCell = null ;
        ChessPosition destCell   = null ;
        
        sourceCell = (ChessPosition) getPosition( mv.srcRow, mv.srcCol ) ;
        destCell   = (ChessPosition) getPosition( mv.destRow, mv.destCol ) ;

        // Update the positional index for the source pieces.
        // Its guaranteed that the source piece will always have a piece.
        int[][] srcPcLocn = pieceLocations[mv.srcPieceColor] ;
        int[][] dstPcLocn = pieceLocations[getEnemyPerspective( mv.srcPieceColor )] ;

        srcPcLocn[mv.destRow][mv.destCol] = 1 ;
        srcPcLocn[mv.srcRow][mv.srcCol] = 0 ;

        if( mv.destPiece != EMPTY ) {
            dstPcLocn[mv.destRow][mv.destCol] = 0 ;
            if( mv.destPiece == KING ) {
                gameActive = false ;
                if( mv.destPieceColor == BLACK ) {
                    endingMessage = "White wins the game" ;
                }
                else {
                    endingMessage = "Black wins the game" ;
                }
            }
        }

        destCell.piece = sourceCell.piece ;
        destCell.pieceColor = sourceCell.pieceColor ;

        // Queen promotion
        if( mv.srcPiece == PAWN ) {
            int factorForColor = ( mv.srcPieceColor == topColor ) ? 1 : -1 ;
            if( factorForColor == 1 && mv.destRow == 7 ) {
                destCell.piece = QUEEN ;
            }
            else if( factorForColor == -1 && mv.destRow == 0 ) {
                destCell.piece = QUEEN ;
            }
        }

        sourceCell.piece = EMPTY ;
        sourceCell.pieceColor = EMPTY ;

        moves.push( mv ) ;
    }

    public void undoLastMove() {
        
        if( !moves.empty() ) {
            
            ChessMove mv = (ChessMove) moves.pop() ;
            ChessPosition sourceCell = null ;
            ChessPosition destCell = null ;
            
            sourceCell = (ChessPosition) getPosition( mv.srcRow, mv.srcCol ) ;
            destCell = (ChessPosition) getPosition( mv.destRow, mv.destCol ) ;

            sourceCell.piece = mv.srcPiece ;
            sourceCell.pieceColor = mv.srcPieceColor ;

            destCell.piece = mv.destPiece ;
            destCell.pieceColor = mv.destPieceColor ;

            if( mv.destPiece == KING ) {
                gameActive = true ;
                endingMessage = "Test" ;
            }

            // Update the positional index for the source pieces.
            // Its guaranteed that the source piece will always have a piece.
            int[][] srcPcLocn = pieceLocations[mv.srcPieceColor] ;
            int[][] dstPcLocn = pieceLocations[getEnemyPerspective( mv.srcPieceColor )] ;

            srcPcLocn[mv.destRow][mv.destCol] = 0 ;
            srcPcLocn[mv.srcRow][mv.srcCol] = 1 ;

            if( mv.destPiece != EMPTY ) {
                dstPcLocn[mv.destRow][mv.destCol] = 1 ;
            }
        }
    }

    private void initializeBoard( int topColor ) {
        int boardData[] = { ROOK, KNIGHT, BISHOP, 
                            QUEEN, KING, 
                            BISHOP, KNIGHT, ROOK } ;

        int opponentColor = getEnemyPerspective( topColor ) ;

        for( int col = 0 ; col < 8 ; col++ ) {
            
            positions[0][col] = new ChessPosition( boardData[col], topColor, col % 2, 0, col ) ;
            positions[1][col] = new ChessPosition( PAWN, topColor, ( 1 + col ) % 2, 1, col ) ;
            
            pieceLocations[topColor][0][col] = 1 ;
            pieceLocations[topColor][1][col] = 1 ;

            positions[7][col] = new ChessPosition( boardData[col], opponentColor, ( 7 + col ) % 2, 7, col ) ;
            positions[6][col] = new ChessPosition( PAWN, opponentColor, ( 6 + col ) % 2, 6, col ) ;
            
            pieceLocations[opponentColor][7][col] = 1 ;
            pieceLocations[opponentColor][6][col] = 1 ;

            for( int row = 2 ; row < 6 ; row++ ) {
                positions[row][col] = new ChessPosition( EMPTY, EMPTY,
                        ( row + col ) % 2, row, col ) ;
            }
        }
    }

    public void initializeBoard() {
    }

    public List<Move> getAllPossibleMoves( int perspective ) {

        List<Move> allMoves = new ArrayList<Move>() ;
        int[][] locn = pieceLocations[perspective] ;

        for( int row = 0 ; row < 8 ; row++ ) {
            for( int col = 0 ; col < 8 ; col++ ) {
                if( locn[row][col] == 1 ) {
                    allMoves.addAll( getPossibleMoves( (ChessPosition) positions[row][col] ) ) ;
                }
            }
        }
        return allMoves ;
    }

    private List<Move> getPossibleMoves( ChessPosition seedCell ) {
        
        List<Move> possibleMoves = new ArrayList<Move>() ;

        int cellRow = seedCell.row ;
        int cellCol = seedCell.col ;

        // yStepDir determines the direction of scan. If the pieceColor is
        // for the side which is in the bottom part of the board, then the scan
        // is upward, else it is downwards ( -1 ).
        int yStepDir = ( seedCell.pieceColor == topColor ) ? 1 : -1 ;
        
        int[][] ownLocation = pieceLocations[seedCell.pieceColor] ;
        int[][] opponentLocation = pieceLocations[getEnemyPerspective( seedCell.pieceColor )] ;

        // If there is no piece in the cell, there are no moves.
        if( seedCell.piece == EMPTY ) return possibleMoves ;

        switch( seedCell.piece ){
            case PAWN:
                return getMovesForPawn( cellRow, cellCol,
                                        yStepDir, 
                                        ownLocation, opponentLocation ) ;
            case KNIGHT:
                return getMovesForKnight( cellRow, cellCol, ownLocation ) ;

            case KING:
                return getMovesForKing( cellRow, cellCol, ownLocation ) ;
                
            // Compute the possible moves if the piece at the seed cell location
            // is a ROOK
            case ROOK:
            case BISHOP:
            case QUEEN: {
                boolean bForQueen = false ;
                switch( seedCell.piece ){
                    case QUEEN:
                        bForQueen = true ;

                    case ROOK: {
                        int limit = 0 ;
                        // Get the possible moves in the same row.
                        for( int incr = -1 ; incr <= 1 ; incr += 2 ) {
                            // The above is to toggle incr for the two loops,
                            // once its -1 and then 1.
                            limit = Math.max( -1, 8 * incr ) ;
                            for( int col = cellCol + incr ; col != limit ; col += incr ) {
                                if( ownLocation[cellRow][col] == 0 ) {
                                    possibleMoves.add( getMove( cellRow, cellCol, cellRow, col ) ) ;
                                    if( opponentLocation[cellRow][col] == 1 ) break ;
                                }
                                else break ;
                            }
                        }

                        // Get the possible moves in the same column.
                        for( int incr = -1 ; incr <= 1 ; incr += 2 ) {
                            // The above is to toggle incr for the two loops,
                            // once its -1 and then 1.
                            limit = Math.max( -1, 8 * incr ) ;
                            for( int row = cellRow + incr ; row != limit ; row += incr ) {
                                if( ownLocation[row][cellCol] == 0 ) {
                                    possibleMoves.add( getMove( cellRow, cellCol, row, cellCol ) ) ;
                                    
                                    if( opponentLocation[row][cellCol] == 1 ) break ;
                                }
                                else break ;
                            }
                        }
                        if( !bForQueen ) break ;
                    }

                    case BISHOP: {
                        for( int cIncr = -1 ; cIncr <= 1 ; cIncr += 2 ) {
                            for( int rIncr = -1 ; rIncr <= 1 ; rIncr += 2 ) {
                                int col = 0 ;
                                int row = 0 ;

                                int colLimit = ( cIncr < 0 ) ? cellCol
                                        : ( 7 - cellCol ) ;
                                int rowLimit = ( rIncr < 0 ) ? cellRow
                                        : ( 7 - cellRow ) ;
                                int numIter = Math.min( colLimit, rowLimit ) ;

                                for( int i = 1 ; i <= numIter ; i++ ) {
                                    col = cellCol + i * cIncr ;
                                    row = cellRow + i * rIncr ;

                                    if( ownLocation[row][col] == 0 ) {
                                        possibleMoves.add( getMove( cellRow, cellCol, row, col ) ) ;
                                        if( opponentLocation[row][col] == 1 ) break ;
                                    }
                                    else break ;
                                }
                            }
                        }
                    }
                }
                break ;
            }

        }

        return possibleMoves ;
    }

    private List<Move> getMovesForKing( int cellRow, int cellCol, int[][] ownLocation ) {
        
        List<Move> moves = new ArrayList<Move>() ;
        
        int startCol = Math.max( 0, ( cellCol - 1 ) ) ;
        int endCol   = Math.min( 7, ( cellCol + 1 ) ) ;
        int startRow = Math.max( 0, ( cellRow - 1 ) ) ;
        int endRow   = Math.min( 7, ( cellRow + 1 ) ) ;

        for( int col = startCol ; col <= endCol ; col++ ) {
            for( int row = startRow ; row <= endRow ; row++ ) {
                
                if( ownLocation[row][col] == 0 ) {
                    if( ( Math.abs( col - cellCol ) == 1 ) || 
                        ( Math.abs( row - cellRow ) == 1 ) ) {
                        
                        moves.add( getMove( cellRow, cellCol, row, col ) ) ;
                    }
                }
            }
        }
        
        return moves ;
    }

    private List<Move> getMovesForKnight( int cellRow, int cellCol, 
                                          int[][] ownLocation ) {
        
        int startCol = Math.max( 0, ( cellCol - 2 ) ) ;
        int startRow = Math.min( 7, ( cellCol + 2 ) ) ;
        int endCol   = Math.max( 0, ( cellRow - 2 ) ) ;
        int endRow   = Math.min( 7, ( cellRow + 2 ) ) ;
        
        List<Move> moves = new ArrayList<Move>() ;
        
        for( int col = startCol ; col <= endCol ; col++ ) {
            for( int row = startRow ; row <= endRow ; row++ ) {
                
                if( ownLocation[row][col] == 0 ) {
                    
                    if( ( Math.abs( col - cellCol ) == 2 ) && 
                        ( Math.abs( row - cellRow ) == 1 ) ) {
                        
                        moves.add( getMove( cellRow, cellCol, row, col ) ) ;
                    }
                    else if( ( Math.abs( col - cellCol ) == 1 ) && 
                             ( Math.abs( row - cellRow ) == 2 ) ) {
                        
                        moves.add( getMove( cellRow, cellCol, row, col ) ) ;
                    }
                }
            }
        }
        
        return moves ;
    }

    private List<Move> getMovesForPawn( int cellRow, int cellCol, 
                                        int yStepDir, 
                                        int[][] ownLocation,
                                        int[][] opponentLocation ) {
        
        List<Move> moves = new ArrayList<Move>() ;
        int startCol, endCol ;
        
        // Base line is the row from where the pawn starts its life. If
        // its upward movement then baseline is row1 else its row6.
        int baseLine = ( yStepDir == 1 ) ? 1 : 6 ;
        boolean pieceOnBaseLine = ( yStepDir ==  1 && cellRow == baseLine ) ||
                                  ( yStepDir == -1 && cellRow == baseLine ) ;

        // If the pawn is in the last row for either color, no moves possible
        if( cellRow > 6 || cellRow < 1 ) return moves ;

        startCol = ( ( cellCol % 8 ) == 0 ) ? cellCol : ( cellCol - 1 ) ;
        endCol   = ( ( cellCol % 8 ) == 7 ) ? cellCol : ( cellCol + 1 ) ;

        for( int col = startCol ; col <= endCol ; col++ ) {
            if( ( col == cellCol ) ) {
                if( ( ownLocation[cellRow + yStepDir][col] == 0 ) &&
                    ( opponentLocation[cellRow + yStepDir][col] == 0 ) ) {
                    
                    moves.add( getMove( cellRow, cellCol, 
                                       (cellRow + yStepDir), col ) ) ;
                }
            }
            else if( opponentLocation[cellRow + yStepDir][col] == 1 ) {
                moves.add( getMove( cellRow, cellCol,
                                    (cellRow + yStepDir), col ) ) ;
            }
        }

        if( ( pieceOnBaseLine )
                && ( ownLocation[cellRow + yStepDir][cellCol] == 0 )
                && ( ownLocation[cellRow + 2 * yStepDir][cellCol] == 0 )
                && ( opponentLocation[cellRow + yStepDir][cellCol] == 0 )
                && ( opponentLocation[cellRow + 2 * yStepDir][cellCol] == 0 ) ) {
            
            moves.add( getMove( cellRow, cellCol,
                                (cellRow + 2*yStepDir), cellCol ) ) ;
        }
        
        return moves ;
    }

    private Move getMove( int cellRow, int cellCol, int row, int col ) {
        Move move = null ;
        ChessPosition sourceCell = (ChessPosition) getPosition( cellRow,
                cellCol ) ;
        ChessPosition destCell = (ChessPosition) getPosition( row, col ) ;

        move = new ChessMove( sourceCell, destCell ) ;

        return move ;
    }

    public int[][] getPositionIndex( int perspective ) {
        return pieceLocations[perspective] ;
    }

    public boolean isValidMove( Move move ) {
        
        ChessMove mv = (ChessMove) move ;

        if( checkBaseMoveValidity( mv ) ) {
            switch( mv.srcPiece ){
                case BISHOP:
                    return isValidMoveForBishop( mv ) ;
                    
                case ROOK:
                    return isValidMoveForRook( mv ) ;
                    
                case KNIGHT:
                    return isValidMoveForKnight( mv ) ;
                    
                case QUEEN:
                    return isValidMoveForQueen( mv ) ;
                    
                case KING:
                    return isValidMoveForKing( mv ) ;
                    
                case PAWN:
                    return isValidMoveForPawn( mv ) ;
            }
        }
        
        return false ;
    }

    private boolean checkBaseMoveValidity( ChessMove mv ) {
        
        // If the destination cell is not empty and if there is a piece of the
        // same colour, then the move is invalid.
        if( mv.destPiece != EMPTY ) {
            if( mv.destPieceColor == mv.srcPieceColor ) {
                return false ;
            }
        }

        // A piece can't be moved onto itself.
        if( ( mv.destCol == mv.srcCol ) && 
            ( mv.srcRow == mv.destRow ) ) {
            return false ;
        }
        
        return true ;
    }

    private boolean isValidMoveForPawn( ChessMove mv ) {
        
        int pawnMvDir = ( mv.srcPieceColor == topColor ) ? 1 : -1 ;
        int opponentColor = getEnemyPerspective( mv.srcPieceColor ) ;
        int baseLine = ( pawnMvDir == 1 ) ? 1 : 6 ;

        if( mv.destCol == mv.srcCol ) 
            if( ( mv.destRow - mv.srcRow ) == ( pawnMvDir * 1 ) ) 
                if( mv.destPiece == EMPTY ) return true ;

        if( ( mv.destRow - mv.srcRow ) == ( pawnMvDir * 1 ) ) 
            if( Math.abs( mv.destCol - mv.srcCol ) == 1 ) 
                if( ( mv.destPiece != EMPTY ) && ( mv.destPieceColor == opponentColor ) ) 
                    return true ;

        if( mv.destCol == mv.srcCol ) 
            if( ( mv.destRow - mv.srcRow ) == ( pawnMvDir * 2 ) ) 
                if( mv.srcRow == baseLine ) 
                    if( mv.destPiece == EMPTY ) 
                        if( isPathClear( mv ) ) 
                            return true ;
        
        return false ;
    }

    private boolean isValidMoveForKing( ChessMove mv ) {
        
        int colDist = Math.abs( mv.destCol - mv.srcCol ) ; 
        int rowDist = Math.abs( mv.destRow - mv.srcRow ) ;
        
        if( ( colDist == 1 ) && ( rowDist <= 1 ) ) return true ;
        if( ( rowDist == 1 ) && ( colDist <= 1 ) ) return true ;
        
        return false ;
    }

    private boolean isValidMoveForQueen( ChessMove mv ) {
        
        boolean pathIsDiagonal = Math.abs( mv.destRow - mv.srcRow ) == 
                                 Math.abs( mv.destCol - mv.srcCol ) ;
        
        boolean pathIsStraight = ( mv.destCol == mv.srcCol ) || 
                                 ( mv.destRow == mv.srcRow ) ; 
        
        if( pathIsDiagonal || pathIsStraight ) {
            if( isPathClear( mv ) ) {
                return true ;
            }
        }
        return false ;
    }

    private boolean isValidMoveForKnight( ChessMove mv ) {
        
        if( Math.abs( mv.destRow - mv.srcRow ) == 1 ) { 
            if( Math.abs( mv.destCol - mv.srcCol ) == 2 ) { 
                return true ;
            }
        }

        if( Math.abs( mv.destCol - mv.srcCol ) == 1 ) { 
            if( Math.abs( mv.destRow - mv.srcRow ) == 2 ) { 
                return true ;
            }
        }
        return false ;
    }

    private boolean isValidMoveForRook( ChessMove mv ) {
        // Rook can only move in a straight line, hence first check if the
        // source and destination cells line on the same line. If so, then
        // the move is valid only if the path from source to destination
        // is clear.
        if( ( mv.destCol == mv.srcCol ) || 
            ( mv.destRow == mv.srcRow ) ) {
            
            if( isPathClear( mv ) ) {
                return true ;
            }
        }
        return false ;
    }

    private boolean isValidMoveForBishop( ChessMove mv ) {
        // Bishop can only move diagonally, hence first check if the
        // source and destination cells form a diagonal. If so, then
        // the move is valid only if the path from source to destination
        // is clear.
        if( Math.abs( mv.destRow - mv.srcRow ) == 
            Math.abs( mv.destCol - mv.srcCol ) ) {
            
            if( isPathClear( mv ) ) {
                return true ;
            }
        }
        return false ;
    }

    private boolean isPathClear( ChessMove mv ) {
        
        if( mv.srcCol == mv.destCol ) {
            int minY = Math.min( mv.srcRow, mv.destRow ) ;
            int maxY = Math.max( mv.srcRow, mv.destRow ) ;
            
            for( int i = minY + 1 ; i < maxY ; i++ ) {
                ChessPosition pos = (ChessPosition) getPosition( i, mv.srcCol ) ;
                if( pos.piece != EMPTY ) return false ;
            }
        }

        if( mv.srcRow == mv.destRow ) {
            int minX = Math.min( mv.srcCol, mv.destCol ) ;
            int maxX = Math.max( mv.srcCol, mv.destCol ) ;
            
            for( int i = minX + 1 ; i < maxX ; i++ ) {
                ChessPosition pos = (ChessPosition) getPosition( mv.srcRow, i ) ;
                if( pos.piece != EMPTY ) return false ;
            }
        }

        if( Math.abs( mv.srcCol - mv.destCol ) == 
            Math.abs( mv.srcRow - mv.destRow ) ) {
            
            int incrX = Math.abs( mv.destCol - mv.srcCol ) / ( mv.destCol - mv.srcCol ) ;
            int incrY = Math.abs( mv.destRow - mv.srcRow ) / ( mv.destRow - mv.srcRow ) ;

            int x = mv.srcCol + incrX ;
            int y = mv.srcRow + incrY ;

            for( ; ( x != mv.destCol ) ; x += incrX, y += incrY ) {
                ChessPosition pos = (ChessPosition) getPosition( y, x ) ;
                if( pos.piece != EMPTY ) return false ;
            }
        }

        return true ;
    }

    public String getEndingMessage() {
        return endingMessage ;
    }

    public void printBoard() {
        for( int row = 0 ; row < 8 ; row++ ) {
            for( int col = 0 ; col < 8 ; col++ ) {
                if( pieceLocations[BLACK][row][col] == 1 ) 
                    System.out.print( "B" ) ;
                else if( pieceLocations[WHITE][row][col] == 1 ) 
                    System.out.print( "W" ) ;
                else 
                    System.out.print( " " ) ;
            }
            System.out.println() ;
        }
    }

    public GameBoard getScratchBoard() {
        ChessBoard chessBoard = new ChessBoard( this ) ;
        return chessBoard ;
    }

    private void initializeBoard( String initFile ) {
        
        BufferedReader in = null ;

        try {
            in = new BufferedReader( new FileReader( initFile ) ) ;
            for( int row = 0 ; row < NUM_ROWS ; row++ ) {
                
                String tempString = in.readLine() ;
                StringTokenizer tokenizer = new StringTokenizer( tempString, "|" ) ;
                in.readLine() ;

                for( int col = 0 ; col < NUM_COLS ; col++ ) {
                    String pos = tokenizer.nextToken() ;
                    char color = (char) pos.charAt( 0 ) ;
                    char pc = (char) pos.charAt( 1 ) ;

                    int pieceColor = ( color == 'b' ) ? BLACK
                            : ( color == 'w' ) ? WHITE : EMPTY ;
                    int piece = getPiece( pc ) ;

                    positions[row][col] = new ChessPosition( piece, pieceColor, 
                                                             ( row + col ) % 2, 
                                                             row, col ) ;
                    if( piece != EMPTY ) {
                        pieceLocations[pieceColor][row][col] = 1 ;
                    }
                }
            }
        }
        catch( Exception e ) {
            e.printStackTrace() ;
        }
    }

    private int getPiece( int pc ) {
        switch( pc ){
            case 'p':
                return PAWN ;

            case 'r':
                return ROOK ;

            case 'k':
                return KNIGHT ;

            case 'b':
                return BISHOP ;

            case 'K':
                return KING ;

            case 'Q':
                return QUEEN ;
        }

        return EMPTY ;
    }

    public static double getMaterialWeight( int piece ) {
        double wt = 0 ;

        switch( piece ){
            case ChessBoard.PAWN:
                wt = PAWN_WT ;
                break ;

            case ChessBoard.ROOK:
                wt = ROOK_WT ;
                break ;

            case ChessBoard.BISHOP:
                wt = BISHOP_WT ;
                break ;

            case ChessBoard.KNIGHT:
                wt = KNIGHT_WT ;
                break ;

            case ChessBoard.QUEEN:
                wt = QUEEN_WT ;
                break ;

            case ChessBoard.KING:
                wt = KING_WT ;
                break ;

            case ChessBoard.EMPTY:
                wt = 0 ;
        }

        return wt ;
    }

    public int getTopColor() {
        return topColor ;
    }

    public void setGameActive( boolean active, String message ) {
        gameActive = active ;
        endingMessage = message ;
    }
}
