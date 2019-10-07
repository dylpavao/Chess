/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.pieces;

import chess.board.Board;
import chess.board.MoveChecker;
import java.util.ArrayList;

/**
 *
 * @author Dylan
 */
public class King implements Piece{
    
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    private final int color;
    private final String name;
    private final int[] position;
    private boolean moved;
    private int ate;
    
    public King(int color){
         this.color = color;
         name = "king";
         position = new int[2];
         moved = false;
         ate = 0;
    }
    
    public boolean checkIfAttackZone(int row, int col) {
        for(int[] cz : MoveChecker.checkZone){
            if(cz[0] == row && cz[1] == col)return true;
        }
        return false;
    }
    
    @Override
    public ArrayList<int[]> getAllPossibleMoves() {

        ArrayList<int[]> list = new ArrayList<>();
        MoveChecker.fillCheckZone(color);
        //top three squares
        if(position[0]-1 >= 0){
            if (!checkIfAttackZone(position[0]-1, position[1])) {
                if (Board.getTile(position[0]-1, position[1]).getPiece() == null) {
                    list.add(new int[]{position[0]-1,position[1]});
                }
                else {
                    if (Board.getTile(position[0]-1, position[1]).getPiece().color() != color) {
                        list.add(new int[]{position[0]-1,position[1]});
                    }
                }
            }
            
            if(position[1]-1 >= 0) {
                if (!checkIfAttackZone(position[0]-1, position[1]-1)) {
                    if (Board.getTile(position[0]-1, position[1]-1).getPiece() == null) {
                            list.add(new int[]{position[0]-1,position[1]-1});
                    }
                    else {
                        if (Board.getTile(position[0]-1, position[1]-1).getPiece().color() != color) {
                            list.add(new int[]{position[0]-1,position[1]-1});
                        }
                    }
                }
            }
            if(position[1]+1 < 8) {
                if (!checkIfAttackZone(position[0]-1, position[1]+1)) {
                    if (Board.getTile(position[0]-1, position[1]+1).getPiece() == null) {
                            list.add(new int[]{position[0]-1,position[1]+1});
                    }
                    else {
                        if (Board.getTile(position[0]-1, position[1]+1).getPiece().color() != color) {
                            list.add(new int[]{position[0]-1,position[1]+1});
                        }
                    }
                }
            }
        }
        
        //bottom three squares
        if(position[0]+1 < 8){
            if (!checkIfAttackZone(position[0]+1, position[1])) {
                if (Board.getTile(position[0]+1, position[1]).getPiece() == null) {
                        list.add(new int[]{position[0]+1,position[1]});
                }
                else {
                    if (Board.getTile(position[0]+1, position[1]).getPiece().color() != color) {
                        list.add(new int[]{position[0]+1,position[1]});
                    }
                }
            }
            if(position[1]-1 >= 0) {
                if (!checkIfAttackZone(position[0]+1, position[1]-1)) {
                    if (Board.getTile(position[0]+1, position[1]-1).getPiece() == null) {
                            list.add(new int[]{position[0]+1,position[1]-1});
                    }
                    else {
                        if (Board.getTile(position[0]+1, position[1]-1).getPiece().color() != color) {
                            list.add(new int[]{position[0]+1,position[1]-1});
                        }
                    }
                }
            }
            if(position[1]+1 < 8) {
                if (!checkIfAttackZone(position[0]+1, position[1]+1)) {
                    if (Board.getTile(position[0]+1, position[1]+1).getPiece() == null) {
                            list.add(new int[]{position[0]+1,position[1]+1});
                    }
                    else {
                        if (Board.getTile(position[0]+1, position[1]+1).getPiece().color() != color) {
                            list.add(new int[]{position[0]+1,position[1]+1});
                        }
                    }
                }
            }
        }
        //left square
        if(position[1]-1 >= 0){
            if (!checkIfAttackZone(position[0], position[1]-1)) {
                if (Board.getTile(position[0], position[1]-1).getPiece() == null) {
                        list.add(new int[]{position[0],position[1]-1});
                }
                else {
                    if (Board.getTile(position[0], position[1]-1).getPiece().color() != color) {
                        list.add(new int[]{position[0],position[1]-1});
                    }
                }
            }
        }
        //right square
        if(position[1]+1 < 8){
            if (!checkIfAttackZone(position[0], position[1]+1)) {
                if (Board.getTile(position[0], position[1]+1).getPiece() == null) {
                        list.add(new int[]{position[0],position[1]+1});
                }
                else {
                    if (Board.getTile(position[0], position[1]+1).getPiece().color() != color) {
                        list.add(new int[]{position[0],position[1]+1});
                    }
                }
            }
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


