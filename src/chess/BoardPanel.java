package chess;

import chess.move.Move;
import chess.piece.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardPanel extends JPanel implements MouseListener, KeyListener {

    public static BoardPanel INSTANCE;

    // DEBUG OPTIONS
    private boolean showThreats = false;
    private boolean showPins = false;
    private boolean showChecks = false;

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

        if (this.showThreats) {
            g.setColor(new Color(220, 55, 55, 100));
            for (Position position : this.board.getThreatenedPositions()) {
                g.fillRect(position.getX()*64, (7-position.getY())*64, 64, 64);
            }
        }

        if (this.showPins) {
            g.setColor(new Color(55, 113, 220, 100));
            this.board.getPins()
                    .stream()
                    .flatMap(piecePin -> piecePin.getPossiblePositions().stream())
                    .forEach(position -> g.fillRect(position.getX()*64, (7-position.getY())*64, 64, 64));
        }

        if (this.showChecks) {
            g.setColor(new Color(250, 99, 180, 148));
            this.board.getCheckSources()
                    .stream()
                    .flatMap(source -> source.getResolvingPositions().stream())
                    .forEach(position -> g.fillRect(position.getX()*64, (7-position.getY())*64, 64, 64));
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

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyChar());
        switch (e.getKeyChar()) {
            case 'p' -> this.showPins = !this.showPins;
            case 'm', 't' -> this.showThreats = !this.showThreats;
            case 'c' -> this.showChecks = !this.showChecks;
        }
        this.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
