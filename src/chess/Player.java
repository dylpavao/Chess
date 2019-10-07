package chess;

import chess.pieces.*;

/**
 *
 * @author Dylan
 */
public class Player {
    
    private final int color;
    private boolean choosing;
    private boolean check;
    private Piece selected;
    private final String name;
    
    public Player(int color){
        this.color = color;
        name = (color == Chess.WHITE) ? "White" : "Black";
        choosing = false;
        check = false;
        selected = null;
    }
    
    public void select(Piece p){
        selected = p;
    }
    
    public void isChoosing(boolean c){
        choosing = c;
    }
    
    public void check(boolean c){
        check = c;
    }
    
    public boolean inCheck(){
        return check;
    }
    
    public boolean choosing(){
        return choosing;
    }
    
    public Piece selected(){
        return selected;
    }
    
    public int getColor(){
        return color;
    }
    
    public String name(){
        return name;
    }
}
