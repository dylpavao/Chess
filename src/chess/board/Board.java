package chess.board;

import static chess.board.Tile.*;
import chess.pieces.*;

/**
 *
 * @author Dylan
 */
public class Board {
    
    static final int NUM_TILES = 64;
    public static final int ROWS = 8;
    public static final int COLS = 8;
    private static Tile[][] tiles;
    
    public Board(){
        int color = 2;
        tiles = new Tile[ROWS][COLS];
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLS; j++){
                tiles[i][j] = new Tile(color%2, i, j);
                color++;
            }
            color++;
        }
        addPieces();
    }
    
    private void addPieces(){
        
        for(int i=1; i<=6; i+=5){
            for(int j=0; j<COLS; j++){
                if (i==1)
                    tiles[i][j].addPiece(new Pawn(BLACK));
                else
                    tiles[i][j].addPiece(new Pawn(WHITE));
            }
        }
        for(int i=0; i<=7; i+=7){
            if(i==0){
                tiles[i][0].addPiece(new Rook(BLACK));
                tiles[i][7].addPiece(new Rook(BLACK));
                tiles[i][1].addPiece(new Knight(BLACK));
                tiles[i][6].addPiece(new Knight(BLACK));
                tiles[i][2].addPiece(new Bishop(BLACK));
                tiles[i][5].addPiece(new Bishop(BLACK));
                tiles[i][3].addPiece(new Queen(BLACK));
                tiles[i][4].addPiece(new King(BLACK));
            }
            else{
                tiles[i][0].addPiece(new Rook(WHITE));
                tiles[i][7].addPiece(new Rook(WHITE));
                tiles[i][1].addPiece(new Knight(WHITE));
                tiles[i][6].addPiece(new Knight(WHITE));
                tiles[i][2].addPiece(new Bishop(WHITE));
                tiles[i][5].addPiece(new Bishop(WHITE));
                tiles[i][3].addPiece(new Queen(WHITE));
                tiles[i][4].addPiece(new King(WHITE));
            }
        }
        //Add Knights
    }
    
    public void clearSelection(){
        for(int i=0; i<ROWS; i++){
            for(int j=0; j<COLS; j++){
                if(tiles[i][j].isSelected()){
                    tiles[i][j].clearSelect();
                }
            }
        }
    }
    
    public boolean movePending(){
        boolean result = false;
        for(int i=0; i<ROWS; i++){
         for(int j=0; j<COLS; j++){
             if(tiles[i][j].isSelected())result = true;
            }
        }
        return result;
    }
    
    public static Tile getTile(int x, int y){
        return tiles[x][y];
    }
    
    public static int[] kingPosition(int color){
        for(int i=0; i<Board.ROWS; i++){
            for(int j=0; j<Board.COLS; j++){
                Piece p = Board.getTile(i,j).getPiece();
                if(p!=null && p.getName().equals("king") && p.color() == color){
                    return p.position();
                }
            }
        }
        return new int[2];
    }
    
    public static void clearBoard(){
        for(int i=0; i<ROWS; i++){
            for(int j=0; j<COLS; j++){
                tiles[i][j].clearSelect();
                tiles[i][j].removePiece();
            }
        }
    }
    
    public void reset(){
        for(int i=0; i<ROWS; i++){
            for(int j=0; j<COLS; j++){
                tiles[i][j].clearSelect();
                tiles[i][j].removePiece();
            }
        }
        addPieces();
    }
    
    public static void fakeReset(){
        for(int i=0; i<ROWS; i++){
            for(int j=0; j<COLS; j++){
                tiles[i][j].clearSelect();
                tiles[i][j].fakeRemovePiece();
            }
        }
    }
    
}
