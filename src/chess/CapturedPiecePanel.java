package chess;

import chess.piece.*;
import chess.player.Team;

import javax.swing.*;
import java.awt.*;

public class CapturedPiecePanel extends JPanel {


    private Team team;
    private Board board;

    public CapturedPiecePanel(Team team, Board plateau){
        this.team = team;
        this.board = plateau;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Piece captured : this.board.getCapturedPiece() ) {
            if (captured.getTeam() != this.team) {
                continue;
            }

            if(captured instanceof Pawn){
                int i = 32;
                int j = 32;
                g.drawImage(captured.getImage(), i, j, 16, 16, null);
                i = i + 32;
                if(i == 224){ //4 pions max par ligne
                    i=0;
                    j=j+64;
                }
            }
            if(captured instanceof Knight){
                int i= 32;
                int j=160;
                g.drawImage(captured.getImage(), i, j, 16, 16, null);
                i = i + 64;
            }
            if(captured instanceof Bishop){
                int i= 32;
                int j=224;
                g.drawImage(captured.getImage(), i, j, 16, 16, null);
                i = i + 64;
            }
            if(captured instanceof Rook){
                int i= 32;
                int j=288;
                g.drawImage(captured.getImage(), i, j, 16, 16, null);
                i = i + 64;
            }
            if(captured instanceof Queen){
                int i= 32;
                int j=352;
                g.drawImage(captured.getImage(), i, j, 16, 16, null);
                i = i + 64;
            }
        }
    }
}
