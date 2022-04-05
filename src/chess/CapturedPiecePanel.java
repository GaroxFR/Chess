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
        int iPawn = 8;
        int jPawn = 8;
        int iKnight = 8;
        int jKnight = 56;
        int iBishop = 8;
        int jBishop = 104;
        int iRook = 8;
        int jRook = 152;
        int iQueen = 8;
        int jQueen = 200;

        for (Piece captured : this.board.getCapturedPiece() ) {
            if (captured.getTeam() != this.team) {
                continue;
            }

            if(captured instanceof Pawn){
                g.drawImage(captured.getImage(), iPawn, jPawn, 16, 16, null);
                iPawn = iPawn + 16;
                if(iPawn > 56){ //4 pions max par ligne
                    iPawn=8;
                    jPawn=jPawn+16;
                }
            }
            if(captured instanceof Knight){
                g.drawImage(captured.getImage(), iKnight, jKnight, 16, 16, null);
                iKnight = iKnight + 16;
                if(iKnight > 56){ //4 pions max par ligne
                    iKnight=8;
                    jKnight=jKnight+16;
                }
            }
            if(captured instanceof Bishop){
                g.drawImage(captured.getImage(), iBishop, jBishop, 16, 16, null);
                iBishop = iBishop + 16;
                if(iBishop > 56){ //4 pions max par ligne au cas ou pour la promotion
                    iBishop=8;
                    jBishop=jBishop+16;
                }
            }
            if(captured instanceof Rook){
                g.drawImage(captured.getImage(), iRook, jRook, 16, 16, null);
                iRook = iRook + 16;
                if(iRook > 56){ //4 pions max par ligne
                    iRook=8;
                    jBishop=jBishop+16;
                }
            }
            if(captured instanceof Queen){
                g.drawImage(captured.getImage(), iQueen, jQueen, 16, 16, null);
                iQueen = iQueen + 16;
                if(iQueen > 56){ //4 pions max par ligne
                    iQueen=8;
                    jQueen=jQueen+16;
                }
            }
        }
    }
}
