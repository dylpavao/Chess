package chess.pieces;

import java.util.ArrayList;

public interface Piece {
    
    public void setPosition(int i, int j);
    
    public ArrayList<int[]> getAllPossibleMoves();
    
    public String getName();
    
    public String getColor();
    
    public int[] position();
    
    public int color();
    
    public int getAte();
    
    public void setAte(int val);
    
}
