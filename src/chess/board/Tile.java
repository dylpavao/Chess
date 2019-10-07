package chess.board;

import chess.Chess;
import chess.Player;
import chess.gui.GameFrame;
import chess.pieces.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author Dylan
 */
public class Tile extends JLabel implements MouseListener{
   
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    private Piece piece;
    private final Color color;
    private boolean selected;
    private final int[] position;
    
    public Tile(int color, int i, int j){
        selected = false;
        piece = null;
        position = new int[]{i,j};
        if (color == WHITE){
            this.color = new Color(250,233,201);
        }
        else{
            this.color = new Color(150,84,24);
        }
        setup();
    }
    
    private void setup(){
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setBackground(color);
        setSize(GameFrame.WIDTH/8,GameFrame.HEIGHT/8);
        setOpaque(true);
        addMouseListener(this);
    }
    
    //Adds the piece to this tile
    public void addPiece(Piece piece){
        this.piece = piece;
        piece.setPosition(position[0],position[1]);
        setIcon(new ImageIcon("images/pieces/"+piece.getColor()+"/"+piece.getName()+".png"));
    }
    
    //Removes the piece for this tile
    public void removePiece(){
        piece = null;
        setIcon(null);
    }
    
    /* Adds the piece to tile without displaying on it on board.
        Used when determining if move clears the king from check
    */
    public void fakeAddPiece(Piece piece){
        this.piece = piece;
        piece.setPosition(position[0],position[1]);
    }
    
    /* Removes the piece from tile without displaying on it on board.
        Used when determining if move clears the king from check
    */
    public void fakeRemovePiece(){
        piece = null;
    }
    
    //Check to see if move is valid and determine what kind of move it is
    private void checkMove(Player p){
        Piece pce = p.selected();
        Tile tile = Board.getTile(pce.position()[0],pce.position()[1]);
        //Make the king move out of check if in check
        if(p.inCheck() && MoveChecker.escapedCheck(pce,position,false)){
            movePiece(p,tile,pce);
            p.check(false);
            Chess.clearCheck();
        }
        //Check for castle
        else if(!p.inCheck() && pce.getName().equals("king") && piece != null 
                && piece.getName().equals("rook") 
                && pce.color() == piece.color()){
            King king = (King)pce;
            Rook rook = (Rook)piece;
            if(!(king.hasMoved() || rook.hasMoved())){
                //Check which rook is castling
                if(rook.position()[1] == 0){
                    if(p.getColor() == WHITE){
                        if(MoveChecker.validCastle(0,WHITE)){
                            performCastle(king,rook);
                            p.isChoosing(false);
                        }
                    }
                    else{
                        if(MoveChecker.validCastle(0,BLACK)){
                            performCastle(king,rook);
                            p.isChoosing(false);
                        }
                    }
                }
                else if(rook.position()[1] == 7){
                    if(p.getColor() == WHITE){
                        if(MoveChecker.validCastle(7,WHITE)){
                            performCastle(king,rook);
                            p.isChoosing(false);
                        }
                    }
                    else{
                        if(MoveChecker.validCastle(7,BLACK)){
                            performCastle(king,rook);
                            p.isChoosing(false);
                        }
                    }
                }
            }
            Chess.board.clearSelection();
        }
        //Regular move
        else if(!p.inCheck() && MoveChecker.validMove(pce,position)){
            movePiece(p,tile,pce);
        }
        //Invalid move
        else Chess.board.clearSelection();
    }
    
    //Execute the castling move, move both king and rook
    private void performCastle(King k, Rook r){
        int row = (k.color()==Chess.WHITE)? 7 : 0 ;
        int[] cols = new int[3];
        if(r.position()[1] == 0)
            cols = new int[]{0,2,3};
        else if (r.position()[1] == 7)
            cols = new int[]{7,6,5};
        Tile tile = Board.getTile(k.position()[0],k.position()[1]);
        r.moved();
        k.moved();
        tile.removePiece();
        tile.clearSelect();
        Board.getTile(row,cols[0]).removePiece();
        Board.getTile(row,cols[1]).addPiece(k);
        Board.getTile(row,cols[2]).addPiece(r);
        
    }
    private void movePieceAI(Tile tile, Piece pce){
        tile.removePiece();
        tile.clearSelect();
        addPiece(pce);
        if(pce.getName().equals("rook") || pce.getName().equals("king")){
            switch(pce.getName()){
                case "rook":
                    Rook r = (Rook)pce;
                    if(!r.hasMoved())r.moved();
                    break;
                case "king":
                    King k = (King)pce;
                    if(!k.hasMoved())k.moved();
                    break;
            }
        }
    }
    
    //Moves the piece to the desired location
    private void movePiece(Player p, Tile tile, Piece pce){
        tile.removePiece();
        tile.clearSelect();
        addPiece(pce);
        if(pce.getName().equals("rook") || pce.getName().equals("king")){
            switch(pce.getName()){
                case "rook":
                    Rook r = (Rook)pce;
                    if(!r.hasMoved())r.moved();
                    break;
                case "king":
                    King k = (King)pce;
                    if(!k.hasMoved())k.moved();
                    break;
            }
        }
        p.isChoosing(false);
    }
    
    //Selects this tile and adds blue border
    public void select(){
        Border border = BorderFactory.createLineBorder(new Color(89,91,212));
        setBorder(BorderFactory.createCompoundBorder(border, border));
        selected = true;
    }
    
    //Clears this tile from selection
    public void clearSelect(){
        selected = false;
        setBorder(null);
    }
    
    public Piece getPiece(){
        return piece;
    }
    
    public boolean isSelected(){
        return selected;
    }
    
    //Determines whether a piece is being selected or a move is being made
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == 1){
            if(Chess.turn == WHITE){
                //Move being made
                if(Chess.board.movePending()){
                    checkMove(Chess.p1);
                }
                //Piece being selected
                else if(piece!=null && piece.color()==WHITE) {
                    select();
                    Chess.p1.select(piece);
                }
            }
//            else{
//               if(Chess.board.movePending()){
//                   checkMove(Chess.p2);
//                } 
//                else if(piece!=null && piece.color()==BLACK) {
//                    select();
//                    Chess.p2.select(piece);
//                } 
//            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

}
