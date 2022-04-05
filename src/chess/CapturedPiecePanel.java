package chess;

import chess.piece.*;
import chess.player.Team;

import javax.swing.*;
import java.awt.*;

public class CapturedPiecePanel extends JPanel {


    private Team team;
    private Board board;
    private Image fond;

    public CapturedPiecePanel(Team team, Board plateau){
        this.team = team;
        this.board = plateau;
        this.fond = Toolkit.getDefaultToolkit().getImage("res/fond3.png");
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.fond,0,0,128,490, this);

        int iPawn = 24;
        int jPawn = 16;
        int iKnight = 24;
        int jKnight = 64;
        int iBishop = 24;
        int jBishop = 112;
        int iRook = 24;
        int jRook = 160;
        int iQueen = 24;
        int jQueen = 208;

        for (Piece captured : this.board.getCapturedPiece() ) {
            if (captured.getTeam() != this.team) {
                continue;
            }

            if(captured instanceof Pawn){
                g.drawImage(captured.getImage(), iPawn, jPawn, 16, 16, null);
                iPawn = iPawn + 16;
                if(iPawn > 72){ //4 pions max par ligne
                    iPawn=24;
                    jPawn=jPawn+16;
                }
            }
            if(captured instanceof Knight){
                g.drawImage(captured.getImage(), iKnight, jKnight, 16, 16, null);
                iKnight = iKnight + 16;
                if(iKnight > 56){ //4 pions max par ligne
                    iKnight=24;
                    jKnight=jKnight+16;
                }
            }
            if(captured instanceof Bishop){
                g.drawImage(captured.getImage(), iBishop, jBishop, 16, 16, null);
                iBishop = iBishop + 16;
                if(iBishop > 56){ //4 pions max par ligne au cas ou pour la promotion
                    iBishop=24;
                    jBishop=jBishop+16;
                }
            }
            if(captured instanceof Rook){
                g.drawImage(captured.getImage(), iRook, jRook, 16, 16, null);
                iRook = iRook + 16;
                if(iRook > 56){ //4 pions max par ligne
                    iRook=24;
                    jBishop=jBishop+16;
                }
            }
            if(captured instanceof Queen){
                g.drawImage(captured.getImage(), iQueen, jQueen, 16, 16, null);
                iQueen = iQueen + 16;
                if(iQueen > 56){ //4 pions max par ligne
                    iQueen=24;
                    jQueen=jQueen+16;
                }
            }
        }
    }
}
