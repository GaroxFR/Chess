package chess.move;

import chess.move.component.EnPassantPossibleCapture;

public class PreMoveState {
    private final EnPassantPossibleCapture enPassantPossibleCapture;
    private final boolean hadPieceMove;
    private final long positionHash;

    public PreMoveState(EnPassantPossibleCapture enPassantPossibleCapture, boolean hadPieceMove, long positionHash) {
        this.enPassantPossibleCapture = enPassantPossibleCapture;
        this.hadPieceMove = hadPieceMove;
        this.positionHash = positionHash;
    }

    public EnPassantPossibleCapture getEnPassantPossibleCapture() {
        return this.enPassantPossibleCapture;
    }

    public boolean hadPieceMove() {
        return this.hadPieceMove;
    }

    public long getPositionHash() {
        return this.positionHash;
    }
}
