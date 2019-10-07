package chess;

import chess.board.Board;
import chess.board.MoveChecker;
import chess.gui.GameFrame;
import chess.pieces.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import chess.ai.ABPruning;


public class Chess {

    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static Board board;
    public static Player p1;
    public static Player p2;
    public static int turn;
    public static boolean gOver;
    private static boolean reset;
    private String endGameMessage;
    public static int isWinner = 3;
    public static int totalMoveCnt = 0;
    private static GameFrame frame;
    
    public Chess(){
        board = new Board();
        frame = new GameFrame(board);
        while(true){
            p1 = new Player(WHITE);
            p2 = new Player(BLACK);
            turn = WHITE;
            gOver = false;
            reset = false;
            while(!gOver){
                if(turn == WHITE){         //player 1 turn
                    clearEnPassant(WHITE); //clears enPassant pawns
                    takeTurn(p1);          //waits until valid move is made 
                    checkPromotion(WHITE); //check for pawn promotion
                    check(p2);             //see if other player is in check 
                    checkMate(BLACK);      //see if other player is in checkmate 
                    checkStalemate(BLACK);
                    totalMoveCnt++;
                    turn = BLACK;          //end the turn of this player 
                }
                else if(turn == BLACK){
                    clearEnPassant(BLACK);
                    AITakeTurn();
                    //takeTurn(p2);
                    checkPromotion(BLACK);
                    check(p1);
                    checkMate(WHITE);
                    checkStalemate(WHITE);
                    totalMoveCnt++;
                    turn = WHITE;
                }
            }
            if(!reset){
                //Once the game is over prompt user to play again
                String[] options = new String[] {"New Game", "Quit"};
                int response = JOptionPane.showOptionDialog(null, 
                        endGameMessage, "Game Over",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                if(!(response == 0)){
                     System.exit(0);
                }
                else resetGame();
            }
        }
    }
    
    public static int getTotalMoveCnt() {
        return totalMoveCnt;
    }
    
    //reset the game
    public static void resetGame(){
        reset = true;
        gOver = true;
        board.reset();
        frame.setCheck("");
    }
    
    //Clear check icon on MenuBar
    public static void clearCheck(){
        frame.setCheck("");
    }
    
    private void AITakeTurn() {
        ABPruning.aiPlay(turn, 2);
        //ABPruning.getMax(turn, 1000, 1000, 1);
    }
    
    //Wait for Player to make a valid move
    private void takeTurn(Player p){
        p.isChoosing(true);
        while(p.choosing()){
            try{
                Thread.sleep(50);
                if(reset)break;
            }catch(InterruptedException ex){}
        }
    }
    
    //Clear all pawns of this color that are enPassant
    private void clearEnPassant(int color){
        for(int i=0; i<Board.ROWS; i++){
            for(int j=0; j<Board.COLS; j++){
                Piece p = Board.getTile(i,j).getPiece();
                if(p != null){
                    if(p.getName().equals("pawn") && p.color() == color){
                        Pawn pawn = (Pawn)p;
                        pawn.setPassant(false);
                    }
                }
            }
        }
    }
    
    //Check if any pawns are viable for pawn promotion
    private void checkPromotion(int color){
        int row = (color == WHITE) ? 0 : 7;
        int response = -2;
        String[] options = new String[] {"Queen", "Knight", "Rook", "Bishop"};
        int[] pos = new int[2];
        for(int i=0; i<Board.COLS; i++){
            Piece p = Board.getTile(row,i).getPiece();
            if(p != null && p.getName().equals("pawn")){
                if(color == WHITE){
                    response = JOptionPane.showOptionDialog(null, 
                    "Select a piece to replace your pawn.", "Pawn Promotion",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
                }
                else response = 0;
                pos = new int[]{row,i};
            }
        }
        if(response != -2){
            Board.getTile(pos[0], pos[1]).removePiece();
            Piece prom = new Queen(color);
            switch(response){
                case 0:
                    prom = new Queen(color);
                    break;
                case 1:
                    prom = new Knight(color);
                    break;
                case 2:
                    prom = new Rook(color);
                    break;
                case 3:
                    prom = new Bishop(color);
                    break;
            }
            prom.setPosition(pos[0], pos[1]);
            Board.getTile(pos[0],pos[1]).addPiece(prom);
        }
    }
    
    //Determine if this player is in check
    private void check(Player player){
        int color = player.getColor();
        int[] kPos = Board.kingPosition(color);
        MoveChecker.fillCheckZone((color==WHITE)? WHITE : BLACK);
        for(int[] cz : MoveChecker.checkZone){
            if(kPos[0] == cz[0] && kPos[1] == cz[1]){
                player.check(true);
                frame.setCheck(player.name());
                break;
            }
        }
    }
    
    //Checks for a stalemate
    private void checkStalemate(int color){
        if(MoveChecker.numberOfPossibleMoves(color) == 0){
            endGameMessage = "Players have reached a stalemate.";
            gOver = true;
        }
    }
    
    //Determine of the player with this color is in checkmate 
    private void checkMate(int color){
        ArrayList<int[]> king = MoveChecker.getKingMoves(color);
        MoveChecker.fillCheckZone(color);
        int checkMate = 0;
        for(int[] kz : king){
            for(int[] cz : MoveChecker.checkZone){
                if(kz[0] == cz[0] && kz[1] == cz[1]){
                    checkMate++;
                    break;
                }
            }
        }
        checkMate -= MoveChecker.checkBreakers(color);
        if(!king.isEmpty() && checkMate == king.size()){
            endGameMessage = (color == WHITE) ? "Black wins!" : "White wins!";
            isWinner = (color == WHITE) ? 1 : 0;
            gOver = true;
        }
    }
    
    public static void main(String[] args) {Chess c = new Chess();}
    
}
