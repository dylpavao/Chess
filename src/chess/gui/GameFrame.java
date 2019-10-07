package chess.gui;

import chess.Chess;
import chess.board.Board;
import chess.board.Tile;
import chess.pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.swing.*;

/**
 *
 * @author Dylan
 */
public class GameFrame extends JFrame{
    
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private final JPanel pane;
    private final Board board;
    private static JMenuBar menu;
    private static JMenuItem check; 
    
    public GameFrame(Board board){
        this.board = board;
        pane = new JPanel(new GridLayout(8,8));
        setUpBoard();
        setUpFrame();
    }
    
    private void setUpFrame(){
        ImageIcon img = new ImageIcon("images/icon.png");
        setIconImage(img.getImage());
        setTitle("Chess");
        setUpMenu();
        setContentPane(pane);
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    private void setUpMenu(){
        menu = new JMenuBar();
        final JMenu options = new JMenu("Options");
        final JMenuItem newGame = new JMenuItem("New Game");
        final JMenuItem save = new JMenuItem("Save Game");
        final JMenuItem load = new JMenuItem("Load Game");
        check = new JMenuItem("Check: ");
        newGame.addActionListener((ActionEvent e) -> {
            int response = JOptionPane.showConfirmDialog(null, 
                    "Your current game progress will be lost.", 
                    "Start a new game?", JOptionPane.YES_NO_OPTION);
            if(response == 0)
                Chess.resetGame();
        });
        save.addActionListener((ActionEvent e) -> {
            PrintWriter writer;
            try {
                writer = new PrintWriter("savedgame.txt", "UTF-8");
                saveGame(writer);
            } catch (FileNotFoundException ex) {
                System.out.println("FileNotFoundException");
            } catch (UnsupportedEncodingException ex) {
                System.out.println("UnsupportedEncodingException");
            }
        });
        load.addActionListener((ActionEvent e) -> {
            try {
                FileReader reader = new FileReader("savedgame.txt");
                BufferedReader buffer = new BufferedReader(reader);
                loadGame(buffer);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "No saved game found...", 
                "Load Game", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        options.add(newGame);
        options.add(save);
        options.add(load);
        menu.add(options);
        menu.add(check);
        setJMenuBar(menu);
    }
    
    private void loadGame(BufferedReader reader){
        Board.clearBoard();
        String data;
        String[] pieceData;
        Piece piece;
        Tile tile;
        try{
            if(reader.readLine().equals("Check: White")){
                Chess.p1.check(true);
                setCheck("White");
            }
            while((data = reader.readLine()) != null){
                pieceData = data.split("-");
                piece = createPiece(pieceData);
                int r = Integer.parseInt(pieceData[2]);
                int c = Integer.parseInt(pieceData[3]);
                tile = Board.getTile(r,c);
                tile.addPiece(piece);
            }
        }catch(IOException e){
            System.out.println("IOException while reading from file: "+e);
        }
        try {
            reader.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        JOptionPane.showMessageDialog(null, "Game state loaded successfully!", 
                "Load Game", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Piece createPiece(String[] data){
        Piece piece = new Pawn(0);
        switch(data[0]){
            case "rook":
                Rook rook = new Rook(Integer.parseInt(data[1]));
                if(Integer.parseInt(data[4]) == 1)rook.moved();
                piece = rook;
                break;
            case "knight":
                piece = new Knight(Integer.parseInt(data[1]));
                break;
            case "bishop":
                piece = new Bishop(Integer.parseInt(data[1]));
                break;
            case "queen":
                piece = new Queen(Integer.parseInt(data[1]));
                break;
            case "king":
                King king = new King(Integer.parseInt(data[1]));
                if(Integer.parseInt(data[4]) == 1)king.moved();
                piece = king;
                break;
            case "pawn":
                Pawn pawn = new Pawn(Integer.parseInt(data[1]));
                if(Integer.parseInt(data[4]) == 1)pawn.setPassant(true);
                piece = pawn;
                break;
        }
        return piece;
    }
    
    private void saveGame(PrintWriter writer){
        int r, c, color;
        String name;
        int condition;
        Piece p;
        writer.println(check.getText());
        for(int i=0; i<Board.ROWS; i++){
            for(int j=0; j<Board.COLS; j++){
                if(Board.getTile(i, j).getPiece() != null){
                    p = Board.getTile(i,j).getPiece();
                    r = p.position()[0];
                    c = p.position()[1];
                    color = p.color();
                    name = p.getName();
                    condition = 0;
                    switch(name){
                        case "rook":
                            Rook rook = (Rook)p;
                            if(rook.hasMoved())condition = 1;
                            break;
                        case "king":
                            King king = (King)p;
                            if(king.hasMoved())condition = 1;
                            break;
                        case "pawn":
                            Pawn pawn = (Pawn)p;
                            if(pawn.enPassant())condition = 1; 
                            break;
                    }
                    writer.println(name+"-"+color+"-"+r+"-"+c+"-"+condition);
                }
            }
        }
        writer.close();
        JOptionPane.showMessageDialog(null, "Game state saved successfully!", 
                "Save Game", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setUpBoard(){
        for(int i=0; i<Board.ROWS; i++){
            for(int j=0; j<Board.COLS; j++){
                pane.add(board.getTile(i,j));
            }
        }
    }
    
    public void setCheck(String player){
        if(!player.equals("Black")){
            menu.remove(check);
            check = new JMenuItem("Check: "+player);
            menu.add(check);
            revalidate();
            repaint();
        }
    }
}
