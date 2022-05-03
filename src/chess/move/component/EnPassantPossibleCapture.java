package chess.move.component;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

/**
 * Cette classe représente la possibilité de prise d'un pion en passant.
 * Elle est générée à chaque fois qu'un pion avance de 2 cases et est aussi utilisé dans le calcul des coups
 * au tour suivant.
 */
public class EnPassantPossibleCapture implements MoveComponent {

    private final Position capturePosition;
    private final Piece capturedPiece;

    public EnPassantPossibleCapture(Position capturePosition, Piece capturePiece) {
        this.capturePosition = capturePosition;
        this.capturedPiece = capturePiece;
    }

    @Override
    public void apply(Board board) {
        board.setEnPassantPossibleCapture(this);
    }

    @Override
    public void revert(Board board) {

    }

    public Position getCapturePosition() {
        return this.capturePosition;
    }

    public Piece getCapturedPiece() {
        return this.capturedPiece;
    }
}
