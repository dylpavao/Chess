package chess.board;

import chess.pieces.Piece;
import chess.Chess;
import chess.pieces.Pawn;
import java.util.ArrayList;

/**
 *
 * @author Dylan
 */
public class MoveChecker {
    
    private static int[] pos;    //position of selected piece 
    private static int[] dest;   //destination of the piece
    private static String pName; //name of piece
    private static int color;    //color of piece
    private static Tile destTile;//tile that piece wants to move to
    private static Piece piece;  //selected piece
    public static ArrayList<int[]> checkZone = null;
    
    public static boolean validMove(Piece p, int[] d){
        pos = p.position();
        dest = d;
        pName = p.getName();
        color = p.color();
        destTile = Board.getTile(dest[0],dest[1]);
        piece = p;
        //Check if move puts king in check   
        if(moveExposesKing())return false; 
        //Check for peices in the way
        if(conflictions())return false;
        //Check if move is valid based on piece mechanics
        switch(pName){
            case "pawn":
                if(dest[1]==pos[1]){
                    if(color==Chess.WHITE){
                        if(pos[0]==6 && dest[0]==4){
                            Pawn pawn = (Pawn)p;
                            pawn.setPassant(true);
                            return true;
                        }
                        if(dest[0]==pos[0]-1)return true;
                    }
                    else{
                        if(pos[0]==1 && dest[0]==3){
                            Pawn pawn = (Pawn)p;
                            pawn.setPassant(true);
                            return true;
                        }
                        if(dest[0]==pos[0]+1)return true;
                    }
                }
                else if(pos[1]-dest[1] == -1 || pos[1]-dest[1] == 1){
                    if(color == Chess.WHITE &&  pos[0]-dest[0] == 1){
                        if(destTile.getPiece() != null)return true;
                        else if(Board.getTile(dest[0]+1,dest[1]).getPiece() != null){
                            if(Board.getTile(dest[0]+1,dest[1]).getPiece().getName().equals("pawn")){
                                Pawn pawn = (Pawn)Board.getTile(dest[0]+1,dest[1]).getPiece();
                                if(pawn.enPassant()){
                                    Board.getTile(dest[0]+1,dest[1]).removePiece();
                                    return true;
                                }
                            }
                        }
                    }
                    else if(color == Chess.BLACK && pos[0]-dest[0] == -1){
                        if(destTile.getPiece() != null)return true;
                        else if(Board.getTile(dest[0]-1,dest[1]).getPiece() != null){
                            if(Board.getTile(dest[0]-1,dest[1]).getPiece().getName().equals("pawn")){
                                Pawn pawn = (Pawn)Board.getTile(dest[0]-1,dest[1]).getPiece();
                                if(pawn.enPassant()){
                                    Board.getTile(dest[0]-1,dest[1]).removePiece();
                                    return true;
                                }
                            }
                        }
                    }
                }
                break;
            case "rook":
                //Check if moving horizontal or vertical
                if(!(pos[0] == dest[0] && pos[1] == dest[1])){
                    if(pos[0]==dest[0] || pos[1]==dest[1]){
                       return true;
                    }
                }
                break;
            case "knight":
                //Generate possible positions
                ArrayList<int[]> list  = new ArrayList<>();
                if(pos[0]-2 >= 0){
                    if(pos[1]-1 >= 0)
                        list.add(new int[]{pos[0]-2,pos[1]-1});
                    if(pos[1]+1 < 8)
                        list.add(new int[]{pos[0]-2,pos[1]+1});
                }
                if(pos[0]-1 >= 0){
                    if(pos[1]-2 >= 0)
                        list.add(new int[]{pos[0]-1,pos[1]-2});
                    if(pos[1]+2 < 8)
                        list.add(new int[]{pos[0]-1,pos[1]+2});
                }
                if(pos[0]+2 < 8){
                    if(pos[1]-1 >= 0)
                        list.add(new int[]{pos[0]+2,pos[1]-1});
                    if(pos[1]+1 < 8)
                        list.add(new int[]{pos[0]+2,pos[1]+1});
                }
                if(pos[0]+1 < 8){
                    if(pos[1]-2 >= 0)
                        list.add(new int[]{pos[0]+1,pos[1]-2});
                    if(pos[1]+2 < 8)
                        list.add(new int[]{pos[0]+1,pos[1]+2});
                }
                //Check if destination matches valid position
                for(int[] ps : list){
                    if(ps[0] == dest[0] && ps[1] == dest[1]){
                        if(!(destTile.getPiece() != null && 
                                destTile.getPiece().color() == piece.color())){
                            return true;
                        }
                    }
                }
                break;
            case "bishop":
                list  = new ArrayList<>();
                //top left
                int i=1;
                while(pos[0]-i >= 0 && pos[1]-i >= 0){
                    list.add(new int[]{pos[0]-i,pos[1]-i});
                    if(Board.getTile(pos[0]-i,pos[1]-i).getPiece() != null)
                        break;
                    i++;
                }
                //top right
                i=1;
                while(pos[0]-i >= 0 && pos[1]+i < Board.COLS){
                    list.add(new int[]{pos[0]-i,pos[1]+i});
                    if(Board.getTile(pos[0]-i,pos[1]+i).getPiece() != null)
                        break;
                    i++;
                }
                //bottom left
                i=1;
                while(pos[0]+i < Board.ROWS && pos[1]-i >= 0){
                    list.add(new int[]{pos[0]+i,pos[1]-i});
                    if(Board.getTile(pos[0]+i,pos[1]-i).getPiece() != null)
                        break;
                    i++;                    
                }
                //bottom right
                i=1;
                while(pos[0]+i < Board.ROWS && pos[1]+i < Board.COLS){
                    list.add(new int[]{pos[0]+i,pos[1]+i});
                    if(Board.getTile(pos[0]+i,pos[1]+i).getPiece() != null)
                        break;
                    i++;                    
                }
                //Check if destination matches valid position
                for(int[] ps : list){
                    if(ps[0] == dest[0] && ps[1] == dest[1]){
                        if(!(destTile.getPiece() != null && 
                                destTile.getPiece().color() == piece.color())){
                            return true;
                        }
                    }
                }
                break;
            case "queen":
                list  = new ArrayList<>();
                //top left
                i=1;
                while(pos[0]-i >= 0 && pos[1]-i >= 0){
                    list.add(new int[]{pos[0]-i,pos[1]-i});
                    if(Board.getTile(pos[0]-i,pos[1]-i).getPiece() != null)
                        break;
                    i++;
                }
                //top right
                i=1;
                while(pos[0]-i >= 0 && pos[1]+i < Board.COLS){
                    list.add(new int[]{pos[0]-i,pos[1]+i});
                    if(Board.getTile(pos[0]-i,pos[1]+i).getPiece() != null)
                        break;
                    i++;
                }
                //bottom left
                i=1;
                while(pos[0]+i < Board.ROWS && pos[1]-i >= 0){
                    list.add(new int[]{pos[0]+i,pos[1]-i});
                    if(Board.getTile(pos[0]+i,pos[1]-i).getPiece() != null)
                        break;
                    i++;                    
                }
                //bottom right
                i=1;
                while(pos[0]+i < Board.ROWS && pos[1]+i < Board.COLS){
                    list.add(new int[]{pos[0]+i,pos[1]+i});
                    if(Board.getTile(pos[0]+i,pos[1]+i).getPiece() != null)
                        break;
                    i++;                    
                }
                //up
                i=1;
                while(pos[0]-i >= 0){
                    list.add(new int[]{pos[0]-i,pos[1]});
                    if(Board.getTile(pos[0]-i,pos[1]).getPiece() != null)
                        break;
                    i++;
                }
                //down
                i=1;
                while(pos[0]+i < Board.ROWS){
                    list.add(new int[]{pos[0]+i,pos[1]});
                    if(Board.getTile(pos[0]+i,pos[1]).getPiece() != null)
                        break;
                    i++;
                }
                //left
                i=1;
                while(pos[1]-i >= 0){
                    list.add(new int[]{pos[0],pos[1]-i});
                    if(Board.getTile(pos[0],pos[1]-i).getPiece() != null)
                        break;
                    i++;
                }
                //right
                i=1;
                while(pos[1]+i < Board.COLS){
                    list.add(new int[]{pos[0],pos[1]+i});
                    if(Board.getTile(pos[0],pos[1]+i).getPiece() != null)
                        break;
                    i++;
                }
                //Check if destination matches valid position
                for(int[] ps : list){
                    if(ps[0] == dest[0] && ps[1] == dest[1]){
                        if(!(destTile.getPiece() != null && 
                                destTile.getPiece().color() == piece.color())){
                            return true;
                        }
                    }
                }
                break;
            case "king":
                fillCheckZone(color);
                list  = new ArrayList<>();
                if(pos[0]-1 >= 0){
                    list.add(new int[]{pos[0]-1,pos[1]});
                    if(pos[1]-1 >= 0)
                        list.add(new int[]{pos[0]-1,pos[1]-1});
                    if(pos[1]+1 < 8){
                        list.add(new int[]{pos[0]-1,pos[1]+1});
                    }
                }
                if(pos[0]+1 < 8){
                    list.add(new int[]{pos[0]+1,pos[1]});
                    if(pos[1]-1 >= 0)
                        list.add(new int[]{pos[0]+1,pos[1]-1});
                    if(pos[1]+1 < 8){
                        list.add(new int[]{pos[0]+1,pos[1]+1});
                    }
                }
                if(pos[1]-1 >= 0){
                    list.add(new int[]{pos[0],pos[1]-1});
                }
                if(pos[1]+1 < 8){
                    list.add(new int[]{pos[0],pos[1]+1});
                }
                //See if dest is in "checkZone"
                for(int[] cz : checkZone){
                    if(cz[0] == dest[0] && cz[1] == dest[1])return false;
                }
                //Check if destination matches valid position
                for(int[] ps : list){
                    if(ps[0] == dest[0] && ps[1] == dest[1]){
                        if(!(destTile.getPiece() != null && 
                                destTile.getPiece().color() == piece.color())){
                            return true;
                        }
                    }
                }
                break;
                
        }
        return false;
    }
    
