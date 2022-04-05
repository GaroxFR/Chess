package chess.ihm.panels;

import chess.move.Move;
import chess.piece.Bishop;
import chess.piece.Knight;
import chess.piece.Queen;
import chess.piece.Rook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PromotionPanel extends JPanel implements MouseListener {

    private final Map<Class<?>, Move> promotionMoveMap = new HashMap<>();
    private final Consumer<Move> moveConsumer;

    public PromotionPanel(List<Move> promotionMoves, Consumer<Move> moveConsumer) {
        for (Move promotionMove : promotionMoves) {
            if (promotionMove.getPromotion() == null) {
                continue;
            }

            this.promotionMoveMap.put(promotionMove.getPromotion().getPiece().getClass(), promotionMove);
        }
        this.moveConsumer = moveConsumer;

        this.addMouseListener(this);
    }

    public void setPosition(int x, int y) {
        this.setBounds(x, y, 64, 256);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(77, 71, 61));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(new Color(237, 217, 179));
        g.fillRect(5, 5, this.getWidth()-10, this.getHeight()-10);

        Point point = this.getMousePosition();
        if (point != null) {
            g.setColor(new Color(186, 170, 140));
            g.fillRect(5, (int) (point.getY() / 64) * 64, 54, 64);
        }

        g.drawImage(this.promotionMoveMap.get(Queen.class).getPromotion().getPiece().getImage(), 0, 0, 64, 64, null);
        g.drawImage(this.promotionMoveMap.get(Rook.class).getPromotion().getPiece().getImage(), 0, 64, 64, 64, null);
        g.drawImage(this.promotionMoveMap.get(Bishop.class).getPromotion().getPiece().getImage(), 0, 128, 64, 64, null);
        g.drawImage(this.promotionMoveMap.get(Knight.class).getPromotion().getPiece().getImage(), 0, 192, 64, 64, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int i = e.getY() / 64;
        Move move = switch (i) {
            case 1 -> this.promotionMoveMap.get(Rook.class);
            case 2 -> this.promotionMoveMap.get(Bishop.class);
            case 3 -> this.promotionMoveMap.get(Knight.class);
            default -> this.promotionMoveMap.get(Queen.class);
        };

        this.moveConsumer.accept(move);
        this.getParent().remove(this);

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