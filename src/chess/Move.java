package chess;

import chess.piece.Piece;

import java.util.Objects;

public class Move {

    private final Position startPosition;
    private final Position endPosition;
    private final Piece piece;
    private final Piece capturedPiece;

    public Move(Position startPosition, Position endPosition, Piece piece) {
        this(startPosition, endPosition, piece, null);
    }

    public Move(Position startPosition, Position endPosition, Piece piece, Piece capturedPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.piece = piece;
        this.capturedPiece = capturedPiece;
    }

    public Position getStartPosition() {
        return this.startPosition;
    }

    public Position getEndPosition() {
        return this.endPosition;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public boolean isCapture() {
        return this.capturedPiece != null;
    }

    public Piece getCapturedPiece() {
        return this.capturedPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Move move = (Move) o;
        return this.startPosition.equals(move.startPosition) && this.endPosition.equals(move.endPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startPosition, this.endPosition);
    }
}
