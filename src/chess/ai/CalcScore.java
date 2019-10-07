package chess.ai;

import chess.Chess;
import chess.board.Board;
import static chess.board.Board.COLS;
import static chess.board.Board.ROWS;
import chess.pieces.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lingxuan925
 */
public class CalcScore {
    
    private static final int PAWN_VALUE     =    100;
    private static final int KNIGHT_VALUE   =    320;
    private static final int BISHOP_VALUE   =    330;
    private static final int ROOK_VALUE     =    500;
    private static final int QUEEN_VALUE    =    900;
    private static final int KING_VALUE     = 20_000;

    private static int[][] PawnTable = new int[][] {
        {  0,  0,  0,  0,  0,  0,  0,  0 },
        { 50, 50, 50, 50, 50, 50, 50, 50 },
        { 10, 10, 20, 30, 30, 20, 10, 10 },
        {  5,  5, 10, 25, 25, 10,  5,  5 },
        {  0,  0,  0, 20, 20,  0,  0,  0 },
        {  5, -5,-10,  0,  0,-10, -5,  5 },
        {  5, 10, 10,-20,-20, 10, 10,  5 },
        {  0,  0,  0,  0,  0,  0,  0,  0 }
    };

    private static int[][] KnightTable = new int[][] {
        { -50,-40,-30,-30,-30,-30,-40,-50 },
        { -40,-20,  0,  0,  0,  0,-20,-40 },
        { -30,  0, 10, 15, 15, 10,  0,-30 },
        { -30,  5, 15, 20, 20, 15,  5,-30 },
        { -30,  0, 15, 20, 20, 15,  0,-30 },
        { -30,  5, 10, 15, 15, 10,  5,-30 },
        { -40,-20,  0,  5,  5,  0,-20,-40 },
        { -50,-40,-30,-30,-30,-30,-40,-50 }
    };

    private static int[][] BishopTable = new int[][] {
        { -20,-10,-10,-10,-10,-10,-10,-20 },
        { -10,  0,  0,  0,  0,  0,  0,-10 },
        { -10,  0,  5, 10, 10,  5,  0,-10 },
        { -10,  5,  5, 10, 10,  5,  5,-10 },
        { -10,  0, 10, 10, 10, 10,  0,-10 },
        { -10, 10, 10, 10, 10, 10, 10,-10 },
        { -10,  5,  0,  0,  0,  0,  5,-10 },
        { -20,-10,-10,-10,-10,-10,-10,-20 }
    };

    private static int[][] RookTable = new int[][] {
        {  0,  0,  0,  0,  0,  0,  0,  0 },
        {  5, 10, 10, 10, 10, 10, 10,  5 },
        { -5,  0,  0,  0,  0,  0,  0, -5 },
        { -5,  0,  0,  0,  0,  0,  0, -5 },
        { -5,  0,  0,  0,  0,  0,  0, -5 },
        { -5,  0,  0,  0,  0,  0,  0, -5 },
        { -5,  0,  0,  0,  0,  0,  0, -5 },
        {  0,  0,  0,  5,  5,  0,  0,  0 }
    };

    private static int[][] QueenTable = new int[][] {
        { -20,-10,-10, -5, -5,-10,-10,-20 },
        { -10,  0,  0,  0,  0,  0,  0,-10 },
        { -10,  0,  5,  5,  5,  5,  0,-10 },
        {  -5,  0,  5,  5,  5,  5,  0, -5 },
        {   0,  0,  5,  5,  5,  5,  0, -5 },
        { -10,  5,  5,  5,  5,  5,  0,-10 },
        { -10,  0,  5,  0,  0,  0,  0,-10 },
        { -20,-10,-10, -5, -5,-10,-10,-20 }
    };

    private static int[][] KingTableMiddleGame = new int[][] {
        { -30,-40,-40,-50,-50,-40,-40,-30 },
        { -30,-40,-40,-50,-50,-40,-40,-30 },
        { -30,-40,-40,-50,-50,-40,-40,-30 },
        { -30,-40,-40,-50,-50,-40,-40,-30 },
        { -20,-30,-30,-40,-40,-30,-30,-20 },
        { -10,-20,-20,-20,-20,-20,-20,-10 },
        {  20, 20,  0,  0,  0,  0, 20, 20 },
        {  20, 30, 10,  0,  0, 10, 30, 20 }
    };

    private static int[][] KingTableEndGame = new int[][] {
        { -50,-40,-30,-20,-20,-30,-40,-50 },
        { -30,-20,-10,  0,  0,-10,-20,-30 },
        { -30,-10, 20, 30, 30, 20,-10,-30 },
        { -30,-10, 30, 40, 40, 30,-10,-30 },
        { -30,-10, 30, 40, 40, 30,-10,-30 },
        { -30,-10, 20, 30, 30, 20,-10,-30 },
        { -30,-30,  0,  0,  0,  0,-30,-30 },
        { -50,-30,-30,-30,-30,-30,-30,-50 }
    };
    
