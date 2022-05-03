package chess.move.component;

import chess.Board;
import chess.piece.Piece;

/**
 * Représente la partie Promotion d'un coup
 */
public class Promotion implements MoveComponent {

    private final Piece pawn;
    private final Piece piece;

    public Promotion(Piece pawn, Piece piece) {
        this.pawn = pawn;
        this.piece = piece;
    }

    @Override
    public void apply(Board board) {

    }

    @Override
    public void applyPostMove(Board board) {
        board.setPiece(this.pawn.getPosition(), this.piece);
    }

    @Override
    public void revertPostMove(Board board) {
        board.setPiece(this.piece.getPosition(), this.pawn);
    }

    @Override
    public void revert(Board board) {

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
