/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.pieces;

import chess.board.Board;
import java.util.ArrayList;

/**
 *
 * @author Dylan
 */
public class Rook implements Piece{
    
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    private final int color;
    private final String name;
    private final int[] position;
    private boolean moved;
    private int ate;
    
    public Rook(int color){
         this.color = color;
         name = "rook";
         position = new int[2];
         moved = false;
         ate = 0;
    }
    
    @Override
    public ArrayList<int[]> getAllPossibleMoves() {

        ArrayList<int[]> list = new ArrayList<>();
        
        //left
        int i=1;
        while(position[1]-i >= 0){
            if(Board.getTile(position[0],position[1]-i).getPiece() != null) {
                if (Board.getTile(position[0],position[1]-i).getPiece().color() == color) break;
                else {
                    list.add(new int[]{position[0],position[1]-i});
                    break;
                }
            }
            list.add(new int[]{position[0],position[1]-i});
            i++;
        }
        //right
        i=1;
        while(position[1]+i < Board.COLS){
            if(Board.getTile(position[0],position[1]+i).getPiece() != null) {
                if (Board.getTile(position[0],position[1]+i).getPiece().color() == color) break;
                else {
                    list.add(new int[]{position[0],position[1]+i});
                    break;
                }
            }
            list.add(new int[]{position[0],position[1]+i});
            i++;
        }
        //south
        i=1;
        while(position[0]+i < Board.ROWS){
            if(Board.getTile(position[0]+i,position[1]).getPiece() != null) {
                if (Board.getTile(position[0]+i,position[1]).getPiece().color() == color) break;
                else {
                    list.add(new int[]{position[0]+i,position[1]});
                    break;
                }
            }
            list.add(new int[]{position[0]+i,position[1]});
            i++;                    
        }
        //north
        i=1;
        while(position[0]-i >= 0){
            if(Board.getTile(position[0]-i,position[1]).getPiece() != null) {
                if (Board.getTile(position[0]-i,position[1]).getPiece().color() == color) break;
                else {
                    list.add(new int[]{position[0]-i,position[1]});
                    break;
                }
            }
            list.add(new int[]{position[0]-i,position[1]});
            i++;                    
        }
        
        return list;
    }
    
    public void moved(){
        moved = true;
    }
    
    public boolean hasMoved(){
        return moved;
    }
    
    @Override
    public void setPosition(int i, int j) {
        position[0] = i;
        position[1] = j;
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

