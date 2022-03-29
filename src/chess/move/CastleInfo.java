package chess.move;

import chess.Position;
import chess.piece.Piece;

public class CastleInfo {
    private final Piece rook;
    private final Position oldRookPosition;
    private final Position newRookPosition;

    public CastleInfo(Piece rook, Position oldRookPosition, Position newRookPosition) {
        this.rook = rook;
        this.oldRookPosition = oldRookPosition;
        this.newRookPosition = newRookPosition;
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
