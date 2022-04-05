package chess;

import chess.ihm.panels.PromotionPanel;
import chess.move.Move;
import chess.piece.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Optional;

public class BoardPanel extends JPanel implements MouseListener, KeyListener, MouseMotionListener {

    public static BoardPanel INSTANCE;

    // DEBUG OPTIONS
    private boolean showThreats = false;
    private boolean showPins = false;
    private boolean showChecks = false;

    private boolean whiteTop = false;

    private Image fond;
    private Image fondReverse;
    private Board board;

    public BoardPanel(Board plateau){
        BoardPanel.INSTANCE = this;
        this.board = plateau;
        this.fond        = Toolkit.getDefaultToolkit().getImage("res/board.png");
        this.fondReverse = Toolkit.getDefaultToolkit().getImage("res/board_reverse.png");
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//méthode paint qui herite de la classe mere
        g.drawImage(this.whiteTop ? this.fondReverse : this.fond,0,0, this);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = this.board.getPiece(x, y);
                if (piece != null && (this.board.getSelectedPiece() == null || this.board.getSelectedPiece() != piece)) {
                   g.drawImage(piece.getImage(), this.getScreenX(x), this.getScreenY(y), 64, 64, null);
                }
            }
        }

        g.setColor(new Color(59, 92, 22));
        for (Move move : this.board.getSelectedPieceMoves()) {
            g.fillOval(this.getScreenX(move.getEndPosition().getX()) + 23, this.getScreenY(move.getEndPosition().getY()) + 23, 18, 18);
        }

        Piece piece = this.board.getSelectedPiece();
        Point point = this.getMousePosition();
        if (piece != null && point != null) {
            g.drawImage(piece.getImage(), point.x - 32, point.y - 32, 64, 64, null);
        }

        if (this.showThreats) {
            g.setColor(new Color(220, 55, 55, 100));
            for (Position position : this.board.getThreatenedPositions()) {
                g.fillRect(this.getScreenX(position.getX()), this.getScreenY(position.getY())+1, 64, 64);
            }
        }

        if (this.showPins) {
            g.setColor(new Color(55, 113, 220, 100));
            this.board.getPins()
                    .stream()
                    .flatMap(piecePin -> piecePin.getPossiblePositions().stream())
                    .forEach(position -> g.fillRect(this.getScreenX(position.getX()), this.getScreenY(position.getY())+1, 64, 64));
        }

        if (this.showChecks) {
            g.setColor(new Color(250, 99, 180, 148));
            this.board.getCheckSources()
                    .stream()
                    .flatMap(source -> source.getResolvingPositions().stream())
                    .forEach(position -> g.fillRect(this.getScreenX(position.getX()), this.getScreenY(position.getY())+1, 64, 64));
        }
    }

    //redessine l'IHM pour le mettre à jour
    public void update() {
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = this.getBoardX(e.getX());
        int y = this.getBoardY(e.getY());
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
            this.board.onPressed(x, y);
        }
    }

    public int getScreenX(int boardX) {
        return boardX * 64;
    }

    public int getScreenY(int boardY) {
        if (this.whiteTop) {
            return boardY * 64;
        } else {
            return (7 - boardY) * 64;
        }
    }

    public int getBoardX(int screenX) {
        return screenX / 64;
    }

    public int getBoardY(int screenY) {
        if (this.whiteTop) {
            return screenY / 64;
        } else {
            return 7 - (screenY / 64);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = this.getBoardX(e.getX());
        int y = this.getBoardY(e.getY());
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
            Optional<PromotionPanel> promotionPanelOptional = this.board.onRelease(x, y);
            promotionPanelOptional.ifPresent(panel -> {
                int promotionX = this.getScreenX(x);
                int promotionY = this.getScreenY(y);
                if (promotionY + 256 > 512) {
                    promotionY -= 192;
                }
                panel.setPosition(promotionX, promotionY);
                this.add(panel);
            });
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'p' -> this.showPins = !this.showPins;
            case 'm', 't' -> this.showThreats = !this.showThreats;
            case 'c' -> this.showChecks = !this.showChecks;
            case 'f' -> this.whiteTop = !this.whiteTop;
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> board.goBackHistory();
            case KeyEvent.VK_RIGHT -> board.goForwardHistory();
        }
    }
}
