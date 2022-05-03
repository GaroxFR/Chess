package chess.move.component;

import chess.Board;
import chess.piece.Piece;

/**
 * Représente la partie capture d'un coup
 */
public class Capture implements MoveComponent{

    private final Piece capturedPiece;

    public Capture(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    @Override
    public void apply(Board board) {
        this.capturedPiece.setAlive(false);
        board.setPiece(this.capturedPiece.getPosition(), null);
    }

    @Override
    public void revert(Board board) {
        this.capturedPiece.setAlive(true);
        board.setPiece(this.capturedPiece.getPosition(), this.capturedPiece);
    }

    public Piece getCapturedPiece() {
        return this.capturedPiece;
    }
}
