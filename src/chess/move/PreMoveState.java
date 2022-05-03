package chess.move;

import chess.move.component.EnPassantPossibleCapture;

/**
 * Afin d'annuler un coup, il est nécessaire de garder en mémoire certaines informations sur la position précédent le coup.
 * C'est exactement le rôle de cette classe.
 */
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
