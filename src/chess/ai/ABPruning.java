package chess.ai;

import chess.Chess;
import chess.board.*;
import static chess.board.Board.COLS;
import static chess.board.Board.ROWS;
import chess.pieces.*;
import chess.pieces.Piece;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author lingxuan925
 */
public class ABPruning {
    
    private static double maxDepth;
    
    private static Piece bestPiece;
    private static int[] bestPosition;
    
    private static final Stack<Piece> pces = new Stack<Piece>();
    private static final Stack<Piece> eatenPces = new Stack<Piece>();
    private static final Stack<Integer> preRow = new Stack<Integer>();
    private static final Stack<Integer> preCol = new Stack<Integer>();
    
    private ABPruning() {}
    
    public static void aiPlay (int turn, double maxD) {
        ABPruning.maxDepth = maxD;
//        ArrayList<Piece> config = new ArrayList<Piece>();
//        ArrayList<Integer> row = new ArrayList<Integer>();
//        ArrayList<Integer> col = new ArrayList<Integer>();
//        for (int i=0; i<ROWS; i++){
//            for (int j=0; j<COLS; j++){
//                if (Board.getTile(i, j).getPiece() != null) {
//                    config.add(Board.getTile(i, j).getPiece());
//                    row.add(Board.getTile(i, j).getPiece().position()[0]);
//                    col.add(Board.getTile(i, j).getPiece().position()[1]);
//                }
//            }
//        }
        alphaBeta(turn, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        //printBoard();
        aiMove(bestPiece, bestPosition);
        //System.out.println();
        //printBoard();
        //System.out.println();
    }
    
    private static int alphaBeta(int turn, double alpha, double beta, int curDepth) {
        if (curDepth++ == maxDepth || Chess.gOver) {
            return CalcScore.score(turn, curDepth);
        }

        if (Chess.turn == turn) {
            return getMax(turn, alpha, beta, curDepth);
        } else {
            return getMin(turn, alpha, beta, curDepth);
        }
    }
    
    public static int getMax(int turn, double alpha, double beta, int curDepth) {
        Piece bestPieceToMove = null;
        int[] bestPosToMove = null;
        
        int totalMovesMadeSoFar = Chess.totalMoveCnt;
        //System.out.println(totalMovesMadeSoFar);
        
        ArrayList<Piece> availablePieces = new ArrayList<Piece>();
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLS; j++){
                if (Board.getTile(i, j).getPiece() != null && Board.getTile(i, j).getPiece().color() == Chess.turn) {
                    //System.out.print(Board.getTile(i, j).getPiece().getName()+" ");
                    //System.out.println(Board.getTile(i, j).getPiece().getColor());
//                    if (Board.getTile(i, j).getPiece().getName().equals("king")) {
//                        ArrayList<int[]> list = Board.getTile(i, j).getPiece().getAllPossibleMoves();
//                        System.out.println("king at ("+i+","+j+")");
//                        for (int[] ps : Board.getTile(i, j).getPiece().getAllPossibleMoves()) {
//                            System.out.println("("+ps[0]+","+ps[1]+")");                           
//                        }
//                    }
                    availablePieces.add(Board.getTile(i, j).getPiece());
//                    ArrayList<int[]> list = Board.getTile(i, j).getPiece().getAllPossibleMoves();

//                    if (!list.isEmpty()) {
//                        System.out.println(Board.getTile(i, j).getPiece().getName());
//                        Board.getTile(list.get(0)[0], list.get(0)[1]).addPiece(Board.getTile(i, j).getPiece());
//                        Board.getTile(i, j).removePiece();
//                        break;
//                    }
                }
            }
        }
        //System.out.println(availablePieces.size());
        for (Piece pce : availablePieces) {
            for (int[] move : pce.getAllPossibleMoves()) {
                pces.add(pce);
                preRow.add(pce.position()[0]);
                preCol.add(pce.position()[1]);
                if (Board.getTile(move[0], move[1]).getPiece() != null) {
                    pce.setAte(pce.getAte()+1);
                    eatenPces.add(Board.getTile(move[0], move[1]).getPiece());
                }
                aiFakeMove(pce, move);
                int score = alphaBeta(turn, alpha, beta, curDepth);
                //undo the moves made till chess move cnt equals total moves made so far, needs to undo chess turn
                if (totalMovesMadeSoFar != Chess.totalMoveCnt) {
                    //System.out.println(pces.size()+" "+preRow.size()+" "+preCol.size());
                    if (pces.peek().getAte() == 0) {
                        undoFakeMove(pces.pop(), preRow.pop(), preCol.pop());
                    }
                    else {
                        pces.peek().setAte(pces.peek().getAte()-1);
                        undoFakeMove(pces.pop(), preRow.pop(), preCol.pop());
                        Board.getTile(eatenPces.peek().position()[0], eatenPces.peek().position()[1]).fakeAddPiece(eatenPces.pop());
                    }
                }
                
                if (score > alpha) {
                    alpha = score;
                    bestPieceToMove = pce;
                    bestPosToMove = move;
                }
                if (alpha >= beta) break; //pruning
            }
        }
        
