package chess;

import chess.move.Move;
import chess.piece.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardPanel extends JPanel implements MouseListener {

    public static BoardPanel INSTANCE;

    private Image fond;
    private Board board;

    public BoardPanel(Board plateau){
        BoardPanel.INSTANCE = this;
        this.board = plateau;
        this.fond = Toolkit.getDefaultToolkit().getImage("res/board.png");
        this.setPreferredSize(new Dimension(530, 530));
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//méthode paint qui herite de la classe mere
        g.drawImage(this.fond,0,0, this);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = this.board.getPiece(x, 7-y);
                if (piece != null) {
                   g.drawImage(piece.getImage(), x*64, y*64, null);
                }
            }
        }

        g.setColor(Color.GREEN);
        for (Move move : this.board.getSelectedPieceMoves()) {
            g.fillOval(move.getEndPosition().getX() * 64 + 20, (7 - move.getEndPosition().getY()) * 64 + 20, 24, 24);
        }
    }

    //redessine l'IHM pour le mettre à jour
    public void update() {
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / 64;
        int y = 7 - (e.getY() / 64);
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
            this.board.onClick(x, y);
            this.repaint();
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
