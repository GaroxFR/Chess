package chess.move;

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

    public boolean isHadPieceMove() {
        return this.hadPieceMove;
    }
}
