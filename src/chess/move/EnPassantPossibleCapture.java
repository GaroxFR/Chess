package chess.move;

import chess.Position;
import chess.piece.Piece;

public class EnPassantPossibleCapture {

    private final Position capturePosition;
    private final Piece capturedPiece;

    public EnPassantPossibleCapture(Position capturePosition, Piece capturePiece) {
        this.capturePosition = capturePosition;
        this.capturedPiece = capturePiece;
    }

    public Position getCapturePosition() {
        return this.capturePosition;
    }

    public Piece getCapturedPiece() {
        return this.capturedPiece;
    }
}
