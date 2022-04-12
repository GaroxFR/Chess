package chess.move;

import chess.move.component.EnPassantPossibleCapture;

public class PreMoveState {
    private final EnPassantPossibleCapture enPassantPossibleCapture;
    private final boolean hadPieceMove;

    public PreMoveState(EnPassantPossibleCapture enPassantPossibleCapture, boolean hadPieceMove) {
        this.enPassantPossibleCapture = enPassantPossibleCapture;
        this.hadPieceMove = hadPieceMove;
    }

    public EnPassantPossibleCapture getEnPassantPossibleCapture() {
        return this.enPassantPossibleCapture;
    }

    public boolean hadPieceMove() {
        return this.hadPieceMove;
    }
}
