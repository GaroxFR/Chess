package chess.move.component;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

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