        if (bestPosToMove != null) {
            ABPruning.bestPiece = bestPieceToMove;
            ABPruning.bestPosition = bestPosToMove;
        }

        return (int)alpha;
    }
    
    private static int getMin(int turn, double alpha, double beta, int curDepth) {       
        //int totalMovesMadeSoFar = Chess.totalMoveCnt;
        //System.out.println(totalMovesMadeSoFar);
        
        ArrayList<Piece> availablePieces = new ArrayList<Piece>();
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLS; j++){
                if (Board.getTile(i, j).getPiece() != null && Board.getTile(i, j).getPiece().color() == Chess.turn) {
                    availablePieces.add(Board.getTile(i, j).getPiece());
                }
            }
        }
        //System.out.println(availablePieces.size());
        for (Piece pce : availablePieces) {
            for (int[] move : pce.getAllPossibleMoves()) {
                pces.add(pce);
                preRow.add(pce.position()[0]);
                preCol.add(pce.position()[1]);
                if (Board.getTile(move[0], move[1]).getPiece() != null) {
                    //System.out.println(Board.getTile(move[0], move[1]).getPiece().getName());
                    pce.setAte(pce.getAte()+1);
                    eatenPces.add(Board.getTile(move[0], move[1]).getPiece());
                    //System.out.println(eatenPces.size());
                }
                aiFakeMove(pce, move);
                int score = alphaBeta(turn, alpha, beta, curDepth);
                //undo the move made, undo chess turn as well
                //System.out.println(pces.size()+" "+preRow.size()+" "+preCol.size());
                if (pces.peek().getAte() == 0) {
                        undoFakeMove(pces.pop(), preRow.pop(), preCol.pop());
                    }
                else {
                    pces.peek().setAte(pces.peek().getAte()-1);
                    undoFakeMove(pces.pop(), preRow.pop(), preCol.pop());
                    Board.getTile(eatenPces.peek().position()[0], eatenPces.peek().position()[1]).fakeAddPiece(eatenPces.pop());
                }
                
                if (score < beta) beta = score;

                if (alpha >= beta) break; //pruning
            }
        }
        
        return (int)beta;
    }
    
    private static void aiMove(Piece pce, int[] posToMove) {
        int tmprow = pce.position()[0], tmpcol = pce.position()[1];
        //System.out.println(pce.getName()+" at ("+tmprow+","+tmpcol+")");
        Board.getTile(posToMove[0], posToMove[1]).addPiece(pce);
        //System.out.println("Move "+pce.getName()+" to ("+posToMove[0]+","+posToMove[1]+")");
        Board.getTile(tmprow, tmpcol).removePiece();
        //System.out.println("Remove "+pce.getName()+" from ("+tmprow+","+tmpcol+")");
        if(pce.getName().equals("pawn") && posToMove[0] == 3){
            Pawn pawn = (Pawn)pce;
            pawn.setPassant(true);
        }
        Chess.totalMoveCnt++;
        Chess.turn = (Chess.turn == 1) ? 0 : 1;
    }
    
    private static void aiFakeMove(Piece pce, int[] posToMove) {
        int tmprow = pce.position()[0], tmpcol = pce.position()[1];
        //System.out.println(pce.getName()+" at ("+tmprow+","+tmpcol+")");
        Board.getTile(posToMove[0], posToMove[1]).fakeAddPiece(pce);
        //System.out.println("Move "+pce.getName()+" to ("+posToMove[0]+","+posToMove[1]+")");
        Board.getTile(tmprow, tmpcol).fakeRemovePiece();
        //System.out.println("Remove "+pce.getName()+" from ("+tmprow+","+tmpcol+")");
        Chess.totalMoveCnt++;
        Chess.turn = (Chess.turn == 1) ? 0 : 1;
    }
    
    private static void undoFakeMove(Piece pce, int preRow, int preCol) {
        int tmprow = pce.position()[0], tmpcol = pce.position()[1];
        //System.out.println(pce.getName()+" at ("+tmprow+","+tmpcol+")");
        Board.getTile(preRow, preCol).fakeAddPiece(pce);
        //System.out.println("Move "+pce.getName()+" to ("+posToMove[0]+","+posToMove[1]+")");
        Board.getTile(tmprow, tmpcol).fakeRemovePiece();
        //System.out.println("Remove "+pce.getName()+" from ("+tmprow+","+tmpcol+")");
        Chess.totalMoveCnt--;
        Chess.turn = (Chess.turn == 1) ? 0 : 1;
    }
    
    private static void printBoard() {
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLS; j++){
                if (Board.getTile(i, j).getPiece() != null) {
                    System.out.print("("+Board.getTile(i, j).getPiece().color()+","+Board.getTile(i, j).getPiece().getName()+")\t");
                }
                else {
                    System.out.print("(NULL)\t");
                }
            }
            System.out.println();
        }
    }
}
