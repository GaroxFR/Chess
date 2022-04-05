package chess.move;

import chess.piece.Piece;

public class Promotion {

    private final Piece pawn;
    private final Piece piece;

    public Promotion(Piece pawn, Piece piece) {
        this.pawn = pawn;
        this.piece = piece;
    }

    public Piece getPawn() {
        return this.pawn;
    }

    public Piece getPiece() {
        return this.piece;
    }

    @Override
    public int hashCode() {
        return this.piece.getClass().getName().hashCode();
    }
}
