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
public class Knight implements Piece{
    
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    private final int color;
    private final String name;
    private final int[] position;
    private int ate;
    
    public Knight(int color){
         this.color = color;
         name = "knight";
         position = new int[2];
         ate = 0;
    }
    
    @Override
    public ArrayList<int[]> getAllPossibleMoves() {

        ArrayList<int[]> list = new ArrayList<>();
        //bottom left and right
        if(position[0]-2 >= 0){
            if(position[1]-1 >= 0) {
                if (Board.getTile(position[0]-2, position[1]-1).getPiece() == null) list.add(new int[]{position[0]-2,position[1]-1});
                else {
                  if (Board.getTile(position[0]-2, position[1]-1).getPiece().color() != color) list.add(new int[]{position[0]-2,position[1]-1});
                }
            }
            if(position[1]+1 < 8) {
                if (Board.getTile(position[0]-2, position[1]+1).getPiece() == null) list.add(new int[]{position[0]-2,position[1]+1});
                else {
                  if (Board.getTile(position[0]-2, position[1]+1).getPiece().color() != color) list.add(new int[]{position[0]-2,position[1]+1});
                }
            }
        }
        //left and right bottom
        if(position[0]-1 >= 0){
            if(position[1]-2 >= 0) {
                if (Board.getTile(position[0]-1, position[1]-2).getPiece() == null) list.add(new int[]{position[0]-1,position[1]-2});
                else {
                  if (Board.getTile(position[0]-1, position[1]-2).getPiece().color() != color) list.add(new int[]{position[0]-1,position[1]-2});
                }
            }
            if(position[1]+2 < 8) {
                if (Board.getTile(position[0]-1, position[1]+2).getPiece() == null) list.add(new int[]{position[0]-1,position[1]+2});
                else {
                  if (Board.getTile(position[0]-1, position[1]+2).getPiece().color() != color) list.add(new int[]{position[0]-1,position[1]+2});
                }
            }
        }
        //top left and right
        if(position[0]+2 < 8){
            if(position[1]-1 >= 0) {
                if (Board.getTile(position[0]+2, position[1]-1).getPiece() == null) list.add(new int[]{position[0]+2,position[1]-1});
                else {
                  if (Board.getTile(position[0]+2, position[1]-1).getPiece().color() != color) list.add(new int[]{position[0]+2,position[1]-1});
                }
            }
            if(position[1]+1 < 8) {
                if (Board.getTile(position[0]+2, position[1]+1).getPiece() == null) list.add(new int[]{position[0]+2,position[1]+1});
                else {
                  if (Board.getTile(position[0]+2, position[1]+1).getPiece().color() != color) list.add(new int[]{position[0]+2,position[1]+1});
                }
            }
        }
        //left and right top
        if(position[0]+1 < 8){
            if(position[1]-2 >= 0) {
                if (Board.getTile(position[0]+1, position[1]-2).getPiece() == null) list.add(new int[]{position[0]+1,position[1]-2});
                else {
                  if (Board.getTile(position[0]+1, position[1]-2).getPiece().color() != color) list.add(new int[]{position[0]+1,position[1]-2});
                }
            }
            if(position[1]+2 < 8) {
                if (Board.getTile(position[0]+1, position[1]+2).getPiece() == null) list.add(new int[]{position[0]+1,position[1]+2});
                else {
                  if (Board.getTile(position[0]+1, position[1]+2).getPiece().color() != color) list.add(new int[]{position[0]+1,position[1]+2});
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

