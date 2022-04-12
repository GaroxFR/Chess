package chess;

import chess.piece.*;
import chess.player.Team;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class CapturedPiecePanel extends JPanel {

    private Team team;
    private Board board;
    private Image fond;
    private Image fond2;
    private JLabel timer;
    private JLabel playerName;

    public CapturedPiecePanel(Team team, Board plateau){
        this.team = team;
        this.board = plateau;
        this.fond = Toolkit.getDefaultToolkit().getImage("res/fond3.png");
        this.fond2 = Toolkit.getDefaultToolkit().getImage("res/fondtest.png");


        timer = new JLabel();
        Font police = new Font("Arial",Font.BOLD,10);
        timer.setFont(police);
        timer.setText("Temps restant :");
        timer.setBounds(2,20,128,40);

        playerName = new JLabel();
        playerName.setFont(police);
        /*
        if(team == Team.WHITE) {
            playerName.setText();
            timer.setText();
        }else(team == Team.BLACK){
            playerName.setText();
            timer.setText();
        }
         */
        playerName.setText("Player");
        playerName.setBounds(2,0,128,40);

        this.add(playerName);
        this.add(timer);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.fond,0,75,128,415, this);
        g.drawImage(this.fond2,0,0,128,75, this);

        int i = 8;
        int j = 91;
        this.board.getCapturedPiece().sort(Comparator.comparing(Piece::getValue, Comparator.naturalOrder())); //Trie dans l'ordre croissante
        for (Piece captured : this.board.getCapturedPiece() ) { //parcours liste pions capturé
            if (captured.getTeam() != this.team) {
                continue;
            }
            if (i > 80){ // pas plus de 4 pions par lignes
                j = j + 32;
                i = 8;
            }
            if(captured instanceof Pawn){
                g.drawImage(captured.getImage(), i, j, 24, 24, null);
                i = i + 24;
            }
            if(captured instanceof Knight){
                g.drawImage(captured.getImage(), i, j, 24, 24, null);
                i = i + 24;
            }
            if(captured instanceof Bishop){
                g.drawImage(captured.getImage(), i, j, 24, 24, null);
                i = i + 24;
            }
            if(captured instanceof Rook){
                g.drawImage(captured.getImage(), i, j, 24, 24, null);
                i = i+ 24;
            }
            if(captured instanceof Queen){
                g.drawImage(captured.getImage(), i, j, 24, 24, null);
                i = i + 24;
            }
            timer.setText("Temps restant : ");
        }
    }
}
