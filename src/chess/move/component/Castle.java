package chess.move.component;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

/**
 * Représente la partie Roque d'un coup.
 * Le déplacement du roi étant géré par le coup lui-même, ce composant s'assure que la tour soit aussi déplacée.
 */
public class Castle implements MoveComponent {
    private final Piece rook;
    private final Position oldRookPosition;
    private final Position newRookPosition;

    public Castle(Piece rook, Position oldRookPosition, Position newRookPosition) {
        this.rook = rook;
        this.oldRookPosition = oldRookPosition;
        this.newRookPosition = newRookPosition;
    }

    @Override
    public void apply(Board board) {
        board.setPiece(this.newRookPosition, this.rook);
        board.setPiece(this.oldRookPosition, null);
    }

    @Override
    public void revert(Board board) {
        board.setPiece(this.oldRookPosition, this.rook);
        board.setPiece(this.newRookPosition, null);
    }

    public Piece getRook() {
        return this.rook;
    }

    public Position getOldRookPosition() {
        return this.oldRookPosition;
    }

    public Position getNewRookPosition() {
        return this.newRookPosition;
    }
}