    //Check if the castling attempt is valid
    public static boolean validCastle(int column, int color){
        //Left castle
        if(column == 0){
            if(color == Chess.WHITE){
                if(Board.getTile(7,1).getPiece() == null
                        && Board.getTile(7,2).getPiece() == null
                        && Board.getTile(7,3).getPiece() == null){
                    fillCheckZone(Chess.WHITE);
                    for(int[] cz : checkZone){
                        if(cz[0]==7 && (cz[1]==1 || cz[1]==2 || cz[1]==3)){
                            return false;
                        }
                    }
                    return true;
                }
            }
            else{
                if(Board.getTile(0,1).getPiece() == null
                        && Board.getTile(0,2).getPiece() == null
                        && Board.getTile(0,3).getPiece() == null){
                    fillCheckZone(Chess.BLACK);
                    for(int[] cz : checkZone){
                        if(cz[0]==0 && (cz[1]==1 || cz[1]==2 || cz[1]==3)){
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        //Right castle
        else if(column == 7){
            if(color == Chess.WHITE){
                 if(Board.getTile(7,6).getPiece() == null
                        && Board.getTile(7,5).getPiece() == null){
                    fillCheckZone(Chess.WHITE);
                    for(int[] cz : checkZone){
                        if(cz[0]==7 && (cz[1]==6 || cz[1]==5)){
                            return false;
                        }
                    }
                    return true;
                }
            }
            else{
                if(Board.getTile(0,6).getPiece() == null
                        && Board.getTile(0,5).getPiece() == null){
                    fillCheckZone(Chess.BLACK);
                    for(int[] cz : checkZone){
                        if(cz[0]==0 && (cz[1]==6 || cz[1]==5)){
                            return false;
                        }
                    }
                    return true;
                }
            }

        }
        return false;
    }
    
    //Determine if the attempted move removes the king from check
    public static boolean escapedCheck(Piece p, int[] d, boolean valid){
        boolean escaped = true;
        if(valid || validMove(p,d)){
            if(p.getName().equals("king")){
                fillCheckZone(p.color());
                for(int[] cz : checkZone){
                    if(cz[0] == d[0] && cz[1] == d[1]){
                        escaped = false;
                        break;
                    }
                }
                return escaped;
            }
            else {
                Piece temp = null;
                int[] kp = Board.kingPosition(p.color());
                Tile pt = Board.getTile(p.position()[0],p.position()[1]);
                Tile dt = Board.getTile(d[0],d[1]);
                pt.fakeRemovePiece();
                if(dt.getPiece() != null)temp = dt.getPiece();
                dt.fakeAddPiece(p);
                fillCheckZone(p.color());
                for(int[] cz : checkZone){
                    if(kp[0]==cz[0] && kp[1]==cz[1]){
                        escaped = false;
                        break;
                    }
                }
                dt.fakeRemovePiece();
                if(temp != null)dt.addPiece(temp);
                pt.fakeAddPiece(p);
                return escaped;
            }
        }
        return false;
    }
    
    //See if any of the pieces can break the check based on their possible moves
    public static int checkBreakers(int color){
        int checkBreak = 0;
        for(int m=0; m<Board.ROWS; m++){
            for (int j=0; j<Board.COLS; j++) {
                Piece p = Board.getTile(m,j).getPiece();
                if (p != null && p.color() == color) {
                    int[] pos = p.position();
                    int[] dest;
                    Tile t;
                    switch (p.getName()) {
                        case "pawn":
                            if(color == Chess.BLACK){
                                //If in first rank check double move
                                if(pos[0]==1){
                                    t = Board.getTile(pos[0]+2,pos[1]);
                                    dest = new int[]{pos[0]+2,pos[1]};
                                    if(t.getPiece() == null
                                       && Board.getTile(pos[0]+1,pos[1]).getPiece() == null)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                //Add black pawn attackzones plus regular move
                                if(pos[0]+1 < Board.ROWS){
                                    t = Board.getTile(pos[0]+1, pos[1]);
                                    dest = new int[]{pos[0]+1,pos[1]};
                                    if(t.getPiece() == null)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                    if(pos[1]-1 >= 0){
                                        t = Board.getTile(pos[0]+1,pos[1]-1);
                                        dest = new int[]{pos[0]+1,pos[1]-1};
                                        if(t.getPiece() != null && t.getPiece().color() != color)
                                            if(escapedCheck(p,dest,true))checkBreak++;
                                    }
                                    if(pos[1]+1 < Board.COLS){
                                        t = Board.getTile(pos[0]+1,pos[1]+1);
                                        dest = new int[]{pos[0]+1,pos[1]+1};
                                        if(t.getPiece() != null && t.getPiece().color() != color)
                                            if(escapedCheck(p,dest,true))checkBreak++;
                                    }
                                }
                            }
                            else{
                                //If in first rank check double move
                                if(pos[0]==6){
                                    t = Board.getTile(pos[0]-2,pos[1]);
                                    dest = new int[]{pos[0]-2,pos[1]};
                                    if(t.getPiece() == null 
                                       && Board.getTile(pos[0]-1,pos[1]).getPiece() == null)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                //Add white pawn attackzones plus regular move
                                if(pos[0]-1 >= 0){
                                    t = Board.getTile(pos[0]-1,pos[1]);
                                    dest = new int[]{pos[0]-1,pos[1]};
                                    if(t.getPiece() == null)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                    if(pos[1]-1 >= 0){
                                        t = Board.getTile(pos[0]-1, pos[1]-1);
                                        dest = new int[]{pos[0]-1,pos[1]-1};
                                        if(t.getPiece() != null && t.getPiece().color() != color)
                                            if(escapedCheck(p,dest,true))checkBreak++;
                                    }
                                    if(pos[1]+1 < Board.COLS){
                                        t = Board.getTile(pos[0]-1, pos[1]+1);
                                        dest = new int[]{pos[0]-1,pos[1]+1};
                                        if(t.getPiece() != null && t.getPiece().color() != color)
                                            if(escapedCheck(p,dest,true))checkBreak++;
                                    }
                                }
                            }   
                            break;
                        case "rook":
                            //Add top attack zones
                            for(int i=pos[0]-1; i>=0; i--){
                                t = Board.getTile(i, pos[1]);
                                dest = new int[]{i,pos[1]};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                            }   //Add bottom attack zones
                            for(int i=pos[0]+1; i<Board.ROWS; i++){
                                t = Board.getTile(i, pos[1]);
                                dest = new int[]{i,pos[1]};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                            }   //Add left attack zones
                            for(int i=pos[1]-1; i>=0; i--){
                                t = Board.getTile(pos[0], i);
                                dest = new int[]{pos[0], i};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                            }   //Add right attack zones
                            for(int i=pos[1]+1; i<Board.COLS; i++){
                                t = Board.getTile(pos[0], i);
                                dest = new int[]{pos[0], i};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                            } 
                            break;
                        case "knight":
                            if(pos[0]-2 >= 0){
                                if(pos[1]-1 >= 0){
                                    t = Board.getTile(pos[0]-2,pos[1]-1);
                                    dest = new int[]{pos[0]-2,pos[1]-1};
                                    if(t.getPiece() == null || t.getPiece().color() != color)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                if(pos[1]+1 < 8){
                                    t = Board.getTile(pos[0]-2,pos[1]+1);
                                    dest = new int[]{pos[0]-2,pos[1]+1};
                                    if(t.getPiece() == null || t.getPiece().color() != color)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                }
                            }
                            if(pos[0]-1 >= 0){
                                if(pos[1]-2 >= 0){
                                    t = Board.getTile(pos[0]-1,pos[1]-2);
                                    dest = new int[]{pos[0]-1,pos[1]-2};
                                    if(t.getPiece() == null || t.getPiece().color() != color)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                if(pos[1]+2 < 8){
                                    t = Board.getTile(pos[0]-1,pos[1]+2);
                                    dest = new int[]{pos[0]-1,pos[1]+2};
                                    if(t.getPiece() == null || t.getPiece().color() != color)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                }
                            }
                            if(pos[0]+2 < 8){
                                if(pos[1]-1 >= 0){
                                    t = Board.getTile(pos[0]+2,pos[1]-1);
                                    dest = new int[]{pos[0]+2,pos[1]-1};
                                    if(t.getPiece() == null || t.getPiece().color() != color)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                if(pos[1]+1 < 8){
                                    t = Board.getTile(pos[0]+2,pos[1]+1);
                                    dest = new int[]{pos[0]+2,pos[1]+1};
                                    if(t.getPiece() == null || t.getPiece().color() != color)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                }
                            }
                            if(pos[0]+1 < 8){
                                if(pos[1]-2 >= 0){
                                    t = Board.getTile(pos[0]+1,pos[1]-2);
                                    dest = new int[]{pos[0]+1,pos[1]-2};
                                    if(t.getPiece() == null || t.getPiece().color() != color)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                   
                                }
                                if(pos[1]+2 < 8){
                                    t = Board.getTile(pos[0]+1,pos[1]+2);
                                    dest = new int[]{pos[0]+1,pos[1]+2};
                                    if(t.getPiece() == null || t.getPiece().color() != color)
                                        if(escapedCheck(p,dest,true))checkBreak++;
                                }
                            }
                            break;
                        case "bishop":
                            int k=1;
                            while(pos[0]-k >= 0 && pos[1]-k >= 0){
                                t = Board.getTile(pos[0]-k,pos[1]-k);
                                dest = new int[]{pos[0]-k,pos[1]-k};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                                k++;
                            }
                            //Add top right attack zones
                            k=1;
                            while(pos[0]-k >= 0 && pos[1]+k < Board.COLS){
                                t = Board.getTile(pos[0]-k,pos[1]+k);
                                dest = new int[]{pos[0]-k,pos[1]+k};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                                k++;
                            }
                            //Add bottom left attack zones
                            k=1;
                            while(pos[0]+k < Board.ROWS && pos[1]-k >= 0){
                                t = Board.getTile(pos[0]+k,pos[1]-k);
                                dest = new int[]{pos[0]+k,pos[1]-k};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                                k++;
                            }
                            //Add bottom right attack zones
                            k=1;
                            while(pos[0]+k < Board.ROWS && pos[1]+k < Board.COLS){
                                t = Board.getTile(pos[0]+k,pos[1]+k);
                                dest = new int[]{pos[0]+k,pos[1]+k};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                                k++;
                            }
                            break;
                        case "queen":
                            //Add top attack zones
                            for(int i=pos[0]-1; i>=0; i--){
                                t = Board.getTile(i, pos[1]);
                                dest = new int[]{i,pos[1]};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                            }   //Add bottom attack zones
                            for(int i=pos[0]+1; i<Board.ROWS; i++){
                                t = Board.getTile(i, pos[1]);
                                dest = new int[]{i,pos[1]};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                            }   //Add left attack zones
                            for(int i=pos[1]-1; i>=0; i--){
                                t = Board.getTile(pos[0], i);
                                dest = new int[]{pos[0], i};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                            }   //Add right attack zones
                            for(int i=pos[1]+1; i<Board.COLS; i++){
                                t = Board.getTile(pos[0], i);
                                dest = new int[]{pos[0], i};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                            }
                            k=1;
                            while(pos[0]-k >= 0 && pos[1]-k >= 0){
                                t = Board.getTile(pos[0]-k,pos[1]-k);
                                dest = new int[]{pos[0]-k,pos[1]-k};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                                k++;
                            }
                            //Add top right attack zones
                            k=1;
                            while(pos[0]-k >= 0 && pos[1]+k < Board.COLS){
                                t = Board.getTile(pos[0]-k,pos[1]+k);
                                dest = new int[]{pos[0]-k,pos[1]+k};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                                k++;
                            }
                            //Add bottom left attack zones
                            k=1;
                            while(pos[0]+k < Board.ROWS && pos[1]-k >= 0){
                                t = Board.getTile(pos[0]+k,pos[1]-k);
                                dest = new int[]{pos[0]+k,pos[1]-k};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                                k++;
                            }
                            //Add bottom right attack zones
                            k=1;
                            while(pos[0]+k < Board.ROWS && pos[1]+k < Board.COLS){
                                t = Board.getTile(pos[0]+k,pos[1]+k);
                                dest = new int[]{pos[0]+k,pos[1]+k};
                                if(t.getPiece() == null){
                                    if(escapedCheck(p,dest,true))checkBreak++;
                                }
                                else if(t.getPiece().color() != color){
                                    if(escapedCheck(p,dest,true)){
                                        checkBreak++;
                                        break;
                                    }
                                }
                                else break;
                                k++;
                            }
                            break;
                    }
                }
            }
        }
        return checkBreak;
    }
    
    //Returns the number of possible moves for this color
    public static int numberOfPossibleMoves(int color){
        int numMoves = 0;
        for(int m=0; m<Board.ROWS; m++){
            for (int j=0; j<Board.COLS; j++) {
                Piece p = Board.getTile(m,j).getPiece();
                if (p != null && p.color() == color) {
                    int[] pos = p.position();
                    Tile t;
                    switch (p.getName()) {
                        case "pawn":
                            if(color == Chess.BLACK){
                                //If in first rank check double move
                                if(pos[0]==1){
                                    t = Board.getTile(pos[0]+2,pos[1]);
                                    if(t.getPiece() == null 
                                            && Board.getTile(pos[0]+1,pos[1]).getPiece() == null)
                                        numMoves++;
                                }
                                //Add black pawn attackzones plus regular move
                                if(pos[0]+1 < Board.ROWS){
                                    t = Board.getTile(pos[0]+1, pos[1]);
                                    if(t.getPiece() == null)numMoves++;
                                    if(pos[1]-1 >= 0){
                                        t = Board.getTile(pos[0]+1,pos[1]-1);
                                        if(t.getPiece() != null && t.getPiece().color() != color)
                                            numMoves++;
                                    }
                                    if(pos[1]+1 < Board.COLS){
                                        t = Board.getTile(pos[0]+1,pos[1]+1);
                                        if(t.getPiece() != null && t.getPiece().color() != color)
                                            numMoves++;
                                    }
                                }
                            }
                            else{
                                //If in first rank check double move
                                if(pos[0]==6){
                                    t = Board.getTile(pos[0]-2,pos[1]);
                                    if((t.getPiece() == null)
                                       && Board.getTile(pos[0]-1,pos[1]).getPiece() == null)
                                        numMoves++;
                                }
                                //Add white pawn attackzones plus regular move
                                if(pos[0]-1 >= 0){
                                    t = Board.getTile(pos[0]-1,pos[1]);
                                    if(t.getPiece() == null)numMoves++;
                                    if(pos[1]-1 >= 0){
                                        t = Board.getTile(pos[0]-1, pos[1]-1);
                                        if(t.getPiece() != null && t.getPiece().color() != color)
                                            numMoves++;
                                    }
                                    if(pos[1]+1 < Board.COLS){
                                        t = Board.getTile(pos[0]-1, pos[1]+1);                                        
                                        if(t.getPiece() != null && t.getPiece().color() != color)
                                            numMoves++;
                                    }
                                }
                            }   
                            break;
                        case "rook":
                            //Add top attack zones
                            for(int i=pos[0]-1; i>=0; i--){
                                t = Board.getTile(i, pos[1]);
                                if(t.getPiece() == null)numMoves++;                                    
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                            }   //Add bottom attack zones
                            for(int i=pos[0]+1; i<Board.ROWS; i++){
                                t = Board.getTile(i, pos[1]);
                                if(t.getPiece() == null)numMoves++;                                    
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                            }   //Add left attack zones
                            for(int i=pos[1]-1; i>=0; i--){
                                t = Board.getTile(pos[0], i);
                                if(t.getPiece() == null)numMoves++;                                    
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                            }   //Add right attack zones
                            for(int i=pos[1]+1; i<Board.COLS; i++){
                                t = Board.getTile(pos[0], i);
                                if(t.getPiece() == null)numMoves++;                                    
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                            } 
                            break;
                        case "knight":
                            if(pos[0]-2 >= 0){
                                if(pos[1]-1 >= 0){
                                    t = Board.getTile(pos[0]-2,pos[1]-1);
                                    if(t.getPiece() == null 
                                            || t.getPiece().color() != color)
                                        numMoves++;
                                }
                                if(pos[1]+1 < 8){
                                    t = Board.getTile(pos[0]-2,pos[1]+1);                                    
                                    if(t.getPiece() == null 
                                            || t.getPiece().color() != color)
                                        numMoves++;
                                }
                            }
                            if(pos[0]-1 >= 0){
                                if(pos[1]-2 >= 0){
                                    t = Board.getTile(pos[0]-1,pos[1]-2);                                    
                                    if(t.getPiece() == null 
                                            || t.getPiece().color() != color)
                                        numMoves++;
                                }
                                if(pos[1]+2 < 8){
                                    t = Board.getTile(pos[0]-1,pos[1]+2);                                    
                                    if(t.getPiece() == null 
                                            || t.getPiece().color() != color)
                                        numMoves++;
                                }
                            }
                            if(pos[0]+2 < 8){
                                if(pos[1]-1 >= 0){
                                    t = Board.getTile(pos[0]+2,pos[1]-1);                                    
                                    if(t.getPiece() == null 
                                            || t.getPiece().color() != color)
                                        numMoves++;
                                }
                                if(pos[1]+1 < 8){
                                    t = Board.getTile(pos[0]+2,pos[1]+1);                                    
                                    if(t.getPiece() == null 
                                            || t.getPiece().color() != color)
                                        numMoves++;
                                }
                            }
                            if(pos[0]+1 < 8){
                                if(pos[1]-2 >= 0){
                                    t = Board.getTile(pos[0]+1,pos[1]-2);                                    
                                    if(t.getPiece() == null 
                                            || t.getPiece().color() != color)
                                        numMoves++;
                                }
                                if(pos[1]+2 < 8){
                                    t = Board.getTile(pos[0]+1,pos[1]+2);                                    
                                    if(t.getPiece() == null 
                                            || t.getPiece().color() != color)
                                        numMoves++;
                                }
                            }
                            break;
                        case "bishop":
                            int k=1;
                            while(pos[0]-k >= 0 && pos[1]-k >= 0){
                                t = Board.getTile(pos[0]-k,pos[1]-k);                                
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                                k++;
                            }
                            //Add top right attack zones
                            k=1;
                            while(pos[0]-k >= 0 && pos[1]+k < Board.COLS){
                                t = Board.getTile(pos[0]-k,pos[1]+k);
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                                k++;
                            }
                            //Add bottom left attack zones
                            k=1;
                            while(pos[0]+k < Board.ROWS && pos[1]-k >= 0){
                                t = Board.getTile(pos[0]+k,pos[1]-k);
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                k++;
                            }
                            //Add bottom right attack zones
                            k=1;
                            while(pos[0]+k < Board.ROWS && pos[1]+k < Board.COLS){
                                t = Board.getTile(pos[0]+k,pos[1]+k);
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                                k++;
                            }
                            break;
                        case "queen":
                            //Add top attack zones
                            for(int i=pos[0]-1; i>=0; i--){
                                t = Board.getTile(i, pos[1]);
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                            }   //Add bottom attack zones
                            for(int i=pos[0]+1; i<Board.ROWS; i++){
                                t = Board.getTile(i, pos[1]);
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                            }   //Add left attack zones
                            for(int i=pos[1]-1; i>=0; i--){
                                t = Board.getTile(pos[0], i);
                                dest = new int[]{pos[0], i};
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                            }   //Add right attack zones
                            for(int i=pos[1]+1; i<Board.COLS; i++){
                                t = Board.getTile(pos[0], i);
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                            }
                            k=1;
                            while(pos[0]-k >= 0 && pos[1]-k >= 0){
                                t = Board.getTile(pos[0]-k,pos[1]-k);
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                                k++;
                            }
                            //Add top right attack zones
                            k=1;
                            while(pos[0]-k >= 0 && pos[1]+k < Board.COLS){
                                t = Board.getTile(pos[0]-k,pos[1]+k);
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                                k++;
                            }
                            //Add bottom left attack zones
                            k=1;
                            while(pos[0]+k < Board.ROWS && pos[1]-k >= 0){
                                t = Board.getTile(pos[0]+k,pos[1]-k);
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                                k++;
                            }
                            //Add bottom right attack zones
                            k=1;
                            while(pos[0]+k < Board.ROWS && pos[1]+k < Board.COLS){
                                t = Board.getTile(pos[0]+k,pos[1]+k);
                                dest = new int[]{pos[0]+k,pos[1]+k};
                                if(t.getPiece() == null)numMoves++;
                                else if(t.getPiece().color() != color){
                                    numMoves++;
                                    break;
                                }
                                else break;
                                k++;
                            }
                            break;
                        case "king":
                            ArrayList<int[]> kingMoves = getKingMoves(color);
                            int moves = kingMoves.size()-1;
                            fillCheckZone(color);
                            for(int[] km : kingMoves){
                                for(int[] cz : checkZone){
                                    if(km[0]==cz[0] && km[1]==cz[1]){
                                        moves--;
                                        break;
                                    }
                                }
                            }
                            numMoves += moves;
                            break;
                    }
                }
            }
        }
        return numMoves;
    }
    
    //Determine if the attempted move puts the king in check
    private static boolean moveExposesKing(){
        boolean exposed = false;
        Piece temp = null;
        int[] kp = Board.kingPosition(piece.color());
        Tile pt = Board.getTile(pos[0],pos[1]);
        pt.fakeRemovePiece();
        if(destTile.getPiece() != null)temp = destTile.getPiece();
        destTile.fakeAddPiece(piece);
        fillCheckZone(piece.color());
        for(int[] cz : checkZone){
            if(kp[0]==cz[0] && kp[1]==cz[1]){
                exposed = true;
                break;
            }
        }
        destTile.fakeRemovePiece();
        if(temp != null)destTile.addPiece(temp);
        pt.fakeAddPiece(piece);
        return exposed;
    }
    
    //Determine if there are any team pieces in the way of the move
    private static boolean conflictions(){
        switch(pName){
            case "pawn":
                if(pos[1] != dest[1] && destTile.getPiece() != null){
                    if(destTile.getPiece().color() == color){
                        return true;
                    }
                }
                else{
                    int dist = pos[0] - dest[0];
                    if(dist < 0)dist = dist*-1;
                    for(int i=1; i<=dist; i++){
                        if(color == Chess.WHITE && pos[0]-i >= 0){
                            if(Board.getTile(pos[0]-i,pos[1]).getPiece() != null){
                                if(!enPassant(pos, Chess.BLACK))
                                    return true;
                            }
                        }
                        else if(pos[0]+i <= 7){ 
                            if(Board.getTile(pos[0]+i,pos[1]).getPiece() != null){
                                if(!enPassant(pos, Chess.WHITE))
                                    return true;
                            }
                        }
                    }
                }
                break;
            case "rook":
                //Check horizontal conflictions
                if(pos[0]==dest[0]){
                    int dist = pos[1] - dest[1];
                    //Moving right
                    if (dist < 0){
                        for(int i=pos[1]+1; i<=dest[1]; i++){
                            Piece pce = Board.getTile(pos[0],i).getPiece();
                            if(pce != null){
                                if(!(i==dest[1] && pce.color()!=piece.color())){
                                    return true;
                                }
                            }
                        }
                    }
                    //Moving left
                    else if (dist > 0){
                        for(int i=pos[1]-1; i>=dest[1]; i--){
                            Piece pce = Board.getTile(pos[0],i).getPiece();
                            if(pce != null){
                                if(!(i==dest[1] && pce.color()!=piece.color())){
                                    return true;
                                }
                            }
                        }
                    }
                }
                //Check vertical conflictions
                else if(pos[1]==dest[1]){
                    int dist = pos[0] - dest[0];
                    //Moving down
                    if (dist < 0){
                        for(int i=pos[0]+1; i<=dest[0]; i++){
                            Piece pce = Board.getTile(i,pos[1]).getPiece();
                            if(pce != null){
                                if(!(i==dest[0] && pce.color()!=piece.color())){
                                    return true;
                                }
                            }
                        }
                    }
                    //Moving up
                    else if (dist > 0){
                        for(int i=pos[0]-1; i>=dest[0]; i--){
                            Piece pce = Board.getTile(i,pos[1]).getPiece();
                            if(pce != null){
                                if(!(i==dest[0] && pce.color()!=piece.color())){
                                    return true;
                                }
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }
    
    /* Generates a list of every spot on the board where the opposing color 
      can attack this color. i.e if color == white, generate black attack zones
     * */
    public static void fillCheckZone(int color){
        checkZone = new ArrayList<>();
        for(int i=0; i<Board.ROWS; i++){
            for(int j=0; j<Board.COLS; j++){
                Piece pce = Board.getTile(i,j).getPiece();
                if(pce != null && pce.color() != color){
                    getAttackZone(pce);
                }
            }
        }
    }
    
    //Adds the spots where piece can attack to the checkZone list
    private static void getAttackZone(Piece piece){
        int[] pos = piece.position();
        int color = piece.color();
        Tile t;
        switch(piece.getName()){
            case "pawn":
                if(piece.color() == Chess.BLACK){
                    //Add black pawn attackzones
                    if(pos[0]+1 < Board.ROWS){
                        if(pos[1]-1 >= 0){
                            t = Board.getTile(pos[0]+1,pos[1]-1);
                            if(t.getPiece() == null 
                                    || t.getPiece().color()==Chess.BLACK
                                    || otherKing(t.getPiece(),Chess.BLACK))
                            checkZone.add(new int[]{pos[0]+1,pos[1]-1});
                        }
                        if(pos[1]+1 < Board.COLS){
                            t = Board.getTile(pos[0]+1,pos[1]+1);
                            if(t.getPiece() == null
                                    || t.getPiece().color()==Chess.BLACK
                                    || otherKing(t.getPiece(),Chess.BLACK))
                            checkZone.add(new int[]{pos[0]+1,pos[1]+1});
                        }
                    }
                }
                else{
                    //Add white pawn attackzones
                    if(pos[0]-1 >= 0){
                        if(pos[1]-1 >= 0){
                            t = Board.getTile(pos[0]-1, pos[1]-1);
                            if(t.getPiece() == null 
                                    || t.getPiece().color()==Chess.WHITE
                                    || otherKing(t.getPiece(),Chess.WHITE))
                                checkZone.add(new int[]{pos[0]-1,pos[1]-1});
                        }
                        if(pos[1]+1 < Board.COLS){
                            t = Board.getTile(pos[0]-1, pos[1]+1);
                            if(t.getPiece() == null 
                                    || t.getPiece().color()==Chess.WHITE
                                    || otherKing(t.getPiece(),Chess.WHITE))
                                checkZone.add(new int[]{pos[0]-1,pos[1]+1});
                        }
                    }
                }
                break;
            case "rook":
                //Add top attack zones
                for(int i=pos[0]-1; i>=0; i--){
                    t = Board.getTile(i, pos[1]);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color))
                        checkZone.add(new int[]{i,pos[1]});
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{i,pos[1]});
                        break;
                    }
                    else break;
                }
                //Add bottom attack zones
                for(int i=pos[0]+1; i<Board.ROWS; i++){
                    t = Board.getTile(i, pos[1]);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color))
                        checkZone.add(new int[]{i,pos[1]});
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{i,pos[1]});
                        break;
                    }
                    else break;
                }
                //Add left attack zones
                for(int i=pos[1]-1; i>=0; i--){
                    t = Board.getTile(pos[0], i);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color))
                        checkZone.add(new int[]{pos[0],i});
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0],i});
                        break;
                    }
                    else break;
                }
                //Add right attack zones
                for(int i=pos[1]+1; i<Board.COLS; i++){
                    t = Board.getTile(pos[0], i);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color))
                        checkZone.add(new int[]{pos[0],i});
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0],i});
                        break;
                    }
                    else break;
                }
                break;
            case "knight":
                //Add all attack zones
                if(pos[0]-2 >= 0){
                    if(pos[1]-1 >= 0){
                        t = Board.getTile(pos[0]-2,pos[1]-1);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]-2,pos[1]-1});
                    }
                    if(pos[1]+1 < 8){
                        t = Board.getTile(pos[0]-2,pos[1]+1);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]-2,pos[1]+1});
                    }
                }
                if(pos[0]-1 >= 0){
                    if(pos[1]-2 >= 0){
                        t = Board.getTile(pos[0]-1,pos[1]-2);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]-1,pos[1]-2});
                        
                    }
                    if(pos[1]+2 < 8){
                        t = Board.getTile(pos[0]-1,pos[1]+2);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]-1,pos[1]+2});
                    }
                }
                if(pos[0]+2 < 8){
                    if(pos[1]-1 >= 0){
                        t = Board.getTile(pos[0]+2,pos[1]-1);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]+2,pos[1]-1});
                    }
                    if(pos[1]+1 < 8){
                        t = Board.getTile(pos[0]+2,pos[1]+1);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]+2,pos[1]+1});
                    }
                }
                if(pos[0]+1 < 8){
                    if(pos[1]-2 >= 0){
                        t = Board.getTile(pos[0]+1,pos[1]-2);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]+1,pos[1]-2});
                    }
                    if(pos[1]+2 < 8){
                        t = Board.getTile(pos[0]+1,pos[1]+2);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]+1,pos[1]+2});
                    }
                }
                break;
            case "bishop":
                //Add top left attack zones
                int k=1;
                while(pos[0]-k >= 0 && pos[1]-k >= 0){
                    t = Board.getTile(pos[0]-k,pos[1]-k);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)){
                        checkZone.add(new int[]{pos[0]-k,pos[1]-k});
                        k++;
                    }
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0]-k,pos[1]-k});
                        break;
                    }
                    else break;
                }
                //Add top right attack zones
                k=1;
                while(pos[0]-k >= 0 && pos[1]+k < Board.COLS){
                    t = Board.getTile(pos[0]-k,pos[1]+k);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)){
                        checkZone.add(new int[]{pos[0]-k,pos[1]+k});
                        k++;
                    }
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0]-k,pos[1]+k});
                        break;
                    }
                    else break;
                }
                //Add bottom left attack zones
                k=1;
                while(pos[0]+k < Board.ROWS && pos[1]-k >= 0){
                    t = Board.getTile(pos[0]+k,pos[1]-k);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)){
                        checkZone.add(new int[]{pos[0]+k,pos[1]-k});
                        k++;
                    }
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0]+k,pos[1]-k});
                        break;
                    }
                    else break;
                }
                //Add bottom right attack zones
                k=1;
                while(pos[0]+k < Board.ROWS && pos[1]+k < Board.COLS){
                    t = Board.getTile(pos[0]+k,pos[1]+k);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)){
                        checkZone.add(new int[]{pos[0]+k,pos[1]+k});
                        k++;
                    }
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0]+k,pos[1]+k});
                        break;
                    }
                    else break;
                }
                break;
            case "queen":
                //Add top attack zones
                for(int i=pos[0]-1; i>=0; i--){
                    t = Board.getTile(i, pos[1]);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color))
                        checkZone.add(new int[]{i,pos[1]});
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{i,pos[1]});
                        break;
                    }
                    else break;
                }
                //Add bottom attack zones
                for(int i=pos[0]+1; i<Board.ROWS; i++){
                    t = Board.getTile(i, pos[1]);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color))
                        checkZone.add(new int[]{i,pos[1]});
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{i,pos[1]});
                        break;
                    }
                    else break;
                }
                //Add left attack zones
                for(int i=pos[1]-1; i>=0; i--){
                    t = Board.getTile(pos[0],i);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color))
                        checkZone.add(new int[]{pos[0],i});
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0],i});
                        break;
                    }
                    else break;
                }
                //Add right attack zones
                for(int i=pos[1]+1; i<Board.COLS; i++){
                    t = Board.getTile(pos[0],i);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color))
                        checkZone.add(new int[]{pos[0],i});
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0],i});
                        break;
                    }
                    else break;
                }
                //Add top left attack zones
                k=1;
                while(pos[0]-k >= 0 && pos[1]-k >= 0){
                    t = Board.getTile(pos[0]-k,pos[1]-k);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)){
                        checkZone.add(new int[]{pos[0]-k,pos[1]-k});
                        k++;
                    }
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0]-k,pos[1]-k});
                        break;
                    }
                    else break;
                }
                //Add top right attack zones
                k=1;
                while(pos[0]-k >= 0 && pos[1]+k < Board.COLS){
                    t = Board.getTile(pos[0]-k,pos[1]+k);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)){
                        checkZone.add(new int[]{pos[0]-k,pos[1]+k});
                        k++;
                    }
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0]-k,pos[1]+k});
                        break;
                    }
                    else break;
                }
                //Add bottom left attack zones
                k=1;
                while(pos[0]+k < Board.ROWS && pos[1]-k >= 0){
                    t = Board.getTile(pos[0]+k,pos[1]-k);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)){
                        checkZone.add(new int[]{pos[0]+k,pos[1]-k});
                        k++;
                    }
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0]+k,pos[1]-k});
                        break;
                    }
                    else break;                
                }
                //Add bottom right attack zones
                k=1;
                while(pos[0]+k < Board.ROWS && pos[1]+k < Board.COLS){
                    t = Board.getTile(pos[0]+k,pos[1]+k);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)){
                        checkZone.add(new int[]{pos[0]+k,pos[1]+k});
                        k++;
                    }
                    else if(t.getPiece().color() == color){
                        checkZone.add(new int[]{pos[0]+k,pos[1]+k});
                        break;
                    }
                    else break;                 
                }
                break;
            case "king":
                //Add all attack zones
                if(pos[0]-1 >= 0){
                    t = Board.getTile(pos[0]-1,pos[1]);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)
                            || t.getPiece().color() == color)
                        checkZone.add(new int[]{pos[0]-1,pos[1]});
                    if(pos[1]-1 >= 0){
                        t = Board.getTile(pos[0]-1,pos[1]-1);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]-1,pos[1]-1});
                    }
                    if(pos[1]+1 < 8){
                        t = Board.getTile(pos[0]-1,pos[1]+1);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]-1,pos[1]+1});
                    }
                }
                if(pos[0]+1 < 8){
                    t = Board.getTile(pos[0]+1,pos[1]);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)
                            || t.getPiece().color() == color)
                        checkZone.add(new int[]{pos[0]+1,pos[1]});
                    if(pos[1]-1 >= 0){
                        t = Board.getTile(pos[0]+1,pos[1]-1);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]+1,pos[1]-1});
                    }
                    if(pos[1]+1 < 8){
                        t = Board.getTile(pos[0]+1,pos[1]+1);
                        if(t.getPiece() == null || otherKing(t.getPiece(),color)
                                || t.getPiece().color() == color)
                            checkZone.add(new int[]{pos[0]+1,pos[1]+1});
                    }
                }
                if(pos[1]-1 >= 0){
                    t = Board.getTile(pos[0],pos[1]-1);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)
                            || t.getPiece().color() == color)
                        checkZone.add(new int[]{pos[0],pos[1]-1});
                }
                if(pos[1]+1 < 8){
                    t = Board.getTile(pos[0],pos[1]+1);
                    if(t.getPiece() == null || otherKing(t.getPiece(),color)
                            || t.getPiece().color() == color)
                        checkZone.add(new int[]{pos[0],pos[1]+1});
                }
                break;
        }
    }
    
    //Returns a list of possible moves for the king of the given color
    public static ArrayList<int[]> getKingMoves(int color){
        ArrayList<int[]> list = new ArrayList<>();
        //Find kings position
        int[] pos = new int[2];
        for(int i=0; i<Board.ROWS; i++){
            for(int j=0; j<Board.COLS; j++){
                Piece p = Board.getTile(i,j).getPiece();
                if(p != null && p.getName().equals("king") && p.color() == color){
                    pos = p.position();
                }
            }
        }
        //Add king position
        list.add(pos);
        //Add top row moves
        if(pos[0]-1 >= 0){
            if(Board.getTile(pos[0]-1,pos[1]).getPiece() == null || 
                    Board.getTile(pos[0]-1,pos[1]).getPiece().color() != color)
                    list.add(new int[]{pos[0]-1,pos[1]});
            if(pos[1]-1 >= 0)
                if(Board.getTile(pos[0]-1,pos[1]-1).getPiece() == null || 
                    Board.getTile(pos[0]-1,pos[1]-1).getPiece().color() != color)
                    list.add(new int[]{pos[0]-1,pos[1]-1});
            if(pos[1]+1 < 8){
                if(Board.getTile(pos[0]-1,pos[1]+1).getPiece() == null || 
                    Board.getTile(pos[0]-1,pos[1]+1).getPiece().color() != color)
                    list.add(new int[]{pos[0]-1,pos[1]+1});
            }
        }
        //Add bottom row moves
        if(pos[0]+1 < 8){
            if(Board.getTile(pos[0]+1,pos[1]).getPiece() == null || 
                    Board.getTile(pos[0]+1,pos[1]).getPiece().color() != color)
                    list.add(new int[]{pos[0]+1,pos[1]});
            if(pos[1]-1 >= 0)
                if(Board.getTile(pos[0]+1,pos[1]-1).getPiece() == null || 
                    Board.getTile(pos[0]+1,pos[1]-1).getPiece().color() != color)
                    list.add(new int[]{pos[0]+1,pos[1]-1});
            if(pos[1]+1 < 8){
                if(Board.getTile(pos[0]+1,pos[1]+1).getPiece() == null || 
                    Board.getTile(pos[0]+1,pos[1]+1).getPiece().color() != color)
                    list.add(new int[]{pos[0]+1,pos[1]+1});
            }
        }
        //Add left move
        if(pos[1]-1 >= 0){
            if(Board.getTile(pos[0],pos[1]-1).getPiece() == null || 
                    Board.getTile(pos[0],pos[1]-1).getPiece().color() != color)
                    list.add(new int[]{pos[0],pos[1]-1});
        }
        //Add right move
        if(pos[1]+1 < 8){
            if(Board.getTile(pos[0],pos[1]+1).getPiece() == null || 
                    Board.getTile(pos[0],pos[1]+1).getPiece().color() != color)
                    list.add(new int[]{pos[0],pos[1]+1});
        }
        return list;
    }
    
    //Determines if this piece is a king of the opposing color
    private static boolean otherKing(Piece p, int color){
        return p.getName().equals("king") && p.color() != color;
    }
    
    //Check if there are any pawns near this position that can be enPassant
    private static boolean enPassant(int[] pos, int color){
        Tile t;
        if(pos[1]-1 == dest[1]){
            t = Board.getTile(pos[0],pos[1]-1);
            if(t.getPiece() != null && t.getPiece().getName().equals("pawn")
                    && t.getPiece().color() == color){
                Pawn pawn = (Pawn)t.getPiece();
                if(pawn.enPassant())return true;
            }
        }
        else if(pos[1]+1 == dest[1]){
            t = Board.getTile(pos[0],pos[1]+1);
            if(t.getPiece() != null && t.getPiece().getName().equals("pawn")
                    && t.getPiece().color() == color){
                Pawn pawn = (Pawn)t.getPiece();
                if(pawn.enPassant())return true;
            }
        }
        return false;
    }
}