    private static int valueOfPiece (Piece piece) {
        if (piece instanceof Pawn)          return PAWN_VALUE;
        else if (piece instanceof Knight)   return KNIGHT_VALUE;
        else if (piece instanceof Bishop)   return BISHOP_VALUE;
        else if (piece instanceof Rook)     return ROOK_VALUE;
        else if (piece instanceof  Queen)   return QUEEN_VALUE;
        else                                return KING_VALUE;
    }
    
    private static int valueOfPosition (int[] pos) {

        Piece piece = Board.getTile(pos[0], pos[1]).getPiece();

        int col = pos[1];
        int row = (piece.color() == 0) ? (pos[0]) : (7 - pos[0]);

        if (piece instanceof Pawn) {
            return PawnTable[row][col];
        }
        else if (piece instanceof Knight) {
            return KnightTable[row][col];
        }
        else if (piece instanceof Bishop) {
            return BishopTable[row][col];
        }
        else if (piece instanceof Rook) {
            return RookTable[row][col];
        }
        else if (piece instanceof  Queen) {
            return QueenTable[row][col];
        }
        else if (endGame()) {
            return KingTableEndGame[row][col];
        } else {
            return KingTableMiddleGame[row][col];
        }

    }
    
    private static boolean endGame() {

        final int endGameScore = KING_VALUE + QUEEN_VALUE + ROOK_VALUE;

        boolean endWhite = hasQueen(0);
        endWhite = endWhite && teamScore(0) <= endGameScore;

        boolean endBlack = hasQueen(0);
        endBlack = endBlack && teamScore(1) <= endGameScore;

        return noQueensLeft() || (endWhite && endBlack);
    }
    
    private static boolean hasQueen (int turn) {
        ArrayList<Piece> availablePieces = new ArrayList<Piece>();
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLS; j++){
                if (Board.getTile(i, j).getPiece() != null) {
                    availablePieces.add(Board.getTile(i, j).getPiece());
                }
            }
        }
        
        for (Piece pce : availablePieces) {
            if (pce.color() == turn && pce instanceof Queen) {
                    return true;
            }
        }
        return false;
    }

    private static boolean noQueensLeft () {
        ArrayList<Piece> availablePieces = new ArrayList<Piece>();
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLS; j++){
                if (Board.getTile(i, j).getPiece() != null) {
                    availablePieces.add(Board.getTile(i, j).getPiece());
                }
            }
        }
        
        for (Piece pce : availablePieces) {
            if (pce instanceof Queen) {
                return false;
            }
        }
        return true;
    }

    private static int teamScore (int turn) {
        ArrayList<Piece> availablePieces = new ArrayList<Piece>();
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLS; j++){
                if (Board.getTile(i, j).getPiece() != null) {
                    availablePieces.add(Board.getTile(i, j).getPiece());
                }
            }
        }
        
        int score = 0;
        for (Piece pce : availablePieces) {
            if (pce.color() == turn) {
                score += valueOfPiece(pce);
            }
        }
        return score;
    }
    
    private static int compute (int turn, int curDepth) {
        int score = 0;
        
        ArrayList<Piece> availablePieces = new ArrayList<Piece>();
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLS; j++){
                if (Board.getTile(i, j).getPiece() != null) {
                    availablePieces.add(Board.getTile(i, j).getPiece());
                }
            }
        }

        for (Piece pce : availablePieces) {
            if (pce.color() == turn) {
                score += valueOfPiece(pce);
                score += valueOfPosition(pce.position());
                score -= curDepth; // Quicker win or slower loss is preferred.
                score += new Random().nextInt(5); // Element of randomness.
            } else {
                score -= valueOfPiece(pce);
                score -= valueOfPosition(pce.position());
                score += curDepth; // Quicker win or slower loss is preferred.
                score -= new Random().nextInt(5); // Element of randomness.
            }
        }

        return score;
    }
    
    static int score (int turn, int curDepth) {
        int opponent = (turn == 1) ? 0 : 1;

        if (Chess.gOver && Chess.isWinner == turn) {
            return (int)Double.POSITIVE_INFINITY;
        } else if (Chess.gOver && Chess.isWinner == opponent) {
            return (int)Double.NEGATIVE_INFINITY;
        } else if (Chess.gOver && Chess.isWinner == 3) { // Draw.
            return 0;
        } else {
            return CalcScore.compute(turn, curDepth);
        }
    }
}
