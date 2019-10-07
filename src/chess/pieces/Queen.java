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
public class Queen implements Piece{
    
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    private final int color;
    private final String name;
    private final int[] position;
    private int ate;
    
    public Queen(int color){
         this.color = color;
         name = "queen";
         position = new int[2];
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
        //top left
        i=1;
        while(position[0]-i >= 0 && position[1]-i >= 0){
            if(Board.getTile(position[0]-i,position[1]-i).getPiece() != null) {
                if (Board.getTile(position[0]-i,position[1]-i).getPiece().color() == color) break;
                else {
                    list.add(new int[]{position[0]-i,position[1]-i});
                    break;
                }
            }
            list.add(new int[]{position[0]-i,position[1]-i});
            i++;
        }
        //top right
        i=1;
        while(position[0]-i >= 0 && position[1]+i < Board.COLS){
            if(Board.getTile(position[0]-i,position[1]+i).getPiece() != null) {
                if (Board.getTile(position[0]-i,position[1]+i).getPiece().color() == color) break;
                else {
                    list.add(new int[]{position[0]-i,position[1]+i});
                    break;
                }
            }
            list.add(new int[]{position[0]-i,position[1]+i});
            i++;
        }
        //bottom left
        i=1;
        while(position[0]+i < Board.ROWS && position[1]-i >= 0){
            if(Board.getTile(position[0]+i,position[1]-i).getPiece() != null) {
                if (Board.getTile(position[0]+i,position[1]-i).getPiece().color() == color) break;
                else {
                    list.add(new int[]{position[0]+i,position[1]-i});
                    break;
                }
            }
            list.add(new int[]{position[0]+i,position[1]-i});
            i++;                    
        }
        //bottom right
        i=1;
        while(position[0]+i < Board.ROWS && position[1]+i < Board.COLS){
            if(Board.getTile(position[0]+i,position[1]+i).getPiece() != null) {
                if (Board.getTile(position[0]+i,position[1]+i).getPiece().color() == color) break;
                else {
                    list.add(new int[]{position[0]+i,position[1]+i});
                    break;
                }
            }
            list.add(new int[]{position[0]+i,position[1]+i});
            i++;                    
        }
        
        return list;
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


