package chess.pieces;

import chess.board.*;
import java.util.ArrayList;

/**
 *
 * @author Dylan
 */
public class Pawn implements Piece{
    
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    private final int color;
    private boolean enpassant;
    private final String name;
    private final int[] position;
    private int ate;
    
    public Pawn(int color){
         this.color = color;
         enpassant = false;
         name = "pawn";
         position = new int[2];
         ate = 0;
    }
    
    @Override
    public ArrayList<int[]> getAllPossibleMoves() {

        ArrayList<int[]> list = new ArrayList<>();
        if (color == 1) { //black piece
            //can move forward one
            if (position[0]+1 <= 7 && Board.getTile(position[0]+1, position[1]).getPiece() == null) {
                list.add(new int[]{position[0]+1,position[1]});
            }
            //can double step
            if (position[0] == 1) {
                if (Board.getTile(position[0]+2, position[1]).getPiece() == null) {
                    list.add(new int[]{position[0]+2, position[1]});
                }
            }
            //check if can eat left
            if (position[0]+1 <= 7 && position[1]-1 >= 0) {
                if (Board.getTile(position[0]+1, position[1]-1).getPiece() != null && Board.getTile(position[0]+1, position[1]-1).getPiece().color() == 0) {
                    list.add(new int[]{position[0]+1, position[1]-1});
                }
            }
            //check if can eat right
            if (position[0]+1 <= 7 && position[1]+1 <= 7) {
                if (Board.getTile(position[0]+1, position[1]+1).getPiece() != null && Board.getTile(position[0]+1, position[1]+1).getPiece().color() == 0) {
                    list.add(new int[]{position[0]+1, position[1]+1});
                }
            }
        }
        else { //white piece
            //can move forward one
            if (position[0]-1 >=0 && Board.getTile(position[0]-1, position[1]).getPiece() == null) {
                list.add(new int[]{position[0]-1,position[1]});
            }
            //can double step
            if (position[0] == 6) {
                if (Board.getTile(position[0]-2, position[1]).getPiece() == null) {
                    list.add(new int[]{position[0]-2, position[1]});
                }
            }
            //check if can eat left
            if (position[0]-1 >=0 && position[1]-1 >= 0) {
                if (Board.getTile(position[0]-1, position[1]-1).getPiece() != null && Board.getTile(position[0]-1, position[1]-1).getPiece().color() == 1) {
                    list.add(new int[]{position[0]-1, position[1]-1});
                }
            }
            //check if can eat right
            if (position[0]-1 >=0 && position[1]+1 <= 7) {
                if (Board.getTile(position[0]-1, position[1]+1).getPiece() != null && Board.getTile(position[0]-1, position[1]+1).getPiece().color() == 1) {
                    list.add(new int[]{position[0]-1, position[1]+1});
                }
            }
        }
        
        return list;
    }
    
    @Override
    public void setPosition(int i, int j) {
        position[0] = i;
        position[1] = j;
    }
    
    public void setPassant(boolean passant){
        enpassant = passant;
    }
    
    public boolean enPassant(){
        return enpassant;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getColor(){
        if (color == WHITE)
            return "white";
        else
            return "black";
    }
    
    @Override
    public int color(){
        return color;
    }

    @Override
    public int[] position() {
        return position;
    }

    @Override
    public int getAte() {
        return ate;
    }
    
    @Override
    public void setAte(int val) {
        ate = val;
    }
}
