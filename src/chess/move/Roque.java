package chess.move;

import chess.Position;
import chess.piece.Piece;

public class Roque {

    private boolean kingMoved;
    private boolean rookMoved;

    public Roque(boolean kingMoved, boolean rookMoved){
        this.kingMoved = false;
        this.rookMoved = false;
    }

    public boolean kingMoved() {
        return this.kingMoved;
    }

    public boolean rookMoved() {
        return this.rookMoved;
    }

}
